package toolBox.datasetsManager.applications;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DataRunnable implements Runnable{
	/**
	 * @param args
	 * @throws Throwable
	 */
	//private String[] args;
	private static Runtime rt = Runtime.getRuntime();
	private int exitVal=0;
	//private Text submitInfo;


	public void run() {
		String command = "java -jar source/data/datacount/dataCount.jar  hdfs://master:54310/user/hadmin/DodoData/test/test.dat hdfs://master:54310/user/hadmin/DodoData/test/out";
		System.out.println(command);
		String[] bufferedReader=excuteCommand(command);
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
