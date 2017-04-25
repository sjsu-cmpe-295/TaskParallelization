package sjsu.cmpe.B295.clusterMonitoring;

import java.util.concurrent.ConcurrentHashMap;

public class Cluster {
	private ConcurrentHashMap<Integer, PiNode> piNodes;
	public Cluster() {
		piNodes = new ConcurrentHashMap<>();
	}
	
	public void addPiNode(PiNode piNode){
		this.piNodes.put(piNode.getId(), piNode);
	}
	
	public PiNode removePiNode(String id){
		return this.piNodes.get(id);
	}
	
	public ConcurrentHashMap<Integer, PiNode> getPiNodes() {
		return piNodes;
	}
	
	public void setPiNodes(ConcurrentHashMap<Integer, PiNode> piNodes) {
		this.piNodes = piNodes;
	}

}
