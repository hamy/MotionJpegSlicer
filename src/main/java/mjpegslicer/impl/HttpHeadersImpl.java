package mjpegslicer.impl;

import java.util.ArrayList;
import java.util.List;

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

	private final String[] keys;

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

	private final String[] values;

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

	private final int contentLength;

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

	private final String contentType;

	/**
	 * Indicates whether this container contains a "Content-Type" header entry.
	 * 
	 * @return The flag.
	 */
	@Override
	public boolean hasContentType() {
		return contentType != null;
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

	private final String boundaryLookUpString;
	private final String boundary;

	/**
	 * Indicates whether this container contains a boundary token in a
	 * "Content-Type" header entry.
	 * 
	 * @return The flag.
	 */
	@Override
	public boolean hasBoundary() {
		return boundary != null;
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

	public static class Builder extends LoggableObject {
		private List<String> keys = new ArrayList<String>();
		private List<String> values = new ArrayList<String>();
		private String lastKey;
		private String lastValue;
		private String contentType = null;
		private int contentLength = INVALID_CONTENT_LENGTH;
		private String boundary = null;
		private final String boundaryLookUpString;

		public Builder(String boundaryLookUpString) {
			debugEntering(MN_INIT);
			this.boundaryLookUpString = boundaryLookUpString;
			debug(MN_INIT, "boundary look-up string: ", boundaryLookUpString);
			debugLeaving(MN_INIT);
		}

		private void boundaryLookUp() {
			String mn = debugEntering("boundaryLookUp");
			if (boundaryLookUpString != null) {
				if (lastKey.equals(boundaryLookUpString)) {
					boundary = lastKey;
					debug(mn, "found boundary: ", boundary);
				}
			}
			debugLeaving(mn);
		}

		private void contentTypeLookUp() {
			String mn = debugEntering("contentTypeLookUp");
			if (lastKey.equalsIgnoreCase("Content-Type")) {
				contentType = lastValue;
				debug(mn, "found content type: ", contentType);
			}
			debugLeaving(mn);
		}

		private void contentLengthLookUp() {
			String mn = debugEntering("contentLengthLookUp");
			if (lastKey.equalsIgnoreCase("Content-Length")) {
				contentLength = Integer.parseInt(lastValue);
				debug(mn, "found content length: ", contentLength);
			}
			debugLeaving(mn);
		}

		public Builder addHeader(String line) {
			String mn = debugEntering("addHeader", "line: ", line);
			Validator.argumentMustNotBeNull("addHeader", "line", line);
			int colonPos = line.indexOf(':');
			if (colonPos < 0) {
				lastKey = line;
				lastValue = "";
			} else {
				lastKey = line.substring(0, colonPos);
				if (colonPos == line.length() - 1) {
					lastValue = "";
				} else {
					lastValue = line.substring(colonPos + 1);
				}
				lastValue = lastValue.trim();
			}
			keys.add(lastKey);
			values.add(lastValue);
			debug(mn, "key=", lastKey, ", value=", lastValue);
			contentTypeLookUp();
			contentLengthLookUp();
			boundaryLookUp();
			debugLeaving(mn);
			return this;
		}

		public HttpHeadersImpl build() {
			String mn = debugEntering("build");
			HttpHeadersImpl result = new HttpHeadersImpl(this);
			debugLeaving(mn);
			return result;
		}
	}

	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	private HttpHeadersImpl(Builder builder) {
		String mn = debugEntering(MN_INIT);
		keys = builder.keys.toArray(EMPTY_STRING_ARRAY);
		values = builder.values.toArray(EMPTY_STRING_ARRAY);
		contentType = builder.contentType;
		contentLength = builder.contentLength;
		boundaryLookUpString = builder.boundaryLookUpString;
		boundary = builder.boundary;
		debugLeaving(mn);
	}

}
