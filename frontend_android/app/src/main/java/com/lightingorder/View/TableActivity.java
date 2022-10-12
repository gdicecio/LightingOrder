package com.lightingorder.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lightingorder.Controller.AppStateController;
import com.lightingorder.Controller.ConnectivityController;
import com.lightingorder.Controller.UserSessionController;
import com.lightingorder.Model.Data;
import com.lightingorder.Model.UserData.User;
import com.lightingorder.R;
import com.lightingorder.StdTerms;
import com.lightingorder.Model.StateOp;
import com.lightingorder.View.Adapters.StateAdapter;
import com.lightingorder.Model.RestaurantArea.Table;
import com.lightingorder.View.Adapters.TablesAdapter;

import java.util.ArrayList;

public class TableActivity extends AppCompatActivity {

    GridView tables_view;

    private UserSessionController user_contr = new UserSessionController();
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ListView lv_states;
    private ArrayList<StateOp> operationsAvlbl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table_map);
        tables_view = (GridView) findViewById(R.id.tables);
        //Retrieve data from local memory
        ArrayList<Table> tables = Data.getData().getTablesList();
        TablesAdapter tab_adap = new TablesAdapter(this, tables);
        tables_view.setAdapter(tab_adap);

        tables_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Table tab = tables.get(position);

                if (user_contr.getCurrentRole().equals(StdTerms.roles.Cameriere.name())) {
                    Intent i = new Intent(getApplicationContext(), OrderListActivity.class);
                    UserSessionController.table_selected = tab.getTableID();
                    UserSessionController.roomNumber_selected = tab.getTableRoomNumber();
                    startActivity(i);
                }
                else if(user_contr.getCurrentRole().equals(StdTerms.roles.Accoglienza.name())) {
                    createNewContactDialog(tab.getTableID(),tab.getTableRoomNumber(),tab.getTableState());
                }
            }
        });
    }


    //pop-up
    private void createNewContactDialog(String tableID, int tableRoom, String currentTableState){
        dialogBuilder = new AlertDialog.Builder(this);
        final View changeTableStatePopUpView = getLayoutInflater().inflate(R.layout.popup_table_operation, null);
        operationsAvlbl = new ArrayList<StateOp>();

        if(currentTableState.equals(StdTerms.statesList.free.name())){
            operationsAvlbl.add(new StateOp("Riserva tavolo", StdTerms.blue, StdTerms.statesList.reserved.name()));
            operationsAvlbl.add(new StateOp("Occupa tavolo", StdTerms.red, StdTerms.statesList.Occupied.name()));
        }
        else if(currentTableState.equals(StdTerms.statesList.Occupied.name()) ||
                currentTableState.equals(StdTerms.statesList.waitingForOrders.name())){
            operationsAvlbl.add(new StateOp("Libera tavolo", StdTerms.green, StdTerms.statesList.free.name()));
        }

        StateAdapter adapter = new StateAdapter(this, operationsAvlbl);
        lv_states = (ListView) changeTableStatePopUpView.findViewById(R.id.states_list);
        lv_states.setAdapter(adapter);

        dialogBuilder.setView(changeTableStatePopUpView);
        dialog = dialogBuilder.create();
        dialog.show();

        lv_states.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StateOp s = operationsAvlbl.get(position);
                ConnectivityController.sendTableOperationRequest(user_contr,
                        user_contr.getCurrentProxy(),tableID,tableRoom,s.getID());
                dialog.dismiss();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppStateController.getApplication().setCurrent_activity(this);
        Log.d("CURRENT_ACTIVITY",
                "CURRENT ACTIVITY : "+AppStateController.getApplication().getCurrent_activity().getLocalClassName());
    }

}