package toolBox.core.system;

import toolBox.algorithmsManager.applications.AlgorithmsMainController;
import toolBox.algorithmsManager.applications.AlgorithmsMainControllerImp;
import toolBox.core.logging.Logger;
import toolBox.core.utility.UtilityParameters.ChildSystemsName;
import toolBox.datasetsManager.applications.DatasetsMainController;
import toolBox.datasetsManager.applications.DatasetsMainControllerImp;
import toolBox.gui.MainBoard;
import toolBox.platformManager.applications.PlatformMainController;
import toolBox.platformManager.applications.PlatformMainControllerImp;
import toolBox.tasksManager.applications.TasksMainController;


public class MainController implements ControllerInterface{
	
	static MainController mainController = null;
	MainBoard mainBoard = null;
	
	Thread platformThread = null;
	Thread datasetsThread = null;
	Thread algorithmsThread = null;
	Thread tasksThread = null;
	
	PlatformMainController platformController = null;
	DatasetsMainController datasetsController = null;
	AlgorithmsMainController algorithmsController = null;
	TasksMainController tasksController = null;

	private MainController()
	{
	}
	
	public static MainController getMainController()
	{
		if(mainController==null)
			mainController = new MainController();
		return mainController;
	}
	
	public void start()
	{
		mainBoard = new MainBoard(mainController);
		mainBoard.work();
	}
	
	public void createChildSystem(ChildSystemsName systemName)
	{
		switch(systemName)
		{
		case platform:
			Logger.log("Platform sub-system has been set up!");
			platformController = PlatformMainControllerImp.getPlatformMainController();
			platformThread = new Thread(platformController);
			platformThread.run();
			break;
		case datasets:
			datasetsController = DatasetsMainControllerImp.getDatasetsMainController();
			datasetsThread = new Thread(datasetsController);
			datasetsThread.run();
			break;
		case algorithms:
			algorithmsController = AlgorithmsMainControllerImp.getAlgorithmsMainController();
			algorithmsThread = new Thread(algorithmsController);
			algorithmsThread.run();
			break;
		case tasks:
			tasksController = TasksMainController.getTasksMainController();
			tasksThread = new Thread(tasksController);
			tasksThread.run();
			break;
		}
	}

	public void renewChildSystemsButton(ChildSystemsName systemName) {
		mainBoard.renewChildSystemsButton(systemName);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		platformController.dispose();
		datasetsController.dispose();
		algorithmsController.dispose();
		tasksController.dispose();
	}
}
