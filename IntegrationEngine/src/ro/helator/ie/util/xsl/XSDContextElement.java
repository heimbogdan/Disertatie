package ro.helator.ie.util.xsl;

public class XSDContextElement {

	private String name;
	private String type;
	
	public XSDContextElement(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "XSDContextElement [name=" + name + ", type=" + type + "]";
	}
	
	
}
