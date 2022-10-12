package com.lightingorder.Model.messages;

import java.util.List;

import com.google.gson.annotations.Expose;

public class orderToTableGenerationRequest extends baseMessage {

	public static class orderParameters{
		@Expose(serialize=true,   deserialize=true)
		public List<String> itemNames;
		@Expose(serialize=true, deserialize=true)
		public List<List<String>> addGoods;
		@Expose(serialize=true,deserialize=true)
		public List<List<String>>subGoods;
		@Expose(serialize=true,deserialize=true)
		public List<Integer>  priority;

		public orderParameters(List<String> itemNames, List<List<String>> addGoods, List<List<String>> subGoods, List<Integer> priority) {
			this.itemNames = itemNames;
			this.addGoods = addGoods;
			this.subGoods = subGoods;
			this.priority = priority;
		}
	}
	
	public String tableId;
	
	public int tableRoomNumber;

	public orderParameters orderParams;

	public orderToTableGenerationRequest(String tableId, int tableRoomNumber, orderParameters orderParams) {
		this.tableId = tableId;
		this.tableRoomNumber = tableRoomNumber;
		this.orderParams = orderParams;
	}

	public orderToTableGenerationRequest(String user, String proxySource, String messageName, String result, String response, String tableId, int tableRoomNumber, orderParameters orderParams) {
		super(user, proxySource, messageName, result, response);
		this.tableId = tableId;
		this.tableRoomNumber = tableRoomNumber;
		this.orderParams = orderParams;
	}
}
