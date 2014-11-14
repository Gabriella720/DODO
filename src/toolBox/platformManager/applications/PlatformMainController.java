package toolBox.platformManager.applications;

import toolBox.core.system.ControllerInterface;

public interface PlatformMainController extends ControllerInterface{

	public boolean connect(String jobTrackerIP,String jobTrackerPort,String hdfsIP,String hdfsPort);
	
	public String[] excuteCommand(String command);
	
}
