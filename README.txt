======================================================= DATABASE ============================================================
- Scaricare PostgreSQL dal sito ufficiale
- Installare e impostare una password per l'utente amministratore
	Oltre all'installazione del database è utile installare anche PgAdmin (basta marcare una spunta durante l'installazione)
	PgAdmin è un'interfaccia grafica per visualizzare, modificare, creare ecc tutte le informazioni sul DB.
- Creare un database tramite interfaccia grafica (pgAdmin). Il nome è indifferente, io l'ho chiamato "Ristorante".
- Il backup del database è il file "lightingOrder.sql"

IMPORT DATABASE
- Per caricare il backup, andare nella cartella di installazione di PostgreSQL fino alla cartella "bin"
- Dalla cartella bin eseguire il terminale (PowerShell per Windows) e digitare il seguente comando:
	.\psql.exe -U postgres -f "C:\Users\giuse\Universita\SecureSystemDesign2022\Progetto\db.sql" -d Ristorante
	
	-U Indica l'utente con cui fare il backup
	-d Indica il nome del database. Prima l'ho chiamato "Ristorante". Il database deve già esistere!
	-f Indica il file in cui è racchiuso il backup. Basta inserire il path assoluto al file .sql
- Se tutto va a buon fine, l'output del terminale deve essere una lista di operazioni (e.g. CREATE TABLE, COPY, ...)
- Come ultimo controllo, si può accedere tramite PgAdmin e verificare che effettivamente ci siano tutte le tabelle.

EXPORT DATABASE
- Andare nella cartella di installazione di PostgreSQL fino alla cartella "bin"
- Dalla cartella bin eseguire il terminale (PowerShell per Windows) e digitare il seguente comando:
	.\pg_dump.exe -d Ristorante -f 'C:\Users\giuse\Desktop\backup.sql' -U postgres

========================================================== SERVER ==========================================================
- La directory di riferimento è "backend".
- Scaricare IntelliJ per utilizzare, eseguire e modificare il codice. 
	In realtà si può usare qualnque IDE (e.g. Eclipse). L'interfaccia grafica di IntelliJ è più semplice da usare.
- Aprire la repository direttamente con IDE. All'interno ci sono più progetti
- Aprire il progetto Lighting_Order ed eseguire tutti i test (tasto destro "Run" su ogni test in src/test)
	I test devono andare TUTTI a buon fine altrimenti l'esecuzione non funziona.
	I test si rivolgono solo ai dati sul database. Quindi assicurarsi che il DBMS sia in esecuzione (di base è sempre in esecuzione)

- Scaricare ActiveMQ Artemis dal sito ufficiale:
	https://activemq.apache.org/components/artemis/
- Non c'è bisogno di installarlo, basta estrarre il file .zip
- Aprire il terminale e andare nella cartella bin/ ed eseguire il seguente comando per creare un broker
	./artemis create PATH
	PATH è il percorso dove si vuole creare il broker
- Dal broker andare nella cartella bin/ ed eseguire il seguente comando per lanciare il broker
	./artemis run
- Si può accedere alla configurazione dal browser all'indirizzo:
	localhost:8161

========================================================== FRONTEND ==========================================================
- Per il momento l'unico frontend un applicazione Android.
- Scaricare e installare AndroidStudio dal sito ufficiale
	https://developer.android.com/studio 
- Non è necessario instllare anche un emulatore. Si può scaricare in un secondo momento o si può simulare un sistema android con BlueStack
	https://www.bluestacks.com/it/index.html
	Se si dispone di un dispositivo android è ancora meglio
- Da AndroidStudio si può aprire il progetto nella cartella "frontend_android"
- Si devono fare una singola modifica che dipende dalla rete locale in cui si vuole testare il sistema.
	All'interno del progetto modificare il file:
		app -> java -> com -> lightingorder -> StdTerms.java
	Modificare il parametro:
		public static final String proxyLoginAddress = "192.168.1.111:8085/loginSend";
	Con l'indirizzo ip del computer che esegue tutto il lato server.
- Dopo le modifiche si può creare l'apk e installarlo sul device (reale o simulato)

========================================================== ESECUZIONE ==========================================================
In ordine eseguire i seguenti passi (possono essere automazzati alla fine del progetto, lo abbiamo già fatto in SAD)
1. Eseguire Artemis
2. Eseguire tutto il lato server. Quindi:
	LightingOrder
	ProxyLogin
	ProxyAccoglienza
	ProxyCameriere
	ProxyRealizzatore
3. Eseguire l'applicazione Android inserendo le credenziali di un generico utente (riferimento: Tabella "Dipendente" nel DB).

TUTTI gli applicativi devono essere nella stessa rete locale!