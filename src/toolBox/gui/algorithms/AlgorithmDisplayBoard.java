package toolBox.gui.algorithms;

import org.dom4j.DocumentException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;
import toolBox.algorithmsManager.applications.AlgorithmXmlAnalysis;
import toolBox.algorithmsManager.applications.DodoProgram;
import toolBox.algorithmsManager.applications.GenarateXml;
import toolBox.algorithmsManager.applications.Parameter;

import java.io.File;
import java.io.IOException;


public class AlgorithmDisplayBoard {
	final Display display = Display.getDefault();

	private Text[] txtValue;
	//private Text[] txtDescription;
	AlgorithmXmlAnalysis analysis=new AlgorithmXmlAnalysis();

	TableItem it;
	Shell shell;
	
	DodoProgram program=null;
	/**
	 * @wbp.parser.entryPoint
	 */
	public void creatMainGroup(Group group,DodoProgram program){		
		
		group.setText("Main Information");
		group.setBounds(10, 10, 578, 346);
		
		Label lblAlgorithmName = new Label(group, SWT.NONE);
		lblAlgorithmName.setText("Algorithm Name:");
		lblAlgorithmName.setBounds(73, 30, 160, 27);
		
		Label lblAlgorithmCategory = new Label(group, SWT.NONE);
		lblAlgorithmCategory.setText("Algorithm Category:");
		lblAlgorithmCategory.setBounds(73, 80, 160, 27);
		
		Label lblParameterNumber = new Label(group, SWT.NONE);
		lblParameterNumber.setText("Parameter Number:");
		lblParameterNumber.setBounds(73, 130, 160, 27);
		
		Label lblAlgorithmDescription = new Label(group, SWT.NONE);
		lblAlgorithmDescription.setText("Algorithm Description:");
		lblAlgorithmDescription.setBounds(73, 230, 160, 27);
		
		Label lblAlgorithmUploadDate = new Label(group, SWT.NONE);
		lblAlgorithmUploadDate.setText("Upload Date:");
		lblAlgorithmUploadDate.setBounds(73, 180, 160, 27);
		
		Label algorithmNameLabel =new Label(group,SWT.BORDER);
		algorithmNameLabel.setBounds(265, 30, 250, 27);
		algorithmNameLabel.setText(program.getAlgorithmName());
		
		Label algorithmCategoryLabel=new Label(group, SWT.BORDER);
		algorithmCategoryLabel.setText(program.getAlgorithmCategory());
		algorithmCategoryLabel.setBounds(263, 80, 250, 27);
		
		Label timeLabel=new Label(group, SWT.BORDER);
		timeLabel.setText(program.getUploadDate());
		timeLabel.setBounds(263, 180, 250, 27);
		
		Label numOfParametersLabel=new Label(group, SWT.BORDER);
		numOfParametersLabel.setText(""+program.getNumOfParameters());
		numOfParametersLabel.setBounds(265, 130, 250, 29);

		Label algorithmDesLabel = new Label(group, SWT.BORDER);
		algorithmDesLabel.setBounds(265, 230, 250, 85);
		algorithmDesLabel.setText(program.getAlgorithmDescription());
	}
	
	public void creatParameterGroup(final Group[] groups,final int i,Parameter[] parameters){
		
		groups[i].setText("Paramter "+(i+1)+" Information");
		Label lblParameterName = new Label(groups[i], SWT.NONE);
		lblParameterName.setBounds(41, 36, 119, 27);
		lblParameterName.setText("Parameter Name:");
		
		Label nameLabel = new Label(groups[i], SWT.BORDER);
		nameLabel.setBounds(166, 36, 93, 27);
		nameLabel.setText(parameters[i].getName());
		
		Label lblParameterValue = new Label(groups[i], SWT.NONE);
		lblParameterValue.setText("Parameter Value:");
		lblParameterValue.setBounds(320, 35, 117, 27);
		
		txtValue[i] = new Text(groups[i], SWT.BORDER);
		txtValue[i].setBounds(443, 35, 93, 27);
		txtValue[i].setText(parameters[i].getValue());
		
		Label lblParameterType = new Label(groups[i], SWT.NONE);
		lblParameterType.setText("Parameter Type:");
		lblParameterType.setBounds(41, 96, 119, 27);
		
//		cmbType[i] = new Combo(groups[i], SWT.READ_ONLY);
//		cmbType[i].setBounds(166, 96, 93, 27);
//		cmbType[i].setText(parameters[i].getType());
//		cmbType[i].add("int");
//		cmbType[i].add("double");
//		cmbType[i].add("string");
		
		Label lblType=new Label(groups[i], SWT.BORDER);
		lblType.setText(parameters[i].getType());
		lblType.setBounds(166,96,93,27);
		
		Label lblParameterDescription = new Label(groups[i], SWT.NONE);
		lblParameterDescription.setText("Parameter Description:");
		lblParameterDescription.setBounds(280, 96, 157, 27);
		
		Label txtDescription = new Label(groups[i], SWT.MULTI|SWT.BORDER);
		txtDescription.setBounds(443, 96, 93, 40);
		txtDescription.setText(parameters[i].getDescription());
		
		Button btnNext = new Button(groups[i], SWT.NONE);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				groups[i].setVisible(false);
				groups[i+1].setVisible(true);
			}
		});
		btnNext.setText("Next");
		btnNext.setBounds(179, 157, 80, 40);
		
		Button btnPrevious = new Button(groups[i], SWT.NONE);
		btnPrevious.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				groups[i].setVisible(false);
				groups[i-1].setVisible(true);
			}
		});
		btnPrevious.setText("Previous");
		btnPrevious.setBounds(320, 157, 80, 40);
		
		Button btnHome = new Button(groups[i], SWT.NONE);
		btnHome.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				groups[i].setVisible(false);
				groups[0].setVisible(true);
			}
		});
		btnHome.setBounds(41, 157, 80, 40);
		btnHome.setText("Home");
		
		Button btnEnd = new Button(groups[i], SWT.NONE);
		btnEnd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				groups[i].setVisible(false);
				groups[groups.length-1].setVisible(true);
			}
		});
		btnEnd.setBounds(456, 157, 80, 40);
		btnEnd.setText("End");		
		
		if(i==0){
			btnHome.setEnabled(false);
			btnPrevious.setEnabled(false);
		}
		if(i==groups.length-1){
			btnNext.setEnabled(false);
			btnEnd.setEnabled(false);
		}

	}
	
	
	
	public void paintTheBoard(DodoProgram dodoprogram,Table table,int index) throws DocumentException
	{
		shell =  new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setSize(600, 714);
		shell.setText("Algorithms Display");
		
		program=dodoprogram;
		
		Group MainInformationGroup = new Group(shell, SWT.NONE);
		
		creatMainGroup(MainInformationGroup,program);
		
		Group[] groups =new Group[program.getNumOfParameters()];
		txtValue=new Text[program.getNumOfParameters()];
		//txtDescription=new Text[program.getNumOfParameters()];
		
		for(int i=0;i<program.getNumOfParameters();i++){
			groups[i] = new Group(shell, SWT.NONE);
			groups[i].setBounds(10, 362, 578, 232);		
			
			if(i!=0) groups[i].setVisible(false);
			
			creatParameterGroup(groups,i,program.getParameters());

		}
		
		creatControllGroup(table,index);		
		
		shell.layout();
		shell.open();
		//System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZ"+program.getParameters()[0].getValue());
	}
	
	public void creatControllGroup(final Table table,final int index){
		Group ControlGroup = new Group(shell, SWT.NONE);
		ControlGroup.setBounds(10, 600, 578, 59);
		
		Button btnRestore = new Button(ControlGroup, SWT.NONE);

		btnRestore.setBounds(114, 10, 120, 42);
		btnRestore.setText("Restore Default");

		btnRestore.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(int i=0;i<program.getNumOfParameters();i++){
					txtValue[i].setText(program.getParameters()[i].getValue());
					//txtDescription[i].setText(program.getParameters()[i].getDescription());
				}
			}
		});
		
		Button btnSave = new Button(ControlGroup, SWT.NONE);
		btnSave.setBounds(354, 10, 120, 42);
		btnSave.setText("Save and Exit");
		
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int ok=1;
				for(int i=0;i<program.getNumOfParameters();i++){
					program.getParameters()[i].setValue(txtValue[i].getText());
					if(txtValue[i].getText().equals("")){
						ok=0;
						break;
					}
					//program.getParameters()[i].setDescription(txtDescription[i].getText());
					
					TableItem item=new TableItem(table,0);
					for(int j=0;j<program.getNumOfParameters();j++){
						item.setText(j+1,program.getParameters()[j].getValue());					
					}
				}
				if(ok==1){
				String path;
				try {
					path = new File(".").getCanonicalPath()+"/source/data/algo/UserDefined/"+program.getAlgorithmCategory()+"/"+program.getAlgorithmName()+"/xmls/";
					GenarateXml gxml=new GenarateXml();
					gxml.genarate(program, path, index);
					
					File file=new File(path);
					while(table.getItemCount()!=0){
						table.getItem(0).dispose();
					}
					
					for(int i=0;i<file.listFiles().length;i++){
						DodoProgram p = null;
						try {
							p=analysis.Analysis(file.listFiles()[i].getPath());
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						TableItem item1 =new TableItem(table,0);
						item1.setText(0,""+i);
						for(int j=0;j<table.getColumnCount()-1;j++){
							item1.setText(j+1,p.getParameters()[j].getValue());
						}
						
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				shell.close();
				}
				else{
					MessageBox mb = new MessageBox(shell,SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
	                 mb.setText("Tip:");
	                 mb.setMessage("The value of the parameters must not be null!");
	                 mb.open();
				}
			}
		});
		
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	public DodoProgram work(DodoProgram pro,Table table,int index) throws DocumentException {
		paintTheBoard(pro,table,index);
		return program;
	}
	
	class myRestoreButtonListener extends SelectionAdapter {

		public void widgetSelected(SelectionEvent e) {
			
			
		}
	}
	
	class mySaveButtonListener extends SelectionAdapter {

		public void widgetSelected(SelectionEvent e) {
			int ok=1;
			for(int i=0;i<program.getNumOfParameters();i++){
				program.getParameters()[i].setValue(txtValue[i].getText());
				if(txtValue[i].getText().equals("")){
					ok=0;
					break;
				}
				//program.getParameters()[i].setDescription(txtDescription[i].getText());
			}
			if(ok==1){
				shell.close();
			}
		}
	}
}
