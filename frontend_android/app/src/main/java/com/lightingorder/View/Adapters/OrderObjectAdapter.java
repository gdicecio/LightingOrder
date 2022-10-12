package com.lightingorder.View.Adapters;

import com.lightingorder.Model.RestaurantArea.Order;
import com.lightingorder.Model.RestaurantArea.OrderedItem;

public class OrderObjectAdapter {

    private int line_number;
    private int priority;
    private String product_state;
    private String product;
    private String table_number;
    private int table_room_number;
    private String user_id;
    private int order_id;

    public OrderObjectAdapter(OrderedItem ordered_item, Order order){
        this.line_number = ordered_item.getLineNumber();
        this.priority = ordered_item.getPriority();
        this.product = ordered_item.getItem();
        this.product_state = ordered_item.getActualState();
        this.table_room_number = order.getTableRoomNumber();
        this.table_number = order.getTableID();
        this.user_id = order.getUserID();
        this.order_id = order.getOrderID();
    }

    public int getLine_number() {
        return line_number;
    }

    public int getPriority() {
        return priority;
    }

    public String getProduct() {
        return product;
    }

    public String getTable_number() {
        return table_number;
    }

    public String getProduct_state(){return product_state;}

    public int getTable_room_number() {
        return table_room_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getOrder_id() {
        return order_id;
    }



}
