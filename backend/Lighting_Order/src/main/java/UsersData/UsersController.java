package UsersData;

import java.util.*;

import com.google.gson.Gson;
import messages.KeycloakToken.KeycloakToken;
import org.apache.activemq.artemis.api.core.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.stereotype.Service;

import DataAccess.UserDAOPSQL;
import TableAndOrdersArea.Order;
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
	
	public List<String> login(String token) {

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
		Optional<User> u = getUserBy_Token(token);
		List<String> roles = null;
		if(u.isPresent())
			roles = u.get().getRoles();
		
		return roles;
	}

	public Pair<List<String>, String> loginFirstTime(String id, String password){
		List<String> roles = new ArrayList<>();
		//Accesso in keycloak
		AuthzClient keycloak_client = AuthzClient.create();
		AccessTokenResponse response = keycloak_client.obtainAccessToken(id,password);

		if (response.getError().isEmpty()) {
			String token = response.getToken();

			Base64.Decoder decoder = Base64.getUrlDecoder();
			String[] jwtToken = token.split("\\.");
			String head = new String(decoder.decode(jwtToken[0]));
			String body = new String(decoder.decode(jwtToken[1]));

			Gson parser = new Gson();
			KeycloakToken obj = parser.fromJson(body, KeycloakToken.class);

			for (String role : obj.realm_access.roles) {
				if (!role.equals("offline_access") &&
						!role.equals("default-roles-ssd-realm") &&
						!role.equals("uma_authorization"))
					roles.add(role);
			}

			setUserByJSONToken(obj, token);

			return new Pair<List<String>, String>(roles,token);
		}
		else {
			this.ec = ErrorCode.UserPasswordWrong;
			return null;
		}

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
	
	public boolean checkUser(String token) {
		boolean found = false;
		for(User u : users) {
			if(u.isMe_ByToken(token))
				found = true;
		}
		return found;
	}

	/*
	public String getUserByIdJSON(String id) {
		Optional<User> u = getUserById(id);
		if(u.isPresent())
			return u.get().getJSONReppresentation();
		else 
			return "";
	}
	 */

	public boolean setUserByJSONToken(KeycloakToken tokenJSON, String token) {
		User u = new User();
		boolean created = false;
		try {
			//JSONObject user = new JSONObject(UserJSON);
			u.setName(tokenJSON.name);
			u.setSurname(tokenJSON.family_name);
			u.setId(tokenJSON.sid);
			u.setToken(token);

			List<String> temp = new ArrayList<String>();
			for(int i=0; i<tokenJSON.realm_access.roles.size(); i++)
				temp.add(tokenJSON.realm_access.roles.get(i));
			
			u.setRoles(temp);
			if(checkUser(u.getToken()))
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
	
	public Optional<User> getUserBy_Token(String token){
		boolean found = false;
		this.ec = UsersController.ErrorCode.UserNotFound;
		int index = 0;
		
		while(!found && index<users.size()) {
			if(users.get(index).isMe_ByToken(token))
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

	public Optional<User> getUserById(String id){
		boolean found = false;
		this.ec = UsersController.ErrorCode.UserNotFound;
		int index = 0;

		while(!found && index<users.size()) {
			if(users.get(index).isMe_ByID(id))
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
	public Optional<User> registerOrderToUser(String token, Order o) {
		login(token);
		Optional<User> user=this.getUserById(token);
		boolean result;
		if(user.isPresent()) {
			result=user.get().registerOrder(o);
			if(result)
				return user;
		}
		return Optional.empty();
	}
}
