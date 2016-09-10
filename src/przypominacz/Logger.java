/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package przypominacz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author BotNaEasyEnv
 */
public class Logger {
    
    private Class classLog;
    public static final String LOG_PATH = "D:\\logs\\";
    private String logName;
    private BufferedWriter writer;
    
    private static Logger instance;
    
    public static Logger getInstance(Class clazz){
        if(instance == null){
            instance = new Logger(clazz);
        }
        return instance;
    }
    
    public static Logger getInstance(){
        return instance;
    }
    
    private Logger(Class classLog){
        this.classLog = classLog;
        initPathName();
        initWriter();
    }
    
    private void initPathName(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String logName = classLog.getSimpleName()+"_"+dateFormat.format(Calendar.getInstance().getTime())+".txt";
        this.logName = logName;
    }
    
    private void initWriter(){
        try{
            String fullPath = LOG_PATH+logName;
            File logFile = new File(fullPath);
            if(!logFile.exists())
                    logFile.createNewFile();
            writer = new BufferedWriter(new FileWriter(logFile));
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void debug(String debug){
        try{
            writer.write(debug);
            writer.newLine();
            writer.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void error(Throwable t){
        try{
            writer.write("ERROR: "+t.getMessage());
            writer.newLine();
            writer.write(t.getCause().toString());
            writer.newLine();
            writer.flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}