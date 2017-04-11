package sjsu.cmpe.B295.clusterMonitoring;

public class PiNode {

	public PiNode() {

	}

	private Integer id;
	private String ipAddress;
	private PiNodeType piNodeType;
	private PiNodeState piNodeState;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public PiNodeType getPiNodeType() {
		return piNodeType;
	}

	public PiNodeState getPiNodeState() {
		return piNodeState;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setPiNodeType(PiNodeType piNodeType) {
		this.piNodeType = piNodeType;
	}

	public void setPiNodeState(PiNodeState piNodeState) {
		this.piNodeState = piNodeState;
	}
}
