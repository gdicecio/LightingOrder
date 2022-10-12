package com.lightingorder.Model.RestaurantArea;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class OrderedItem {

    @Expose(serialize=true,deserialize=true)
    private String item;

    @Expose(serialize = true,deserialize = true)
    private String itemArea;

    @Expose(serialize=true,deserialize=true)
    private List<String> additive=new ArrayList<String>();

    @Expose(serialize=true,deserialize=true)
    private List<String> sub=new ArrayList<String>();

    @Expose(serialize=true,deserialize=true)
    private String actualState;

    @Expose(serialize=true,deserialize=true)
    private int lineNumber;

    @Expose(serialize=true,deserialize=true)
    private int priority;

    public OrderedItem() {}

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemArea() {
        return itemArea;
    }

    public void setItemArea(String itemArea) {
        this.itemArea = itemArea;
    }

    public List<String> getAdditive() {
        return additive;
    }

    public void setAdditive(List<String> additive) {
        this.additive = additive;
    }

    public List<String> getSub() {
        return sub;
    }

    public void setSub(List<String> sub) {
        this.sub = sub;
    }

    public String getActualState() {
        return actualState;
    }

    public void setActualState(String actualState) {
        this.actualState = actualState;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return this.item ;
    }
}
