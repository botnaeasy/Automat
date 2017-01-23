/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package przypominacz;

import java.io.InputStream;
import java.sql.Time;
import java.util.Scanner;


/**
 *
 * @author BotNaEasyEnv
 */
public class Przypomnienie {
    
    private static Przypomnienie instance;
    
    private Time now;
    private Time last;
    private Long coIle;
    private PrzypomnienieFrame frame;
    private Warunek warunek;
    
    private static Logger logger = Logger.getInstance();
    
    private Przypomnienie() {
         warunek = new Warunek();
    }
    
    public static Przypomnienie getInstance() {
        if(instance!=null){
            return instance;
        }else{
            instance = new Przypomnienie();
            return instance;
        }
    }
    
    public void setVariables(Time now, Time last, Long coIle){
        this.now = now;
        this.last = last;
        this.coIle = coIle;
    }
    
    public boolean isReady(){
       return isGoodTime() && warunek.isDone();
    }
    
    private boolean isGoodTime(){
        logger.debug(now.getTime()+">"+last.getTime()+"+"+coIle +" result: "+(now.getTime()>last.getTime()+coIle));
        return now.getTime()>last.getTime()+coIle;
    }
    
    public void showPrzypomnienie(){
        frame = new PrzypomnienieFrame(this);
        frame.setVisible(true);
    }
    
    public void stopPrzypomnienie(){
        synchronized(this){
                frame.dispose();
                notify();
        }
    }
    public void justWait(){
        try{
            synchronized(this){
                wait();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}
