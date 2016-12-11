package test;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.xml.parsers.ParserConfigurationException;

import ro.helator.ie.util.WSDLUtil;

public class Main {

	public static void main(String[] args) {
		try {
			
			// to do 
			String url = "http://www.thomas-bayer.com/axis2/services/BLZService?wsdl";
//			String url = "http://www.webservicex.com/globalweather.asmx?WSDL";
			WSDLUtil.getWsdlAndSave(url, "BLZService");
			String wsdl = WSDLUtil.getWsdlAsString(url);
			System.out.println(wsdl);
//			
//			
//			Document doc = FileUtil.convertStringToDocument(WSDLUtil.liniarizeWSDL(wsdl));
//			List<Document> list = WSDLUtil.extractInternalSchemas(doc);
//			System.out.println(list.size());
//			for(Document schema : list){
//				System.out.println(FileUtil.convertDocumentToString(schema, true, true));
//			}
//			
//			String formatedWsdl = FileUtil.convertDocumentToString(doc, false, false);
//			System.out.println(formatedWsdl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}