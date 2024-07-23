package com.zhou.postgres.service;

import java.util.List;

import com.zhou.postgres.dto.CountryBasedCustomersDto;

public interface FuncService {
	int add(int a, int b);
	List<String> getCountries();
	List<CountryBasedCustomersDto> getCountryBasedCustomers();
}
