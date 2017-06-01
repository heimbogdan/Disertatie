package ro.helator.dia.entity;

import java.util.Properties;

public class Template {

	private String type;
	private String subtype;
	private Integer tokensNo;
	private Integer deployedNo;
	private Properties tokens;

	public Template(String type, String subtype, Integer deployedNo,
			Properties tokens){
		
		this.type = type;
		this.subtype = subtype;
		this.deployedNo = deployedNo;
		this.tokens = tokens;
		this.tokensNo = this.tokens.keySet().size();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public Integer getTokensNo() {
		return tokensNo;
	}

	public void setTokensNo(Integer tokensNo) {
		this.tokensNo = tokensNo;
	}

	public Integer getDeployedNo() {
		return deployedNo;
	}

	public void setDeployedNo(Integer deployedNo) {
		this.deployedNo = deployedNo;
	}

	public Properties getTokens() {
		return tokens;
	}

	public void setTokens(Properties tokens) {
		this.tokens = tokens;
	}
	
	
}
