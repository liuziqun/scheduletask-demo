package com.fusion.servicetaskscheduling.controller;

import com.fusion.servicetaskscheduling.service.DailyTaskSchedulingService;
import com.fusion.servicetaskscheduling.service.WeeklyTaskSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taskScheduling")
public class TaskSchedulingController{

    @Autowired
    DailyTaskSchedulingService dailyTaskService ;

    @Autowired
    WeeklyTaskSchedulingService weeklyTaskService ;

    @RequestMapping("/runDailyTask")
    @ResponseBody
    public String runDailyTask(){
        boolean flag = dailyTaskService.runTask();
        if(flag){
            return "DailyTaskSchedulingService run is ok." ;
        } else {
            return "DailyTaskSchedulingService run is fail." ;
        }
    }

    @RequestMapping("/runWeeklyTask")
    @ResponseBody
    public String runWeeklyTask(){
        boolean flag = weeklyTaskService.runTask();
        if(flag){
            return "WeeklyTaskSchedulingService run is ok." ;
        } else {
            return "WeeklyTaskSchedulingService run is fail." ;
        }
    }
}
