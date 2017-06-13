package ro.helator.ie.camel.bean.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.Produce;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.helator.ie.camel.bean.BeanElement;
import ro.helator.ie.camel.bean.ParamDesc;
import ro.helator.ie.camel.interfaces.BeanRegistryService;

@Component(name = "IE_CamelBeanTest", immediate=true)
public class IE_CamelBeanTest implements BeanElement{

	private static final Logger log = LoggerFactory.getLogger(IE_CamelBeanTest.class);
	
	private BeanRegistryService beanRegistry;
	
	private String[] methods = {"getDate","formatDate"};
	
	@Activate
	public void activate() {
		log.info("Helator's Integration Engine - Camel Bean Test bundle has started!");
		beanRegistry.registerBean(this);
	}

	@Deactivate
	public void deactivate() {
		log.info("Helator's Integration Engine - Camel Bean Test bundle has been stoped!");
		beanRegistry.unregisterBean(this);
	}

	@Reference
	public void setBeanRegistry(final BeanRegistryService beanRegistry) {
		this.beanRegistry = beanRegistry;
	}

	public void unsetBeanRegistry(final BeanRegistryService beanRegistry) {
		this.beanRegistry = beanRegistry;
	}

	public String getBeanName() {
		return "IE_CamelBeanTest";
	}

	public String[] getMethods() {
		return methods;
	}
	
	
	public static String getDate(){
		return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
	}
	
	public static String formatDate(@ParamDesc(name="dateParam") Date dateParam, @ParamDesc(name="formatParam") String formatParam){
		return new SimpleDateFormat(formatParam).format(dateParam);
	}
}