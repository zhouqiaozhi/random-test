package com.zhou.k8s.template.clusterrole;

import com.zhou.k8s.template.Base;
import com.zhou.k8s.utils.StringUtils;

public class ClusterRoleBinding extends Base<ClusterRoleBinding> {
	public ClusterRoleBinding() {
		super("rbac.authorization.k8s.io/v1", "ClusterRoleBinding");
		wrapper.self(this);
	}
	
	public ClusterRoleBinding subject(String kind, String name, String namespace, String apiGroup) {
		var subjects = main.child("subjects");
		subjects.addFirst("kind", kind);
		subjects.add("name", name);
		subjects.add("namespace", namespace);
		subjects.add("apiGroup", StringUtils.getString(apiGroup));
		return this;
	}
	
	public ClusterRoleBinding roleRef(String kind, String name, String apiGroup) {
		var roleRef = main.child("roleRef");
		roleRef.add("kind", kind);
		roleRef.add("name", name);
		roleRef.add("apiGroup", StringUtils.getString(apiGroup));
		return this;
	}
}
