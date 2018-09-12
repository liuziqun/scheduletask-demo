package com.fusion.servicetaskscheduling.quartz;

import org.quartz.*;

public class ResetCron {

    public static void reset(JobExecutionContext jobExecutionContext , String frequency){
//        JobDetail jobDetail = jobExecutionContext.getJobDetail() ;
        Trigger trigger = jobExecutionContext.getTrigger();
        Scheduler scheduler = jobExecutionContext.getScheduler() ;

        CronTrigger cronTrigger = null;
        try {
            cronTrigger = (CronTrigger) scheduler.getTrigger(trigger.getKey());
            String currentCron = cronTrigger.getCronExpression();// 当前Trigger使用的
            if (currentCron.equals(frequency)) {
                System.out.println(currentCron+"--------->>>>>>>>>"+frequency);
                // 如果当前使用的cron表达式和从数据库中查询出来的cron表达式一致，则不刷新任务
            } else {
                System.out.println(currentCron+"--------->>>>>>>>>"+frequency);
                // 表达式调度构建器
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(frequency);
                // 按新的cronExpression表达式重新构建trigger
                cronTrigger = (CronTrigger) scheduler.getTrigger(trigger.getKey());
                cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(trigger.getKey())
                        .withSchedule(scheduleBuilder).build();
                // 按新的trigger重新设置job执行
                scheduler.rescheduleJob(trigger.getKey(), cronTrigger);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
