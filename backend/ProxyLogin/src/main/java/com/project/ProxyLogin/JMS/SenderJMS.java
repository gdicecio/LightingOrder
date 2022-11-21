package com.project.ProxyLogin.JMS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SenderJMS {

        @Autowired
        private JmsTemplate JmsTemp;

        @Value("CodaLogin")
        private String JmsQueue;

        public void sendMessage (String order) {

                //IMplementare codice per keycloak
            JmsTemp.convertAndSend(JmsQueue, order);

            //Ritorna Access Token e Refresh o null
        }
}

