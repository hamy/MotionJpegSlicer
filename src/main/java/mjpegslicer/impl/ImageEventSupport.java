package mjpegslicer.impl;

import java.util.ArrayList;
import java.util.List;

import mjpegslicer.ImageEvent;
import mjpegslicer.ImageListener;
import mjpegslicer.util.LoggableObject;

/**
 * This is a support class that eases the distribution of {@link ImageEvent}
 * instances to {@link ImageListener} instances.
 */
public class ImageEventSupport extends LoggableObject implements ImageListener {

	private List<ImageListener> listeners = new ArrayList<ImageListener>();

	/**
	 * Returns the number of JPEG listeners.
	 * 
	 * @return The number.
	 */
	public int countJpegListeners() {
		return listeners.size();
	}

	/**
	 * Removes all listeners.
	 */
	public void removeAllJpegListeners() {
		String mn = debugEntering("removeAllJpegListeners");
		listeners.clear();
		debugLeaving(mn);
	}

	/**
	 * Adds a JPEG listener.
	 * 
	 * @param listener
	 *            The listener that is added.
	 */
	public void addJpegListener(ImageListener listener) {
		String mn = debugEntering("addJpegListener", "listener: ", listener);
		if (listener == null) {
			warn(mn, "Listener reference null is ignored.");
		} else if (listeners.contains(listener)) {
			warn(mn, "listener is already registered: ", listener);
		} else {
			listeners.add(listener);
		}
		debugLeaving(mn, "new listener count: ", listeners.size());
	}

	/**
	 * Removes a JPEG listener.
	 * 
	 * @param listener
	 *            The listener that is removed.
	 */
	public void removeJpegListener(ImageListener listener) {
		String mn = debugEntering("removeJpegListener", "listener: ", listener);
		if (listener == null) {
			warn(mn, "Listener reference null is ignored.");
		} else if (!listeners.contains(listener)) {
			warn(mn, "listener is not registered: ", listener);
		} else {
			listeners.remove(listener);
		}
		debugLeaving(mn, "new listener count: ", listeners.size());
	}

	/**
	 * The JPEG listener implementation: distribute events to all registered
	 * listeners.
	 * 
	 * @param event
	 *            The event that shall be distributed.
	 */
	@Override
	public void newImage(ImageEvent event) {
		for (ImageListener listener : listeners) {
			listener.newImage(event);
		}
	}

	/**
	 * Returns the text representation of this object.
	 * 
	 * @return The representation
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ImageEventSupport[listeners:");
		sb.append(listeners.size());
		sb.append("]");
		return sb.toString();
	}
}
