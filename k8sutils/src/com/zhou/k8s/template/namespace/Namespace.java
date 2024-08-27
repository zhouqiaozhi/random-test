package com.zhou.k8s.template.namespace;

import com.zhou.k8s.template.Base;

public class Namespace extends Base<Namespace> {
	public Namespace() {
		super("v1", "Namespace");
		wrapper.self(this);
	}
}
