package mjpegslicer.source;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class AssertSource {

	// prevent instantiation
	private AssertSource() {
	}

	public static void assertSource(String expectedHostName, int expectedPort,
			String expectedPath, String expectedUserName,
			String expectedPassword, boolean expectedIsStarted,
			long expectedByteCount, Source actual) {
		assertNotNull("actual source reference must not be null.", actual);
		assertNotNull("expected host name must not be null.", expectedHostName);
		assertEquals("host name mismatch:", expectedHostName,
				actual.getHostName());
		assertEquals("port mismatch:", expectedPort, actual.getPort());
		assertNotNull("expected path must not be null.", expectedPath);
		assertEquals("path mismatch:", expectedPath, actual.getPath());
		assertEquals("user name mismatch:", expectedUserName,
				actual.getUserName());
		assertEquals("password mismatch:", expectedPassword,
				actual.getPassword());
		assertEquals("start flag mismatch:", expectedIsStarted,
				actual.isStarted());
	}

}
