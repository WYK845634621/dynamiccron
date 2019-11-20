package com.kingstar.dynamiccron.task;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class DynamicTaskManager {
    private static ConcurrentHashMap<String, ScheduledFuture> jobs = new ConcurrentHashMap<>();

    private static ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    static {
        scheduler.initialize();
    }

    public static boolean createJob(String jobId, String modelId){
        try{
            ScheduledFuture<?> future = scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    System.out.println("createJob start job by "+modelId+" at "+sdf.format(new Date()));
                }
            }, new Trigger() {
                @Override
                public Date nextExecutionTime(TriggerContext triggerContext) {
                    //要求必须6位，不支持7位
                    String cron = "05 * * * * ?";
                    if (StringUtils.isEmpty(cron)){
                        return null;
                    }
                    CronTrigger trigger = new CronTrigger(cron);
                    return trigger.nextExecutionTime(triggerContext);
                }
            });

            if (!Objects.isNull(future)){
                jobs.putIfAbsent(jobId, future);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean reCreateJob(String jobId, String modelId){
        try{
            ScheduledFuture<?> future = jobs.getOrDefault(jobId, null);
            if (!Objects.isNull(future)){
                future.cancel(true);
                jobs.remove(jobId);
            }
            future = scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    System.out.println("reCreateJob start job by "+modelId+" at "+sdf.format(new Date()));
                }
            }, new Trigger() {
                @Override
                public Date nextExecutionTime(TriggerContext triggerContext) {
                    //要求必须6位，不支持7位
                    String cron = "0/10 * * * * *";
                    if (StringUtils.isEmpty(cron)){
                        return null;
                    }
                    CronTrigger trigger = new CronTrigger(cron);
                    return trigger.nextExecutionTime(triggerContext);
                }
            });

            if (!Objects.isNull(future)){
                jobs.putIfAbsent(jobId, future);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean closeJob(String jobId){
        try{
            ScheduledFuture<?> future = jobs.getOrDefault(jobId, null);
            if (!Objects.isNull(future)){
                future.cancel(true);
                jobs.remove(jobId);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("closeJob close job "+jobId+" "+sdf.format(new Date()));
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
