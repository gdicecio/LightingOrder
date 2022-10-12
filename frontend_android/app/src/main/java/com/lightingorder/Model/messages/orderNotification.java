package com.lightingorder.Model.messages;

public class orderNotification  extends baseMessage{
	public String area;
	public String order;

	public orderNotification(String area, String order) {
		this.area = area;
		this.order = order;
	}

	public orderNotification(String user, String proxySource, String messageName, String result, String response, String area, String order) {
		super(user, proxySource, messageName, result, response);
		this.area = area;
		this.order = order;
	}
}
