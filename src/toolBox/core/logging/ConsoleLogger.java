package toolBox.core.logging;

import java.util.Date;
/**
 * a simple class for outputing the log information to the console
 * @author hadmin
 *
 */
public class ConsoleLogger extends Logger{

	protected ConsoleLogger()
	{
		super();
		initial();
	}
	
	/**
	 * the actual log function to log given message under given log level to the console
	 * @param level the given level
	 * @param message the given message
	 * @param className the class name where logging happened
	 * @param method the class name where the logging happened
	 * @param lineNumber the line number where the logging happened
	 */
	@Override
	protected void logMessage(LogLevel level, String message, String className,
			String method, String lineNumber) {
		stdoutRedirect.defaultPrintln(dateFormat.format(new Date())+" "+className + " " + method + "\n\t" 
	    		+ level + ": " + message);
	}
	
	/**
	 * Initializes the logger.
	 * Overwritting the abstract function in the superclass-Logger 
	 * Here do nothing  
	 */
	@Override
	protected void initial() {
		//doNothing
	}
	
	public static void main(String[] args)
	{
		Logger.log(LogLevel.INFO, "gogogogogog");
		System.out.println("gono");
	}
}
