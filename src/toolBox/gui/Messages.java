package toolBox.gui;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * a simple class for displaying specific messages for different locale
 * 
 * @author jiangkai
 * 
 */
public class Messages {
	static Locale locale = Locale.getDefault();
	static String packageLocation = Messages.class.getPackage().getName();
	static final String FILE_NAME = ".messages.messages";
	static ResourceBundle defaultResourceBundle;
	
	static 
	{
		try{
			defaultResourceBundle = ResourceBundle.getBundle(packageLocation + FILE_NAME + "_" + locale.getLanguage());
		}catch (MissingResourceException e) {
			try{
				defaultResourceBundle =  ResourceBundle.getBundle(packageLocation + FILE_NAME);
			}catch(MissingResourceException missingResourceException){
				defaultResourceBundle = null;
			}
		}
	}

	/**
	 * 
	 * @param key the key for searching the information
	 * @return
	 */
	public static String getString(String key) {
		return defaultResourceBundle==null?null:defaultResourceBundle.getString(key);
	}
	
	  /**
	   * getString.
	   * @param key the key for retrievaling the information
	   * @param locale the specific language where the application is applied
	   * @return
	   */
	  public static String getString(String key, Locale locale) {
	    try {
	      return ResourceBundle.getBundle(packageLocation + FILE_NAME + "_" + locale.getLanguage()).getString(key);
	    } catch (MissingResourceException e) {
	      try {
	        return ResourceBundle.getBundle(packageLocation + FILE_NAME).getString(key);
	      } catch (MissingResourceException missingResourceException) {
	        return null;
	      }
	    }
	  }
	  
	  /**
	   * for testing
	   * @param args
	   */
	  public static void main(String[] args)
	  {
		  System.out.println(getString("xy"));
		  System.out.println(getString("xy",new Locale("ch")));
	  }
}
