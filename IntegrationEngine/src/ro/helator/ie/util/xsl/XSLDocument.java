package ro.helator.ie.util.xsl;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XSLDocument {

	private String fileName;
	private Document doc;
	private Document inDoc, outDoc;
	private List<String> inNamespaces;
	private List<String> outNamespaces;
	private Element inElement, outElement;
	
	private XSLDocument(String fileName) throws ParserConfigurationException{
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		this.fileName = fileName;
	}
	
	public XSLDocument(String fileName, Document inDoc, Document outDoc) throws ParserConfigurationException{
		this(fileName);
		this.inDoc = inDoc;
		this.outDoc = outDoc;
	}
	
	public Document getInDoc(){
		return inDoc;
	}
	
	public Document getOutDoc(){
		return outDoc;
	}
	
	public void setInElement(Element element){
		this.inElement = element;
	}
	
	public void setOutElement(Element element){
		this.outElement = element;
	}
}
