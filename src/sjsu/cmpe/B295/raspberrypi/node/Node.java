package sjsu.cmpe.B295.raspberrypi.node;

import java.io.File;

import org.codehaus.jackson.map.ObjectMapper;

public class Node {
	
	public static String configFilePath;
	public static ConcreteSubject monitor;
	public static NodeState nodeState;
	
	
	public Node(String configFilePath) {
		this.configFilePath = configFilePath;
		this.monitor = new FileMonitor(this.configFilePath);
		this.nodeState = new NodeState(monitor);
		this.monitor.monitorFile();
		
		
//		this.configFile = new FileMonitor(configFilePath);
//		this.configFile.monitorFile();
	}
	
	public void start() {
		if (nodeState == null) {
			System.out.println("Empty Node State");
		} else {
			System.out.println("Got NodeState");
			if (nodeState.routingConfig == null) {
				System.out.println("Routing Config is null");
			}
			else{
				System.out.println(nodeState.routingConfig.routingEntries.size());
			}
		}
	}
	
	public static class JsonUtil {
		private static JsonUtil instance;

		public static void init(File cfg) {

		}

		public static JsonUtil getInstance() {
			if (instance == null)
				throw new RuntimeException("Server has not been initialized");

			return instance;
		}

		public static String encode(Object data) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(data);
			} catch (Exception ex) {
				return null;
			}
		}

		public static <T> T decode(String data, Class<T> theClass) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(data.getBytes(), theClass);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}

	
}
