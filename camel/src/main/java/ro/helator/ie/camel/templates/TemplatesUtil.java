package ro.helator.ie.camel.templates;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplatesUtil {
	private static final Logger log = LoggerFactory.getLogger(TemplatesUtil.class);

	public static final String SUBFOLDER_SEPARATOR = "/";
	public static final String TEMPLATE_FOLDER = "templates/routes/";
	public static final String TEMPLATE_MAIN_PROPERTIES = "/template.properties";
	public static final String TEMPLATE_SUBTYPE_PROPERTIES = "/tokens.properties";
	public static final String TEMPLATE_FILE = "/template.xml";

	public static Properties getMainProperties(String name) {
		return getProperties(TEMPLATE_MAIN_PROPERTIES, name);
	}

	public static Properties getSubtypeProperties(String name, String subtype) {
		return getProperties(TEMPLATE_SUBTYPE_PROPERTIES, name, subtype);
	}

	public static Properties getProperties(String file, String... subfolders) {
		Properties prop = null;
		try {
			if (!file.startsWith(SUBFOLDER_SEPARATOR)) {
				file = SUBFOLDER_SEPARATOR + file;
			}
			String propString = getFileInput(file, subfolders);
			prop = new Properties();
			prop.load(new ByteArrayInputStream(propString.getBytes()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return prop;
	}

	public static String getTemplateFileInput(String... subfolders) {
		try {
			return getFileInput(TEMPLATE_FILE, subfolders);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static String getFileInput(String file, String... subfolders)
			throws FileNotFoundException, URISyntaxException {
		StringBuilder sb = new StringBuilder();
		for (String subfolder : subfolders) {
			if (sb.length() > 0) {
				sb.append(SUBFOLDER_SEPARATOR);
			}
			sb.append(subfolder);
		}
		InputStream ins = null;
		ByteArrayOutputStream out = null;
		String content = "";
		try {
			ins = new BufferedInputStream(
					TemplatesUtil.class.getClassLoader().getResourceAsStream(TEMPLATE_FOLDER + sb.toString() + file));
			out = new ByteArrayOutputStream();
			int nbytes = 0;
			byte[] buffer = new byte[100000];

			log.info(">> try to read from inputStream...");
			while ((nbytes = ins.read(buffer)) != -1) {
				out.write(buffer, 0, nbytes);
			}

			content = new String(out.toByteArray());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				ins.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
			try {
				out.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}

		log.info("\n\n INPUT STREAM:\n" + content);

		return content;
	}

	public static List<String> getSubfolders(String path) {
		try {
			if(path.endsWith(SUBFOLDER_SEPARATOR)){
				path = path.substring(0, path.length() -1 );
			}
			URL dir_url = TemplatesUtil.class.getClassLoader().getResource(path);
			File dir = new File(dir_url.toURI());
			return getSubfolders(dir);
		} catch (URISyntaxException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static List<String> getSubfolders(File dir) {
		try {
			if (dir.isDirectory()) {
				File[] subdirs = dir.listFiles(f -> {
					return f.isDirectory();
				});
				if (subdirs != null && subdirs.length > 0) {
					List<String> subfolders = new ArrayList<String>();
					Arrays.asList(subdirs).forEach(subdir -> {
						subfolders.add(subdir.getName());
					});
					return subfolders;
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static String readInputStream(InputStream stream) throws Exception {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		while ((line = br.readLine()) != null) {
			sb.append(line).append("\n");
		}
		return sb.toString();
	}

	public static List<String> retrieveTokens(Properties main, Properties subtype) {
		List<String> tokens = new ArrayList<String>();
		String[] subtypeTokens = subtype.getProperty("tokens").split(",");
		for (String subtypeToken : subtypeTokens) {
			String[] mainTokens = main.getProperty(subtypeToken).split(",");
			for (String mainToken : mainTokens) {
				tokens.add(mainToken);
			}
		}

		return tokens;
	}

	public static String templateTokenResolver(String template, List<String> tokens, Properties routeProperties) {
		log.info(">> Template token resolver");
		for (String token : tokens) {

			String value = routeProperties.getProperty(token);
			log.info("Token: " + token + " / Value: " + value);
			if (value != null && !value.isEmpty()) {
				template = template.replaceAll("%" + token.toUpperCase() + "%", value);
			}
		}
		return template.replace("%UUID%", UUID.randomUUID().toString());
	}
	
}
