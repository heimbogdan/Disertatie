package ro.helator.ie.camel.templates;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateScanerUtil {

	private static final Logger log = LoggerFactory.getLogger(TemplateScanerUtil.class);

	private static final String TEMPLATE_FOLDER = "/templates/";
	private static final String _TEMPLATE = "template";
	private static final String _TOKENS = "tokens";
	private static final String _XML = ".xml";
	private static final String _PROPERTIES = ".properties";
	private static final String _ROUTES = "routes";
	private static final String _TEMPLATE_XML = _TEMPLATE + _XML;
	private static final String _TEMPLATE_PROPERTIES = _TEMPLATE + _PROPERTIES;
	private static final String _TOKENS_PROPERTIES = _TOKENS + _PROPERTIES;

	public static synchronized Enumeration<URL> getEntries(Bundle bundle, String path, String token) {
		Enumeration<URL> entries = bundle.findEntries(path, token, true);
		return entries;
	}

	public static synchronized Map<String, Object> getParsedEntries(Bundle bundle, ClassLoader loader, String path,
			String token) {
		return parseEntries(bundle, loader, getEntries(bundle, path, token));
	}

	public static synchronized List<String> entries2List(Enumeration<URL> entries) {
		List<String> entryList = new ArrayList<String>();
		while (entries.hasMoreElements()) {
			entryList.add(entries.nextElement().getFile().replace(TEMPLATE_FOLDER, ""));
		}
		return entryList;
	}

	@SuppressWarnings("unchecked")
	public static synchronized Map<String, Object> parseEntries(Bundle bundle, ClassLoader loader,
			Enumeration<URL> entries) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> entryList = entries2List(entries);
		for (String path : entryList) {
			Object entry = null;
			String[] parents = null;
			Map<String, Object> auxMap = map;
			if (isFolder(path)) {
				parents = extractParentsFromFolderPath(path);
				entry = new HashMap<String, Object>();
			} else {
				parents = extractParentsFromFilePath(path);
				entry = loader.getResourceAsStream(TEMPLATE_FOLDER + path);
			}

			int steps = parents.length;
			for (int i = 0; i < steps - 1; i++) {
				if (!auxMap.containsKey(parents[i])) {
					auxMap.put(parents[i], new HashMap<String, Object>());
				}
				Object aux = auxMap.get(parents[i]);
				auxMap = (Map<String, Object>) aux;
			}
			auxMap.put(parents[steps - 1], entry);
		}

		return map;
	}

	public static synchronized boolean isFolder(String path) {
		return path.endsWith("/");
	}

	public static synchronized String[] extractParentsFromFilePath(String path) {
		return path.split("/");
	}

	public static synchronized String[] extractParentsFromFolderPath(String path) {
		return extractParentsFromFilePath(path.substring(0, path.length() - 1));
	}

	public static synchronized void printParsedEntries(Map<String, Object> map) {
		printParsedEntries2(map, "");
	}

	@SuppressWarnings("unchecked")
	private static void printParsedEntries2(Map<String, Object> map, String prefix) {
		Object[] keys = map.keySet().toArray();
		int length = keys.length;
		for (int i = 0; i < length; i++) {
			String key = (String) keys[i];
			log.info(formatForPrint(key, prefix, i == (length - 1)));
			Object obj = map.get(key);
			if (obj instanceof Map<?, ?>) {
				printParsedEntries2((Map<String, Object>) obj, prefix + (i == (length - 1) ? "   " : "|  "));
			}
		}
	}

	private static String formatForPrint(String key, String prefix, boolean isLast) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix);
		sb.append((!isLast ? "|\\_" : " \\_"));
		sb.append(key);
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static synchronized List<IE_Camel_RouteTemplate> extractRouteTemplates(Map<String, Object> map) {
		List<IE_Camel_RouteTemplate> list = new ArrayList<IE_Camel_RouteTemplate>();
		Object routesFolder = map.get(_ROUTES);
		if (routesFolder != null && routesFolder instanceof Map<?, ?>) {
			Map<String, Object> routesMap = (Map<String, Object>) routesFolder;
			Set<String> types = routesMap.keySet();
			for (String type : types) {
				Object typeFolder = routesMap.get(type);
				if (typeFolder != null && typeFolder instanceof Map<?, ?>) {
					Map<String, Object> typeMap = (Map<String, Object>) typeFolder;
					addTypeIfValid(type, typeMap, list);
				}
			}
		}
		return list;
	}

	private static synchronized void addTypeIfValid(String type, Map<String, Object> typeMap,
			List<IE_Camel_RouteTemplate> list) {
		if (typeMap.containsKey(_TEMPLATE_PROPERTIES)) {
			Properties mainProp = new Properties();
			try {
				mainProp.load((InputStream) typeMap.get(_TEMPLATE_PROPERTIES));
			} catch (IOException e) {
			}
			if (!mainProp.isEmpty()) {
				addTypeIfValid(type, mainProp, typeMap, list);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static synchronized void addTypeIfValid(String type, Properties mainProp, Map<String, Object> typeMap,
			List<IE_Camel_RouteTemplate> list) {
		Set<String> subtypes = typeMap.keySet();
		Map<String, IE_Camel_RouteTemplate_Subtype> subtypesMap = new HashMap<String, IE_Camel_RouteTemplate_Subtype>();
		for (String subtype : subtypes) {
			Object subtypeFolder = typeMap.get(subtype);
			if (subtypeFolder != null && subtypeFolder instanceof Map<?, ?>) {
				Map<String, Object> subMap = (Map<String, Object>) subtypeFolder;
				addSubtypeIfValid(type, subtype, subMap, subtypesMap);
			}
		}
		if (!subtypesMap.isEmpty()) {
			IE_Camel_RouteTemplate routeTemp = new IE_Camel_RouteTemplate(type, mainProp, subtypesMap);
			list.add(routeTemp);
		}
	}

	private static synchronized void addSubtypeIfValid(String type, String subtype, Map<String, Object> subMap,
			Map<String, IE_Camel_RouteTemplate_Subtype> subtypesMap) {
		if (subMap.containsKey(_TEMPLATE_XML) && subMap.containsKey(_TOKENS_PROPERTIES)) {
			String templateXML = null;
			Properties tokenProp = new Properties();
			try {
				templateXML = TemplatesUtil.readInputStream((InputStream) subMap.get(_TEMPLATE_XML));
				tokenProp.load((InputStream) subMap.get(_TOKENS_PROPERTIES));
			} catch (Exception e) {
			}
			if (templateXML != null && !templateXML.isEmpty() && !tokenProp.isEmpty()) {
				IE_Camel_RouteTemplate_Subtype sub = new IE_Camel_RouteTemplate_Subtype(type, subtype, templateXML,
						tokenProp);
				subtypesMap.put(subtype, sub);
			}
		}
	}

}
