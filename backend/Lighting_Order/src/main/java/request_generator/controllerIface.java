package request_generator;

public interface controllerIface {
	
	public enum requests{
		tableRequest,
		userWaitingForOrderRequest,
		freeTableRequest,
		orderRequest,
		itemCompleteRequest,
		itemWorkingRequest,
		menuRequest,
		orderToTableGenerationRequest,
		cancelOrderRequest,
		cancelOrderedItemRequest,
		loginRequest
		;
	}


	public void tableRequest(String request, boolean deny);
	public void userWaitingForOrderRequest(String request, boolean deny);
	public void freeTableRequest(String request, boolean deny);
	public void orderRequest(String request, boolean deny);
	public void itemCompleteRequest(String request, boolean deny);
	public void itemWorkingRequest(String request, boolean deny);
	public void menuRequest(String request, boolean deny);
	public void orderToTableGenerationRequest(String request, boolean deny);
	public void cancelOrderRequest(String request, boolean deny);
	public void cancelOrderedItemRequest(String request, boolean deny);
	public void loginRequest(String request);
}
