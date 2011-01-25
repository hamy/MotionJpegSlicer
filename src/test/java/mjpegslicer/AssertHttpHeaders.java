package mjpegslicer;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class AssertHttpHeaders {

	// prevent instantiation
	private AssertHttpHeaders() {
	}

	public static void assertHttpHeaders(int expectedSize,
			boolean expectedHasBoundary, String expectedBoundary,
			boolean expectedHasContentLength, int expectedContentLength,
			boolean expectedHasContentType, String expectedContentType,
			HttpHeaders actual) {
		assertNotNull("actual HTTP headers reference must not be null.", actual);
		assertEquals("size mismatch:", expectedSize, actual.size());
		assertEquals("hasBoundary mismatch:", expectedHasBoundary,
				actual.hasBoundary());
		if (expectedHasBoundary) {
			assertNotNull("boundary must not be null.", actual.getBoundary());
			assertEquals("boundary mismatch: ", expectedBoundary,
					actual.getBoundary());
		} else {
			assertNull("boundary must be null.", actual.getBoundary());
		}
		assertEquals("hasContentLength mismatch:", expectedHasContentLength,
				actual.hasContentLength());
		assertEquals("content length mismatch: ", expectedContentLength,
				actual.getContentLength());
		assertEquals("hasContentType mismatch:", expectedHasContentType,
				actual.hasContentType());
		if (expectedHasContentType) {
			assertNotNull("content type must not be null.",
					actual.getContentType());
			assertEquals("content type mismatch: ", expectedContentType,
					actual.getContentType());
		} else {
			assertNull("content type must be null.", actual.getContentType());
		}
	}
}
