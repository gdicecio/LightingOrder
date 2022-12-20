package com.project.ProxyLogin.web.messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class tableOperation extends baseMessage{
	public String tableID;
	public int tableRoomNumber;

	@Override
	public boolean validate() {
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(tableID);
		return m.find() | super.validate();
	}
}
