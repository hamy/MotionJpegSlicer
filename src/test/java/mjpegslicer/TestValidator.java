package mjpegslicer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestValidator extends AbstractTestCase {

	@Test
	public void testArgumentMustNotBeNull() {
		String mn = debugEntering("testArgumentMustNotBeNull");
		Object arg = new Object();
		Validator.argumentMustNotBeNull(mn, "arg", arg);
		arg = null;
		try {
			Validator.argumentMustNotBeNull(mn, "arg", arg);
			failedToThrowExpectedException(mn);
		} catch (NullPointerException npe) {
			foundExpectedException(mn, npe);
		}
		debugLeaving(mn);
	}

	@Test
	public void testArgumentMustNotBeEmpty() {
		String mn = debugEntering("testArgumentMustNotBeEmpty");
		String arg = "This is a non-empty argument.";
		Validator.argumentMustNotBeEmpty(mn, "arg", arg);
		arg = " ";
		Validator.argumentMustNotBeEmpty(mn, "arg", arg);
		arg = null;
		try {
			Validator.argumentMustNotBeEmpty(mn, "arg", arg);
			failedToThrowExpectedException(mn);
		} catch (NullPointerException npe) {
			foundExpectedException(mn, npe);
		}
		arg = "";
		try {
			Validator.argumentMustNotBeEmpty(mn, "arg", arg);
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		debugLeaving(mn);
	}

	@Test
	public void testTrimmedArgumentMustNotBeEmpty() {
		String mn = debugEntering("testTrimmedArgumentMustNotBeEmpty");
		String arg = "  abc   ";
		String result = Validator.trimmedArgumentMustNotBeEmpty(mn, "arg", arg);
		assertEquals("abc", result);
		arg = null;
		try {
			Validator.trimmedArgumentMustNotBeEmpty(mn, "arg", arg);
			failedToThrowExpectedException(mn);
		} catch (NullPointerException npe) {
			foundExpectedException(mn, npe);
		}
		arg = "";
		try {
			Validator.trimmedArgumentMustNotBeEmpty(mn, "arg", arg);
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		arg = " \t ";
		try {
			Validator.trimmedArgumentMustNotBeEmpty(mn, "arg", arg);
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		debugLeaving(mn);
	}

	@Test
	public void testCheckArgument() {
		String mn = debugEntering("testCheckArgument");
		int arg = 1;
		Validator.checkArgument(mn, "arg", arg, arg > 0,
				"Argument must be positive.");
		arg = -1;
		try {
			Validator.checkArgument(mn, "arg", arg, arg > 0,
					"Argument must be positive.");
			failedToThrowExpectedException(mn);
		} catch (IllegalArgumentException iae) {
			foundExpectedException(mn, iae);
		}
		debugLeaving(mn);
	}

	@Test
	public void testCheckState() {
		String mn = debugEntering("testCheckState");
		boolean arg = true;
		Validator.checkState(mn, arg, "Invalid state.");
		arg = false;
		try {
			Validator.checkState(mn, arg, "Invalid state.");
			failedToThrowExpectedException(mn);
		} catch (IllegalStateException ise) {
			foundExpectedException(mn, ise);
		}
		debugLeaving(mn);
	}
}
