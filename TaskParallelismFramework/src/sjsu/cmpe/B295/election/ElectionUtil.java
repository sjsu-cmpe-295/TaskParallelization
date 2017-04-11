package sjsu.cmpe.B295.election;

import sjsu.cmpe.B295.common.CommonProto.Header;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.common.ElectionProto.Election;
import sjsu.cmpe.B295.common.ElectionProto.Election.ElectionQuery;
import sjsu.cmpe.B295.common.ElectionProto.Election.LeaderState;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class ElectionUtil {

	public CommunicationMessage createHeartBeat(NodeState nodeState,
		int destination) {
		CommunicationMessage.Builder commMsg = CommunicationMessage
			.newBuilder();
		Header.Builder header = createHeader(
			nodeState.getRoutingConfig().getNodeId(), destination);
		Election.Builder electionMsg = Election.newBuilder();
		electionMsg.setAction(ElectionQuery.BEAT);
		electionMsg.setElectionId(nodeState.getElectionId());
		electionMsg.setLeaderId(nodeState.getRoutingConfig().getNodeId());

		commMsg.setHeader(header);
		commMsg.setElectionMessage(electionMsg);

		return commMsg.build();
	}

	public CommunicationMessage createVoteRequest(NodeState nodeState,
		int electionId) {
		CommunicationMessage.Builder commMsg = CommunicationMessage
			.newBuilder();
		Header.Builder header = createHeader(
			nodeState.getRoutingConfig().getNodeId(), -1);

		Election.Builder electionMsg = Election.newBuilder();
		electionMsg.setAction(ElectionQuery.VOTEREQUEST);
		electionMsg.setElectionId(electionId);
		electionMsg.setLeaderId(nodeState.getRoutingConfig().getNodeId());

		commMsg.setHeader(header);
		commMsg.setElectionMessage(electionMsg);

		return commMsg.build();
	}

	public CommunicationMessage createLeaderIsMessage(NodeState nodeState) {

		CommunicationMessage.Builder commMsg = CommunicationMessage
			.newBuilder();
		Header.Builder header = createHeader(
			nodeState.getRoutingConfig().getNodeId(), -1);

		Election.Builder electionMsg = Election.newBuilder();
		electionMsg.setAction(ElectionQuery.THELEADERIS);
		electionMsg.setElectionId(nodeState.getElectionId());
		electionMsg.setLeaderId(nodeState.getRoutingConfig().getNodeId());
		electionMsg.setState(LeaderState.LEADERALIVE);
		commMsg.setHeader(header);
		commMsg.setElectionMessage(electionMsg);

		return commMsg.build();
	}

	public CommunicationMessage createWhoIsTheLeaderMessage(NodeState nodeState,
		int destination) {
		CommunicationMessage.Builder commMsg = CommunicationMessage
			.newBuilder();
		Header.Builder header = createHeader(
			nodeState.getRoutingConfig().getNodeId(), destination);

		Election.Builder electionMsg = Election.newBuilder();
		electionMsg.setAction(ElectionQuery.WHOISTHELEADER);

		commMsg.setHeader(header);
		commMsg.setElectionMessage(electionMsg);
		return commMsg.build();
	}

	/**
	 * @param nodeId
	 * @param destination
	 * @param maxHops
	 * @return
	 */
	private Header.Builder createHeader(int nodeId, int destination) {
		Header.Builder header = Header.newBuilder();
		header.setNodeId(nodeId);
		header.setDestination(destination);
		header.setTime(System.currentTimeMillis());
		return header;
	}
}
