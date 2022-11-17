package com.lightingorder.Model.messages;

public class loginRequest extends baseMessage{
	public String url;
	private String password;

	public loginRequest(String url) {
		this.url = url;
	}

	public loginRequest(String user, String password, String proxySource, String messageName, String result, String response, String url) {
		super(user, proxySource, messageName, result, response);
		this.url = url;
		this.password = password;
	}
}
