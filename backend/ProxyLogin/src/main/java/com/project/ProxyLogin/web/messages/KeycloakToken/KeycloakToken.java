package com.project.ProxyLogin.web.messages.KeycloakToken;

import java.util.ArrayList;

public class KeycloakToken {
    public float exp;
    public float iat;
    public String jti;
    public String iss;
    public String aud;
    public String sub;
    public String typ;
    public String azp;
    public String session_state;
    public String acr;
    public RealmAccess realm_access;
    public ResourceAccess resource_access;
    public String scope;
    public String sid;
    public boolean email_verified;
    public String name;
    public String preferred_username;
    public String given_name;
    public String family_name;

}
class ResourceAccess {
    public Account account;
}
class Account {
    public ArrayList<String> roles = new ArrayList <> ();

}
