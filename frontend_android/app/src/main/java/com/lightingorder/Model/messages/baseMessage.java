package com.lightingorder.Model.messages;
public class baseMessage {
	public String user;
	public String proxySource;
	public String messageName;
	public String result;
	public String response;

	public baseMessage(){};

	public baseMessage(String user, String proxySource, String messageName, String result, String response) {
		this.user = user;
		this.proxySource = proxySource;
		this.messageName = messageName;
		this.result = result;
		this.response = response;
	}
}
