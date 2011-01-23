package mjpegslicer.util;

/**
 * A helper class that provides several static validation methods.
 */
public class Validator {

	private static String assembleM(String methodName, Object... objects) {
		StringBuilder sb = new StringBuilder("Validation error in method \"");
		sb.append(methodName);
		sb.append("\": ");
		for (Object object : objects) {
			sb.append(object);
		}
		return sb.toString();
	}

	private static String assembleMA(String methodName, String argumentName,
			Object... objects) {
		StringBuilder sb = new StringBuilder("Validation error in method \"");
		sb.append(methodName);
		sb.append("\": argument \"");
		sb.append(argumentName);
		sb.append("\" ");
		for (Object object : objects) {
			sb.append(object);
		}
		return sb.toString();
	}

	private static String assembleMAV(String methodName, String argumentName,
			Object value, Object... objects) {
		StringBuilder sb = new StringBuilder("Validation error in method \"");
		sb.append(methodName);
		sb.append("\": argument \"");
		sb.append(argumentName);
		sb.append("\" = ");
		sb.append(value);
		sb.append(": ");
		for (Object object : objects) {
			sb.append(object);
		}
		return sb.toString();
	}

	/**
	 * Checks that an argument is not null.
	 * 
	 * @param methodName
	 *            The name of the method that performs this check.
	 * @param argumentName
	 *            The name of the argument that is checked.
	 * @param arg
	 *            The argument that is checked.
	 * @throws NullPointerException
	 *             if the condition is not met.
	 */
	public static void argumentMustNotBeNull(String methodName,
			String argumentName, Object arg) {
		if (arg == null) {
			throw new NullPointerException(assembleMA(methodName, argumentName,
					" must not be null."));
		}
	}

	/**
	 * Checks that an argument string is not empty.
	 * 
	 * @param methodName
	 *            The name of the method that performs this check.
	 * @param argumentName
	 *            The name of the argument that is checked.
	 * @param arg
	 *            The argument that is checked.
	 * @throws NullPointerException
	 *             if the condition is not met.
	 * @throws IllegalArgumentException
	 *             if the condition is not met.
	 */
	public static void argumentMustNotBeEmpty(String methodName,
			String argumentName, String arg) {
		argumentMustNotBeNull(methodName, argumentName, arg);
		if (arg.equals("")) {
			throw new IllegalArgumentException(assembleMA(methodName,
					argumentName, " must not be empty"));
		}
	}

	/**
	 * Checks that an trimmed argument string is not empty.
	 * 
	 * @param methodName
	 *            The name of the method that performs this check.
	 * @param argumentName
	 *            The name of the argument that is checked.
	 * @param arg
	 *            The argument that is checked. Heading and trailing white-space
	 *            is removed.
	 * @return The trimmed argument.
	 * @throws NullPointerException
	 *             if the condition is not met.
	 * @throws IllegalArgumentException
	 *             if the condition is not met.
	 */
	public static String trimmedArgumentMustNotBeEmpty(String methodName,
			String argumentName, String arg) {
		argumentMustNotBeNull(methodName, argumentName, arg);
		arg = arg.trim();
		argumentMustNotBeEmpty(methodName, argumentName, arg);
		return arg;
	}

	/**
	 * Check an argument.
	 * 
	 * @param methodName
	 *            The name of the method that performs this check.
	 * @param argumentName
	 *            The name of the argument that is checked.
	 * @param arg
	 *            The argument value.
	 * @param condition
	 *            The condition that is checked.
	 * @throws IllegalArgumentException
	 *             if the condition is not met.
	 */
	public static void checkArgument(String methodName, String argumentName,
			Object arg, boolean condition, Object... objects) {
		if (!condition) {
			throw new IllegalArgumentException(assembleMAV(methodName,
					argumentName, arg, objects));
		}
	}

	/**
	 * Check the state.
	 * 
	 * @param methodName
	 *            The name of the method that performs this check.
	 * @param arg
	 *            The condition that is checked.
	 * @throws IllegalStateException
	 *             if the condition is not met.
	 */
	public static void checkState(String methodName, boolean arg,
			Object... objects) {
		if (!arg) {
			throw new IllegalStateException(assembleM(methodName, objects));
		}
	}
}
