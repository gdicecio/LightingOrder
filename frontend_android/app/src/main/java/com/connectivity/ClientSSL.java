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
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class ClientSSL {

    SSLSocket socket;
    String certificate_path;
    String certificate_name;
    ClientThread client;
    int timeout;
    private String files_dir = "/data/data/com.lightingorder/files";

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
            res = client.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e){
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.ClientTimeout.name());
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
            String path = "/data/data/com.lightingorder/files/certificate.p12";

            //KeyStore con il certificato del server
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("server", cert);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            //KeyStore con il certificato del client (app Android)
            KeyStore ks = KeyStore.getInstance("PKCS12");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
            char[] passphrase = "1111".toCharArray();
            ks.load(new FileInputStream(path), passphrase);
            kmf.init(ks, passphrase);

            //Inizializzazione di SSL con mutua autenticazione
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), null); //sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

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
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }

        return sf;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}