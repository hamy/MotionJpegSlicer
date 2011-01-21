package mjpegslicer;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.StringTokenizer;

/**
 * The base class for the test cases of this project.
 */
public abstract class AbstractTestCase extends LoggableObject {

	/**
	 * Returns a resource as stream. The resource path is derived from the
	 * fully-qualified class name of the test case and a suffix text.
	 * 
	 * @param suffix
	 *            The suffix text.
	 * @return The stream.
	 */
	public InputStream getResourceLeafAsStream(String suffix) {
		String fqcn = getFullyQualifiedClassName();
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(fqcn, ".");
		while (st.hasMoreTokens()) {
			sb.append('/');
			sb.append(st.nextToken());
		}
		sb.append(suffix);
		String path = sb.toString();
		InputStream result = getClass().getResourceAsStream(path);
		assertNotNull(result);
		return result;
	}

	/**
	 * Reports that an expected exception was caught.
	 * 
	 * @param methodName
	 *            The name of the method that generated this message.
	 * @param ta
	 *            The exception.
	 */
	public void foundExpectedException(String methodName, Throwable ta) {
		debug(methodName, "found expected exception: ", ta);
	}

	/**
	 * Reports that an expected exception was NOT thrown.
	 * 
	 * @param methodName
	 *            The name of the method that generated this message.
	 */
	public void failedToThrowExpectedException(String methodName) {
		fail("Failed to throw expected exception in method " + methodName);
	}
}
