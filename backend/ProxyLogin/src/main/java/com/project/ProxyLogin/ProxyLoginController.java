package com.project.ProxyLogin;

import com.google.gson.Gson;
import com.project.ProxyLogin.JMS.SenderJMS;
import com.project.ProxyLogin.web.Post;
import com.project.ProxyLogin.web.messages.loginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@Controller
public class ProxyLoginController {

    @Autowired
    SenderJMS sender;

    private final Post poster = new Post();

    private Logger log = LoggerFactory.getLogger(com.project.ProxyLogin.ProxyLoginController.class);
    private Log l;

    @PostMapping(value = "/loginSend")
    public ResponseEntity<String> login (@RequestBody String Login_msg) {
        /*  data:
            "id": pepped
            "url": 192.168.1.x:YYYY
        */



        try {
            l =Log.getInstance(this.getClass().getName(), "login");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        log.info("Login Request received :"+Login_msg);
        l.info("login", "Richiesta ricevuta: " + Login_msg);


        Gson parser = new Gson();
        loginRequest req = parser.fromJson(Login_msg, loginRequest.class);
        boolean val = req.validate();

        if(!val) {
            sender.sendMessage(Login_msg);  //Invio su CodaLogin
            l.info("login", "Richiesta inviata ad Artemis");
            return new ResponseEntity<>("Request received", HttpStatus.OK);
        }else {
            l.info("login", "Richiesta corrotta. Possibile SQL Injection");
            return new ResponseEntity<>("Request not computed", HttpStatus.BAD_REQUEST);
        }

    }

}
