package com.connectivity;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class ServerSSL {

    SSLServerSocket serverSocket;
    SSLServerSocketFactory ssf;
    ServerThread sthread;
    private String certificate_path;

    int serverPort;
    boolean serverSecurity;

    private HashMap<String, HttpResponseCallback> post_mapping;

    public ServerSSL(int port, String certificate){
        this.serverPort = port;
        this.certificate_path = certificate;

        this.post_mapping = new HashMap<String, HttpResponseCallback>();

        try {
            ssf = getServerSocketFactory();
            serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
        } catch (BindException e){
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.ServerAlreadyExecuted.name());
        } catch (IOException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.CreatingServerSocket.name());
        }

        sthread = new ServerThread(serverSocket, post_mapping);

        Log.d("Server", "Server created!");

    }

    public void listen(){
        sthread.execute();
        Log.d("Server", "Server starded!");
    }
    public void stop(){
        sthread.cancel(true);
    }

    public int getPort(){
        return serverPort;
    }

    public void onDestroy(){
        if(serverSocket != null){
            try{
                serverSocket.close();
                sthread.cancel(true);
            } catch (IOException e){
                ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());
            }
        }
    }

    public SSLServerSocketFactory getServerSocketFactory(){
        /**
         * Metodo per utilizzare un certificato .p12 per la crittografia
         * */
        SSLServerSocketFactory ssf = null;
        SSLContext ctx;
        KeyManagerFactory kmf;
        KeyStore ks;
        char[] passphrase = "1111".toCharArray();

        try {
            ctx = SSLContext.getInstance("TLS");
            kmf = KeyManagerFactory.getInstance("X509");
            ks = KeyStore.getInstance("PKCS12");
            String cert = certificate_path;

            ks.load(new FileInputStream(cert), passphrase);
            kmf.init(ks, passphrase);

            CertificateFactory cert_fac = CertificateFactory.getInstance("X.509");
            InputStream in = new FileInputStream("/data/data/com.lightingorder/files/my_certificate.crt");
            Certificate certificate = cert_fac.generateCertificate(in);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("server", certificate);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            ctx.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            ssf = ctx.getServerSocketFactory();
            Log.d("Server", "Certificate loaded!");

        } catch (UnrecoverableKeyException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());;
        } catch (FileNotFoundException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.CertificateNotFound.name());
        } catch (CertificateException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.CertificateException.name());
        } catch (NoSuchAlgorithmException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());
        } catch (KeyStoreException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());
        } catch (IOException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());;
        } catch (KeyManagementException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());
        }
        return ssf;
    }

    public void postMapping(String path, HttpResponseCallback callback){
        post_mapping.put(path, callback);
    }

}
