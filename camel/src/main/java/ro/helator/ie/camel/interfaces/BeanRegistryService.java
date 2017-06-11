package ro.helator.ie.camel.interfaces;

import java.util.List;

import ro.helator.ie.camel.bean.BeanElement;
import ro.helator.ie.camel.bean.BeanType;

public interface BeanRegistryService {

	public boolean registerBean(BeanElement bean);
	
	public boolean unregisterBean(BeanElement bean);
	
	public BeanType getBean(String name);
	
	public List<BeanType> getBeans(String name);
}
