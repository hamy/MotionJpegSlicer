package mjpegslicer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class TestImageDataCorruptionException extends AbstractTestCase {

	private String setUpMessage;
	private Throwable setUpCause;

	@Before
	public void setUp() {
		setUpMessage = "Oops: data corruption!";
		setUpCause = new RuntimeException("This is the cause.");
	}

	@Test
	public void testSetUp() {
		String mn = debugEntering("testSetUp");
		assertNotNull(setUpMessage);
		assertNotNull(setUpCause);
		debugLeaving(mn);
	}

	@Test
	public void testConstructor1() {
		String mn = debugEntering("testConstructor1");
		ImageDataCorruptionException idce = new ImageDataCorruptionException(
				setUpMessage);
		assertNotNull(idce.getMessage());
		assertEquals(setUpMessage, idce.getMessage());
		assertNull(idce.getCause());
		debugLeaving(mn);
	}

	@Test
	public void testConstructor2() {
		String mn = debugEntering("testConstructor2");
		ImageDataCorruptionException idce = new ImageDataCorruptionException(
				setUpMessage, setUpCause);
		assertNotNull(idce.getMessage());
		assertEquals(setUpMessage, idce.getMessage());
		assertNotNull(idce.getCause());
		assertSame(setUpCause, idce.getCause());
		debugLeaving(mn);
	}
}
