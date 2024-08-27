package com.zhou.k8s.template.controller;

import java.util.function.Function;

import com.zhou.k8s.template.Base;
import com.zhou.k8s.template.KeyValue;
import com.zhou.k8s.template.controller.pod.PodTemplate;

public class Controller<T> extends Base<T> {
//	protected ControllerSpec spec;
	protected KeyValue spec;
	protected Controller(String apiVersion, String kind) {
		super(apiVersion, kind);
		spec = main.child("spec");
	}
	
	public T serviceName(String serviceName) {
//		spec.serviceName(serviceName);
		return self();
	}
	
	public T replicas(Integer replicas) {
		spec.add("replicas", replicas);
		return self();
	}
	
	public T selector(String key, String value) {
		spec.child("selector").child("matchLabels").add(key, value);
		return self();
	}
	
	public T template(Function<PodTemplate, PodTemplate> func) {
		func.apply(new PodTemplate(spec.child("template")));
		return self();
	}
	
}
