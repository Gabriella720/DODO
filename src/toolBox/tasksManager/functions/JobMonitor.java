package toolBox.tasksManager.functions;


import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.RunningJob;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;


public class JobMonitor implements Runnable {
	private RunningJob runningJob;
	
	
	//private Label jobName;
	private JobStatus jobstatus;
	private Label jobStatus;
	
	//private Label jobStartTime;
	//private Label jobEndTime;
	//private Label schedulingInfo;
	//private Label trackingUrl;
	
	private ProgressBar setup;
	private ProgressBar map;
	private ProgressBar reduce;
	private ProgressBar cleanup;
	private Button stop;
	
	public JobMonitor(RunningJob runningJob,Label jobStatus,ProgressBar setup,ProgressBar map, ProgressBar reduce,ProgressBar cleanup,Button stop) {
		this.runningJob=runningJob;
		this.jobStatus=jobStatus;
		this.setup=setup;
		this.map = map;
		this.reduce = reduce;
		this.cleanup=cleanup;
		//this.jobEndTime=jobEndTime;
		this.stop=stop;
		
	}

	@Override
	public void run() {
		Display.getDefault().asyncExec(new Runnable() {  
			public void run() {
				setup.setMaximum(100);
				setup.setMinimum(0);
				setup.setSelection(10);
				
				map.setMaximum(100);
				map.setMinimum(0);
				map.setSelection(10);
				
				reduce.setMaximum(100);
				reduce.setMinimum(0);
				reduce.setSelection(90);
				
				cleanup.setMaximum(100);
				cleanup.setMinimum(0);
				cleanup.setSelection(10);
				
				new Thread(new MonitorStatusThread(runningJob, jobStatus,setup, map, reduce,cleanup,stop)).start();
			}
		});
	}
}
