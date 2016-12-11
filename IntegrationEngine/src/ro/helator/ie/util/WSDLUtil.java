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
	
	public static void getWsdlAndSave(String URL, String name) throws MalformedURLException, IOException, ParserConfigurationException{
		String wsdl = getWsdlAsString(URL);
		Document doc = FileUtil.convertStringToDocument(liniarizeWSDL(wsdl));
//		List<Document> list = extractExternalSchemas(doc);
		String folder = WSDL_FOLDER.replace("?", name);
		FileUtil.writeFile(new File(folder + WSDL_FILENAME), FileUtil.convertDocumentToString(doc, false, false));
//		for(Document schema : list){
//			FileUtil.writeFile(new File(folder + WSDL_SCHEMANAME.replace("?", (list.indexOf(schema) + 1) + "")),
//					FileUtil.convertDocumentToString(schema, true, true));
//		}
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
	
	public static List<Document> extractExternalSchemas(Document doc) throws ParserConfigurationException{
		Element root = doc.getDocumentElement();
		return extractExternalSchemas(root);
	}
	
	public static List<Document> extractExternalSchemas(Element root) throws ParserConfigurationException{
		List<Document> schemas = new ArrayList<Document>();
		extractExternalSchemas(root, schemas);
		return schemas;
	}
	
	private static void extractExternalSchemas(Element root, List<Document> schemas) throws ParserConfigurationException{
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
					
				} else if(node instanceof Element){
					extractExternalSchemas((Element) node, schemas);
				}
			}
		}
	}
	
	public static String liniarizeWSDL(String wsdl){
		return wsdl.replaceAll("> +<", "><");
	}
	
	
}
