
package toolBox.algorithmsManager.applications;

import java.util.Calendar;

public class DodoProgram {
	private static DodoProgram dodoProgram = null;
	String algorithmName=null;    //算法名称
	//boolean isUserDefined=false;  //是否为Dodo自带经典算法
	String algorithmCategory=null;    //算法类型 （classification cluster ...）
	//String authorID=null;       //作者名称
	String uploadDate=null;       //上传日期
	int numOfParameters=0;        //参数数量
	Parameter[] parameters =null; //算法参数
	String algorithmDescription=null;  //算法描述
	String copyPathOnLocal=null;
	
	public DodoProgram(){
		algorithmName="unfilled";
		//isUserDefined=false;
		algorithmCategory="User Defined";
		//authorID="unfilled";
		String date = Calendar.getInstance().getTime().toString(); //get date
		this.uploadDate=date.substring(24,28)+date.substring(4,7)+date.substring(8,10);
		algorithmDescription="unfilled";
	}
	
	public DodoProgram(DodoProgram program){
		this.algorithmName=program.getAlgorithmName();
		this.algorithmCategory=program.getAlgorithmCategory();		
		this.uploadDate=program.getUploadDate();		
		this.numOfParameters=program.getNumOfParameters();
		parameters =new Parameter[this.numOfParameters];
		for(int i=0;i<this.getNumOfParameters();i++){
			this.parameters[i] =new Parameter();
			this.parameters[i].setName(program.getParameters()[i].getName());
			this.parameters[i].setType(program.getParameters()[i].getType());
			this.parameters[i].setValue(program.getParameters()[i].getValue());
			this.parameters[i].setDescription(program.getParameters()[i].getDescription());
		}
		this.algorithmDescription=program.getAlgorithmDescription();
	}
	
	public DodoProgram(int numofparameters){
		parameters =new Parameter[numofparameters];
		for(int i=0;i<numofparameters;i++){
			parameters[i]=new Parameter();
		}
		this.numOfParameters=numofparameters;
		String date = Calendar.getInstance().getTime().toString(); //get date
		this.uploadDate=date.substring(24,28)+date.substring(4,7)+date.substring(8,10);
	
	}
	public void copy(DodoProgram pro){
		this.algorithmName=pro.getAlgorithmName();
		this.algorithmCategory=pro.getAlgorithmCategory();		
		this.uploadDate=pro.getUploadDate();		
		this.numOfParameters=pro.getNumOfParameters();
		parameters =new Parameter[this.numOfParameters];
		for(int i=0;i<this.getNumOfParameters();i++){
			this.parameters[i] =new Parameter();
			this.parameters[i].setName(pro.getParameters()[i].getName());
			this.parameters[i].setType(pro.getParameters()[i].getType());
			this.parameters[i].setValue(pro.getParameters()[i].getValue());
			this.parameters[i].setDescription(pro.getParameters()[i].getDescription());
		}
		this.algorithmDescription=pro.getAlgorithmDescription();
	}
	
	public void reset(){
		//parameters =new Parameter[this.numOfParameters];
		for(int i=0;i<numOfParameters;i++){
			//this.parameters[i] =new Parameter();
			//this.parameters[i].setType("");
			this.parameters[i].setValue("");
			//this.parameters[i].setDescription("");
		}		
		this.algorithmDescription="";
	}

	public static DodoProgram getDodoProgram()
	{
		if(dodoProgram==null)
			System.out.println("dodoProgram is null");
		return dodoProgram;
	}
	
	public String getAlgorithmName(){
		return algorithmName;
	}
	
	public String getAlgorithmCopyPathOnLocal(){
		return copyPathOnLocal;
	}
	
	public String getUploadDate(){
		return uploadDate;
	}
	public String getAlgorithmCategory(){
		return algorithmCategory;
	}
	
	public int getNumOfParameters(){
		return numOfParameters;
	}
	
	public String getAlgorithmDescription(){
		return algorithmDescription;
	}
	
	public Parameter[] getParameters(){
		return parameters;
	}
	
	public void setAlgorithmName(String algorithmName){
		this.algorithmName=algorithmName;
	}

	
	public void setAlgorithmCategory(String algorithmCategory){
		this.algorithmCategory=algorithmCategory;
	}
	
	public void setAlgorithmDescription(String algorithmDescription){
		this.algorithmDescription=algorithmDescription;
	}
	
	public void setcopyPathOnLocal(String copyPathOnLocal){
		this.copyPathOnLocal=copyPathOnLocal;
	}
	
	public void setUploadDate(String uploadDate){
		this.uploadDate=uploadDate;
	}
	
	public void setParameters(Parameter[] parameters){
		for(int i=0;i<numOfParameters;i++){
			this.parameters[i].setName(parameters[i].getName());
			this.parameters[i].setType(parameters[i].getType());
			this.parameters[i].setValue(parameters[i].getValue());
			this.parameters[i].setDescription(parameters[i].getDescription());
		}
	}
	
	public void setNumberOfParameters(int i){
		this.numOfParameters=i;
	}

}
