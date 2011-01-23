package mjpegslicer.impl;

import static mjpegslicer.AssertSource.assertSource;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mjpegslicer.AbstractTestCase;
import mjpegslicer.Source;
import mjpegslicer.impl.SourceTemplate;
import mjpegslicer.mockcam.Camera;
import mjpegslicer.mockcam.DefaultFeederFactory;

public class TestSourceTemplate extends AbstractTestCase {

	private SourceTemplate template;

	@Before
	public void setUp() {
		String mn = debugEntering("setUp");
		template = new SourceTemplate();
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
		debugLeaving(mn);
	}

	@Test
	public void testSetUp() {
		String mn = debugEntering("testSetUp");
		assertSource(Source.DEFAULT_HOST_NAME, Source.DEFAULT_PORT,
				Source.DEFAULT_PATH, Source.DEFAULT_USER_NAME,
				Source.DEFAULT_PASSWORD, false, 0, template);
		debugLeaving(mn);
	}

	@Test
	public void testValidHostName() {
		String mn = debugEntering("testValidHostName");
		String newHostName = "foo.bar.com";
		template.setHostName(newHostName);
		assertSource(newHostName, Source.DEFAULT_PORT, Source.DEFAULT_PATH,
				Source.DEFAULT_USER_NAME, Source.DEFAULT_PASSWORD, false, 0,
				template);
		debugLeaving(mn);
	}

	@Test
	public void testNullHostName() {
		String mn = debugEntering("testNullHostName");
		try {
			template.setHostName(null);
			failedToThrowExpectedException(mn);
		} catch (NullPointerException npe) {
			foundExpectedException(mn, npe);
		}
		debugLeaving(mn);
	}

	@Test
	public void testEmptyHostName() {
		String mn = debugEntering("testEmptyHostName");
		try {
			template.setHostName("");
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		try {
			template.setHostName("  \t \r \n ");
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		debugLeaving(mn);
	}

	@Test
	public void testValidPort() {
		String mn = debugEntering("testValidPort");
		int newPort = 8080;
		template.setPort(newPort);
		assertSource(Source.DEFAULT_HOST_NAME, newPort, Source.DEFAULT_PATH,
				Source.DEFAULT_USER_NAME, Source.DEFAULT_PASSWORD, false, 0,
				template);
		newPort = 1;
		template.setPort(newPort);
		assertSource(Source.DEFAULT_HOST_NAME, newPort, Source.DEFAULT_PATH,
				Source.DEFAULT_USER_NAME, Source.DEFAULT_PASSWORD, false, 0,
				template);
		newPort = 65535;
		template.setPort(newPort);
		assertSource(Source.DEFAULT_HOST_NAME, newPort, Source.DEFAULT_PATH,
				Source.DEFAULT_USER_NAME, Source.DEFAULT_PASSWORD, false, 0,
				template);
		debugLeaving(mn);
	}

	@Test
	public void testInvalidPort() {
		String mn = debugEntering("testInvalidPort");
		try {
			template.setPort(-1);
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		try {
			template.setPort(0);
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		try {
			template.setPort(65536);
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		try {
			template.setPort(65537);
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		debugLeaving(mn);
	}

	@Test
	public void testValidPath() {
		String mn = debugEntering("testValidPath");
		String newPath = "/foo/bar";
		template.setPath(newPath);
		assertSource(Source.DEFAULT_HOST_NAME, Source.DEFAULT_PORT, newPath,
				Source.DEFAULT_USER_NAME, Source.DEFAULT_PASSWORD, false, 0,
				template);
		debugLeaving(mn);
	}

	@Test
	public void testNullPath() {
		String mn = debugEntering("testNullPath");
		try {
			template.setPath(null);
			failedToThrowExpectedException(mn);
		} catch (NullPointerException npe) {
			foundExpectedException(mn, npe);
		}
		debugLeaving(mn);
	}

	@Test
	public void testEmptyPath() {
		String mn = debugEntering("testEmptyPath");
		try {
			template.setPath("");
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		try {
			template.setPath("  \t \r \n ");
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		debugLeaving(mn);
	}

	@Test
	public void testValidUserName() {
		String mn = debugEntering("testValidUserName");
		String newUserName = "jane.doe";
		template.setUserName(newUserName);
		assertSource(Source.DEFAULT_HOST_NAME, Source.DEFAULT_PORT,
				Source.DEFAULT_PATH, newUserName, Source.DEFAULT_PASSWORD,
				false, 0, template);
		debugLeaving(mn);
	}

	@Test
	public void testNullUserName() {
		String mn = debugEntering("testNullUserName");
		template.setUserName(null);
		assertSource(Source.DEFAULT_HOST_NAME, Source.DEFAULT_PORT,
				Source.DEFAULT_PATH, null, Source.DEFAULT_PASSWORD, false, 0,
				template);
		debugLeaving(mn);
	}

	@Test
	public void testValidPassword() {
		String mn = debugEntering("testValidPassword");
		String newPassword = "secret";
		template.setPassword(newPassword);
		assertSource(Source.DEFAULT_HOST_NAME, Source.DEFAULT_PORT,
				Source.DEFAULT_PATH, Source.DEFAULT_USER_NAME, newPassword,
				false, 0, template);
		debugLeaving(mn);
	}

	@Test
	public void testNullPassword() {
		String mn = debugEntering("testNullPassword");
		template.setPassword(null);
		assertSource(Source.DEFAULT_HOST_NAME, Source.DEFAULT_PORT,
				Source.DEFAULT_PATH, Source.DEFAULT_USER_NAME, null, false, 0,
				template);
		debugLeaving(mn);
	}

	@Test
	public void testWithMockCamera() {
		String mn = debugEntering("testWithMockCamera");
		Camera camera = null;
		try {
			camera = new Camera();
			camera.setFeederFactory(new DefaultFeederFactory());
			camera.start();
			template.setPort(Camera.DEFAULT_PORT);
			template.startStream();
			Thread.sleep(2000);
			template.stopStream();
		} catch (Throwable ta) {
			fail("exception: " + ta);
		} finally {
			if (camera != null) {
				try {
					camera.stop();
				} catch (Throwable ignored) {
				}
			}
		}
		debugLeaving(mn);
	}

}
