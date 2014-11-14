package toolBox.gui.view;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataInput {
	
	protected String URL = null;
	protected String Name = null;
	protected String Format = null;
	protected String[] attributeName = null;
	protected boolean[] attributeType = null;
	protected double[][] datas = null;
	protected enumItem[] enumItems = null;
	protected double[] maxs = null;
	protected double[] mins = null;
	protected int attributes;
	protected int numOfEnums;
	private int numOfValues;
	
	public DataInput(){
		
	}
	
	public DataInput(double [][]in, int r, int c){
		datas = in;
		numOfValues = r;
		attributes = c;
		maxs = new double[attributes];
		mins = new double[attributes];
		for(int i=0; i<attributes; i++){
			maxs[i] = datas[0][i];
			mins[i] = datas[0][i];
		}
		for(int i=0; i<numOfValues; i++){
			for(int j =0; j<attributes; j++){
				if(datas[i][j]>maxs[j])
					maxs[j] = datas[i][j];
				else if(datas[i][j]<mins[j])
					mins[j] = datas[i][j];
			}
		}
	}
	
	public DataInput(String u, int type){
	
		if(type==0)
			dataFromLocal(u);
		else
			dataFromHDFS(u);
			
	}
	
	private void dataFromHDFS(String u){
			
		Name = u;
		URL = "hdfs://localhost:9000/user/ryan/datahead/" + Name;
		Format = "hdfs://localhost:9000/user/ryan/datahead/" + Name + ".head";

		Configuration conf = new Configuration(); 
        conf.set("hadoop.job.ugi", "hadoop-user,hadoop-user");          
        //FileSystem是用户操作HDFS的核心类，它获得URI对应的HDFS文件系统 
        FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create(Format),conf);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
        FSDataInputStream in = null;        
		BufferedReader reader = null;
		numOfValues = 0;
		String line = null;	
		
		//get num of attributes

		try {
			in = fs.open( new Path(URL) );
			reader = new BufferedReader(new InputStreamReader(in));

			while(reader.readLine() !=null){
				numOfValues++;
			}
			reader.close();
			System.out.println(numOfValues);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		int current = 0;
		try {
			double temp = 0;
			in = fs.open( new Path(Format) );
			reader = new BufferedReader(new InputStreamReader(in));
			line = reader.readLine();
			while(line!=null && line.startsWith("%")){
				//numOfValues--;
				line = reader.readLine();
			}		
			attributes = 0;
			numOfEnums = 0;	
			while(line!=null && line.startsWith("@")){
				//numOfValues--;
				if(line.startsWith("@attribute"))
					attributes++;
				if(line.endsWith("}"))
					numOfEnums++;
				line = reader.readLine();
			}
			in = fs.open( new Path(Format) );
			reader = new BufferedReader(new InputStreamReader(in));
			String[] contents = null;
			line = reader.readLine();
			while(!line.startsWith("@")){
				
				line = reader.readLine();
			}
			attributeName = new String[attributes];
			attributeType = new boolean[attributes];
			enumItems = new enumItem[numOfEnums+1];
			
			int lines = 0;
			int tempindexOfEnums = 0;
			while(line!=null){
				contents = line.split(" ");
				if(contents[0].startsWith("@relation"))
					Name = contents[1];
				if(contents[0].contains("@attribute")){
					//attribute
					attributeName[lines] = contents[1];
					if(contents[2].startsWith("real")){
					//real	
						attributeType[lines] = true;
					}
					else{
					//enum	
						attributeType[lines] = false;
						String tempStr = line.substring(contents[0].length()+contents[1].length()+3, line.length()-1);
						enumItems[tempindexOfEnums] = new enumItem(tempStr);
						tempindexOfEnums++;
						
					}
					lines++;
					
				}
				
				line = reader.readLine();
			}
			
			System.out.println("attributes:" + attributes);
			//----------------read data------------------
			in = fs.open( new Path(URL) );
			reader = new BufferedReader(new InputStreamReader(in));
			line = reader.readLine();
			contents = line.split(",");
			datas = new double[numOfValues][attributes];
			maxs = new double[attributes];
			mins = new double[attributes];
			int Enums = 0;
			double tempDouble;
			Pattern pattern = Pattern.compile("[0-9]*.[0-9]*");
			Matcher isNum;
			for(int i=0; i<attributes; i++){
				
				isNum = pattern.matcher(contents[i]);
				if( !isNum.matches() ){
					tempDouble = enumItems[Enums].getItemNumber(contents[i]);
					Enums++;
				}
				else{
					tempDouble = Double.parseDouble(contents[i]);
				}
				maxs[i] = tempDouble;
				mins[i] = tempDouble;
			}
			while(line!=null){
				Enums = 0;
				contents = line.split(",");
				for(int i=0; i<attributes; i++){
					isNum = pattern.matcher(contents[i]);
					if( !isNum.matches() ){
						tempDouble = enumItems[Enums].getItemNumber(contents[i]);
						Enums++;
					}
					else{
						tempDouble = Double.parseDouble(contents[i]);
					}
					datas[current][i] = tempDouble;
					if(tempDouble > maxs[i])
						maxs[i] = tempDouble;
					if(tempDouble < mins[i])
						mins[i] = tempDouble;
				}
				current++;
				line = reader.readLine();
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//for(int i=0; i<numOfValues; i++){		
			//System.out.print(datas[i][0] + " " + datas[i][1] + "\n");
		//}
		
		//for(int i =0; i<attributes; i++){
			//System.out.print(maxs[i] + " " + mins[i] + "\n");
		//}		
	
	}
	
	private void dataFromLocal(String u){	
		Name = u;
		URL = "data/" + Name;
		Format = "data/" + Name + ".head";
		File file = new File(URL);
		File formatFile = new File(Format);
		BufferedReader reader = null;
		numOfValues = 0;
		String line = null;	
		
		//get num of attributes

		try {
			reader = new BufferedReader(new FileReader(file));
			while(reader.readLine()!=null)
				numOfValues++;
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		int current = 0;
		try {
			double temp = 0;
			FileReader fileReader = new FileReader(formatFile);
			reader = new BufferedReader(fileReader);
			line = reader.readLine();
			while(line!=null && line.startsWith("%")){
				//numOfValues--;
				line = reader.readLine();
			}		
			attributes = 0;
			numOfEnums = 0;	
			while(line!=null && line.startsWith("@")){
				//numOfValues--;
				if(line.startsWith("@attribute"))
					attributes++;
				if(line.endsWith("}"))
					numOfEnums++;
				line = reader.readLine();
			}
			reader = new BufferedReader(new FileReader(formatFile));
			String[] contents = null;
			line = reader.readLine();
			while(!line.startsWith("@")){
				
				line = reader.readLine();
			}
			attributeName = new String[attributes];
			attributeType = new boolean[attributes];
			enumItems = new enumItem[numOfEnums+1];
			
			int lines = 0;
			int tempindexOfEnums = 0;
			while(line!=null){
				contents = line.split(" ");
				if(contents[0].startsWith("@relation"))
					Name = contents[1];
				if(contents[0].contains("@attribute")){
					//attribute
					attributeName[lines] = contents[1];
					if(contents[2].startsWith("real")){
					//real	
						attributeType[lines] = true;
					}
					else{
					//enum	
						attributeType[lines] = false;
						String tempStr = line.substring(contents[0].length()+contents[1].length()+3, line.length()-1);
						enumItems[tempindexOfEnums] = new enumItem(tempStr);
						tempindexOfEnums++;
						
					}
					lines++;
					
				}
				
				line = reader.readLine();
			}
			
			
			//----------------read data------------------
			fileReader = new FileReader(file);
			reader = new BufferedReader(fileReader);
			line = reader.readLine();
			contents = line.split(",");
			datas = new double[numOfValues][attributes];
			maxs = new double[attributes];
			mins = new double[attributes];
			int Enums = 0;
			double tempDouble;
			Pattern pattern = Pattern.compile("[0-9]*.[0-9]*");
			Matcher isNum;
			for(int i=0; i<attributes; i++){
				
				isNum = pattern.matcher(contents[i]);
				if( !isNum.matches() ){
					tempDouble = enumItems[Enums].getItemNumber(contents[i]);
					Enums++;
				}
				else{
					tempDouble = Double.parseDouble(contents[i]);
				}
				maxs[i] = tempDouble;
				mins[i] = tempDouble;
			}
			while(line!=null){
				Enums = 0;
				contents = line.split(",");
				for(int i=0; i<attributes; i++){
					isNum = pattern.matcher(contents[i]);
					if( !isNum.matches() ){
						tempDouble = enumItems[Enums].getItemNumber(contents[i]);
						Enums++;
					}
					else{
						tempDouble = Double.parseDouble(contents[i]);
					}
					datas[current][i] = tempDouble;
					if(tempDouble > maxs[i])
						maxs[i] = tempDouble;
					if(tempDouble < mins[i])
						mins[i] = tempDouble;
				}
				current++;
				line = reader.readLine();
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//for(int i=0; i<numOfValues; i++){		
			//System.out.print(datas[i][0] + " " + datas[i][1] + "\n");
		//}
		
		//for(int i =0; i<attributes; i++){
			//System.out.print(maxs[i] + " " + mins[i] + "\n");
		//}
	}
	
	
	public double[] getMax(){
		
		return maxs;
	}
	
	public double[] getMin(){
		
		return mins;
	}
	
	public int getAtrributes(){
		
		return attributes;
	}
	
	public int getNums(){
		
		return numOfValues;
	}
	
	public double[][] getData(){
		
		return datas;
	}
	
	public String[] getAttributes(){
				
		return attributeName;
	}
	
////-------------testing output---------------------	
//	public static void main(String[] args){
//		DataInput datainput = new DataInput("data/cpu.arff");
//		double data[][] = datainput.getData();
//		for(int i=0; i<datainput.getNums(); i++){
//			for(int j=0; j<datainput.getAtrributes(); j++)
//				System.out.print(data[i][j] + " ");
//		System.out.println();
//		}
//	}

}
