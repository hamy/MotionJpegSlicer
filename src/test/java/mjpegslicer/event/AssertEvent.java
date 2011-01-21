package mjpegslicer.event;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

public class AssertEvent {

	// prevent instantiation
	private AssertEvent() {
	}

	public static void assertJpegEvent(Object expectedSource,
			long expectedCreationMillis, int expectedSequenceNumber,
			byte[] expectedImageData, JpegEvent actual) {
		assertNotNull("actual event reference must not be null.", actual);
		assertNotNull("expected source reference must not be null.",
				expectedSource);
		assertNotNull("actual source reference must not be null.",
				actual.getSource());
		assertSame("source reference mismatch: ", expectedSource,
				actual.getSource());
		assertEquals("creation time mismatch:", expectedCreationMillis,
				actual.getCreationMillis());
		assertNotNull("actual creation date must not be null.",
				actual.getCreationDate());
		assertEquals("creation date mismatch:",
				new Date(expectedCreationMillis), actual.getCreationDate());
		assertEquals("sequence number mismatch:", expectedSequenceNumber,
				actual.getSequenceNumber());
		byte[] actualData = actual.getImageData();
		assertNotNull("actual image data must not be null.", actualData);
		assertEquals("image length mismatch:", expectedImageData.length,
				actual.getImageLength());
		for (int i = 0; i < expectedImageData.length; i++) {
			assertEquals("mismatch at data byte #" + i + ";",
					expectedImageData[i], actualData[i]);
		}
	}

}
