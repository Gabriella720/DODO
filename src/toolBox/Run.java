
package toolBox;

import toolBox.core.logging.Logger;
import toolBox.core.system.DodoSystem;

import java.io.IOException;

class Run{
	public static void main(String[] args) throws IOException
	{
		Logger.log("System start!");
		DodoSystem.getDodoSystem().start();
	}
}