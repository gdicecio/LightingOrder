package com.project.ProxyLogin.web.messages;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tableRequest extends baseMessage {
	public boolean forRoom;
	public int roomNumber;
	public boolean showItemsInArea;
	public String orderArea;

	@Override
	public boolean validate() {
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(orderArea);
		return m.find() | super.validate();
	}
}
