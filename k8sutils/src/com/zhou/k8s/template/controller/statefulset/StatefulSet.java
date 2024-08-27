package com.zhou.k8s.template.controller.statefulset;

import com.zhou.k8s.template.controller.Controller;

public class StatefulSet extends Controller<StatefulSet> {	
	public StatefulSet() {
		super("apps/v1", "StatefulSet");
		wrapper.self(this);
	}
}
