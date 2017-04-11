package sjsu.cmpe.B295.raspberrypi.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskParallelizeApp {
	protected static Logger logger = LoggerFactory.getLogger("TaskParallelizeApp");
	

	public static void main(String[] args) {
		logger.warn("Task Parallelize App Started");
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
