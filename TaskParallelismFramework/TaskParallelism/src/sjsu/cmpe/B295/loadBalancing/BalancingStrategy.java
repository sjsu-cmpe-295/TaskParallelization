package sjsu.cmpe.B295.loadBalancing;

import java.util.List;

public interface BalancingStrategy {

	HostAndPort selectTarget(String originHost, int originPort);

	List<HostAndPort> getTargetAddresses();
}