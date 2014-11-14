package toolBox.gui.algorithms;

//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;

import org.dom4j.DocumentException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;
import toolBox.algorithmsManager.applications.AlgorithmXmlAnalysis;
import toolBox.algorithmsManager.applications.AlgorithmsMainController;
import toolBox.algorithmsManager.applications.CopyFile;
import toolBox.algorithmsManager.applications.DodoProgram;
import toolBox.core.system.MainController;
import toolBox.core.utility.UtilityParameters.ChildSystemsName;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AlgorithmsMainBoard {
	
	AlgorithmsMainController algorithmsController = null;
	final Display display = Display.getDefault();
	Shell shell = null;
	private Text selectText=null;
	private Text jarText=null;
	private Text xmlFileText;
	private Text xmlText=null;
	private Text uploadText;
	
	DodoProgram[] outputPrograms=null;
	DodoProgram program=null;
	AlgorithmXmlAnalysis analysis=new AlgorithmXmlAnalysis();
	CopyFile filecopy=new CopyFile();
	String localCopyPath=null;
	File xmlFile=null;
	int tableHeight=0;
	int tableWidth=0;
	int tableCount;

	
	Button DisplayButton =null;
	Button SaveButton =null;
	Button configureButton=null;
	Button uploadButton=null;
	ArrayList arraylist;
	
	public AlgorithmsMainBoard(AlgorithmsMainController controller)
	{
		algorithmsController = controller;
	}
	
	public void setController(AlgorithmsMainController controller)
	{
		algorithmsController = controller;
	}
	
	//-------------------------------------------------------------------------------------------

	public void paintTheBoard() throws DocumentException, IOException
	{
		shell =  new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setSize(913, 552);
		shell.setText("Algorithms Manager");
		
		creatUploadGroup();
		creatSelectGroup();
		creatControllGroup();
		
		shell.addDisposeListener(new  DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				MainController.getMainController().renewChildSystemsButton(ChildSystemsName.algorithms);
			}
		});


		shell.layout();
		shell.open();
	}
	
	public void creatUploadGroup() throws DocumentException{
		final Group UploadGroup = new Group(shell, SWT.NONE);
		UploadGroup.setText("Algorithm Upload");
		UploadGroup.setBounds(28, 36, 851, 219);
		
		Label configurationLabel = new Label(UploadGroup, SWT.NONE);
		configurationLabel.setBounds(47, 69, 182, 18);
		configurationLabel.setText("Configuration Information:");
		
		xmlFileText = new Text(UploadGroup, SWT.BORDER);
		xmlFileText.setBounds(254, 58, 372, 29);
		
		configureButton = new Button(UploadGroup, SWT.NONE);
		configureButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				AlgorithmConfigurationBoard algorithmConfigurationBoard=new AlgorithmConfigurationBoard();
				boolean ok=algorithmConfigurationBoard.open(xmlFileText);
				if(ok)uploadButton.setEnabled(true);
				uploadButton.redraw();
			}
		});
		
		Label UploadLabel = new Label(UploadGroup, SWT.NONE);
		UploadLabel.setBounds(47, 136, 182, 18);
		UploadLabel.setText("Upload Your Algorithm:");
		
		uploadText = new Text(UploadGroup, SWT.BORDER);
		uploadText.setBounds(254, 125, 372, 29);
		
		uploadButton = new Button(UploadGroup, SWT.NONE);
		uploadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					program=analysis.Analysis(xmlFileText.getText());
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				FileDialog dialog = new FileDialog(shell,SWT.NONE);
            	dialog.setText("Add Algorithm into Dodo ToolBox");
            	dialog.setFilterExtensions(new String[]{"*.jar"});
            	dialog.setFilterPath("/home/");
            	String txt=dialog.open();
            	if(txt!=null){
            		uploadText.setText(txt);
            		String src=uploadText.getText();				
            		String dst =program.getAlgorithmCopyPathOnLocal();
               		filecopy.copyAToB(src, dst);
            		uploadButton.setEnabled(false);
            		xmlFileText.setText("");
            		uploadText.setText("");
            	}
			}
			
		});
		uploadButton.setBounds(693, 125, 113, 30);
		uploadButton.setText("Upload");		
		uploadButton.setEnabled(false);
		
		configureButton.setBounds(693, 58, 113, 30);
		configureButton.setText("Configure");
		
	}
	
	public void creatSelectGroup() throws DocumentException, IOException{
		
	}
	
	public void creatControllGroup(){
		Group SaveAndDisplayGroup = new Group(shell, SWT.NONE);
		SaveAndDisplayGroup.setText("Program Control");
		SaveAndDisplayGroup.setBounds(28, 292, 851, 100);
		
		Button ExitButton = new Button(SaveAndDisplayGroup, SWT.NONE);
		ExitButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		ExitButton.setBounds(503, 43, 150, 30);
		ExitButton.setText("Back to Mainboard");
		
		Button saveButton = new Button(SaveAndDisplayGroup, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(shell,SWT.NONE);
            	dialog.setText("Delete Algorithm From Dodo ToolBox");
            	String path = null;
            	try {
					path = new File(".").getCanonicalPath()+"/source/data/algo/UserDefined/";
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	dialog.setFilterPath(path);
            	String txt=dialog.open();
            	if(txt!=null){
            		File file =new File(txt);
            		int length=file.listFiles().length;
            		for(int i=0;i<length;i++){
            			file.listFiles()[i].delete();
            		}
            		file.delete();
            	}
			}
		});
		saveButton.setBounds(217, 43, 150, 30);
	
		saveButton.setText("Delete Algorithm");
	}

	/**
	 * @throws DocumentException 
	 * @throws IOException 
	 * @wbp.parser.entryPoint
	 */
	public void work() throws DocumentException, IOException {
		paintTheBoard();
	}
}
