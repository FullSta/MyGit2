package cn.itcast.bos.quartz;

import cn.itcast.bos.service.take_delivery.PromotionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class promotionJob implements Job {

    @Autowired
    private PromotionService promotionService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("活动过期程序执行中...");
        // 每分钟执行一次,当前时间大于配偶motion数据表endDdate,活动已经过期,并且设置status='2'
        promotionService.updateStatus(new Date());


    }
}
