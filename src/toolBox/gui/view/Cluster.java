package toolBox.gui.view;

import javax.swing.*;
import java.awt.*;

 

public class Cluster extends JFrame {

	
	
	private static final long serialVersionUID = 1L;
	
	class MyPanel extends JPanel {
		private String URL = null;
		private String Title = null;
		private double[][] data = null;
		private int attributes;
		private int numOfValues;
		private double xmin, xmax, ymin, ymax;
		private DataInput datainput;
//		
		public void setPath(String t, String u, int type){	
			URL = u;
			Title = t;
			datainput = new DataInput(URL,type);
			getData(t);
		}
		void getData(String title){		
			data = datainput.getData();
			attributes = datainput.getAtrributes();
			numOfValues = datainput.getNums();
		}
		
		private String getURL(){
			return URL;
		}	
	
		private static final long serialVersionUID = 1L;		
		public void paint(Graphics graphics) {		
			super.paint(graphics);		
			Graphics g2d = (Graphics2D) graphics;			
			int blank_x  = 10;
			int blank_y  = 60;
			int x_r = 10;
			int y_r = 10;
			int type = 0;
			Font font=new Font("Georgia",Font.BOLD,30);
			g2d.setFont(font);
			g2d.drawString("Cluster", 250, 30);
			g2d.setColor(Color.white);
			g2d.fillRect(blank_x, blank_y, 600, 600);						
			for(int x=0; x<numOfValues; x++){				
					x_r = (int)data[x][0] + blank_x;
					y_r = (int)data[x][1] + blank_y;
					type = (int)data[x][2];
					g2d.setColor(new Color((type*77)%200,(type*17)%200,(type*177)%200));
					g2d.drawLine(x_r-4, y_r, x_r+4, y_r);
					g2d.drawLine(x_r, y_r-4, x_r, y_r+4);
			}
		
		}
	
	}
	
	public Cluster(String name) {
		MyPanel panel = new MyPanel();
		panel.setPath("Cluster", name, 1);
		panel.setBackground(new Color(205,205,205));
		this.add(panel);	
		this.setSize(620, 700);
	
	}
	
	 
	
//	public static void main(String[] args) {
//	
//		Cluster frame = new Cluster();		
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
//		frame.setVisible(true);
//	
//	}

}