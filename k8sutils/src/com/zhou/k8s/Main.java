package com.zhou.k8s;

import com.zhou.k8s.template.Template;
import com.zhou.k8s.utils.FileUtils;
import com.zhou.k8s.utils.StringUtils;

public class Main {

	public static void main(String[] args) {
		var ns = namespaceKubeLogging();
		FileUtils.write("elk-test/namespace.yml", ns);
		var el = elasticSearch();
		FileUtils.write("elk-test/elasticSearch.yml", el);
		var fb = filebeat();
		FileUtils.write("elk-test/filebeat.yml", fb);
		var ls = logstash();
		FileUtils.write("elk-test/logstash.yml", ls);
		var ki = kibana();
		FileUtils.write("elk-test/kibana.yml", ki);
	}
	
	static String logstash() {
		return Template
				.buidler()
				.configMap(configMap -> configMap
										.name("logstash-config")
										.namespace("kube-logging")
										.label("k8s-app", "logstash")
										.data(data -> data
													  .child("logstash.conf", "|-")
													  // or for read from file
													  .add("input {\r\n"
													  		+ "        beats {\r\n"
													  		+ "            port => 5044\r\n"
													  		+ "        }\r\n"
													  		+ "    }\r\n"
													  		+ "    filter {\r\n"
													  		+ "        if [input][type] == \"log\" {\r\n"
													  		+ "            grok {\r\n"
													  		+ "                match => { \"message\" => \"%{TIMESTAMP_ISO8601:timestamp}%{SPACE}%{LOGLEVEL:logLevel}%{SPACE}%{NUMBER:pid}%{SPACE}---%{SPACE}%{SYSLOG5424SD:appName}%{SPACE}%{SYSLOG5424SD:threadName}%{SPACE}%{NOTSPACE:loggerName}%{SPACE}:%{SPACE}%{GREEDYDATA:message}\" }\r\n"
													  		+ "                overwrite => [\"message\"]\r\n"
													  		+ "            }\r\n"
													  		+ "            date {\r\n"
													  		+ "                match => [\"timestamp\", \"ISO8601\"]\r\n"
													  		+ "            }\r\n"
													  		+ "        }\r\n"
													  		+ "    }\r\n"
													  		+ "    output {\r\n"
													  		+ "        elasticsearch {\r\n"
													  		+ "            hosts => [\"http://es-service:9200\"]\r\n"
													  		+ "            codec => json\r\n"
													  		+ "            index => \"logstash-%{+YYYY.MM.dd}\"\r\n"
													  		+ "        }\r\n"
													  		+ "    }")
													  .done()
													  .child("logstash.yml", "|-")
													  .add("xpack.monitoring.enabled", "true")
													  .add("http.host", StringUtils.getString("0.0.0.0"))
													  .add("xpack.monitoring.elasticsearch.hosts", "http://es-service:9200")
													  .add("pipeline.ecs_compatibility", "disabled")
												)
						)
				.service(service -> service
									.name("logstash-service")
									.namespace("kube-logging")
									.clusterIP("None")
									.port(5044, "TCP", "db")
									.selector("k8s-app", "logstash")
						)
				.deploy(deploy -> deploy
								  .name("logstash-deploy")
								  .namespace("kube-logging")
								  .selector("k8s-app", "logstash")
								  .template(template -> template
										  				.label("k8s-app", "logstash")
										  				.container(container -> container
										  										.name("logstash")
										  										.image("docker.elastic.co/logstash/logstash:8.14.3")
										  										.port(5044, "beats")
										  										.env("XPACK_MONITORING_ELASTICSEARCH_HOSTS", "http://es-service:9200")
										  										.command("logstash", "-f", "/etc/logstash_c/logstash.conf")
										  										.resources(resources -> resources
										  																.requests("500m", "512Mi")
										  																.limit("1000m", "1Gi")
										  												)
										  										.mount("config-volume", "/etc/logstash_c/")
										  										.mount("config-yml-volume", "/usr/share/logstash/config/")
										  						)
										  				.configMap("config-volume", "logstash-config", item -> item
										  																	   .key("logstash.conf")
										  																	   .path("logstash.conf")
										  						)
										  				.configMap("config-yml-volume", "logstash-config", item -> item
										  																		   .key("logstash.yml")
										  																		   .path("logstash.yml")
																)
										  )
						)
				.build();
	}
	
	static String kibana() {
		return Template
				.buidler()
				.configMap(configMap -> configMap
										.name("kibana-config")
										.namespace("kube-logging")
										.label("k8s-app", "kibana")
										.data(data -> data
													  .child("kibana.yml", "|-")
													  .add("server.name", "kibana")
													  .add("server.host", StringUtils.getString("0.0.0.0"))
													  .add("i18n.locale", "zh-CN")
													  .add("xpack.reporting.roles.enabled", "false")
													  .child("elasticsearch")
													  .add("hosts", "${ELASTICSEARCH_HOSTS}")
												)
						)
				.service(service -> service
									.name("kibana-service")
									.namespace("kube-logging")
									.label("k8s-app", "kibana")
									.type("NodePort")
									.port(5601, "ui")
									.selector("k8s-app", "kibana")
						)
				.ingress(ingress -> ingress
									.name("kibana-ingress")
									.namespace("kube-logging")
									.ingressClassName("nginx")
									.rule(
											"kibana.zhou.com",
											path -> path
												  	.path("/")
												  	.pathType("Prefix")
												  	.serviceName("kibana-service")
												  	.portNumber(5601)
											)
						)
				.deploy(deploy -> deploy
								 .name("kibana-deploy")
								 .namespace("kube-logging")
								 .label("k8s-app", "kibana")
								 .replicas(1)
								 .selector("k8s-app", "kibana")
								 .template(template -> template
										 			   .label("k8s-app", "kibana")
										 			   .container(container -> container
										 					   				   .name("kibana")
										 					   				   .image("docker.elastic.co/kibana/kibana:8.14.3")
										 					   				   .port(5601, "ui")
										 					   				   .env("ELASTICSEARCH_HOSTS", "http://es-service:9200")
										 					   				   .resources(resources -> resources
										 					   						   				   .requests("500m", "512Mi")
										 					   						   				   .limit("1000m", "1Gi")
										 					   						   )
										 					   				   .mount("config", "/usr/share/kibana/config/kibana.yml", "kibana.yml", true)
										 					   )
										 			   .configMap("config", "kibana-config")
										 )
						)
				.build();
	}
	
	static String filebeat() {
		return Template
				.buidler()
				.configMap(configMap -> configMap
										.name("filebeat-config")
										.namespace("kube-logging")
										.label("k8s-app", "elasticsearch")
										.data(data -> {
											var data1 = data.child("filebeat.yml", "|-");
											
											  data1.child("filebeat.inputs")
											  .addFirst("type", "log")
											  .add("enable", "true")
											  .child("paths")
											  .addFirst("/var/log/test/*.log")
											  .done()
											  .child("processors")
											  .childFirst("add_kubernetes_metadata")
											  .add("host", "${NODE_NAME}")
											  .child("matchers")
											  .childFirst("logs_path")
											  .add("logs_path", "/var/log/test/");
											  
											  data1.child("output.logstash")
											  .child("hosts")
											  .addFirst("logstash-service:5044")
											  .done()
											  .add("enable", true);
											  
											  return data;
										}
												)
						)
				.serviceAccount(serviceAccount -> serviceAccount
									.name("filebeat-service-account")
									.namespace("kube-logging")
									.label("k8s-app", "filebeat")
						)
				.clusterRole(clusterRole -> clusterRole
											.name("filebeat-cluster-role")
											.label("k8s-app", "filebeat")
											.rule(rule -> rule
														  .apiGroups("")
														  .resources("namespaces", "pods", "nodes")
														  .verbs("get", "watch", "list")
													)
						)
				.clusterRoleBinding(clusterRoleBinding -> clusterRoleBinding
														  .name("filebeat-cluster-role-binding")
														  .namespace("kube-logging")
														  .subject("ServiceAccount", "filebeat-service-account", "kube-logging", "")
														  .roleRef("ClusterRole", "filebeat-cluster-role", "")
						)
				.daemonset(daemonset -> daemonset
										.name("filebeat-daemonset")
										.namespace("kube-logging")
										.label("k8s-app", "filebeat")
										.selector("k8s-app", "filebeat")
										.template(template -> template
															  .label("k8s-app", "filebeat")
															  .serviceAccountName("filebeat-service-account")
															  .terminationGracePeriodSeconds(30)
															  .container(container -> container
																	  				  .name("filebeat")
																	  				  .image("docker.elastic.co/beats/filebeat:8.14.3")
																	  				  .envField("NODE_NAME", "spec.nodeName")
																	  				  .resources(resources -> resources
																	  						  				  .requests("500m", "100Mi")
																	  						  				  .limit("1000m", "512Mi")
																	  						  )
																	  				  .securityContext("runAsUser", "0")
																	  				  .args("-c", "/etc/config/filebeat.yml", "-e")
																	  				  .mount("config", "/etc/config/")
																	  				  .mount("data", "/usr/share/filebeat/data/")
																	  				  .mount("varlog", "/var/log/test/")
																	  )
															  .configMap("config", "filebeat-config", "0600")
															  .hostPath("varlog", "/var/log/test/")
															  .hostPath("data", "/data/filebeat-data/", "DirectoryOrCreate")
												)
						)
				.build();
	}
	
	static String elasticSearch() {
		return Template
				.buidler()
				.service(service -> service
									.name("es-service")
									.namespace("kube-logging")
									.label("k8s-app", "elasticsearch")
									.port(9200, "TCP", "db")
									.selector("k8s-app", "elasticsearch")
						)
				.serviceAccount(serviceAccount -> serviceAccount
												  .name("es-service-account")
												  .namespace("kube-logging")
												  .label("k8s-app", "elasticsearch")
						)
				.clusterRole(clusterRole -> clusterRole
											.name("es-cluster-role")
											.label("k8s-app", "elasticsearch")
											.rule(rule -> rule
														  .apiGroups("")
														  .resources("services", "namesapces", "endpoints")
														  .verbs("get")
													)
						)
				.clusterRoleBinding(clusterRoleBinding -> clusterRoleBinding
														  .name("es-cluster-role-binding")
														  .namespace("kube-logging")
														  .subject("ServiceAccount", "es-service-account", "kube-logging", "")
														  .roleRef("ClusterRole", "es-cluster-role", "")
						)
				.statefulset(statefulset -> statefulset
											.name("es-statefulset")
											.namespace("kube-logging")
											.label("k8s-app", "elasticsearch")
											.serviceName("es-service")
											.replicas(1)
											.selector("k8s-app", "elasticsearch")
											.template(template -> template
																  .label("k8s-app", "elasticsearch")
																  .serviceAccountName("es-service-account")
																  .nodeSelector("es", "data")
																  .container(container -> container
																		  				  .name("elasticsearch")
																		  				  .image("docker.elastic.co/elasticsearch/elasticsearch:8.14.3")
																		  				  .port(9200, "db")
																		  				  .port(9300, "transport")
																		  				  .envField("NAMESAPCE", "metadata.namespace")
																		  				  .env("xpack.security.enabled", "false")
																		  				  .env("discovery.type", "single-node")
																		  				  .env("ES_JAVA_OPTS", "-Xms512m -Xmx1g")
																		  				  .resources(resources -> resources
																		  						  				  .requests("500m", "512Mi")
																		  						  				  .limit("1000m", "1Gi")
																		  						  )
																		  				  .mount("es-volume", "/usr/share/elasticsearch/data/")
														  				  )
																  .hostPath("es-volume", "/data/es")
													)
						)
				.build();
		
	}
	
	static String namespaceKubeLogging() {
		return Template.buidler()
				.namespace(namespace -> namespace
										.name("kube-logging")
						)
				.build();
	}
}
