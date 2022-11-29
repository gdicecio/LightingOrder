package com.project.ProxyLogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class ProxyLoginApplication {



	public static void main(String[] args) {

		VaultTemplate template = null;
		try {
			template = new VaultTemplate(VaultEndpoint.from(new URI("http://127.0.0.1:8200")), new TokenAuthentication("hvs.bLXKpXMba7g0IayOm6AbDbHX"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		VaultResponse response = template.read("kv/keys");

		String pin = response.getData().get("certificate_pin").toString();

		System.setProperty("server.ssl.key-store-password", pin);

		SpringApplication.run(ProxyLoginApplication.class, args);
	}




}
