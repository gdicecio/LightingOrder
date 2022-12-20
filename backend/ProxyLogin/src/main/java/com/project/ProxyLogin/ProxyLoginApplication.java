package com.project.ProxyLogin;

import com.project.ProxyLogin.web.KeyArtemis;
import com.project.ProxyLogin.web.KeyCertificate;
import com.project.ProxyLogin.web.messages.baseMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.support.VaultResponseSupport;

import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;

@SpringBootApplication
public class ProxyLoginApplication {

	public static void main(String[] args) {


		VaultTemplate template = null;
		try {
			template = new VaultTemplate(VaultEndpoint.from(new URI("http://127.0.0.1:8200")),
					new TokenAuthentication("hvs.bLXKpXMba7g0IayOm6AbDbHX"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		//VaultResponse certificate = template.read("kv/keys");
		VaultResponseSupport<KeyArtemis> artemis = template.read("kv/artemis", KeyArtemis.class);
		VaultResponseSupport<KeyCertificate> certificate = template.read("kv/certificate", KeyCertificate.class);

		//String pin = certificate.getData().get("certificate_pin").toString();

		System.setProperty("server.ssl.key-store-password", certificate.getData().getPin());
		System.setProperty("spring.artemis.host", artemis.getData().getHost());
		System.setProperty("spring.artemis.port", artemis.getData().getPort());
		System.setProperty("spring.artemis.user", artemis.getData().getUser());
		System.setProperty("spring.artemis.password", artemis.getData().getPassword());
		Log l;
		try {
			l = Log.getInstance(ProxyLoginApplication.class.getName(), "login");
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		l.info("main", "Applicazione avviata");


		SpringApplication.run(ProxyLoginApplication.class, args);
	}




}
