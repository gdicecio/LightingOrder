package com.connectivity;

import android.os.AsyncTask;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class ClientSSL {

    SSLSocket socket;
    String certificate_path;
    String certificate_name;
    ClientThread client;
    private String files_dir = "/data/user/0/com.example.server_https/files";

    public ClientSSL() {
        certificate_path = files_dir + "/my_certificate.crt"; //FOR LOCAL SERVER
    //    certificate_path = files_dir + "/server.crt"; //FOR TOMCAT homework
        try {
            SSLSocketFactory sf = getSocketFactory();
            socket = (SSLSocket) sf.createSocket();
        } catch (Exception e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.CreatingClientSocket.name());
        }

        client = new ClientThread(socket);
    }

    public ClientSSL(String certificate){
        certificate_name = certificate;
        certificate_path = files_dir + "/" +certificate;
        try {
            SSLSocketFactory sf = getSocketFactory();
            socket = (SSLSocket) sf.createSocket();
        } catch (Exception e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.CreatingClientSocket.name());
        }

        client = new ClientThread(socket);
    }

    public ClientSSL(String certificate, String directory){
        certificate_name = certificate;
        certificate_path = directory + "/" +certificate;
        try {
            SSLSocketFactory sf = getSocketFactory();
            socket = (SSLSocket) sf.createSocket();
        } catch (Exception e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.CreatingClientSocket.name());
        }

        client = new ClientThread(socket);
    }

    public void setCertificateDir(String path){
        this.files_dir = path;
        this.certificate_path = this.files_dir + "/" + this.certificate_name;
    }

    public HttpResponse send(HttpRequest req)  {
        HttpResponse res = null;
        client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, req); //Esecuzione parallela
        Log.d("Client", "Request sent");
        try {
            res = client.get();
        } catch (Exception e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.ExecutionClient.name());
        }
        return res;
    }

    public SSLSocketFactory getSocketFactory() {
        /** Questo metodo serve per autentiare il server.
         * Quando si fa una richiesta, la socket effettua un check con il certificato che gli passo
         * */
        SSLSocketFactory sf = null;
        try {

            /*Carico il cerificato. In teoria deve essere lo stesso del server*/
            CertificateFactory cert_fac = CertificateFactory.getInstance("X.509");
            InputStream in = new FileInputStream(certificate_path);
            Certificate cert = cert_fac.generateCertificate(in);
            KeyStore keyStore = null;

            /*Lo carico in KeyStore. KeyStore è una struttura dati capace di contenere chiavi, certificati, algoritmi ecc*/
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("server", cert);

            /*TrustManager serve per fidarsi del certificato che si ottiene durante l'handshake*/
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);


            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            sf = (SSLSocketFactory) sslContext.getSocketFactory();

        } catch (FileNotFoundException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.CertificateNotFound.name());
        } catch (CertificateException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.CertificateException.name());
        } catch (KeyStoreException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());
        } catch (IOException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());
        } catch (NoSuchAlgorithmException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());
        } catch (KeyManagementException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());
        }

        return sf;
    }
}