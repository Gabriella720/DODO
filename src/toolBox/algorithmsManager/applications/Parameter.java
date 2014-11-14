
package toolBox.algorithmsManager.applications;

public class Parameter {
	String name=null;
	String type=null;
	String value=null;
	String description=null;
	
	public Parameter(){

	}
	
	public Parameter(String name, String type, String value, String description){
		this.name=name;
		this.type=type;
		this.value=value;
		this.description=description;
		
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setValue(String value){
		this.value=value;
	}
	
	public void setType(String type){
		this.type=type;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
 

}
