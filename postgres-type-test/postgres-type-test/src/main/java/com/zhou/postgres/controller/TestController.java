package com.zhou.postgres.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.zhou.postgres.dto.CountryBasedCustomersDto;
import com.zhou.postgres.service.FuncService;

@RestController
public class TestController {
	private final FuncService funcService;
	
	TestController(FuncService funcService) {
		this.funcService = funcService;
		
	}
	
	@GetMapping("/add/{a}/{b}")
	public Integer add(@PathVariable Integer a, @PathVariable Integer b) {
		return funcService.add(a, b);
	}
	
	@GetMapping("/countries")
	public List<String> add() {
		return funcService.getCountries();
	}
	@GetMapping("/getCountryBasedCustomers")
	public List<CountryBasedCustomersDto> getCountryBasedCustomers() {
		return funcService.getCountryBasedCustomers();
	}
	
}
