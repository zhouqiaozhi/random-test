package com.zhou.k8s.template.container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.zhou.k8s.template.KeyValue;
import com.zhou.k8s.template.env.Env;
import com.zhou.k8s.template.env.EnvField;
import com.zhou.k8s.template.model.Model;
import com.zhou.k8s.template.model.Port;
import com.zhou.k8s.template.model.Resource;
import com.zhou.k8s.template.volume.VolumeMount;
import com.zhou.k8s.utils.StringUtils;

public class Container extends Model {
	private String name;
	private String image;
	private List<Port> ports;
	private List<Env> env;
	private Resource resources;
	private List<String> command;
	private List<String> args;
	private Map<String, String> securityContext;
	private List<VolumeMount> volumeMounts;

	public Container name(String name) {
		this.name = name;
		return this;
	}
	
	public Container image(String image) {
		this.image = image;
		return this;
	}
	
	public Container port(Integer containerPort, String name) {
		if(ports == null) ports = new ArrayList<>();
		var p = new Port();
		p.setContainerPort(containerPort);
		p.setName(name);
		ports.add(p);
		return this;
	}
	
	public Container envField(String name, String value) {
		if(env == null) env = new ArrayList<>();
		var e = new EnvField();
		e.setName(name);
		e.setValue(value);
		env.add(e);
		return this;
	}
	
	public Container env(String name, String value) {
		if(env == null) env = new ArrayList<>();
		var e = new Env();
		e.setName(name);
		e.setValue(value);
		env.add(e);
		return this;
	}
	
	public Container securityContext(String key, String value) {
		if(securityContext == null) securityContext = new HashMap<>();
		securityContext.put(key, value);
		return this;
	}
	
	public Container command(String... command) {
		this.command = Arrays.asList(command);
		return this;
	}
	
	public Container args(String... args) {
		this.args = Arrays.asList(args);
		return this;
	}
	
	
	
	public Container resources(Function<Resource, Resource> func) {
		resources = func.apply(new Resource());
		return this;
	}
	
	
	
	public Container mount(String name, String mountPath) {
		if(volumeMounts == null) volumeMounts = new ArrayList<>();
		var vm = new VolumeMount();
		vm.setName(name);
		vm.setMountPath(mountPath);
		volumeMounts.add(vm);
		return this;
	}
	
	public Container mount(String name, String mountPath, String subPath, Boolean readOnly) {
		if(volumeMounts == null) volumeMounts = new ArrayList<>();
		var vm = new VolumeMount();
		vm.setName(name);
		vm.setMountPath(mountPath);
		vm.setSubPath(subPath);
		vm.setReadOnly(readOnly);
		volumeMounts.add(vm);
		return this;
	}
	
	public void add(KeyValue containers) {
		containers.addFirst("name", name);
		containers.add("image", image);
		if(ports != null) {
			var ps = containers.child("ports");
			for(var p: ports) {
				p.add(ps);
			}
		}
		if(env != null) {
			var envs = containers.child("env");
			for(var e: env) {
				e.add(envs);
			}
		}
		if(command != null) {
			var commands = containers.child("command");
			for(var c: command) {
				commands.addFirst(StringUtils.getString(c));
			}
		}
		if(args != null) {
			var ags = containers.child("args");
			for(var a: args) {
				ags.addFirst(StringUtils.getString(a));
			}
		}
		if(securityContext != null) {
			var SC = containers.child("securityContext");
			for(var sc: securityContext.entrySet()) {
				SC.add(sc.getKey(), sc.getValue());
			}
		}
		if(resources != null) {
			resources.add(containers.child("resources"));
		}
		if(volumeMounts != null) {
			var vms = containers.child("volumeMounts");
			for(var vm: volumeMounts) {
				vm.add(vms);
			}
		}
	}
}
