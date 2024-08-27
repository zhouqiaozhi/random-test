package com.zhou.k8s.template.volume;

import com.zhou.k8s.template.KeyValue;
import com.zhou.k8s.template.model.Model;

public class VolumeMount extends Model {
	private String name;
	private String mountPath;
	private String subPath;
	private Boolean readOnly;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMountPath() {
		return mountPath;
	}
	public void setMountPath(String mountPath) {
		this.mountPath = mountPath;
	}
	public String getSubPath() {
		return subPath;
	}
	public void setSubPath(String subPath) {
		this.subPath = subPath;
	}
	public Boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}
	public void add(KeyValue parent) {
		parent.addFirst("name", name);
		parent.add("mountPath", mountPath);
		parent.add("subPath", subPath);
		parent.add("readOnly", readOnly);
	}
}
