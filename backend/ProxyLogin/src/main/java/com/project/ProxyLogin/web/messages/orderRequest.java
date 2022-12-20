package com.project.ProxyLogin.web.messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class orderRequest extends baseMessage{
	public boolean areaVisualization;
	public String area;

	@Override
	public boolean validate() {
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(area);
		return m.find() | super.validate();
	}
}
