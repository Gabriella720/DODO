package toolBox.tasksManager.applications;


import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.JobTracker;
import toolBox.core.system.ControllerInterface;
import toolBox.core.system.DodoSystem;
import toolBox.gui.tasks.JobStatusTableBoard;
import toolBox.gui.tasks.TasksMainBoard;

import java.io.IOException;

public class TasksMainController implements ControllerInterface{
	
	protected TasksMainBoard tasksBoard = null;
	protected static TasksMainController tasksMainController=null;
	static JobStatusTableBoard showWindow = null;
	JobTracker jobTracker;
	
	//private JobClient jobClient = null;
	//private DodoProgram program=null;

	public TasksMainController()
	{
		//this.jobClient=DodoSystem.getDodoSystem().getJobClient();
		//this.program=DodoSystem.getDodoSystem().getDodoProgram();
	}
	
	public static TasksMainController getTasksMainController()
	{
		if(tasksMainController==null)
			tasksMainController=new TasksMainController();
		return tasksMainController;
	}
	
	@Override
	public void run() {
		start();
	}

	@Override
	public void start() {
		 if(tasksBoard==null){
			tasksBoard = new TasksMainBoard(tasksMainController);
		    tasksBoard.work();
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	
	public boolean ShowJobDetails(JobClient jobClient,String jobId) throws IllegalArgumentException, IOException, InterruptedException
	{
		if(jobClient==null) 
		{
			return false;
		}
		if(jobId==null || jobId.length()==0){
			//JOptionPane.showMessageDialog(showPanel, "Please check the jobId!","Error Message",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		JobStatus jobStatus = null;
		JobStatus[] allJobs = DodoSystem.getDodoSystem().getJobClient().getAllJobs();
		for(JobStatus tempJobState:allJobs){
			if(tempJobState.getJobID().toString().equals(jobId))
			{
				jobStatus = tempJobState;
				break;
			}
		}
		if(jobStatus!=null){
			new JobStatusTableBoard(jobClient,jobStatus,jobId);
		}
		return true;		
	}
	
	/*public boolean ShowQueueDetails(JobClient jobClient)
	{
		if(jobClient==null)
			return false;
		new QueueStatusTableBoard(jobClient);
		return true;
	}*/
	
	//----------------------------------------------------------------
	
	/*public void finalSynchronize(String jobId) throws IOException {
		// TODO Auto-generated method stub
		JobStatus jobStatus = null;
		JobStatus[] allJobs = DodoSystem.getDodoSystem().getJobClient().getAllJobs();
		for(JobStatus tempJobState:allJobs){
			if(tempJobState.getJobID().toString().equals(jobId))
			{
				jobStatus = tempJobState;
				break;
			}
		}
		if(jobStatus!=null){
			showWindow.finalRefresh(jobStatus);
		}
	}*/
	
}
