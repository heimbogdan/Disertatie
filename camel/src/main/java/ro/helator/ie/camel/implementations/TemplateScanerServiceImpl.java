package ro.helator.ie.camel.implementations;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.helator.ie.camel.interfaces.TemplateRegistryService;
import ro.helator.ie.camel.interfaces.TemplateScanerService;
import ro.helator.ie.camel.templates.IE_Camel_RouteTemplate;
import ro.helator.ie.camel.templates.TemplateScanerUtil;

@Component(name = TemplateScanerServiceImpl.COMPONENT_NAME)
public class TemplateScanerServiceImpl implements TemplateScanerService {

	private static final Logger log = LoggerFactory.getLogger(TemplateScanerServiceImpl.class);

	public static final String COMPONENT_NAME = "IE_Camel_TemplateScanerService";
	public static final String COMPONENT_LABEL = "IE_Camel_TemplateScanerService";

	private TemplateRegistryService templateReg;

	@Activate
	private void activate() {
		log.info("Activating component " + COMPONENT_LABEL);
	}

	@Deactivate
	public void deactivate() {
		log.info("Deactivating the " + COMPONENT_LABEL);
	}

	@Override
	public void register(Bundle bundle, ClassLoader loader) {
		forEachTemplate(bundle, loader, t -> {
			log.info("Register template [" + t.getName() + "]");
			templateReg.registerTemplate(t);
		});
	}

	@Override
	public void unregister(Bundle bundle, ClassLoader loader) {
		forEachTemplate(bundle, loader, t -> {
			log.info("Unregister template [" + t.getName() + "]");
			templateReg.unregisterTemplate(t);
		});
	}

	private void forEachTemplate(Bundle bundle, ClassLoader loader, Consumer<IE_Camel_RouteTemplate> consumer) {
		log.info("Scan for templates...");
		try {
			Map<String, Object> map = TemplateScanerUtil.getParsedEntries(bundle, loader, "templates", "*");
			TemplateScanerUtil.printParsedEntries(map);
			List<IE_Camel_RouteTemplate> list = TemplateScanerUtil.extractRouteTemplates(map);
			log.info("Valid Templates:");
			list.forEach(t -> {
				log.info(t.formatForPrint());
			});
			list.forEach(consumer);
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

//	private String removeString(String in, String... strings) {
//		for (String string : strings) {
//			in = in.replace(string, "");
//		}
//		return in;
//	}
//
//	private boolean isFolder(String path) {
//		return path.endsWith("/");
//	}
}
