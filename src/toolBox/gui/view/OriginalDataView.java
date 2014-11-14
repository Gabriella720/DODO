package toolBox.gui.view;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class OriginalDataView extends Shell {		
	
	String filePath = null; 
	Combo xAxis = null;
	Combo yAxis = null;
	Button viewButton = null;
	Label viewPrint = null;
	Scatter scatter = null;
	
	String attributes[] = null;
	/**
	 * Launch the application.
	 * @/**
	 * Launch the application.
	 * @param args
	 */

	/**
	 * Create the shell.
	 * @param display
	 * @wbp.parser.constructor
	 */
	public OriginalDataView(Display display, String name) {
		super(display, SWT.SHELL_TRIM);
		setLayout(null);
		scatter = new Scatter("Scatter",name,1);
		//scatter = new Scatter("Scatter" , "hdfs://localhost:54310/usr/local/hadoop/data/cpu");
		attributes = scatter.getAttributes();
		createContents();
		
	}
	
	public OriginalDataView(String path){
		super(Display.getDefault(), SWT.SHELL_TRIM);
		setLayout(new GridLayout(1, false));
		createContents();
		filePath = path;
	}
	
	public int viewType(String URL){
		int type = 0;
		Configuration conf = new Configuration(); 
        conf.set("hadoop.job.ugi", "hadoop-user,hadoop-user");          
        //FileSystem是用户操作HDFS的核心类，它获得URI对应的HDFS文件系统 
        FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create(URL),conf);
			FSDataInputStream in = null;        
			BufferedReader reader = null;
			String line = null;
			in = fs.open( new Path(URL) );
			reader = new BufferedReader(new InputStreamReader(in));
			line = reader.readLine();
			while(line!=null){
				if(line.startsWith("@relation"))
					return 0;
				else if(line.startsWith("@clustern"))
					return 1;
				line = reader.readLine();
			}		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return -1;
		} 
        
		return -1;
	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("Data View");
		setSize(723, 515);
		Label lblXaxis = new Label(this, SWT.NONE);
		lblXaxis.setBounds(5, 11, 40, 18);
		lblXaxis.setText("X-axis");				
		
		Label lblYaxis = new Label(this, SWT.NONE);
		lblYaxis.setBounds(6, 46, 39, 18);
		lblYaxis.setText("Y-axis");
		
		createCombos();
		createCanvas();
		createButtons();

	}
	
	protected void createCombos(){
		xAxis = new Combo(this, SWT.NONE);
		xAxis.setBounds(50, 5, 471, 30);
		xAxis.setItems(attributes);
		yAxis = new Combo(this, SWT.NONE);
		yAxis.setBounds(50, 40, 471, 30);
		yAxis.setItems(attributes);
	}
	
	protected void createButtons(){
		viewButton = new Button(this, SWT.NONE);
		viewButton.setBounds(527, 5, 189, 65);
		viewButton.setText("View");
		viewButton.addSelectionListener(new SelectionListener(){
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				int x = xAxis.getSelectionIndex();
				int y = yAxis.getSelectionIndex();
				if(x*y>=0)
					try {
						scatter.getScatter(x, y);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					viewPrint.setImage(new Image(null, "output/" + x + "_" + y + ".jpg"));
				
			}
		});
	}
	
	protected void createCanvas(){
		viewPrint = new Label(this,SWT.BORDER);
		viewPrint.setBounds(5, 79, 711, 396);
	}

	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
