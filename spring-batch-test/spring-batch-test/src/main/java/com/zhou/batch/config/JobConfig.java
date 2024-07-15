package com.zhou.batch.config;

import static org.assertj.core.api.Assertions.entry;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.kafka.core.KafkaAdmin;

import com.zhou.batch.basic.JobBasic;
import com.zhou.batch.listener.StepStateListener;
import com.zhou.batch.utils.StringUtils;

@Configuration
public class JobConfig extends JobBasic {

	private static final Map<String, Map<String, ConcurrentHashMap<Integer, Integer>>> map = new HashMap<>();
	
	@Value("${com.zhou.file.path}")
	private String filepath;
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private KafkaProperties kafkaProperties;

	@Autowired
	private KafkaAdmin kafkaAdmin;

	@Value("${spring.kafka.template.default-topic}")
	private String topic;

	JobConfig() {
		super("simple-job");
	}

	@Bean("printJob")
	protected Job job() {
		return new JobBuilder("print-job", jobRepository).incrementer(new RunIdIncrementer()).start(step1())
				.next(step2()).build();
	}

	@Bean("insertJob")
	Job insertJob() {
		return new JobBuilder("insert-job", jobRepository).incrementer(new RunIdIncrementer()).start(step1())
				.next(fileInsertStep2()).build();
	}

	@Bean("kafkaInsertJob")
	Job kafkaInsertJob() {
		return new JobBuilder("kafka-insert-job", jobRepository).incrementer(new RunIdIncrementer()).start(step1())
				.next(kafkaInsertStep2()).build();
	}

	@Bean("paralelInsertJob")
	Job paralelInsertJob() {
		var flowFile = new FlowBuilder<Flow>("flow-file").start(fileInsertStep2()).build();
		var flowKafka = new FlowBuilder<Flow>("flow-kafka").start(kafkaInsertStep2()).split(simpleAsyncTaskExecutor())
				.add(flowFile).build();
		return new JobBuilder("paralel-insert-job", jobRepository).incrementer(new RunIdIncrementer()).start(flowKafka)
				.end().build();
	}

	@Bean("paralelCountJob")
	Job paralelCountJob() {
		var flowFile = new FlowBuilder<Flow>("flow-file").start(fileCountStep2()).build();
		var flowKafka = new FlowBuilder<Flow>("flow-kafka").start(kafkaCountStep2()).split(simpleAsyncTaskExecutor())
				.add(flowFile).build();
		var mainFlow = new FlowBuilder<Flow>("flow-main").start(countStep1()).next(flowKafka).next(countStep3())
				.build();
		return new JobBuilder("paralel-count-job", jobRepository).incrementer(new RunIdIncrementer()).start(mainFlow)
				.end().build();
	}

	@Bean("partitionJob")
	Job partitionJob() {
		return new JobBuilder("partition-job", jobRepository).incrementer(new RunIdIncrementer())
				.start(partMasterStep1()).build();
	}

	@Bean
	Step step1() {
		return new StepBuilder("step-1", jobRepository).tasklet(tasklet1(), platformTransactionManager).build();
	}

	@Bean
	Step countStep1() {
		return new StepBuilder("count-step-1", jobRepository).tasklet(tasklet2(), platformTransactionManager)
				.allowStartIfComplete(true).build();
	}

	@Bean
	Step partMasterStep1() {
		return new StepBuilder("part-master-step-1", jobRepository)
				.partitioner(partChildStep1().getName(), (gridSize) -> {
					var exMap = new HashMap<String, ExecutionContext>();
					for(int i = 0; i < gridSize; i++) {
						var ex = new ExecutionContext();
						try {
							var resource = new FileUrlResource(String.format("%s/%s-%d.csv", filepath, "test", i)).getURL().toExternalForm();
							ex.put("file", resource);
							exMap.put("partition_" + i, ex);
						} catch(Exception e) {
							log.error(e.getMessage());
						}
					}
					return exMap;
				})
				.partitionHandler(partitionHandler())
				.taskExecutor(simpleAsyncTaskExecutor())
				.build();
	}

	@Bean
	Step partChildStep1() {
		return new StepBuilder("part-child-step-1", jobRepository)
				.<String, String>chunk(100, platformTransactionManager).reader(reader5(null)).writer(writer3())
				.listener(stepStateListener()).build();
	}

	@Bean
	Step step2() {
		return new StepBuilder("step-2", jobRepository).<String, Integer>chunk(100, platformTransactionManager)
				.reader(reader2()).processor(processor()).writer(writer1()).taskExecutor(simpleAsyncTaskExecutor())
				.build();
	}

	@Bean
	Step fileInsertStep2() {
		return new StepBuilder("file-insert-step-2", jobRepository)
				.<String, String>chunk(200, platformTransactionManager).reader(reader3()).writer(writer2())
				.taskExecutor(simpleAsyncTaskExecutor()).build();
	}

	@Bean
	Step kafkaInsertStep2() {
		return new StepBuilder("kafka-insert-step-2", jobRepository)
				.<String, String>chunk(200, platformTransactionManager).reader(reader4()).writer(writer2())
				.faultTolerant().skipLimit(3).skip(DataIntegrityViolationException.class).build();
	}

	@Bean("fileCountStep")
	Step fileCountStep2() {
		return new StepBuilder("file-count-step-2", jobRepository)
				.<String, String>chunk(200, platformTransactionManager).reader(reader3()).writer(writer4(null, null))
				.taskExecutor(simpleAsyncTaskExecutor()).listener(stepStateListener()).build();
	}

	@Bean("kafkaCountStep")
	Step kafkaCountStep2() {
		return new StepBuilder("kafka-count-step-2", jobRepository)
				.<String, String>chunk(200, platformTransactionManager).reader(reader4()).writer(writer4(null, null))
				.faultTolerant().skipLimit(3).skip(DataIntegrityViolationException.class).listener(stepStateListener())
				.build();
	}

	@Bean
	Step countStep3() {
		return new StepBuilder("step-count-3", jobRepository).tasklet(tasklet3(), platformTransactionManager).build();
	}

	@Bean
	Tasklet tasklet1() {
		return (contribution, chunkContext) -> {
			var params = chunkContext.getStepContext().getJobParameters();
			log.info("\n-----------step1(owner: {}, desc: {})--------------", params.get("owner"),
					params.get("taskDesc"));
			return RepeatStatus.FINISHED;
		};
	}

	@Bean
	Tasklet tasklet2() {
		return (contribution, chunkContext) -> {
			var params = chunkContext.getStepContext().getJobParameters();
			String owner = (String) params.get("owner");
			String desc = (String) params.get("taskDesc");
			log.info("\n-----------step1(owner: {}, desc: {})--------------", params.get("owner"),
					params.get("taskDesc"));
			var file = new FileUrlResource(String.format("%s/%s.csv", filepath, "test"));
			// you can also use listNode
			if (!file.exists()) {
				log.info("\n-----------input file({}) not found--------------", "test.csv");
				chunkContext.getStepContext().getStepExecution().setTerminateOnly();
			} else {
				if (!kafkaAdminClient().listTopics().names().get().contains("test-topic")) {
					log.info("\n-----------kafka topic({}) not found--------------", "test-topic");
					chunkContext.getStepContext().getStepExecution().setTerminateOnly();
				} else {
					map.computeIfAbsent(owner, x -> new HashMap<>()).put(desc, new ConcurrentHashMap<>());
				}
			}
			return RepeatStatus.FINISHED;
		};
	}

	@Bean
	Tasklet tasklet3() {
		return (contribution, chunkContext) -> {
			var params = chunkContext.getStepContext().getJobParameters();
			String owner = (String) params.get("owner");
			String desc = (String) params.get("taskDesc");
			var countMap = map.get(owner).get(desc);
			log.info("\n-----------step3(owner: {}, desc: {})--------------", params.get("owner"),
					params.get("taskDesc"));
			int count = 0;

			for (var entry : countMap.entrySet()) {
				log.info("{}:{}", entry.getKey(), entry.getValue());
				count += entry.getValue();
			}
			log.info("valid item {}", count);
			map.get(owner).remove(desc);
			return RepeatStatus.FINISHED;
		};
	}

//	do not use common interface read/processor/write to prevent unexpected error
//	org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder<T>
	@Bean
	JdbcCursorItemReader<String> reader1() {
		return new JdbcCursorItemReaderBuilder<String>().name("cursor-batch-test-reader").dataSource(dataSource)
				.sql("select value from BATCH_TEST where value < ?")
				.preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[] { 10 }))
				.rowMapper((rs, rowNum) -> {
					return rs.getString("value");
				}).build();
	}

//	org.springframework.batch.item.database.builder.HibernatePagingItemReaderBuilder<T>
	@Bean
	JdbcPagingItemReader<String> reader2() {
		return new JdbcPagingItemReaderBuilder<String>().name("paging-batch-test-reader").dataSource(dataSource)
				.queryProvider(pagingQueryProvider()).parameterValues(Map.ofEntries(entry("value", 10))).pageSize(100)
				.rowMapper((rs, rowNum) -> {
					return rs.getString("value");
				}).build();
	}

	@Bean
	FlatFileItemReader<String> reader3() {
		FileUrlResource resource = null;
		try {
			resource = new FileUrlResource(String.format("%s/%s.csv", filepath, "test"));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return new FlatFileItemReaderBuilder<String>().name("test-file-reader").saveState(false)
				.resource(resource).delimited().delimiter(",").names("value")
				.fieldSetMapper((fieldSet) -> {
					return fieldSet.readString("value");
				}).build();
	}

//	kafka-producer-perf-test  --topic test-topic --throughput  -1  --record-size 1 --num-records 500 --producer-props bootstrap.servers=localhost:9092

	@Bean
	KafkaItemReader<String, String> reader4() {
		return new KafkaItemReaderBuilder<String, String>().name("test-file-reader").partitions(0, 1)
				.consumerProperties(kafkaProperties()).topic(topic).pollTimeout(Duration.ofSeconds(10)).build();
	}

	@Bean
	@StepScope
	FlatFileItemReader<String> reader5(@Value("#{stepExecutionContext['file']}") Resource resource) {
		return new FlatFileItemReaderBuilder<String>().name("part-file-reader").saveState(false).resource(resource)
				.delimited().delimiter(",").names("value").fieldSetMapper((fieldSet) -> {
					return fieldSet.readString("value");
				}).build();
	}

	@Bean
	ItemProcessor<String, Integer> processor() {
		return (item) -> {
			Integer res = Integer.valueOf(item);
			return res;
		};
	}

	@Bean
	ItemWriter<Integer> writer1() {
		return (items) -> {
			for (var item : items) {
				log.info("{}", item);
			}
		};
	}

	@Bean
	JdbcBatchItemWriter<String> writer2() {
		return new JdbcBatchItemWriterBuilder<String>().dataSource(dataSource)
				.sql("insert into BATCH_TEST (value) values(?)").itemPreparedStatementSetter((item, ps) -> {
					ps.setString(1, item);
				}).build();
	}

	@Bean
	ItemWriter<String> writer3() {
		return (items) -> {
			for (var item : items) {
				log.info("{}", item);
			}
		};
	}

	@Bean("writer4")
	@StepScope
	ItemWriter<String> writer4(@Value("#{jobParameters['owner']}") String owner,
			@Value("#{jobParameters['taskDesc']}") String desc) {
		return (items) -> {
			var concMap = map.get(owner).get(desc);
			for (var item : items) {
				if (StringUtils.isNumber(item)) {
					concMap.merge(Integer.valueOf(item), 1, Integer::sum);
				}
//				else {
//					log.info("drop {}, is not a number", item);
//				}
			}
		};
	}

	@Bean
	SimpleAsyncTaskExecutor simpleAsyncTaskExecutor() {
		var executor = new SimpleAsyncTaskExecutor(name);
//		executor.setConcurrencyLimit(2);
		return executor;
	}

	@Bean
	StepStateListener stepStateListener() {
		return new StepStateListener(log);
	}

	@Bean
	Properties kafkaProperties() {
		var props = new Properties();
		props.putAll(kafkaProperties.buildConsumerProperties(null));
		return props;
	}

	@Bean
	PagingQueryProvider pagingQueryProvider() {
		SqlPagingQueryProviderFactoryBean bean = new SqlPagingQueryProviderFactoryBean();
		bean.setDataSource(dataSource);
		bean.setSelectClause("select value");
		bean.setFromClause("from BATCH_TEST");
		bean.setWhereClause("where value < :value");
		bean.setSortKey("value");
		PagingQueryProvider provider = null;
		try {
			provider = bean.getObject();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return provider;
	}

	@Bean
	PartitionHandler partitionHandler() {
		var handler = new TaskExecutorPartitionHandler();
		handler.setGridSize(2);
		handler.setTaskExecutor(simpleAsyncTaskExecutor());
		handler.setStep(partChildStep1());
		try {
			handler.afterPropertiesSet();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return handler;
	}

	@Bean
	AdminClient kafkaAdminClient() {
		return KafkaAdminClient.create(kafkaAdmin.getConfigurationProperties());
	}

}
