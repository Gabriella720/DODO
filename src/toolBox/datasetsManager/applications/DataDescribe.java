package toolBox.datasetsManager.applications;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataDescribe {
	private Path Path = null;
	private String relationName = "";
	private Path savePath;
	private Path metaPath = new Path("hdfs://master:54310/user/hadmin/DodoData/metaData");
	private int count = 0;
	private DistributedFileSystem dfs;
	
	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	
	public Path getPath() {
		return Path;
	}

	public void setPath(Path path) {
		try {
			if (dfs.isDirectory(path)){
				Path = path;
				System.out.println(Path);
			}
			else{
				int index= path.toString().lastIndexOf("/") ;
				Path = new Path(path.toString().substring(0, index));
				System.out.println(Path);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DataDescribe(DistributedFileSystem dfs){
		this.dfs = dfs;
	}
	
	/*public int countColumn(){
		BufferedReader reader = null;
		int numColumn = 0;
        try {
            System.out.println("读取第一行");
            reader = new BufferedReader(new InputStreamReader(input)); 
            
            String tempString = reader.readLine();
            System.out.println(tempString);
            String[] ss = tempString.split(" ");
            numColumn = ss.length;
            System.out.println(numColumn);
                        
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return numColumn;
	}*/
	
	public String getRelationName(Path path){
		int begin = 0;
		int end = 0;
    	try {
			if (dfs.isDirectory(path)){
				begin = path.toString().lastIndexOf("/")+1;
				end = path.toString().length();
			  }
			  else{		  
				begin = path.toString().substring(0, path.toString().lastIndexOf("/")-1).lastIndexOf("/") + 1;
				end = path.toString().lastIndexOf("/");

			  }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		String name = path.toString().substring(begin, end);
		//System.out.println(name);
		return name;		
	}
	
	public void setSavePath(){
		savePath = new Path(metaPath.toString() + "/" + relationName + ".xml") ;		
	}
	
	public void generate(String[] attributeArray, String[] typeArray, DistributedFileSystem dfs){
		try {
			if(Path == null){
				Shell shell =Display.getCurrent().getActiveShell();
				MessageDialog.openWarning(shell, "Warning", "Please specify the data first!");
				return;
			}
			
			if(dfs.exists(savePath)){
				Shell shell =Display.getCurrent().getActiveShell();
				MessageDialog.openWarning(shell, "Warning", "File has already exits! Please rename!");
			}
			else{
				Thread thread=new Thread(new DataRunnable());
				thread.run();
				
				BufferedReader reader = null;
				String src = "hdfs://master:54310/user/hadmin/DodoData/test/out";
				FSDataInputStream input = dfs.open(new Path(src + "/part-r-00000"));	
	            reader = new BufferedReader(new InputStreamReader(input)); 
	            
				DocumentFactory factory = new DocumentFactory();
				Document doc = factory.createDocument();
				Element root = doc.addElement("Data_Description");
				Element relation = root.addElement("Relation_name");
				relation.addAttribute("Name", relationName);
				Element dataPath = root.addElement("Path_of_datasets");
				dataPath.addAttribute("URI", Path.toString());
				Element number = root.addElement("number_of_attributes");
				number.addAttribute("number", String.valueOf(count));
				for (int i = 0; i < count; i++){
					System.out.println("读取一行");
		            String tempString = reader.readLine();
		            System.out.println(tempString);
		            String[] ss = tempString.split(" ");
		            
					Element attribute = root.addElement("Attribute");
					Element name = attribute.addElement("Name");
					name.setText(attributeArray[i]);
					Element type = attribute.addElement("Type");
					type.setText(typeArray[i]);
					Element distinct = attribute.addElement("Distinct");
					distinct.setText(ss[0]);
					Element unique = attribute.addElement("Unique");
					unique.setText(ss[1]);
					Element min = attribute.addElement("Min");
					min.setText(ss[2]);
					Element max = attribute.addElement("Max");
					max.setText(ss[3]);
					Element mean = attribute.addElement("Mean");
					mean.setText(ss[4]);
					Element stddev = attribute.addElement("Stddev");
					stddev.setText(ss[5]);
					
				}	
				 if (reader != null) {
		                try {
		                    reader.close();
		                } catch (IOException e1) {
		                }
				 }
		        dfs.delete(new Path(src), true);
				OutputFormat format = new OutputFormat("	", true);
				try {
					//System.out.println(savePath);
					FSDataOutputStream fw = dfs.create(savePath);
					XMLWriter writer = new XMLWriter(fw, format);
					writer.write(doc);
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName(){
		return relationName;
	}

}


