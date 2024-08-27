package com.zhou.k8s.template;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.zhou.k8s.template.clusterrole.ClusterRole;
import com.zhou.k8s.template.clusterrole.ClusterRoleBinding;
import com.zhou.k8s.template.configmap.ConfigMap;
import com.zhou.k8s.template.controller.daemonset.DaemonSet;
import com.zhou.k8s.template.controller.deploy.Deloy;
import com.zhou.k8s.template.controller.statefulset.StatefulSet;
import com.zhou.k8s.template.ingress.Ingress;
import com.zhou.k8s.template.namespace.Namespace;
import com.zhou.k8s.template.service.Service;
import com.zhou.k8s.template.serviceAccount.ServiceAccount;

public class Template {
	protected final String lb = System.getProperty("line.separator");
	@SuppressWarnings("rawtypes")
	List<Base> list = new ArrayList<>();
	public static Template buidler() {
		return new Template();
	}
	
	public String build() {
		var sb = new StringBuilder();
		if(list.size() == 1) {
			sb.append(list.get(0));
		} else {
			for(var t: list) {
				sb.append("---");
				sb.append(lb);
				sb.append(t);
				sb.append(lb);
			}
		}
		
		return sb.toString();
	}
	
	public Template namespace(Function<Namespace, Namespace> func) {
		var namespace = new Namespace().applyOrDefault(func);
		list.add(namespace);
		return this;
	}
	
	public Template service(Function<Service, Service> func) {
		var service = new Service().applyOrDefault(func);
		list.add(service);
		return this;
	}
	
	public Template clusterRoleBinding(Function<ClusterRoleBinding, ClusterRoleBinding> func) {
		var clusterRoleBinding = new ClusterRoleBinding().applyOrDefault(func);
		list.add(clusterRoleBinding);
		return this;
	}
	
	public Template serviceAccount(Function<ServiceAccount, ServiceAccount> func) {
		var serviceAccount = new ServiceAccount().applyOrDefault(func);
		list.add(serviceAccount);
		return this;
	}
	
	public Template clusterRole(Function<ClusterRole, ClusterRole> func) {
		var clusterRole = new ClusterRole().applyOrDefault(func);
		list.add(clusterRole);
		return this;
	}
	
	public Template deploy(Function<Deloy, Deloy> func) {
		var deloy = new Deloy().applyOrDefault(func);
		list.add(deloy);
		return this;
	}
	
	public Template configMap(Function<ConfigMap, ConfigMap> func) {
		var configMap = new ConfigMap().applyOrDefault(func);
		list.add(configMap);
		return this;
	}
	
	public Template statefulset(Function<StatefulSet, StatefulSet> func) {
		var statefulset = new StatefulSet().applyOrDefault(func);
		list.add(statefulset);
		return this;
	}
	
	public Template daemonset(Function<DaemonSet, DaemonSet> func) {
		var daemonSet = new DaemonSet().applyOrDefault(func);
		list.add(daemonSet);
		return this;
	}
	
	public Template ingress(Function<Ingress, Ingress> func) {
		var ingress = new Ingress().applyOrDefault(func);
		list.add(ingress);
		return this;
	}
}
