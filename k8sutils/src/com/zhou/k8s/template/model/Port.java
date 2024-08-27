package com.zhou.k8s.template.model;

import com.zhou.k8s.template.KeyValue;

public class Port extends Model {
	private Integer containerPort;
	private Integer port;
	private String name;
	private String protocol;
	private String targetPort;

	public Integer getContainerPort() {
		return containerPort;
	}

	public void setContainerPort(Integer containerPort) {
		this.containerPort = containerPort;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(String targetPort) {
		this.targetPort = targetPort;
	}
	
	public void add(KeyValue parent) {
		if(port != null) {
			parent.addFirst("port", port);
		} else {
			parent.addFirst("containerPort", containerPort);
		}
		parent.add("protocol", protocol);
		parent.add("name", name);
		parent.add("targetPort", targetPort);
	}
}
