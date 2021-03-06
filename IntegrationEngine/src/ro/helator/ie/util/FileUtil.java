package ro.helator.ie.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class FileUtil {

	public static String readFile(File file) throws IOException{
		StringBuilder sb = new StringBuilder();
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while((line = br.readLine()) != null){
			sb.append(line);
		}
		fr.close();
		br.close();
		return sb.toString();
	}
	
	public static String readFile(String filePath) throws IOException{
		return readFile(new File(filePath));
	}
	
	public static void writeToFileFromInputStream(InputStream in, File file) throws IOException{
		FileOutputStream fos = new FileOutputStream(file);
		byte[] buf = new byte[512];
		while (true) {
		    int len = in.read(buf);
		    if (len == -1) {
		        break;
		    }
		    fos.write(buf, 0, len);
		}
		in.close();
		fos.flush();
		fos.close();
	}
	
	public static String convertDocumentToString(Document doc, boolean xmlDeclaration, boolean standalone) {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = tf.newTransformer();
            // below code to remove XML declaration
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, xmlDeclaration ? "no" : "yes");
//            transformer.setOutputProperty(OutputKeys.STANDALONE, standalone ? "yes" : "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
         
        return null;
    }
	
	
	public static Document convertStringToDocument(String xmlStr) {
		
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try 
        {  
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
	
	
	
	public static Document convertNodeToDocumet(Node node) throws ParserConfigurationException{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		Node aux = node.cloneNode(true);
		String[] names = node.getNodeName().split(":");
		if(names.length > 1){
			String attr = "xmlns:" + names[0];
			((Element)aux).setAttribute(attr, node.getOwnerDocument().getDocumentElement().getAttribute(attr));
		}
		doc.adoptNode(aux);
		doc.appendChild(aux);
		return doc;
	}
	
	public static void writeFile(File file, String text) throws IOException{
		createNewFile(file);
		FileWriter fw = new FileWriter(file);
		fw.write(text);
		fw.close();
	}
	
	public static void createNewFile(File file) throws IOException{
		if(file.exists()){
			file.delete();
		}
		file.getParentFile().mkdirs();
		file.createNewFile();
	}
	
public static String getFileAsString(String  URL) throws MalformedURLException, IOException{
		
		URL url = new URL(URL);
		URLConnection connection = url.openConnection();
		InputStream in = connection.getInputStream();
		
		File file = new File(GeneralStringConstants.TEMP_FOLDER + UUID.randomUUID().toString());
		FileUtil.createNewFile(file);
		FileUtil.writeToFileFromInputStream(in, file);
		
		String content = FileUtil.readFile(file);
		file.delete();
		return content;
	}
}
