package com.project.ProxyLogin.web.messages;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class orderToTableGenerationRequest extends baseMessage {

	public class orderParameters{
		@Expose(serialize=true,   deserialize=true)
		public List<String> itemNames;
		@Expose(serialize=true, deserialize=true)
		public List<List<String>> addGoods;
		@Expose(serialize=true,deserialize=true)
		public List<List<String>>subGoods;
		@Expose(serialize=true,deserialize=true)
		public List<Integer>  priority;

		public boolean validate() {
			Pattern p = Pattern.compile("[^A-Za-z0-9]");
			boolean check = false;
			for(String item : itemNames){
				Matcher m = p.matcher(item);
				check |= m.find();
			}
			for(List<String> items : addGoods){
				for(String item : items){
					Matcher m= p.matcher(item);
					check |= m.find();
				}
			}
			for(List<String> items : subGoods){
				for(String item : items){
					Matcher m= p.matcher(item);
					check |= m.find();
				}
			}
			return check;
		}
	}
	
	public String tableId;
	
	public int tableRoomNumber;

	public orderParameters orderParams;

	@Override
	public boolean validate() {
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		boolean check = orderParams.validate();
		Matcher m = p.matcher(tableId);
		check |= m.find();
		return check | super.validate();
	}
}
