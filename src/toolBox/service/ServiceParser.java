package toolBox.service;

public class ServiceParser {

	public static ServiceJob parse(String str)
	{
		ServiceJob serviceJob = new ServiceJob();
		
		String[] options = str.split(" ");
		for(String option:options)
		{
			String[] args = option.split("=");
			String arg = args[0].substring(1);
			
			if(arg.equals("algorithmName"))
				serviceJob.setAlgorithmName(args[1]);
			else if(arg.equals("datasetPath"))
				serviceJob.setDatasetPath(args[1]);
			else if(arg.equals("resultPath"))
				serviceJob.setResultPath(args[1]);
			else serviceJob.setArgs(arg, args[1]);
		}
		
		return serviceJob;
	}
}
