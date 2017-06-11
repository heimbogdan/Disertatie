package ro.helator.ie.camel.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.helator.ie.camel.bean.BeanElement;
import ro.helator.ie.camel.bean.BeanType;
import ro.helator.ie.camel.interfaces.BeanRegistryService;
import ro.helator.ie.camel.templates.IE_Camel_RouteTemplate;

@Component(name = BeanRegistryServiceImpl.COMPONENT_NAME)
public class BeanRegistryServiceImpl implements BeanRegistryService {

	private static final Logger log = LoggerFactory.getLogger(BeanRegistryServiceImpl.class);
	
	public static final String COMPONENT_NAME = "IE_Camel_BeanRegistryService";
	public static final String COMPONENT_LABEL = "IE_Camel_BeanRegistryService";
			
	private Map<String, BeanType> beanRegistry;
	
	@Activate
	private void activate() {
		log.info("Activating component " + COMPONENT_LABEL);
		beanRegistry = new HashMap<String, BeanType>();
	}
	
	@Deactivate
	public void deactivate() {
		log.info("Deactivating the " + COMPONENT_LABEL);
		beanRegistry.clear();
		beanRegistry = null;
	}
	
	@Override
	public boolean registerBean(BeanElement bean) {
		try {
			beanRegistry.put(bean.getBeanName(), new BeanType(bean));
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public boolean unregisterBean(BeanElement bean) {
		try {
			beanRegistry.remove(bean.getBeanName());
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return false;
	}

	@Override
	public BeanType getBean(String name) {
		return beanRegistry.get(name);
	}

	@Override
	public List<BeanType> getBeans(String name) {
		List<BeanType> list = new ArrayList<BeanType>();
		
		if(name == null){
			name = "";
		}
		
		Set<String> keys = beanRegistry.keySet();
		if(keys != null && !keys.isEmpty()){
			for(String key : keys){
				if(key.contains(name)){
					list.add(beanRegistry.get(key));
				}
			}
		}
		
		return list;
	}

}
