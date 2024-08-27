package com.zhou.k8s.template.env;

import com.zhou.k8s.template.KeyValue;
import com.zhou.k8s.template.model.Model;
import com.zhou.k8s.utils.StringUtils;

public class Env extends Model {
	protected String name;
	protected String value;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public void add(KeyValue parent) {
		parent.addFirst("name", name);
		parent.add("value", StringUtils.getString(value));
	}
}
