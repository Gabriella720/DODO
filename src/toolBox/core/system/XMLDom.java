package toolBox.core.system;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XMLDom {
	public static List XML2List(String xmlStr,String elementName){
		
		List list=new ArrayList();
		
		Document document=null;
		try{
			document=DocumentHelper.parseText(xmlStr);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		
		Element root=document.getRootElement();
		
		Element element=root.element(elementName);
		Iterator iter=element.elementIterator();
		
		while(iter.hasNext()){
			
		}
		return list;
		
		
	}

}
