package com.lighting_order;
import messages.KeyArtemis;
import messages.KeyKeycloak;
import org.keycloak.authorization.client.AuthorizationDeniedException;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootApplication(scanBasePackages= {"request_generator","broker","controller"})
public class LightingOrderApplication {
	
	public static void main(String[] args) {


		VaultTemplate template = null;

		//Connection Vault
		try {
			template = new VaultTemplate(VaultEndpoint.from(new URI("http://127.0.0.1:8200")
			), new TokenAuthentication("hvs.bLXKpXMba7g0IayOm6AbDbHX"));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		//Get Keys from Vault
		VaultResponseSupport<KeyArtemis> artemis = template.read("kv/artemis", KeyArtemis.class);

		VaultResponseSupport<KeyKeycloak> keycloak = template.read("kv/keycloak", KeyKeycloak.class);
		Map<String, Object> credentials = new HashMap<>();
		credentials.put("secret", new String("zkDcjndhfTahODX6Ix86Es0MYtD3XetK"));
		Configuration config = new Configuration(keycloak.getData().getHost(), keycloak.getData().getRealm(), keycloak.getData().getClientId(), keycloak.getData().getCredentials(), null);
		AuthzClient client = AuthzClient.create(config);
		AccessTokenResponse response = client.obtainAccessToken("1","giuseppe");

		System.setProperty("spring.artemis.host", artemis.getData().getHost());
		System.setProperty("spring.artemis.port", artemis.getData().getPort());
		System.setProperty("spring.artemis.user", artemis.getData().getUser());
		System.setProperty("spring.artemis.password", artemis.getData().getPassword());


		SpringApplication.run(LightingOrderApplication.class, args);
	}

}
