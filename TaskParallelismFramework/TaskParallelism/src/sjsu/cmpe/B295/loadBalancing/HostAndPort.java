package sjsu.cmpe.B295.loadBalancing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HostAndPort {

	// constants
	// ------------------------------------------------------------------------------------------------------

	public static final String ANY = "*";
	private static final Pattern ANY_PATTERN = Pattern
		.compile("\\*:(\\d{1,5})");
	private static final Pattern HOST_PORT_PATTERN = Pattern
		.compile("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})");

	// configuration
	// --------------------------------------------------------------------------------------------------

	private final String host;
	private final int port;

	// constructors
	// ---------------------------------------------------------------------------------------------------

	public HostAndPort(int port) {
		this.host = ANY;
		this.port = port;
	}

	public HostAndPort(String host, int port) {
		if ((port < 0) || (port > 65536)) {
			throw new IllegalArgumentException("Port must be in range 0-65536");
		}
		this.host = host;
		this.port = port;
	}

	// public static methods
	// ------------------------------------------------------------------------------------------

	public static HostAndPort decode(String string) {
		// TODO add support for ipv6 and named addresses
		Matcher m = HOST_PORT_PATTERN.matcher(string);
		if (m.find()) {
			// No need to catch potential exception since REGEX ensures this is
			// numeric.
			int port = Integer.parseInt(m.group(2));
			return new HostAndPort(m.group(1), port);
		} else {
			m = ANY_PATTERN.matcher(string);
			if (m.find()) {
				int port = Integer.parseInt(m.group(1));
				return new HostAndPort(ANY, port);
			}
		}

		return null;
	}

	// public methods
	// -------------------------------------------------------------------------------------------------

	public boolean isAnyHost() {
		return ANY.equals(this.host);
	}

	// getters & setters
	// ----------------------------------------------------------------------------------------------

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	// low level overrides
	// --------------------------------------------------------------------------------------------

	@Override
	public String toString() {
		return this.host + ':' + this.port;
	}
}
