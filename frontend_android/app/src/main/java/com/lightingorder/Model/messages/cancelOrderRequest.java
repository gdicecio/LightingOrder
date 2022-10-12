package com.lightingorder.Model.messages;

public class cancelOrderRequest extends baseMessage {
	public int orderID;

	public cancelOrderRequest(int orderID) {
		this.orderID = orderID;
	}

	public cancelOrderRequest(String user, String proxySource, String messageName, String result, String response, int orderID) {
		super(user, proxySource, messageName, result, response);
		this.orderID = orderID;
	}
}
