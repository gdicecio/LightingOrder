package com.lightingorder.Controller;

import android.app.Activity;
import android.content.Context;

//Singleton
public class AppStateController {
    private static AppStateController istanza = null;
    private Activity current_activity;
    private int proxyConnectionState;        //Code of proxy response

    private AppStateController(){}

    public static synchronized AppStateController getApplication(){
        if(istanza == null){
            istanza = new AppStateController();
        }
        return istanza;
    }

    public  Activity getCurrent_activity(){
        return istanza.current_activity;
    }

    public void setCurrent_activity(Activity activity){
        istanza.current_activity = activity;
    }

    public Context getCurrentContext(){
        return istanza.current_activity.getApplicationContext();
    }

    public int getProxyConnectionState() {return istanza.proxyConnectionState; }

    public void setProxyConnectionState(int connectionState) {istanza.proxyConnectionState = connectionState;}

    public boolean connectionStateIsOK(){
        if(istanza.proxyConnectionState >= 200 && istanza.proxyConnectionState < 300)
            return true;
        else return false;
    }
}