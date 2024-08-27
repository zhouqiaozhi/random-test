package com.zhou.k8s.template;

public class Wrapper<T> {
	T self;
	
	public Wrapper<T> self(T self) {
		this.self = self;
		return this;
	}
	
	public T getSelf() {
		return self;
	}
}
