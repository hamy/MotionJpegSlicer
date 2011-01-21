package mjpegslicer.mockcam;

import java.awt.image.BufferedImage;

import java.util.List;

/**
 * This interface defines a nestable content provider that feeds content into a
 * {@link BufferedImage} instance.
 */
public interface ContentProvider {

	/**
	 * Returns a clone of the list of predecessor content providers.
	 * 
	 * @return The list reference or <code>null</code> if no such provider list
	 *         is specified.
	 */
	public List<ContentProvider> getPredecessors();

	/**
	 * Redefines the list of predecessor content providers.
	 * 
	 * @param predecessors
	 *            The list reference or <code>null</code> if no such provider
	 *            list is specified.
	 */
	public void setPredecessors(List<ContentProvider> predecessors);

	/**
	 * Returns a clone of the list of successor content providers.
	 * 
	 * @return The list reference or <code>null</code> if no such provider list
	 *         is specified.
	 */
	public List<ContentProvider> getSuccessors();

	/**
	 * Redefines the list of successor content providers.
	 * 
	 * @param successors
	 *            The list reference or <code>null</code> if no such provider
	 *            list is specified.
	 */
	public void setSuccessors(List<ContentProvider> successors);

	/**
	 * Traverse the tree of nested content providers and add content at each
	 * visited node.
	 * 
	 * @param image
	 *            The {@link BufferedImage} instance that receives the content.
	 */
	public void traverse(BufferedImage image);

	/**
	 * Provide content for the local node.
	 * 
	 * @param image
	 *            The {@link BufferedImage} instance that receives the content.
	 */
	public void provideContent(BufferedImage image);
}
