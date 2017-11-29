package com.huotu.scrm.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hxh on 2017-07-24.
 */
@Configuration
@EnableScheduling
@Profile("!noSchedule")
public class SchedulingConfig implements SchedulingConfigurer {

    private ExecutorService executorService = taskExecutor();

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(executorService);
    }

    @PreDestroy
    public void beforeClose() {
        executorService.shutdown();
    }

    private ExecutorService taskExecutor() {
        return Executors.newScheduledThreadPool(10);
    }
}
