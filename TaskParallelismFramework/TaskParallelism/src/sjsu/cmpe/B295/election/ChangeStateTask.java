package sjsu.cmpe.B295.election;

import java.util.TimerTask;

import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class ChangeStateTask extends TimerTask {
	NodeState nodeState;

	public ChangeStateTask(NodeState nodeState) {
		this.nodeState = nodeState;
	}

	@Override
	public void run() {
		this.nodeState.setElectionNodeState(ElectionNodeStates.CANDIDATE);
	}

}
