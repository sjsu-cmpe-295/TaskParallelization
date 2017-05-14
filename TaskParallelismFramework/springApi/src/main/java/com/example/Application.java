package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import sjsu.cmpe.B295.raspberrypi.node.TaskParallelizeApp;

@SpringBootApplication
@EnableAsync
public class Application {
    protected static Logger logger = LoggerFactory.getLogger("Application");

//    @Value("${pool.size:1}")
//    private int poolSize;;
//
//    @Value("${queue.capacity:0}")
//    private int queueCapacity;

//    @Bean(name="workExecutor")
//    public TaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setMaxPoolSize(poolSize);
//        taskExecutor.setQueueCapacity(queueCapacity);
//        taskExecutor.afterPropertiesSet();
//        return taskExecutor;
//    }
    public static void main(String[] args) {
        if (args.length == 0) {
            logger.error("usage: server <config file>");
            System.exit(1);
        }
        SpringApplication.run(Application.class, args);
        TaskParallelizeApp taskParallelizeApp = new TaskParallelizeApp();
        taskParallelizeApp.start(args[0]);
    }
}
