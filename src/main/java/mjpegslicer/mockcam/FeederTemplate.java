package mjpegslicer.mockcam;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import mjpegslicer.ImageDataCorruptionException;
import mjpegslicer.util.LoggableObject;
import mjpegslicer.util.Validator;

public class FeederTemplate extends LoggableObject implements Feeder {

	private ContentProvider contentProvider;

	/**
	 * Returns the content provider.
	 * 
	 * @return The provider or <code>null</code> if none was specified.
	 */
	@Override
	public ContentProvider getContentProvider() {
		return contentProvider;
	}

	/**
	 * Redefines the content provider.
	 * 
	 * @param contentProvider
	 *            The new provider or <code>null</code> if none was specified.
	 */
	@Override
	public void setContentProvider(ContentProvider contentProvider) {
		String mn = debugEntering("setContentProvider", "value: ",
				contentProvider);
		this.contentProvider = contentProvider;
		debugLeaving(mn);
	}

	private OutputStream outputStream;

	/**
	 * Returns the output stream.
	 * 
	 * @return The stream.
	 */
	@Override
	public OutputStream getOutputStream() {
		return outputStream;
	}

	/**
	 * Redefines the output stream.
	 * 
	 * @param outputStream
	 *            The new stream.
	 */
	@Override
	public void setOutputStream(OutputStream outputStream) {
		String mn = debugEntering("setOutputStream");
		Validator.argumentMustNotBeNull(mn, "outputStream", outputStream);
		this.outputStream = outputStream;
		debugLeaving(mn);
	}

	private void writeToOutputStream(String data) throws IOException {
		outputStream.write(data.getBytes());
	}

	private void writeToOutputStream(byte[] data) throws IOException {
		outputStream.write(data);
	}

	private void writeToOutputStream(int ord) throws IOException {
		outputStream.write(ord);
	}

	private void lineToOutputStream(String line) throws IOException {
		writeToOutputStream(line);
		lineToOutputStream();
	}

	private void lineToOutputStream() throws IOException {
		writeToOutputStream(CR);
		writeToOutputStream(LF);
	}

	private int rate = DEFAULT_RATE;

	/**
	 * Returns the rate.
	 * 
	 * @return The rate in frames per second.
	 */
	@Override
	public int getRate() {
		return rate;
	}

	/**
	 * Redefines the rate.
	 * 
	 * @param rate
	 *            The new rate in frames per second.
	 */
	@Override
	public void setRate(int rate) {
		String mn = debugEntering("setRate", "value: ", rate);
		Validator.checkArgument(mn, "rate", rate, rate >= MIN_RATE
				&& rate <= MAX_RATE, "not in range ", MIN_RATE, "..", MAX_RATE);
		this.rate = rate;
		msecPerFrame = rateToMsecPerFrame(rate);
		debugLeaving(mn, "msec per frame: ", msecPerFrame);
	}

	private static long rateToMsecPerFrame(int rate) {
		return Math.round(1000.0 / rate);
	}

	private long msecPerFrame = rateToMsecPerFrame(DEFAULT_RATE);

	/**
	 * Returns the number of milliseconds per frame.
	 * 
	 * @return The duration.
	 */
	@Override
	public long getMsecPerFrame() {
		return msecPerFrame;
	}

	private int width = DEFAULT_WIDTH;

	/**
	 * Returns the image width.
	 * 
	 * @return The width.
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * Redefines the image width.
	 * 
	 * @param width
	 *            The new width.
	 */
	@Override
	public void setWidth(int width) {
		String mn = debugEntering("setWidth", "value: ", width);
		Validator.checkArgument(mn, "width", width, width >= 0,
				"must be positive.");
		this.width = width;
		debugLeaving(mn);
	}

	private int height = DEFAULT_HEIGHT;

	/**
	 * Returns the image height.
	 * 
	 * @return The height.
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/**
	 * Redefines the image height.
	 * 
	 * @param height
	 *            The new height.
	 */
	@Override
	public void setHeight(int height) {
		String mn = debugEntering("setHeight", "value: ", height);
		Validator.checkArgument(mn, "height", height, height >= 0,
				"must be positive.");
		this.height = height;
		debugLeaving(mn);
	}

	/**
	 * Creates an empty image that may receive content by a content provider.
	 * 
	 * @return The newly created image.
	 */
	@Override
	public BufferedImage createImage() {
		String mn = debugEntering("createdImage");
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2d = image.createGraphics();
		g2d.setPaint(Color.GRAY);
		g2d.fillRect(0, 0, width, height);
		debugLeaving(mn, "image: ", image);
		return image;
	}

	private AtomicInteger frameSequenceNumber = new AtomicInteger();

	/**
	 * Returns the current frame sequence number.
	 * 
	 * @return The number.
	 */
	@Override
	public int getFrameSequenceNumber() {
		return frameSequenceNumber.get();
	}

	private int maxFrameSequenceNumber = DEFAULT_MAX_FRAME_SEQUENCE_NUMBER;

	/**
	 * Returns the maximum number of frames to be generated.
	 * 
	 * @return The number.
	 */
	@Override
	public int getMaxFrameSequenceNumber() {
		return maxFrameSequenceNumber;
	}

	/**
	 * Redefines the maximum number of frames to be generated.
	 * 
	 * @param maxFrameSequenceNumber
	 *            The new number. A value of
	 *            {@link #UNLIMITED_MAX_FRAME_SEQUENCE_NUMBER} means no limit.
	 */
	@Override
	public void setMaxFrameSequenceNumber(int maxFrameSequenceNumber) {
		String mn = debugEntering("setMaxFrameSequenceNumber", "value: ",
				maxFrameSequenceNumber);
		Validator.checkArgument(mn, "maxFrameSequenceNumber",
				maxFrameSequenceNumber, maxFrameSequenceNumber >= 0,
				"value must not be negative.");
		this.maxFrameSequenceNumber = maxFrameSequenceNumber;
		debugLeaving(mn);
	}

	/**
	 * Increments the sequence number.
	 */
	@Override
	public void incrFrameSequenceNumber() {
		String mn = debugEntering("incrFrameSequenceNumber");
		int seqNo = frameSequenceNumber.incrementAndGet();
		debug(mn, "new frame sequence number: ", seqNo);
		if (maxFrameSequenceNumber != UNLIMITED_MAX_FRAME_SEQUENCE_NUMBER
				&& seqNo > maxFrameSequenceNumber) {
			stop();
		}
		debugLeaving(mn);
	}

	private boolean started = false;

	/**
	 * Indicates whether the frame output has started.
	 * 
	 * @return The flag.
	 */
	@Override
	public boolean isStarted() {
		return started;
	}

	private final ExecutorService pool = Executors.newSingleThreadExecutor();
	private Future<?> future;
	private FeederRunner feederRunner;

	/**
	 * Starts the feed.
	 */
	@Override
	public void start() {
		String mn = debugEntering("start");
		Validator.checkState(mn, outputStream != null,
				"Output stream was not defined.");
		frameSequenceNumber.set(0);
		started = true;
		flushHttpHeader();
		feederRunner = new FeederRunner(this);
		future = pool.submit(feederRunner);
		debugLeaving(mn);
	}

	/**
	 * Stops the feed.
	 */
	@Override
	public void stop() {
		String mn = debugEntering("stop");
		started = false;
		feederRunner.shutdown();
		future.cancel(true);
		debugLeaving(mn);
	}

	/**
	 * Prepares the next frame.
	 */
	@Override
	public void prepareFrame() {
		String mn = debugEntering("prepareFrame");
		BufferedImage image = createImage();
		if (contentProvider != null) {
			contentProvider.traverse(image);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "JPG", baos);
			jpegData = baos.toByteArray();
			baos.close();
		} catch (Exception ex) {
			throw new ImageDataCorruptionException(
					"Failed to create JPG image: ", ex);
		}
		debugLeaving(mn);
	}

	private byte[] jpegData;
	private static final int CR = 0x0a;
	private static final int LF = 0x0d;

	/**
	 * Flushes the frame to the output stream.
	 */
	@Override
	public void flushFrame() {
		String mn = debugEntering("flushFrame");
		try {
			lineToOutputStream("--myboundary");
			lineToOutputStream("Content-Type: image/jpeg");
			lineToOutputStream("Content-Length: " + jpegData.length);
			lineToOutputStream();
			writeToOutputStream(jpegData);
			lineToOutputStream();
			outputStream.flush();
		} catch (IOException ioe) {
			error(mn, "Oops: ", ioe);
		}
		debugLeaving(mn);
	}

	private void flushHttpHeader() {
		String mn = debugEntering("flushHttpHeader");
		try {
			lineToOutputStream("HTTP/1.1 200 OK");
			lineToOutputStream("Server: MotionJpegSlicer-MockCam");
			lineToOutputStream("Pragma: no-cache");
			lineToOutputStream("Cache-Control: no-cache");
			lineToOutputStream("Content-Type: multipart/x-mixed-replace;boundary=myboundary");
			lineToOutputStream();
			outputStream.flush();
		} catch (IOException ioe) {
			error(mn, "Oops: ", ioe);
		}
		debugLeaving(mn);
	}

}
