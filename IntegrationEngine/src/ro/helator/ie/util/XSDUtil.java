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
	public static final String XSD_FOLDER = "./xsd/?/";
	public static final String XSD_FILENAME = "XmlSchemaDefinition.xsd";
	
	public static void getXsdAndSave(String URL) throws IOException{
		String xsd = getXsdAsString(URL);
		Document doc = FileUtil.convertStringToDocument(liniarizeXSD(xsd));
		
		String folder = XSD_FOLDER.replace("?", getXsdName(URL));
		FileUtil.writeFile(new File(folder + XSD_FILENAME), FileUtil.convertDocumentToString(doc, true, true));
	}
	
	public static void getXsdAndSave(String URL, String dir) throws IOException{
		String xsd = getXsdAsString(URL);
		Document doc = FileUtil.convertStringToDocument(liniarizeXSD(xsd));
		
		FileUtil.writeFile(new File(dir + getXsdName(URL)), FileUtil.convertDocumentToString(doc, true, true));
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
	
	public static String createLocation(String url, String location){
		StringBuilder sb = new StringBuilder();
		if(!url.endsWith(GeneralStringConstants.FW_SLASH)){
			int index = url.lastIndexOf(GeneralStringConstants.FW_SLASH);
			sb.append(url.substring(0, index));
		} else {
			sb.append(url);
		}
		
		int indexL = 0;
		if(location.startsWith(GeneralStringConstants.ROOT_FOLDER)){
			indexL = 2;
		} else if (location.startsWith(GeneralStringConstants.FW_SLASH)){
			indexL = 1;
		}
		sb.append(location.substring(indexL));
		return sb.toString();
	}
	
	public static String getXsdName(String URL){
		int index = URL.lastIndexOf("/");
		return URL.substring(index);
	}
}
