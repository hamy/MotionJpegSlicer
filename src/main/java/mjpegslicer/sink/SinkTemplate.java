package mjpegslicer.sink;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import mjpegslicer.ImageDataCorruptionException;
import mjpegslicer.LoggableObject;
import mjpegslicer.Validator;

public class SinkTemplate extends LoggableObject implements Sink {

	private boolean started = false;

	/**
	 * Indicates whether the streaming was started.
	 * 
	 * @return <code>true</code> if the streaming was started, otherwise
	 *         <code>false</code>
	 */
	public boolean isStarted() {
		return started;
	}

	private AtomicLong byteCount = new AtomicLong();

	/**
	 * Returns the number of bytes transferred so far.
	 * 
	 * @return The byte count.
	 */
	public long getByteCount() {
		return byteCount.get();
	}

	private InputStream inputStream;

	/**
	 * Starts the streaming.
	 * 
	 * @param inputStream
	 *            The {@link InputStream} that is used for streaming.
	 */
	public void startStream(InputStream inputStream) {
		String mn = debugEntering("startStream", "value: ", inputStream);
		Validator.argumentMustNotBeNull(mn, "inputStream", inputStream);
		Validator.checkState(mn, !started, "Sink is already started.");
		started = true;
		this.inputStream = new BufferedInputStream(inputStream, 8192);
		debugLeaving(mn);
	}

	/**
	 * Stops the streaming.
	 */
	public void stopStream() {
		String mn = debugEntering("stopStream");
		Validator.checkState(mn, started, "Sink was not started.");
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Throwable ignored) {
			} finally {
				inputStream = null;
			}
		}
		started = false;
		debugLeaving(mn);
	}

	/**
	 * Extracts the next HTTP header from the stream.
	 * 
	 * @return The header lines without the end of line characters or
	 *         <code>null</code> if the end of data is reached.
	 */
	public String[] readHttpHeader() {
		String mn = debugEntering("stopStream");
		Validator.checkState(mn, started, "Sink was not started.");
		List<String> headers = new LinkedList<String>();
		boolean skipEmpty = true;
		boolean cont = true;
		boolean eod = false;
		while (cont && !eod) {
			String line = null;
			try {
				line = readLine();
			} catch (IOException ioe) {
				throw new ImageDataCorruptionException(
						"Failed to read HTTP header line: ", ioe);
			}
			if (line == null) {
				warn(mn, "EOD found.");
				eod = true;
			} else {
				if (line.equals("")) {
					if (skipEmpty) {
						// nop: continue to skip heading empty lines...
					} else {
						cont = false;
						httpHeaderCount.incrementAndGet();
					}
				} else {
					skipEmpty = false;
					headers.add(line);
				}
			}
		}
		String[] result = eod ? null : headers.toArray(new String[0]);
		debugLeaving(mn);
		return result;
	}

	/**
	 * Reads the next input line.
	 * 
	 * @return The line without the end of line characters or <code>null</code>
	 *         if the end of data is reached.
	 * @throws IOException
	 */
	protected String readLine() throws IOException {
		String mn = debugEntering("readLine");
		StringBuilder sb = new StringBuilder();
		boolean eol = false;
		boolean eod = false;
		boolean searchingEOL = true;
		while (searchingEOL && !eod) {
			int ch = inputStream.read();
			if (ch < 0) {
				warn(mn, "EOD found after ", byteCount.get(), " bytes.");
				eod = true;
			} else {
				byteCount.incrementAndGet();
				switch (ch) {
				case 0x0d:
					eol = true;
					break;
				case 0x0a:
					if (eol) {
						searchingEOL = false;
					}
					break;
				default:
					eol = false;
					sb.append((char) ch);
					break;
				}
			}
		}
		String result = eod ? null : sb.toString();
		debugLeaving(mn, "result: ", result);
		return result;
	}

	private AtomicInteger httpHeaderCount = new AtomicInteger();

	/**
	 * Returns the number of HTTP headers read so far.
	 * 
	 * @return The header count.
	 */
	public int getHttpHeaderCount() {
		return httpHeaderCount.get();
	}

	/**
	 * Extracts the next image data chunk from the stream.
	 * 
	 * @param contentLength
	 *            The content length.
	 * @return The data or <code>null</code> if the end of data is reached.
	 */
	public byte[] readImageData(int contentLength) {
		String mn = debugEntering("readImageData");
		Validator.checkState(mn, started, "Sink was not started.");
		Validator.checkArgument(mn, "contentLength", contentLength,
				contentLength > 0, "content length must be positive.");
		byte[] result = new byte[contentLength];
		int remaining = contentLength;
		int pos = 0;
		while (remaining > 0) {
			try {
				int actual = inputStream.read(result, pos, remaining);
				if (actual < 0) {
					remaining = 0;
					result = null;
					warn(mn, "EOD found after ", byteCount.get(), " bytes.");
				} else if (actual > 0) {
					pos += actual;
					remaining -= actual;
					byteCount.addAndGet(actual);
				}
			} catch (IOException ioe) {
				throw new ImageDataCorruptionException(
						"Failed to read HTTP header line: ", ioe);
			}

		}
		if (result != null) {
			imageCount.incrementAndGet();
		}
		debugLeaving(mn);
		return result;
	}

	private AtomicInteger imageCount = new AtomicInteger();

	/**
	 * Returns the number of images read so far.
	 * 
	 * @return The image count.
	 */
	public int getImageCount() {
		return imageCount.get();
	}
}
