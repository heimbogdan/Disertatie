package ro.helator.ie.camel.interfaces;

import ro.helator.ie.camel.templates.IE_Camel_RouteTemplate;

public interface TemplateRegistryService {

	public boolean registerTemplate(IE_Camel_RouteTemplate template);
	
	public boolean unregisterTemplate(IE_Camel_RouteTemplate template);
	
	public boolean unregisterTemplate(String key);
	
	public IE_Camel_RouteTemplate getTemplate(String key);
	
}
