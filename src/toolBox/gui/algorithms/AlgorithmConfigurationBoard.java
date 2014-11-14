package toolBox.gui.algorithms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;
import toolBox.algorithmsManager.applications.DodoProgram;
import toolBox.algorithmsManager.applications.GenarateXml;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
//import org.eclipse.wb.swt.SWTResourceManager;


public class AlgorithmConfigurationBoard {

	protected Shell shell;
	private Table parametersTable;
	private Text algorithmNameText;
	private Text algorithmDescriptionText;
	private Combo algorithmCategoryCombo;
	int h=0;
	DodoProgram program;
	GenarateXml gxml;
	boolean saveOK=false;
	public class TableItemControls{
		   Text name;
		   Combo type;
		   Text value;
		   Text des;
		   TableEditor editor1;
		   TableEditor editor2;
		   TableEditor editor3;
		   TableEditor editor4;
		  
		   public TableItemControls(Text t1, Combo c,Text t2, Text t3,
		     TableEditor e1, TableEditor e2, TableEditor e3, TableEditor e4) {

		    this.name = t1;
		    this.type = c;
		    this.value = t2;
		    this.des = t3;
		    this.editor1 = e1;
		    this.editor2 = e2;
		    this.editor3 = e3;
		    this.editor4 = e4;
		   }
		   public void dispose()
		   {
		    name.dispose();
		    type.dispose();
		    value.dispose();
		    des.dispose();
		    editor1.dispose();
		    editor2.dispose();
		    editor3.dispose();
		    editor4.dispose();
		   }
		};
		private Hashtable<TableItem, TableItemControls> tableControls = new Hashtable<TableItem, TableItemControls>();
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AlgorithmConfigurationBoard window = new AlgorithmConfigurationBoard();
			window.open(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public boolean open(Text text) {
		Display display = Display.getDefault();
		createContents(text);
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return saveOK;
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(final Text text) {
		shell = new Shell();
		shell.setSize(575, 492);
		shell.setText("Algorithm Configuration Board");
		
		parametersTable = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		parametersTable.setBounds(70, 228, 436, 145);
		parametersTable.setHeaderVisible(true);
		parametersTable.setLinesVisible(true);
		
		TableColumn parameterNumber = new TableColumn(parametersTable, SWT.NONE);
		parameterNumber.setWidth(49);
		parameterNumber.setText("NO.");
		
		TableColumn parameterName = new TableColumn(parametersTable, SWT.NONE);
		parameterName.setWidth(84);
		parameterName.setText("Name");
		
		TableColumn parameterType = new TableColumn(parametersTable, SWT.NONE);
		parameterType.setWidth(81);
		parameterType.setText("Type");
		
		TableColumn parameterValue = new TableColumn(parametersTable, SWT.NONE);
		parameterValue.setWidth(100);
		parameterValue.setText("Value");
		
		TableColumn parameterDescription = new TableColumn(parametersTable, SWT.NONE);
		parameterDescription.setWidth(100);
		parameterDescription.setText("Description");
		
		Button addButton = new Button(shell, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem item=new TableItem(parametersTable,0);
				item.setText(0, ""+parametersTable.getItemCount()+"");
				
				final TableEditor editor1 = new TableEditor (parametersTable);
				final Text name = new Text (parametersTable, SWT.NONE);
				editor1.grabHorizontal = true;
				editor1.setEditor(name, item, 1);
				name.addModifyListener( new ModifyListener(){
				     public void modifyText(ModifyEvent e) {
				      editor1.getItem().setText(1,name.getText());
				     }
				     });
				
				final TableEditor editor2 = new TableEditor (parametersTable);
				final Combo type = new Combo (parametersTable, SWT.READ_ONLY);

				type.add("int");
				type.add("double");
				type.add("String");
				type.setText("int");
				
				editor2.grabHorizontal = true;
				editor2.setEditor(type, item, 2);
				editor2.getItem().setText(2,type.getText());
				type.addModifyListener( new ModifyListener(){
				     public void modifyText(ModifyEvent e) {
				      editor2.getItem().setText(2,type.getText());
				      System.out.println("hera"+type.getText());
				     }
				     });
				
				final TableEditor editor3 = new TableEditor (parametersTable);
				final Text value = new Text (parametersTable, SWT.NONE);
				editor3.grabHorizontal = true;
				editor3.setEditor(value, item, 3);
				value.addModifyListener( new ModifyListener(){
				     public void modifyText(ModifyEvent e) {
				      editor3.getItem().setText(3,value.getText());
				     }
				     });
				
				final TableEditor editor4 = new TableEditor (parametersTable);
				final Text des = new Text (parametersTable, SWT.NONE);
				editor4.grabHorizontal = true;
				editor4.setEditor(des, item, 4);
				des.addModifyListener( new ModifyListener(){
				     public void modifyText(ModifyEvent e) {
				      editor4.getItem().setText(4,des.getText());
				     }
				     });
				
				TableItemControls cons = new TableItemControls(name, type, value, des, editor1, editor2, editor3, editor4);
			    tableControls.put(item, cons);
				h++;
			}
		});
		addButton.setBounds(91, 404, 91, 29);
		addButton.setText("Add");
		
		Button saveButton = new Button(shell, SWT.NONE);
		saveButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				 program =new DodoProgram(h);
			     program.setAlgorithmName(algorithmNameText.getText());			    
			     program.setAlgorithmCategory(algorithmCategoryCombo.getText());
			     program.setAlgorithmDescription(algorithmDescriptionText.getText());			     
			     
			     for(int i=0;i<h;i++){
			    	 program.getParameters()[i].setName(parametersTable.getItem(i).getText(1));
			    	 program.getParameters()[i].setType(parametersTable.getItem(i).getText(2));
			    	
			    	 program.getParameters()[i].setValue(parametersTable.getItem(i).getText(3));
			    	 program.getParameters()[i].setDescription(parametersTable.getItem(i).getText(4));
			     }
			     if(program.getAlgorithmName().equals("")){
					 MessageBox mb = new MessageBox(shell,SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
	                 mb.setText("Tip:");
	                 mb.setMessage("Please fill the algorithm name!");
	                 mb.open();
				 }
				 else if(program.getAlgorithmCategory()==""){
					 MessageBox mb2 = new MessageBox(shell,SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
	                 mb2.setText("Tip:");
	                 mb2.setMessage("Please select the algorithm category!");
	                 mb2.open();
				 }
				 else{
					 
			     //
			     String path;
				try {
					path = new File(".").getCanonicalPath()+"/source/data/algo/UserDefined/"+program.getAlgorithmCategory()+"/"+program.getAlgorithmName()+"/";
					if(new File(path).exists()){
						MessageBox mb2 = new MessageBox(shell,SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
            			mb2.setText("Tip:");
            			mb2.setMessage("Sorry, the algorithm already exists! Please rename!");
   	                 	mb2.open();
					}
					else{
						gxml=new GenarateXml();
						gxml.genarate(program, path,-1);
						text.redraw();
						text.setText(path+algorithmNameText.getText()+".xml");
						saveOK=true;
					     shell.close();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	     
				
				 }
			}
		});
		saveButton.setBounds(400, 404, 91, 29);
		saveButton.setText("Save");
		
		
		
		Label algorithmNameLabel = new Label(shell, SWT.NONE);
		algorithmNameLabel.setBounds(91, 29, 70, 18);
		algorithmNameLabel.setText("Name:");
		
		algorithmNameText = new Text(shell, SWT.BORDER);
		//algorithmNameText.setText("unfilled");
		algorithmNameText.setBounds(218, 29, 262, 27);
		
		Label algorithmCategoiryLabel = new Label(shell, SWT.NONE);
		algorithmCategoiryLabel.setBounds(91, 80, 70, 18);
		algorithmCategoiryLabel.setText("Category:");
		
		algorithmCategoryCombo = new Combo(shell, SWT.READ_ONLY);
		algorithmCategoryCombo.setBounds(218, 80, 262, 30);
		//algorithmCategoryCombo.setText("unfilled");
		algorithmCategoryCombo.add("Basic");
		algorithmCategoryCombo.add("Classification");
		algorithmCategoryCombo.add("Cluster");
		algorithmCategoryCombo.add("Association Rule");
		algorithmCategoryCombo.add("WebPage Parsing");
		algorithmCategoryCombo.add("Other");
		
		Label algorithmDescriptionLabel = new Label(shell, SWT.NONE);
		algorithmDescriptionLabel.setBounds(91, 136, 91, 18);
		algorithmDescriptionLabel.setText("Description:");
		
		algorithmDescriptionText = new Text(shell, SWT.MULTI|SWT.BORDER);
		//algorithmDescriptionText.setText("unfilled");
		algorithmDescriptionText.setBounds(218, 136, 262, 40);
		
		Label parametersLabel = new Label(shell, SWT.NONE);
		parametersLabel.setBounds(91, 186, 91, 18);
		parametersLabel.setText("Parameters:");
		
		Button deleteButton = new Button(shell, SWT.NONE);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int sell=parametersTable.getSelectionIndex();
				TableItemControls cons = tableControls.get(parametersTable.getItem(sell));
			    if (cons != null) {
			       cons.dispose();
			       tableControls.remove(parametersTable.getItem(sell));
			    }
			    parametersTable.remove(sell);
			    parametersTable.redraw();
			   //parametersTable.pack();
			    h--;
			}
		});
		deleteButton.setBounds(244, 404, 91, 29);
		deleteButton.setText("Delete");
			     
	}
}


