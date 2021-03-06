package ro.helator.dia.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ro.helator.dia.entity.Route;

public class RouteListParser {

	public static synchronized List<Route> parseResponse(String response) {
		List<Route> list = new ArrayList<Route>();
		
		Document doc = Util.convertStringToDocument(response);
		if(doc != null){
			Element root = doc.getDocumentElement();
			if(root != null && "routes".equals(root.getNodeName())){
				NodeList routes = root.getChildNodes();
				int size = routes.getLength();
				for(int i = 0; i < size; i++){
					Node route = routes.item(i);
					NodeList attrs = route.getChildNodes();
					int sizeA = attrs.getLength();
					
					Route r = new Route();
					for(int j = 0; j < sizeA; j++){
						Node attr = attrs.item(j);
						if("context".equals(attr.getNodeName())){
							r.setContext(attr.getTextContent());
						} else if("id".equals(attr.getNodeName())){
							r.setId(attr.getTextContent());
						} else if("endpoint".equals(attr.getNodeName())){
							r.setEndpoint(attr.getTextContent().replace("<![CDATA[ ", "").replace(" ]]>", ""));
						} else if("state".equals(attr.getNodeName())){
							r.setState(attr.getTextContent());
						} else if("uptime".equals(attr.getNodeName())){
							r.setUptime(attr.getTextContent());
						}
					}
					list.add(r);
				}
			}
		}
		return list;
	}
}
