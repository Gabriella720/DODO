package toolBox.algorithmsManager.applications;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenarateXml {
	public void genarate(DodoProgram pro,String path,int index){
		 Document document = DocumentHelper.createDocument();

	     Element root = document.addElement("AlgorithmConfiguration");	    
	     root.addComment("An XML file for algorithm configuration");	    
	     Element algorithmname =  root.addElement("AlgorithmName");	    
	     algorithmname.addAttribute("value", pro.getAlgorithmName());	     
	     Element algorithmcategory =  root.addElement("AlgorithmCategory");	    
	     algorithmcategory.addAttribute("value", pro.getAlgorithmCategory());	     
	     Element numofparameters =  root.addElement("NumOfParameters");	    
	     numofparameters.addAttribute("value", pro.getNumOfParameters()+"");	     
	     Element algorithmdescription =  root.addElement("AlgorithmDescription");	    
	     algorithmdescription.addAttribute("value", pro.getAlgorithmDescription());	     
	     Element parameters = root.addElement("Parameters");
	     
	     for(int i=0;i<pro.getNumOfParameters();i++){
	    	 Element parameter= parameters.addElement("parameter"+(i+1));
	    	 Element parameterID= parameter.addElement("ID");
	    	 parameterID.addAttribute("value", i+"");
	    	 Element parameterName= parameter.addElement("Name");
	    	 parameterName.addAttribute("value", pro.getParameters()[i].getName());
	    	 Element parameterType= parameter.addElement("Type");
	    	 parameterType.addAttribute("value", pro.getParameters()[i].getType());
	    	 Element parameterValue= parameter.addElement("Value");
	    	 parameterValue.addAttribute("value", pro.getParameters()[i].getValue());
	    	 Element parameterDescription= parameter.addElement("Description");
	    	 parameterDescription.addAttribute("value", pro.getParameters()[i].getDescription());
	     }
	     try{
	    	 File fileFolder = new File(path);
	    	 if (!fileFolder.exists()){
	    		 fileFolder.mkdirs();
	    	 }
	    	 String filePath=path+pro.getAlgorithmName();
	    	 File file=new File(filePath+".xml");
	    	 int i=0;
	    	 if(index!=-1){
	    		 file=new File(filePath+"_"+index+".xml");
	    	 }
	    	 else{
	    		 while(file.exists()){
	    			 i++;
	    			 file=new File(filePath+"_"+i+".xml");
	    		 }
	    	 }
	    	 System.out.println("gosh"+file.getPath());
	          XMLWriter output = new XMLWriter(new FileWriter(file.getPath()));
	          output.write(document);
	          output.close();
	     }
	     catch(IOException e1)
	     {System.out.println(e1.getMessage());} 
	}

}
