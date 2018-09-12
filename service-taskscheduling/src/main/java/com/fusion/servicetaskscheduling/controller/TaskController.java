package com.fusion.servicetaskscheduling.controller;

import com.fusion.servicetaskscheduling.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService ;

    @RequestMapping("/reboot")
    @ResponseBody
    public String runTask(){
        boolean flag = taskService.runTask();
        if(flag){
            return "RebootTaskService run is ok." ;
        } else {
            return "RebootTaskService run is fail." ;
        }
    }
}
