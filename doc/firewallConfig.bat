::La porta del Server centrale, Keycloak, Vault e Artemis non devono essere accessibili dall'esterno
netsh advfirewall firewall add rule name= "Block Port 5432" dir=in action=block protocol=TCP localport=5432
netsh advfirewall firewall add rule name= "Block Port 9000" dir=in action=block protocol=TCP localport=9000
netsh advfirewall firewall add rule name= "Block Port 8200" dir=in action=block protocol=TCP localport=8200
netsh advfirewall firewall add rule name= "Block Port 61616" dir=in action=block protocol=TCP localport=61616


:: Le porte dei proxy devono essere aperte perchè sono le uniche che cmunicano con l'app
netsh advfirewall firewall add rule name= "Open Port 8081" dir=in action=Allow protocol=TCP localport=8081
netsh advfirewall firewall add rule name= "Open Port 8082" dir=in action=Allow protocol=TCP localport=8082
netsh advfirewall firewall add rule name= "Open Port 8083" dir=in action=Allow protocol=TCP localport=8083
netsh advfirewall firewall add rule name= "Open Port 8085" dir=in action=Allow protocol=TCP localport=8085