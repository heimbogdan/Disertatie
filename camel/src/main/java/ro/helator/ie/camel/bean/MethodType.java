package ro.helator.ie.camel.bean;

import java.util.Map;

public class MethodType {

	private String name;
	private Map<String, String> parameters;
	
	public MethodType() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MethodType(String name, Map<String, String> parameters) {
		super();
		this.name = name;
		this.parameters = parameters;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	
}
