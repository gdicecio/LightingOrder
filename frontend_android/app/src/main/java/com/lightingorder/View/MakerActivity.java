package com.lightingorder.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lightingorder.Controller.AppStateController;
import com.lightingorder.Controller.ConnectivityController;
import com.lightingorder.Controller.UserSessionController;
import com.lightingorder.Model.Data;
import com.lightingorder.Model.RestaurantArea.Order;
import com.lightingorder.Model.RestaurantArea.OrderedItem;
import com.lightingorder.R;
import com.lightingorder.View.Adapters.MakerAdapter;
import com.lightingorder.View.Adapters.OrderObjectAdapter;

import java.util.ArrayList;

public class MakerActivity extends AppCompatActivity {

    ListView order_area_list;
    Button prendi_in_carico;
    Button segnala_completamento;
    TextView nome_prodotto;
    TextView numero_ordine;

    private ArrayList<Order> total_order;
    private ArrayList<OrderObjectAdapter> order_item_list;
    private UserSessionController user_contr = new UserSessionController();
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private int orderID;
    private int line_number;
    private String product_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maker);
        order_area_list = (ListView) findViewById(R.id.order_area_list);

        order_item_list = getOrdersArea();

        MakerAdapter adapter = new MakerAdapter(this, order_item_list);
        order_area_list.setAdapter(adapter);

        order_area_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderObjectAdapter productData = order_item_list.get(position);
                orderID = productData.getOrder_id();
                line_number = productData.getLine_number();
                product_name = productData.getProduct();
                createItemOpPopup(productData.getProduct_state());

            }
        });
    }


    public ArrayList<OrderObjectAdapter> getOrdersArea(){
        order_item_list = new ArrayList<OrderObjectAdapter>();
        total_order = new ArrayList<Order>();
        total_order = Data.getData().getOrdersList(user_contr.getCurrentRole());
        for(Order o : total_order){
            for(OrderedItem oi : o.getOrderedItems()){
                OrderObjectAdapter object_adapter = new OrderObjectAdapter(oi, o);
                order_item_list.add(object_adapter);
            }
        }
        return order_item_list;
    }



    public void createItemOpPopup(String state){
        dialogBuilder = new AlertDialog.Builder(this);
        final View itemOpPopup = getLayoutInflater().inflate(R.layout.popup_chef_item_operation,null);

        prendi_in_carico = (Button) itemOpPopup.findViewById(R.id.button_prendi_in_carico);
        segnala_completamento = (Button) itemOpPopup.findViewById(R.id.button_completamento);
        numero_ordine = (TextView) itemOpPopup.findViewById(R.id.textView_order_ID_popup_chef);
        nome_prodotto = (TextView) itemOpPopup.findViewById(R.id.textView_nome_prodotto_popup_chef);

        numero_ordine.setText(Integer.toString(orderID));
        nome_prodotto.setText(product_name);


        if(state.equals("WaitingForWorking")){
            segnala_completamento.setVisibility(View.INVISIBLE);
            prendi_in_carico.setVisibility(View.VISIBLE);
            dialogBuilder.setView(itemOpPopup);
            dialog = dialogBuilder.create();
            dialog.show();
        }
        else if(state.equals("Working")){
            segnala_completamento.setVisibility(View.VISIBLE);
            prendi_in_carico.setVisibility(View.INVISIBLE);
            dialogBuilder.setView(itemOpPopup);
            dialog = dialogBuilder.create();
            dialog.show();
        }
        else{}
    }


    public void itemCompleted(View view){
        ConnectivityController.sendItemCompletedRequest(user_contr, user_contr.getCurrentProxy(), orderID, line_number);
        dialog.dismiss();
    }


    public void itemWorking(View view){
        ConnectivityController.sendItemWorkingRequest(user_contr, user_contr.getCurrentProxy(), orderID, line_number);
        dialog.dismiss();
    }


    @Override
    protected void onStart() {
        super.onStart();
        AppStateController.getApplication().setCurrent_activity(this);
        Log.d("CURRENT_ACTIVITY",
                "CURRENT ACTIVITY : "+AppStateController.getApplication().getCurrent_activity().getLocalClassName());
    }
}

