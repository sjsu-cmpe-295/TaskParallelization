package sjsu.cmpe.B295.loadBalancing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import sjsu.cmpe.B295.raspberrypi.node.NodeState;

public class RoundRobinBalancingStrategy implements BalancingStrategy {

	// configuration
	// --------------------------------------------------------------------------------------------------

	private List<HostAndPort> targets;
	private AtomicInteger currentTarget;
	private NodeState nodeState;

	// constructors
	// ---------------------------------------
	// ------------------------------------------------------------
	public RoundRobinBalancingStrategy(NodeState nodeState,
		List<HostAndPort> targets) {
		this.targets = new ArrayList<>();
		this.currentTarget = new AtomicInteger(-1);
		this.nodeState = nodeState;
		seTargetAddresses(targets);
	}

	public RoundRobinBalancingStrategy(List<HostAndPort> targets) {
		if ((targets == null) || targets.isEmpty()) {
			throw new IllegalArgumentException(
				"Target list cannot be null or empty");
		}
		this.targets = new CopyOnWriteArrayList<HostAndPort>(targets);
		this.currentTarget = new AtomicInteger(0);
	}

	// BalancingStrategy
	// ----------------------------------------------------------------------------------------------

	@Override
	public HostAndPort selectTarget(String originHost, int originPort) {
		int currentTarget;
		synchronized (this.currentTarget) {
			if (this.currentTarget.get() != -1) {
				currentTarget = this.currentTarget.getAndIncrement();
				if (currentTarget >= this.targets.size()) {
					currentTarget = 0;
					this.currentTarget.set(0);
				}
			} else {
				return new HostAndPort(
					this.nodeState.getRoutingConfig().getHost(),
					this.nodeState.getRoutingConfig().getCommandPort());
			}
		}

		return this.targets.get(currentTarget);
	}

	@Override
	public List<HostAndPort> getTargetAddresses() {
		return this.targets;
	}

	public void seTargetAddresses(List<HostAndPort> targets) {
		this.targets = new CopyOnWriteArrayList<HostAndPort>(targets);
		if (targets.size() > 0) {
			this.currentTarget.getAndIncrement();
		} else {
			this.currentTarget = new AtomicInteger(-1);
		}

	}

	public void addTargetAddress(HostAndPort target) {
		this.targets.add(target);
	}

	public void removeTargetAddress(HostAndPort target) {
		this.targets.remove(target);
	}
}
