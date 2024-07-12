package com.zhou.batch.config;

import static org.assertj.core.api.Assertions.entry;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;

import com.zhou.batch.basic.JobBasic;
import com.zhou.batch.utils.StringUtils;

@Configuration
public class JobConfig extends JobBasic {
	
	private static final Map<String, Map<String, ConcurrentHashMap<Integer, Integer>>> map = new HashMap<>();
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private KafkaProperties kafkaProperties;
	
	@Value("${spring.kafka.template.default-topic}")
	private String topic;
	
	
	JobConfig() {
		super("simple-job");
	}
	@Bean
	Tasklet tasklet1() {
		return (contribution, chunkContext) -> {
			var params = chunkContext.getStepContext().getJobParameters();
			log.info("task owner:{} desc:{}", params.get("owner"), params.get("taskDesc"));
			return RepeatStatus.FINISHED;
		};
	}
	
	@Bean
	Tasklet tasklet2() {
		return (contribution, chunkContext) -> {
			var params = chunkContext.getStepContext().getJobParameters();
			String owner = (String)params.get("owner");
			String desc = (String)params.get("taskDesc");
			log.info("task owner:{} desc:{}", owner, desc);
			map.computeIfAbsent(owner, x -> new HashMap<>()).putIfAbsent(desc, new ConcurrentHashMap<>());
			return RepeatStatus.FINISHED;
		};
	}
	
	@Bean
	Tasklet tasklet3() {
		return (contribution, chunkContext) -> {
			var params = chunkContext.getStepContext().getJobParameters();
			String owner = (String)params.get("owner");
			String desc = (String)params.get("taskDesc");
			log.info("task owner:{} desc:{}", owner, desc);
			var countMap = map.get(owner).get(desc);
			int count = 0;
			for(var entry: countMap.entrySet()) {
				count += entry.getValue();
				log.info("{}:{}", entry.getKey(), entry.getValue());
			}
			log.info("valid item {}", count);
			map.get(owner).remove(desc);
			return RepeatStatus.FINISHED;
		};
	}
	
//	org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder<T>
	@Bean
	ItemReader<String> reader1() {
		return new JdbcCursorItemReaderBuilder<String>()
				.name("simpleFromReader")
				.dataSource(dataSource)
				.sql("select value from BATCH_TEST where value < ?")
				.preparedStatementSetter(new ArgumentPreparedStatementSetter(new Object[] { 10 }))
				.rowMapper((rs, rowNum) -> {
					return rs.getString("value");
				})
				.build();
	}
//	org.springframework.batch.item.database.builder.HibernatePagingItemReaderBuilder<T>
	@Bean
	ItemReader<String> reader2() {
		return new JdbcPagingItemReaderBuilder<String>()
				.name("simpleFromPagingReader")
				.dataSource(dataSource)
				.queryProvider(pagingQueryProvider())
				.parameterValues(Map.ofEntries(entry("value", 10)))
				.pageSize(100)
				.rowMapper((rs, rowNum) -> {
					return rs.getString("value");
				})
				.build();
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
		} catch(Exception e) {
			log.error(e.getMessage());
		}
		return provider;
	}
	
	@Bean
	ItemReader<String> reader3() {
		return new FlatFileItemReaderBuilder<String>()
				.name("test-file-reader")
				.saveState(false)
				.resource(new ClassPathResource("static/test.csv"))
				.delimited().delimiter(",")
				.names("value")
				.fieldSetMapper((fieldSet) -> {
					return fieldSet.readString("value");
				})
				.build();
	}
	
//	kafka-producer-perf-test  --topic test-topic --throughput  -1  --record-size 1 --num-records 500 --producer-props bootstrap.servers=localhost:9092
	
	@Bean
	ItemReader<String> reader4() {
		return new KafkaItemReaderBuilder<String, String>()
				.name("kafka string reader")
				.saveState(true)
				.partitions(0, 1)
				.consumerProperties(kafkaProperties())
				.topic(topic)
				.pollTimeout(Duration.ofSeconds(30))
				.build();
	}
	
	@Bean
	Properties kafkaProperties() {
		var props = new Properties();
		props.putAll(kafkaProperties.buildConsumerProperties(null));
		return props;
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
			for(var item: items) {
				log.info("{}", item);
			}
		};
	}
	
	@Bean
	ItemWriter<String> writer2() {
		return new JdbcBatchItemWriterBuilder<String>()
				.dataSource(dataSource)
				.sql("insert into BATCH_TEST (value) values(?)")
				.itemPreparedStatementSetter((item, ps) -> {
					ps.setString(1, item);
				})
				.build();
	}
	
	@Bean
	ItemWriter<String> writer3() {
		return (items) -> {
			for(var item: items) {
				log.info("{}", item);
			}
		};
	}
	
	@Bean("writer4")
	@StepScope
	ItemWriter<String> writer4(@Value("#{jobParameters['owner']}")String owner, @Value("#{jobParameters['taskDesc']}")String desc) {
		return (items) -> {
			var concMap = map.get(owner).get(desc);
			for(var item: items) {
				if(StringUtils.isNumber(item)) {
					concMap.merge(Integer.valueOf(item), 1, Integer::sum);
				} 
//				else {
//					log.info("drop {}, is not a number", item);
//				}
			}
		};
	}
	
	@Bean
	Step step1() {
		return new StepBuilder("step-1", jobRepository)
				.tasklet(tasklet1(), platformTransactionManager)
				.build();
	}
	
	@Bean
	Step countStep1() {
		return new StepBuilder("step-count-1", jobRepository)
				.tasklet(tasklet2(), platformTransactionManager)
				.build();
	}
	
	@Bean
	Step countStep3() {
		return new StepBuilder("step-count-3", jobRepository)
				.tasklet(tasklet3(), platformTransactionManager)
				.build();
	}
	
	@Bean
	Step step2() {
		return new StepBuilder("step-2", jobRepository)
				.<String, Integer>chunk(100, platformTransactionManager)
				.reader(reader2())
				.processor(processor())
				.writer(writer1())
				.taskExecutor(simpleAsyncTaskExecutor())
				.build();
	}
	@Bean 
	Step insertStep2() {
		return new StepBuilder("step-insert-2", jobRepository)
				.<String, String>chunk(200, platformTransactionManager)
				.reader(reader3())
				.writer(writer2())
				.taskExecutor(simpleAsyncTaskExecutor())
				.build();
	}
	
	@Bean
	Step kafkaInsertStep() {
		return new StepBuilder("step-insert-2", jobRepository)
				.<String, String>chunk(200, platformTransactionManager)
				.reader(reader4())
				.writer(writer2())
				.faultTolerant()
				.skipLimit(3)
				.skip(DataIntegrityViolationException.class)
				.build();
	}
	
	@Bean("fileCountStep")
	Step fileCountStep(@Qualifier("writer4") ItemWriter<String> writer4) {
		return new StepBuilder("step-count-file-2", jobRepository)
				.<String, String>chunk(200, platformTransactionManager)
				.reader(reader3())
				.writer(writer4)
				.taskExecutor(simpleAsyncTaskExecutor())
				.build();
	}
	
	@Bean("kafkaCountStep")
	Step kafkaCountStep(@Qualifier("writer4") ItemWriter<String> writer4) {
		return new StepBuilder("step-count-kafka-2", jobRepository)
				.<String, String>chunk(200, platformTransactionManager)
				.reader(reader4())
				.writer(writer4)
				.faultTolerant()
				.skipLimit(3)
				.skip(DataIntegrityViolationException.class)
				.build();
	}
	
	@Bean
	SimpleAsyncTaskExecutor simpleAsyncTaskExecutor() {
		var executor = new SimpleAsyncTaskExecutor(name);
		executor.setConcurrencyLimit(5);
		return executor;
	}
	
	
	@Bean("printJob")
	protected Job job() {
		return new JobBuilder("print-job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.next(step2())
				.build();
	}
	
	@Bean("insertJob")
	Job insertJob() {
		return new JobBuilder("insert-job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.next(insertStep2())
				.build();
	}
	
	@Bean("kafkaInsertJob")
	Job kafkaInsertJob() {
		return new JobBuilder("kafka-insert-job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.next(kafkaInsertStep())
				.build();
	}
	
	@Bean("paralelInsertJob")
	Job paralelInsertJob() {
		var flowFile = new FlowBuilder<Flow>("flow-file")
				.start(insertStep2())
				.build();
		var flowKafka = new FlowBuilder<Flow>("flow-kafka")
				.start(kafkaInsertStep())
				.split(simpleAsyncTaskExecutor())
				.add(flowFile)
				.build();
		return new JobBuilder("paralel-insert-job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(flowKafka)
				.end()
				.build();
	}
	
	@Bean("paralelCountJob")
	Job paralelCountJob(@Qualifier("fileCountStep") Step fileCountStep, @Qualifier("kafkaCountStep") Step kafkaCountStep) {
		var flowFile = new FlowBuilder<Flow>("flow-file")
				.start(fileCountStep)
				.build();
		var flowKafka = new FlowBuilder<Flow>("flow-kafka")
				.start(kafkaCountStep)
				.split(simpleAsyncTaskExecutor())
				.add(flowFile)
				.build();
		var mainFlow = new FlowBuilder<Flow>("flow-main")
				.start(countStep1())
				.next(flowKafka)
				.next(countStep3())
				.build();
		return new JobBuilder("paralel-count-job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(mainFlow)
				.end()
				.build();
	}
	
	// TODO
	
}
