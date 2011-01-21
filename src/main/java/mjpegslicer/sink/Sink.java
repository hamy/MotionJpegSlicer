package mjpegslicer.sink;

import java.io.InputStream;

public interface Sink {

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
	 * @param inputStream
	 *            The {@link InputStream} that is used for streaming.
	 */
	public void startStream(InputStream inputStream);

	/**
	 * Stops the streaming.
	 */
	public void stopStream();

	/**
	 * Extracts the next HTTP header from the stream.
	 * 
	 * @return The header lines without the end of line characters or
	 *         <code>null</code> if the end of data is reached.
	 */
	public String[] readHttpHeader();

	/**
	 * Returns the number of HTTP headers read so far.
	 * 
	 * @return The header count.
	 */
	public int getHttpHeaderCount();

	/**
	 * Extracts the next image data chunk from the stream.
	 * 
	 * @param contentLength
	 *            The content length.
	 * @return The data or <code>null</code> if the end of data is reached.
	 */
	public byte[] readImageData(int contentLength);

	/**
	 * Returns the number of images read so far.
	 * 
	 * @return The image count.
	 */
	public int getImageCount();
}
