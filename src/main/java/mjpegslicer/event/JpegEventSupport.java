package mjpegslicer.event;

import java.util.ArrayList;
import java.util.List;

import mjpegslicer.LoggableObject;

/**
 * This is a support class that eases the distribution of {@link JpegEvent}
 * instances to {@link JpegListener} instances.
 */
public class JpegEventSupport extends LoggableObject implements JpegListener {

	private List<JpegListener> listeners = new ArrayList<JpegListener>();

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
	public void addJpegListener(JpegListener listener) {
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
	public void removeJpegListener(JpegListener listener) {
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
	public void newImage(JpegEvent event) {
		for (JpegListener listener : listeners) {
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
		StringBuilder sb = new StringBuilder("JpegEventSupport[listeners:");
		sb.append(listeners.size());
		sb.append("]");
		return sb.toString();
	}
}
