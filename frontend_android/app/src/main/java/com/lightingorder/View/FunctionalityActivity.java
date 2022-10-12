package com.lightingorder.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.lightingorder.Controller.AppStateController;
import com.lightingorder.Controller.ConnectivityController;
import com.lightingorder.View.Adapters.FunctionalityAdapter;
import com.lightingorder.Controller.UserSessionController;
import com.lightingorder.R;
import com.lightingorder.StdTerms;
import com.lightingorder.Model.Functionality;

import java.util.ArrayList;

public class FunctionalityActivity extends AppCompatActivity {

    ListView lv_function;
    private UserSessionController user_contr = new UserSessionController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functionality);
        ArrayList<Functionality> funs = user_contr.determineFunctionality();

        FunctionalityAdapter adapter = new FunctionalityAdapter(this, funs);
        lv_function = (ListView) findViewById(R.id.function_list);
        lv_function.setAdapter(adapter);

        lv_function.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Functionality f = funs.get(position);

                //Retrieve the ID of the Functionality ==> Use case name
                String use_case =  f.getID();

                //Retrieve the role needed for this use case --> it'll be necessary for TableActivity
                user_contr.setCurrentRole(StdTerms.UC_Role.get(use_case));
                Log.d("USER","Current Role: "+user_contr.getCurrentRole());

                //Retrieve the relative proxy address
                String proxy_addr = user_contr.getHashRuoli_Proxy().get(user_contr.getCurrentRole());
                Log.d("USER","Current Proxy: "+proxy_addr);

                if(use_case.equals(StdTerms.useCases.CreaOrdinazione.name()) ||
                        use_case.equals(StdTerms.useCases.AggiornaStatoTavolo.name())) {
                    ConnectivityController.sendTableRequest(user_contr,proxy_addr);
                    Log.d("ACTIVITY","FUNCTIONALITY ACTIVITY: Table request sent");

                }
                else if(use_case.equals(StdTerms.useCases.VisualizzaOrdinazioniBar.name()) ||
                        use_case.equals(StdTerms.useCases.VisualizzaOrdinazioniCucina.name()) ||
                        use_case.equals(StdTerms.useCases.VisualizzaOrdinazioniForno.name()) ){
                    ConnectivityController.sendOrderRequest(user_contr,user_contr.getCurrentProxy(),
                            user_contr.getCurrentRole());
                    Log.d("ACTIVITY","FUNCTIONALITY ACTIVITY: Order request sent");
                }
            }
        });
        backgroundUpdateModel();
    }

    private void backgroundUpdateModel(){
        if(user_contr.checkRole(StdTerms.roles.Cameriere.name())){
            String proxy_cameriere = user_contr.getHashRuoli_Proxy().get(StdTerms.roles.Cameriere.name());

            ConnectivityController.sendMenuRequest(
                    user_contr,
                    proxy_cameriere);
            Log.d("ACTIVITY","FUNCTIONALITY ACTIVITY: Menu request sent");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        AppStateController.getApplication().setCurrent_activity(this);
        Log.d("CURRENT_ACTIVITY",
                "CURRENT ACTIVITY : "+AppStateController.getApplication().getCurrent_activity().getLocalClassName());
    }


}
