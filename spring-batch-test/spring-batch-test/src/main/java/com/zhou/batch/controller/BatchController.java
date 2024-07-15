package com.zhou.batch.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhou.batch.service.BatchService;

@RestController
@RequestMapping("/batch")
public class BatchController {
	
	@Value("${com.zhou.file.path}")
	private String filepath;
	
	@Autowired
	private BatchService batchService;
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	@PostMapping("/print")
	public Boolean print(@RequestBody HashMap<String, Object> params) {
		batchService.start("print-job", params);
		return true;
	}
	
	@PostMapping("/insert")
	public Boolean insert(@RequestBody HashMap<String, Object> params) {
		batchService.start("insert-job", params);
		return true;
	}
	
	@PostMapping("/insert/kakfa")
	public Boolean kafkaInsert(@RequestBody HashMap<String, Object> params) {
		batchService.start("kafka-insert-job", params);
		return true;
	}
	
	@PostMapping("/insert/paralel")
	public Boolean paralelInsert(@RequestBody HashMap<String, Object> params) {
		batchService.start("paralel-insert-job", params);
		return true;
	}
	
	
	@PostMapping("/count/paralel")
	public Boolean paralelCount(@RequestBody HashMap<String, Object> params) {
		batchService.start("paralel-count-job", params);
		return true;
	}
	@PostMapping("/partition")
	public Boolean partition(@RequestBody HashMap<String, Object> params) {
		batchService.start("partition-job", params);
		return true;
	}
	//test
	@PostMapping("/generate/csv/{name}/{type}/{size}")
	public Boolean generate(@PathVariable("name") String name, @PathVariable("type") Integer type, @PathVariable("size") Integer size) throws Exception {
		Random random = new Random();
		var list = new ArrayList<String[]>();
		switch(type) {
			case 1 -> {
				for(int i = 0; i < size; i++) {
					int k = random.nextInt(10);
					list.add(new String[] {String.valueOf(k)});
				}
			}
			default -> {}
		}
		
		var f = new File(String.format("%s/%s.csv", filepath, name));
		if(!f.exists()) {
			f.getParentFile().mkdirs();
			f.createNewFile();
		}
		try (PrintWriter pw = new PrintWriter(f)) {
			list.stream()
	          .map(this::convertToCSV)
	          .forEach(pw::println);
	    }
		return true;
	}
	
	public String convertToCSV(String[] data) {
	    return Stream.of(data).collect(Collectors.joining(","));
	}
}
