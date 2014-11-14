package toolBox.gui.view;

public class enumItem {
	String enumItem[];
	int num;
	
	public enumItem(String s){
		//construct with form like "a,b,c"
		enumItem = s.split(", ");
		num = enumItem.length;
	}
	
	public int getItemNumber(String s){
		
		int n=0;
		for(n=0; n<num; n++){
			if(s.equals(enumItem[n]))
				return n;
		}
		return -1;
	}
}
