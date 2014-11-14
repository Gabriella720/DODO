package toolBox.tasksManager.functions;

import org.eclipse.swt.widgets.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RunJarThread implements Runnable {
	/**
	 * @param args
	 * @throws Throwable
	 */
	private String[] args;
	// private String hadoopHome;
	private static Runtime rt = Runtime.getRuntime();
	private int exitVal=0;
	private Text submitInfo;
	//StringBuffer errorMessage = new StringBuffer();
	//StringBuffer inputMessage = new StringBuffer();
	//String line = null;
	//Process proc = null;
	//BufferedReader error = null;
	//BufferedReader input = null;

	public RunJarThread(String[] args, Text submitInfo) {
		this.args = args;
		// this.hadoopHome=hadoopHome;
		this.submitInfo = submitInfo;
	}

	public void run() {
		String command = "/home/ryan/Downloads/Dodo-project/hadoop-1.0.1/bin/hadoop jar";
		for (int i = 0; i < args.length; i++) {
			command = command + " " + args[i];

		}
		System.out.println(command);

		/*try {
			proc = rt.exec(command);
			error = new BufferedReader(new InputStreamReader(
					proc.getErrorStream()));
			input = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					do{
						try {
							Thread.sleep(100);
							while ((line = error.readLine()) != null) {
								errorMessage.append(line + '\n');
							}
							while ((line = input.readLine()) != null) {
								inputMessage.append(line + '\n');
							}
							submitInfo.setText(errorMessage.toString()+"\n"+inputMessage.toString());
							exitVal = proc.waitFor();

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						
						
					}while(exitVal==1);
				}
			});

			System.out.println("Process exitValue: " + exitVal);
		} catch (Throwable t) {
			t.printStackTrace();
		}*/

		String[] bufferedReader=excuteCommand(command);
		//submitInfo.setText(bufferedReader[0].toString()+bufferedReader[1].toString());
		System.out.print(bufferedReader[0].toString());
		System.out.print(bufferedReader[1].toString());
	}

	
	public String[] excuteCommand(String command) {
		try {
			Process proc = rt.exec(command);
			BufferedReader error = new BufferedReader(new InputStreamReader(
					proc.getErrorStream()));
			BufferedReader input = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			StringBuffer errorMessage = new StringBuffer();
			StringBuffer inputMessage = new StringBuffer();
			String line = null;
			while ((line = error.readLine()) != null) {
				errorMessage.append(line + '\n');
			}
			while ((line = input.readLine()) != null) {
				inputMessage.append(line + '\n');
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