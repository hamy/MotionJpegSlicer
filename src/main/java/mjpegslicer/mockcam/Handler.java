package mjpegslicer.mockcam;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.Socket;

import mjpegslicer.LoggableObject;

public class Handler extends LoggableObject {
	private Feeder feeder;
	private Socket socket;
	private LineNumberReader reader;
	private OutputStream outputStream;

	/**
	 * Creates a handler instance.
	 * 
	 * @param camera
	 *            The camera that owns this runner.
	 * @param socket
	 *            The socket
	 */
	public Handler(Camera camera, Socket socket) throws IOException {
		debugEntering(MN_INIT);
		FeederFactory feederFactory = camera.getFeederFactory();
		feeder = feederFactory.newFeeder();
		wireStreams(socket);
		readHttpRequestHeader();
		feeder.start();
		debugLeaving(MN_INIT);
	}

	private void wireStreams(Socket socket) throws IOException {
		String mn = debugEntering("wireStreams");
		this.socket = socket;
		reader = new LineNumberReader(new InputStreamReader(
				socket.getInputStream()));
		outputStream = socket.getOutputStream();
		feeder.setOutputStream(outputStream);
		debugLeaving(mn);
	}

	private void readHttpRequestHeader() throws IOException {
		String mn = debugEntering("readHttpRequestHeader");
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				warn(mn, "EOF in socket input stream.");
				break;
			}
			debug(mn, "line: ", line);
			if (line.equals("")) {
				debug(mn, "header end line found.");
				break;
			}
		}
		debugLeaving(mn);
	}

	/**
	 * Ends the handler run.
	 */
	public void shutdown() {
		String mn = debugEntering("shutdown");
		if (feeder != null) {
			if (feeder.isStarted()) {
				feeder.stop();
			}
		}
		cleanUpSocketConnection();
		debugLeaving(mn);
	}

	private void cleanUpSocketConnection() {
		String mn = debugEntering("cleanUpSocketConnection");
		if (reader != null) {
			try {
				reader.close();
			} catch (Throwable ignored) {
			}
			reader = null;
		}
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (Throwable ignored) {
			}
			outputStream = null;
		}
		if (socket != null) {
			try {
				socket.close();
			} catch (Throwable ignored) {
			}
			socket = null;
		}
		debugLeaving(mn);
	}
}