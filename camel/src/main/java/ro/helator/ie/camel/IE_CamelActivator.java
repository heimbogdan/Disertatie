package ro.helator.ie.camel;

import java.util.List;

import javax.xml.XMLConstants;

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.core.osgi.OsgiCamelContextPublisher;
import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
import org.apache.camel.core.osgi.OsgiServiceRegistry;
import org.apache.camel.core.osgi.utils.BundleContextUtils;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IE_CamelActivator implements BundleActivator {

	private static final Logger log = LoggerFactory.getLogger(IE_CamelActivator.class);

	private static OsgiDefaultCamelContext ctx;

	@Override
	public void start(BundleContext context) throws Exception {

		log.info("Helator's Integration Engine - Camel bundle will be started...");
		log.info("Create camel context...");

		OsgiServiceRegistry reg = new OsgiServiceRegistry(context);
		ctx = new OsgiDefaultCamelContext(context, reg);
		ctx.setName("IE_Camel");
		ctx.getManagementStrategy()
				.addEventNotifier(new OsgiCamelContextPublisher(BundleContextUtils.getBundleContext(getClass())));
		ctx.start();
		log.info("Helator's Integration Engine - Camel bundle has started!");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		log.info("Helator's Integration Engine - Camel bundle will be stoped...");
		log.info("Getting existing routes...");
		List<Route> routes = ctx.getRoutes();
		for (Route route : routes) {
			String routeId = route.getId();
			ctx.stopRoute(routeId);
			ctx.removeRoute(routeId);
		}
		ctx.stop();
		log.info("Helator's Integration Engine - Camel bundle has been stoped!");
	}

//	private Map<String, String> getCamelContextProperties() {
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("", "");
//		return map;
//	}
	
	public static CamelContext getCamelContext(){
		return ctx;
	}

}
