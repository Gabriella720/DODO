package toolBox.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ServiceJob {
	
	private String algorithmName;
	private String datasetPath;
	private String resultPath;
	private Map<String,String> argsMap;
	
	private static Runtime rt = Runtime.getRuntime();
	private int exitVal=0;

	public ServiceJob(){
		argsMap = new HashMap<String,String>();
	}
	
	
	public void setArgs(String arg,String value)
	{
		argsMap.put(arg, value);
	}
	
	public String executeJob()
	{
		StringBuffer command = new StringBuffer("/home/hadmin/hadoop/hadoop-1.0.1/bin/hadoop jar");
		command.append(" ").append(algorithmName).append(".jar")
		.append(" ").append(datasetPath).append(" ").append(resultPath);
		
		for(String value:argsMap.values())
			command.append(" ").append(value);				
		
		System.out.println(command);
		String[] result = excuteCommand(command.toString());
		return result[0];
	}


	private String[] excuteCommand(String command) {
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
	
	
	
	public String getAlgorithmName() {
		return algorithmName;
	}


	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String getDatasetPath() {
		return datasetPath;
	}


	public void setDatasetPath(String datasetPath) {
		this.datasetPath = datasetPath;
	}


	public String getResultPath() {
		return resultPath;
	}


	public void setResultPath(String resultPath) {
		this.resultPath = resultPath;
	}
	
	
}
