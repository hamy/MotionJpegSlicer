package mjpegslicer;

public interface HttpHeaders {

	/**
	 * Returns the number of HTTP headers in this container.
	 * 
	 * @return The count.
	 */
	public int size();

	/**
	 * Returns the i-th key.
	 * 
	 * @param i
	 *            The index.
	 * @return The key.
	 */
	public String getKey(int i);

	/**
	 * Returns the i-th value.
	 * 
	 * @param i
	 *            The index.
	 * @return The value.
	 */
	public String getValue(int i);

	/**
	 * Indicates whether this container contains a "Content-Length" header
	 * entry.
	 * 
	 * @return The flag.
	 */
	public boolean hasContentLength();

	/**
	 * Indicates unknown content length.
	 */
	public static final int INVALID_CONTENT_LENGTH = -1;

	/**
	 * Returns the value of the "Content-Length" header entry.
	 * 
	 * @return The value or {@link #INVALID_CONTENT_LENGTH} if no matching entry
	 *         was found.
	 */
	public int getContentLength();

	/**
	 * Indicates whether this container contains a "Content-Type" header entry.
	 * 
	 * @return The flag.
	 */
	public boolean hasContentType();

	/**
	 * Returns the value of the "Content-Type" header entry.
	 * 
	 * @return The value or <code>null</code> if no matching entry was found.
	 */
	public String getContentType();

	/**
	 * Indicates whether this container contains a boundary token in a
	 * "Content-Type" header entry.
	 * 
	 * @return The flag.
	 */
	public boolean hasBoundary();

	/**
	 * Returns the value of the boundary token.
	 * 
	 * @return The value or <code>null</code> if no matching entry was found.
	 */
	public String getBoundary();
}
