package sjsu.cmpe.B295.raspberrypi.node;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class TaskParallelizeApp {
    protected static Logger logger = Logger.getLogger("TaskParallelizeApp");
    public static final String[] status = {"slave", "master"};


    public static void main(String[] args) {
        Logger.getLogger("io.netty").setLevel(Level.OFF);
        logger.info("Task Parallelize App Started");

        if (args.length == 0) {
            logger.info("usage: server <config file>");
            System.exit(1);
        }

        String configFilePath = args[0];
        logger.debug(configFilePath);
        try {
            Node node = new Node(configFilePath);
            node.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("Task Parallelize App Closing");
        }

    }

    public void start(String configFilePath) {
        Logger.getLogger("io.netty").setLevel(Level.OFF);
        logger.info("Task Parallelize App Started");

        logger.debug("configFilePath " + configFilePath);
        try {
            Node node = new Node(configFilePath);
            node.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            logger.info("Task Parallelize App Closing");
        }
    }

}
