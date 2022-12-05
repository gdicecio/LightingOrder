package com.project.ProxyCameriere.ProxyCameriere;

import com.project.ProxyCameriere.ProxyCameriere.web.KeyArtemis;
import com.project.ProxyCameriere.ProxyCameriere.web.KeyCertificate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponseSupport;

import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class ProxyCameriereApplication {

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
		VaultResponseSupport<KeyCertificate> certificate = template.read("kv/certificate", KeyCertificate.class);

		System.setProperty("server.ssl.key-store-password", certificate.getData().getPin());
		System.setProperty("spring.artemis.host", artemis.getData().getHost());
		System.setProperty("spring.artemis.port", artemis.getData().getPort());
		System.setProperty("spring.artemis.user", artemis.getData().getUser());
		System.setProperty("spring.artemis.password", artemis.getData().getPassword());

		SpringApplication.run(ProxyCameriereApplication.class, args);
	}

}
