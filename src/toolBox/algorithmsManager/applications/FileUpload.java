package toolBox.algorithmsManager.applications;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import toolBox.core.system.DodoSystem;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class FileUpload {
	public void upload() throws IOException, URISyntaxException{
		String uri="hdfs://master:54310/";
		URI hdfsURI=new URI(uri);
		String localPath="/home/hadmin/hadoop/Algorithm";
		String hdfsPath="hdfs://master:54310/user/hadmin/wjy1/";
		Path src=new Path(localPath);
		Path dst=new Path(hdfsPath);
		Configuration conf =new Configuration();
		FileSystem fs=FileSystem.get(URI.create(uri), conf);
		//fs.copyFromLocalFile(src,dst);
		DistributedFileSystem hdfs=DodoSystem.getDodoSystem().getDistributedFileSystem();
		if(hdfs==null){
			System.out.println("ssssssssssssss");
		}
		hdfs.copyFromLocalFile(src, dst);
	}
}
