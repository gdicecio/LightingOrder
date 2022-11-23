package com.lightingorder.Controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.connectivity.ClientSSL;
import com.connectivity.HttpRequest;
import com.connectivity.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lightingorder.Model.Data;
import com.lightingorder.Model.MenuAndWareHouseArea.MenuItem;
import com.lightingorder.Model.RestaurantArea.Order;
import com.lightingorder.Model.RestaurantArea.Table;
import com.lightingorder.Model.messages.baseMessage;
import com.lightingorder.Model.messages.cancelOrderRequest;
import com.lightingorder.Model.messages.itemOpRequest;
import com.lightingorder.Model.messages.loginRequest;
import com.lightingorder.Model.messages.menuRequest;
import com.lightingorder.Model.messages.orderNotification;
import com.lightingorder.Model.messages.orderRequest;
import com.lightingorder.Model.messages.orderToTableGenerationRequest;
import com.lightingorder.Model.messages.tableOperation;
import com.lightingorder.Model.messages.tableRequest;
import com.lightingorder.R;
import com.lightingorder.StdTerms;
import com.lightingorder.View.MakerActivity;
import com.lightingorder.View.TableActivity;
import com.connectivity.HttpResponseCallback;
import com.connectivity.ServerSSL;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ConnectivityController {   //Singleton
    private static ConnectivityController istanza = null;
    private final String path = "/data/data/com.lightingorder/files/certificate.p12";

    private final ServerSSL myserver;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView testo_notifca;
    private Button vai_alla_pagina;
    private Button chiudi;

    private ConnectivityController(){
         myserver = new ServerSSL(StdTerms.server_port, path);
    }

    public static synchronized ConnectivityController getConnectivity(){
        if(istanza == null){
            istanza = new ConnectivityController();
        }
        return istanza;
    }

    public void configPostMapping() {

        Gson gsonReq = new Gson();
        UserSessionController user_contr = new UserSessionController();

        istanza.myserver.postMapping("/login", new HttpResponseCallback() {
            @Override
            public HttpResponse onRequest(HttpRequest request) {
                String req = request.getBody();
                loginRequest msg_rcvd = gsonReq.fromJson(req, loginRequest.class);
                user_contr.addRoleAndProxy(msg_rcvd.result,msg_rcvd.proxySource);
                AppStateController.getApplication().getCurrent_activity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast t = Toast.makeText(AppStateController.getApplication().getCurrent_activity(), "Ruolo " + msg_rcvd.result + " riconosciuto!", Toast.LENGTH_LONG);
                        t.show();
                    }
                });
                HttpResponse response = new HttpResponse();
                response.setCode(200);
                response.setBody("Message Received");
                return response;
            }
        });

        istanza.myserver.postMapping("/request", new HttpResponseCallback() {
            @Override
            public HttpResponse onRequest(HttpRequest request) {
                //Retrieve the message body as String (in JSON format)
                String req = request.getBody();
                //Create the object using the JSON string
                baseMessage msg_rcvd = gsonReq.fromJson(req, baseMessage.class);
                String message_type  = msg_rcvd.messageName;
                Gson gson_no_expose = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                String txt_to_show;
                String resp = ""+msg_rcvd.response;

                if(!(msg_rcvd.result.contains("Failed") || msg_rcvd.result.contains("NotFound") ||
                        resp.contains("NotCreated") || resp.contains("NotCanceled") ||
                        msg_rcvd.result.contains("Aborted") || resp.contains("false"))) {

                    switch (message_type) {

                        case "cancelOrderedItemRequest":
                            itemOpRequest cancel_item_msg = gsonReq.fromJson(req, itemOpRequest.class);
                            Data.getData().removeOrderedItem(cancel_item_msg.orderID,cancel_item_msg.itemLineNumber);

                            Activity current0 = AppStateController.getApplication().getCurrent_activity();
                            if(current0.getLocalClassName().equals("View.OrderListActivity")){
                                current0.finish();
                                current0.startActivity(current0.getIntent());
                            }
                            txt_to_show = "Product removed from the order";
                            Log.d("SERVER","CancelOrderedItemRequest: Product removed from the order");
                            break;


                        case "cancelOrderRequest":
                            cancelOrderRequest cancel_order_msg = gsonReq.fromJson(req, cancelOrderRequest.class);
                            Data.getData().removeOrderFromTableList(cancel_order_msg.orderID);

                            Activity current1 = AppStateController.getApplication().getCurrent_activity();
                            if(current1.getLocalClassName().equals("View.OrderListActivity")){
                                current1.finish();
                                current1.startActivity(current1.getIntent());
                            }

                            txt_to_show = "Order removed";
                            Log.d("SERVER","CancelOrderRequest: Order removed");
                            break;


                        case "orderToTableGenerationRequest":
                            AppStateController.getApplication().getCurrent_activity().finish();
                            sendTableRequest(
                                    user_contr,
                                    user_contr.getHashRuoli_Proxy().get(StdTerms.roles.Cameriere.name()));

                            txt_to_show = "Order added";
                            Log.d("SERVER","OrderToTableGenerationRequest: Order added");
                            break;


                        case "itemCompleteRequest":
                            itemOpRequest item_compl_msg = gsonReq.fromJson(req, itemOpRequest.class);
                            Data.getData().updateItemState(item_compl_msg.orderID,
                                    item_compl_msg.itemLineNumber, StdTerms.state_item.Completed.name(),
                                    item_compl_msg.proxySource);

                            Activity current2 = AppStateController.getApplication().getCurrent_activity();
                            if(current2.getLocalClassName().equals("View.MakerActivity")){
                                //current2.recreate();
                                current2.finish();
                                current2.startActivity(current2.getIntent());
                            }

                            txt_to_show = "Action registered";
                            Log.d("SERVER","ItemCompleteRequest: Action registred");
                            break;


                        case "itemWorkingRequest":
                            itemOpRequest item_work_msg = gsonReq.fromJson(req, itemOpRequest.class);
                            Data.getData().updateItemState(item_work_msg.orderID,
                                    item_work_msg.itemLineNumber, StdTerms.state_item.Working.name(),
                                    item_work_msg.proxySource);

                            Activity current3 = AppStateController.getApplication().getCurrent_activity();
                            if(current3.getLocalClassName().equals("View.MakerActivity")){
                                current3.finish();
                                current3.startActivity(current3.getIntent());
                                //current3.recreate();
                            }
                            txt_to_show = "Request accepted";
                            Log.d("SERVER","ItemWorkingRequest: Request accepted");
                            break;


                        case "freeTableRequest":
                            tableOperation free_msg = gsonReq.fromJson(req, tableOperation.class);
                            Data.getData().updateTableState(free_msg.tableID,
                                    free_msg.tableRoomNumber,
                                    StdTerms.statesList.free.name());
                            Activity current4 = AppStateController.getApplication().getCurrent_activity();
                            if(current4.getLocalClassName().equals("View.TableActivity")){
                                current4.finish();
                                current4.startActivity(current4.getIntent());
                            }
                            txt_to_show = "Table state updated";
                            Log.d("SERVER","FreeTableRequest: Table state updated");
                            break;


                        case "userWaitingForOrderRequest":
                            tableOperation usr_wait_msg = gsonReq.fromJson(req, tableOperation.class);
                            Data.getData().updateTableState(usr_wait_msg.tableID,
                                    usr_wait_msg.tableRoomNumber,
                                    StdTerms.statesList.waitingForOrders.name());

                            Activity current5 = AppStateController.getApplication().getCurrent_activity();
                            if(current5.getLocalClassName().equals("View.TableActivity")){
                                current5.finish();
                                current5.startActivity(current5.getIntent());
                            }
                            txt_to_show = "Table state updated";
                            Log.d("SERVER","UserWaitingForOrderRequest: Table state updated");
                            break;


                        case "tableRequest":

                            ArrayList<Table> tables = new ArrayList<Table>();
                            try {
                                JSONArray j = new JSONArray(msg_rcvd.response);
                                for(int i=0; i<j.length(); i++){
                                    Table single_table = (Table) gson_no_expose.fromJson(j.get(i).toString(), Table.class);
                                    tables.add(single_table);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Data.getData().setTablesList(tables);

                            Activity current6 = AppStateController.getApplication().getCurrent_activity();
                            Intent i = new Intent(current6, TableActivity.class);
                            if(current6.getLocalClassName().equals("View.FunctionalityActivity")) {
                                current6.startActivity(i);

                            }
                            else if(current6.getLocalClassName().equals("View.OrderListActivity")){
                                current6.finish();
                            }


                            txt_to_show = "Table list updated";
                            Log.d("SERVER","TableRequest: Table list updated");
                            break;

                        case "menuRequest":
                            ArrayList<MenuItem> menu = new ArrayList<MenuItem>();
                            try {
                                JSONArray j = new JSONArray(msg_rcvd.response);
                                for(int k=0; k<j.length(); k++){
                                    MenuItem single_item = new MenuItem();
                                    single_item = (MenuItem) gson_no_expose.fromJson(j.get(k).toString(), MenuItem.class);
                                    menu.add(single_item);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Data.getData().setMenuList(menu);
                            txt_to_show = "Menu list updated";
                            Log.d("SERVER","MenuRequest: Menu list updated");
                            break;


                        case "orderRequest":
                            orderRequest order_msg = gsonReq.fromJson(req, orderRequest.class);
                            ArrayList<Order> orders = new ArrayList<Order>();
                            try {
                                JSONArray j = new JSONArray(msg_rcvd.response);
                                for(int k=0; k<j.length(); k++){
                                    Order single_order = (Order) gson_no_expose.fromJson(j.get(k).toString(), Order.class);
                                    orders.add(single_order);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Data.getData().setOrdersList(orders, order_msg.area);

                            Activity current7 = AppStateController.getApplication().getCurrent_activity();
                            Intent intent;
                            intent = new Intent(current7, MakerActivity.class);
                            current7.startActivity(intent);

                            txt_to_show = "Order list updated";
                            break;

                        default:
                            txt_to_show = "Message not recognized";
                            Log.e("SERVER","Message not recognized");
                    }
                }
                else{
                    txt_to_show = "Main System: operation not allowed";
                    Log.e("SERVER", "Main System : operation not allowed");
                }
                String finalTxt_to_show = txt_to_show;
                AppStateController.getApplication().getCurrent_activity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast t = Toast.makeText(AppStateController.getApplication().getCurrent_activity(), finalTxt_to_show, Toast.LENGTH_SHORT);
                        t.show();
                    }
                });

                //Sending the response to the proxy
                HttpResponse response = new HttpResponse();
                response.setCode(200);
                response.setBody("Message received");
                return response;
            }
        });

        istanza.myserver.postMapping("/notification", new HttpResponseCallback() {
            @Override
            public HttpResponse onRequest(HttpRequest request) {
                HttpResponse response = new HttpResponse();
                if(user_contr.getLoginResult()) {
                    //Retrieve the message body as String (in JSON format)
                    String req = request.getBody();
                    //Create the object using the JSON string
                    baseMessage msg_rcvd = gsonReq.fromJson(req, baseMessage.class);
                    String message_type = msg_rcvd.messageName;
                    String popup_txt = "" ;
                    String notificationType= "";
                    Activity current_activity;

                    switch (message_type) {

                        case "userWaitingNotification":
                            tableOperation tab_upd = gsonReq.fromJson(req, tableOperation.class);

                            current_activity = AppStateController.getApplication().getCurrent_activity();

                            if(current_activity.getLocalClassName().equals("View.FunctionalityActivity")
                                    || !(user_contr.getCurrentRole().equals(StdTerms.roles.Accoglienza.name())) ) {

                                popup_txt = "Il tavolo " + tab_upd.tableID + " è in attesa di ordinazioni";
                                notificationType = StdTerms.messages.userWaitingNotification.name();
                                createWaiterNotificationPopup(popup_txt, notificationType, tab_upd.tableID, tab_upd.tableRoomNumber);

                            }
                            Log.d("SERVER", "SERVER: Notification - User waiting Notification received ");
                            break;


                        case "orderNotification":

                            notificationType = StdTerms.messages.orderNotification.name();
                            orderNotification ord_not = gsonReq.fromJson(req, orderNotification.class);
                            current_activity = AppStateController.getApplication().getCurrent_activity();


                            if(current_activity.getLocalClassName().equals("View.MakerActivity") &&
                                    user_contr.getCurrentRole().equals(ord_not.area)){

                                Intent i = new Intent(current_activity, MakerActivity.class);
                                current_activity.finish();
                                current_activity.startActivity(i);

                                AppStateController.getApplication().getCurrent_activity().runOnUiThread(new Runnable() {
                                    public void run() {

                                        Toast t = Toast.makeText(AppStateController.getApplication().getCurrent_activity(),
                                                "Un nuovo ordine è stato aggiunto", Toast.LENGTH_SHORT);
                                        t.show();
                                    }
                                });

                            }
                            else{

                                popup_txt = "Un ordine è stato aggiunto";
                                createMakerNotificationPopup(popup_txt, notificationType, ord_not.area);

                            }

                            Log.d("SERVER", "SERVER: Notification - Order Notification received ");
                            break;


                        default:
                            Log.d("SERVER", "SERVER: Notification not recognized");
                    }

                    //Sending the response to the proxy

                    response.setCode(200);
                    response.setBody("Message received");
                }
                else {
                    //Sending the response to the proxy
                    response.setCode(500);
                    response.setBody("User not logged yet");
                }
                return response;
            }
        });

    }


    public void startServer(){
        istanza.myserver.listen();
    }


    //"192.168.43.16:8085/loginSend";
    static private HttpResponse sendPost(String body, String urlDestination){
        ClientSSL client = new ClientSSL("my_certificate.crt", AppStateController.getApplication().getCurrent_activity().getFilesDir().toString());
        client.setTimeout(5000);
        String address_and_port = urlDestination.split("/")[0];
        String path = urlDestination.split("/")[1];
        String address = address_and_port.split(":")[0];
        int port = Integer.valueOf(address_and_port.split(":")[1]);
        Log.d("Client",  "[Address]: "+ address);
        Log.d("Client",  "[Port]: "+ port);
        Log.d("Client",  "[Address]: "+ path);
        HttpRequest req = new HttpRequest(address, port, "/"+path);
        req.setMethod("POST");
        req.setHeader("Content-Length", ""+body.length());
        req.setHeader("Content-Type", "application/json");
        req.setHeader("User-Agent", "Android Frontend");
        req.setHeader("Accept", "*/*");
        req.setHeader("Connection", "keep-alive");
        req.setHeader("Host", address_and_port);
        if(StdTerms.access_token != " ")
            req.setHeader("Authorization", StdTerms.type_token.toLowerCase() + " " + StdTerms.access_token);
        req.setBody(body);

        HttpResponse res = client.send(req);
        if(res!=null) {
            AppStateController.getApplication().setProxyConnectionState(res.getCode());
            Log.d("Login Response", res.getBody());
        }
        else {
            res = new HttpResponse();
            AppStateController.getApplication().setProxyConnectionState(-1);
        }
        return res;
    }
/*
    static private void sendPost(String body, String urlDestination){

        //POST Request whit String Body
        AsyncHttpRequest req = new AsyncHttpRequest(Uri.parse("http://"+urlDestination), "POST");
        StringBody post_body = new StringBody(body);
        req.setBody(post_body);

        //Send HTTP POST Request
        try {
            AsyncHttpClient.getDefaultInstance().executeString(req, new AsyncHttpClient.StringCallback() {
                @Override
                public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
                    if(e == null) {
                        if(source.code() >= 200 && source.code() < 300)
                            Log.d("PROXY","Proxy received your message");
                        else
                           Log.d("PROXY","There were problems contacting the Proxy");

                        AppStateController.getApplication().setProxyConnectionState(source.code());
                    } else {
                        Log.d("PROXY","Proxy not reachable");
                        AppStateController.getApplication().setProxyConnectionState(600);// In this case 600 means that an exception has been thrown
                    }
                    //t.show();

                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
*/
    public static HttpResponse sendLoginRequest(UserSessionController us_contr, String password){
        //Perchè non mettere la password nella struttura dati dell'utente?
        //La risposta è che la password deve solo inviata per l'accesso, non serve memorizzarla per tutta la sessione.

        //Body of my request ---> request type = loginRequest
        loginRequest req_body = new loginRequest(
                us_contr.getUserID(), //user
                password,
                "", //proxySource
                StdTerms.messages.loginRequest.name(),  //messageName
                "",     //result
                "",     //response
                us_contr.getUserIpAddress());    //url

        //Convert into string and send Post request
        Gson gson = new Gson();
        String msg_body = gson.toJson(req_body);
        Log.d("Login", msg_body);
        return ConnectivityController.sendPost(msg_body, StdTerms.proxyLoginAddress);

    }

    public static HttpResponse sendMenuRequest(UserSessionController us_contr, String proxy_addr){
        menuRequest req_body = new menuRequest(
                us_contr.getUserID(),
                "",
                StdTerms.messages.menuRequest.name(),
                "",
                "",
                false,
                "" );
        Gson gson = new Gson();
        String msg_body = gson.toJson(req_body);
        return ConnectivityController.sendPost(msg_body, proxy_addr);
    }

    public static HttpResponse sendTableRequest(UserSessionController us_contr, String proxy_addr){

        String proxy_name = "";
        if(us_contr.getCurrentRole().equals(StdTerms.roles.Cameriere.name()))
            proxy_name = "waitersProxy";
        else if(us_contr.getCurrentRole().equals(StdTerms.roles.Accoglienza.name()))
            proxy_name = "acceptanceProxy";

        //Body of my request ---> request type = tableRequest
        tableRequest req_body = new tableRequest(
                us_contr.getUserID(), //user
                proxy_name,
                StdTerms.messages.tableRequest.name(),  //messageName
                "",
                "",
                true,
                1,
                false,
                "");

        //Convert into string and send Post request
        Gson gson = new Gson();
        String msg_body = gson.toJson(req_body);
        return ConnectivityController.sendPost(msg_body, proxy_addr);
    }

    public static void sendTableOperationRequest(UserSessionController us_contr, String proxy_addr,
                                                 String tableID, int tableRoom, String new_state){

        String message_name = "";
        if (new_state.equals(StdTerms.statesList.free.name())){
             message_name = StdTerms.messages.freeTableRequest.name();
        }
        else if(new_state.equals(StdTerms.statesList.Occupied.name())){
            message_name = StdTerms.messages.userWaitingForOrderRequest.name();
        }

        Gson gson = new Gson();
        tableOperation req_body = new tableOperation(
                us_contr.getUserID(),
                proxy_addr,
                message_name,
                "",
                "",
                tableID,
                tableRoom);
        String msg_body = gson.toJson(req_body);
        ConnectivityController.sendPost(msg_body,proxy_addr);
    }

    public static HttpResponse sendOrderRequest(UserSessionController us_contr, String proxy_addr, String area){


        Gson gson = new Gson();
        orderRequest req_body = new orderRequest(
                us_contr.getUserID(),
                area,
                StdTerms.messages.orderRequest.name(),
                "",
                "",
                true,
                area);
        String msg_body = gson.toJson(req_body);
        return ConnectivityController.sendPost(msg_body,proxy_addr);
    }

    public static void sendAddOrderToTableRequest(UserSessionController us_contr,
                                                  String proxy_addr, String tableID, int roomNumber,
                                                  List<String> items_name,List<List<String>> additive, List<List<String>> sub,
                                                  List<Integer> priority){

        orderToTableGenerationRequest.orderParameters param = new orderToTableGenerationRequest.orderParameters(
                        items_name,
                        additive,
                        sub,
                        priority);

        Gson gson = new Gson();
        orderToTableGenerationRequest req_body = new orderToTableGenerationRequest(
                us_contr.getUserID(),
                proxy_addr,
                StdTerms.messages.orderToTableGenerationRequest.name(),
                "",
                "",
                tableID,
                roomNumber,
                param);
        String msg_body = gson.toJson(req_body);
        ConnectivityController.sendPost(msg_body,proxy_addr);

    }

    public static void sendCancelOrderRequest(UserSessionController us_contr, String proxy_addr, int orderID){

        Gson gson = new Gson();
        cancelOrderRequest req_body = new cancelOrderRequest(
                us_contr.getUserID(),
                proxy_addr,
                StdTerms.messages.cancelOrderRequest.name(),
                "",
                "",
                orderID);
        String msg_body = gson.toJson(req_body);
        ConnectivityController.sendPost(msg_body,proxy_addr);
    }

    public static void sendCancelOrderedItemRequest(UserSessionController us_contr, String proxy_addr,
                                                    int orderID, int lineNumber){

        Gson gson = new Gson();
        itemOpRequest req_body = new itemOpRequest(
                us_contr.getUserID(),
                proxy_addr,
                StdTerms.messages.cancelOrderedItemRequest.name(),
                "",
                "",
                orderID,
                lineNumber);
        String msg_body = gson.toJson(req_body);
        ConnectivityController.sendPost(msg_body,proxy_addr);

    }

    public static void sendItemCompletedRequest(UserSessionController us_contr, String proxy_addr,
                                                int orderID, int lineNumber){

        Gson gson = new Gson();
        itemOpRequest req_body = new itemOpRequest(
                us_contr.getUserID(),
                us_contr.getCurrentRole(),
                StdTerms.messages.itemCompleteRequest.name(),
                null,
                null,
                orderID,
                lineNumber);
        String msg_body = gson.toJson(req_body);
        ConnectivityController.sendPost(msg_body,proxy_addr);

    }

    public static void sendItemWorkingRequest(UserSessionController us_contr, String proxy_addr,
                                                int orderID, int lineNumber){

        Gson gson = new Gson();
        itemOpRequest req_body = new itemOpRequest(
                us_contr.getUserID(),
                us_contr.getCurrentRole(),
                StdTerms.messages.itemWorkingRequest.name(),
                null,
                null,
                orderID,
                lineNumber);
        String msg_body = gson.toJson(req_body);
        ConnectivityController.sendPost(msg_body,proxy_addr);
    }

    private void createNotificationPopup(String notificationTxt, String notificationType, String tableID, int roomNumber, String area){

        Activity current = AppStateController.getApplication().getCurrent_activity();
        dialogBuilder = new AlertDialog.Builder(current);
        final View notificationPopup = current.getLayoutInflater().inflate(R.layout.popup_notification,null);

        vai_alla_pagina = (Button) notificationPopup.findViewById(R.id.notification_popup_goToActivity_button);
        chiudi = (Button) notificationPopup.findViewById(R.id.popup_notification_chiudi_button);
        testo_notifca = (TextView) notificationPopup.findViewById(R.id.popup_notification_textView);

        vai_alla_pagina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notificationType.equals(StdTerms.messages.userWaitingNotification.name()))
                    goToTablePage(tableID, roomNumber);
                else
                    goToMakerPage(area);
            }
        });

        chiudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                close(v);
            }
        });

        testo_notifca.setText(notificationTxt);
        dialogBuilder.setView(notificationPopup);

        current.runOnUiThread(new Runnable() {
            public void run() {

                dialog = dialogBuilder.create();
                dialog.show();

            }
        });

    }

    public void createWaiterNotificationPopup(String notificationTxt, String notificationType, String tableID, int roomNumber){
        createNotificationPopup(notificationTxt, notificationType, tableID, roomNumber, null);
    }

    public void createMakerNotificationPopup(String notificationTxt, String notificationType, String area ){
        createNotificationPopup(notificationTxt, notificationType, null , 0, area);
    }

    private void goToTablePage(String tableID, int tableRoomNumber){

        UserSessionController us_contr = new UserSessionController();
        Activity current = AppStateController.getApplication().getCurrent_activity();

        if(!current.getLocalClassName().equals("View.TableActivity")) {

            us_contr.setCurrentRole(StdTerms.roles.Cameriere.name());
            Log.d("USER","USER : Ruolo corrente: "+ us_contr.getCurrentRole() );
            sendTableRequest(us_contr, us_contr.getCurrentProxy());
            dialog.dismiss();
            if(!current.getLocalClassName().equals("View.FunctionalityActivity"))
                current.finish();

        }
        else{

            Data.getData().updateTableState(tableID,
                                            tableRoomNumber,
                                            StdTerms.statesList.waitingForOrders.name());

                dialog.dismiss();
                current.finish();
                current.startActivity(current.getIntent());
            }

    }

    private void goToMakerPage(String area){
        UserSessionController us_contr = new UserSessionController();
        Activity current = AppStateController.getApplication().getCurrent_activity();
        if(current.getLocalClassName().equals("View.MakerActivity"))
            current.finish();

        us_contr.setCurrentRole(area);
        Log.d("USER","USER : Ruolo corrente: "+ us_contr.getCurrentRole() );
        sendOrderRequest(us_contr,us_contr.getCurrentProxy(),area);
        dialog.dismiss();
    }

    private void close(View view){
        Log.d("POPUP", "Tasto close cliccato");
        dialog.dismiss();
    }

}


