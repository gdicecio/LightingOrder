
package request_generator;
import messages.KeyKeycloak;
import org.keycloak.authorization.client.AuthorizationDeniedException;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import messages.baseMessage;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Controller
@ComponentScan(basePackages= {"controller"})
public class DispatcherInfo {
	private controllerIface controllerFunctions;
	private AuthzClient keycloak_client;

	@Autowired
	public DispatcherInfo(@Qualifier("SystemController") controllerIface input) {
		this.controllerFunctions=input;
		VaultTemplate template = null;
		try {
			template = new VaultTemplate(VaultEndpoint.from(new URI("http://127.0.0.1:8200")
			), new TokenAuthentication("hvs.bLXKpXMba7g0IayOm6AbDbHX"));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
		VaultResponseSupport<KeyKeycloak> keycloak = template.read("kv/keycloak", KeyKeycloak.class);
		Configuration config = new Configuration(keycloak.getData().getHost(),
				keycloak.getData().getRealm(),
				keycloak.getData().getClientId(),
				keycloak.getData().getCredentials(),
				null);
		this.keycloak_client = AuthzClient.create(config);

	}
	public void callerFactory(String mex) {

		Gson gson=new Gson();

		baseMessage rec=gson.fromJson(mex, baseMessage.class);

		boolean deny = false;

		if(rec.messageName!=null) {
			if (rec.messageName.equals(controllerIface.requests.tableRequest.name())) {
				deny = permissionResource(rec.access_token, "/api/tableRequest");
				controllerFunctions.tableRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.userWaitingForOrderRequest.name())) {
				deny = permissionResource(rec.access_token, "/api/userWaitingForOrderRequest");
				controllerFunctions.userWaitingForOrderRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.freeTableRequest.name())) {
				deny = permissionResource(rec.access_token, "/api/freeTableRequest");
				controllerFunctions.freeTableRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.itemCompleteRequest.name())) {
				deny = permissionResource(rec.access_token, "/api/itemCompleteRequest");
				controllerFunctions.itemCompleteRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.itemWorkingRequest.name())) {
				deny = permissionResource(rec.access_token, "/api/itemWorkingRequest");
				controllerFunctions.itemWorkingRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.orderRequest.name())) {
				deny = permissionResource(rec.access_token, "/api/orderRequest");
				controllerFunctions.orderRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.menuRequest.name())) {
				deny = permissionResource(rec.access_token, "/api/menuRequest");
				controllerFunctions.menuRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.orderToTableGenerationRequest.name())) {
				deny = permissionResource(rec.access_token, "/api/orderToTableGenerationRequest");
				controllerFunctions.orderToTableGenerationRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.cancelOrderRequest.name())) {
				deny = permissionResource(rec.access_token, "/api/cancelOrderRequest");
				controllerFunctions.cancelOrderRequest(mex, deny);
			} else if (rec.messageName.equals(controllerIface.requests.cancelOrderedItemRequest.name())) {
				deny = permissionResource(rec.access_token, "/api/cancelOrderedItemRequest");
				controllerFunctions.cancelOrderedItemRequest(mex, deny);
			} else if(rec.messageName.equals(controllerIface.requests.loginRequest.name()))
				controllerFunctions.loginRequest(mex);
			}
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
