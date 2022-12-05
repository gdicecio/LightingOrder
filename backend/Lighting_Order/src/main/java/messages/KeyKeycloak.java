package messages;

import lombok.Data;

import java.util.Map;

@Data
public class KeyKeycloak {
    public String getRealm() {
        return realm;
    }

    public String getHost() {
        return host;
    }

    public String getClientId() {
        return client_id;
    }

    public Map<String, Object> getCredentials() {
        return credentials;
    }

    String realm;
    String host;
    String client_id;
    Map<String,Object> credentials;
}
