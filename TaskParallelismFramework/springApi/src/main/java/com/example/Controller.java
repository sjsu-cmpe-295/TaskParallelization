package com.example;

import org.codehaus.jackson.map.util.JSONPObject;
import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sjsu.cmpe.B295.raspberrypi.node.TaskParallelizeApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class Controller {
    protected static Logger logger = LoggerFactory.getLogger("Controller");
    //Stores taskId and SubTask count
    static ConcurrentHashMap<String, Integer> taskSubTasksMap = new ConcurrentHashMap<String, Integer>();
    static ConcurrentHashMap<String, String> taskDetailsMap = new ConcurrentHashMap<String, String>();


    @Autowired
    TaskSubmission taskSubmission;
    private final AtomicLong counter = new AtomicLong();

    /**
     * This method is for the master node and will begin distributing tasks
     */
    @RequestMapping(value = "/submitTask")
    public String submitTask(@RequestBody Tasks tasks) {
        logger.info("submitTask accessed");
        for (Task task : tasks.getTasks()) {
            task.setId(tasks.getId());
            if (taskSubTasksMap.containsKey(task.getId()))
                taskSubTasksMap.put(task.getId(), taskSubTasksMap.get(task.getId()) + 1);
            else
                taskSubTasksMap.put(task.getId(), 1);
        }

        for (Task task : tasks.getTasks()) {
            logger.info("Task is " + task.toString());
            taskSubmission.callWorker(task);
        }
        logger.info("sending response");

        return "ok";
    }

    /**
     * This method is for the worker nodes and will begin task execution
     */
    @RequestMapping("/doTask")
    public String doTask(@RequestParam(value = "id") String id, @RequestParam(value = "sensor") String sensor, @RequestParam(value = "time") String time) {
        logger.info("doTask accessed");
        logger.info("request is id:" + id + " sensor:" + sensor + " time:" + time + " ");

//        List l = new ArrayList<Node>();
//        l.add(new Node(Long.valueOf(id), TaskParallelizeApp.status[new Random().nextInt(TaskParallelizeApp.status.length)], "rasp1", "127.168.0.34", "Idle"));
//        l.add(new Node(Long.valueOf(id), TaskParallelizeApp.status[new Random().nextInt(TaskParallelizeApp.status.length)], "rasp2", "127.168.0.05", "Running"));
//        l.add(new Node(Long.valueOf(id), TaskParallelizeApp.status[new Random().nextInt(TaskParallelizeApp.status.length)], "rasp3", "127.168.0.21", "Idle"));
//        l.add(new Node(Long.valueOf(id), TaskParallelizeApp.status[new Random().nextInt(TaskParallelizeApp.status.length)], "rasp4", "127.168.0.17", "Running"));
        return "{\"" + sensor + "\":\""+sensor+id+"\"}";
    }


}
