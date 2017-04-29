package ro.helator.ie.camel;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(name = MyServiceImpl2Component.COMPONENT_NAME)
public class MyServiceImpl2Component {

	private static final Logger log = LoggerFactory.getLogger(MyServiceImpl2Component.class);
	
	public static final String COMPONENT_NAME = "MyServiceImpl2Component";
	 
    public static final String COMPONENT_LABEL = "My Service Impl2 Component";
    
	private MyService2 service;
	
	@Activate
	private void activate(){
		log.info("Activating component " + COMPONENT_LABEL);
		service.echo();
	}
	
	@Deactivate
    public void deactivate() {
        log.info("Deactivating the " + COMPONENT_LABEL);
    }
	
	@Reference
    public void setMyService2(final MyService2 service) {
        this.service = service;
    }
 
    public void unsetMyService2(final MyService2 service) {
        this.service = null;
    }
}
