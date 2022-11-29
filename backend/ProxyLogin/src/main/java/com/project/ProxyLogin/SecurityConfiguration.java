package com.project.ProxyLogin;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.security.cert.Certificate;

@Configuration
public class SecurityConfiguration {
    @Value(value = "${passKeystore}")
    public String passKeystore;

    public String getPassKeystore() {
        return passKeystore;
    }

    public void setPassKeystore(String passKeystore) {
        this.passKeystore = passKeystore;
    }

}
