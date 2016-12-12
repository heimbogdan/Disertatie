package test;

import ro.helator.ie.util.FileUtil;
import ro.helator.ie.util.WSDLUtil;

public class Main {

	public static void main(String[] args) {
		try {
			
			// to do 
			String[] urls = new String[3];
			
			for(String url : urls){
				WSDLUtil.getWsdlAndSave(url);
				String wsdl = WSDLUtil.liniarizeWSDL(FileUtil.getFileAsString(WSDLUtil.formatURL(url)));
				System.out.println(wsdl);
			}
			
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
