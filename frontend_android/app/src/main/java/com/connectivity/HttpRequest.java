package com.connectivity;


import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HttpRequest {

    private String address;
    private int port;
    private String path;
    private String method;
    private HashMap<String, String> headers;
    private String body;
    private final String version = "HTTP/1.1";


    HttpRequest(){
        headers = new HashMap<String, String>();
        body = "";
        address = "0.0.0.0";
        port = 0;
    }

    public HttpRequest(String address, int port, String path){
        this.address = address;
        this.port = port;
        this.path = path;
        this.headers = new HashMap<String, String>();
        this.body = "";
    }

    public void reset(){
        this.address = "0.0.0.0";
        this.port = 0;
        this.path = "";
        headers = new HashMap<String, String>();
        this.body = "";
        this.method = "";
    }

    public void parse(BufferedReader parser){

        try {
            parseStartLine(parser.readLine());

        String header;
        while( !(header=parser.readLine()).isEmpty() && header.length()>0 ){
            addHeader(header);
        }

        int length = getBodySize();
        if(length>0) {
            char[] body = new char[length];
            parser.read(body, 0, length);
            this.body = String.valueOf(body);
        } else
            this.body = "";

        } catch (IOException e) {
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.HttpRequestParse.name());
        }
    }

    private void parseStartLine(String start){
        StringTokenizer parser = new StringTokenizer(start, " ");
        this.method = parser.nextToken().toUpperCase();
        this.path = parser.nextToken();
    }

    private void addHeader(String header){
        int idx = header.indexOf(":");
        if(idx!=-1)
            headers.put(header.substring(0,idx), header.substring(idx+2)); //Dopo i ":" c'è lo spazio per descrivere il campo dell'header
    }

    public String getHeader(String id) {
        if(headers.containsKey(id))
            return headers.get(id);
        else
            return null;
    }
    public void setHeader(String id, String value){
        this.headers.put(id, value);
    }

    public void setPort(int port){
        this.port = port;
    }
    public int getPort(){return  this.port;}

    public String getAddress(){return this.address;}
    public void setAddress(String address){this.address = address;}

    public String getPath(){
        return this.path;
    }
    public void setPath(String path){
        this.path = path;
    }

    public String getBody(){
        return body;
    }
    public void setBody(String body){
        this.body = body;
    }
    public int getBodySize(){
        int size = -1;
        if(headers.containsKey("Content-Length")){
            size = Integer.valueOf(headers.get("Content-Length"));
        }
        return size;
    }

    public String getMethod(){
        return this.method;
    }
    public void setMethod(String method){
        this.method = method;
    }

    public String getContentType(){
        return getHeader("Content-Type");
    }

    public String getPacket(){
        String message = method + " " + path + " " + version + "\r\n";
        for(String key : this.headers.keySet()){
            message += key + ": " + this.headers.get(key) +"\r\n";
        }
        message += "\r\n";
        message += this.body;
        return message;
    }
}
