package com.lightingorder.Model.messages;

public class loginRequest extends baseMessage{
	public String url;

	public loginRequest(String url) {
		this.url = url;
	}

	public loginRequest(String user, String proxySource, String messageName, String result, String response, String url) {
		super(user, proxySource, messageName, result, response);
		this.url = url;
	}
}
