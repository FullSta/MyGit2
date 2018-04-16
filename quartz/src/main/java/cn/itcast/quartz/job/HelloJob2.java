package cn.itcast.quartz.job;

import cn.itcast.quartz.service.HelloService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class HelloJob2 implements Job {

    @Autowired
    private HelloService helloService;

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        helloService.sayHello();
    }
}
