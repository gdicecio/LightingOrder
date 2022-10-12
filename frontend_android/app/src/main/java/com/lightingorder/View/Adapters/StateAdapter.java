package com.lightingorder.View.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lightingorder.Model.StateOp;
import com.lightingorder.R;

import java.util.ArrayList;

public class StateAdapter extends ArrayAdapter<StateOp>{

    private final Activity context;

    public StateAdapter(Activity context, ArrayList<StateOp> s){
        super(context, R.layout.item_list_func, s);
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent){
        StateOp st = getItem(position);

        view = LayoutInflater.from(context).inflate(R.layout.item_list_func, parent, false);

        TextView titleText = (TextView) view.findViewById(R.id.title);
        TextView iconText = (TextView) view.findViewById(R.id.icon);

        titleText.setText(st.getText());
        iconText.setBackgroundColor(st.getColor());

        return view;
    }
}
