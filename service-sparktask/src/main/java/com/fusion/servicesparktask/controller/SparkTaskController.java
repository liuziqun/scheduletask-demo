package com.fusion.servicesparktask.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@RestController
@RefreshScope
public class SparkTaskController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    @Value("${server.port}")
    String port;
    @RequestMapping("/hi")
    @ResponseBody
    public String home(@RequestParam String name) {
        System.out.println("hi "+name+",i am from port:" +port);
        return "hi "+name+",i am from port:" +port;
    }

    @Value("${schedule.ESToHDFSTask}")
    String eSToHDFSTask ;
    @RequestMapping("/getESToHDFSTask")
    @ResponseBody
    public String runESToHDFSTask(){
        boolean flag = executeLinuxCmd(eSToHDFSTask.replace("dateParam",getYesteday())) ;
        return String.valueOf(flag) ;
    }

    @Value("${schedule.DataProcessTask}")
    String dataProcessTask ;
    @RequestMapping("/getDataProcessTask")
    @ResponseBody
    public String runDataProcessTask(){
        boolean flag = executeLinuxCmd(dataProcessTask) ;
        return String.valueOf(flag) ;
    }

    @Value("${schedule.Data2SqliteTask}")
    String data2SqliteTask ;
    @RequestMapping("/getData2SqliteTask")
    @ResponseBody
    public String runData2SqliteTask(){
        boolean flag = executeLinuxCmd(data2SqliteTask.replace("dateParam",getYesteday())) ;
        return String.valueOf(flag) ;
    }

    @Value("${schedule.Data2SqliteNotLogoutTask}")
    String data2SqliteNotLogoutTask ;
    @RequestMapping("/getData2SqliteNotLogoutTask")
    @ResponseBody
    public String runData2SqliteNotLogoutTask(){
        boolean flag = executeLinuxCmd(data2SqliteNotLogoutTask.replace("dateParam",getYesteday())) ;
        return String.valueOf(flag) ;
    }

    @Value("${schedule.UserLabelTask}")
    String userLabelTask ;
    @RequestMapping("/getUserLabelTask")
    @ResponseBody
    public String runUserLabelTask(){
        boolean flag = executeLinuxCmd(userLabelTask.replace("dateParam",getYesteday())) ;
        return String.valueOf(flag) ;
    }

    @Value("${schedule.ExceptionDataDeviationTask}")
    String deviationTask ;
    @RequestMapping("/getExceptionDataDeviationTask")
    @ResponseBody
    public String runDeviationTask(){
        boolean flag = executeLinuxCmd(deviationTask.replace("dateParam",getYesteday())) ;
        return String.valueOf(flag) ;
    }

    @Value("${schedule.ClearSaprkLogTask}")
    String clearSaprkLogTask ;
    @RequestMapping("/getClearSaprkLogTask")
    @ResponseBody
    public String runClearSaprkLogTask(){
        boolean flag = executeLinuxCmd(clearSaprkLogTask) ;
        return String.valueOf(flag) ;
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

    private String getYesteday(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,-1);
        String yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()) ;
        return yestedayDate ;
    }
}
