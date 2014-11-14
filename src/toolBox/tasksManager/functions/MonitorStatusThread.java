package toolBox.tasksManager.functions;


import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.RunningJob;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import toolBox.core.system.DodoSystem;
import toolBox.tasksManager.applications.TasksMainController;

import java.io.IOException;


public class MonitorStatusThread implements Runnable {
	TasksMainController controller=null;
	private String jobId;
	private RunningJob runningJob;
	private Label jobStatus;
	private ProgressBar setup;
	private ProgressBar map;
	private ProgressBar reduce;
	private ProgressBar cleanup;
	//private Label jobEndTime;
	private JobStatus jobstatus;
	private Button stop;
	private boolean terminal = false;
	
	public MonitorStatusThread(RunningJob runningJob,Label jobStatus,ProgressBar setup, ProgressBar map, ProgressBar reduce,ProgressBar cleanup,Button stop) {
		this.runningJob =runningJob;
		this.jobStatus = jobStatus;
		this.setup=setup;
		this.map = map;
		this.reduce = reduce;
		this.cleanup=cleanup;
		//this.jobEndTime=jobEndTime;
		this.stop=stop;
		
	}
	@Override
	public void run() {
		jobId=runningJob.getJobID();
		do {
			try {
				Thread.sleep(100);
				
				final String status = JobStatus.getJobRunState(runningJob.getJobState());
				final int setupInt=(int)(runningJob.setupProgress()*100);
				final int mapInt = (int)(runningJob.mapProgress()*100);
				final int reduceInt = (int)(runningJob.reduceProgress()*100);
				final int cleanupInt=(int)(runningJob.cleanupProgress()*100);
				//System.out.println(counters);
				if (status.equals("SUCCEEDED") || status.equals("FAILED") || status.equals("KILLED"))
					terminal = true;
				Display.getDefault().asyncExec(new Runnable() {  
					public void run() {
						jobStatus.setText(status);
						setup.setSelection(setupInt);
						map.setSelection(mapInt);
						reduce.setSelection(reduceInt);
						cleanup.setSelection(cleanupInt);
						if(terminal==true){
							//button
							JobStatus[] allJobs=null;
							try {
								allJobs = DodoSystem.getDodoSystem().getJobClient().getAllJobs();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							for(JobStatus tempJobState:allJobs){
								if(tempJobState.getJobID().toString().equals(jobId))
								{
									jobstatus = tempJobState;
									break;
								}
							}
							
							stop.setEnabled(false);
						}
						
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		while (!terminal);
	}

}
