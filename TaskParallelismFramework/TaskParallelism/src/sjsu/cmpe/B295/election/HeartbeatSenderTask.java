package sjsu.cmpe.B295.election;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sjsu.cmpe.B295.clusterMonitoring.Cluster;
import sjsu.cmpe.B295.clusterMonitoring.PiNode;
import sjsu.cmpe.B295.clusterMonitoring.PiNodeState;
import sjsu.cmpe.B295.clusterMonitoring.PiNodeType;
import sjsu.cmpe.B295.common.CommunicationMessageProto.CommunicationMessage;
import sjsu.cmpe.B295.raspberrypi.node.NodeState;
import sjsu.cmpe.B295.raspberrypi.node.edges.EdgeInfo;

public class HeartbeatSenderTask extends TimerTask {
    protected static Logger logger = LoggerFactory
            .getLogger("HeartbeatSenderTask");
    private NodeState nodeState;
    private Leader leader;
    private ElectionUtil util;
    private Cluster cluster;
    private boolean updateUI = false;
    private StringBuffer response = new StringBuffer();
    private static final String RESPONSE_START = "{\"nodes\":[";
    private static final String RESPONSE_END = "]}";
    private String masterResponse = "";
    private StringBuffer workerResponse = new StringBuffer("");

    public HeartbeatSenderTask(Leader leader, NodeState nodeState) {
        this.nodeState = nodeState;
        this.leader = leader;
        this.cluster = leader.getCluster();
        this.util = new ElectionUtil();
    }

    @Override
    public void run() {

        if (!cluster.getPiNodes().containsKey(nodeState.getRoutingConfig().getNodeId()))
            updateUI = true;
        else if (!cluster.getPiNodes().get(nodeState.getRoutingConfig().getNodeId()).getPiNodeType().equals(PiNodeType.MASTER))
            updateUI = true;

        masterResponse = "{\"id\": \"" + nodeState.getRoutingConfig().getNodeId() + "\", \"ip\": \"" + nodeState.getRoutingConfig().getHost() + "\",\"isMaster\": \"" + PiNodeType.MASTER + "\",\"state\": \"" + PiNodeState.ACTIVE + "\"},";


        PiNode piNodeLeader = new PiNode();
        piNodeLeader.setId(nodeState.getRoutingConfig().getNodeId());
        piNodeLeader.setIpAddress(nodeState.getRoutingConfig().getHost());
        piNodeLeader.setPiNodeState(PiNodeState.ACTIVE);
        piNodeLeader.setPiNodeType(PiNodeType.MASTER);
        cluster.addPiNode(piNodeLeader);


        nodeState.getEdgeMonitor().getOutboundEdges().getEdgesMap().values()
                .stream().forEach(ei -> {
            if (ei.getChannel() != null) {
                PiNode piNode = new PiNode();
                if (ei.getChannel().isOpen()) {

                    logger.info(
                            "Node " + nodeState.getRoutingConfig().getNodeId()
                                    + " sending heartbeat to node " + ei.getRef());
                    CommunicationMessage commMsg = this.util
                            .createHeartBeat(nodeState, ei.getRef());
                    ei.getChannel().writeAndFlush(commMsg);
                }
            }
        });
        logClusterDetails();
    }

    public void logClusterDetails() {
        workerResponse.setLength(0);
        nodeState.getRoutingConfig().getRoutingEntries().stream()
                .forEach(ri -> {
                    int id = ri.getId();

                    if (!updateUI) {
                        if (!cluster.getPiNodes().containsKey(ri.getId()))
                            updateUI = true;
                        else if (!cluster.getPiNodes().get(ri.getId()).getPiNodeType().equals(PiNodeType.WORKER))
//						|| !cluster.getPiNodes().get(ri.getId()).getPiNodeState().equals(ri.get)
                            updateUI = true;
                    }

                    PiNode piNode = new PiNode();
                    piNode.setId(id);
                    piNode.setIpAddress(ri.getHost());
                    piNode.setPiNodeState(PiNodeState.INACTIVE);
                    piNode.setPiNodeType(PiNodeType.WORKER);
                    EdgeInfo ei = nodeState.getEdgeMonitor().getOutboundEdges()
                            .getEdgesMap().get(id);
                    if (ei != null) {
                        if (ei.getChannel() != null) {
                            if (ei.getChannel().isOpen()) {
                                piNode.setPiNodeState(PiNodeState.ACTIVE);
                            }
                        }
                    }
                    if (cluster.getPiNodes().containsKey(ri.getId())) {
                        if (!cluster.getPiNodes().get(ri.getId()).getPiNodeState().equals(piNode.getPiNodeState()))
                            updateUI = true;
                    }
                    cluster.addPiNode(piNode);
                    workerResponse.append("{\"id\": \"" + id + "\", \"ip\": \"" + ri.getHost() + "\",\"isMaster\": \"" + PiNodeType.WORKER +"\",\"state\": \"" + piNode.getPiNodeState()+ "\"},");

                });
        logger.info("@@@@@@@@@@@@@@ Cluster Details @@@@@@@@@@@@@@ ");
        for (Integer piNodeId : cluster.getPiNodes().keySet()) {
            PiNode piNode = cluster.getPiNodes().get(piNodeId);
            logger.info(piNode.getId() + "-" + piNode.getIpAddress() + "-"
                    + piNode.getPiNodeState() + "-" + piNode.getPiNodeType());
        }
        logger.info("@@@@@@@@@@@@@@ Cluster Details @@@@@@@@@@@@@@ ");
        if (updateUI)
            updateUI();
    }

    public void updateUI() {
        response.setLength(0);
        try {
            URL url = new URL("http://" + nodeState.getRoutingConfig().getClientIP() + ":1300/updateCluster");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            response.append(RESPONSE_START);
            response.append(masterResponse);
            response.append(workerResponse.substring(0, workerResponse.length() - 1));
            response.append(RESPONSE_END);
            logger.info("request is "+response.toString());
            out.write(response.toString());
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while (in.readLine() != null) {
            }
            in.close();
            logger.info("updateCluster invoked successfully.");
            updateUI = false;
            response.setLength(0);
            workerResponse.setLength(0);
        } catch (Exception e) {
            logger.info("Error while calling updateCluster");
            logger.info(e.getMessage());
            workerResponse.setLength(0);
        }
    }
}
