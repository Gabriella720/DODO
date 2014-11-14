package toolBox.core.utility;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.*;
import java.util.List;

public class XMLUtility {   
	
	private Document doc = null;
	private Element root;
	
	public void saveXml(String fileName) {
		Format format = Format.getCompactFormat();
		format.setIndent("  ");
		XMLOutputter out = new XMLOutputter(format);
		try {
			out.output(doc, new FileOutputStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}   
	public void addXml(String name,String value) throws JDOMException, FileNotFoundException, IOException {
		 Element element = new Element("property");
		 Element element1 = new Element("name");
		 element1.setText(name);
		 Element element2 = new Element("value");
		 element2.setText(value);
		 element.addContent(element1);
		 element.addContent(element2);
		 root.addContent(element);
		 doc.setRootElement(root);
	 }
	 
	public void update(List<Element> list,String name,String value) throws JDOMException,IOException {
		boolean find = false;
		for(Element e:list) {
			if(e.getChild("name").getValue().equals(name)) {
				find = true;
				if(!value.equals("")){
					e.getChild("value").setText(value);
					System.out.println("save: "+name+'\n'+"value: "+value);
					return;
				}
			}
		}
		if(!find) {
			addXml(name,value);
			System.out.println("add: "+name+'\n'+"value: "+value);
		}
	}
	
	public List readXml(String fileName) {   
		SAXBuilder builder = new SAXBuilder();
		InputStream file1 = null;
		try {
			file1 = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			//doc = builder.build(getClass().getClassLoader().getResourceAsStream(fileName));
			doc=builder.build(file1);
			//if(doc==null)System.out.println("doc is null");
			//else System.out.println("doc is not null");
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		root = doc.getRootElement();
		List<Element> list = root.getChildren();
		return list;
	}   
}   
