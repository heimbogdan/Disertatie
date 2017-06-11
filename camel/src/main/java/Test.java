import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.dataformat.JacksonXMLDataFormat;

import ro.helator.ie.camel.bean.BeanElement;
import ro.helator.ie.camel.bean.BeanType;
import ro.helator.ie.camel.json.views.BeanView;

public class Test implements BeanElement{

	@Override
	public String getBeanName() {
		return "Test";
	}

	@Override
	public String[] getMethods() {
		// TODO Auto-generated method stub
		return new String[] {"method1", "method2"};
	}

	public void method1(){
		
	}
	
	public void method2(String param1, int param2){
		
	}
	
	public static void main(String[] args) {
		Test t = new Test();
		BeanType bt = new BeanType(t);
		ExchangeBuilder eb = new ExchangeBuilder(new DefaultCamelContext());
		
		JacksonXMLDataFormat df = new JacksonXMLDataFormat();
		df.setJsonView(BeanView.Name.class);
		df.setUnmarshalType(new ArrayList<BeanType>().getClass());
		Exchange ex = eb.withBody(bt).build();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			df.getDataFormat().marshal(ex, bt, baos);
			System.out.println(new String(baos.toByteArray()) + "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
