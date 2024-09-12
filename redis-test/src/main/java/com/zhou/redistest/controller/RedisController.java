package com.zhou.redistest.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {
	
	@Autowired
	RedisTemplate<String, String> redisTemplate;
	
	@GetMapping("/get/{key}")
	public String get(@PathVariable String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	@GetMapping("/set/{key}/{value}")
	public Boolean set(@PathVariable String key, @PathVariable String value) {
//		redisTemplate.opsForValue().set(key, value, 10, TimeUnit.SECONDS);
		redisTemplate.opsForValue().set(key, value);
		return true;
	}
	
	@GetMapping("/list/set/{key}/{values}")
	public Boolean set(@PathVariable String key, @PathVariable List<String> values) {
		redisTemplate.opsForList().rightPop(key, redisTemplate.opsForList().size(key));
		redisTemplate.opsForList().rightPushAll(key, values);
		return true;
	}
	
	@GetMapping("/list/get/{key}")
	public List<String> listGet(@PathVariable String key) {
		return redisTemplate.opsForList().range(key, 0, -1);
	}
	
	@GetMapping("/map/set/{key}/{values}")
	public Boolean mapSet(@PathVariable String key, @PathVariable Map<String, String> map) {
		var valuesS = map.get("values");
		Map<String, String> values = new HashMap<>();
		for(var v: ((String)valuesS).split(",")) {
			var entry = v.split("=");
			values.put(entry[0], entry[1]);
		}
		var existKeys = redisTemplate.opsForHash().keys(key).stream().toArray();
		if(existKeys.length != 0) redisTemplate.opsForHash().delete(key, existKeys);
		redisTemplate.opsForHash().putAll(key, (Map<String, String>)values);
		return true;
	}
	
	@GetMapping("/map/get/{key}")
	public Map<String, String> mapGet(@PathVariable String key) {
		return redisTemplate.<String, String>opsForHash().entries(key);
	}
	
	@GetMapping("/set/set/{key}/{values}")
	public Boolean setSet(@PathVariable String key, @PathVariable String[] values) {
		redisTemplate.opsForSet().pop(key, redisTemplate.opsForSet().size(key));
		redisTemplate.opsForSet().add(key, values);
		return true;
	}
	
	@GetMapping("/set/get/{key}")
	public Set<String> setGet(@PathVariable String key) {
		return redisTemplate.opsForSet().members(key);
	}
	
	@GetMapping("/zset/set/{key}/{values}")
	public Boolean zsetSet(@PathVariable String key, @PathVariable String[] values) {
		redisTemplate.opsForZSet().popMax(key, redisTemplate.opsForSet().size(key));
		Set<TypedTuple<String>> set = new HashSet<>();
		for(int i = 0; i < values.length; i++) {
			set.add(TypedTuple.of(values[i], i * 1.));
		}
		redisTemplate.opsForZSet().add(key, set);
		return true;
	}
	
	@GetMapping("/zset/get/{key}")
	public Set<TypedTuple<String>> zsetGet(@PathVariable String key) {
		return redisTemplate.opsForZSet().rangeWithScores(key, 0, -1);
	}
}
