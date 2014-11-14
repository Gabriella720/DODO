
package toolBox.core.utility;

public class UtilityParameters {
	
	public enum ChildSystemsName{
		platform,datasets,algorithms,tasks;
		
		public static ChildSystemsName getChlidSystemsName(String systemName)
		{
			return valueOf(systemName);
		}
	}
	
	public final static int MAIN_BOARD_WIDTH  = 650;
	public final static int MAIN_BOARD_HEIGHT  = 450;
	
	public final static int CHILD_BOARD_WIDTH = 1000;
	public final static int CHILD_BOARD_HEIGHT = 600;
	
	public final static int SUB_WINDOW_WIDTH = 400;
	public final static int SUB_WINDOW_HEIGHT = 290;
	
	public final static int TAB_WINDOW_WIDTH = 950;
	public final static int TAB_WINDOW_HEIGHT = 550;
	
	public final static int CHILD_SYSTEMS_COUNTS = 4;
	
	public static final int REFRESH_TIME_INTERVAL = 50;
	
	public final static String IMAGE_PATH = "source/images/DODO-logo.png";
	public final static String IMAGE_FILE_PATH = "source/images/file.gif";
	public final static String IMAGE_FOLDER_PATH = "source/images/folder.gif";
	//public final static String IMAGE_PATH = "/source/images/DODO-logo.png";
	
	
}
