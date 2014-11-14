package toolBox.core.logging;

import org.eclipse.swt.widgets.Text;
import toolBox.gui.LogWindow;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;

/**
 * A simple window logger for capturing log information and standard out or err
 * and then show them in the log window
 * 
 * @author hadmin
 * 
 */
public class WindowLogger extends Logger {

	/** The GUI log window to show the log information to the users */
	protected LogWindow logWindow = null;

	
	/**
	 * A target stream for redirecting the standard out or err stream to the log
	 * window
	 */
	protected WindowStream m_windowStream = null;

	/**
	 * a stream for capturing standard out or err stream to output to the log
	 * window
	 * 
	 * @author jiangkai
	 * 
	 */
	public static class WindowStream extends PrintStream {

		protected Text textArea = null;

		public WindowStream(PrintStream ps, Text ta) {
			super(ps);
			textArea = ta;
		}

		/**
		 * flushes the printstream
		 */
		public synchronized void flush() {
			// ignored
		}

		/**
		 * prints the given int
		 */
		public synchronized void print(int x) {
			print(new Integer(x).toString());
		}

		/**
		 * prints the given boolean
		 */
		public synchronized void print(boolean x) {
			print(new Boolean(x).toString());
		}

		/**
		 * prints the given string
		 */
		public synchronized void print(String x) {
			textArea.insert(x);
		}

		/**
		 * prints the given object
		 */
		public synchronized void print(Object x) {
			String line;
			Throwable t;
			StackTraceElement[] trace;
			int i;

			if (x instanceof Throwable) {
				t = (Throwable) x;
				trace = t.getStackTrace();
				line = t.getMessage() + "\n";
				for (i = 0; i < trace.length; i++)
					line += "\t" + trace[i].toString() + "\n";
				x = line;
			}

			if (x == null)
				print("null");
			else
				print(x.toString());
		}

		/**
		 * prints a new line
		 */
		public synchronized void println() {
			print("\n");
		}

		/**
		 * prints the given int
		 */
		public synchronized void println(int x) {
			print(x);
			println();
		}

		/**
		 * prints the given boolean
		 */
		public synchronized void println(boolean x) {
			print(x);
			println();
		}

		/**
		 * prints the given string
		 */
		public synchronized void println(String x) {
			print(x);
			println();
		}

		/**
		 * prints the given object (for Throwables we print the stack trace)
		 */
		public synchronized void println(Object x) {
			print(x);
			println();
		}
	}

	protected WindowLogger() {
		super();
		initial();
	}

	/**
	 * the actual log function to log given message under given log level to the
	 * log window
	 * 
	 * @param level
	 *            the given level
	 * @param message
	 *            the given message
	 * @param className
	 *            the class name where logging happened
	 * @param method
	 *            the class name where the logging happened
	 * @param lineNumber
	 *            the line number where the logging happened
	 */
	@Override
	protected void logMessage(LogLevel level, String message, String className,
			String method, String lineNumber) {
		logWindow.getTextArea().insert(
				dateFormat.format(new Date()) + " " + className + " " + method
						+ "\n\t" + level + ": " + message + "\n");
	}

	/**
	 * Initializes the logger. Overwritting the abstract function in the
	 * superclass-Logger
	 * 
	 */
	@Override
	protected void initial() {
		logWindow = new LogWindow();
		try {
			m_windowStream = new WindowStream(new PrintStream(new FileOutputStream("default")), logWindow.getTextArea());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		stdoutRedirect.addPrintStream(m_windowStream, true);
		stderrRedirect.addPrintStream(m_windowStream, true);
	}

	protected void setWindowVisiable() {
		if (logWindow != null)
			logWindow.setVisible(true);
	}

}
