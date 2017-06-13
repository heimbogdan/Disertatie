package ro.helator.ie.camel.bean;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.ParameterMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;

import ro.helator.ie.camel.json.views.BeanView;

public class BeanType {

	@JsonView(BeanView.Name.class)
	private String name;

	@JsonView(BeanView.Methods.class)
	private List<MethodType> methods;

	public BeanType() {
	}

	public BeanType(String name, List<MethodType> methods) {
		this.name = name;
		this.methods = methods;

	}

	public BeanType(BeanElement bean) {
		this.name = bean.getBeanName();
		this.methods = new ArrayList<MethodType>();

		String[] methodsNames = bean.getMethods();
		Method[] methodsSingature = bean.getClass().getMethods();

		for (Method method : methodsSingature) {
			for (String methodName : methodsNames) {
				if (methodName.equals(method.getName())) {

					Parameter[] params = method.getParameters();
					if (params.length > 0) {
						Map<String, String> parameters = new HashMap<String, String>();
						for (Parameter param : params) {
							ParamDesc pd = param.getAnnotation(ParamDesc.class);
							parameters.put(pd != null && !pd.name().isEmpty() ? pd.name() : param.getName(),
									param.getType().getName());
						}
						methods.add(new MethodType(methodName, parameters));
					} else {
						methods.add(new MethodType(methodName, null));
					}
				}
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MethodType> getMethods() {
		return methods;
	}

	public void setMethods(List<MethodType> methods) {
		this.methods = methods;
	}

}