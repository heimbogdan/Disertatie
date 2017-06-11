package ro.helator.ie.camel.bean.test;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ro.helator.ie.camel.bean.BeanElement;
import ro.helator.ie.camel.bean.ParamDesc;
import ro.helator.ie.camel.interfaces.BeanRegistryService;

@Component(name = "IE_CamelTransformerBean", immediate = true)
public class IE_CamelTransformerBean implements BeanElement {

	private static final Logger log = LoggerFactory.getLogger(IE_CamelBeanTest.class);

	private BeanRegistryService beanRegistry;

	private String[] methods = { "transform" };

	private static final String xslRootFolder = "integrationEngine/camel/xsl/";
	private static final String xslExtension = ".xsl";

	@Activate
	public void activate() {
		log.info("Helator's Integration Engine - Camel Transformer Bean has started!");
		beanRegistry.registerBean(this);
	}

	@Deactivate
	public void deactivate() {
		log.info("Helator's Integration Engine - Camel Transformer Bean has been stoped!");
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

	public static String transform(@ParamDesc(name = "inXml", description = "Input message in XML format") String inXml,
			@ParamDesc(name = "xslName", description = "The name of the XSL file that will be used") String xslName) {

		File xsl = new File(xslRootFolder + xslName + xslExtension);
		if (xsl.exists()) {
			try {
				TransformerFactory factory = TransformerFactory.newInstance();
				Source xslt = new StreamSource(xsl);
				Transformer transformer = factory.newTransformer(xslt);
				
				Source xml = new StreamSource(new StringReader(inXml));
				StringWriter res = new StringWriter();
				StreamResult streamRes = new StreamResult(res);
				transformer.transform(xml, streamRes);
				
				return res.toString();
				
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				return new StringBuilder("<Fail><Error>").append(e.getMessage()).append("</Error><Trace>")
						.append(sw.toString()).append("</Trace></Fail>").toString();
			}
		}
		return inXml;
	}
}
