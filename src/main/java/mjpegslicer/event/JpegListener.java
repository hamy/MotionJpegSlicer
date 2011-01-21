package mjpegslicer.event;

/**
 * Clients that want to be notified when a JPEG image was sliced from the camera
 * M-JPEG stream must implement this interface.
 */
public interface JpegListener {

	/**
	 * Notification method.
	 * 
	 * @param event
	 *            The event that describes the JPEG image.
	 */
	public void newImage(JpegEvent event);
}
