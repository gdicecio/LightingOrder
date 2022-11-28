
package request_generator;
import org.keycloak.authorization.client.AuthorizationDeniedException;
import org.keycloak.authorization.client.AuthzClient;
<<<<<<< Updated upstream
=======
import org.keycloak.authorization.client.util.HttpResponseException;
>>>>>>> Stashed changes
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import messages.baseMessage;

@Controller
@ComponentScan(basePackages= {"controller"})
public class DispatcherInfo {
	private controllerIface controllerFunctions;
<<<<<<< Updated upstream
	private AuthzClient c;

	@Autowired
	public DispatcherInfo(@Qualifier("SystemController") controllerIface input) {
		this.controllerFunctions=input;
		this.c = AuthzClient.create();
=======
	private AuthzClient keycloak_client;
	
	@Autowired
	public DispatcherInfo(@Qualifier("SystemController") controllerIface input) {
		this.controllerFunctions=input;
		this.keycloak_client = AuthzClient.create();
>>>>>>> Stashed changes
	}
    public void callerFactory(String mex) {
    	
    	Gson gson=new Gson();
    	
    	baseMessage rec=gson.fromJson(mex, baseMessage.class);

		boolean deny = false;

    	if(rec.messageName!=null) {
<<<<<<< Updated upstream
	    	if(rec.messageName.equals(controllerIface.requests.tableRequest.name())) {
				AuthorizationRequest req = new AuthorizationRequest();
				req.addPermission("api/tablerequest");
				try {
					// ACCESS TOKEN O REFRESH TOKEN?
					AuthorizationResponse resp = c.authorization(rec.access_token).authorize();
					controllerFunctions.tableRequest(mex);
				} catch (AuthorizationDeniedException e) {
					e.printStackTrace();
				}
			}
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.userWaitingForOrderRequest.name()))
	    		controllerFunctions.userWaitingForOrderRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.freeTableRequest.name()))
	    		controllerFunctions.freeTableRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.itemCompleteRequest.name()))
	    		controllerFunctions.itemCompleteRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.itemWorkingRequest.name()))
	    		controllerFunctions.itemWorkingRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.orderRequest.name()))
	    		controllerFunctions.orderRequest(mex);
	       	
	    	else if(rec.messageName.equals(controllerIface.requests.menuRequest.name())) 
	    		controllerFunctions.menuRequest(mex);
	    	
	       
	    	else if(rec.messageName.equals(controllerIface.requests.orderToTableGenerationRequest.name()))
	    		controllerFunctions.orderToTableGenerationRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.cancelOrderRequest.name()))
	    		controllerFunctions.cancelOrderRequest(mex);
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.cancelOrderedItemRequest.name()))
	    		controllerFunctions.cancelOrderedItemRequest(mex);
	    	
=======
			if (rec.messageName.equals(controllerIface.requests.tableRequest.name())) {

				deny = permissionResource(rec.access_token, "api/tableRequest");
				controllerFunctions.tableRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.userWaitingForOrderRequest.name())) {
				deny = permissionResource(rec.access_token, "api/userWaitingForOrderRequest");
				controllerFunctions.userWaitingForOrderRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.freeTableRequest.name())) {
				deny = permissionResource(rec.access_token, "api/freeTableRequest");
				controllerFunctions.freeTableRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.itemCompleteRequest.name())) {
				deny = permissionResource(rec.access_token, "api/itemCompleteRequest");
				controllerFunctions.itemCompleteRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.itemWorkingRequest.name())) {
				deny = permissionResource(rec.access_token, "api/itemWorkingRequest");
				controllerFunctions.itemWorkingRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.orderRequest.name())) {
				deny = permissionResource(rec.access_token, "api/orderRequest");
				controllerFunctions.orderRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.menuRequest.name())) {
				deny = permissionResource(rec.access_token, "api/menuRequest");
				controllerFunctions.menuRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.orderToTableGenerationRequest.name())) {
				deny = permissionResource(rec.access_token, "api/orderToTableGenerationRequest");
				controllerFunctions.orderToTableGenerationRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.cancelOrderRequest.name())) {
				deny = permissionResource(rec.access_token, "api/cancelOrderRequest");
				controllerFunctions.cancelOrderRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.cancelOrderedItemRequest.name()))
				deny = permissionResource(rec.access_token, "api/cancelOrderedItemRequest");
			controllerFunctions.cancelOrderedItemRequest(mex, deny);
			}
>>>>>>> Stashed changes
	    	else if(rec.messageName.equals(controllerIface.requests.loginRequest.name()))
				controllerFunctions.loginRequest(mex);

    	}


	private boolean permissionResource(String token, String resource){
		boolean deny = false;
		AuthorizationRequest req = new AuthorizationRequest();
		req.addPermission(resource);
		try {
			AuthorizationResponse res = keycloak_client.authorization(token).authorize(req);
		}catch (AuthorizationDeniedException e){
			deny = true;
		} catch(Exception e){
			deny = true;
		}
		return deny;
	}
}
