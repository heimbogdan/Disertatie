package ro.helator.ie.camel.interfaces;

import org.osgi.framework.Bundle;

public interface TemplateScanerService {

	public void register(Bundle bundle, ClassLoader loader);
	
	public void unregister(Bundle bundle, ClassLoader loader);
	
}
