package sjsu.cmpe.B295.raspberrypi.node;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.raspberrypi.node.Node.JsonUtil;

public class NodeState implements Observer{
	protected static Logger logger = LoggerFactory.getLogger("NodeState");
	protected static RoutingConfig routingConfig;
	protected ConcreteSubject subject;
	
	public NodeState(ConcreteSubject subject){
		this.subject = subject;
		this.subject.addObserver(this);
	}

	private boolean verifyConf(RoutingConfig conf) {
		return (conf != null);
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
		this.routingConfig = conf;
		logger.info(this.routingConfig.routingEntries.size()+"");
	}

}
