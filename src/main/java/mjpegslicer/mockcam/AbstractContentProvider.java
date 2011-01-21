package mjpegslicer.mockcam;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import mjpegslicer.LoggableObject;

/**
 * This is the base class for content providers.
 */
public abstract class AbstractContentProvider extends LoggableObject implements
		ContentProvider {

	private static List<ContentProvider> cloneList(List<ContentProvider> list) {
		if (list == null) {
			return null;
		}
		List<ContentProvider> result = new ArrayList<ContentProvider>();
		for (ContentProvider provider : list) {
			result.add(provider);
		}
		return list;
	}

	private List<ContentProvider> predecessors;

	/**
	 * Returns a clone of the list of predecessor content providers.
	 * 
	 * @return The list reference or <code>null</code> if no such provider list
	 *         is specified.
	 */
	@Override
	public List<ContentProvider> getPredecessors() {
		return cloneList(predecessors);
	}

	/**
	 * Redefines the list of predecessor content providers.
	 * 
	 * @param predecessors
	 *            The list reference or <code>null</code> if no such provider
	 *            list is specified.
	 */
	@Override
	public void setPredecessors(List<ContentProvider> predecessors) {
		String mn = debugEntering("setPreviousContentProvider", "value: ",
				predecessors);
		this.predecessors = cloneList(predecessors);
		debugLeaving(mn);
	}

	private List<ContentProvider> successors;

	/**
	 * Returns a clone of the list of successor content providers.
	 * 
	 * @return The list reference or <code>null</code> if no such provider list
	 *         is specified.
	 */
	@Override
	public List<ContentProvider> getSuccessors() {
		return cloneList(successors);
	}

	/**
	 * Redefines the list of successor content providers.
	 * 
	 * @param successors
	 *            The list reference or <code>null</code> if no such provider
	 *            list is specified.
	 */
	@Override
	public void setSuccessors(List<ContentProvider> successors) {
		String mn = debugEntering("setNextContentProvider", "value: ",
				successors);
		this.successors = cloneList(successors);
		debugLeaving(mn);
	}

	/**
	 * Traverse the tree of nested content providers and add content at each
	 * visited node.
	 * 
	 * @param image
	 *            The {@link BufferedImage} instance that receives the content.
	 */
	@Override
	public void traverse(BufferedImage image) {
		String mn = debugEntering("traverse");
		if (predecessors != null) {
			for (ContentProvider predecessor : predecessors) {
				predecessor.traverse(image);
			}
		}
		provideContent(image);
		if (successors != null) {
			for (ContentProvider successor : successors) {
				successor.traverse(image);
			}
		}
		debugLeaving(mn);
	}
}
