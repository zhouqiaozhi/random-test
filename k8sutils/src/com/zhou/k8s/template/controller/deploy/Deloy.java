package com.zhou.k8s.template.controller.deploy;

import com.zhou.k8s.template.controller.Controller;

public class Deloy extends Controller<Deloy> {
	
	public Deloy() {
		super("apps/v1", "Deployment");
		wrapper.self(this);
	}
}
