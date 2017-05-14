package com.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.*;

/**
 * Created by dmodh on 4/29/17.
 */
public class ReadDataFromDB {

    public String readData(String ipAddress, String sensor, String startTime, String endTime) {
        String jsonInString = "";
        try {
            DataStructure dataStructure = new DataStructure();
            MongoClient mongo = new MongoClient( "localhost" , 27017 );
            DB db = mongo.getDB("rpiData");
            DBCollection table = db.getCollection("rpiData");

            BasicDBObject query = new BasicDBObject();
            query.put("dateTime", BasicDBObjectBuilder.start("$gte", startTime).add("$lte", endTime).get());

            DBCursor cursor = table.find(query).sort(new BasicDBObject("timestamp", -1));

            while (cursor.hasNext()) {
//                System.out.println(cursor.next());
                BasicDBObject obj = (BasicDBObject) cursor.next();
                if(sensor.equals("temperature")) {
                    dataStructure.addTemperatureDataPoint(obj.getString("dateTime"), obj.getDouble("temperature"));
                } else if(sensor.equals("humidity")) {
                    dataStructure.addHumidityDataPoint(obj.getString("dateTime"), obj.getDouble("humidity"));
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            jsonInString = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(dataStructure);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonInString;
    }

}
