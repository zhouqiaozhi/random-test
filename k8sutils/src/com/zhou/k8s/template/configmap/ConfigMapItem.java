package com.zhou.k8s.template.configmap;

import com.zhou.k8s.template.KeyValue;
import com.zhou.k8s.template.model.ListModel;

public class ConfigMapItem extends ListModel<ConfigMapItem> {
	private String key;
	private String path;
	public ConfigMapItem key(String key) {
		this.key = key;
		return this;
	}
	
	public ConfigMapItem path(String path) {
		this.path = path;
		return this;
	}
	
	public String getKey() {
		return key;
	}
	public String getPath() {
		return path;
	}
	
	public ConfigMapItem createNew() {
		return new ConfigMapItem();
	}
	
	public void add(KeyValue items) {
		items.addFirst("key", key);
		items.add("path", path);
	}
}
