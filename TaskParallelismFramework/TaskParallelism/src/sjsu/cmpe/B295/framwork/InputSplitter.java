package sjsu.cmpe.B295.framwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.communicationMessageHandlers.IHttpRequestHandler;
import sjsu.cmpe.B295.election.Leader;
import sjsu.cmpe.B295.sensorDataCollection.HumidityCollectorTask;
import sjsu.cmpe.B295.sensorDataCollection.TemperatureCollectorTask;

public class InputSplitter implements IHttpRequestHandler{
	protected static Logger logger = LoggerFactory.getLogger("Input Splitter");
	Leader leader;
	public InputSplitter(Leader leader) {
		this.leader = leader;
	}

	@Override
	public void handleHttpRequest(String uri, Integer time) {
		// TODO Auto-generated method stub
		
	}

	public void createTask(String taskType){
		logger.info("In Splitter Create Task");
		switch(taskType){
		case "temperature":
			TemperatureCollectorTask tempTask = new TemperatureCollectorTask();
			leader.taskQueue.add(tempTask);
			break;
		case "humidity":
			HumidityCollectorTask humidityTask = new HumidityCollectorTask();
			leader.taskQueue.add(humidityTask);
			break;
		}
	}
	
	public void splitTask(String taskType){
		logger.info("In Splitter split Task");
		if(taskType == "multiple"){
			createTask("temperature");
			createTask("humidity");
		}
	}
	@Override
	public void setNextHandler(IHttpRequestHandler nextHandler) {
		// TODO Auto-generated method stub
		
	}

}
