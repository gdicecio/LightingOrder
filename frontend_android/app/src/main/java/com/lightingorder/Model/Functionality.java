package com.lightingorder.Model;

import android.graphics.Color;

public class Functionality {

    private String ID;
    private String text;
    private int color;

    public Functionality(String text, int color, String id){
        this.text = text;
        this.color = color;
        this.ID = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

}
