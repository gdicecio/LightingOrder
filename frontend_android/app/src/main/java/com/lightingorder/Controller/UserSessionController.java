package com.lightingorder.Controller;

import android.graphics.Color;

import com.lightingorder.Model.Functionality;
import com.lightingorder.Model.UserData.User;
import com.lightingorder.StdTerms;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSessionController {
    private User utente;
    public static String table_selected;
    public static int roomNumber_selected;

    public UserSessionController() {
        this.utente = User.getUser();
    }

    public void addRoleAndProxy(String role, String proxy_address){
        utente.addRole(role,proxy_address);
    }

    public void setUserID(String ID){ utente.setID(ID);}

    public void setUserIpAddress(String IP){
        utente.setIpAddress(IP);
    }

    public String getUserID(){
        return utente.getID();
    }

    public String getUserIpAddress(){
        return utente.getIpAddress();
    }

    public boolean checkRole(String role){
        return utente.checkRole(role);
    }

    public HashMap<String,String> getHashRuoli_Proxy(){
        return utente.getRuoli_proxy();
    }

    public String getCurrentRole(){ return utente.getCurrent_role();}

    public String getCurrentProxy(){ return utente.getRuoli_proxy().get(utente.getCurrent_role());}

    public void setCurrentRole(String new_role){ utente.setCurrent_role(new_role);}


    //This function below determines the functionality of an user basing on the list of his roles
    public ArrayList<Functionality> determineFunctionality(){

        ArrayList<Functionality> funs = new ArrayList<Functionality>();

        for(String r : utente.getRuoli_proxy().keySet()){

            if(r.equals(StdTerms.roles.Bar.name())){

                funs.add(new Functionality("Visualizza Oridnazioni Bar",
                        Color.GREEN,
                        StdTerms.useCases.VisualizzaOrdinazioniBar.name()));

            } else if (r.equals(StdTerms.roles.Accoglienza.name())){

                funs.add(new Functionality("Aggiorna Stato Tavoli",
                        Color.BLUE,
                        StdTerms.useCases.AggiornaStatoTavolo.name()));

            } else if (r.equals(StdTerms.roles.Cameriere.name())){

                funs.add(new Functionality("Gestisci Ordinazioni",
                        Color.WHITE,
                        StdTerms.useCases.CreaOrdinazione.name()));

            } else if (r.equals(StdTerms.roles.Cucina.name())){

                funs.add(new Functionality("Visualizza Ordinazioni Cucina",
                        Color.DKGRAY,
                        StdTerms.useCases.VisualizzaOrdinazioniCucina.name()));

            } else if (r.equals(StdTerms.roles.Forno.name())){

                funs.add(new Functionality("Visualizza Ordinazioni Forno",
                        Color.YELLOW,
                        StdTerms.useCases.VisualizzaOrdinazioniForno.name()));
            }
        }
        return funs;
    }

    public boolean getLoginResult(){return utente.isLoginSuccessful();}

    public void setloginResult(boolean result){ utente.setLoginSuccessful(result);}
}
