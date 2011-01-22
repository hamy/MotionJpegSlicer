package mjpegslicer.sink;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class AssertSink {

	// prevent instantiation
	private AssertSink() {
	}

	public static void assertSink(boolean expectedIsStarted,
			long expectedByteCount, int expectedHttpHeaderCount,
			int expectedImageCount, Sink actual) {
		assertNotNull("actual sink reference must not be null.", actual);
		assertEquals("start flag mismatch:", expectedIsStarted,
				actual.isStarted());
		assertEquals("byte count mismatch:", expectedByteCount,
				actual.getByteCount());
		assertEquals("HTTP header count mismatch:", expectedHttpHeaderCount,
				actual.getHttpHeaderCount());
		assertEquals("image count mismatch:", expectedImageCount,
				actual.getImageCount());
	}

}
