package toolBox.gui.view;

import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;
import org.jfree.data.xy.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Scatter {
	
	private static DefaultXYDataset xydataset = null;
	private String URL = null;
	private static String Title = null;
	private double[][] data = null;
	private static int attributes;
	private static int numOfValues;
	private static double xmin, xmax, ymin, ymax;
	private static DataInput datainput;
	public Scatter(String t, String u, int type){	
		URL = u;
		Title = t;
		datainput = new DataInput(URL,type);
		getData(t);
	}
	
	public Scatter(String t, double[][] in, int r, int c){
		Title = t;
		datainput = new DataInput(in, r, c);
		getData(t);
	}
	void getData(String title){		
		xydataset = new DefaultXYDataset();
//		datainput = new DataInput(URL);
		data = datainput.getData();
		//System.out.print(data[0][2] + " " + data[1][2] + "\n");
		//xydataset.addSeries(title, data);
		attributes = datainput.getAtrributes();
		numOfValues = datainput.getNums();
//		xmax = (datainput.getMax())[0];
//		ymax = (datainput.getMax())[1];
//		xmin = (datainput.getMin())[0];
//		ymin = (datainput.getMin())[1];
	}
	
	void setXYDataset(int x, int y){
		double[][] xydatas = new double[2][numOfValues];
		for(int i=0; i<numOfValues; i++){
			xydatas[0][i] = data[i][x];
			xydatas[1][i] = data[i][y];
			//System.out.print(data[x][i] + " " + data[y][i] + "\n");
		}
		xmax = (datainput.getMax())[x];
		ymax = (datainput.getMax())[y];
		xmin = (datainput.getMin())[x];
		ymin = (datainput.getMin())[y];
		xydataset.addSeries(Title, xydatas);
	}
	
	private String getURL(){
		return URL;
	}
	
	private String getTitle(){
		return Title;
	}
	
	private DefaultXYDataset getDataset(){
		return xydataset;
	}
	
	public JFreeChart createChart(XYDataset xydataset,  
        String tile, String xlabel, String ylabel) {  							
        JFreeChart jfreechart = ChartFactory.createScatterPlot(tile,  
                xlabel, ylabel, xydataset, PlotOrientation.VERTICAL, true, false,  
                false);  
        jfreechart.setBackgroundPaint(Color.white);  
        jfreechart.setBorderPaint(Color.GREEN);  
        jfreechart.setBorderStroke(new BasicStroke(1.5f));  
        XYPlot xyplot = (XYPlot) jfreechart.getPlot();  
        xyplot.setNoDataMessage("No Data!");  
        xyplot.setNoDataMessageFont(new Font("", Font.BOLD, 14));  
        xyplot.setNoDataMessagePaint(new Color(87, 149, 117));  
  
        xyplot.setBackgroundPaint(new Color(255, 253, 246));  
        ValueAxis vaaxis = xyplot.getDomainAxis();  
        vaaxis.setAxisLineStroke(new BasicStroke(1.5f));
       // vaaxis.setVisible(false);
        
        ValueAxis va = xyplot.getDomainAxis(0);  
        va.setAxisLineStroke(new BasicStroke(1.5f));
        //va.setVisible(false);
  
        va.setAxisLineStroke(new BasicStroke(1.5f));   
        va.setAxisLinePaint(new Color(215, 215, 215));   
        xyplot.setOutlineStroke(new BasicStroke(1.5f));      
        va.setLabelPaint(new Color(10, 10, 10)); 
        va.setTickLabelPaint(new Color(102, 102, 102)); 
        ValueAxis axis = xyplot.getRangeAxis();  
        axis.setAxisLineStroke(new BasicStroke(1.5f));
       // axis.setVisible(false);
  
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot  
                .getRenderer();    
 
        xylineandshaperenderer.setBaseFillPaint(Color.BLUE);
        xylineandshaperenderer.setUseFillPaint(true);
        xylineandshaperenderer.setSeriesOutlinePaint(0, Color.WHITE);  
        xylineandshaperenderer.setUseOutlinePaint(true);  
        NumberAxis numberaxis = (NumberAxis) xyplot.getDomainAxis();  
        numberaxis.setAutoRangeIncludesZero(false);  
        numberaxis.setTickMarkInsideLength(2.0F);  
        numberaxis.setTickMarkOutsideLength(0.0F);  
        numberaxis.setAxisLineStroke(new BasicStroke(1.5f));  
        numberaxis.setUpperBound(xmax);  
        numberaxis.setLowerBound(xmin);  
        NumberAxis numberaxis1 = (NumberAxis) xyplot.getRangeAxis();  
        numberaxis1.setTickMarkInsideLength(2.0F);  
        numberaxis1.setTickMarkOutsideLength(0.0F);  
        numberaxis1.setUpperBound(ymax);  
        numberaxis1.setLowerBound(ymin);  
        numberaxis1.setAxisLineStroke(new BasicStroke(1.5f));  
        
        //let legend and title not dispaly
        jfreechart.getLegend().setVisible(false);
        jfreechart.getTitle().setVisible(false);
       
        return jfreechart;  
    }  
	
	public void drawScatterChart(int x, int y) throws IOException {   
        JFreeChart chart = createChart(xydataset, Title, "x", "y");   
        String filename = "";  
        String graphURL = "";  
        File fp = new File("output/" + x + "_" + y + ".jpg");
        ChartUtilities.saveChartAsJPEG(fp,chart,711,396);
  
    }  
	
	public String[] getAttributes(){
		
		String attributeNames[] = datainput.getAttributes();
		return attributeNames;
	}
	
	public void getScatter(int x, int y) throws IOException{
		this.setXYDataset(x, y);
		this.drawScatterChart(x, y);
	}
	
//	public static void main(String[] args) throws IOException{
//		Scatter scatter = new Scatter("TestScatter","data/data.txt");
////		scatter.setXYDataset(0, 1);
////		scatter.drawScatterChart(0, 1);
//		
//		for(int i=0; i<attributes; i++)
//			for(int j=0; j<attributes; j++){
//				scatter.setXYDataset(i, j);
//				scatter.drawScatterChart(i, j);
//			}
//		//scatter.setXYDataset(0, 1);
//		//scatter.drawScatterChart(0, 1);
//		 
//		
//	}

}

