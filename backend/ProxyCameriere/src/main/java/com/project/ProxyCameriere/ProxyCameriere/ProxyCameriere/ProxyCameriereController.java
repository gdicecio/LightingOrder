package com.project.ProxyCameriere.ProxyCameriere.ProxyCameriere;


import com.project.ProxyCameriere.ProxyCameriere.Log;
import com.project.ProxyCameriere.ProxyCameriere.ProxyCameriere.JMS.ReceiverJMS;
import com.project.ProxyCameriere.ProxyCameriere.ProxyCameriere.JMS.SenderJMS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;

/*
 * 1) POST mapping /register -> Iscrizione dell'applicazione al webhook del proxy
 * 2) POST mapping /send -> Creazione di un ordine
 */

@Controller
public class ProxyCameriereController {

    @Autowired
    private ReceiverJMS receiver;

    @Autowired
    private SenderJMS sender;

    private static final String name="waitersProxy";
    private final Logger log = LoggerFactory.getLogger(ProxyCameriereController.class);
    private Log l;

    /*
     * Example needed format:
     * http://localhost:8080/register/localhost:8081
     */

    /*
    @PostMapping (value = "/register/{url}")
    public ResponseEntity<String> registerUrl (@PathVariable String url) {
        log.info("URL registered: " + url);
        receiver.webhook.addUrl(url);
        return new ResponseEntity<>("URL registered", HttpStatus.OK);
    }
    */

    /*
     * Example format:
     * http://localhost:8081/sendorder
     *
     * Post request must contain a not null body.
     */


    @PostMapping (value = "/waitersSend")
    public ResponseEntity<String> sendJMS (@RequestBody String event) {
        try{
            l = Log.getInstance(this.getClass().getName(), "cameriere");
        } catch (IOException e){
            e.printStackTrace();
        }

        l.info("sendJMS", "Richiesta ricevuta: " + event);
        sender.sendMessage(event);
        log.info(event);
        return new ResponseEntity<>("Event sent", HttpStatus.OK);
    }
}
