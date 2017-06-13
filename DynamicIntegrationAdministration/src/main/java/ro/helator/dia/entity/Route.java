package ro.helator.dia.entity;

public class Route {

	private String context;
	private String id;
	private String endpoint;
	private String state;
	private String uptime;
	
	public Route() {
		super();
	}

	public Route(String context, String id, String endpoint, String state, String uptime) {
		super();
		this.context = context;
		this.id = id;
		this.endpoint = endpoint;
		this.state = state;
		this.uptime = uptime;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUptime() {
		return uptime;
	}

	public void setUptime(String uptime) {
		this.uptime = uptime;
	}
	
	
}
