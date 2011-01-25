package mjpegslicer.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

import mjpegslicer.AbstractTestCase;
import static mjpegslicer.AssertHttpHeaders.assertHttpHeaders;
import mjpegslicer.HttpHeaders;
import mjpegslicer.impl.HttpHeadersImpl;

public class TestHttpHeadersImpl extends AbstractTestCase {

	private HttpHeadersImpl impl;
	private HttpHeadersImpl.Builder bldr;
	private static final String BOUNDARY_LOOK_UP_STRING = "--myboundary";

	@Before
	public void setUp() {
		String mn = debugEntering("setUp");
		bldr = new HttpHeadersImpl.Builder(BOUNDARY_LOOK_UP_STRING);
		debugLeaving(mn, "created: ", impl);
	}

	@Test
	public void testSetUp() {
		String mn = debugEntering("testSetUp");
		assertNull(impl);
		assertNotNull(bldr);
		debugLeaving(mn);
	}

	@Test
	public void testEmptyHeaders() {
		String mn = debugEntering("testEmptyHeaders");
		impl = bldr.build();
		assertNotNull(impl);
		assertHttpHeaders(0, false, null, false,
				HttpHeaders.INVALID_CONTENT_LENGTH, false, null, impl);
		debugLeaving(mn);
	}

	@Test
	public void testWithBoundary() {
		String mn = debugEntering("testWithBoundary");
		bldr.addHeader(BOUNDARY_LOOK_UP_STRING);
		impl = bldr.build();
		assertNotNull(impl);
		assertHttpHeaders(1, true, BOUNDARY_LOOK_UP_STRING, false,
				HttpHeaders.INVALID_CONTENT_LENGTH, false, null, impl);
		debugLeaving(mn);
	}

	@Test
	public void testWithContentLength() {
		String mn = debugEntering("testWithContentLength");
		bldr.addHeader("content-length: 12345");
		impl = bldr.build();
		assertNotNull(impl);
		assertHttpHeaders(1, false, null, true,
				12345, false, null, impl);
		debugLeaving(mn);
	}

	@Test
	public void testWithContentType() {
		String mn = debugEntering("testWithContentType");
		bldr.addHeader("content-type: image/png");
		impl = bldr.build();
		assertNotNull(impl);
		assertHttpHeaders(1, false, null, false,
				HttpHeaders.INVALID_CONTENT_LENGTH, true, "image/png", impl);
		debugLeaving(mn);
	}

	@Test
	public void testWithAll() {
		String mn = debugEntering("testWithAll");
		bldr.addHeader("some-header: foo");
		bldr.addHeader(BOUNDARY_LOOK_UP_STRING);
		bldr.addHeader("content-length: 9876");
		bldr.addHeader("content-type: text/html");
		bldr.addHeader("another-header: bar");
		impl = bldr.build();
		assertNotNull(impl);
		assertHttpHeaders(5, true, BOUNDARY_LOOK_UP_STRING, true, 9876, true,
				"text/html", impl);
		debugLeaving(mn);
	}
}
