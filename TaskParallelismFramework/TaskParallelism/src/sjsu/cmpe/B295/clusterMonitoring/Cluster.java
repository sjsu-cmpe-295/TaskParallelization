package sjsu.cmpe.B295.clusterMonitoring;

import java.util.concurrent.ConcurrentHashMap;

public class Cluster {
	private ConcurrentHashMap<String, PiNode> piNodes;
	public Cluster() {
		piNodes = new ConcurrentHashMap<>();
	}
	
	public void addPiNode(PiNode piNode){
		this.piNodes.put(piNode.getIpAddress(), piNode);
	}
	
	public PiNode removePiNode(String id){
		return this.piNodes.get(id);
	}
	
	public ConcurrentHashMap<String, PiNode> getPiNodes() {
		return piNodes;
	}
	
	public void setPiNodes(ConcurrentHashMap<String, PiNode> piNodes) {
		this.piNodes = piNodes;
	}

}
