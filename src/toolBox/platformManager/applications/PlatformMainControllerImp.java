package toolBox.platformManager.applications;

import toolBox.gui.platform.PlatformMainBoard;
import toolBox.platformManager.functions.PlatformManagerFunctions;
import toolBox.platformManager.functions.PlatformManagerFunctionsImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class PlatformMainControllerImp implements PlatformMainController {

	private static PlatformMainControllerImp platformMainController = null;
	private int exitVal;

	protected PlatformMainBoard platformBoard = null;
	protected PlatformManagerFunctions platformFunctions = new PlatformManagerFunctionsImpl();

	private PlatformMainControllerImp() {

	}

	private static Runtime rt = Runtime.getRuntime();

	public static PlatformMainController getPlatformMainController() {
		if (platformMainController == null)
			platformMainController = new PlatformMainControllerImp();
		return platformMainController;
	}

	@Override
	public void start() {
		if (platformBoard == null)
			platformBoard = new PlatformMainBoard(platformMainController);
		platformBoard.work();
	}

	@Override
	public void run() {
		start();
	}

	@Override
	public boolean connect(String jobTrackerIP, String jobTrackerPort,
			String hdfsIP, String hdfsPort) {
		return platformFunctions.connect(jobTrackerIP, jobTrackerPort, hdfsIP,
				hdfsPort);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] excuteCommand(String command) {
		try {
            Process proc = rt.exec(command);
//			final InputStream es = proc.getErrorStream();
//			final InputStream is = proc.getInputStream();
//			// two thread is only response to ssh master
//			new Thread() {
//				public void run() {
//					BufferedReader bufferedReader = new BufferedReader(
//							new InputStreamReader(is));
//					String line = null;
//					try {
//						while ((line = bufferedReader.readLine()) != null) {
//							System.out.println(line + '\n');
//						}
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//				}
//			}.start();
//
//			new Thread() {
//				public void run() {
//					BufferedReader bufferedReader = new BufferedReader(
//							new InputStreamReader(es));
//					String line = null;
//					try {
//						while ((line = bufferedReader.readLine()) != null) {
//							System.out.println(line + '\n');
//						}
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//				}
//			}.start();
//			BufferedReader bufferedReader = new BufferedReader(
//					new InputStreamReader(proc.getInputStream()));
//			ProcessBuilder p = new ProcessBuilder(command);
//			p.redirectErrorStream(true);
//			Process proc = p.start();
//			BufferedReader bufferedReader = new BufferedReader(
//					new InputStreamReader(proc.getInputStream()));
//			String line = null;
//			try {
//				while ((line = bufferedReader.readLine()) != null) {
//					System.out.println(line + '\n');
//				}
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			exitVal = proc.waitFor();
//			System.out.println("Process exitValue: " + exitVal);
//			return bufferedReader;
			BufferedReader error = new BufferedReader(new InputStreamReader(
					proc.getErrorStream()));
			BufferedReader input = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			StringBuffer errorMessage = new StringBuffer();
			StringBuffer inputMessage = new StringBuffer();
			String line = null;
			while ((line = error.readLine()) != null) {
				errorMessage.append(line+'\n');
			}
			while ((line = input.readLine()) != null) {
				inputMessage.append(line+'\n');
			}
			String[] ret = new String[2];
			ret[0] = errorMessage.toString();
			ret[1] = inputMessage.toString();
			exitVal = proc.waitFor();
			System.out.println("Process exitValue: " + exitVal);
			return ret;
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
}
