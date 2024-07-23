package com.zhou.postgres.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.postgresql.jdbc.PgArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import com.zhou.postgres.dto.CountryBasedCustomersDto;
import com.zhou.postgres.model.CountryBasedCustomers;
import com.zhou.postgres.service.FuncService;

@Service
public class FuncServiceImpl implements FuncService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Override
	public int add(int a, int b) {
		var call = new SimpleJdbcCall(jdbcTemplate).withFunctionName("add_int").withSchemaName("public");
		var params = new MapSqlParameterSource()
				.addValue("a", a)
				.addValue("b", b);
		var res = call.executeFunction(Integer.class, params);
		return res;
	}
	@Override
	public List<String> getCountries() {
		var call = new SimpleJdbcCall(jdbcTemplate).withFunctionName("getcountries").withSchemaName("public");
		var resp = call.executeFunction(PgArray.class, new MapSqlParameterSource());
		List<String> res = null;
		try {
			res = Arrays.stream((String[])resp.getArray()).toList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	@Override
	public List<CountryBasedCustomersDto> getCountryBasedCustomers() {
		var call = new SimpleJdbcCall(jdbcTemplate).withFunctionName("getcountrybasedcustomers").withSchemaName("public");
		var resp = call.executeFunction(PgArray.class, new MapSqlParameterSource());
		List<CountryBasedCustomersDto> res = null;
		try {
			var array = (Object[])resp.getArray();
			res = new ArrayList<>();
			for(var x: array) {
				res.add(new CountryBasedCustomersDto((CountryBasedCustomers)x));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
}
