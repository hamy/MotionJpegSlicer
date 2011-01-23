package mjpegslicer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.EventObject;

import javax.imageio.ImageIO;


/**
 * This class describes a JPEG image that was sliced from the M-JPEG stream.
 */
public class ImageEvent extends EventObject {

	private static final long serialVersionUID = -1L;
	
	private long creationMillis;

	/**
	 * Returns the event creation timestamp.
	 * 
	 * @return The timestamp as number of milliseconds since 1970/1/1 0:0 UTC.
	 */
	public long getCreationMillis() {
		return creationMillis;
	}

	/**
	 * Returns the event creation timestamp.
	 * 
	 * @return The timestamp as {@link Date} instance.
	 */
	public Date getCreationDate() {
		return new Date(creationMillis);
	}

	private int sequenceNumber;

	/**
	 * Returns the image sequence number.
	 * 
	 * @return The number.
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	private final byte[] imageData;

	private static byte[] cloneImageData(byte[] original) {
		int len = original.length;
		byte[] result = new byte[len];
		System.arraycopy(original, 0, result, 0, len);
		return result;
	}

	/**
	 * Returns the image data. Clients should not modify the original contents
	 * of the event's image data. So the result data are cloned.
	 * 
	 * @return The cloned image data.
	 */
	public byte[] getImageData() {
		return cloneImageData(imageData);
	}

	/**
	 * Returns the size of the image data.
	 * 
	 * @return The number of bytes.
	 */
	public int getImageLength() {
		return imageData.length;
	}

	/**
	 * Creates an event instance.
	 * 
	 * @param source
	 *            The event source.
	 * @param creationMillis
	 *            The timestamp as number of milliseconds since 1970/1/1 0:0
	 *            UTC.
	 * @param sequenceNumber
	 *            The image sequence number.
	 * @param imageData
	 *            The image data.
	 * @throws ImageDataCorruptionException
	 *             if an invalid byte array was specified.
	 */
	public ImageEvent(Object source, long creationMillis, int sequenceNumber,
			byte[] imageData) {
		super(source);
		if (imageData == null) {
			throw new ImageDataCorruptionException(
					"Image data byte array reference must not be null.");
		}
		if (imageData.length == 0) {
			throw new ImageDataCorruptionException(
					"Image data byte array length must not be zero.");
		}
		this.creationMillis = creationMillis;
		this.sequenceNumber = sequenceNumber;
		this.imageData = cloneImageData(imageData);
	}

	/**
	 * Creates a {@link BufferedImage} instance from the image data byte array.
	 * 
	 * @return The image
	 * @throws ImageDataCorruptionException
	 *             if the image data were corrupted.
	 */
	public BufferedImage createBufferedImage() {
		BufferedImage result = null;
		InputStream bais = new ByteArrayInputStream(imageData);
		try {
			result = ImageIO.read(bais);
			if (result == null) {
				throw new ImageDataCorruptionException(
						"Failed to create buffered image from byte array.");
			}
		} catch (IOException ioe) {
			throw new ImageDataCorruptionException(
					"Failed to create buffered image from byte array.", ioe);
		} finally {
			try {
				bais.close();
			} catch (IOException ignored) {
			}
		}
		return result;
	}

	/**
	 * Returns the text representation of this object.
	 * 
	 * @return The representation
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ImageEvent[src=");
		sb.append(source.hashCode());
		sb.append(",created=");
		sb.append(creationMillis);
		sb.append("=");
		sb.append(getCreationDate());
		sb.append(",seqNo=");
		sb.append(sequenceNumber);
		sb.append(",image:");
		sb.append(imageData.length);
		sb.append(" bytes]");
		return sb.toString();
	}
}
