package cn.itcast.quartz.job;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class HelloQuartz1 {
    public static void main(String[] args) throws SchedulerException {
        System.out.println("11");
        // 定时器对象
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        // 定义一个工作对象
        JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("job1","group1").build();

        // 定义触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1")
                .startNow().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5)).build();

        scheduler.scheduleJob(job,trigger);
        // 开启定时任务
        scheduler.start();
        // 关闭定时任务
       // scheduler.shutdown();
    }

}
