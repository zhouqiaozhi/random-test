package com.zhou.k8s.template;

import java.util.function.Function;

public class Base<T> {
	protected KeyValue main;
	protected Wrapper<T> wrapper = new Wrapper<>();
	private KeyValue metadata;
	private KeyValue labels;
	protected Base() {
		
	}
	protected Base(String apiVersion, String kind) {
		main = new KeyValue();
		main.child("apiVersion", apiVersion);
		main.child("kind", kind);
		metadata = main.child("metadata");
	}
	
	public T self() {
		return wrapper.getSelf();
	}
	
	public T applyOrDefault(Function<T, T> func) {
		if(func != null) return func.apply(self());
		return self();
	}
	
	public T name(String name) {
		metadata.child("name", name);
		return self();
	}
	
	public T namespace(String namespace) {
		metadata.child("namespace", namespace);
		return self();
	}
	
	public T label(String name, String value) {
		if(labels == null) labels = metadata.child("labels");
		labels.add(name, value);
		return self();
	}
	
	@Override
	public String toString() {
		return main.toString();
	}
}
