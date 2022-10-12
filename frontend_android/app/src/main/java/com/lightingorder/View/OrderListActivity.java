package com.lightingorder.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
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
import com.lightingorder.StdTerms;
import com.lightingorder.View.Adapters.OrderListAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {

    ListView order_list;
    TextView table_number;
    TextView room_number;
    Button addOrder;
    ListView product_list_view;
    Button deleteOrder;
    Button deleteProducts;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ArrayList<Order> orderList;
    private UserSessionController user_contr = new UserSessionController();
    private String tableID;
    private int roomNumber;
    private List<OrderedItem> orderedProducts;
    private int orderID;
    private OrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        tableID = UserSessionController.table_selected;
        roomNumber = UserSessionController.roomNumber_selected;

        orderList = Data.getData().getTableOrders(tableID,roomNumber);

        OrderListAdapter adapter = new OrderListAdapter(this, orderList);

        order_list = (ListView) findViewById(R.id.orderlist);
        table_number = (TextView) findViewById(R.id.table_number_order_list);
        room_number = (TextView) findViewById(R.id.room_number_orderlist);
        addOrder = (Button) findViewById(R.id.aggiungi_orderlist);
        table_number.setText(tableID);
        room_number.setText(Integer.toString(roomNumber));

        String tableState = Data.getData().getTable(tableID,roomNumber).getTableState();
        if(tableState.equals(StdTerms.statesList.free.name()) ||
                tableState.equals(StdTerms.statesList.reserved.name()))
            addOrder.setVisibility(View.INVISIBLE);
        else
            addOrder.setVisibility(View.VISIBLE);



        order_list.setAdapter(adapter);
        order_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Order ord = orderList.get(position);
                orderID = ord.getOrderID();
                Log.d("ACTIVITY", "ORDER LIST ACTIVITY: Order selected"+ Integer.toString(orderID));
                createCancelOrderPopup(orderID);
                return true;
            }
        });

    }


    public void addOrder(View view){
        Intent i = new Intent(getApplicationContext(), OrderActivity.class);
        startActivity(i);
    }


    private void createCancelOrderPopup(int orderID) {

        orderedProducts = Data.getData().getOrderedItemsForOrder(tableID,roomNumber,orderID);
        dialogBuilder = new AlertDialog.Builder(this);
        final View cancelOrderPopupView = getLayoutInflater().inflate(R.layout.popup_order_management,null);

        product_list_view = (ListView) cancelOrderPopupView.findViewById(R.id.list_view_prodotti);
        deleteOrder = (Button) cancelOrderPopupView.findViewById(R.id.delete_order_button);
        deleteProducts = (Button) cancelOrderPopupView.findViewById(R.id.delete_product_button);

        product_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        product_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView v = (CheckedTextView) view;
                boolean currentCheck = v.isChecked();
            }
        });

        ArrayAdapter<OrderedItem> arrayAdapter = new ArrayAdapter<OrderedItem>(this,
                android.R.layout.simple_list_item_checked ,orderedProducts);
        product_list_view.setAdapter(arrayAdapter);
        for(int i=0;i< orderedProducts.size(); i++ )  {
            this.product_list_view.setItemChecked(i,true);
        }
        dialogBuilder.setView(cancelOrderPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

    }

    public void deleteOrder(View view){
        ConnectivityController.sendCancelOrderRequest(user_contr,user_contr.getCurrentProxy(),orderID);
        Log.d("ACTIVITY", "ORDER LIST ACTIVITY: Cancel order request sent");
        dialog.dismiss();
    }

    public void deleteProduct(View view){
        int productLineNumber = orderedProducts.get((product_list_view.getCheckedItemPosition())).getLineNumber();
        ConnectivityController.sendCancelOrderedItemRequest(user_contr,user_contr.getCurrentProxy(),orderID,productLineNumber);
        Log.d("ACTIVITY", "ORDER LIST ACTIVITY: Cancel ordered item request sent");
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
