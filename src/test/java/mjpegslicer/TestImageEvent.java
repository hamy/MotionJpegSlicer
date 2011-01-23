package mjpegslicer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static mjpegslicer.AssertImageEvent.assertImageEvent;

import mjpegslicer.ImageDataCorruptionException;
import mjpegslicer.ImageEvent;

public class TestImageEvent extends AbstractTestCase {

	private Object setUpSource;
	private long setUpCreationMillis;
	private int setUpSequenceNumber;
	private byte[] setUpImageData;
	private ImageEvent setUpEvent;

	private void setUpReadImageData(String suffix) throws Exception {
		String mn = debugEntering("readImageData", "suffix: ", suffix);
		InputStream is = getResourceLeafAsStream(suffix);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (true) {
			int b = is.read();
			if (b < 0)
				break;
			baos.write(b);
		}
		is.close();
		baos.flush();
		setUpImageData = baos.toByteArray();
		baos.close();
		debugLeaving(mn, "byte array size: ", setUpImageData.length);
	}

	@Before
	public void setUp() throws Exception {
		String mn = debugEntering("setUp");
		setUpSource = this;
		setUpCreationMillis = System.currentTimeMillis();
		setUpSequenceNumber = 123;
		setUpReadImageData(".jpg");
		setUpEvent = new ImageEvent(setUpSource, setUpCreationMillis,
				setUpSequenceNumber, setUpImageData);
		debugLeaving(mn, "event: ", setUpEvent);
	}

	@Test
	public void testSetUp() {
		String mn = debugEntering("testSetUp");
		assertImageEvent(setUpSource, setUpCreationMillis, setUpSequenceNumber,
				setUpImageData, setUpEvent);
		debugLeaving(mn);
	}

	@Test
	public void testImageCreation() {
		String mn = debugEntering("testImageCreation");
		BufferedImage bi = setUpEvent.createBufferedImage();
		debug(mn, "image: ", bi);
		assertNotNull(bi);
		assertEquals(640, bi.getWidth());
		assertEquals(480, bi.getHeight());
		debugLeaving(mn);
	}

	@Test
	public void testConstructorWithNullImageData() {
		String mn = debugEntering("testConstructorWithNullImageData");
		try {
			setUpEvent = new ImageEvent(setUpSource, setUpCreationMillis,
					setUpSequenceNumber, null);
			failedToThrowExpectedException(mn);
		} catch (ImageDataCorruptionException idce) {
			foundExpectedException(mn, idce);
		}
		debugLeaving(mn);
	}

	@Test
	public void testConstructorWithEmptyImageData() throws Exception {
		String mn = debugEntering("testConstructorWithEmptyImageData");
		setUpReadImageData("-no-data.jpg");
		try {
			setUpEvent = new ImageEvent(setUpSource, setUpCreationMillis,
					setUpSequenceNumber, setUpImageData);
			failedToThrowExpectedException(mn);
		} catch (ImageDataCorruptionException idce) {
			foundExpectedException(mn, idce);
		}
		debugLeaving(mn);
	}

	@Test
	public void testImageCreationWithCorruptedImageData() throws Exception {
		String mn = debugEntering("testImageCreationWithCorruptedImageData");
		setUpReadImageData("-corrupted-data.jpg");
		setUpEvent = new ImageEvent(setUpSource, setUpCreationMillis,
				setUpSequenceNumber, setUpImageData);
		try {
			setUpEvent.createBufferedImage();
			failedToThrowExpectedException(mn);
		} catch (ImageDataCorruptionException idce) {
			foundExpectedException(mn, idce);
		}
		debugLeaving(mn);
	}
}
