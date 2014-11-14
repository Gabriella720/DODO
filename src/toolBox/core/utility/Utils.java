package toolBox.core.utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public final class Utils {
	/**
	 * Reads properties that inherit from three locations. Properties are first
	 * defined in the system resource location (i.e. in the CLASSPATH). These
	 * default properties must exist. Properties defined in the users home
	 * directory (optional) override default settings. Properties defined in the
	 * current directory (optional) override all these settings.
	 * 
	 * @param resourceName
	 *            the location of the resource that should be loaded. e.g.:
	 *            "weka/core/Utils.props". (The use of hardcoded forward slashes
	 *            here is OK - see jdk1.1/docs/guide/misc/resources.html) This
	 *            routine will also look for the file (in this case)
	 *            "Utils.props" in the users home directory and the current
	 *            directory.
	 * @return the Properties
	 * @exception Exception
	 *                if no default properties are defined, or if an error
	 *                occurs reading the properties files.
	 */
	public static Properties readProperties(String resourceName)
			throws Exception {

		Properties defaultProps = new Properties();
		try {
			// Apparently hardcoded slashes are OK here
			// jdk1.1/docs/guide/misc/resources.html
			// defaultProps.load(ClassLoader.getSystemResourceAsStream(resourceName));
			defaultProps.load((new Utils()).getClass().getClassLoader()
					.getResourceAsStream(resourceName));
		} catch (Exception ex) {
			/*
			 * throw new Exception("Problem reading default properties: " +
			 * ex.getMessage());
			 */
			System.err.println("Warning, unable to load properties file from "
					+ "system resource (Utils.java)");
		}

		// Hardcoded slash is OK here
		// eg: see jdk1.1/docs/guide/misc/resources.html
		int slInd = resourceName.lastIndexOf('/');
		if (slInd != -1) {
			resourceName = resourceName.substring(slInd + 1);
		}

		// Allow a properties file in the home directory to override
		Properties userProps = new Properties(defaultProps);
		File propFile = new File(System.getProperties()
				.getProperty("user.home") + File.separatorChar + resourceName);
		if (propFile.exists()) {
			try {
				userProps.load(new FileInputStream(propFile));
			} catch (Exception ex) {
				throw new Exception("Problem reading user properties: "
						+ propFile);
			}
		}

		// Allow a properties file in the current directory to override
		Properties localProps = new Properties(userProps);
		propFile = new File(resourceName);
		if (propFile.exists()) {
			try {
				localProps.load(new FileInputStream(propFile));
			} catch (Exception ex) {
				throw new Exception("Problem reading local properties: "
						+ propFile);
			}
		}

		return localProps;
	}

	/**
	 * get the location where the function was called
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
			if (trace[i].getClassName().equals(Utils.class.getName()))
				continue;

			// fill in result
			result[0] = trace[i].getClassName();
			result[1] = trace[i].getMethodName();
			result[2] = "" + trace[i].getLineNumber();
			break;
		}

		return result;
	}
}
