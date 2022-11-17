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

import com.google.gson.Gson;
import com.lightingorder.Controller.ConnectivityController;
import com.lightingorder.Controller.AppStateController;
import com.lightingorder.Controller.UserSessionController;
import com.lightingorder.R;
import com.lightingorder.StdTerms;
import com.lightingorder.Model.messages.loginRequest;

public class MainActivity extends AppCompatActivity {

    EditText ed_user_id, ed_user_password;
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
        b_login = (Button) findViewById(R.id.login);
        ConnectivityController.getConnectivity().configPostMapping();
        ConnectivityController.getConnectivity().startServer();
    }

    public void sendLogin(View view){

        user_contr.setUserID(ed_user_id.getText().toString());
        String ipAddress = null;

        if(!StdTerms.manual_ip_address) {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            ipAddress = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        } else
            ipAddress = StdTerms.ipAddress;

        Log.d("myIP", ipAddress);
        Log.d("loginIP", StdTerms.proxyLoginAddress);
        user_contr.setUserIpAddress(ipAddress+":"+(StdTerms.server_port));
        //user_contr.setUserIpAddress("192.168.42.129:5000");
        //user_contr.setUserIpAddress("192.168.1.130:5000");

        ConnectivityController.sendLoginRequest(user_contr, ed_user_password.getText().toString());
        Log.d("ACTIVITY","MAIN ACTIVITY: Login request sent");

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppStateController.getApplication().setCurrent_activity(this);
        Log.d("CURRENT_ACTIVITY",
                "CURRENT ACTIVITY : "+AppStateController.getApplication().getCurrent_activity().getLocalClassName());
    }
}

