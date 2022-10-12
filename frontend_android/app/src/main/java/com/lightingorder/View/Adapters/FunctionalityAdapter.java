package com.lightingorder.View.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lightingorder.Model.Functionality;
import com.lightingorder.R;

import java.util.ArrayList;

public class FunctionalityAdapter extends ArrayAdapter<Functionality> {

    private final Activity context;

    public FunctionalityAdapter(Activity context, ArrayList<Functionality> f){
        super(context, R.layout.item_list_func, f);
        this.context = context;
    }

    public View getView(int position, View view, ViewGroup parent){
        Functionality fun = getItem(position);

        view = LayoutInflater.from(context).inflate(R.layout.item_list_func, parent, false);

        TextView titleText = (TextView) view.findViewById(R.id.title);
        TextView iconText = (TextView) view.findViewById(R.id.icon);

        titleText.setText(fun.getText());
        iconText.setBackgroundColor(fun.getColor());

        return view;
    }
}
