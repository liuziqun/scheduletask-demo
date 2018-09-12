/**
 *
 */
package com.fusion.servicetaskscheduling.quartz.jobs;

import com.fusion.servicetaskscheduling.configs.QuartzConfigrations;
import com.fusion.servicetaskscheduling.quartz.ResetCron;
import com.fusion.servicetaskscheduling.service.WeeklyTaskSchedulingService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@DisallowConcurrentExecution
public class EveryWeekJobWithCronTrigger implements Job {

    private final static Logger logger = LoggerFactory.getLogger(EveryWeekJobWithCronTrigger.class) ;

    @Value("${cron.frequency.everyweekjobwithcrontrigger}")
    private String frequency;

    @Lazy
    @Autowired
    WeeklyTaskSchedulingService taskSchedulingService ;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        logger.info("Running EveryWeekJob | frequency frequency --> " + frequency);
        ResetCron.reset(jobExecutionContext,frequency) ;
        boolean flag = taskSchedulingService.runTask();
        if(flag){
            logger.info("----------EveryWeekJob has been successfully execution.----------");
        } else {
            logger.info("----------EveryWeekJob appear problem, program terminating.----------");
        }
    }

    @Bean(name = "everyWeekJobWithCronTriggerBean")
    public JobDetailFactoryBean sampleJob() {
        return QuartzConfigrations.createJobDetail(this.getClass());
    }

    @Bean(name = "everyWeekJobWithCronTriggerBeanTrigger")
    public CronTriggerFactoryBean sampleJobTrigger(@Qualifier("everyWeekJobWithCronTriggerBean") JobDetail jobDetail) {
        return QuartzConfigrations.createCronTrigger(jobDetail, frequency);
    }
}
