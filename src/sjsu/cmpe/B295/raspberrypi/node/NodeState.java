package sjsu.cmpe.B295.raspberrypi.node;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.raspberrypi.node.Node.JsonUtil;
import sjsu.cmpe.B295.raspberrypi.node.RoutingConfig.RoutingEntry;
import sjsu.cmpe.B295.raspberrypi.node.edges.EdgeMonitor;


public class NodeState implements IFileObserver{
	protected static Logger logger = LoggerFactory.getLogger("NodeState");
	protected static RoutingConfig routingConfig;
	protected ConcreteFileMonitor subject;
	private EdgeMonitor edgeMonitor;
	
	public NodeState(ConcreteFileMonitor subject){
		this.subject = subject;
		this.subject.addObserver(this);
	}

	private boolean verifyConf(RoutingConfig conf) {
		return (conf != null);
	}
	
	/**
	 * @return the edgeMonitor
	 */
	public EdgeMonitor getEdgeMonitor() {
		return edgeMonitor;
	}

	/**
	 * @param edgeMonitor the edgeMonitor to set
	 */
	public void setEdgeMonitor(EdgeMonitor edgeMonitor) {
		this.edgeMonitor = edgeMonitor;
	}
	
	/**
	 * @return the routingConfig
	 */
	public static RoutingConfig getRoutingConfig() {
		return routingConfig;
	}

	/**
	 * @param routingConfig the routingConfig to set
	 */
	public static void setRoutingConfig(RoutingConfig routingConfig) {
		NodeState.routingConfig = routingConfig;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		logger.info("Updating RoutingConfig");
		RoutingConfig conf = null;
		File configFile = this.subject.getConfigFile();
		if (!configFile.exists())
			throw new RuntimeException(
				configFile.getAbsolutePath() + " not found");
		// resource initialization - how message are processed
		BufferedInputStream br = null;
		try {
			byte[] raw = new byte[(int) configFile.length()];
			br = new BufferedInputStream(new FileInputStream(configFile));
			br.read(raw);
			conf = JsonUtil.decode(new String(raw), RoutingConfig.class);
//			System.out.println(conf.getNodeId());
			if (!verifyConf(conf))
				throw new RuntimeException(
					"verification of configuration failed");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
//		this.routingConfig = conf;
		setRoutingConfig(conf);
//		for (RoutingEntry routingEntry : routingConfig.routingEntries) {
//			System.out.println(routingEntry.getId());
//			System.out.println(routingEntry.getHost());
//			System.out.println(routingEntry.getPort());
//			System.out.println();
//		}
//		logger.info("*************************************************");
	}
}
