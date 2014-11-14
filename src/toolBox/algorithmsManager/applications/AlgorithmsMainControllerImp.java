package toolBox.algorithmsManager.applications;

import org.dom4j.DocumentException;
import toolBox.gui.algorithms.AlgorithmsMainBoard;

import java.io.IOException;

public class AlgorithmsMainControllerImp extends AlgorithmsMainController{
	protected AlgorithmsMainBoard algorithmsBoard = null;

	private AlgorithmsMainControllerImp()
	{
		
	}
	
	public static AlgorithmsMainController getAlgorithmsMainController()
	{
		if(algorithmsController==null)
			algorithmsController = new AlgorithmsMainControllerImp();
		return algorithmsController;
	}
	
	@Override
	public void run() {
		start();
	}

	@Override
	public void start() {
		if(algorithmsBoard==null)
			algorithmsBoard = new AlgorithmsMainBoard(algorithmsController);
		try {
			try {
				algorithmsBoard.work();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
