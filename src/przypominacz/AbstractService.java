/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package przypominacz;

import java.sql.Time;
import java.util.Properties;
import org.joda.time.DateTime;

/**
 *
 * @author BotNaEasyEnv
 */
public abstract class AbstractService extends Thread{
    
    protected String configuractionFile;
    private Properties configuration;
    protected Time startTime;
    protected Time endTime;
    protected Time lastConfiguration;
    protected long confWaitTime;
    protected long waitTime;
    private boolean exit;
    private boolean running = false;
    private final Object lock = new Object();
    protected static Logger logger;
    
    public AbstractService(){
        setDaemon(true);
    }
    
    public void startService(){
        try{
            configuration = new Properties();
            configuration.load(this.getClass().getClassLoader().getResourceAsStream(configuractionFile));
            initLogger();
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e);
        }
    }
    
    protected abstract void initLogger();
    
    private void reloadConfiguration(){
        try{
            configuration.load(this.getClass().getClassLoader().getResourceAsStream(configuractionFile));
            lastConfiguration = new Time(System.currentTimeMillis());
            startTime = Time.valueOf(configuration.getProperty("startTime"));
            endTime = Time.valueOf(configuration.getProperty("endTime"));
            confWaitTime = Long.parseLong(configuration.getProperty("confWaitTime"));
            waitTime = Long.parseLong(configuration.getProperty("waitTime"));
            configuration(configuration);
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e);
        }
    }
    
    protected void configuration(Properties prop){}
    
    protected abstract void startObject();

    @Override
    public void run() {
        reloadConfiguration();
        while(!isInterrupted()&&!isExit()){
            DateTime now = new DateTime();
            if(lastConfiguration.getTime() + confWaitTime < now.getMillis()){
                reloadConfiguration();
            }
            if(now.getMillisOfDay()>startTime.getTime()&&now.getMillisOfDay()<endTime.getTime()){
                if(!running){
                    try{
                        setRunning(true);
                        startObject();
                    }catch(Throwable t){
                        t.printStackTrace();
                        logger.error(t);
                    }finally{
                        setRunning(false);
                        try{
                            synchronized(lock){
                                logger.debug("AbstractService: It's : "+new DateTime()+" i'm going sleep to "+new DateTime().plus(waitTime));
                                lock.wait(waitTime);
                                lock.notify();
                            }
                        }catch(InterruptedException e){
                            e.printStackTrace();
                            logger.error(e);
                        }
                    }
                }
            }
        }
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    
    
    
    
    
}
