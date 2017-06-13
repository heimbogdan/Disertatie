package ro.helator.dia.entity;

import java.util.List;

public class BeanElement {

	private String name;
	private List<String> methods;
	
	public BeanElement() {
		super();
	}

	public BeanElement(String name, List<String> methods) {
		super();
		this.name = name;
		this.methods = methods;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}
	
	
}
