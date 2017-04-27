package sjsu.cmpe.B295.loadBalancing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConcreteLoadBalancer {
	public static void main(String[] args) {

		List<HostAndPort> destinations = new ArrayList<HostAndPort>();
		// destinations.addAll(Arrays.asList(new HostAndPort("127.0.0.1", 5568),
		// new HostAndPort("127.0.0.1", 6568),
		// new HostAndPort("127.0.0.1", 7568)));

		RoundRobinBalancingStrategy strategy = new RoundRobinBalancingStrategy(
			destinations);
		final DefaultLoadBalancer loadBalancer = new DefaultLoadBalancer(
			"defaultLoadBalancer", new HostAndPort("127.0.0.1", 4468),
			strategy);

		if (!loadBalancer.init()) {
			System.err.println("Failed to launch LoadBalancer with options: ");
			return;
		}

		Thread shutdownHook = new Thread() {

			@Override
			public void run() {
				loadBalancer.terminate();
			}
		};
		Runtime.getRuntime().addShutdownHook(shutdownHook);
	}
}
