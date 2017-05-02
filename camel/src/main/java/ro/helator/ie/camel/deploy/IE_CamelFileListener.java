package ro.helator.ie.camel.deploy;

import org.apache.camel.builder.RouteBuilder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.helator.ie.camel.IE_CamelActivator;
import ro.helator.ie.camel.interfaces.RouteFactoryBean;

@Component(name = IE_CamelFileListener.COMPONENT_NAME)
public class IE_CamelFileListener extends RouteBuilder {

	private static final Logger log = LoggerFactory.getLogger(IE_CamelFileListener.class);

	public static final String COMPONENT_NAME = "IE_CamelFileListener";
	public static final String COMPONENT_LABEL = "IE_Camel File Listener";

	private static final String ROUTE_TYPE = "route.type";
	private static final String ROUTE_SUBTYPE = "route.subtype";

	private RouteFactoryBean routeFactory;

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
			IE_CamelActivator.getCamelContext().stopRoute("IE_Camel File Listener");
			IE_CamelActivator.getCamelContext().removeRoute("IE_Camel File Listener");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void configure() throws Exception {

		from("file://integrationEngine/camel/routes?" + "delay=10000" + "&recursive=true" + "&noop=true"
				+ "&idempotentKey=${file:name}-${file:modified}").routeId("IE_Camel File Listener")
						.log("Reading file [${file:name}]").bean(routeFactory, "buildRoute(${file:name},${body})");
	}

	@Reference
	public void setRouteFactory(final RouteFactoryBean routeFactory) {
		this.routeFactory = routeFactory;
	}

	public void unsetRouteFactory(final RouteFactoryBean routeFactory) {
		this.routeFactory = null;
	}

}
