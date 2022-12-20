package com.project.ProxyLogin.web.messages;

import lombok.Data;
@Data
public class KeyArtemis {

    String user;
    String password;
    String port;
    String host;

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }


}
