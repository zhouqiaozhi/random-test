package com.zhou.k8s.template.controller.daemonset;

import com.zhou.k8s.template.controller.Controller;

public class DaemonSet extends Controller<DaemonSet> {
	public DaemonSet() {
		super("apps/v1", "DaemonSet");
		wrapper.self(this);
	}
}
