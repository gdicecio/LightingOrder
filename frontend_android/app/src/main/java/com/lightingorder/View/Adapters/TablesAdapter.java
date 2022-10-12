package com.lightingorder.View.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lightingorder.Model.RestaurantArea.Table;
import com.lightingorder.R;
import com.lightingorder.StdTerms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TablesAdapter extends ArrayAdapter<Table>{

    private final Activity context;

    static final private Map<String, Integer> color;

    static {
        Map <String, Integer> aMap = new HashMap<String, Integer>();
        aMap.put(StdTerms.statesList.waitingForOrders.name(),StdTerms.orange);//YELLOW
        aMap.put(StdTerms.statesList.Occupied.name(), StdTerms.red);//RED
        aMap.put(StdTerms.statesList.free.name(), StdTerms.green);//GREEN
        aMap.put(StdTerms.statesList.reserved.name(), StdTerms.blue);//BLUE
        color = Collections.unmodifiableMap(aMap);
    }


    public TablesAdapter(Activity context, ArrayList<Table> t){
            super(context, R.layout.tables_item_list, t);
            this.context = context;
        }

        public View getView(int position, View view, ViewGroup parent){
            Table tab = getItem(position);

            view = LayoutInflater.from(context).inflate(R.layout.tables_item_list, parent, false);

            TextView iconText = (TextView) view.findViewById(R.id.icon);

            iconText.setText(tab.getIdAndState());
            iconText.setBackgroundColor(color.get(tab.getTableState()));

            return view;
        }
}
