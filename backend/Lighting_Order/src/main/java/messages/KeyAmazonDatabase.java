package messages;

import lombok.Data;

@Data
public class KeyAmazonDatabase {
    String url;
    String username;
    String password;
    String certificate_path = "";

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCertificate_path() {
        return certificate_path;
    }

    public void setCertificatePath(String path){
        this.certificate_path = path;
    }
    public String getUrl(){
        return url+certificate_path;
    }

}
