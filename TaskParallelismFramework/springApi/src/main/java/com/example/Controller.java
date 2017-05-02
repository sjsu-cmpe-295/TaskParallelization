package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static sjsu.cmpe.B295.election.HeartbeatSenderTask.clientIP;

@RestController
public class Controller {

    protected static Logger logger = LoggerFactory.getLogger("Controller");
    //Stores taskId and SubTask count
    static ConcurrentHashMap<String, Integer> taskSubTasksMap = new ConcurrentHashMap<>();
    static ConcurrentHashMap<String, String> taskDetailsMap = new ConcurrentHashMap<>();

    @Autowired
    private RpiDataRepository repository;

    @Autowired
    TaskSubmission taskSubmission;
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/sayHello")
    public String submitTask() {
        logger.info("sayHello accessed");
        return "Hello there";
    }

    /**
     * This method is for the master node and will begin distributing tasks
     */
    @RequestMapping(value = "/submitTask")
    public String submitTask(@RequestBody TaskBody taskBody) {
        long startTime = System.currentTimeMillis();
        logger.info("submitTask accessed");
        for (Task task : taskBody.getTasks()) {
            task.setId(taskBody.getId());
            if (taskSubTasksMap.containsKey(task.getId()))
                taskSubTasksMap.put(task.getId(), taskSubTasksMap.get(task.getId()) + 1);
            else
                taskSubTasksMap.put(task.getId(), 1);
        }


        for (Task task : taskBody.getTasks()) {
            logger.info("Task is " + task.toString());
            if (taskBody.getIsTp().equals("false")) {
                // TODO Check for blocking calls?
                taskSubmission.callMaster(task, new Notifiable() {
                    @Override
                    public void onTaskCompletion(String taskId, String jsonOutput) {
                        if (jsonOutput != null) {
                            Controller.taskSubTasksMap.put(taskId, Controller.taskSubTasksMap.get(taskId) - 1);
                            Controller.taskDetailsMap.put(taskId, Controller.taskDetailsMap.containsKey(taskId) ? Controller.taskDetailsMap.get(taskId) + "#" + jsonOutput : jsonOutput);
                        }
                    }
                });
            } else {
                taskSubmission.callWorker(task, taskBody.getIsTp(), new Notifiable() {
                    @Override
                    public void onTaskCompletion(String taskId, String jsonOutput) {
                        synchronized (this) {
                            if (jsonOutput != null) {
                                //Reduce task sub count
                                Controller.taskSubTasksMap.put(taskId, Controller.taskSubTasksMap.get(taskId) - 1);
                                //Store task output
                                Controller.taskDetailsMap.put(taskId, Controller.taskDetailsMap.containsKey(taskId) ? Controller.taskDetailsMap.get(taskId) + "#" + jsonOutput : jsonOutput);
                                //If all tasks have completed, send output
                            }
//                        } else
//                            sendOutput(clientIP, "{\"Error\":\"No output received from worker, check logs ip: " + ip + "\"}");
                        }
                    }
                });
            }

        }

        while (true) {
            for (String taskId : Controller.taskDetailsMap.keySet()) {
                if (Controller.taskSubTasksMap.containsKey(taskId) && Controller.taskSubTasksMap.get(taskId) == 0) {
                    Controller.taskSubTasksMap.remove(taskId);

                    ObjectMapper mapperFinal = new ObjectMapper();
                    DataStructure ds = new DataStructure();
                    String jsonInString = "";

                    String finalOutputInTaskDetailsMap = Controller.taskDetailsMap.get(taskId);

                    String[] individualJsonStrings = finalOutputInTaskDetailsMap.split("#");

                    for (String oneOutput : individualJsonStrings) {
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            DataStructure obj = mapper.readValue(oneOutput, DataStructure.class);
                            if (obj.getHumidityDataPoints().size() != 0) {
                                ds.setHumidityDataPoints(obj.getHumidityDataPoints());
                            } else if (obj.getTemperatureDataPoints().size() != 0) {
                                ds.setTemperatureDataPoints(obj.getTemperatureDataPoints());
                            } else if (obj.getHumidityMetrics() != null) {
                                ds.setHumidityMetrics(obj.getHumidityMetrics());
                            } else if (obj.getTemperatueMetrics() != null) {
                                ds.setTemperatueMetrics(obj.getTemperatueMetrics());
                            } else {
                                logger.info("Yaha to ana nahi chahiye????");
                            }

                        } catch (IOException ioe) {
                            logger.info(ioe.getMessage());
                        }
                    }
                    try {
                        jsonInString = mapperFinal.writeValueAsString(ds);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }

                    logger.info("sending response");
                    sendOutput(clientIP, "{\"id\":\"" + taskId + "\",\"timeTaken\":\"" + (System.currentTimeMillis() - startTime) + "\",\"output\":" + jsonInString + "}");
                }
            }
            if (Controller.taskSubTasksMap.size() == 0)
                break;
        }


        // Reducer


        return "ok";
    }


    public void sendOutput(String clientIp, String output) {
        logger.info("sending output");
        logger.info("client ip is " + clientIp);
        logger.info("output is " + output);

        try {
            URL url = new URL("http://" + clientIp + ":1300/getOutput");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            logger.info("postdata is " + output);
            out.write(output);
            out.close();
            if (new InputStreamReader(connection.getInputStream()) != null) ;
            logger.info("getOutput invoked successfully.");

        } catch (Exception e) {
            logger.info("Error while calling getOutput");
            logger.info(e.getMessage());
        }
    }

    /**
     * This method is for the worker nodes and will begin task execution
     */
    @RequestMapping("/doTask")
    public String doTask(@RequestParam(value = "id") String id,
                         @RequestParam(value = "sensor") String sensor,
                         @RequestParam(value = "startTime") String startTime,
                         @RequestParam(value = "endTime") String endTime,
                         @RequestParam(value = "type") String type) {

        logger.info("request is id:" + id +
                " sensor:" + sensor +
                " startTime:" + startTime +
                " endTime: " + endTime +
                " type: " + type);

        String jsonInString = "";

        long epochStartTime = getEpochTime(startTime);
        long epochEndTime = getEpochTime(endTime);

        List<RpiData> allData = repository.findByTimestampBetween(epochStartTime,
                epochEndTime,
                new Sort(Sort.Direction.ASC, "timestamp"));

        DataStructure dataStructure = new DataStructure();

        if (type.equals("dataPoints")) {  // type is dataPoints
            for (RpiData rpiData : allData) {
                if (sensor.equals("temperature")) {
                    dataStructure.addTemperatureDataPoint(rpiData.getDateTime(), rpiData.getTemperature());
                } else if (sensor.equals("humidity")) {
                    dataStructure.addHumidityDataPoint(rpiData.getDateTime(), rpiData.getHumidity());
                }
            }
        } else if (type.equals("metrics")) {   // type is metrics
            if (sensor.equals("temperature")) {
                DataStructure.Metrics temperatureMetrics = dataStructure.new Metrics();
                double tempSum = 0;
                double maxTemp = allData.get(0).getTemperature();
                double minTemp = allData.get(0).getTemperature();
                for (RpiData rpiData : allData) {
                    tempSum += rpiData.getTemperature();
                    if (rpiData.getTemperature() > maxTemp) maxTemp = rpiData.getTemperature();
                    if (rpiData.getTemperature() < minTemp) minTemp = rpiData.getTemperature();
                }
                temperatureMetrics.setCount(allData.size());
                temperatureMetrics.setAverage(Double.parseDouble(new DecimalFormat("##.##").format(tempSum / allData.size())));
                temperatureMetrics.setMaximum(maxTemp);
                temperatureMetrics.setMinimum(minTemp);
                dataStructure.setTemperatueMetrics(temperatureMetrics);
            } else if (sensor.equals("humidity")) {
                DataStructure.Metrics humidityMetrics = dataStructure.new Metrics();
                double humiditySum = 0;
                double maxHumidity = allData.get(0).getHumidity();
                double minHumidity = allData.get(0).getHumidity();
                for (RpiData rpiData : allData) {
                    humiditySum += rpiData.getHumidity();
                    if (rpiData.getHumidity() > maxHumidity) maxHumidity = rpiData.getHumidity();
                    if (rpiData.getHumidity() < minHumidity) minHumidity = rpiData.getHumidity();
                }
                humidityMetrics.setCount(allData.size());
                humidityMetrics.setAverage(Double.parseDouble(new DecimalFormat("##.##").format(humiditySum / allData.size())));
                humidityMetrics.setMaximum(maxHumidity);
                humidityMetrics.setMinimum(minHumidity);
                dataStructure.setHumidityMetrics(humidityMetrics);
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dataStructure);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonInString;
    }

    // 2017-04-27 21:46:52
    public long getEpochTime(String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long epoch = date.getTime() / 1000;
        return epoch;
    }


}
