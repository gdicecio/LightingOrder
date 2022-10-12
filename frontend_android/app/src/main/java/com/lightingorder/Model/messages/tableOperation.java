package com.lightingorder.Model.messages;

import com.google.gson.annotations.Expose;

public class tableOperation extends baseMessage{
	public String tableID;
	public int tableRoomNumber;

	public tableOperation(String tableID, int tableRoomNumber) {
		this.tableID = tableID;
		this.tableRoomNumber = tableRoomNumber;
	}

	public tableOperation(String user, String proxySource, String messageName, String result, String response, String tableID, int tableRoomNumber) {
		super(user, proxySource, messageName, result, response);
		this.tableID = tableID;
		this.tableRoomNumber = tableRoomNumber;
	}
}
