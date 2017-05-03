package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import sjsu.cmpe.B295.election.HeartbeatSenderTask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;


@Service
@EnableAsync
public class TaskSubmission {
    protected static Logger logger = LoggerFactory.getLogger("TaskSubmission");
    private String clientIP = "";

    public void callMaster(Task task, Notifiable notifiable) {
        // TODO Need leader ip, not the clientIP
        // Need leader ip, not the clientIP
        String output = sendTask(HeartbeatSenderTask.masterIP, task.toString());
        notifiable.onTaskCompletion(task.getId(), output);
    }

    @Async
    public void callWorker(Task task, String isTp, Notifiable notifiable) {

        logger.info("calling worker");

        clientIP = HeartbeatSenderTask.clientIP;
        String ip = "";
        String taskId = task.getId();

        String[] workerIPs = new String[HeartbeatSenderTask.workerStatusMap.keySet().size()];
        int index = 0;

        ///get free worker
        for (String key : HeartbeatSenderTask.workerStatusMap.keySet()) {
            workerIPs[index++] = key;
            if (HeartbeatSenderTask.workerStatusMap.get(key) == 0) {
                HeartbeatSenderTask.workerStatusMap.put(key, 1);
                ip = key;
                logger.info("Idle node found, ip: " + ip);
                break;
            }
        }

        if (ip.isEmpty() && HeartbeatSenderTask.workerStatusMap.size() == 0) {
            // No Workers, isTp == true, hence empty Ip, therefore choosing master
            logger.info("Choosing master: " + ip + "to work because, workers:"
                    + HeartbeatSenderTask.workerStatusMap.size() + "; isTP:" + isTp + ".");
            ip = HeartbeatSenderTask.masterIP;
            String output = sendTask(ip, task.toString());
            notifiable.onTaskCompletion(taskId, output);
        } else if (ip.isEmpty() && HeartbeatSenderTask.workerStatusMap.size() != 0) {
            // All workers were busy
            logger.info("No idle node, random ip chosen " + ip);
            ArrayList<String> ipList = new ArrayList<>();
            for (String key : HeartbeatSenderTask.workerStatusMap.keySet()) {
                ipList.add(key);
            }
            Random random = new Random();
            int indexForIP = random.nextInt(HeartbeatSenderTask.workerStatusMap.size());
            ip = ipList.get(indexForIP);

            HeartbeatSenderTask.workerStatusMap.put(ip, HeartbeatSenderTask.workerStatusMap.get(ip) + 1);

            String output = sendTask(ip, task.toString());

            HeartbeatSenderTask.workerStatusMap.put(ip, HeartbeatSenderTask.workerStatusMap.get(ip) - 1);
            notifiable.onTaskCompletion(taskId, output);

        } else if (!ip.isEmpty()) {
            // Found worker
            HeartbeatSenderTask.workerStatusMap.put(ip, HeartbeatSenderTask.workerStatusMap.get(ip) + 1);
            String output = sendTask(ip, task.toString());
            HeartbeatSenderTask.workerStatusMap.put(ip, HeartbeatSenderTask.workerStatusMap.get(ip) - 1);
            notifiable.onTaskCompletion(taskId, output);
        }

    }

    public String sendTask(String ip, String data) {
//        logger.info("inside sendTask");
        String result = null;
        try {
            String uri = "http://" + ip + ":8080/doTask?" + data;

            RestTemplate restTemplate = new RestTemplate();
            result = restTemplate.getForObject(uri, String.class);

            logger.info("output received from worker " + result);
            return result;
        } catch (Exception e) {
            logger.info("Error while calling sendTask");
            logger.info(e.getMessage());
        }
        return result;
    }

//    public String sendMetrics(String ip, String data) {
//        String result=null;
//        try {
//            String uri = "http://"+ip+":8080/calculateMetrics?"+data;
//
//            RestTemplate restTemplate = new RestTemplate();
//            result = restTemplate.getForObject(uri, String.class);
//
//            logger.info("Metrics output"+result);
//            return result;
//        } catch (Exception e) {
//            logger.info("Error while calling calculateMetrics");
//            logger.info(e.getMessage());
//        }
//        return result;
//    }


}