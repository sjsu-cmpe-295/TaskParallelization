package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sjsu.cmpe.B295.raspberrypi.node.TaskParallelizeApp;

@SpringBootApplication
public class Application {
    protected static Logger logger = LoggerFactory.getLogger("Application");

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
