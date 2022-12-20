package com.project.ProxyCameriere.ProxyCameriere;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.*;

public class Log {

    Logger l;
    FileHandler fh;

    private static Log istanza = null;

    String log_path = "C:/Users/giuse/Universita/SecureSystemDesign2022/Progetto/LightingOrder/backend/log/";
    String file_path;
    public Log(String cls, String log_name) throws FileNotFoundException {

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        file_path = log_path + log_name + "_" + df.format(now) + ".log";
        createLog();
        l = Logger.getLogger(cls);
        try {
            fh = new FileHandler(file_path, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                Calendar cal = new GregorianCalendar();
                cal.setTimeInMillis(record.getMillis());
                return "[" +record.getLevel() + "] "
                        + logTime.format(cal.getTime())
                        + " || "
                        + cls +"."
                        + record.getMessage() + "\n";
            }
        });

        l.addHandler(fh);
    }

    public static synchronized Log getInstance(String cls, String name) throws FileNotFoundException {
        if(istanza == null){
            istanza = new Log(cls, name);
        }
        return istanza;

    }

    public void createLog(){
        File log_file = new File(file_path);
        try {
            log_file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void info(String method, String text){
        String log = method + "() : " + text;
        l.log(Level.INFO, log);
    }
    public void error(String method, String text){
        String log = method + "() : " + text;
        l.log(Level.SEVERE, log);
    }
    public void warning(String method, String text){
        String log = method + "() : " + text;
        l.log(Level.WARNING, log);
    }
}
