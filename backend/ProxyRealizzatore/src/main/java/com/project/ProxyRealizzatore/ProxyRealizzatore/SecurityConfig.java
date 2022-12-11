package com.project.ProxyRealizzatore.ProxyRealizzatore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
public class SecurityConfig {

    @Bean
    public Boolean disableSSLValidation() throws Exception {
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        FileInputStream in = new FileInputStream("C:/Users/giuse/Universita/SecureSystemDesign2022/Progetto/LightingOrder/backend/ProxyRealizzatore/src/main/resources/client.crt");
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Certificate cert = certificateFactory.generateCertificate(in);

        KeyStore key = KeyStore.getInstance(KeyStore.getDefaultType());
        key.load(null, null);
        key.setCertificateEntry("client", cert);
        TrustManagerFactory trust = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trust.init(key);
        sslContext.init(null, trust.getTrustManagers(), null);


        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        return true;
    }
}
