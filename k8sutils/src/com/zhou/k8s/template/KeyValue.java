package com.zhou.k8s.template;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KeyValue {
	private boolean first = false;
	private String lb = System.lineSeparator();
	private KeyValue parent;
	private String space;
	private String spacet;
	private List<KeyValue> keyValues;
	private String name;
	private Object value;
	public KeyValue() {
		parent = null;
	}
	
	public KeyValue(KeyValue parent, String space, String name) {
		this.parent = parent;
		if(space == null) {
			this.space = "";
		} else {
			this.space = space + "  ";
			this.spacet = space + "- ";
		}
		this.name = name;
	}
	
	public KeyValue(KeyValue parent, String space, String name, Object value) {
		this.parent = parent;
		if(space == null) {
			this.space = "";
		} else {
			this.space = space + "  ";
			this.spacet = space + "- ";
		}
		this.name = name;
		this.value = value;
	}
	
	public KeyValue(KeyValue parent, String space, String name, Object value, boolean first) {
		this.parent = parent;
		if(space == null) {
			this.space = "";
		} else {
			this.space = space + "  ";
			this.spacet = space + "- ";
		}
		this.name = name;
		this.value = value;
		this.first = first;
	}
	
	public KeyValue childFirst(String name) {
		if(keyValues == null) keyValues = new ArrayList<>();
		var item = new KeyValue(this, space, name, null, true);
		keyValues.add(item);
		return item;
	}
	
	public KeyValue child(String name) {
		if(keyValues == null) keyValues = new ArrayList<>();
		var item = new KeyValue(this, space, name);
		keyValues.add(item);
		return item;
	}
	
	public KeyValue child(String name, Object value) {
		if(keyValues == null) keyValues = new ArrayList<>();
		var item = new KeyValue(this, space, name, value);
		keyValues.add(item);
		return item;
	}
	
	public KeyValue add(String value) {
		if(value == null) return this;
		if(keyValues == null) keyValues = new ArrayList<>();
		var item = new KeyValue(this, space, null, value);
		keyValues.add(item);
		return this;
	}
	
	public KeyValue addFirst(String value) {
		if(value == null) return this;
		if(keyValues == null) keyValues = new ArrayList<>();
		var item = new KeyValue(this, space, null, value, true);
		keyValues.add(item);
		return this;
	}
	
	public KeyValue add(String name, Object value) {
		if(value == null) return this;
		if(keyValues == null) keyValues = new ArrayList<>();
		var item = new KeyValue(this, space, name, value);
		keyValues.add(item);
		return this;
	}
	
	public KeyValue addFirst(String name, Object value) {
		if(value == null) return this;
		if(keyValues == null) keyValues = new ArrayList<>();
		var item = new KeyValue(this, space, name, value, true);
		keyValues.add(item);
		return this;
	}
	
	public KeyValue done() {
		return parent;
	}
	
	@Override
	public String toString() {
		var sb = new StringBuilder();
		if(name != null) {
			if(space != null) sb.append(first ? spacet : space);
			sb.append(name);
			sb.append(":");
		}
		if(value != null) {
			if(name != null) sb.append(" ");
			else if(first) {
				 sb.append(spacet);
			} else {
				sb.append(space);
			}
			sb.append(value);
		}
		if(keyValues != null) {
			if(!sb.isEmpty()) sb.append(lb);
			sb.append(keyValues.stream().map(KeyValue::toString).collect(Collectors.joining("\n")));
		}
		return sb.toString();
	}
}
