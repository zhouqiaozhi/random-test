package com.zhou.k8s.template.serviceAccount;

import com.zhou.k8s.template.Base;

public class ServiceAccount extends Base<ServiceAccount> {
	public ServiceAccount() {
		super("v1", "ServiceAccount");
		wrapper.self(this);
	}
	
}
