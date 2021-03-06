package ro.helator.ie.camel.templates.test;

import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.helator.ie.camel.interfaces.TemplateScanerService;

@Component(name = "IE_CamelTemplatesTestActivator")
public class IE_CamelTemplatesTestActivator {

	private static final Logger log = LoggerFactory.getLogger(IE_CamelTemplatesTestActivator.class);

	private TemplateScanerService templateScaner;
	@Activate
	public void activate() {
		log.info("Helator's Integration Engine - Camel Templates Test bundle has started!");
		templateScaner.register(FrameworkUtil.getBundle(IE_CamelTemplatesTestActivator.class),
				this.getClass().getClassLoader());
//		InputStream is = this.getClass().getClassLoader().getResourceAsStream("templates/routes/jmsToHttp/multipart/template.xml");
//		log.info(" ads  = " + (is != null));
	}

	@Deactivate
	public void deactivate() {
		log.info("Helator's Integration Engine - Camel Templates Test bundle has been stoped!");
		templateScaner.unregister(FrameworkUtil.getBundle(IE_CamelTemplatesTestActivator.class),
				this.getClass().getClassLoader());
	}

	@Reference
	public void setTemplateScaner(final TemplateScanerService templateScaner) {
		this.templateScaner = templateScaner;
	}

	public void unsetTemplateScaner(final TemplateScanerService templateScaner) {
		this.templateScaner = templateScaner;
	}
}
