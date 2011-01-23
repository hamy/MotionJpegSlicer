package mjpegslicer.mockcam;

import java.io.IOException;

import mjpegslicer.util.LoggableObject;
import mjpegslicer.util.Validator;

public class Camera extends LoggableObject {

	private FeederFactory feederFactory;

	/**
	 * Returns the feeder factory.
	 * 
	 * @return The factory.
	 */
	public FeederFactory getFeederFactory() {
		return feederFactory;
	}

	/**
	 * Redefines the feeder factory.
	 * 
	 * @param feederFactory
	 *            The new feeder factory.
	 */
	public void setFeederFactory(FeederFactory feederFactory) {
		String mn = debugEntering("setFeederFactory", "value: ", feederFactory);
		Validator.argumentMustNotBeNull(mn, "feederFactory", feederFactory);
		this.feederFactory = feederFactory;
		debugLeaving(mn);
	}

	/**
	 * The default port.
	 */
	public static final int DEFAULT_PORT = 18090;

	/**
	 * The minimum port.
	 */
	public static final int MIN_PORT = 1025;

	/**
	 * The maximum port.
	 */
	public static final int MAX_PORT = 1025;

	private int port = DEFAULT_PORT;

	/**
	 * Returns the port.
	 * 
	 * @return The port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Redefines the port.
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		String mn = debugEntering("setPort", "value: ", port);
		Validator.checkArgument(mn, "port", port, port >= MIN_PORT
				&& port <= MAX_PORT, "not in ", MIN_PORT, "..", MAX_PORT);
		this.port = port;
		debugLeaving(mn);
	}

	private Acceptor acceptor;
	private boolean started = false;

	public boolean isStarted() {
		return started;
	}

	public void start() throws IOException {
		String mn = debugEntering("start");
		started = true;
		acceptor = new Acceptor(this);
		debugLeaving(mn);
	}

	public void stop() throws IOException {
		String mn = debugEntering("stop");
		acceptor.shutdown();
		started = false;
		debugLeaving(mn);
	}
}
