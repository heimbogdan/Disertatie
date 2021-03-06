package ro.helator.ie.camel.templates;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonView;

import ro.helator.ie.camel.json.views.RouteTemplateView;

public class IE_Camel_RouteTemplate {

	@JsonView(RouteTemplateView.Name.class)
	private String name;
	
	@JsonView(RouteTemplateView.MainProp.class)
	private Properties mainProp;
	
	@JsonView(RouteTemplateView.Subtypes.class)
	private Map<String, IE_Camel_RouteTemplate_Subtype> subtypes;

	public IE_Camel_RouteTemplate(String name, Properties mainProp, Map<String, IE_Camel_RouteTemplate_Subtype> subtypes){
		this.name = name;
		this.mainProp = mainProp;
		this.subtypes = subtypes;
	}
			
	public IE_Camel_RouteTemplate(String template) {
		this.name = template;
		mainProp = TemplatesUtil.getMainProperties(this.name);

		List<String> subtypeList = TemplatesUtil.getSubfolders(TemplatesUtil.TEMPLATE_FOLDER + this.name);
		if (subtypeList != null) {
			subtypeList.forEach(s -> {
				subtypes.put(s, new IE_Camel_RouteTemplate_Subtype(this.name, s));
			});
		}
	}

	public String getName() {
		return name;
	}

	public Properties getMainProp() {
		return mainProp;
	}

	public Map<String, IE_Camel_RouteTemplate_Subtype> getSubtypes() {
		return subtypes;
	}

	public String formatForPrint(){
		StringBuilder sb = new StringBuilder();
		sb.append(" * ");
		sb.append(this.name);
		sb.append(" [");
		Set<String> keys = subtypes.keySet();
		for(String key : keys){
			sb.append(key).append(",");
		}
		sb.insert(sb.length() - 1, "]");
		return sb.toString();
	}
}
