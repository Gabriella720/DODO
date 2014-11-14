package toolBox.datasetsManager.applications;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import toolBox.core.system.DodoSystem;
import toolBox.datasetsManager.functions.DatasetsManagerFunctions;
import toolBox.gui.datasets.DatasetsMainBoard;

public class DatasetsMainControllerImp extends DatasetsMainController{
	
	protected DatasetsMainBoard datasetsBoard = null;
	protected DatasetsManagerFunctions datasetsManagerFunctions= null;
	
	private DatasetsMainControllerImp()
	{
		
	}
	
	public static DatasetsMainController getDatasetsMainController()
	{
		if(datasetsController==null)
			datasetsController = new DatasetsMainControllerImp();
		return datasetsController;
	}
	
	@Override
	public void run() {
		if(DodoSystem.getDodoSystem().getDistributedFileSystem()==null){
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			MessageBox mb = new MessageBox(shell,SWT.ICON_WARNING);
			mb.setMessage("Please connect Hadoop first!");
			mb.setText("Warning!");
			mb.open();
			return;
		}
		start();
	}

	@Override
	public void start() {
		if(datasetsBoard==null){
			datasetsBoard = new DatasetsMainBoard(datasetsController);		
		}
		datasetsBoard.work();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
