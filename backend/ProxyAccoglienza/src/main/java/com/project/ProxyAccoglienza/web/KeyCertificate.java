package com.project.ProxyAccoglienza.web;

import lombok.Data;

@Data
public class KeyCertificate {
    String pin;

    public String getPin(){
        return this.pin;
    }
}
