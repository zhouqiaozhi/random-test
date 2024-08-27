package com.zhou.k8s.template.configmap;

import java.util.function.Function;

import com.zhou.k8s.template.Base;
import com.zhou.k8s.template.KeyValue;

public class ConfigMap extends Base<ConfigMap> {
	public ConfigMap() {
		super("v1", "ConfigMap");
		wrapper.self(this);
	}
	
	public ConfigMap data(Function<KeyValue, KeyValue> func) {
		func.apply(main.child("data"));
		return this;
	}
}
