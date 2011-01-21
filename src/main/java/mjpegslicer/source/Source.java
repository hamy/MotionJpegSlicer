package mjpegslicer.source;

import java.io.InputStream;

public interface Source {

	/**
	 * The default host name.
	 */
	public static final String DEFAULT_HOST_NAME = "127.0.0.1";

	/**
	 * Returns the host name.
	 * 
	 * @return The host name
	 */
	public String getHostName();

	/**
	 * Redefines the host name.
	 * 
	 * @param hostName
	 *            The new host name.
	 */
	public void setHostName(String hostName);

	/**
	 * The default port.
	 */
	public static final int DEFAULT_PORT = 80;

	/**
	 * Returns the port.
	 * 
	 * @return The port.
	 */
	public int getPort();

	/**
	 * Redefines the port.
	 * 
	 * @param port
	 *            The new port.
	 */
	public void setPort(int port);

	/**
	 * The default path.
	 */
	public static final String DEFAULT_PATH = "/mjpg/video.mjpg";

	/**
	 * Returns the path for the generation of M-JPEG streams.
	 * 
	 * @return The path.
	 */
	public String getPath();

	/**
	 * Redefines the path.
	 * 
	 * @param path
	 *            The new path.
	 */
	public void setPath(String path);

	/**
	 * The default user name.
	 */
	public static final String DEFAULT_USER_NAME = "guest";

	/**
	 * Returns the user name that is used for authentication.
	 * 
	 * @return The user name.
	 */
	public String getUserName();

	/**
	 * Redefines the user name.
	 * 
	 * @param userName
	 *            The new user name.
	 */
	public void setUserName(String userName);

	/**
	 * The default password.
	 */
	public static final String DEFAULT_PASSWORD = "guest";

	/**
	 * Returns the password that is used for authentication.
	 * 
	 * @return The password.
	 */
	public String getPassword();

	/**
	 * Redefines the password.
	 * 
	 * @param password
	 *            The new password.
	 */
	public void setPassword(String password);

	/**
	 * Indicates whether the streaming was started.
	 * 
	 * @return <code>true</code> if the streaming was started, otherwise
	 *         <code>false</code>
	 */
	public boolean isStarted();

	/**
	 * Returns the number of bytes transferred so far.
	 * 
	 * @return The byte count.
	 */
	public long getByteCount();

	/**
	 * Starts the streaming.
	 * 
	 * @return The {@link InputStream} that is used for streaming.
	 */
	public InputStream startStream();

	/**
	 * Stops the streaming.
	 */
	public void stopStream();
}
