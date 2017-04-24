package sjsu.cmpe.B295.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.election.Leader;
import sjsu.cmpe.B295.httpRequestMessageHandlers.IHttpRequestHandler;


public class InputAnalyzer {
	protected static Logger logger = LoggerFactory.getLogger("Input Analyser");
	private IHttpRequestHandler successor = null;
	//IHttpRequestHandler mapHandler = new Mapper();
	InputSplitter splitHandler;
	Leader leader;
	public InputAnalyzer(Leader leader) {
		this.leader = leader;
		splitHandler = new InputSplitter(leader);
	}

	public boolean isIndependentTask(String taskType){
		switch(taskType){
		case "temperature":
			return true;
		case "humidity":
			return true;
			
		case "multiple":
			return false;
			
		default:
			return false;
		}
		
	}

//	@Override
//	public void handleHttpRequest(String uri, Integer time) {
//		logger.info("In Input Analyser Handle Request");
//		String taskType;
//		switch(uri){
//		case "/getTemperature":
//			taskType = "temperature";
//			if(isIndependentTask(taskType))
//				splitHandler.createTask(taskType);
//			break;
//			
//		case "/getHumidity":
//			taskType = "humidity";
//			if(isIndependentTask(taskType))
//				splitHandler.createTask(taskType);
//			break;
//			
//		case "/getBoth":
//			taskType = "multiple";
//			if(!isIndependentTask(taskType))
//				splitHandler.splitTask(taskType);
//			break;
//			
//			
//		
//		}
//		
//		
//		
//	}

//	@Override
//	public void setNextHandler(IHttpRequestHandler nextHandler) {
//		this.successor = nextHandler;
//		
//	}
	
	
}
