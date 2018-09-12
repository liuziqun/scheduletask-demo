package com.fusion.servicepythontask.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@RefreshScope
public class PythonTaskController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    @Value("${server.port}")
    String port;
    @RequestMapping("/hi")
    @ResponseBody
    public String home(@RequestParam String name) {
        System.out.println("hi "+name+",i am from port:" +port);
        return "hi "+name+",i am from port:" +port;
    }

    @Value("${schedule.top500Task.dept}")
    String top500DeptTask ;
    @RequestMapping("/getTop500DeptTask")
    @ResponseBody
    public String runTop500DeptTask(){
        boolean flag = executeLinuxCmd(top500DeptTask.replace("dateParam",getYesteday())) ;
        return String.valueOf(flag) ;
    }

    @Value("${schedule.top500Task.self}")
    String top500SelfTask ;
    @RequestMapping("/getTop500SelfTask")
    @ResponseBody
    public String runTop500SelfTask(){
        boolean flag = executeLinuxCmd(top500SelfTask.replace("dateParam",getYesteday())) ;
        return String.valueOf(flag) ;
    }

    @Value("${schedule.senCommandTask.dept}")
    String senCommandDeptTask ;
    @RequestMapping("/getSenCommandDeptTask")
    @ResponseBody
    public String runSenCommandDeptTask(){
        boolean flag = executeLinuxCmd(senCommandDeptTask.replace("dateParam",getYesteday())) ;
        return String.valueOf(flag) ;
    }

    @Value("${schedule.senCommandTask.self}")
    String senCommandSelfTask ;
    @RequestMapping("/getSenCommandSelfTask")
    @ResponseBody
    public String runSenCommandSelfTask(){
        boolean flag = executeLinuxCmd(senCommandSelfTask.replace("dateParam",getYesteday())) ;
        return String.valueOf(flag) ;
    }

    @Value("${schedule.userBehaviorTask.dept}")
    String userBehaviorDeptTask ;
    @RequestMapping("/getUserBehaviorDeptTask")
    @ResponseBody
    public String runUserBehaviorDeptTask(){
        boolean flag = executeLinuxCmd(userBehaviorDeptTask.replace("dateParam",getYesteday())) ;
        return String.valueOf(flag) ;
    }

    @Value("${schedule.userBehaviorTask.self}")
    String userBehaviorSelfTask ;
    @RequestMapping("/getUserBehaviorSelfTask")
    @ResponseBody
    public String runUserBehaviorSelfTask(){
        boolean flag = executeLinuxCmd(userBehaviorSelfTask.replace("dateParam",getYesteday())) ;
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
