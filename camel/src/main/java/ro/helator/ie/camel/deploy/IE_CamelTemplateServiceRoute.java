package ro.helator.ie.camel.deploy;

import org.apache.camel.builder.RouteBuilder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.helator.ie.camel.IE_CamelActivator;
import ro.helator.ie.camel.interfaces.TemplateRegistryService;
import ro.helator.ie.camel.json.views.RouteTemplateView;
import ro.helator.ie.camel.templates.IE_Camel_RouteTemplate;

@Component(name = IE_CamelTemplateServiceRoute.COMPONENT_NAME)
public class IE_CamelTemplateServiceRoute extends RouteBuilder{

	private static final Logger log = LoggerFactory.getLogger(IE_CamelTemplateServiceRoute.class);
			
	public static final String COMPONENT_NAME = "IE_CamelTemplateServiceRoute";
	public static final String COMPONENT_LABEL = "IE Camel Template Service Route";
	
	private static final String QUEUE_PREFIX = "SYSTEM.TEMPLATE.LIST";
	
	private TemplateRegistryService templateReg;
	
	@Activate
	private void activate() {
		log.info("Activating component " + COMPONENT_LABEL);
		try {
			IE_CamelActivator.getCamelContext().addRoutes(this);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Deactivate
	public void deactivate() {
		log.info("Deactivating the " + COMPONENT_LABEL);
		try {
			IE_CamelActivator.getCamelContext().stopRoute(COMPONENT_LABEL);
			IE_CamelActivator.getCamelContext().removeRoute(COMPONENT_LABEL);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@Reference
	public void setTemplateReg(final TemplateRegistryService templateReg) {
		this.templateReg = templateReg;
	}

	public void unsetTemplateReg(final TemplateRegistryService templateReg) {
		this.templateReg = templateReg;
	}
	
	@Override
	public void configure() throws Exception {
		
		from("activemq:queue:" + QUEUE_PREFIX + ".IN")
			.routeId(COMPONENT_LABEL)
			.bean(templateReg, "getTemplate(jmsToHtml)")
			.marshal().jacksonxml(IE_Camel_RouteTemplate.class, RouteTemplateView.class).to("activemq:queue:" + QUEUE_PREFIX + ".OUT");
		
	}

}