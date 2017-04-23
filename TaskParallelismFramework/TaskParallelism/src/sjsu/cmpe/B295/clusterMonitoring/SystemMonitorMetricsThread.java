package sjsu.cmpe.B295.clusterMonitoring;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.raspberrypi.node.NodeState;


public class SystemMonitorMetricsThread extends TimerTask{
	protected static Logger logger = LoggerFactory
			.getLogger("SystemMonitorMetricsThread");
	SystemStatusReader reader = new SystemStatusReader();
	NodeState nodeState;

	public SystemMonitorMetricsThread(NodeState state){
		this.nodeState = state;
	}
	
	@Override
	public void run() {
		logger.info("Started metrics thread");
		HashMap<String, Float> netStats = null;
		double cpuUsage=0.0;
		float memUsage=0;
		
		try {
			 //cpuUsage = reader.cpuUsage(1);
		} catch (Exception e) {
			cpuUsage = 0;
		}
		try {
			//memUsage = reader.memUsage();
		} catch (Exception e) {
			 memUsage = 0;
			e.printStackTrace();
		}
		try {
			//netStats = reader.networkInOut();
		} catch (Exception e) {
			netStats.put("Network-In", Float.valueOf(0));
    		netStats.put("Network-Out", Float.valueOf(0));
	}
		updateUI(cpuUsage,memUsage,netStats);
		
	}

	public void updateUI(double cpuUsage, float memUsage, HashMap<String, Float> netStats) {
		logger.info("In Metrics Update UI");
		String masterResponse = "";
		StringBuffer response=new StringBuffer();
		final String RESPONSE_START="";
		final String RESPONSE_END="";
		
		//masterResponse="{\"ip\": \""+nodeState.getRoutingConfig().getHost()+"\", \"cpu\": \""+cpuUsage+"\",\"memoryUsage\": \""+memUsage+"\",\"netWorkIn\": \""+netStats.get("Network-In")+"\"},\"netWorkOut\": \""+netStats.get("Network-Out")+"\"}";
		
		masterResponse="{\"ip\": \""+"127.6.5.4"+"\", \"cpu\": \""+0.9+"\",\"memoryUsage\": \""+0.9+"\",\"netWorkIn\": \""+0.9+"\",\"netWorkOut\": \""+0.9+"\"}";
		logger.info(masterResponse);
		URL url;
		try {
			url = new URL("http://localhost:1300/updateMetrics");
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
			response.append(RESPONSE_START);
			response.append(masterResponse);
			response.append(RESPONSE_END);
			out.write(response.toString());
			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while (in.readLine() != null) {}
			in.close();
			logger.info("updateMetrics Invoked Successfully..");
			response.setLength(0);
		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
}