package mjpegslicer.mockcam;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import mjpegslicer.LoggableObject;

public class Acceptor extends LoggableObject implements Runnable {
	private Camera camera;
	private boolean running;
	private ServerSocket serverSocket;
	private Future<?> future;

	/**
	 * Creates an acceptor instance.
	 * 
	 * @param camera
	 *            The camera that owns this runner.
	 */
	public Acceptor(Camera camera) throws IOException {
		debugEntering(MN_INIT);
		this.camera = camera;
		serverSocket = new ServerSocket(camera.getPort());
		debug(MN_INIT, "server socket: ", serverSocket);
		ExecutorService pool = Executors.newSingleThreadExecutor();
		debug(MN_INIT, "starting the acceptor runner...");
		future = pool.submit(this);
		debug(MN_INIT, "runner started, future: ", future);
		debugLeaving(MN_INIT);
	}

	/**
	 * Ends the feeder run.
	 */
	public void shutdown() {
		String mn = debugEntering("shutdown");
		running = false;
		future.cancel(true);
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (Throwable ignored) {
			}
		}
		serverSocket = null;
		debugLeaving(mn);
	}

	/**
	 * The runner implementation.
	 */
	@Override
	public void run() {
		String mn = debugEntering("run");
		running = true;
		try {
			while (running) {
				debug(mn, "waiting for connection request...");
				Socket socket = serverSocket.accept();
				debug(mn, "connection request: ", socket);
				Handler handler = new Handler(camera, socket);
				debug(mn, "handler created: ", handler);
			}
		} catch (IOException ioe) {
			debug(mn, "IO exception: ", ioe);
			shutdown();
		}
		debugLeaving(mn);
	}
}