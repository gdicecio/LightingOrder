package com.lightingorder.Model.messages;

public class tableRequest extends baseMessage {
	public boolean forRoom;
	public int roomNumber;
	public boolean showItemsInArea;
	public String orderArea;

	public tableRequest(boolean forRoom, int roomNumber, boolean showItemsInArea, String orderArea) {
		this.forRoom = forRoom;
		this.roomNumber = roomNumber;
		this.showItemsInArea = showItemsInArea;
		this.orderArea = orderArea;
	}

	public tableRequest(String user, String proxySource, String messageName, String result, String response, boolean forRoom, int roomNumber, boolean showItemsInArea, String orderArea) {
		super(user, proxySource, messageName, result, response);
		this.forRoom = forRoom;
		this.roomNumber = roomNumber;
		this.showItemsInArea = showItemsInArea;
		this.orderArea = orderArea;
	}
}
