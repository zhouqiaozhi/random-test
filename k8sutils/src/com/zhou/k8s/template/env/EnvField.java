package com.zhou.k8s.template.env;

import com.zhou.k8s.template.KeyValue;

public class EnvField extends Env {
	@Override
	public void add(KeyValue env) {
		env.addFirst("name", name);
		env.child("valueFrom").child("fieldRef").add("fieldPath", value);
	}
}
