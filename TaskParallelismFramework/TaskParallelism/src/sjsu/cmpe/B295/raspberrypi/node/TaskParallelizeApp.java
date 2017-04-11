package sjsu.cmpe.B295.raspberrypi.node;

import java.io.File;

import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.raspberrypi.node.RoutingConfig.RoutingEntry;

import org.slf4j.Logger;

public class TaskParallelizeApp {
    protected static Logger logger = LoggerFactory.getLogger("TaskParallelizeApp");
    public static final String[] status = {"slave", "master"};


    public void start(String configFilePath) {
        logger.info("Task Parallelize App Started");
        logger.info("configFilePath " + configFilePath);
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
