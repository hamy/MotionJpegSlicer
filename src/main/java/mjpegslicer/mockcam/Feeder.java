package mjpegslicer.mockcam;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

public interface Feeder {

	/**
	 * Returns the content provider.
	 * 
	 * @return The provider or <code>null</code> if none was specified.
	 */
	public ContentProvider getContentProvider();

	/**
	 * Redefines the content provider.
	 * 
	 * @param contentProvider
	 *            The new provider or <code>null</code> if none was specified.
	 */
	public void setContentProvider(ContentProvider contentProvider);

	/**
	 * Returns the output stream.
	 * 
	 * @return The stream.
	 */
	public OutputStream getOutputStream();

	/**
	 * Redefines the output stream.
	 * 
	 * @param outputStream
	 *            The new stream.
	 */
	public void setOutputStream(OutputStream outputStream);

	/**
	 * The minimum rate in frames per second.
	 */
	public static int MIN_RATE = 1;

	/**
	 * The maximum rate in frames per second.
	 */
	public static int MAX_RATE = 30;

	/**
	 * The default rate in frames per second.
	 */
	public static int DEFAULT_RATE = 10;

	/**
	 * Returns the rate.
	 * 
	 * @return The rate in frames per second.
	 */
	public int getRate();

	/**
	 * Redefines the rate.
	 * 
	 * @param rate
	 *            The new rate in frames per second.
	 */
	public void setRate(int rate);

	/**
	 * Returns the number of milliseconds per frame.
	 * 
	 * @return The duration.
	 */
	public long getMsecPerFrame();

	/**
	 * The default image width.
	 */
	public static int DEFAULT_WIDTH = 640;

	/**
	 * Returns the image width.
	 * 
	 * @return The width.
	 */
	public int getWidth();

	/**
	 * Redefines the image width.
	 * 
	 * @param width
	 *            The new width.
	 */
	public void setWidth(int width);

	/**
	 * The default image height.
	 */
	public static int DEFAULT_HEIGHT = 480;

	/**
	 * Returns the image height.
	 * 
	 * @return The height.
	 */
	public int getHeight();

	/**
	 * Redefines the image height.
	 * 
	 * @param height
	 *            The new height.
	 */
	public void setHeight(int height);

	/**
	 * Creates an empty image that may receive content by a content provider.
	 * 
	 * @return The newly created image.
	 */
	public BufferedImage createImage();

	/**
	 * Returns the current frame sequence number.
	 * 
	 * @return The number.
	 */
	public int getFrameSequenceNumber();

	/**
	 * The default number of frames to be generated.
	 */
	public static final int DEFAULT_MAX_FRAME_SEQUENCE_NUMBER = 10;

	/**
	 * Indicates an unlimited number of frames.
	 */
	public static final int UNLIMITED_MAX_FRAME_SEQUENCE_NUMBER = 0;

	/**
	 * Returns the maximum number of frames to be generated.
	 * 
	 * @return The number.
	 */
	public int getMaxFrameSequenceNumber();

	/**
	 * Redefines the maximum number of frames to be generated.
	 * 
	 * @param maxFrameSequenceNumber
	 *            The new number. A value of
	 *            {@link #UNLIMITED_MAX_FRAME_SEQUENCE_NUMBER} means no limit.
	 */
	public void setMaxFrameSequenceNumber(int maxFrameSequenceNumber);

	/**
	 * Increments the sequence number.
	 */
	public void incrFrameSequenceNumber();

	/**
	 * Indicates whether the frame output has started.
	 * 
	 * @return The flag.
	 */
	public boolean isStarted();

	/**
	 * Starts the feed.
	 */
	public void start();

	/**
	 * Stops the feed.
	 */
	public void stop();

	/**
	 * Prepares the next frame.
	 */
	public void prepareFrame();

	/**
	 * Flushes the frame to the output stream.
	 */
	public void flushFrame();
}