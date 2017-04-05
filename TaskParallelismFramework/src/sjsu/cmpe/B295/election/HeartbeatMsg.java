package sjsu.cmpe.B295.election;

import sjsu.cmpe.B295.common.CommonProto.Header;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.common.CommunicationMessageProto.Heartbeat;

public class HeartbeatMsg {
	private int nodeId;
	private int destination;
	private String message;
	
	private Header.Builder headerBuilder;
	private Heartbeat.Builder heartBeatBuilder;
	private CommunicationMessage.Builder commMsgBuilder;
	
	public HeartbeatMsg(String message, int nodeId, int destination){
		this.message = message;
		this.nodeId = nodeId;
		this.destination = destination; // To all nodes
		
		headerBuilder = Header.newBuilder();
		headerBuilder.setNodeId(nodeId);
		headerBuilder.setDestination(destination);
		headerBuilder.setTime(System.currentTimeMillis());
		
		heartBeatBuilder = Heartbeat.newBuilder();
		heartBeatBuilder.setMsg(message);
		
		commMsgBuilder = CommunicationMessage.newBuilder();
		commMsgBuilder.setHeader(headerBuilder.build());
		commMsgBuilder.setBeat(heartBeatBuilder.build());
	}
	
	public CommunicationMessage getCommunicationMessage(){
		return this.commMsgBuilder.build();
	}
	
}
