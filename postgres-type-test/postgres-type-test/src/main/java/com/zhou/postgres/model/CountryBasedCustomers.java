package com.zhou.postgres.model;

import java.util.Arrays;
import java.util.List;

import org.postgresql.util.PGobject;

public class CountryBasedCustomers extends PGobject {
	private static final long serialVersionUID = 1L;
	String country;
	List<String> customers;
	
	public CountryBasedCustomers() {}
	
	@Override
	public void setValue(String value) {
		if(value != null && value.length() > 2) {
			value = value.substring(1, value.length() - 1);
			var vals = value.split(",", 2);
			setCountry(vals[0]);
			vals[1] = vals[1].replaceAll("\"", "");
			vals[1] = vals[1].substring(1, vals[1].length() - 1);
			if(!vals[1].isEmpty()) {
				setCustomers(Arrays.stream(vals[1].split(",")).toList());
			}
		}
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<String> getCustomers() {
		return customers;
	}

	public void setCustomers(List<String> customers) {
		this.customers = customers;
	}

	@Override
	public String toString() {
		return "CountryBasedCustomers [country=" + country + ", customers=" + customers + "]";
	}
	
}
