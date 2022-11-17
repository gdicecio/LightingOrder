package com.connectivity;

import java.util.ArrayList;

public class ConnectionErrors {
    public enum Error{
        CertificateNotFound,
        CertificateNotTrusted,
        CertificateException,
        CreatingClientSocket,
        CreatingServerSocket,
        ExecutionClient,
        HostUnknown,
        HttpRequestParse,
        HttpResponseParse,
        ServerGetRequest,
        ServerAlreadyExecuted,
        OtherException,
    };

    public static String currentError = "";
    public static ArrayList<String> hystory_errors = new ArrayList<String>();




}
