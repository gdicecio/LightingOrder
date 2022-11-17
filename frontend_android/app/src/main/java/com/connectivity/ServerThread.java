package com.connectivity;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;


public class ServerThread extends AsyncTask<Void, Void, Void> {

    SSLSocket socket;
    SSLServerSocket serverSocket;
    HashMap<String, HttpResponseCallback> posts;


    ServerThread(SSLServerSocket ss, HashMap<String, HttpResponseCallback> post_mapping){
        this.serverSocket = ss;
        posts = post_mapping;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        HttpRequest req;
        HttpResponse res;
        while (true) {
            try {
                Log.d("Server", "Waiting for requests ...");
                this.socket = (SSLSocket) this.serverSocket.accept();

                res = new HttpResponse();
                req = getRequest();

                Log.d("Server", "New Requester accepted: " + req.getPacket());
                if(posts.containsKey(req.getPath()))
                    res = posts.get(req.getPath()).onRequest(req);
                else{
                    res.setCode(404);
                    res.setHeader("Content-Type", "text/plain");
                    res.setBody("Path not found!");
                }

                sendResponse(res);

                socket.close();


            } catch (Exception e) {
                ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.OtherException.name());
            }

        }

    }

    private HttpRequest getRequest(){
        HttpRequest req = new HttpRequest();
        BufferedReader in = null; //ATTENZIONE Chiudere il reader = Chiudere la socket
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            req.parse(in);
        } catch (IOException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.ServerGetRequest.name());
        }

        return req;
    }

    private void sendResponse(HttpResponse res) throws IOException {
        this.socket.getOutputStream().write(res.getPacket().getBytes(StandardCharsets.UTF_8));
    }

}
