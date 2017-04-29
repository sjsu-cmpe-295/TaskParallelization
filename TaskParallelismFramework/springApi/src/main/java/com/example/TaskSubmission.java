package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;


@Service
@EnableAsync
public class TaskSubmission {
    protected static Logger logger = LoggerFactory.getLogger("TaskSubmission");
    private String clientIP="localhost";
    @Async
    public void callWorker(Task task) {
        logger.info("calling worker");
        String taskId = task.getId();
        String ip = clientIP;

        ///get free worker


        String output=sendTask(ip, task.toString());
        if(output!=null) {
            //Reduce task sub count
            Controller.taskSubTasksMap.put(taskId,Controller.taskSubTasksMap.get(taskId)-1);
            //Store task output
            Controller.taskDetailsMap.put(taskId,Controller.taskDetailsMap.containsKey(taskId) ? Controller.taskDetailsMap.get(taskId)+","+output:output);

            //If all tasks have completed, send output
            if(Controller.taskSubTasksMap.get(taskId)==0)
                sendOutput(clientIP, "{\"id\":\"" + taskId + "\",\"output\":[" + Controller.taskDetailsMap.get(taskId)+ "]}");
        }else
            sendOutput(clientIP,"{\"Error\":\"No output received from worker, check logs ip: "+ip+"\"}");
    }

    public String sendTask(String ip, String data) {
//        logger.info("inside sendTask");
        String result=null;
        try {
            String uri = "http://"+ip+":8082/doTask?"+data;

            RestTemplate restTemplate = new RestTemplate();
            result = restTemplate.getForObject(uri, String.class);

            logger.info("output received from worker "+result);
            return result;
        } catch (Exception e) {
            logger.info("Error while calling sendTask");
            logger.info(e.getMessage());
        }
        return result;
    }

    public void sendOutput(String clientIp, String output) {
//        logger.info("sending output");
        try {
            URL url = new URL("http://" + clientIp + ":1300/getOutput");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            logger.info("postdata is "+output);
            out.write(output);
            out.close();
            if(new InputStreamReader(connection.getInputStream())!=null);
            logger.info("getOutput invoked successfully.");

        } catch (Exception e) {
            logger.info("Error while calling getOutput");
            logger.info(e.getMessage());
        }
    }

}
