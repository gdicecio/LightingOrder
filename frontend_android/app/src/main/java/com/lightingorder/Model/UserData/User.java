package com.lightingorder.Model.UserData;

import java.util.HashMap;

//Singleton
public class User {
    private static User istanza = null;
    private  String ID = "";
    private  HashMap<String,String> ruoli_proxy = new HashMap<String,String>();
    private  String ipAddress = "";
    private  String current_role = "";
    private boolean loginSuccessful = false;


    private User() { }

    public static synchronized User getUser(){
        if(istanza == null){
            istanza = new User();
        }
        return istanza;
    }

    public String getID() {
        return istanza.ID;
    }

    public void addRole(String role, String proxy_add){
        istanza.ruoli_proxy.put(role,proxy_add);
    }

    public  void setID(String ID) {
        istanza.ID = ID;
    }

    public boolean checkRole(String role){
        return istanza.getRuoli_proxy().containsKey(role);
    }

    public  HashMap<String, String> getRuoli_proxy() {
        return istanza.ruoli_proxy;
    }

    public  String getIpAddress() {
        return istanza.ipAddress;
    }

    public  void setIpAddress(String ipAddress) { istanza.ipAddress = ipAddress;}

    public String getCurrent_role() {return istanza.current_role;}

    public void setCurrent_role(String current_role) {istanza.current_role = current_role;}

    public boolean isLoginSuccessful() {
        return istanza.loginSuccessful;
    }

    public void setLoginSuccessful(boolean loginSuccessful) {
        istanza.loginSuccessful = loginSuccessful;
    }

}
