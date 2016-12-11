package ro.helator.ie.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WSDLUtil {

	public static final String WSDL_EXTENSION = "?wsdl";
	public static final String WSDL_TMP_FILE = "./tmp/temp.wsdl";
	public static final String WSDL_FOLDER = "./wsdl/?/";
	public static final String WSDL_FILENAME = "WebServiceDefinition.wsdl";
	public static final String WSDL_SCHEMANAME = "schema_?.xsd";
	
	public static void getWsdlAndSave(String URL) throws MalformedURLException, IOException, ParserConfigurationException{
		String wsdl = getWsdlAsString(URL);
		Document doc = FileUtil.convertStringToDocument(liniarizeWSDL(wsdl));
		String folder = WSDL_FOLDER.replace("?", getWsdlName(URL));
		FileUtil.writeFile(new File(folder + WSDL_FILENAME), FileUtil.convertDocumentToString(doc, false, false));
		
		List<String> schemasLocation = getExternalSchemasLocations(doc, URL);
		for(String schemaLocation : schemasLocation){
			XSDUtil.getXsdAndSave(schemaLocation, folder);
		}
	}
	
	public static String getWsdlAsString(String  URL) throws MalformedURLException, IOException{
		
		URL url = new URL(formatURL(URL));
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		
		File file = new File(WSDL_TMP_FILE);
		FileUtil.createNewFile(file);
		FileUtil.writeToFileFromInputStream(in, file);
		
		String content = FileUtil.readFile(file);
		file.delete();
		return content;
	}
	
	private static String formatURL(String  URL){
		String formatedUrl = URL;
		if(!URL.toLowerCase().endsWith(WSDL_EXTENSION)){
			formatedUrl += WSDL_EXTENSION;
		}
		return formatedUrl;
	}
	
	public static List<String> getExternalSchemasLocations(Document doc, String url) throws ParserConfigurationException{
		Element root = doc.getDocumentElement();
		return getExternalSchemasLocations(root, url);
	}
	
	public static List<String> getExternalSchemasLocations(Element root, String url) throws ParserConfigurationException{
		List<String> schemas = new ArrayList<String>();
		getExternalSchemasLocations(root, schemas, url);
		return schemas;
	}
	
	private static void getExternalSchemasLocations(Element root, List<String> schemas, String url) throws ParserConfigurationException{
		NodeList nodes = root.getChildNodes();
		int size = nodes.getLength();
		for(int i = 0; i < size; i++){
			Node node = nodes.item(i);
			String name = node.getNodeName();
			if(name != null){
				String[] arr = name.split(":");
				String localName = arr[arr.length-1];
				if(localName.equals("import")){
					String location = DocumentUtil.getAttributeValue(node, "schemaLocation");
					schemas.add(XSDUtil.createLocation(url, location));
				} else if(node instanceof Element){
					getExternalSchemasLocations((Element) node, schemas, url);
				}
			}
		}
	}
	
	public static String liniarizeWSDL(String wsdl){
		return wsdl.replaceAll("> +<", "><");
	}
	
	public static String getWsdlName(String URL){
		int index = URL.lastIndexOf("/");
		return URL.substring(index).replace(WSDL_EXTENSION, "")
				.replace(WSDL_EXTENSION.toUpperCase(), "");
	}
}
