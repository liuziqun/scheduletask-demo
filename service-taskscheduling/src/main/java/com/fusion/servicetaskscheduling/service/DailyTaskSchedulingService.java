package com.fusion.servicetaskscheduling.service;

import com.fusion.servicetaskscheduling.interfaces.ITask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DailyTaskSchedulingService implements ITask {
    @Autowired
    RestTemplate restTemplate ;

    @Value("${schedule.dailytaskurl}")
    private String taskUrl;

    @Override
    public boolean runTask() {
        String str = "true" ;

        String[] taskUrls = taskUrl.split(",");
        for(String task : taskUrls){
            System.out.println(task);
            str = restTemplate.getForObject(task,String.class);
            if("false".equals(str)){
                return false ;
            }
        }
        return true ;
    }
}
