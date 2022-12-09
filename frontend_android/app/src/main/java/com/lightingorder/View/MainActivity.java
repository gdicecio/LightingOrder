package com.lightingorder.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.connectivity.ConnectionErrors;
import com.connectivity.HttpResponse;
import com.google.gson.Gson;
import com.lightingorder.Controller.ConnectivityController;
import com.lightingorder.Controller.AppStateController;
import com.lightingorder.Controller.UserSessionController;
import com.lightingorder.Model.messages.KeyCloakToken;
import com.lightingorder.R;
import com.lightingorder.StdTerms;
import com.lightingorder.Model.messages.loginRequest;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText ed_user_id, ed_user_password, proxy;
    Button b_login;
    private UserSessionController user_contr = new UserSessionController();
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppStateController.getApplication().setCurrent_activity(this);
        setContentView(R.layout.activity_main);
        ed_user_id = (EditText) findViewById(R.id.user_id);
        ed_user_password = (EditText) findViewById(R.id.user_password);
        proxy = (EditText) findViewById(R.id.proxy);
        b_login = (Button) findViewById(R.id.login);
        ConnectivityController.getConnectivity().configPostMapping();
        ConnectivityController.getConnectivity().startServer();
    }

    public void sendLogin(View view){

        String username = ed_user_id.getText().toString();
        String password = ed_user_password.getText().toString();

        user_contr.setUserID(username);
        String ipAddress = null;

        if(!StdTerms.manual_ip_address) {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        } else
            ipAddress = StdTerms.ipAddress;

        String proxyLoginAddress = proxy.getText().toString() + ":" + StdTerms.proxyLoginPort + StdTerms.proxyLoginPath;
        Log.d("myIP", ipAddress);
        Log.d("loginIP", proxyLoginAddress);
        user_contr.setUserIpAddress(ipAddress+":"+(StdTerms.server_port));

        HttpResponse res = ConnectivityController.sendLoginRequest(user_contr, password, proxyLoginAddress);
        Log.d("ACTIVITY","MAIN ACTIVITY: Login request sent");
        Log.d("ACTIVITY:", "MAIN ACTIVITY: Login response: " + res.getCode() + res.getResult() );

        if(res.getCode() >= 200 && res.getCode() < 300) {
            while (user_contr.getHashRuoli_Proxy().size() == 0) ; //Aspetto infinitamente di ricevere i ruoli

            if (AppStateController.getApplication().connectionStateIsOK()) {
                if (user_contr.getHashRuoli_Proxy().size() > 0) {
                    user_contr.setloginResult(true);
                    Intent i = new Intent(getApplicationContext(), FunctionalityActivity.class);
                    startActivity(i);
                    Log.d("LOGIN", "LOGIN successful");
                } else {
                    user_contr.setloginResult(false);
                    Log.d("LOGIN", "LOGIN failed");
                }
            } else {
                Log.d("PROXY", "MAIN ACTIVITY: Proxy login not reachable, login failed");
                user_contr.setloginResult(false);
            }
        } else{
            AppStateController.getApplication().getCurrent_activity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast t = Toast.makeText(AppStateController.getApplication().getCurrent_activity(), "Codice: "+res.getCode(), Toast.LENGTH_LONG);
                    t.show();
                }
            });
        }
    }

        /*
        new CountDownTimer(3000, 1000) {
            public void onFinish() {
                // When timer is finished // Execute your code here
                if(AppStateController.getApplication().connectionStateIsOK()){
                    if(user_contr.getHashRuoli_Proxy().size() > 0) {
                        user_contr.setloginResult(true);
                        Intent i = new Intent(getApplicationContext(), FunctionalityActivity.class);
                        startActivity(i);
                        Log.d("LOGIN","LOGIN successful");
                    }
                    else{
                        user_contr.setloginResult(false);
                        Log.d("LOGIN","LOGIN failed");
                    }
                }
                else {
                    Log.d("PROXY","MAIN ACTIVITY: Proxy login not reachable, login failed");
                    user_contr.setloginResult(false);
                }
            }
            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
         */

    @Override
    protected void onStart() {
        super.onStart();
        AppStateController.getApplication().setCurrent_activity(this);
        Log.d("CURRENT_ACTIVITY",
                "CURRENT ACTIVITY : "+AppStateController.getApplication().getCurrent_activity().getLocalClassName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectivityController.stopServer();
    }
}

