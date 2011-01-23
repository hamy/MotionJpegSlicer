package mjpegslicer.impl;

import static mjpegslicer.AssertSink.assertSink;
import static org.junit.Assert.assertTrue;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mjpegslicer.AbstractTestCase;
import mjpegslicer.Sink;
import mjpegslicer.impl.SinkTemplate;

public class TestSinkTemplate extends AbstractTestCase {

	private SinkTemplate template;
	private PipedInputStream pis;
	private PipedOutputStream pos;

	private void posLine(String line) throws Exception {
		pos.write(line.getBytes());
		pos.write(0x0d);
		pos.write(0x0a);
		pos.flush();
	}

	private void posInitialHttpHeader() throws Exception {
		posLine("HTTP/1.1 200 OK");
		posLine("Server: testFirstHttpHeader");
		posLine("Pragma: no-cache");
		posLine("Cache-Control: no-cache");
		posLine("Content-Type: multipart/x-mixed-replace; boundary=myboundary");
		posLine("");
		sleepMillis(500);
	}

	private void posImage() throws Exception {
		posLine("--myboundary");
		posLine("Content-Type: image/jpeg");
		posLine("Content-Length: 10");
		posLine("");
		posLine("0123456789"); // 10 bytes pseudo-JPEG data and CRLF
		sleepMillis(500);
	}

	@Before
	public void setUp() throws Exception {
		String mn = debugEntering("setUp");
		template = new SinkTemplate();
		pis = new PipedInputStream();
		pos = new PipedOutputStream(pis);
		debugLeaving(mn, "created: ", template);
	}

	@After
	public void tearDown() {
		String mn = debugEntering("tearDown");
		if (template != null) {
			if (template.isStarted()) {
				template.stopStream();
			}
		}
		if (pis != null) {
			try {
				pis.close();
			} catch (Throwable ignored) {
			} finally {
				pis = null;
			}
		}
		if (pos != null) {
			try {
				pos.close();
			} catch (Throwable ignored) {
			} finally {
				pos = null;
			}
		}
		debugLeaving(mn);
	}

	@Test
	public void testSetUp() {
		String mn = debugEntering("testSetUp");
		assertSink(false, 0, 0, 0, template);
		debugLeaving(mn);
	}

	@Test
	public void testStartWithNullInputStream() {
		String mn = debugEntering("testStartWithNullInputStream");
		try {
			template.startStream(null);
			failedToThrowExpectedException(mn);
		} catch (NullPointerException npe) {
			foundExpectedException(mn, npe);
		}
		assertSink(false, 0, 0, 0, template);
		debugLeaving(mn);
	}

	@Test
	public void testStartAndStop() throws Exception {
		String mn = debugEntering("testStartAndStop");
		template.startStream(pis);
		assertSink(true, 0, 0, 0, template);
		sleepMillis(500);
		template.stopStream();
		assertSink(false, 0, 0, 0, template);
		debugLeaving(mn);
	}

	@Test
	public void testStartAndMultipleStop() throws Exception {
		String mn = debugEntering("testStartAndMultipleStop");
		template.startStream(pis);
		assertSink(true, 0, 0, 0, template);
		sleepMillis(500);
		template.stopStream();
		assertSink(false, 0, 0, 0, template);
		try {
			template.stopStream();
			failedToThrowExpectedException(mn);
		} catch (IllegalStateException ise) {
			foundExpectedException(mn, ise);
		}
		debugLeaving(mn);
	}

	@Test
	public void testMultipleStart() {
		String mn = debugEntering("testMultipleStart");
		template.startStream(pis);
		assertSink(true, 0, 0, 0, template);
		try {
			template.startStream(pis);
			failedToThrowExpectedException(mn);
		} catch (IllegalStateException ise) {
			foundExpectedException(mn, ise);
		}
		debugLeaving(mn);
	}

	@Test
	public void testInitialHttpHeader() throws Exception {
		String mn = debugEntering("testInitialHttpHeader");
		template.startStream(pis);
		assertSink(true, 0, 0, 0, template);
		posInitialHttpHeader();
		long bc = template.getByteCount();
		assertTrue("byte count should be positive.", bc > 0);
		assertSink(true, bc, 1, 0, template);
		debugLeaving(mn);
	}

	@Test
	public void testInitialHttpHeaderAndTwoImages() throws Exception {
		String mn = debugEntering("testInitialHttpHeaderAndTwoImages");
		template.startStream(pis);
		assertSink(true, 0, 0, 0, template);
		posInitialHttpHeader();
		long bc = template.getByteCount();
		assertTrue("byte count should be positive.", bc > 0);
		assertSink(true, bc, 1, 0, template);
		posImage();
		bc = template.getByteCount();
		assertSink(true, bc, 2, 1, template);
		posImage();
		bc = template.getByteCount();
		assertSink(true, bc, 3, 2, template);
		debugLeaving(mn);
	}
}
