package sjsu.cmpe.B295.election;

import sjsu.cmpe.B295.common.CommonProto.Header;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.common.CommunicationMessageProto.Election;
import sjsu.cmpe.B295.common.CommunicationMessageProto.Heartbeat;

public class ElectionMsg {
	private int nodeId;
	private int destination;
	private String message;
	
	private Header.Builder headerBuilder;
	private Election.Builder electionBuilder;
	private CommunicationMessage.Builder commMsgBuilder;
	
	public ElectionMsg(String message, int nodeId, int destination){
		this.message = message;
		this.nodeId = nodeId;
		this.destination = destination; // To all nodes
		
		headerBuilder = Header.newBuilder();
		headerBuilder.setNodeId(nodeId);
		headerBuilder.setDestination(destination);
		headerBuilder.setTime(System.currentTimeMillis());
		
		electionBuilder = Election.newBuilder();
		electionBuilder.setMsg(message);
		
		commMsgBuilder = CommunicationMessage.newBuilder();
		commMsgBuilder.setHeader(headerBuilder.build());
		commMsgBuilder.setElectionMessage(electionBuilder.build());
	}
	
	public CommunicationMessage getCommunicationMessage(){
		return this.commMsgBuilder.build();
	}
}
