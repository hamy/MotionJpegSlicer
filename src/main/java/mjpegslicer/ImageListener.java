package mjpegslicer;

/**
 * Clients that want to be notified when an image was sliced from the camera
 * M-JPEG stream must implement this interface.
 */
public interface ImageListener {

	/**
	 * Notification method.
	 * 
	 * @param event
	 *            The event that describes the image.
	 */
	public void newImage(ImageEvent event);
}
