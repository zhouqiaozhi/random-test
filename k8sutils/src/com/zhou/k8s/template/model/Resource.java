package com.zhou.k8s.template.model;

import com.zhou.k8s.template.KeyValue;

public class Resource extends Model {
	private String requestsCpu;
	private String limitCpu;
	private String requestsMemory;
	private String limitMemory;
	
	public Resource requests(String requestsCpu, String requestsMemory) {
		this.requestsCpu = requestsCpu;
		this.requestsMemory = requestsMemory;
		return this;
	}
	
	public Resource limit(String limitCpu, String limitMemory) {
		this.limitCpu = limitCpu;
		this.limitMemory = limitMemory;
		return this;
	}
	
	public void add(KeyValue parent) {
		var requests = parent.child("requests");
		requests.add("cpu", requestsCpu);
		requests.add("memory", requestsMemory);
		var limits = parent.child("limits");
		limits.add("cpu", limitCpu);
		limits.add("memory", limitMemory);
	}
}
