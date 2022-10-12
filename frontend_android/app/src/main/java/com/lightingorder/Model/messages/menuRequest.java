package com.lightingorder.Model.messages;

public class menuRequest extends baseMessage{
	public boolean areaVisualization;
	public String areaMenu;

	public menuRequest(boolean areaVisualization, String areaMenu) {
		this.areaVisualization = areaVisualization;
		this.areaMenu = areaMenu;
	}

	public menuRequest(String user, String proxySource, String messageName, String result, String response, boolean areaVisualization, String areaMenu) {
		super(user, proxySource, messageName, result, response);
		this.areaVisualization = areaVisualization;
		this.areaMenu = areaMenu;
	}
}
