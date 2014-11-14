package toolBox.tasksManager.functions;

import org.eclipse.swt.widgets.Text;


public class TaskSubmission{
	//private String hadoopHome;
	private String algorithmPath;
	//private String inputPath;
	//private String outputPath;
	private String[] parameters;
	private Text submitInfo;
	
	public TaskSubmission(String algorithmPath,String[] parameters,Text submitInfo){
		//this.hadoopHome = hadoopAddress;
		this.algorithmPath=algorithmPath;
		//this.inputPath = inputPath;
		//this.outputPath = outputPath;
		this.parameters=parameters;
		this.submitInfo=submitInfo;
		
	}
	
	public boolean submit() throws Throwable{

		//int num=parameters.length;
		//Parameter[] parameter=new Parameter[num];
		//parameter=program.getParameters();
		
	//	int length=1+num;
		String[] args = new String[parameters.length+1];
		args[0] =algorithmPath;//the directory of the algorithm on local
		if(parameters.length!=0){
			for(int i=0;i<parameters.length;i++){
				args[i+1]=parameters[i];
			//System.out.println(args[i+3]);
			}
		}
		Thread thread=new Thread(new RunJarThread(args,submitInfo));
		thread.start();
		return true;
		
	}
	
}
