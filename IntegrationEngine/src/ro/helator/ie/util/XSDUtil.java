package ro.helator.ie.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.w3c.dom.Document;

public class XSDUtil {

	public static final String XSD_EXTENSION = ".xsd";
	public static final String XSD_TMP_FILE = "./tmp/temp.xsd";
	
	public static void getXsdAndSave(String URL) throws IOException{
		String xsd = getXsdAsString(URL);
		Document doc = FileUtil.convertStringToDocument(liniarizeXSD(xsd));
		
//		String folder = WSDL_FOLDER.replace("?", name);
//		FileUtil.writeFile(new File(folder + WSDL_FILENAME), FileUtil.convertDocumentToString(doc, false, false));
	}
	
	public static String getXsdAsString(String  URL) throws IOException{
		URL url = new URL(formatURL(URL));
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		
		File file = new File(XSD_TMP_FILE);
		FileUtil.createNewFile(file);
		FileUtil.writeToFileFromInputStream(in, file);
		
		String content = FileUtil.readFile(file);
		file.delete();
		return content;
	}
	
	private static String formatURL(String  URL){
		String formatedUrl = URL;
		if(!URL.toLowerCase().endsWith(XSD_EXTENSION)){
			formatedUrl += XSD_EXTENSION;
		}
		return formatedUrl;
	}
	
	public static String liniarizeXSD(String wsdl){
		return wsdl.replaceAll("> +<", "><");
	}
}
