package mjpegslicer;

/**
 * This exception is thrown when corrupted images from an M-JPEG stream are
 * processed.
 */
public class ImageDataCorruptionException extends RuntimeException {

	private static final long serialVersionUID = -1L;

	/**
	 * Creates an instance of this exception.
	 * 
	 * @param message
	 *            The error message.
	 */
	public ImageDataCorruptionException(String message) {
		super(message);
	}

	/**
	 * Creates an instance of this exception.
	 * 
	 * @param message
	 *            The error message.
	 * @param cause
	 *            The underlying cause for this exception.
	 */
	public ImageDataCorruptionException(String message, Throwable cause) {
		super(message, cause);
	}
}
