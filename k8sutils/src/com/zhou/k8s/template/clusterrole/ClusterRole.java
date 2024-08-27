package com.zhou.k8s.template.clusterrole;

import java.util.function.Function;

import com.zhou.k8s.template.Base;
import com.zhou.k8s.template.KeyValue;

public class ClusterRole extends Base<ClusterRole> {
	private KeyValue rules;
	
	public ClusterRole() {
		super("rbac.authorization.k8s.io/v1", "ClusterRole");
		wrapper.self(this);
	}
	
	public ClusterRole rule(Function<Rule, Rule> func) {
		if(rules == null) rules = main.child("rules");
		var rule = func.apply(new Rule());
		rule.add(rules);
		return this;
	}
}
