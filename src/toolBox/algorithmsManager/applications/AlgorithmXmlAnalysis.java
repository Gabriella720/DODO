package toolBox.algorithmsManager.applications;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AlgorithmXmlAnalysis {
	
	AlgorithmXmlAnalysis analysis=null;
	
	public AlgorithmXmlAnalysis(){
		
	}
	
	@SuppressWarnings("unchecked")
	public DodoProgram Analysis(String path) throws DocumentException, IOException{
		DodoProgram program=new DodoProgram();
		
		File file=new File(path);
		SAXReader reader = new SAXReader();
		Document doc= reader.read(file);
		Element rootEle =doc.getRootElement();		
		
		program.copyPathOnLocal=new File(".").getCanonicalPath()+"/source/data/algo/UserDefined/";
		
		program.setAlgorithmCategory(rootEle.element("AlgorithmCategory").attributeValue("value"));
		
		if(rootEle.element("AlgorithmCategory").attributeValue("value").equals("Classification")){
			program.copyPathOnLocal += "Classification/";
		}
		else if(rootEle.element("AlgorithmCategory").attributeValue("value").equals("Cluster")){
			program.copyPathOnLocal += "Cluster/";
		}
		else{
			program.copyPathOnLocal += "Other/";
		}
		
		program.setAlgorithmName(rootEle.element("AlgorithmName").attributeValue("value"));
		
		program.copyPathOnLocal += program.getAlgorithmName();
		program.copyPathOnLocal += "/";
		
		program.numOfParameters=Integer.parseInt(rootEle.element("NumOfParameters").attributeValue("value"));
		
		program.algorithmDescription=rootEle.element("AlgorithmDescription").attributeValue("value");
		
		program.parameters =new Parameter[program.numOfParameters];

		for(int i=0;i<program.getNumOfParameters();i++){
			List<Element> parameters=rootEle.element("Parameters").elements();
			
			int j=Integer.parseInt(parameters.get(i).element("ID").attributeValue("value"));
			program.getParameters()[j]=new Parameter();
			program.getParameters()[j].setName(parameters.get(i).element("Name").attributeValue("value"));
			program.getParameters()[j].setType(parameters.get(i).element("Type").attributeValue("value"));
			program.getParameters()[j].setValue(parameters.get(i).element("Value").attributeValue("value"));
			program.getParameters()[j].setDescription(parameters.get(i).element("Description").attributeValue("value"));
		}
		return program;
	}
	

}
