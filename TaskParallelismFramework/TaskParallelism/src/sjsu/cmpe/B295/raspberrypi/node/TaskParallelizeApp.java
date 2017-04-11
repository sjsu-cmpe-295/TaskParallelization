package sjsu.cmpe.B295.raspberrypi.node;

import java.io.File;
import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.raspberrypi.node.RoutingConfig.RoutingEntry;

import org.slf4j.Logger;

public class TaskParallelizeApp {
	protected static Logger logger = LoggerFactory.getLogger("TaskParallelizeApp");
	    public static final String[] status = {"slave","master"};


	public static void main(String[] args) {
		logger.info("Task Parallelize App Started");
		if (args.length == 0) {
			logger.info("usage: server <config file>");
			System.exit(1);
		}

		String configFilePath = args[0];
		logger.info(configFilePath);
		try {
			Node node = new Node(configFilePath);
			node.start();
//			for(RoutingEntry entry: node.nodeState.routingConfig.routingEntries){
//				logger.info(entry.getId()+":"+entry.getHost());
//			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.info("Task Parallelize App Closing");
		}

	}

}
