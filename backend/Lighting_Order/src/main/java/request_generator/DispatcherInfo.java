
package request_generator;
import org.keycloak.authorization.client.AuthorizationDeniedException;
import org.keycloak.authorization.client.AuthzClient;
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
	private AuthzClient c;

	@Autowired
	public DispatcherInfo(@Qualifier("SystemController") controllerIface input) {
		this.controllerFunctions=input;
		this.c = AuthzClient.create();
	}
    public void callerFactory(String mex) {
    	
    	Gson gson=new Gson();
    	
    	baseMessage rec=gson.fromJson(mex, baseMessage.class);	
    	if(rec.messageName!=null) {
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
	    	
	    	else if(rec.messageName.equals(controllerIface.requests.loginRequest.name()))
	    		controllerFunctions.loginRequest(mex); 
    	}
    		
    }
}
