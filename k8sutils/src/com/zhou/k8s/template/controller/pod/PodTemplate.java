package com.zhou.k8s.template.controller.pod;

import java.util.function.Function;

import com.zhou.k8s.template.KeyValue;
import com.zhou.k8s.template.configmap.ConfigMapItem;
import com.zhou.k8s.template.container.Container;

public class PodTemplate {
	private KeyValue metadata;
	private KeyValue labels;
	private KeyValue spec;
	private KeyValue volumes;
	private KeyValue containers;
	
	public PodTemplate(KeyValue content) {
		metadata = content.child("metadata");
		spec = content.child("spec");
	}
	
	public PodTemplate label(String key, String value) {
		if(labels == null) labels = metadata.child("labels");
		labels.add(key, value);
		return this;
	}
	
	public PodTemplate serviceAccountName(String serviceAccountName) {
		spec.add("serviceAccountName", serviceAccountName);
		return this;
	}
	
	public PodTemplate terminationGracePeriodSeconds(Integer terminationGracePeriodSeconds) {
		spec.add("terminationGracePeriodSeconds", terminationGracePeriodSeconds);
		return this;
	}
	
	public PodTemplate nodeSelector(String key, String value) {
		spec.child("nodeSelector").add(key, value);
		return this;
	}
	
	public PodTemplate container(Function<Container, Container> func) {
		if(containers == null) containers = spec.child("containers");
		var container = func.apply(new Container());
		container.add(containers);
		return this;
	}
	
	private KeyValue volumes() {
		return volumes != null ? volumes : (volumes = spec.child("volumes"));
	}
	
	public PodTemplate configMap(String name, String path) {
		var volumes = volumes();
		volumes.addFirst("name", name);
		var configMap = volumes.child("configMap");
		configMap.add("name", path);
		return this;
	}
	
	public PodTemplate configMap(String name, String configName, Function<ConfigMapItem, ConfigMapItem> func) {
		var volumes = volumes();
		volumes.addFirst("name", name);
		var configMap = volumes.child("configMap");
		configMap.add("name", configName);
		var item = new ConfigMapItem();
		func.apply(item);
		var its = configMap.child("items");
		while(item != null) {
			item.add(its);
			item = item.next(false);
		}
		return this;
	}
	
	public PodTemplate configMap(String name, String path, String defaultMode) {
		var volumes = volumes();
		volumes.addFirst("name", name);
		var configMap = volumes.child("configMap");
		configMap.add("name", path);
		configMap.add("defaultMode", defaultMode);
		return this;
	}
	
	public PodTemplate hostPath(String name, String path) {
		var volumes = volumes();
		volumes.addFirst("name", name);
		volumes.child("hostPath").add("path", path);
		return this;
	}
	
	public PodTemplate hostPath(String name, String path, String type) {
		var volumes = volumes();
		volumes.addFirst("name", name);
		var hostpath = volumes.child("hostPath");
		hostpath.add("path", path);
		hostpath.add("type", type);
		return this;
	}
}
