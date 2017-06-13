package ro.helator.ie.camel.deploy;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Route;
import org.apache.camel.builder.RouteBuilder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.helator.ie.camel.IE_CamelActivator;

@Component(name = IE_CamelRoutesServiceRoute.COMPONENT_NAME, immediate=true)
public class IE_CamelRoutesServiceRoute extends RouteBuilder {

	private static final Logger log = LoggerFactory.getLogger(IE_CamelRoutesServiceRoute.class);
	
	public static final String COMPONENT_NAME = "IE_CamelRoutesServiceRoute";
	public static final String COMPONENT_LABEL = "IE Camel Routes Service Route";
	
	private static final String QUEUE_PREFIX = "SYSTEM.ROUTE.LIST";
	
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
			IE_CamelActivator.getCamelContext().stopRoute("IE Camel Routes Listener");
			IE_CamelActivator.getCamelContext().removeRoute("IE Camel Routes Listener");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@Override
	public void configure() throws Exception {

		from("activemq:queue:" + QUEUE_PREFIX + ".IN").routeId(COMPONENT_LABEL)
			.to("activemq:queue:" + QUEUE_PREFIX + ".CPY")
			.doTry()
				.process(new Processor() {
					
					@Override
					public void process(Exchange arg0) throws Exception {
						CamelContext ctx = IE_CamelActivator.getCamelContext();
						List<Route> list = ctx.getRoutes();
						StringBuilder sb = new StringBuilder("<routes>");
						for(Route r : list){
							sb.append("<route>");
							sb.append("<context>").append(ctx.getName()).append("</context>");
							sb.append("<id>").append(r.getId()).append("</id>");
							sb.append("<endpoint><![CDATA[ ").append(r.getEndpoint().getEndpointUri()).append(" ]]></endpoint>");
							sb.append("<state>").append(ctx.getRouteStatus(r.getId()).toString()).append("</state>");
							sb.append("<uptime>").append(r.getUptime()).append("</uptime>");
							sb.append("</route>");
						}
						sb.append("</routes>");
						arg0.getIn().setBody(sb.toString(), String.class);
					}
				})
				.convertBodyTo(String.class)
				.log("${body}").to("activemq:queue:" + QUEUE_PREFIX + ".OUT")
			.doCatch(Exception.class)
			.to("activemq:queue:" + QUEUE_PREFIX + ".ERR").end();
	}

}
