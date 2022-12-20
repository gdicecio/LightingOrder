package com.project.ProxyLogin.web.messages;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class baseMessage {
	public String user;
	public String access_token;
	public String proxySource;
	public String messageName;
	public String result;
	public String response;


	public boolean validate(){
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(user);
		return m.find();
	}
}
