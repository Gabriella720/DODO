package toolBox.gui.tasks;

import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.JobTracker;
import org.apache.hadoop.mapred.RunningJob;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;
import toolBox.tasksManager.functions.JobMonitor;

import java.io.IOException;
import java.util.Date;


public class JobStatusTableBoard {
	private String jobID=null;
	private Label jobId=null;
	//private Label jobName=null;
	private Label jobStatus=null;
	private Label startTime=null;
	//private Label endTime=null;
	
	private ProgressBar setup;
	private ProgressBar map;
	private ProgressBar reduce;
	private ProgressBar cleanup;
	private Label schedulingInfo=null;
	//private Label trackingUrl=null;
	private JobStatus jobstatus=null;
	private JobClient jobclient=null;
	private RunningJob runningJob=null;
	
	private JobTracker jobTracker;
	
	Shell shell=null;
	final Display display = Display.getDefault();
	final int Width=400;
	final int height=30;
	
	public JobStatusTableBoard(JobClient jobclient,JobStatus jobstatus, String jobID)
	{
		this.jobstatus=jobstatus;
		this.jobID=jobID;
		this.jobclient=jobclient;
		try {
			paintMyBoard();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void paintMyBoard() throws IOException {
		// TODO Auto-generated method stub
		shell =  new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setSize(500,450);
		shell.setText(jobID.toString()+"  detail");
		
		Label label=new Label(shell,SWT.NONE);
		label.setText("Job Id:");
		label.setBounds(30, 30, 120, 30);
		jobId=new Label(shell,SWT.NONE);
		jobId.setText(jobID);
		jobId.setBounds(160, 30, 300, 30);
		
		/*label=new Label(shell,SWT.NONE);
		label.setText("Job Name:");
		label.setBounds(30, 70, 120, 30);
		jobName=new Label(shell,SWT.NONE);
		jobName.setBounds(160, 70, 500, 30);
		//jobName.setText(jobTracker.getJobProfile(JobID.forName(jobID)).getJobName());
		*/
		label=new Label(shell,SWT.NONE);
		label.setText("Job Status:");
		label.setBounds(30, 70, 120, 30);
		jobStatus=new Label(shell,SWT.NONE);
		jobStatus.setBounds(160, 70, 300, 30);
		
		
		label=new Label(shell,SWT.NONE);
		label.setText("Start Time:");
		label.setBounds(30, 110, 120, 30);
		startTime=new Label(shell,SWT.NONE);
		startTime.setBounds(160, 110, 300, 30);
		startTime.setText(new Date(jobstatus.getStartTime()).toString());
		
		label=new Label(shell,SWT.NONE);
		label.setText("Setup Progress:");
		label.setBounds(30, 150, 120, 30);
		setup=new ProgressBar(shell,SWT.NONE);
		setup.setBounds(160, 150, 300, 30);
		//jobName.setText(jobTracker.getJobProfile(JobID.forName(jobID)).getJobName());
		
		label=new Label(shell,SWT.NONE);
		label.setText("Map Progress:");
		label.setBounds(30, 190, 120, 30);
		map=new ProgressBar(shell,SWT.NONE);
		map.setBounds(160, 190, 300, 30);
		
		label=new Label(shell,SWT.NONE);
		label.setText("Reduce Progress:");
		label.setBounds(30, 230, 120, 30);
		reduce=new ProgressBar(shell,SWT.NONE);
		reduce.setBounds(160, 230, 300, 30);
		
		label=new Label(shell,SWT.NONE);
		label.setText("Cleanup Progress:");
		label.setBounds(30, 270, 120, 30);
		cleanup=new ProgressBar(shell,SWT.NONE);
		cleanup.setBounds(160, 270,300, 30);
		
		label=new Label(shell,SWT.NONE);
		label.setText("SchedulingInfo:");
		label.setBounds(30, 310, 120, 30);
		schedulingInfo=new Label(shell,SWT.NONE);
		schedulingInfo.setBounds(160, 310, 300, 30);
		schedulingInfo.setText(jobstatus.getSchedulingInfo());
		
		
		/*label=new Label(shell,SWT.NONE);
		label.setText("Tracking URL:");
		label.setBounds(30,350,120,30);
		trackingUrl=new Label(shell,SWT.NONE);
		trackingUrl.setBounds(160, 350, 500, 30);
		//trackingUrl.setText(jobTracker.getJobProfile(jobstatus.getJobID()).getURL().toString());
*/		
		
		final Button stop=new Button(shell,SWT.NONE);
		stop.setText("Stop");
		stop.setBounds(230, 350, 50, 30);
		if(!jobstatus.isJobComplete()){
			/*
			 * 实时监控任务的运行状态
			 * */
			runningJob=jobclient.getJob(jobstatus.getJobId());
			Runnable jobDetail=new JobMonitor(runningJob,jobStatus,setup,map, reduce,cleanup,stop);
			Thread jobThread=new Thread(jobDetail);
			jobThread.start();
			
			
			stop.setEnabled(true);
			stop.addSelectionListener(new SelectionAdapter(){
	            public void widgetSelected(SelectionEvent e) {
	            	try {
	            		//RunningJob runningjob=jobclient.getJob(jobstatus.getJobId());
	            		runningJob.killJob();
	            		stop.setEnabled(false);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            }
	            });
		}else 
			{
				stop.setEnabled(false);
				finalRefresh(jobstatus);
			}
		//---------------------------------------------------------------------------------------
		
		
		shell.layout();
		shell.open();
		
	}
	
	public void finalRefresh(JobStatus jobstatus) {
		jobId.setText(jobstatus.getJobID().toString());
		//jobName.setText(jobTracker.getJobProfile(jobstatus.getJobID()).getJobName());
		jobStatus.setText(JobStatus.getJobRunState(jobstatus.getRunState()));
		startTime.setText(new Date(jobstatus.getStartTime()).toString());
		map.setSelection((int)(jobstatus.mapProgress()*100));
		reduce.setSelection((int)(jobstatus.reduceProgress()*100));
		//endTime.setText(new Date(jobTracker.getJob(jobstatus.getJobID()).getFinishTime()).toString());
		schedulingInfo.setText(jobstatus.getSchedulingInfo());
		//trackingUrl.setText(jobTracker.getJobProfile(jobstatus.getJobID()).getURL().toString());
	}

}
