package toolBox.core.logging;

import java.io.*;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * A simple file logger, that just logs to a single file.
 * @author jiangkai
 *
 */
public class FileLogger extends Logger {

	public static class FileStream extends PrintStream {

		FileLogger fileLogger = null;

		protected FileStream(PrintStream s, FileLogger fl) {
			super(s);
			fileLogger = fl;
		}

		/**
		 * ignored.
		 */
		public void flush() {
		}

		/**
		 * prints the given int to the streams.
		 * 
		 * @param x
		 *            the object to print
		 */
		public void print(int x) {
			fileLogger.append("" + x);
		}

		/**
		 * prints the given boolean to the streams.
		 * 
		 * @param x
		 *            the object to print
		 */
		public void print(boolean x) {
			fileLogger.append("" + x);
		}

		/**
		 * prints the given string to the streams.
		 * 
		 * @param x
		 *            the object to print
		 */
		public void print(String x) {
			fileLogger.append("" + x);
		}

		/**
		 * prints the given object to the streams.
		 * 
		 * @param x
		 *            the object to print
		 */
		public void print(Object x) {
			fileLogger.append("" + x);
		}

		/**
		 * prints a new line to the streams.
		 */
		public void println() {
			fileLogger.append("\n");
		}

		/**
		 * prints the given int to the streams.
		 * 
		 * @param x
		 *            the object to print
		 */
		public void println(int x) {
			fileLogger.append(x + "\n");
		}

		/**
		 * prints the given boolean to the streams.
		 * 
		 * @param x
		 *            the object to print
		 */
		public void println(boolean x) {
			fileLogger.append(x + "\n");
		}

		/**
		 * prints the given string to the streams.
		 * 
		 * @param x
		 *            the object to print
		 */
		public void println(String x) {
			fileLogger.append(x + "\n");
		}

		/**
		 * prints the given object to the streams (for Throwables we print the
		 * stack trace).
		 * 
		 * @param x
		 *            the object to print
		 */
		public void println(Object x) {
			fileLogger.append(x + "\n");
		}
	}

	/**the file stream to be the target stream for redirecting the standard out stream or the standard err stream*/
	protected FileStream m_FileStream = null;

	/**the external file for logging */
	protected File m_LogFile = null;

	
	protected FileLogger() {
		super();
		initial();
	}

	/**
	 * the actual log function to log given message under given log level to the log file
	 * @param level the given level
	 * @param message the given message
	 * @param className the class name where logging happened
	 * @param method the class name where the logging happened
	 * @param lineNumber the line number where the logging happened
	 */
	@Override
	protected void logMessage(LogLevel level, String message, String className,
			String method, String lineNumber) {
		append(dateFormat.format(new Date()) + " " + className + " " + method
				+ "\n\t" + level + ": " + message + "\n");
	}

	/**
	 * Appends the given string to the log file (without new line!).
	 * 
	 * @param s
	 *            the string to append
	 */
	protected void append(String s) {
		BufferedWriter writer;

		if (m_LogFile == null)
			return;

		// append output to file
		try {
			writer = new BufferedWriter(new FileWriter(m_LogFile, true));
			writer.write(s);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			// ignored
		}
	}

	/**
	 * Initializes the logger.
	 * Overwritting the abstract function in the superclass-Logger   
	 */
	@Override
	protected void initial() {
		String filePath = logProperties.getProperty("LogFilePath",
				"%h/toolBox_%d.log");
		filePath = filePath.replace("%h",
				Matcher.quoteReplacement(System.getProperty("user.home")));
		filePath = filePath.replace("%d",
				Matcher.quoteReplacement(dateFormat.format(new Date())));
		m_LogFile = new File(filePath);
		
		
		try {
			m_FileStream = new FileStream(new PrintStream(new FileOutputStream(m_LogFile)), this);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		stdoutRedirect.addPrintStream(m_FileStream,true);
		stderrRedirect.addPrintStream(m_FileStream,true);
	}

	public static void main(String[] args) {
		Logger.log("gogogo");
		System.err.println("error");
		Logger.log("no1no2");
		System.out.println("just do it");
	}
}
