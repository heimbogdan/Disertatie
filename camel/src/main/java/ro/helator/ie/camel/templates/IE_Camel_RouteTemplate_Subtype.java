package ro.helator.ie.camel.templates;

import java.util.Properties;

public class IE_Camel_RouteTemplate_Subtype {

	private String parent;
	private String name;
	private String templateXML;
	private Properties tokenProp;

	public IE_Camel_RouteTemplate_Subtype(String parent, String name, String templateXML, Properties tokenProp){
			this.parent = parent;
			this.name = name;
			this.templateXML = templateXML;
			this.tokenProp = tokenProp;
		}

	public IE_Camel_RouteTemplate_Subtype(String parent, String name) {
			this.parent = parent;
			this.name = name;
			this.templateXML = TemplatesUtil.getTemplateFileInput(this.parent, this.name);
			this.tokenProp = TemplatesUtil.getSubtypeProperties(this.parent, this.name);
		}

	public String getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}

	public Properties getTokenProp() {
		return tokenProp;
	}

	public String getTemplateXML() {
		return templateXML;
	}

}
