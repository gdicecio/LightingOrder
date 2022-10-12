package com.lightingorder.Model.messages;

public class orderRequest extends baseMessage{
	public boolean areaVisualization;
	public String area;

	public orderRequest(boolean areaVisualization, String area) {
		this.areaVisualization = areaVisualization;
		this.area = area;
	}

	public orderRequest(String user, String proxySource, String messageName, String result, String response, boolean areaVisualization, String area) {
		super(user, proxySource, messageName, result, response);
		this.areaVisualization = areaVisualization;
		this.area = area;
	}
}
