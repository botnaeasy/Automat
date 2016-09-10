/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package przypominacz;

import static java.lang.Thread.sleep;

/**
 *
 * @author BotNaEasyEnv
 */
public class MainApp {
    
    private static final Object lock = new Object();
    
    public static void main(String []args){
        Przypominacz przypominacz = new Przypominacz();
        przypominacz.startService();
        przypominacz.start();
        try{
            while(przypominacz.isAlive()){
                sleep(1000);
            }
        }catch(Exception e){
            
        }
    }
}
