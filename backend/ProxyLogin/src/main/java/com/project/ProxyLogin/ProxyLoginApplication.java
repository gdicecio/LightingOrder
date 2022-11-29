package com.project.ProxyLogin;

import com.project.ProxyLogin.web.Secret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.vault.config.Secrets;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.vault.support.VaultResponseSupport;

@SpringBootApplication
public class ProxyLoginApplication {



	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SecurityConfiguration.class);
		context.start();
		SecurityConfiguration securityConfiguration = context.getBean(SecurityConfiguration.class);
		String passKeystore=securityConfiguration.getPassKeystore();
		System.out.println("Questa è la passKeystore:"+passKeystore);
		System.setProperty("server.ssl.key-store-password",passKeystore);
		SpringApplication.run(ProxyLoginApplication.class, args);
	}


}
