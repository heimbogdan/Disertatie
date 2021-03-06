package ro.helator.ie.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XSDUtil {

	public static final String XSD_EXTENSION = ".xsd";
	public static final String XSD_TMP_FILE = "./tmp/temp.xsd";
	public static final String XSD_FOLDER = "./xsd/?/";
	public static final String XSD_FILENAME = "XmlSchemaDefinition.xsd";
	public static final String XSD_SCHEMANAME = "schema.xsd";
	public static final String XML_NAMESPACE = "xmlns";
	public static final String XSD_ELEMENT = "element";
	public static final String XSD_COMPLEXTYPE = "complexType";
	public static final String XSD_ATRIBUTE_NAME = "name";

	public static void getXsdAndSave(String URL) throws IOException, ParserConfigurationException {
		String xsd = FileUtil.getFileAsString(URL);
		Document doc = FileUtil.convertStringToDocument(liniarizeXSD(xsd));

		String folder = XSD_FOLDER.replace("?", getXsdName(URL));
		FileUtil.writeFile(new File(folder + XSD_FILENAME), FileUtil.convertDocumentToString(doc, true, true));

		List<String> schemasLocation = getExternalSchemasLocations(doc, URL, XSD_SCHEMANAME);
		for (String schemaLocation : schemasLocation) {
			getXsdAndSave(schemaLocation, folder, XSD_SCHEMANAME);
		}
	}

	public static void getXsdAndSave(String URL, String dir) throws IOException, ParserConfigurationException {
		String xsd = FileUtil.getFileAsString(URL);
		Document doc = FileUtil.convertStringToDocument(liniarizeXSD(xsd));

		FileUtil.writeFile(new File(dir + getXsdName(URL)), FileUtil.convertDocumentToString(doc, true, true));
		List<String> schemasLocation = getExternalSchemasLocations(doc, URL, XSD_SCHEMANAME);
		for (String schemaLocation : schemasLocation) {
			getXsdAndSave(schemaLocation, dir, XSD_SCHEMANAME);
		}
	}

	public static void getXsdAndSave(String URL, String dir, String name)
			throws IOException, ParserConfigurationException {
		String xsd = FileUtil.getFileAsString(URL);
		Document doc = FileUtil.convertStringToDocument(liniarizeXSD(xsd));
		List<String> schemasLocation = getExternalSchemasLocations(doc, URL, name);
		FileUtil.writeFile(new File(dir + name), FileUtil.convertDocumentToString(doc, true, true));
		
		for (String schemaLocation : schemasLocation) {
			getXsdAndSave(schemaLocation, dir,
					name.replace(".xsd", "_" + (schemasLocation.indexOf(schemaLocation) + 1) + ".xsd"));
		}
	}

	// public static String getXsdAsString(String URL) throws IOException{
	// URL url = new URL(URL);
	// URLConnection connection = url.openConnection();
	// InputStream in = connection.getInputStream();
	//
	// File file = new File(XSD_TMP_FILE);
	// FileUtil.createNewFile(file);
	// FileUtil.writeToFileFromInputStream(in, file);
	//
	// String content = FileUtil.readFile(file);
	// file.delete();
	// return content;
	// }

	public static String liniarizeXSD(String wsdl) {
		return wsdl.replaceAll("> +<", "><").replaceAll(">\t+<", "><").replaceAll(">\n+<", "><");
	}

	public static String createLocation(String url, String location) {
		StringBuilder sb = new StringBuilder();
		if (!url.endsWith(GeneralStringConstants.FW_SLASH)) {
			int index = url.lastIndexOf(GeneralStringConstants.FW_SLASH);
			sb.append(url.substring(0, index + 1));
		} else {
			sb.append(url);
		}

		int indexL = 0;
		if (location.startsWith(sb.toString())) {
			indexL = sb.toString().length();
		} else if (location.startsWith(GeneralStringConstants.ROOT_FOLDER)) {
			indexL = 2;
		} else if (location.startsWith(GeneralStringConstants.FW_SLASH)) {
			indexL = 1;
		}
		sb.append(location.substring(indexL));
		return sb.toString();
	}

	public static String getXsdName(String URL) {
		int index = URL.lastIndexOf("/");
		return URL.substring(index);
	}

	public static List<String> getExternalSchemasLocations(Document doc, String url, String name)
			throws ParserConfigurationException {
		Element root = doc.getDocumentElement();
		return getExternalSchemasLocations(root, url, name);
	}

	public static List<String> getExternalSchemasLocations(Element root, String url, String name)
			throws ParserConfigurationException {
		List<String> schemas = new ArrayList<String>();
		getExternalSchemasLocations(root, schemas, url, name);
		return schemas;
	}

	private static void getExternalSchemasLocations(Element root, List<String> schemas, String url, String nameXSD)
			throws ParserConfigurationException {
		NodeList nodes = root.getChildNodes();
		int size = nodes.getLength();
		for (int i = 0; i < size; i++) {
			Node node = nodes.item(i);
			String name = node.getNodeName();
			if (name != null) {
				String[] arr = name.split(":");
				String localName = arr[arr.length - 1];
				if (localName.equals("import")) {
					String location = DocumentUtil.getAttributeValue(node, "schemaLocation");
					schemas.add(XSDUtil.createLocation(url, location));
					DocumentUtil.setAttributeValue(node, "schemaLocation",
							GeneralStringConstants.ROOT_FOLDER + nameXSD.replace(XSD_EXTENSION, 
									"_" + schemas.size() + XSD_EXTENSION));
				} else if (node instanceof Element) {
					getExternalSchemasLocations((Element) node, schemas, url, nameXSD);
				}
			}
		}
	}

	public static List<String> getListOfNamespaces(Document doc) {
		List<String> namespaces = new ArrayList<String>();

		return namespaces;
	}

	public static List<Element> getElementsAndComplexTypesFromXSD(Document doc) {
		List<Element> elements = new ArrayList<Element>();
		Element root = doc.getDocumentElement();
		getLocalNameByType(root, elements);
		return elements;
	}

	private static void getLocalNameByType(Element root, List<Element> list) {
		NodeList nodeList = root.getChildNodes();
		int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element
					&& (node.getLocalName().equals(XSD_ELEMENT) || node.getLocalName().equals(XSD_COMPLEXTYPE))) {
				list.add((Element) node);
			}
		}
	}

}
