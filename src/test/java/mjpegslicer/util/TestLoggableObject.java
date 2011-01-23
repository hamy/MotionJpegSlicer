package mjpegslicer.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import mjpegslicer.AbstractTestCase;
import mjpegslicer.util.LoggableObject;

import org.junit.Before;
import org.junit.Test;

public class TestLoggableObject extends AbstractTestCase {

	private LoggableObject lo;

	@Before
	public void setUp() {
		lo = new LoggableObject();
	}

	@Test
	public void testSetUp() {
		String mn = debugEntering("testSetUp");
		assertNotNull(lo);
		assertEquals("LoggableObject", lo.getSimpleClassName());
		assertEquals(lo.getClass().getName(), lo.getFullyQualifiedClassName());
		assertEquals("TestLoggableObject", getSimpleClassName());
		assertEquals(getClass().getName(), getFullyQualifiedClassName());
		debugLeaving(mn);
	}
}
