package ro.helator.dia.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ro.helator.dia.entity.Template;

public class RouteTemplateParser {

	
	public static synchronized List<Template> parseResponse(String response) {
		List<Template> list = new ArrayList<Template>();
		
		Document doc = Util.convertStringToDocument(response);
		
		if(doc != null){
			Element root = doc.getDocumentElement();
			if(root != null && "ArrayList".equals(root.getNodeName())){
				NodeList templates = root.getChildNodes();
				int size = templates.getLength();
				for(int i = 0; i < size; i++){
					Node item = templates.item(i);
					String type = item.getFirstChild().getTextContent();
					Node mainProp = ((Element) item).getElementsByTagName("mainProp").item(0);
					
					NodeList subitems = ((Element) item).getElementsByTagName("subtypes").item(0).getChildNodes();
					int subSize = subitems.getLength();
					for(int j = 0; j < subSize; j++){
						Node subtype = subitems.item(j);
						Node tokens = ((Element) subtype).getElementsByTagName("tokenProp").item(0).getFirstChild();
						String[] tokenArr = tokens.getTextContent().split(",");
						Properties prop = new Properties();
						for(String token : tokenArr){
							Node tokenProp = ((Element) mainProp).getElementsByTagName(token).item(0);
							String[] tokenPropNames = tokenProp.getTextContent().split(",");
							for(String name : tokenPropNames){
								prop.setProperty(name, "");
							}
						}
						Template t = new Template(type, subtype.getNodeName(), 0, prop);
						list.add(t);
					}
				}
			}
		}
		
		return list;
	}
	
	public static synchronized List<Template> parseResponse(byte[] response) {
		
		return parseResponse(new String(response));
	}
}
