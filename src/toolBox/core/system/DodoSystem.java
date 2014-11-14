package toolBox.core.system;

import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.mapred.JobClient;

import java.util.ArrayList;

public class DodoSystem {
	
	private static DodoSystem dodoSystem = null;
	
	private MainController mainController = null;
	
	private JobClient jobClient = null;
	private	DFSClient dfsClient = null;
	private DistributedFileSystem distributedFileSystem = null;
	private ArrayList program=null;
	

	private DodoSystem()
	{

	}
	
	public static DodoSystem getDodoSystem()
	{
		if(dodoSystem==null)
			dodoSystem = new DodoSystem();
		return dodoSystem;
	}
	
	public void start()
	{
		mainController = MainController.getMainController();
		mainController.start();
	}
	
	public DistributedFileSystem getDistributedFileSystem() {
		return distributedFileSystem;
	}
	
	public ArrayList getDodoProgram(){
		return program;
	}

	
	
	public JobClient getJobClient()
	{
		return jobClient;
	}
	
	public DFSClient getDFSClient()
	{
		return dfsClient;
	}
	
	
	public void setJobClient(JobClient jc)
	{
		jobClient = jc;
	}
	
	public void setDFSClient(DFSClient dc)
	{
		dfsClient = dc;
	}
	
	public void setDistributedFileSystem(DistributedFileSystem distributedFileSystem) {
		this.distributedFileSystem = distributedFileSystem;
	}
	
	/*public void setProgram(ArrayList arraylist){
		program=arraylist;
	}*/
	
	
	
}
