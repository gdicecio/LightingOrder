package com.lightingorder.Model.messages;

public class itemOpRequest extends baseMessage{
	public int orderID;
	public int itemLineNumber;

	public itemOpRequest(int orderID, int itemLineNumber) {
		this.orderID = orderID;
		this.itemLineNumber = itemLineNumber;
	}

	public itemOpRequest(String user, String proxySource, String messageName, String result, String response, int orderID, int itemLineNumber) {
		super(user, proxySource, messageName, result, response);
		this.orderID = orderID;
		this.itemLineNumber = itemLineNumber;
	}
}
