package com.zhou.k8s.template.ingress;

import java.util.function.Function;

import com.zhou.k8s.template.Base;
import com.zhou.k8s.template.KeyValue;

public class Ingress extends Base<Ingress> {
	private KeyValue spec;
	private KeyValue rules;
	public Ingress() {
		super("networking.k8s.io/v1", "Ingress");
		wrapper.self(this);
		spec = main.child("spec");
	}
	
	public Ingress ingressClassName(String ingressClassName) {
		spec.child("ingressClassName", ingressClassName);
		return this;
	}
	
	public Ingress rule(String host, Function<IngressPath, IngressPath> func) {
		if(rules == null) rules = spec.child("rules");
		rules.addFirst("host", host);
		var paths = rules.child("http").child("paths");
		var path = new IngressPath();
		func.apply(path);
		while(path != null) {
			path.add(paths);
			path = path.next(false);
		}
		return this;
	}
//	spec:
//	  ingressClassName: nginx
//	  rules:
//	  - host: kibana.zhou.com
//	    http:
//	      paths:
//	      - path: /
//	        pathType: Prefix
//	        backend:
//	          service:
//	            name: kibana-service
//	            port: 
//	              number: 5601
	
}
