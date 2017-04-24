package sjsu.cmpe.B295.framework;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.clusterMonitoring.Cluster;
import sjsu.cmpe.B295.clusterMonitoring.PiNode;
import sjsu.cmpe.B295.clusterMonitoring.PiNodeType;
import sjsu.cmpe.B295.election.Leader;
import sjsu.cmpe.B295.httpRequestMessageHandlers.IHttpRequestHandler;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;
import sjsu.cmpe.B295.sensorDataCollection.IParallelizable;

public class Mapper extends TimerTask{
	protected static Logger logger = LoggerFactory.getLogger("Mapper");
	int counter = 0;
	Leader leader;
	Cluster cluster;
	NodeState nodestate;
	List <String> roundRobinArray = new ArrayList<String>();
	private ConcurrentHashMap<String, PiNode> piNodes;
	public Mapper(Leader leader,NodeState nodestate){
		this.leader = leader;
		this.nodestate = nodestate;
	}
	
	@Override
	public void run() {
		IParallelizable task;
		logger.info("In Mapper Thread");
		if(leader.taskQueue.peek()!=null){
			getActiveNodesInCluster();
			while(leader.taskQueue.peek()!=null){
				task = leader.taskQueue.poll();
				map(task);
			}
		}else{
			logger.info("No Task to Perform");
		}
		
	}

	private void getActiveNodesInCluster() {
		cluster = leader.getCluster();
		piNodes = cluster.getPiNodes();
		PiNode piNode;
		
		Iterator i = piNodes.entrySet().iterator();
		while(i.hasNext()){
			Map.Entry<String, PiNode> pair = (Map.Entry<String, PiNode>)i.next();
			piNode = pair.getValue();
			if(piNode.getPiNodeType()==PiNodeType.MASTER){
				piNodes.remove(pair.getKey(),pair.getValue());
			}else{
				roundRobinArray.add(pair.getKey());
				
			}
		}
		
	}

	public void map(IParallelizable task){
		int nodeToPick;
		String id;
		PiNode node;
		if(roundRobinArray.size()!=0){
			nodeToPick = counter % roundRobinArray.size();
			id = roundRobinArray.get(nodeToPick);
			node = piNodes.get(id);
			
		}else{
			logger.info("No Worker to Perform Task");
		}
		
		
		
	}
	

}
