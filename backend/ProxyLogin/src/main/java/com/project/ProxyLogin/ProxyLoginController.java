package com.project.ProxyLogin;

import com.project.ProxyLogin.JMS.SenderJMS;
import com.project.ProxyLogin.web.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
        return new ResponseEntity<>("[Login Request] - Received from Proxy", HttpStatus.OK);
    }

    @RolesAllowed("cameriere") //@RequestHeader String Authorization,
    @GetMapping(value = "/test")
    public ResponseEntity<String> getUser(@RequestHeader String Authorization, @RequestBody String msg){
        return ResponseEntity.ok("Hello Test\r\n" + msg);
    }





    @PostMapping(value = "/loginAuth")
    public ResponseEntity<String> authentication(){

        //TODO bisogna formattare i campi in relazione alla richiesta di ingresso
        String msg = "username=giuseppe&grant_type=password&password=giuseppe&client_id=proxy-app&client_secret=f7e30020-0cea-4a46-9412-103dc9443b28";
        String uri = "http://localhost:9000/auth/realms/SpringBootKeyCloak/protocol/openid-connect/token";
        String k = poster.createPost(uri, msg);
        return ResponseEntity.ok(k);
    }

}
