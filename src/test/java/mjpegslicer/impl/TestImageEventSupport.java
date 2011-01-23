package mjpegslicer.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import mjpegslicer.AbstractTestCase;
import mjpegslicer.ImageEvent;
import mjpegslicer.ImageListener;
import mjpegslicer.impl.ImageEventSupport;

public class TestImageEventSupport extends AbstractTestCase {

	private ImageEvent event;
	private ImageEventSupport support;
	private MyListener listener1, listener2;

	@Before
	public void setUp() throws Exception {
		String mn = debugEntering("setUp");
		InputStream is = getResourceLeafAsStream(".jpg");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (true) {
			int b = is.read();
			if (b < 0)
				break;
			baos.write(b);
		}
		is.close();
		baos.flush();
		byte[] data = baos.toByteArray();
		baos.close();
		event = new ImageEvent(this, System.currentTimeMillis(), 123, data);
		debug(mn, "event: ", event);
		support = new ImageEventSupport();
		debug(mn, "support: ", support);
		listener1 = new MyListener();
		listener2 = new MyListener();
		debugLeaving(mn);
	}

	@Test
	public void testSetUp() {
		String mn = debugEntering("testSetUp");
		assertNotNull(event);
		assertNotNull(support);
		assertEquals(0, support.countImageListeners());
		assertNotNull(listener1);
		assertEquals(0, listener1.count);
		assertNotNull(listener2);
		assertEquals(0, listener2.count);
		debugLeaving(mn);
	}

	@Test
	public void testAddValidListeners() {
		String mn = debugEntering("testAddValidListeners");
		assertEquals(0, support.countImageListeners());
		support.addImageListener(listener1);
		assertEquals(1, support.countImageListeners());
		support.addImageListener(listener2);
		assertEquals(2, support.countImageListeners());
		debugLeaving(mn);
	}

	@Test
	public void testRemoveValidListeners() {
		String mn = debugEntering("testRemoveValidListeners");
		support.addImageListener(listener1);
		support.addImageListener(listener2);
		assertEquals(2, support.countImageListeners());
		support.removeImageListener(listener1);
		assertEquals(1, support.countImageListeners());
		support.removeImageListener(listener2);
		assertEquals(0, support.countImageListeners());
		debugLeaving(mn);
	}

	@Test
	public void testAddNullListeners() {
		String mn = debugEntering("testAddNullListeners");
		assertEquals(0, support.countImageListeners());
		support.addImageListener(null);
		assertEquals(0, support.countImageListeners());
		support.addImageListener(listener1);
		assertEquals(1, support.countImageListeners());
		support.addImageListener(null);
		assertEquals(1, support.countImageListeners());
		debugLeaving(mn);
	}

	@Test
	public void testRemoveNullListeners() {
		String mn = debugEntering("testRemoveNullListeners");
		assertEquals(0, support.countImageListeners());
		support.removeImageListener(null);
		assertEquals(0, support.countImageListeners());
		support.addImageListener(listener1);
		assertEquals(1, support.countImageListeners());
		support.removeImageListener(null);
		assertEquals(1, support.countImageListeners());
		debugLeaving(mn);
	}

	@Test
	public void testAddSameListenerTwice() {
		String mn = debugEntering("testAddSameListenerTwice");
		assertEquals(0, support.countImageListeners());
		support.addImageListener(listener1);
		assertEquals(1, support.countImageListeners());
		support.addImageListener(listener1);
		assertEquals(1, support.countImageListeners());
		support.addImageListener(listener2);
		assertEquals(2, support.countImageListeners());
		support.addImageListener(listener2);
		assertEquals(2, support.countImageListeners());
		debugLeaving(mn);
	}

	@Test
	public void testRemoveSameListenerTwice() {
		String mn = debugEntering("testRemoveSameListenerTwice");
		support.addImageListener(listener1);
		support.addImageListener(listener2);
		assertEquals(2, support.countImageListeners());
		support.removeImageListener(listener1);
		assertEquals(1, support.countImageListeners());
		support.removeImageListener(listener1);
		assertEquals(1, support.countImageListeners());
		support.removeImageListener(listener2);
		assertEquals(0, support.countImageListeners());
		support.removeImageListener(listener2);
		assertEquals(0, support.countImageListeners());
		debugLeaving(mn);
	}

	@Test
	public void testEventDistribution() {
		String mn = debugEntering("testEventDistribution");
		support.newImage(event);
		assertEquals(0, listener1.count);
		assertEquals(0, listener2.count);
		support.newImage(event);
		assertEquals(0, listener1.count);
		assertEquals(0, listener2.count);
		support.addImageListener(listener1);
		support.newImage(event);
		assertEquals(1, listener1.count);
		assertEquals(0, listener2.count);
		support.addImageListener(listener2);
		support.newImage(event);
		assertEquals(2, listener1.count);
		assertEquals(1, listener2.count);
		support.removeImageListener(listener2);
		support.newImage(event);
		assertEquals(3, listener1.count);
		assertEquals(1, listener2.count);
		support.removeImageListener(listener1);
		support.newImage(event);
		assertEquals(3, listener1.count);
		assertEquals(1, listener2.count);
		debugLeaving(mn);
	}

	private class MyListener implements ImageListener {

		private int count;

		@Override
		public void newImage(ImageEvent event) {
			count++;
		}
	}
}
