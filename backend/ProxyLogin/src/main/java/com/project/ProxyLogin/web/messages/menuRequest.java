package com.project.ProxyLogin.web.messages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class menuRequest extends baseMessage{
	public boolean areaVisualization;
	public String areaMenu;

	@Override
	public boolean validate() {
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(areaMenu);
		return m.find() | super.validate();
	}
}
