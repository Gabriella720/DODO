package toolBox.datasetsManager.applications;

import org.dom4j.Element;

import java.util.List;

public class XMLReader {
	private Element root;
	private String name;
	public String getName() {
		return name;
	}
	private int count;
	public int getCount() {
		return count;
	}
	private String[] attributeArray;
	public String[] getAttributeArray() {
		return attributeArray;
	}
	private String[] typeArray;
	public String[] getTypeArray() {
		return typeArray;
	}
	public XMLReader(Element root){
		this.root = root;
		name = root.element("Relation_name").attributeValue("Name");
		count = Integer.parseInt(root.element("number_of_attributes").attributeValue("number"));
		attributeArray = new String[count];
		typeArray = new String[count];
		List<Element> e = root.elements("Attribute");
		
		for(int i = 0; i < count; i++){		
			attributeArray[i] = e.get(i).element("Name").getText();
			typeArray[i] = e.get(i).element("Type").getText();
		}
		
	}
	
	

	
	

}
