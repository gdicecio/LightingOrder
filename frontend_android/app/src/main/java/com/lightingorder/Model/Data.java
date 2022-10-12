package com.lightingorder.Model;

import com.lightingorder.Model.MenuAndWareHouseArea.Goods;
import com.lightingorder.Model.MenuAndWareHouseArea.MenuItem;
import com.lightingorder.Model.RestaurantArea.Order;
import com.lightingorder.Model.RestaurantArea.OrderedItem;
import com.lightingorder.Model.RestaurantArea.Table;
import com.lightingorder.StdTerms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Singleton
public class Data {
    private static Data istanza = null;
    private ArrayList<Table> tablesList = new ArrayList<>();
    private ArrayList<Order> kitchen_ordersList = new ArrayList<>();
    private ArrayList<Order> bar_ordersList = new ArrayList<>();
    private ArrayList<Order> pizza_ordersList = new ArrayList<>();
    private ArrayList<MenuItem> menuList = new ArrayList<>();
    private ArrayList<Goods> goodsList = new ArrayList<>();


    private Data(){}
    public static synchronized Data getData(){
        if(istanza == null){
            istanza = new Data();
        }
        return istanza;
    }

    public ArrayList<Table> getTablesList() {return istanza.tablesList;}



    public ArrayList<Order> getOrdersList(String role) {
        if(role.equals(StdTerms.roles.Forno.name()))
            return istanza.pizza_ordersList;
        else if(role.equals(StdTerms.roles.Cucina.name()))
            return istanza.kitchen_ordersList;
        else if(role.equals(StdTerms.roles.Bar.name()))
            return istanza.bar_ordersList;
        else
            return new ArrayList<Order>();
    }


    public ArrayList<MenuItem> getMenuList(){ return istanza.menuList; }

    public String[] getMenuItemsName(){
        String[] to_ret = new String[istanza.menuList.size()+1];
        to_ret[0] = "No selection";
        int i = 1;
        for(MenuItem m : istanza.menuList){
            to_ret[i] = m.getName();
            i += 1;
        }

        return to_ret;
    }

    public void setTablesList(ArrayList<Table> tablesList) {
        istanza.tablesList = tablesList;
        Collections.sort(istanza.tablesList,(o1, o2) -> o1.getTableID().compareTo(o2.getTableID()));
    }

    public void setOrdersList(ArrayList<Order> ordersList, String role) {
        if(role.equals(StdTerms.roles.Forno.name()))
            istanza.pizza_ordersList = ordersList;
        else if(role.equals(StdTerms.roles.Cucina.name()))
            istanza.kitchen_ordersList = ordersList;
        else if(role.equals(StdTerms.roles.Bar.name()))
            istanza.bar_ordersList = ordersList;
    }

    public void setMenuList(ArrayList<MenuItem> menuList){ istanza.menuList = menuList; }

    public Table getTable(String tableID, int roomNumber){
        boolean find = false;
        int i = 0;
        while(!find){
            if(istanza.tablesList.get(i).getTableID().equals(tableID) &&
                    istanza.tablesList.get(i).getTableRoomNumber() == roomNumber) {
                find = true;
            }
            else i = i + 1;
        }
        return istanza.tablesList.get(i);
    }

    public void updateTableState(String tableID, int roomNumber, String new_state){
        getTable(tableID,roomNumber).setTableState(new_state);
    }

    public ArrayList<Order> getTableOrders(String tableID, int roomNumber){
        return getTable(tableID,roomNumber).getOrdersList();
    }


    public List<OrderedItem> getOrderedItemsForOrder(String tableID, int roomNumber, int orderID){
        ArrayList<Order> orders = getTableOrders(tableID,roomNumber);
        boolean find = false;
        int i = 0;
        while(!find){
            if(orders.get(i).getOrderID() == orderID){
                find = true;
            }
            else i = i + 1;
        }
        return orders.get(i).getOrderedItems();
    }


    public void updateItemState(int orderID, int lineNumber, String new_state, String area) {

        boolean found = false;
        int i = 0;

        if(area.equals(StdTerms.roles.Cucina.name())) {

            while (!found) {
                if (istanza.kitchen_ordersList.get(i).getOrderID() == orderID) {
                    found = true;
                    istanza.kitchen_ordersList.get(i).getOrderedItem(lineNumber).setActualState(new_state);
                }else i = i +1;
            }
        }


        else if(area.equals(StdTerms.roles.Forno.name())){

            while(!found) {
                if(istanza.pizza_ordersList.get(i).getOrderID() == orderID){
                    found = true;
                    istanza.pizza_ordersList.get(i).getOrderedItem(lineNumber).setActualState(new_state);
                }else i = i +1;
            }
        }

        else if(area.equals(StdTerms.roles.Bar.name())) {

            while (!found) {
                if (istanza.bar_ordersList.get(i).getOrderID() == orderID) {
                    found = true;
                    istanza.bar_ordersList.get(i).getOrderedItem(lineNumber).setActualState(new_state);
                } else i = i + 1;
            }
        }

    }


    public void removeOrderFromTableList(int orderID){
        boolean orderFound = false;
        int i = 0;
        int k = 0;
        ArrayList<Order> orders;

        while(!orderFound){
            orders = getTableOrders(istanza.tablesList.get(i).getTableID(),istanza.tablesList.get(i).getTableRoomNumber());
            while(!orderFound && k<orders.size()){
                if(orders.get(k).getOrderID() == orderID){
                    orderFound = true;
                }else k = k+1;
            }
            i = i+1;
        }
        istanza.tablesList.get(i-1).getOrdersList().remove(k);
    }


    public void removeOrderedItem(int orderID, int lineNumber){
        boolean orderFound = false;
        boolean itemFound = false;
        int i = 0;
        int k = 0;
        int j = 0;
        ArrayList<Order> orders;
        OrderedItem item = new OrderedItem();
        while(!orderFound){
            orders = getTableOrders(istanza.tablesList.get(i).getTableID(),istanza.tablesList.get(i).getTableRoomNumber());
            if(orders.size() != 0) {
                while (!orderFound && k < orders.size()) {
                    if (orders.get(k).getOrderID() == orderID) {
                        orderFound = true;
                        item = orders.get(k).getOrderedItem(lineNumber);
                    } else k = k + 1;
                }
            }
            i = i+1;
        }
        istanza.tablesList.get(i-1).getOrdersList().get(k).getOrderedItems().remove(item);
    }


    public void updateOrderedItem(int orderID, int lineNumber, String new_state){
        boolean orderFound = false;
        boolean itemFound = false;
        int i = 0;
        int k = 0;
        int j = 0;
        ArrayList<Order> orders;
        List<OrderedItem> items;
        while(!orderFound){
            orders = getTableOrders(istanza.tablesList.get(i).getTableID(),istanza.tablesList.get(i).getTableRoomNumber());
            while(!orderFound && k<orders.size()){
                if(orders.get(k).getOrderID() == orderID){
                    orderFound = true;
                    items = orders.get(k).getOrderedItems();

                    while(!itemFound){
                        if(items.get(j).getLineNumber() == lineNumber)
                            itemFound = true;
                        else j= j+1;
                    }
                }else k = k +1;
            }
            i = i+1;
        }
        istanza.tablesList.get(i-1).getOrdersList().get(k).getOrderedItems().get(j).setActualState(new_state);
    }

}
