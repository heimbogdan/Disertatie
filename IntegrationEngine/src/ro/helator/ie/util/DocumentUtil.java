package ro.helator.ie.util;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DocumentUtil {

	
	public static void clearChilds(Node node){
		while(node.hasChildNodes()){
			node.removeChild(node.getFirstChild());
		}
	}
	
	public static void clearAttributes(Node node){
		while (node.getAttributes().getLength() > 0) {
		    Node att = node.getAttributes().item(0);
		    node.getAttributes().removeNamedItem(att.getNodeName());
		}
	}
	
	public static void addImportNode(Node parent, String namespace, String Location){
		Element imp = parent.getOwnerDocument().createElementNS("http://www.w3.org/2001/XMLSchema", "xsd:import");
		imp.setAttribute("namespace", namespace);
		imp.setAttribute("schemaLocation", Location);
		parent.appendChild(imp);
	}
	
	public static String getTargetNamespace(Node node){
		Node ns = node.getAttributes().getNamedItem("targetNamespace");
		String nsVal= "";
		if(ns != null){
			nsVal = ns.getNodeValue();
		}
		return nsVal;
	}
	
	public static String getAttributeValue(Node node, String attribute){
		return ((Element) node).getAttribute(attribute);
	}
	
	public static void setAttributeValue(Node node, String attribute, String value){
		((Element) node).setAttribute(attribute, value);
	}
}
