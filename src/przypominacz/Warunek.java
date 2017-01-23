/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package przypominacz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author BotNaEasyEnv
 */
public class Warunek {
    
    private List<String> taskList;
    public static final String TASK_LIST_FILE = "D:\\confs\\tasklist.txt";
    private Scanner scanner;
    
    public Warunek(){
        taskList = new ArrayList<String>();
        setScanner();
        fillTaskList();
    }
    
    private void setScanner() {
        try{
            scanner = new Scanner(new File(TASK_LIST_FILE));
        }catch(Exception e){
            System.out.println("Bzdura! (chyba że nie ma pliku)");
        }
    }
    
    public boolean isDone(){
        check4newTask();
        boolean result = true;
        for(int i=0;i<taskList.size();i++){
            result = result &&  !isProcessRunning(taskList.get(i));
        }
        return result;
    }
    
    private void fillTaskList() {
        taskList.clear();
        setScanner();
        while(scanner.hasNextLine()){
            taskList.add(scanner.nextLine());
        }
    }
    
    
    private void check4newTask(){
        int counter =0;
        setScanner();
        while(scanner.hasNextLine()){
            scanner.nextLine();
            counter++;
        }
        if(counter!=taskList.size()){
            fillTaskList();
        }
    }

     private boolean isProcessRunning(String processName){
        String tasksList="";
        try{
            ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
            Process process = processBuilder.start();
            tasksList = toString(process.getInputStream());
        }catch(Exception e){
            e.printStackTrace();
        }

        return tasksList.contains(processName);
    }

    private String toString(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String string = scanner.hasNext() ? scanner.next() : "";
        scanner.close();

        return string;
    }
}
