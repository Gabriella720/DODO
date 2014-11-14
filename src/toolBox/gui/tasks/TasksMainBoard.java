package toolBox.gui.tasks;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobQueueInfo;
import org.apache.hadoop.mapred.JobStatus;
import org.dom4j.DocumentException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import toolBox.algorithmsManager.applications.AlgorithmXmlAnalysis;
import toolBox.algorithmsManager.applications.DodoProgram;
import toolBox.algorithmsManager.applications.Parameter;
import toolBox.core.system.DodoSystem;
import toolBox.core.system.MainController;
import toolBox.core.utility.UtilityParameters;
import toolBox.core.utility.UtilityParameters.ChildSystemsName;
import toolBox.gui.HdfsTreeBoard;
import toolBox.tasksManager.applications.TasksMainController;
import toolBox.tasksManager.functions.TaskSubmission;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Date;

public class TasksMainBoard {
	
	private String hadoopHome = "/home/hadmin/hadoop/hadoop/";//这个应该动态读取，从/etc/profile里面读取
	//FileInputStream in=new FileInputStream("/etc/profile");
	JobClient jobClient = null;
	DodoProgram program=null;
	AlgorithmXmlAnalysis analysis=new AlgorithmXmlAnalysis();
	
	//Table runningJobstable = null;
	Table parameterTable=null;
	TableEditor editor=null;
	Table allJobstable = null;
	Text dataSetDir=null;
	Text algorithmDir=null;
	File xmlFile=null;
	Text outputDir=null;
	Text partOutput=null;
	Text submitInfo=null;
	
	
	JobQueueInfo[] jobQueueInfo=null;
	JobStatus[] jobStatus=null;
	String[][] jobStatusValue=null;
	String[][] parameters=null;
	
	String uri=null;
	int lineNum;
	int EDITABLECOLUMN=4;
	
	static final String[] tableTitle = {"Job ID","Job Priority","Job State","User Name","Start Time","Map Progress","Reduce Progress",};
	
	TasksMainController tasksController = null;
	final Display display = Display.getDefault();
	Shell shell = null;
		
	public TasksMainBoard(TasksMainController controller)
	{
		tasksController = controller;
		this.jobClient=DodoSystem.getDodoSystem().getJobClient();
		//this.programList=DodoSystem.getDodoSystem().getDodoProgram();
	}
	
	public void setController(TasksMainController controller)
	{
		tasksController = controller;
	}
	
	//-------------------------------------------------------------------------------------------

	public void paintTheBoard()
	{
		//shell =  new Shell(display, SWT.CLOSE | SWT.MIN);
        shell =  new Shell();
		shell.setSize(UtilityParameters.CHILD_BOARD_WIDTH, UtilityParameters.CHILD_BOARD_HEIGHT);
		shell.setText("Tasks Manager");
		
		shell.addDisposeListener(new  DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				MainController.getMainController().renewChildSystemsButton(ChildSystemsName.tasks);
			}
		});
		//--------------------------------------------------------------------------------------
		
		TabFolder tabFolder = new TabFolder(shell,SWT.NONE);
		tabFolder.setBounds(20,10, UtilityParameters.TAB_WINDOW_WIDTH, UtilityParameters.TAB_WINDOW_HEIGHT);
		
		createStartJobItem(tabFolder);
		createJobStatusItem(tabFolder);
		createOutputItem(tabFolder);
		
		//---------------------------------------------------------------------------------------
		shell.layout();
		shell.open();
	}

	private void createStartJobItem(TabFolder tabFolder) {
		TabItem item = new TabItem(tabFolder,SWT.NONE);
		item.setText("Submit Job");
		final Composite composite = new Composite(tabFolder,SWT.NONE);	
		item.setControl(composite);
		
		Label label=new Label(composite,SWT.NONE);
		label.setText("select algorithm:");
		label.setBounds( 30, 30, 150, 30);
		
		algorithmDir=new Text(composite,SWT.NONE);
		algorithmDir.setBounds( 200, 30, 550, 30);
		algorithmDir.setMessage("Algorithm Dir:");
		
		Button button=new Button(composite,SWT.NONE);
		button.setText("select");
		button.setBounds(800, 30, 100, 30);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				parameterTable.removeAll();
				FileDialog dialog = new FileDialog(shell,SWT.NONE);
            	dialog.setText("Algorithm Selection Board");
            	try {
					dialog.setFilterPath(new File(".").getCanonicalPath()+"/source/data/algo/");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dialog.setFilterExtensions(new String[]{"*.jar"});
				algorithmDir.setText(dialog.open());
            	String xmlName=new File(algorithmDir.getText()).getParentFile().getName();
            	//System.out.println(xmlName);
            	int t=algorithmDir.getText().lastIndexOf("/");
            	String xmlFilePath=algorithmDir.getText().substring(0,t)+"/"+xmlName+".xml";
            	//System.out.println(xmlFilePath);
            	xmlFile=new File(xmlFilePath);            	
            	try {           		
					program=analysis.Analysis(xmlFile.getPath());
					int lineNo=program.getNumOfParameters();
					parameters=new String [lineNo][5];
					Parameter[] parameter=program.getParameters();
					for(int i=0;i<lineNo;i++){
						parameters[i][0]=String.valueOf(i);
						parameters[i][1]=parameter[i].getName();
						parameters[i][2]=parameter[i].getType();
						parameters[i][3]=parameter[i].getDescription();
						parameters[i][4]=parameter[i].getValue();
						TableItem tableitem=new TableItem(parameterTable,0);
						tableitem.setText(parameters[i]);
						
					}
					
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	//System.out.println(xmlFilePath);

			}
		});
		
		label=new Label(composite,SWT.NONE);
		label.setText("Parameters information:");
		label.setBounds(30, 70, 200, 30);
		
		parameterTable = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		parameterTable.setBounds(30, 110, 750, 280);
		parameterTable.setHeaderVisible(true);
		parameterTable.setLinesVisible(true);
		
		editor=new TableEditor(parameterTable);
		editor.horizontalAlignment=SWT.LEFT;
		editor.grabHorizontal=true;
		editor.minimumWidth=150;
		
		
		parameterTable.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				Control oldEditor=editor.getEditor();
				if(oldEditor!=null)oldEditor.dispose();
				
				TableItem item=(TableItem)e.item;
				if(item==null)return;
				
				final Text newEditor=new Text(parameterTable,SWT.NONE);
				newEditor.setText(item.getText(EDITABLECOLUMN));
				newEditor.addModifyListener(new ModifyListener(){
					public void modifyText(ModifyEvent me){
						Text text=(Text)editor.getEditor();
						editor.getItem().setText(EDITABLECOLUMN,text.getText());
					}
				});
				newEditor.addFocusListener(new org.eclipse.swt.events.FocusAdapter(){
					public void focusLost(FocusEvent e){
						newEditor.dispose();
						
					}
				});
				
				newEditor.selectAll();
				newEditor.setFocus();
				editor.setEditor(newEditor, item, EDITABLECOLUMN);
				item.addDisposeListener(new DisposeListener(){
					public void widgetDisposed(DisposeEvent e){
						newEditor.dispose();
					}
				});
			}
		});
		
		TableColumn paraNo =  new  TableColumn ( parameterTable, SWT.NONE ) ;
		paraNo.setText("No");
		paraNo.setWidth(100);
		
		
		TableColumn paraName =  new  TableColumn ( parameterTable, SWT.NONE ) ;
		paraName.setText("Name");
		paraName.setWidth(100);
		
		TableColumn paraType =  new  TableColumn ( parameterTable, SWT.NONE ) ;
		paraType.setText("Type");
		paraType.setWidth(100);
		
		TableColumn paraDescription=  new  TableColumn ( parameterTable, SWT.NONE ) ;
		paraDescription.setText("Description");
		paraDescription.setWidth(150);
		
		TableColumn paraValue =  new  TableColumn ( parameterTable, SWT.NONE ) ;
		paraValue.setText("Value");
		paraValue.setWidth(300);
		
		//label=new Label(composite,SWT.NONE);
		//label.setBounds(30, 400, 150, 30);
		submitInfo=new Text(composite,SWT.NONE|SWT.READ_ONLY|SWT.WRAP|SWT.V_SCROLL);
		submitInfo.setBounds(30, 400, 750, 100);
		
		button=new Button(composite,SWT.NONE);
		button.setText("Submit");
		button.setBounds(800,300, 100, 30);
		button.addSelectionListener(new SelectionAdapter(){
			 public void widgetSelected(SelectionEvent e) {
				 //System.out.println(dataSetDir.getText()+" "+outputDir.getText());
				TableItem[] tableItem=parameterTable.getItems();
				String parameters[]=new String[tableItem.length];
				for(int i=0;i<tableItem.length;i++){
					parameters[i]=tableItem[i].getText(4);
				}
				//System.out.println(parameters[0]);
				TaskSubmission taskSubmission=new TaskSubmission(algorithmDir.getText(),parameters,submitInfo);
					try {
						taskSubmission.submit();
					} catch (Throwable e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            }
			
		});
	}

	private void createJobStatusItem(TabFolder tabFolder) {
		
		
		TabItem item = new TabItem(tabFolder,SWT.NONE);
		item.setText("Monitor Jobs");
		Composite composite = new Composite(tabFolder,SWT.NONE);	
		item.setControl(composite);
		
		//-------------------------------------Retried Jobs--------------------------------------------------------
		Label label=new Label(composite,SWT.NONE);
		label.setText("All Jobs:");
		label.setBounds(30, 10, 200, 30);
		
		allJobstable=new Table(composite,SWT.MULTI|SWT.FULL_SELECTION);
		allJobstable.setHeaderVisible(true);
		allJobstable.setLinesVisible(true);
		allJobstable.setBounds(30, 50, 900, 400);
		
		TableColumn col=new TableColumn(allJobstable,SWT.NONE);
		col.setText(tableTitle[0]);
		col.setWidth(150);
		
		col=new TableColumn(allJobstable,SWT.NONE);
		col.setText(tableTitle[1]);
		col.setWidth(150);
		
		col=new TableColumn(allJobstable,SWT.NONE);
		col.setText(tableTitle[2]);
		col.setWidth(150);
		
		col=new TableColumn(allJobstable,SWT.NONE);
		col.setText(tableTitle[3]);
		col.setWidth(150);
		
		col=new TableColumn(allJobstable,SWT.NONE);
		col.setText(tableTitle[4]);
		col.setWidth(150);
		
		col=new TableColumn(allJobstable,SWT.NONE);
		col.setText(tableTitle[5]);
		col.setWidth(150);
		
		col=new TableColumn(allJobstable,SWT.NONE);
		col.setText(tableTitle[6]);
		col.setWidth(150);
		
		try {
			configJobTable();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		Button button=new Button(composite,SWT.NONE);
		button.setText("refresh");
		button.setBounds(480, 460, 100, 30);
		button.addSelectionListener(new SelectionAdapter(){
			 public void widgetSelected(SelectionEvent e) {
				 //System.out.println(dataSetDir.getText()+" "+outputDir.getText());
				 allJobstable.removeAll();
				 try {
					configJobTable();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            }
			
		});
		
		allJobstable.addMouseListener(new MouseAdapter(){
			public void mouseDoubleClick(MouseEvent e){
				int cellindex=allJobstable.getSelectionIndex();
				TableItem item=allJobstable.getItem(cellindex);
				String jobId=item.getText(0);
				//new JobStatusTableBoard(jobClient,jobId);
				try {
					try {
						tasksController.ShowJobDetails(jobClient,jobId);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (IllegalArgumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		//----------------------------------------------------------------------------------------------------------------------------------------
		
	}

	private void createOutputItem(TabFolder tabFolder) {
		
		TabItem item = new TabItem(tabFolder,SWT.NONE);
		item.setText("Visualization");
		Composite composite = new Composite(tabFolder,SWT.NONE);	
		item.setControl(composite);
		
		Label label=new Label(composite,SWT.NONE);
		label.setText("the output file directory:");
		label.setBounds(30, 30, 200, 30);
		
		final Text output=new Text(composite,SWT.NONE);
		output.setMessage("the directory of the output");
		output.setBounds(250,30, 500, 30);
		
		Button select=new Button(composite,SWT.NONE);
		select.setText("select");
		select.setBounds(800, 30, 100, 30);
		select.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	new HdfsTreeBoard(output,"");
            }
        });
		
		label=new Label(composite,SWT.NONE);
		label.setText("the output file size:");
		label.setBounds(30, 70, 200, 30);
		
		label=new Label(composite,SWT.NONE);
		label.setText("");
		label.setBounds(250,70, 500, 30);
		
		label=new Label(composite,SWT.NONE);
		label.setText("show head");
		label.setBounds(30, 110, 100, 30);
		
		final Combo combo=new Combo(composite,SWT.READ_ONLY);
		combo.setBounds(140, 110, 100, 30);
		String[] num={"10","20","30","40","50"};
		combo.setItems(num);

		
		label=new Label(composite,SWT.NONE);
		label.setText("of the output file");
		label.setBounds(250, 110, 200, 30);
		
		Button ok=new Button(composite,SWT.NONE);
		ok.setText("ok");
		ok.setBounds(500, 110,150 ,30 );

		ok.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(SelectionEvent e) {
            	if(combo.getText().toString()==null||combo.getText().toString()==""){
    				MessageBox linenumMb = new MessageBox(shell,SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
    				linenumMb.setText("Tip:");
    				linenumMb.setMessage("lineNum is null,Please select lineNum you want to show first!");
    				linenumMb.open();
    			}
            	lineNum=Integer.parseInt(combo.getText().toString());
            	uri=output.getText().toString();
            	if(uri==null||uri==""){
    				MessageBox urimb = new MessageBox(shell,SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
    				urimb.setText("Tip:");
    				urimb.setMessage("uri is null,Please select a file to show first!");
    				urimb.open();
    			}
            	partOutput.setText(readOutputFile(uri,lineNum));
            }
        });
		
		partOutput=new Text(composite,SWT.READ_ONLY|SWT.WRAP|SWT.V_SCROLL);
		partOutput.setBounds(30, 150, 900, 300);
		
		Button outputAnalysis=new Button(composite,SWT.NONE);
		outputAnalysis.setText("Output Analysis");
		outputAnalysis.setBounds(800, 470, 130, 30);
	}


	public void work() {
		paintTheBoard();
	}
	
	public void configJobTable() throws IOException, InterruptedException{
		//jobTracker=jobTracker.startTracker((JobConf) jobClient.getConf());
		jobStatus=jobClient.getAllJobs();
		
		if(jobStatus!=null){
			jobStatusValue = new String[jobStatus.length][tableTitle.length];
			for(int i=0;i<jobStatus.length;i++){
				jobStatusValue[i][0]=jobStatus[i].getJobID().toString();
				jobStatusValue[i][1]=jobStatus[i].getJobPriority().toString();
				//jobStatusValue[i][2]=jobTracker.getJobProfile(jobStatus[i].getJobID()).getJobName();
				jobStatusValue[i][2]=JobStatus.getJobRunState(jobStatus[i].getRunState());
				jobStatusValue[i][3]=jobStatus[i].getUsername();
				jobStatusValue[i][4]=new Date(jobStatus[i].getStartTime()).toString();
				jobStatusValue[i][5]=""+(int)jobStatus[i].mapProgress()*100+"%";
				jobStatusValue[i][6]= ""+(int)jobStatus[i].reduceProgress()*100+"%";
				//jobStatusValue[i][8]=new Date(jobTracker.getJob(jobStatus[i].getJobID()).getFinishTime()).toString();
				TableItem tableitem=new TableItem(allJobstable,SWT.NONE);
				tableitem.setText(jobStatusValue[i]);
			}
		}
	}
	
	public String readOutputFile(String uri,int lineNum){
		Configuration conf = new Configuration();
		String str="";
		try {
			FileSystem fs = FileSystem.get(URI.create(uri), conf);
			FSDataInputStream in = fs.open(new Path(uri));
			in = fs.open(new Path(uri));		
			
			for(int i=0;i<lineNum;i++){
					str=str+in.readLine()+"\n";
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return str;
	}

	
	/*public void paintAlgorithmBoard(Composite composite,int j){
		Group group = new Group(composite,SWT.NONE);
		group.setText("the description of the execution plan:");
		group.setBounds(30, 150, 720, 350);
		
		Label label=new Label(group,SWT.NONE);
		label.setText("Algorithm Name:");
		label.setBounds(30,30,200,30);
		
		label=new Label(group,SWT.NONE);
		label.setText(program.getAlgorithmName()+j);
		label.setBounds(250,30 ,400 ,30 );
		
		label=new Label(group,SWT.NONE);
		label.setText("Parameter Number:");
		label.setBounds(30,70,200,30);
		
		label=new Label(group,SWT.NONE);
		label.setText(Integer.toString(program.getNumOfParameters()));
		label.setBounds(250,70,400,30);
	
		label=new Label(group,SWT.NONE);
		label.setText("Algorithm Category:");
		label.setBounds(30,110,200,30);
		
		label=new Label(group,SWT.NONE);
		label.setText(program.getAlgorithmCategory());
		label.setBounds(250,110,400,30);
	
		label=new Label(group,SWT.NONE);
		label.setText("Upload Date:");
		label.setBounds(30,150,200,30);
		
		label=new Label(group,SWT.NONE);
		label.setText(program.getUploadDate());
		label.setBounds(250,150,400,30);
		
		label=new Label(group,SWT.NONE);
		label.setText("Algorithm Description:");
		label.setBounds(30,190,200,30);
		
		label=new Label(group,SWT.NONE);
		label.setText(program.getAlgorithmDescription());
		label.setBounds(250,190,400,30);
		
		Group parameterGroup = new Group(group,SWT.NONE);
		parameterGroup.setText("parameters:");
		parameterGroup.setBounds(30, 230, 600, 120);
		
		for(int i=0;i<program.getNumOfParameters();i++){
			Parameter parameter[]=program.getParameters();
			label=new Label(parameterGroup,SWT.NONE);
			label.setText(parameter[i].getName());
			label.setBounds(30,30+i*30,200,30);
			
			label=new Label(parameterGroup,SWT.NONE);
			label.setText(parameter[i].getValue());
			label.setBounds(300,30+i*30,200,30);
		
		}
	}*/
}
