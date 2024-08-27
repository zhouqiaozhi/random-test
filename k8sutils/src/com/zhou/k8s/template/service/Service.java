package com.zhou.k8s.template.service;

import com.zhou.k8s.template.Base;
import com.zhou.k8s.template.KeyValue;
import com.zhou.k8s.template.model.Port;

public class Service extends Base<Service> {
	private KeyValue spec;
	private KeyValue ports;
	public Service () {
		super("v1", "Service");
		wrapper.self(this);
		spec = main.child("spec");
	}
	
	public Service clusterIP(String clusterIP) {
		spec.add("clusterIP", clusterIP);
		return this;
	}
	
	public Service type(String type) {
		spec.add("type", type);
		return this;
	}
	
	public Service port(Integer port, String targetPort) {
		if(ports == null) ports = spec.child("ports");
		var p = new Port();
		p.setPort(port);
		p.setTargetPort(targetPort);
		p.add(ports);
		return this;
	}
	
	public Service port(Integer port, String protocol, String targetPort) {
		if(ports == null) ports = spec.child("ports");
		var p = new Port();
		p.setPort(port);
		p.setProtocol(protocol);
		p.setTargetPort(targetPort);
		p.add(ports);
		return this;
	}
	
	public Service selector(String key, String value) {
		spec.child("selector").add("k8s-app", "kibana");
		return this;
	}
}
