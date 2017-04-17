package ro.helator.dia.server;

import java.io.Serializable;

public class Server implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4026121817394231068L;
	
	private String name;
	private String ip;
	private String port;
	private String user;
	private String password;
	private String karafPath;
	
	public Server(String name, String ip, String port, String user, String password, String karafPath) {
		super();
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.user = user;
		this.password = password;
		this.karafPath = karafPath;
	}

	public Server(){};
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getKarafPath() {
		return karafPath;
	}

	public void setKarafPath(String karafPath) {
		this.karafPath = karafPath;
	}


	@Override
	public String toString() {
		return "Server [name=" + name + ", ip=" + ip + ", port=" + port + ", user=" + user + ", password=" + password
				+ ", karafPath=" + karafPath + "]";
	}

	
	public String getFullHostname(){
		return ip + ":" + port;
	}
}
