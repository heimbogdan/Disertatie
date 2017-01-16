package test;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import ro.helator.ie.util.FileUtil;
import ro.helator.ie.util.WSDLUtil;
import ro.helator.ie.util.XSDUtil;

public class Main {

	public static void main(String[] args) {
		try {
			
			// to do 
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document doc = dbf.newDocumentBuilder().parse(new File("wsdl/accountService/schema_1.xsd"));
			XSDUtil.getElementsFromXSD(doc);
			
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
