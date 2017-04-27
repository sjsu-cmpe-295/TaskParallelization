package sjsu.cmpe.B295.loadBalancing;

import java.util.List;

public interface LoadBalancer {
	boolean init();

	void terminate();

	HostAndPort getBalancerAddress();

	List<HostAndPort> getTargetAddresses();

	BalancingStrategy getBalancingStrategy();
}
