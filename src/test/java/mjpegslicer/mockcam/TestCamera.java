package mjpegslicer.mockcam;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mjpegslicer.AbstractTestCase;

public class TestCamera extends AbstractTestCase {

	private Camera camera;

	@Before
	public void setUp() throws Exception {
		String mn = debugEntering("setUp");
		camera = new Camera();
		camera.setFeederFactory(new DefaultFeederFactory());
		debugLeaving(mn);
	}

	@After
	public void tearDown() throws Exception {
		String mn = debugEntering("tearDown");
		if (camera != null) {
			if (camera.isStarted()) {
				camera.stop();
			}
		}
		debugLeaving(mn);
	}

	@Test
	public void testSetUp() throws Exception {
		String mn = debugEntering("testSetUp");
		debugLeaving(mn);
	}

	@Test
	public void testDryRun() throws Exception {
		String mn = debugEntering("testDryRun");
		camera.start();
		Thread.sleep(1999);
		debugLeaving(mn);
	}

}