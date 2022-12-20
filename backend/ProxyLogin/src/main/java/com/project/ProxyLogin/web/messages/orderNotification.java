package com.project.ProxyLogin.web.messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class orderNotification  extends baseMessage{
	public String area;
	public String order;

	@Override
	public boolean validate() {
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(area);
		return m.find() | super.validate();
	}
}
