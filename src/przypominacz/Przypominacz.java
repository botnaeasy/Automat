/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package przypominacz;

import java.sql.Time;
import java.util.Properties;

/**
 *
 * @author BotNaEasyEnv
 */
public class Przypominacz extends AbstractMultiThreadService<Przypomnienie>{

    private Long coIle;
    private Time lastExecute;
    
    public Przypominacz(){
        super();
        configuractionFile = "resources/przypominacz.properties";
    }

    @Override
    protected Przypomnienie getNextQuest() {
        Przypomnienie przypomnienie = Przypomnienie.getInstance();
        setLastExecute();
        przypomnienie.setVariables(new Time(System.currentTimeMillis()), lastExecute, coIle);
        if(przypomnienie.isReady()){
            logger.debug("ready");
            return przypomnienie;
        }
        return null;
    }
    
    private void setLastExecute(){
        if(lastExecute==null){
            lastExecute = new Time(System.currentTimeMillis());//+coIle);
        }
    }

    @Override
    protected void questTaken(Przypomnienie quest) {
        logger.debug("quest taken");
    }

    @Override
    protected void doQuest(Przypomnienie quest) {
        quest.showPrzypomnienie();
        quest.justWait();
    }

    @Override
    protected void onError(Throwable t, Przypomnienie quest) {
        logger.debug("error on quest");
    }

    @Override
    protected void onFinally(Przypomnienie quest) {
        logger.debug("ending quest");
        lastExecute = new Time(System.currentTimeMillis());
    }

    @Override
    protected void configuration(Properties prop) {
        super.configuration(prop);
        coIle = Long.parseLong(prop.getProperty("coIlePrzypomnienie"));
    }

    @Override
    protected void initLogger() {
        logger = Logger.getInstance(getClass());
    }
    
    
}
