package ro.helator.ie.util;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

import ro.helator.ie.util.xsl.XSLDocument;

public class XSLUtil {

	private static final String XSL_EXTENSION = ".xsl";
	
	public static XSLDocument createXSLDocument(String fileName, Document inDoc, Document outDoc) throws ParserConfigurationException{
		return new XSLDocument(fileName, inDoc, outDoc);
	}
}
