package UsersData;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import com.google.gson.Gson;
import messages.KeyKeycloak;
import messages.KeycloakToken.KeycloakToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import DataAccess.UserDAOPSQL;
import TableAndOrdersArea.Order;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

@Service
public class UsersController {
	
	public enum ErrorCode {
		UserNotFound,
		UserFound,
		UserNotFoundInDB,
		UserFoundInDB,

		UserPasswordWrong
	}
	
	public List<User> users;
	private ErrorCode ec;

	public UsersController() {
		users = new ArrayList<User>();
	}
	
	public List<String> login(String id) {

		//Caricamento preliminare se necessario
	/*	if(!checkUser(id)) {
			UserDAOPSQL db = new UserDAOPSQL();
			String user = db.findUserByIdJSON(id);

			if(user == null) 
				this.ec = ErrorCode.UserNotFoundInDB;
			else {
				setUserByJSON(user);
				this.ec = ErrorCode.UserFoundInDB;
			}
		}
		*/
		Optional<User> u = getUserById(id);
		List<String> roles = null;
		if(u.isPresent())
			roles = u.get().getRoles();
		
		return roles;
	}

	public List<String> getRolesByAccessToken(String token){
		List<String> roles = new ArrayList<>();
		Base64.Decoder decoder = Base64.getUrlDecoder();
		String[] jwtToken = token.split("\\.");
		String head = new String(decoder.decode(jwtToken[0]));
		String body = new String(decoder.decode(jwtToken[1]));

		Gson parser = new Gson();
		KeycloakToken obj = parser.fromJson(body, KeycloakToken.class);

		for(String role : obj.realm_access.roles) {
			if (	!role.equals("offline_access") &&
					!role.equals("default-roles-ssd-realm") &&
					!role.equals("uma_authorization"))
				roles.add(role);
		}

		return roles;
	}

	public String loginFirstTime(String id, String password){
		List<String> roles = new ArrayList<>();

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
		AuthzClient keycloak_client = AuthzClient.create(config);

		//Accesso in keycloak
		AccessTokenResponse response = keycloak_client.obtainAccessToken(id,password);
		return response.getToken();
	}

	/*
	public List<String> loginFirstTime(String id, String password) {
		/** Funzione per caricare la prima volta un utente nel sistema
		 * La password è necessaria solo in questo step.
		 * Altre funzioni che vogliono controllare se l'utente abbia fatto l'accesso
		 * non devono controllare ogni volta anche la password */

		//Caricamento preliminare se necessario
	/*
	if(!checkUser(id)) {
			UserDAOPSQL db = new UserDAOPSQL();
			String user = db.findUserByIdJSON(id);

			if(user == null)
				this.ec = ErrorCode.UserNotFoundInDB;
			else {
				String pass = db.getPasswordById(id);
				if(pass.equals(password)){
					setUserByJSON(user);
					this.ec = ErrorCode.UserFoundInDB;
				} else
					this.ec = ErrorCode.UserPasswordWrong;
			}
		}

		Optional<User> u = getUserById(id);
		List<String> roles = null;
		if(u.isPresent())
			roles = u.get().getRoles();

		return roles;
	}
	*/
	public void loginAll() {
		users = new ArrayList<User>();
		UserDAOPSQL db = new UserDAOPSQL();
		List<String> list = db.findAllUserJSON();
		for(String single_user_json : list)
			setUserByJSON(single_user_json);
	}
	
	public boolean checkRole(String id, String role) {
		Optional<User> u = getUserById(id);
		boolean found = false;
		if(u.isPresent()) {
			for(String r : u.get().getRoles()) {
				if (r.equals(role))
					found = true;
			}
			return found;
		} else 
			return false;
	}
	
	public boolean checkUser(String id) {
		boolean found = false;
		for(User u : users) {
			if(u.isMe(id))
				found = true;
		}
		return found;
	}
	
	public String getUserByIdJSON(String id) {
		Optional<User> u = getUserById(id);
		if(u.isPresent())
			return u.get().getJSONReppresentation();
		else 
			return "";
	}
	
	public boolean setUserByJSON(String UserJSON) {
		User u = new User();
		boolean created = false;
		try {
			JSONObject user = new JSONObject(UserJSON);
			u.setName(user.getString("name"));
			u.setSurname(user.getString("surname"));
			u.setId(user.getString("id"));
			
			
			JSONArray roles_list = user.getJSONArray("roles");
			List<String> temp = new ArrayList<String>();
			for(int i=0; i<roles_list.length(); i++) 
				temp.add(roles_list.get(i).toString());
			
			u.setRoles(temp);
			if(checkUser(u.getId()))
				created = false;
			else {
				users.add(u);
				created = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return created;
	}
	
	public UsersController.ErrorCode getErrorCode(){
		return this.ec;
	}
	
	public Optional<User> getUserById(String id){
		boolean found = false;
		this.ec = UsersController.ErrorCode.UserNotFound;
		int index = 0;
		
		while(!found && index<users.size()) {
			if(users.get(index).isMe(id))
				found = true;
			index++;
		}
		if(!found)
			return Optional.empty();
		else {
			this.ec = UsersController.ErrorCode.UserFound;
			return Optional.of(users.get(index-1));
		}
	}
	/**
	 * 
	 * @param id of the user
	 * @param o order to be inserted
	 * @return empty if the user doesn't exists else optional.of operationn result
	 */
	public Optional<User> registerOrderToUser(String id,Order o) {
		login(id);
		Optional<User> user=this.getUserById(id);
		boolean result;
		if(user.isPresent()) {
			result=user.get().registerOrder(o);
			if(result)
				return user;
		}
		return Optional.empty();
	}
}
