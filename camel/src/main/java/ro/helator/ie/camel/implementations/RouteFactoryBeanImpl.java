package ro.helator.ie.camel.implementations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.camel.CamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.helator.ie.camel.IE_CamelActivator;
import ro.helator.ie.camel.interfaces.RouteFactoryBean;
import ro.helator.ie.camel.interfaces.TemplateRegistryService;
import ro.helator.ie.camel.templates.IE_Camel_RouteTemplate;
import ro.helator.ie.camel.templates.IE_Camel_RouteTemplate_Subtype;
import ro.helator.ie.camel.templates.TemplatesUtil;

@Component(name = RouteFactoryBeanImpl.COMPONENT_NAME)
public class RouteFactoryBeanImpl implements RouteFactoryBean {

	private static final Logger log = LoggerFactory.getLogger(RouteFactoryBeanImpl.class);

	public static final String COMPONENT_NAME = "RouteFactoryBeanImpl";
	public static final String COMPONENT_LABEL = "Route Factory Bean Impl";

	private static final String ROUTE_TYPE = "route.type";
	private static final String ROUTE_SUBTYPE = "route.subtype";

	private TemplateRegistryService templateReg;

	@Activate
	private void activate() {
		log.info("Activating component " + COMPONENT_LABEL);
	}

	@Deactivate
	public void deactivate() {
		log.info("Deactivating the " + COMPONENT_LABEL);
	}

	@Reference
	public void setTemplateReg(final TemplateRegistryService templateReg) {
		this.templateReg = templateReg;
	}

	public void unsetTemplateReg(final TemplateRegistryService templateReg) {
		this.templateReg = null;
	}

	@Override
	public void buildRoute(String fileName, String fileContent) {
		if (log.isInfoEnabled()) {
			log.info("Build Route for file [" + fileName + "]");
		}

		CamelContext context = IE_CamelActivator.getCamelContext();
		log.info("\nContext: " + context);

		InputStream routePropertiesInputStream = new ByteArrayInputStream(fileContent.getBytes());
		Properties routeProperties = new Properties();
		try {
			log.info("Load route properties file: [" + fileName + "]");
			routeProperties.load(routePropertiesInputStream);
			routePropertiesInputStream.close();

			String routeType = routeProperties.getProperty(ROUTE_TYPE);
			String routeSubtype = routeProperties.getProperty(ROUTE_SUBTYPE);
			IE_Camel_RouteTemplate temp = templateReg.getTemplate(routeType);
			IE_Camel_RouteTemplate_Subtype subTemp = temp.getSubtypes().get(routeSubtype);

			String template = subTemp.getTemplateXML();

			log.info("Retrieve tokens...");
			List<String> tokens = TemplatesUtil.retrieveTokens(temp.getMainProp(), subTemp.getTokenProp());
			log.info("Tokents found: " + tokens.size());
			template = TemplatesUtil.templateTokenResolver(template, tokens, routeProperties);
			log.info("Final template:\n" + template);
			RoutesDefinition routes = context.loadRoutesDefinition(new ByteArrayInputStream(template.getBytes()));

			log.info(routes.toString());
			List<RouteDefinition> routeList = routes.getRoutes();
			if (routeList != null && !routeList.isEmpty()) {
				for (RouteDefinition route : routeList) {
					String routeId = route.getId();
					log.info("Route: " + routeId);
					RouteDefinition r = context.getRouteDefinition(routeId);
					if (r != null) {
						context.stopRoute(routeId);
						context.removeRouteDefinition(r);
					}
				}
				context.addRouteDefinitions(routes.getRoutes());
			} else {
				if (log.isInfoEnabled()) {
					log.info("No routes found in [" + fileName + "]");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
