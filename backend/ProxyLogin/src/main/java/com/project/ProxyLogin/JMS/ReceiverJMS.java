package com.project.ProxyLogin.JMS;

import com.google.gson.Gson;

import com.project.ProxyLogin.web.Post;
import com.project.ProxyLogin.web.Webhook;
import com.project.ProxyLogin.web.baseMessage;
import com.project.ProxyLogin.web.loginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.jms.annotation.JmsListener;

import org.springframework.stereotype.Service;


import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.validation.constraints.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;


/*
 * Listen queue "CodaLoginBroker" and if detect an event
 * notify every client registered to the webhook
 */

@Service
public class ReceiverJMS implements MessageListener {

    private final Post poster = new Post();

    private final Logger log = LoggerFactory.getLogger(ReceiverJMS.class);
    @Value("${server.port}")
    public String address_port;

    @JmsListener(destination = "CodaLoginBroker")
    @Override
    public void onMessage(@NotNull Message message) {
        /*
         * Retrieve body of the message sent by ActiveMQ.
         */
        String helper;
        loginRequest msg_received = new loginRequest() ;
        String msg_to_send = "";
        Gson gson=new Gson();
        try {
            helper=  message.getBody(String.class);
            msg_received=gson.fromJson(helper,loginRequest.class);
            log.info("Returned is " +helper);
            msg_to_send = (String) message.getBody(Object.class);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }
        poster.createPost("https://"+msg_received.url+"/login", msg_to_send);
        }
    }
