package mjpegslicer.impl;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.atomic.AtomicLong;

import mjpegslicer.Source;
import mjpegslicer.util.LoggableObject;
import mjpegslicer.util.Validator;

import sun.misc.BASE64Encoder;

public class SourceTemplate extends LoggableObject implements Source {

	private String hostName = DEFAULT_HOST_NAME;

	/**
	 * Returns the host name.
	 * 
	 * @return The host name
	 */
	@Override
	public String getHostName() {
		return hostName;
	}

	/**
	 * Redefines the host name.
	 * 
	 * @param hostName
	 *            The new host name.
	 */
	@Override
	public void setHostName(String hostName) {
		String mn = debugEntering("setHostName", "value: ", hostName);
		hostName = Validator.trimmedArgumentMustNotBeEmpty(mn, "hostName",
				hostName);
		this.hostName = hostName;
		debugLeaving(mn);
	}

	private int port = DEFAULT_PORT;

	/**
	 * Returns the port.
	 * 
	 * @return The port.
	 */
	@Override
	public int getPort() {
		return port;
	}

	/**
	 * Redefines the port.
	 * 
	 * @param port
	 *            The new port.
	 */
	@Override
	public void setPort(int port) {
		String mn = debugEntering("setPort", "value: ", port);
		Validator.checkArgument(mn, "port", port, port > 0 && port < 65536,
				"0 < port < 65536");
		this.port = port;
		debugLeaving(mn);
	}

	private String path = DEFAULT_PATH;

	/**
	 * Returns the path for the generation of M-JPEG streams.
	 * 
	 * @return The path.
	 */
	@Override
	public String getPath() {
		return path;
	}

	/**
	 * Redefines the path.
	 * 
	 * @param path
	 *            The new path.
	 */
	@Override
	public void setPath(String path) {
		String mn = debugEntering("setPath", "value: ", path);
		path = Validator.trimmedArgumentMustNotBeEmpty(mn, "path", path);
		this.path = path;
		debugLeaving(mn);
	}

	private String userName = DEFAULT_USER_NAME;

	/**
	 * Returns the user name that is used for authentication.
	 * 
	 * @return The user name.
	 */
	@Override
	public String getUserName() {
		return userName;
	}

	/**
	 * Redefines the user name.
	 * 
	 * @param userName
	 *            The new user name.
	 */
	@Override
	public void setUserName(String userName) {
		String mn = debugEntering("setUserName", "value: ", userName);
		this.userName = userName;
		debugLeaving(mn);
	}

	private String password = DEFAULT_PASSWORD;

	/**
	 * Returns the password that is used for authentication.
	 * 
	 * @return The password.
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * Redefines the password.
	 * 
	 * @param password
	 *            The new password.
	 */
	@Override
	public void setPassword(String password) {
		String mn = debugEntering("setPassword", "value: ", password);
		this.password = password;
		debugLeaving(mn);
	}

	private boolean started = false;

	/**
	 * Indicates whether the streaming was started.
	 * 
	 * @return <code>true</code> if the streaming was started, otherwise
	 *         <code>false</code>
	 */
	@Override
	public boolean isStarted() {
		return started;
	}

	private AtomicLong byteCount = new AtomicLong();

	/**
	 * Returns the number of bytes transferred so far.
	 * 
	 * @return The byte count.
	 */
	@Override
	public long getByteCount() {
		return byteCount.get();
	}

	protected InputStream inputStream = null;

	/**
	 * Starts the streaming.
	 * 
	 * @return The {@link InputStream} that is used for streaming.
	 */
	@Override
	public InputStream startStream() {
		String mn = debugEntering("startStream");
		Validator.checkState(mn, !started, "stream was already started.");
		inputStream = null;
		byteCount.set(0);
		try {
			inputStream = openInputStream(hostName, port, path, userName,
					password);
			started = true;
		} catch (Exception ex) {
			cleanUp();
			started = false;
			throw new RuntimeException("Failed to start input stream.", ex);
		}
		debugLeaving(mn);
		return inputStream;
	}

	/**
	 * Template method that opens the input stream from the camera. Sub-classes
	 * might want to override this method.
	 * 
	 * @param hostName
	 * @param port
	 * @param path
	 * @param userName
	 * @param password
	 * @return The stream.
	 * @throws Exception
	 *             If any error occured.
	 */
	protected InputStream openInputStream(String hostName, int port,
			String path, String userName, String password) throws Exception {
		String mn = debugEntering("openInputStream");
		URL url = prepareURL(hostName, port, path, userName, password);
		InputStream is = null;
		if (usesAuthentication(userName, password)) {
			is = openAuthenticatedConnection(url, userName, password);
		} else {
			is = openUnauthenticatedConnection(url);
		}
		is = optimizeInputStream(is);
		debugLeaving(mn, "stream: ", is);
		return is;
	}

	/**
	 * Template method that indicates whether authentication is used.
	 * Sub-classes might want to override this method.
	 * 
	 * @param userName
	 * @param password
	 * @return A flag indicating authentication is used or not.
	 * @throws Exception
	 *             If any error occured.
	 */
	protected boolean usesAuthentication(String userName, String password)
			throws Exception {
		String mn = debugEntering("usesAuthentication");
		boolean result = userName != null && password != null;
		debugLeaving(mn, "result: ", result);
		return result;
	}

	/**
	 * Template method that builds the URL for accessing the camera. Sub-classes
	 * might want to override this method.
	 * 
	 * @param hostName
	 * @param port
	 * @param path
	 * @param userName
	 * @param password
	 * @return
	 * @throws Exception
	 *             If any error occured.
	 */
	protected URL prepareURL(String hostName, int port, String path,
			String userName, String password) throws Exception {
		String mn = debugEntering("prepareURL");
		URL url = new URL("http", hostName, port, path);
		debugLeaving(mn, "URL: ", url);
		return url;
	}

	/**
	 * Template method that opens a connection with authentication. Sub-classes
	 * might want to override this method.
	 * 
	 * @param url
	 * @param userName
	 * @param password
	 * @return The stream.
	 * @throws Exception
	 *             If any error occured.
	 */
	protected InputStream openAuthenticatedConnection(URL url, String userName,
			String password) throws Exception {
		String mn = debugEntering("openAuthenticatedConnection");
		URLConnection uc = url.openConnection();
		debug(mn, "url con: ", uc);
		BASE64Encoder encoder = new BASE64Encoder();
		byte[] credentials = (userName + ':' + password).getBytes();
		String encoded = encoder.encode(credentials);
		debug(mn, "encoded credentials: ", encoded);
		uc.setRequestProperty("Authorization", "Basic " + encoded);
		debug(mn, "url con: ", uc);
		InputStream result = uc.getInputStream();
		debugLeaving(mn, "result: ", result);
		return result;
	}

	/**
	 * Template method that opens a connection without authentication.
	 * Sub-classes might want to override this method.
	 * 
	 * @param url
	 * @return The stream.
	 * @throws Exception
	 *             If any error occured.
	 */
	protected InputStream openUnauthenticatedConnection(URL url)
			throws Exception {
		String mn = debugEntering("openUauthenticatedConnection");
		InputStream result = url.openStream();
		debugLeaving(mn, "result: ", result);
		return result;
	}

	/**
	 * Template method that optimizes the stream from the camera. Sub-classes
	 * might want to override this method.
	 * 
	 * @param is
	 *            The unoptimized stream.
	 * @return The optimized stream.
	 * @throws Exception
	 *             If any error occured.
	 */
	protected InputStream optimizeInputStream(InputStream is) throws Exception {
		String mn = debugEntering("optimizeInputStream");
		InputStream result = new BufferedInputStream(is, 8192);
		debugLeaving(mn, "result: ", result);
		return result;
	}

	/**
	 * Template method that performs a clean-up after closing the stream or
	 * after an error. Sub-classes might want to override this method.
	 */
	protected void cleanUp() {
		String mn = debugEntering("cleanUp");
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Throwable ignored) {
			} finally {
				inputStream = null;
			}
		}
		debugLeaving(mn);
	}

	/**
	 * Stops the streaming.
	 */
	@Override
	public void stopStream() {
		String mn = debugEntering("stopStream");
		Validator.checkState(mn, started, "stream was not started.");
		cleanUp();
		started = false;
		debugLeaving(mn);
	}
}
