package ro.helator.ie.camel.implementations;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.helator.ie.camel.interfaces.TemplateRegistryService;
import ro.helator.ie.camel.templates.IE_Camel_RouteTemplate;

@Component(name = TemplateRegistryServiceImpl.COMPONENT_NAME)
public class TemplateRegistryServiceImpl implements TemplateRegistryService {

	private static final Logger log = LoggerFactory.getLogger(TemplateRegistryServiceImpl.class);

	public static final String COMPONENT_NAME = "IE_Camel_TemplateRegistryService";
	public static final String COMPONENT_LABEL = "IE_Camel_TemplateRegistryService";

	private Map<String, IE_Camel_RouteTemplate> templateRegistry;

	@Activate
	private void activate() {
		log.info("Activating component " + COMPONENT_LABEL);
		templateRegistry = new HashMap<String, IE_Camel_RouteTemplate>();
	}

	@Deactivate
	public void deactivate() {
		log.info("Deactivating the " + COMPONENT_LABEL);
		templateRegistry = null;
	}

	@Override
	public boolean registerTemplate(IE_Camel_RouteTemplate template) {
		try {
			templateRegistry.put(template.getName(), template);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public boolean unregisterTemplate(IE_Camel_RouteTemplate template) {
		try {
			templateRegistry.remove(template.getName());
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public IE_Camel_RouteTemplate getTemplate(String key) {
		return templateRegistry.get(key);
	}
	
	@Override
	public boolean unregisterTemplate(String key) {
		IE_Camel_RouteTemplate temp = getTemplate(key);
		if(temp != null){
			return unregisterTemplate(temp);
		}
		return false;
	}
}