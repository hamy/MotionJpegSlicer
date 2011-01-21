package mjpegslicer;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A base class that provides logging methods. The logger is derived from the
 * fully-qualified class name of the object.
 */
public class LoggableObject {

	/** 
	 * The "method name" for a constructor.
	 */
	public static final String MN_INIT = "<<INIT>>";
	
	private enum Level {
		ERROR, WARN, INFO, DEBUG
	}

	private transient Logger logger;

	private final String fullyQualifiedClassName;

	/**
	 * Returns the fully-qualified class name of this object.
	 * 
	 * @return The name.
	 */
	public String getFullyQualifiedClassName() {
		return fullyQualifiedClassName;
	}

	private final String simpleClassName;

	/**
	 * Returns the simple class name of this object; i.e., the class name
	 * without the package name.
	 * 
	 * @return The name;
	 */
	public String getSimpleClassName() {
		return simpleClassName;
	}

	/**
	 * Creates an instance of this class.
	 */
	public LoggableObject() {
		fullyQualifiedClassName = getClass().getName();
		String s = fullyQualifiedClassName;
		StringTokenizer st = new StringTokenizer(s, ".");
		while (st.hasMoreTokens()) {
			s = st.nextToken();
		}
		simpleClassName = s;
	}

	private void accessLogger() {
		if (logger == null) {
			logger = LoggerFactory.getLogger(fullyQualifiedClassName);
		}
	}

	private boolean isLevelEnabled(Level level) {
		switch (level) {
		case ERROR:
			return logger.isErrorEnabled();
		case WARN:
			return logger.isWarnEnabled();
		case INFO:
			return logger.isInfoEnabled();
		case DEBUG:
		default: // stupid compiler needs that clause
			return logger.isDebugEnabled();
		}
	}

	private String assembleMessage(String methodName, String methodDetails,
			Object[] messageParts) {
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		sb.append(methodName);
		if (methodDetails != null) {
			sb.append('/');
			sb.append(methodDetails);
		}
		sb.append("] ");
		for (Object messagePart : messageParts) {
			sb.append(messagePart);
		}
		return sb.toString();
	}

	private void publishMessage(String message, Level level) {
		switch (level) {
		case ERROR:
			logger.debug(message);
			break;
		case WARN:
			logger.warn(message);
			break;
		case INFO:
			logger.info(message);
			break;
		case DEBUG:
			logger.debug(message);
			break;
		}
	}

	private void processMessageParts(String methodName, String methodDetails,
			Level level, Object[] messageParts) {
		accessLogger();
		if (isLevelEnabled(level)) {
			String message = assembleMessage(methodName, methodDetails,
					messageParts);
			publishMessage(message, level);
		}
	}

	/**
	 * General-purpose logging method for Level=DEBUG
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 */
	public void debug(String methodName, Object... messageParts) {
		processMessageParts(methodName, null, Level.DEBUG, messageParts);
	}

	/**
	 * Logging method for Level=DEBUG that is called at the beginning of a
	 * method.
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 * @return The name of the method.
	 */
	public String debugEntering(String methodName, Object... messageParts) {
		processMessageParts(methodName, "Entering", Level.DEBUG, messageParts);
		return methodName;
	}

	/**
	 * Logging method for Level=DEBUG that is called at the end of a method.
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 * @return The name of the method.
	 */
	public void debugLeaving(String methodName, Object... messageParts) {
		processMessageParts(methodName, "Leaving", Level.DEBUG, messageParts);
	}

	/**
	 * General-purpose logging method for Level=INFO
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 */
	public void info(String methodName, Object... messageParts) {
		processMessageParts(methodName, null, Level.INFO, messageParts);
	}

	/**
	 * Logging method for Level=INFO that is called at the beginning of a
	 * method.
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 * @return The name of the method.
	 */
	public String infoEntering(String methodName, Object... messageParts) {
		processMessageParts(methodName, "Entering", Level.INFO, messageParts);
		return methodName;
	}

	/**
	 * Logging method for Level=INFO that is called at the end of a method.
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 * @return The name of the method.
	 */
	public void infoLeaving(String methodName, Object... messageParts) {
		processMessageParts(methodName, "Leaving", Level.INFO, messageParts);
	}

	/**
	 * General-purpose logging method for Level=WARN
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 */
	public void warn(String methodName, Object... messageParts) {
		processMessageParts(methodName, null, Level.WARN, messageParts);
	}

	/**
	 * Logging method for Level=WARN that is called at the beginning of a
	 * method.
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 * @return The name of the method.
	 */
	public String warnEntering(String methodName, Object... messageParts) {
		processMessageParts(methodName, "Entering", Level.WARN, messageParts);
		return methodName;
	}

	/**
	 * Logging method for Level=WARN that is called at the end of a method.
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 * @return The name of the method.
	 */
	public void warnLeaving(String methodName, Object... messageParts) {
		processMessageParts(methodName, "Leaving", Level.WARN, messageParts);
	}

	/**
	 * General-purpose logging method for Level=ERROR
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 */
	public void error(String methodName, Object... messageParts) {
		processMessageParts(methodName, null, Level.ERROR, messageParts);
	}

	/**
	 * Logging method for Level=ERROR that is called at the beginning of a
	 * method.
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 * @return The name of the method.
	 */
	public String errorEntering(String methodName, Object... messageParts) {
		processMessageParts(methodName, "Entering", Level.ERROR, messageParts);
		return methodName;
	}

	/**
	 * Logging method for Level=ERROR that is called at the end of a method.
	 * 
	 * @param methodName
	 *            The name of the method that sends the message.
	 * @param messageParts
	 *            The parts of the message.
	 * @return The name of the method.
	 */
	public void errorLeaving(String methodName, Object... messageParts) {
		processMessageParts(methodName, "Leaving", Level.ERROR, messageParts);
	}
}
