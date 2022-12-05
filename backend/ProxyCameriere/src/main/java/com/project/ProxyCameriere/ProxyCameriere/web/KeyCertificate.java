package com.project.ProxyCameriere.ProxyCameriere.web;

import lombok.Data;

@Data
public class KeyCertificate {
    String pin;

    public String getPin(){
        return this.pin;
    }
}
