package toolBox.platformManager.functions;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.mapred.JobClient;
import org.jdom.JDOMException;
import toolBox.core.system.DodoSystem;
import toolBox.core.utility.XMLUtility;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;

public class PlatformManagerFunctionsImpl implements PlatformManagerFunctions {

	@Override
	public boolean connect(String jobTrackerIP,String jobTrackerPort,String hdfsIP,String hdfsPort)
	{
		//InetAddress inetAddress;
		try {
			Configuration conf = new Configuration();
			
			//inetAddress = InetAddress.getByAddress(jobTrackerIP.getBytes());
			InetSocketAddress inetSocketAddress =new InetSocketAddress(jobTrackerIP,Integer.parseInt(jobTrackerPort));
			DodoSystem.getDodoSystem().setJobClient(new JobClient(inetSocketAddress, conf));
				
			//inetAddress = InetAddress.getByAddress(hdfsIP.getBytes());
//			inetSocketAddress =new InetSocketAddress(hdfsIP,Integer.parseInt(hdfsPort));
//			DodoSystem.getDodoSystem().setDFSClient(new DFSClient(inetSocketAddress, conf));
			DistributedFileSystem dfs = new DistributedFileSystem();
			URI uri = null;
			try {
				uri = new URI("hdfs://"+hdfsIP+":"+hdfsPort);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			dfs.initialize(uri, conf);
			System.out.println(dfs.getHomeDirectory());
			DodoSystem.getDodoSystem().setDistributedFileSystem(dfs);
			DodoSystem.getDodoSystem().setDFSClient(dfs.getClient());
			
			XMLUtility xml = new XMLUtility();
			List list = xml.readXml("./source/data/IPInfo/IPInfo.xml");
			try {
				xml.update(list,"JobTrackerIPAndPort", jobTrackerIP+":"+jobTrackerPort);
				xml.update(list,"HDFSIPAndPort", hdfsIP+":"+hdfsPort);
				xml.saveXml("./source/data/IPInfo/IPInfo.xml");
				
			} catch (JDOMException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
				
		} catch (UnknownHostException e) {
			if(DodoSystem.getDodoSystem().getJobClient() != null)
			{
				try {
					DodoSystem.getDodoSystem().getJobClient().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
			return false;
		}catch(IOException e) {
			if(DodoSystem.getDodoSystem().getJobClient() != null)
			{
				try {
					DodoSystem.getDodoSystem().getJobClient().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
			return false;
		}
			
		return true;
	}
}
