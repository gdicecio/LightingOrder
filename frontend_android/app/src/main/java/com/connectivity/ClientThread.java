package com.connectivity;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;

public class ClientThread extends AsyncTask<HttpRequest, Void, HttpResponse> {

    SSLSocket socket;

    public ClientThread(SSLSocket s) {
        Log.d("Client", "Thread created");
        this.socket = s;
    }

    @Override
    protected HttpResponse doInBackground(HttpRequest... httpRequests) {
        Log.d("Client", "Thread executed");
        InetSocketAddress add = new InetSocketAddress(httpRequests[0].getAddress(), httpRequests[0].getPort());
        HttpResponse res = null;
        try {
            Log.d("Client", "New Request to send");
            socket.connect(add);
            socket.startHandshake();

            BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Send
            socket.getOutputStream().write(httpRequests[0].getPacket().getBytes(StandardCharsets.UTF_8));
            Log.d("Client", "Sent:: " + httpRequests[0].getPacket());
            //Receive
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            res = new HttpResponse();
            res.parse(reader);
            socket.close();

        } catch (SSLHandshakeException e){
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.CertificateNotTrusted.name());
        } catch (UnknownHostException e){
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.HostUnknown.name());
        } catch (IOException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());
        }
        return res;
    }
}








/* FUNZIONA
    @Override
    public void run() {
        String path = activity.getApplicationContext().getFilesDir() + "/my_certificate.crt";
        SSLSocketFactory sf = null;
        try {
            CertificateFactory cert_fac= CertificateFactory.getInstance("X.509");
            InputStream in = new FileInputStream(path);
            Certificate cert = cert_fac.generateCertificate(in);
            KeyStore keyStore = null;

            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("server", cert);


            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());


            trustManagerFactory.init(keyStore);


            SSLContext sslContext = null;

            sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            sf = (SSLSocketFactory) sslContext.getSocketFactory();

            SSLSocket s = (SSLSocket) sf.createSocket("192.168.1.101", 8080);

            String message = "GET / HTTP/1.1\r\n" + "Content-Type: plain/text\r\n" + "\r\n";

            s.startHandshake();
            Log.d("Client", "Handshake done");

            BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
            s.getOutputStream().write(message.getBytes(StandardCharsets.UTF_8));

            Log.d("Client", "Requset sent");


            Log.d("Client", "-"+r.readLine());
            String input;
            while(!(input=r.readLine()).isEmpty())
                Log.d("Client", input);

            r.close();
                s.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */


