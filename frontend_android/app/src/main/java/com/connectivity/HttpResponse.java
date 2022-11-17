package com.connectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HttpResponse {

    int code;
    String result;
    private HashMap<String, String> headers;
    String body;
    private final String version = "HTTP/1.1";


    public HttpResponse(){
        headers = new HashMap<String, String>();
        setHeader("Server", "Android");
        this.body = "";
    }

    public void reset(){
        this.code = 0;
        this.headers = new HashMap<String, String>();
        this.body = "";
        this.result = "";
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
            ConnectionErrors.hystory_errors.add(ConnectionErrors.Error.HttpResponseParse.name());
        }
    }

    public int getBodySize(){
        int size = -1;
        if(headers.containsKey("Content-Length")){
            size = Integer.valueOf(headers.get("Content-Length"));
        }
        return size;
    }

    private void addHeader(String header){
        int idx = header.indexOf(":");
        if(idx!=-1)
            headers.put(header.substring(0,idx), header.substring(idx+2)); //Dopo i ":" c'è lo spazio per descrivere il campo dell'header
    }

    private void parseStartLine(String start) {
        StringTokenizer parser = new StringTokenizer(start, " ");
        parser.nextToken();
        this.code = Integer.valueOf(parser.nextToken());
        String res = "";
        while(parser.hasMoreTokens()){
            res += parser.nextToken() + " ";
        }
        this.result = res;
    }

    public void setCode(int code){
        switch (code){
            case 200:
                this.result = "OK\r\n";
                break;
            case 404:
                this.result = "Not Found\r\n";
                break;
            case 500:
                this.result = "Internal Server Error\r\n";
                break;
            default:
                this.result = "Unspecified\r\n";
        }
    }
    public int getCode(){return this.code;}

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    public void setHeader(String key, String value){
        this.headers.put(key, value);
    }

    public void setBody(String message){
        this.body = message;
    }
    public String getBody(){return this.body;}

    public String getPacket(){
        String start_line = version + " " + code + " " + result;
        String header = "";
        for(String key : headers.keySet()){
            header += key + ": " + headers.get(key) + "\r\n";
        }
        return start_line + header + "\r\n" + body;
    }

}
