package com.zhou.k8s.template.ingress;

import com.zhou.k8s.template.KeyValue;
import com.zhou.k8s.template.model.ListModel;

public class IngressPath extends ListModel<IngressPath> {
	private String path;
	private String pathType;
	private String serviceName;
	private Integer portNumber;
	
	public IngressPath path(String path) {
		this.path = path;
		return this;
	}

	public IngressPath pathType(String pathType) {
		this.pathType = pathType;
		return this;
	}

	public IngressPath serviceName(String serviceName) {
		this.serviceName = serviceName;
		return this;
	}
	
	public IngressPath portNumber(Integer portNumber) {
		this.portNumber = portNumber;
		return this;
	}
	
	public IngressPath createNew() {
		return new IngressPath();
	}
	
	public void add(KeyValue paths) {
		paths.addFirst("path", path);
		paths.add("pathType", pathType);
		if(serviceName != null) {
			var service = paths.child("backend").child("service");
			service.add("name", serviceName);
			service.child("port").add("number", portNumber);
		}
	}

	public String getPath() {
		return path;
	}

	public String getPathType() {
		return pathType;
	}

	public String getServiceName() {
		return serviceName;
	}

	public Integer getPortNumber() {
		return portNumber;
	}
	
}
