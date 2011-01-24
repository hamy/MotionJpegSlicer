package mjpegslicer.impl;

import mjpegslicer.HttpHeaders;
import mjpegslicer.util.LoggableObject;
import mjpegslicer.util.Validator;

public class HttpHeadersImpl extends LoggableObject implements HttpHeaders {

	/**
	 * Returns the number of HTTP headers in this container.
	 * 
	 * @return The count.
	 */
	@Override
	public int size() {
		return keys.length;
	}

	private String[] keys;

	/**
	 * Returns the i-th key.
	 * 
	 * @param i
	 *            The index.
	 * @return The key.
	 */
	@Override
	public String getKey(int i) {
		Validator.checkArgument("getKey", "i", i, i >= 0 && i < keys.length,
				"Invalid index: ", i);
		return keys[i];
	}

	private String[] values;

	/**
	 * Returns the i-th value.
	 * 
	 * @param i
	 *            The index.
	 * @return The value.
	 */
	@Override
	public String getValue(int i) {
		Validator.checkArgument("getValue", "i", i, i >= 0 && i < keys.length,
				"Invalid index: ", i);
		return values[i];
	}

	private int contentLength = INVALID_CONTENT_LENGTH;

	/**
	 * Indicates whether this container contains a "Content-Length" header
	 * entry.
	 * 
	 * @return The flag.
	 */
	@Override
	public boolean hasContentLength() {
		return contentLength != INVALID_CONTENT_LENGTH;
	}

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
	@Override
	public int getContentLength() {
		return contentLength;
	}

	private String contentType = null;

	/**
	 * Indicates whether this container contains a "Content-Type" header entry.
	 * 
	 * @return The flag.
	 */
	@Override
	public boolean hasContentType() {
		return contentType == null;
	}

	/**
	 * Returns the value of the "Content-Type" header entry.
	 * 
	 * @return The value or <code>null</code> if no matching entry was found.
	 */
	@Override
	public String getContentType() {
		return contentType;
	}

	private String boundary = null;

	/**
	 * Indicates whether this container contains a boundary token in a
	 * "Content-Type" header entry.
	 * 
	 * @return The flag.
	 */
	@Override
	public boolean hasBoundary() {
		return boundary == null;
	}

	/**
	 * Returns the value of the boundary token.
	 * 
	 * @return The value or <code>null</code> if no matching entry was found.
	 */
	@Override
	public String getBoundary() {
		return boundary;
	}

	// TBD: add builder class (see Bloch)
}
