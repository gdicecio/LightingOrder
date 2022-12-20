package com.project.ProxyLogin.web.messages;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class loginRequest extends baseMessage{
	public String url;
	public String password;

	@Override
	public boolean validate() {
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(password);
		return m.find() | super.validate();
	}
}

