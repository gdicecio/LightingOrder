package com.project.ProxyLogin;

import com.google.gson.Gson;
import com.project.ProxyLogin.JMS.SenderJMS;
import com.project.ProxyLogin.web.Post;
import com.project.ProxyLogin.web.loginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.security.RolesAllowed;

@Controller
public class ProxyLoginController {

    @Autowired
    SenderJMS sender;

    private final Post poster = new Post();

    private final Logger log = LoggerFactory.getLogger(com.project.ProxyLogin.ProxyLoginController.class);

    @PostMapping(value = "/loginSend")
    public ResponseEntity<String> login (@RequestBody String Login_msg) {
        /*  data:
            "id": pepped
            "url": 192.168.1.x:YYYY
        */

        log.info("Login Request received :"+Login_msg);

        sender.sendMessage(Login_msg);  //Invio su CodaLogin

        return new ResponseEntity<>("Request received", HttpStatus.OK);
    }


}
