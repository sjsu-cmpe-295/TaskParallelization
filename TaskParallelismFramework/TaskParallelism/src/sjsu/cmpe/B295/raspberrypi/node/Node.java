package sjsu.cmpe.B295.raspberrypi.node;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import io.netty.bootstrap.ServerBootstrap;

public class Node {
	protected static Logger logger = Logger.getLogger("Node");
	public static String configFilePath;
	public static ConcreteFileMonitor monitor;
	public static NodeState nodeState;
	protected static HashMap<Integer, ServerBootstrap> bootstrap = new HashMap<Integer, ServerBootstrap>();

	public Node(String configFilePath) {
		this.configFilePath = configFilePath;
		this.monitor = new FileMonitor(this.configFilePath);
		this.nodeState = new NodeState(monitor);
		this.monitor.monitorFile();

	}

	public void start() {
		StartProtoCommunication protoServer = new StartProtoCommunication(
			this.nodeState, this.monitor);
		Thread protoServerThread = new Thread(protoServer);
		protoServerThread.start();

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
