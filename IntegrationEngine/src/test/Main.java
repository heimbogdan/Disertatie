package test;

import java.io.File;
import java.util.List;

import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ro.helator.ie.util.FileUtil;
import ro.helator.ie.util.WSDLUtil;
import ro.helator.ie.util.XSDUtil;
import ro.helator.ie.util.XSLUtil;
import ro.helator.ie.util.xsl.XSLDocument;

public class Main {

	public static void main(String[] args) {
		try {
			System.setProperty("javax.xml.parsers.DocumentBuilder",
		             "net.sf.saxon.om.DocumentBuilderImpl");
			
			// to do 
			String[] urls = new String[3];
			urls[0] = "http://iib-cluster.hq.edata.ro:7800/accountService";
			urls[1] = "http://iib-cluster.hq.edata.ro:7800/customerService";
			urls[2] = "http://iib-cluster.hq.edata.ro:7800/bancsMiscService";
			for(String url : urls){
				WSDLUtil.getWsdlAndSave(url);
				String wsdl = WSDLUtil.liniarizeWSDL(FileUtil.getFileAsString(WSDLUtil.formatURL(url)));
				System.out.println(wsdl);
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setExpandEntityReferences(true);
			Document doc = dbf.newDocumentBuilder().parse(new File("wsdl/accountService/schema_1.xsd"));
			List<Element> list = XSDUtil.getElementsAndComplexTypesFromXSD(doc);

			XSLDocument xsl = XSLUtil.createXSLDocument("temp.xsl", doc, doc);
			xsl.setInElement(list.get(0));
			xsl.setOutElement(list.get(1));
			
//			String url = "http://www.thomas-bayer.com/axis2/services/BLZService?wsdl";
//			String url = "http://www.webservicex.com/globalweather.asmx?WSDL";
//			WSDLUtil.getWsdlAndSave(url);
//			String wsdl = WSDLUtil.getWsdlAsString(url);
//			System.out.println(wsdl);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
