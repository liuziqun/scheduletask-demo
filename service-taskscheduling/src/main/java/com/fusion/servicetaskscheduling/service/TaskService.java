package com.fusion.servicetaskscheduling.service;

import com.fusion.servicetaskscheduling.interfaces.ITask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class TaskService implements ITask {

    private final Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    @Autowired
    RestTemplate restTemplate ;

    @Value("${task.reboot}")
    private String taskUrl;

    @Override
    public boolean runTask() {
        boolean flag = executeLinuxCmd(taskUrl) ;
        return flag ;
    }

    public boolean executeLinuxCmd(String cmd) {
        Runtime run = Runtime.getRuntime() ;
        boolean flag = true ;
        try {
            logger.info("---------------------start run cmd="+cmd+"---------------------");
            Process process = run.exec(cmd) ;

            //处理inpustream的线程
            new Thread(){
                @Override
                public void run(){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line = null ;
                    try {
                        while((line = bufferedReader.readLine())!=null){
                            System.out.println("ouput: " + line);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            //处理inpustream的线程
            new Thread(){
                @Override
                public void run(){
                    BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line = null ;
                    try {
                        while((line = err.readLine())!=null){
                            System.out.println("ouput: " + line);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            err.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            int status = process.waitFor();
            if(status != 0){
                flag = false ;
            }
            logger.info("---------------------finish run cmd="+cmd+"---------------------");
        } catch (Exception e) {
            e.printStackTrace();
            return false ;
        }
        return flag ;
    }
}
