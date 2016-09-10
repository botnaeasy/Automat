/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package przypominacz;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author BotNaEasyEnv
 * @param <T>
 */
public abstract class AbstractMultiThreadService<T> extends AbstractService {

    protected int maxThreads;
    private AtomicInteger currentThreads;
    
    public AbstractMultiThreadService()  {
        super();
        currentThreads =  new AtomicInteger(0);
    }

    @Override
    protected void configuration(Properties prop) {
        maxThreads = Integer.parseInt(prop.getProperty("maxThreads"));
    }

    @Override
    protected void startObject() {
        if(currentThreads.get()< maxThreads){
            T quest = getNextQuest();
            if(quest != null){
                questTaken(quest);
                Thread thread =new Thread(){
                    @Override
                    public void run() {
                        try{
                            currentThreads.getAndIncrement();
                            doQuest(quest);
                        }catch(Throwable e){
                            e.printStackTrace();
                            logger.error(e);
                            onError(e, quest);
                        }finally{
                            try{
                                onFinally(quest);
                            }catch(Throwable e){
                                e.printStackTrace();
                                logger.error(e);
                            }
                            currentThreads.getAndDecrement();
                        }
                    }
                };
                thread.start();
            }
        }
        logger.debug("AbstractMultiThreadService: Working Threads: "+currentThreads.get()+"/"+maxThreads);
    }
    
    protected abstract T getNextQuest();
    
    protected abstract void questTaken(T quest);
    
    protected abstract void doQuest(T quest);
    
    protected abstract void onError(Throwable e, T quest);
    
    protected abstract void onFinally(T quest);
    
}
