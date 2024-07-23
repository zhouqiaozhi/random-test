package com.zhou.postgres.dto;

import java.util.List;

import com.zhou.postgres.model.CountryBasedCustomers;

public class CountryBasedCustomersDto {
	private String country;
	private List<String> customers;
	// or objectMapper
	public CountryBasedCustomersDto(CountryBasedCustomers original) {
		this.country = original.getCountry();
		this.customers = original.getCustomers();
	}
	public String getCountry() {
		return country;
	}
	public List<String> getCustomers() {
		return customers;
	}
	
}
