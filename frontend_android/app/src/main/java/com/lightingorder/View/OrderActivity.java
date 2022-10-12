package com.lightingorder.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lightingorder.Controller.AppStateController;
import com.lightingorder.Controller.ConnectivityController;
import com.lightingorder.Controller.UserSessionController;
import com.lightingorder.Model.Data;
import com.lightingorder.R;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    Spinner prodotto;
    Spinner merceDaAggiungere;
    Spinner merceDaSottrarre;
    TextView numero_tavolo;
    TextView numero_sala;
    EditText priorita;

    private UserSessionController user_contr = new UserSessionController();
    private String tableID;
    private int roomNumber;
    private List<List<String>> additive = new ArrayList<>();
    private List<List<String>> subtract = new ArrayList<>();
    private List<String> items_name = new ArrayList<>();
    private List<Integer> priority  = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        String[] items = Data.getData().getMenuItemsName();
        String[] merce_additiva = {"No selection","Patate","Origano","Aglio","Funghi","Olive"};

        tableID = UserSessionController.table_selected;
        roomNumber = UserSessionController.roomNumber_selected;

        prodotto = (Spinner) findViewById(R.id.prodotto_spinner);
        merceDaAggiungere = (Spinner) findViewById(R.id.additive_spinner);
        merceDaSottrarre= (Spinner) findViewById(R.id.subGoods_spinner);
        numero_tavolo = (TextView) findViewById(R.id.table_number);
        numero_sala = (TextView) findViewById(R.id.room_number);
        priorita = (EditText) findViewById(R.id.priorita);
        numero_sala.setText(Integer.toString(roomNumber));
        numero_tavolo.setText(tableID);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        prodotto.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, merce_additiva);
        merceDaAggiungere.setAdapter(adapter2);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, merce_additiva);
        merceDaSottrarre.setAdapter(adapter3);

    }

    public void addItem(View view){

        List<String> tmp_additive = new ArrayList<>();
        List<String> tmp_subtract = new ArrayList<>();

        if(!(prodotto.getSelectedItem().toString().equals("No selection") || (priorita.getText().toString().equals("")))) {
            items_name.add(prodotto.getSelectedItem().toString());
            priority.add(Integer.parseInt(priorita.getText().toString()));

            if(!(merceDaAggiungere.getSelectedItem().toString().equals("No selection")))
                tmp_additive.add(merceDaAggiungere.getSelectedItem().toString());
            additive.add(tmp_additive);

            if(!(merceDaSottrarre.getSelectedItem().toString().equals("No selection")))
                tmp_subtract.add(merceDaSottrarre.getSelectedItem().toString());
            subtract.add(tmp_subtract);

            Toast.makeText(getApplicationContext(),"Prodotto inserito",Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(getApplicationContext(),"Selezionare un prodotto e la priorità",Toast.LENGTH_SHORT).show();
    }


    public void addOrder(View view) {

        if (items_name.size() == 0) {
            Toast.makeText(getApplicationContext(), "Inserire almeno un prodotto nell'ordine", Toast.LENGTH_SHORT).show();
        } else {
            ConnectivityController.sendAddOrderToTableRequest(
                    user_contr,
                    user_contr.getCurrentProxy(),
                    tableID,
                    roomNumber,
                    items_name,
                    additive,
                    subtract,
                    priority);

            Log.d("ACTIVITY", "ORDER ACTIVITY: Add order request sent");
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
