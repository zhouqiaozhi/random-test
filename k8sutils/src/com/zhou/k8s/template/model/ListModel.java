package com.zhou.k8s.template.model;

public abstract class ListModel<T> extends Model {
	private T next;
	public T next() {
		return next(true);
	}
	
	public T next(boolean createNew) {
		return next != null ? next : (createNew ? (next = createNew()) : null);
	}
	public abstract T createNew();
}
