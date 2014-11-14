package toolBox.core.logging;

import toolBox.core.utility.IORedirect;
import toolBox.core.utility.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * Abstract superclass for all loggers and provide static log functions if some
 * information is to be logged just do like this: Logger.log(LogLevel
 * level,String message);
 * 
 * the properties of this log function are in the logging.props file there
 * 
 * @author jiangkai
 * 
 */
public abstract class Logger {

	/**
	 * the logging level
	 * 
	 * @author hadmin
	 * 
	 */
	enum LogLevel {
		/** log all messages */
		ALL(0),

		/** FINE level */
		FINE(1),

		/** INFO level */
		INFO(2),

		/** WARNNING level */
		WARNNING(3),

		/** ERROR level */
		ERROR(4),

		/** SERVER level */
		SERVER(5),

		/** turns logging off */
		OFF(6);

		/** the order of the level */
		int m_Order;

		/**
		 * Initializes the level
		 * 
		 * @param order
		 *            the order of the level
		 */
		LogLevel(int order) {
			m_Order = order;
		}

		/**
		 * Returns the order of this level
		 * 
		 * @return the order
		 */
		public int getOrder() {
			return m_Order;
		}
	}

	/** for indicating whether the logging function is being used */
	protected static boolean functionOn = false;

	/** the logging properties file */
	protected static Properties logProperties;

	/** a pipeline for redirecting the standout stream to special output streams */
	protected static IORedirect stdoutRedirect = null;

	/** a pipeline for redirecting the standerr stream to special error streams */
	protected static IORedirect stderrRedirect = null;

	/** the logger that log information to a specific logfile */
	protected static FileLogger fileLogger = null;

	/** the logger get the log information and output them to the console */
	protected static ConsoleLogger consoleLogger = null;

	/**
	 * the log that get the log information and output it to the log window for
	 * user to monitor
	 */
	protected static WindowLogger windowLogger = null;

	/** the minimum level of log events to have in order to end up in the log */
	protected static LogLevel minLevel = null;

	/** just for formatting the dates */
	protected static SimpleDateFormat dateFormat = null;

	static {
		// the properties file path
		String propFilePath = "toolBox/core/logging/logging.props";
		try {
			logProperties = Utils.readProperties(propFilePath);
		} catch (Exception e) {
			System.err.println("Error reading the logging properties '"
					+ propFilePath + "': " + e);
			logProperties = new Properties();
		}

		functionOn = PropertyCompare("function", "off", "on");
		if (functionOn) {
			minLevel = LogLevel.valueOf(logProperties.getProperty("MinLevel",
					"INFO"));

			dateFormat = new SimpleDateFormat(logProperties.getProperty(
					"DateFormat", "yyyy-MM-dd HH:mm:ss"));

			if (PropertyCompare("StdoutRedirect", "1", "1")) {
				stdoutRedirect = new IORedirect(System.out, "stdout");
				System.setOut(stdoutRedirect);
			}

			if (PropertyCompare("stderrRedirect", "1", "1")) {
				stderrRedirect = new IORedirect(System.err, "stderr");
				System.setErr(stderrRedirect);			
			}

			if (PropertyCompare("fileLogger", "1", "1"))
				fileLogger = new FileLogger();

			if (PropertyCompare("consoleLogger", "1", "1"))
				consoleLogger = new ConsoleLogger();

			if (PropertyCompare("windowLogger", "1", "1"))
				windowLogger = new WindowLogger();
		}
	}

	/**
	 * for comparing the value in the properties file with the expected value
	 * 
	 * @param key
	 * @param defaultValue
	 * @param expectValue
	 * @return
	 */
	private static boolean PropertyCompare(String key, String defaultValue,
			String expectValue) {
		return expectValue.compareTo(logProperties.getProperty(key,
				defaultValue)) == 0 ? true : false;
	}

	/**
	 * get the location where the logging happened
	 * 
	 * @return the classname(=[0]), the method(=[1]) and the line number(=[2])
	 *         that generated the logging event
	 */
	protected static String[] getLocation() {
		String[] result;
		Throwable t;
		StackTraceElement[] trace;
		int i;

		result = new String[3];

		t = new Throwable();
		t.fillInStackTrace();
		trace = t.getStackTrace();

		for (i = 0; i < trace.length; i++) {
			// skip the Logger class
			if (trace[i].getClassName().equals(Logger.class.getName()))
				continue;

			// skip the Logger window
			if (trace[i].getClassName().equals(
					toolBox.gui.LogWindow.class.getName()))
				continue;

			// fill in result
			result[0] = trace[i].getClassName();
			result[1] = trace[i].getMethodName();
			result[2] = "" + trace[i].getLineNumber();
			break;
		}

		return result;
	}

	/**
	 * log the given message under the default log level-LogLevel.INFO
	 * 
	 * @param message
	 *            the message to log
	 */
	public static void log(String message) {
		log(LogLevel.INFO, message);
	}

	/**
	 * log the given message under the given level
	 * 
	 * @param level
	 *            the given log level
	 * @param message
	 *            the given message
	 */
	public static void log(LogLevel level, String message) {
		if (functionOn) {
			if (level.getOrder() >= minLevel.getOrder()) {
				String[] location = getLocation();
				if (fileLogger != null)
					fileLogger.logMessage(level, message, location[0],
							location[1], location[2]);
				if (consoleLogger != null)
					consoleLogger.logMessage(level, message, location[0],
							location[1], location[2]);
				if (windowLogger != null)
					windowLogger.logMessage(level, message, location[0],
							location[1], location[2]);
			}
		}
	}

	/**
	 * log the given message under the given log level
	 * 
	 * @param level
	 *            the log level of the message
	 * @param t
	 *            the throwable to log
	 */
	public static void log(LogLevel level, Throwable t) {
		StringWriter swriter = new StringWriter();
		PrintWriter pwriter = new PrintWriter(swriter);
		t.printStackTrace(pwriter);

		log(level, swriter.toString());

		try {
			pwriter.close();
			swriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * get the minimum log level
	 * 
	 * @return the lowest log level this logger would log
	 */
	public static LogLevel getMinLevel() {
		return minLevel;
	}

	/**
	 * set the log window visiable
	 * 
	 * @return true if there is a window logger false if the window logger is
	 *         null
	 */

	public static boolean setLogWindowVisiable() {
		if (windowLogger != null) {
			windowLogger.setWindowVisiable();
			return true;
		} else
			return false;
	}

	/**
	 * actual logger implementations must override this method to initialize the
	 * actual logger
	 */
	protected abstract void initial();

	/**
	 * for subclass to implement to do the actual logging actual logger
	 * implementations must override this method
	 * 
	 * @param level
	 *            the log level of the message
	 * @param message
	 *            the message to log
	 * @param className
	 *            the classname originating the log event
	 * @param method
	 *            the method originating the log event
	 * @param lineNumber
	 *            the line number originating the log event
	 */
	protected abstract void logMessage(LogLevel level, String message,
			String className, String method, String lineNumber);

	public static void main(String[] args) {
		System.err.println("gogogogogog");
		// Logger.log(LogLevel.INFO, "gogogogogog");
	}
}
