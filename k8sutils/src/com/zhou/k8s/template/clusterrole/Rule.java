package com.zhou.k8s.template.clusterrole;

import java.util.Arrays;
import java.util.List;

import com.zhou.k8s.template.KeyValue;
import com.zhou.k8s.utils.StringUtils;

public class Rule {

	private List<String> apiGroups;
	private List<String> resources;
	private List<String> verbs;
	
	public Rule apiGroups(String... apiGroups) {
		this.apiGroups = Arrays.asList(apiGroups);
		return this;
	}
	
	public Rule resources(String... resources) {
		this.resources = Arrays.asList(resources);
		return this;
	}
	
	public Rule verbs(String... verbs) {
		this.verbs = Arrays.asList(verbs);
		return this;
	}
	
	public void add(KeyValue rules) {
		var ags = rules.childFirst("apiGroups");
		for(var ag: apiGroups) {
			ags.addFirst(StringUtils.getString(ag));
		}
		var rs = rules.child("resources");
		for(var r: resources) {
			rs.addFirst(StringUtils.getString(r));
		}
		var vs = rules.child("verbs");
		for(var v: verbs) {
			vs.addFirst(StringUtils.getString(v));
		}
	}
}
