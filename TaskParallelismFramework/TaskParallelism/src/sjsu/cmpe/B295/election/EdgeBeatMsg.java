package sjsu.cmpe.B295.election;

import sjsu.cmpe.B295.common.CommonProto.Header;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.common.CommunicationMessageProto.EdgeBeat;

public class EdgeBeatMsg {
	private int nodeId;
	private int destination;
	private String message;

	private Header.Builder headerBuilder;

	private EdgeBeat.Builder edgeBeatBuilder;
	private CommunicationMessage.Builder commMsgBuilder;

	public EdgeBeatMsg(String message, int nodeId, int destination) {
		this.message = message;
		this.nodeId = nodeId;
		this.destination = destination; // To all nodes

		headerBuilder = Header.newBuilder();
		headerBuilder.setNodeId(this.nodeId);
		headerBuilder.setDestination(this.destination);
		headerBuilder.setTime(System.currentTimeMillis());

		edgeBeatBuilder = EdgeBeat.newBuilder();
		edgeBeatBuilder.setMsg(this.message);

		commMsgBuilder = CommunicationMessage.newBuilder();
		commMsgBuilder.setHeader(headerBuilder.build());
		commMsgBuilder.setBeat(edgeBeatBuilder.build());
	}

	public CommunicationMessage getCommunicationMessage() {
		return this.commMsgBuilder.build();
	}

}
