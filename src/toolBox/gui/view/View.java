package toolBox.gui.view;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.eclipse.swt.widgets.Display;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class View{		

	public View() {
		String name = JOptionPane.showInputDialog("Please input filename");
		String URL = "hdfs://localhost:9000/user/ryan/datahead/" + name +".head";
		int type = viewType(URL);
		if(type == 0){
			try {
				Display display = Display.getDefault();
				OriginalDataView shell = new OriginalDataView(display, name);
				shell.open();
				shell.layout();
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();					
			}
			
		}else if(type == 1){
			Cluster frame = new Cluster(name);		
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
			frame.setVisible(true);
		}else{
			JOptionPane.showMessageDialog(null, "File error!");
		}
	}
	
	public int viewType(String URL){
		Configuration conf = new Configuration(); 
        conf.set("hadoop.job.ugi", "hadoop-user,hadoop-user");          
        //FileSystem是用户操作HDFS的核心类，它获得URI对应的HDFS文件系统 
        FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create(URL),conf);
			FSDataInputStream in = null;        
			BufferedReader reader = null;
			String line = null;
			in = fs.open( new Path(URL) );
			reader = new BufferedReader(new InputStreamReader(in));
			line = reader.readLine();
			while(line!=null){
				if(line.startsWith("@relation"))
					return 0;
				else if(line.startsWith("@cluster"))
					return 1;
				line = reader.readLine();
			}		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		} 
        
		return -1;
	}
}
