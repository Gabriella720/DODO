package toolBox.gui.datasets;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.*;
import toolBox.core.system.DodoSystem;
import toolBox.core.system.MainController;
import toolBox.core.utility.UtilityParameters;
import toolBox.core.utility.UtilityParameters.ChildSystemsName;
import toolBox.datasetsManager.applications.DataDescribe;
import toolBox.datasetsManager.applications.DatasetsMainController;


public class DatasetsMainBoard {
	
	DFSClient dfsc = DodoSystem.getDodoSystem().getDFSClient();  //等待平台参数确定
	DatasetsMainController datasetsController = null;
	final Display display = Display.getDefault();
	private Shell shell = null;
	static DistributedFileSystem dfs = DodoSystem.getDodoSystem().getDistributedFileSystem();
	private Path workDir = dfs.getWorkingDirectory();
	private Table preprocessTable_attr;
	private Table preprocessTable_selectedattr;
	private DataDescribe ddc= new DataDescribe(dfs);;
	private Composite boardComposite;
	private DatasetsTree dTree;
	private DatasetsDescriptionBoard ddb;	
		
	public DatasetsMainBoard(DatasetsMainController controller)
	{
		datasetsController = controller;
	}
	
	public void setController(DatasetsMainController controller)
	{
		datasetsController = controller;
	}
	//-------------------------------------------------------------------------------------------

	public void paintTheBoard  () 
	{
		shell =  new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setSize(UtilityParameters.CHILD_BOARD_WIDTH, UtilityParameters.CHILD_BOARD_HEIGHT);
		shell.setText("Datasets Manager");
		
		boardComposite = new Composite(shell, SWT.NONE);
		boardComposite.setBounds(10, 10, 978, 542);
				
		TabFolder tabFolder = new TabFolder(boardComposite, SWT.NONE);
		tabFolder.setBounds(216, 10, 752, 522);
		
		ddb= new DatasetsDescriptionBoard( tabFolder, ddc,  dfs);
		ddb.paint();
		
		/*TabItem tbtmDataDescription = new TabItem(tabFolder, SWT.NONE);
		tbtmDataDescription.setText("Data description");
		
		/*Composite descriptionComposite = new Composite(tabFolder, SWT.NONE);
		tbtmDataDescription.setControl(descriptionComposite);
		
		Label lblRelation_description = new Label(descriptionComposite, SWT.NONE);
		lblRelation_description.setBounds(10, 10, 80, 30);
		lblRelation_description.setText("Relation:");
		
		relationText = new Text(descriptionComposite, SWT.BORDER);
		relationText.setBounds(96, 10, 642, 30);
		
		Label lblAttributes_description = new Label(descriptionComposite, SWT.NONE);
		lblAttributes_description.setBounds(10, 46, 80, 20);
		lblAttributes_description.setText("Attributes:");
		
		descriptionTable = new Table(descriptionComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
		descriptionTable.setBounds(10, 72, 728, 373);
		descriptionTable.setHeaderVisible(true);
		descriptionTable.setLinesVisible(true);
		
		TableColumn tblclmnNumber = new TableColumn(descriptionTable, SWT.NONE);
		tblclmnNumber.setWidth(100);
		tblclmnNumber.setText("No.");		
		TableColumn tblclmnName = new TableColumn(descriptionTable, SWT.NONE);
		tblclmnName.setWidth(271);
		tblclmnName.setText("Name");		
		TableColumn tblclmnType = new TableColumn(descriptionTable, SWT.NONE);
		tblclmnType.setWidth(100);
		tblclmnType.setText("Type");	
		//TableCursor descriptionTableCursor = new TableCursor(descriptionTable, SWT.NONE);
		final TableEditor editor = new TableEditor (descriptionTable);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        descriptionTable.setEnabled(false);*/
        
        dTree = new DatasetsTree(boardComposite, workDir,dfs, ddb.getRelationText(), ddb.getDescriptionTable(), ddc,shell);
		dTree.paintTree();
        
		/*descriptionTable.addListener (SWT.MouseDown, new Listener () {
            public void handleEvent (Event event) {
                Rectangle clientArea = descriptionTable.getClientArea ();
                Point pt = new Point (event.x, event.y);
                int index = descriptionTable.getTopIndex ();
                while (index < descriptionTable.getItemCount ()) {
                    boolean visible = false;
                    final TableItem item = descriptionTable.getItem (index);
                    for (int i=0; i<descriptionTable.getColumnCount (); i++) {
                        Rectangle rect = item.getBounds (i);
                        if (rect.contains (pt)) {
                            final int column = i;
                            final Text text = new Text (descriptionTable, SWT.NONE);
                            Listener textListener = new Listener () {
                                public void handleEvent (final Event e) {
                                    switch (e.type) {
                                        case SWT.FocusOut:
                                            item.setText (column, text.getText ());
                                            text.dispose ();
                                            break;
                                        case SWT.Traverse:
                                            switch (e.detail) {
                                                case SWT.TRAVERSE_RETURN:
                                                    item.setText (column, text.getText ());
                                                    //FALL THROUGH
                                                case SWT.TRAVERSE_ESCAPE:
                                                    text.dispose ();
                                                    e.doit = false;
                                            }
                                            break;
                                    }
                                }
                            };
                            text.addListener (SWT.FocusOut, textListener);                            
                            text.addListener (SWT.Traverse, textListener);                            
                            editor.setEditor (text, item, i);
                            text.setText (item.getText (i));
                            text.selectAll ();
                            text.setFocus ();
                            return;
                        }
                        if (!visible && rect.intersects (clientArea)) {
                            visible = true;
                        }
                    }
                    if (!visible) return;
                    index++;
                }
            }
        });
				
		
		Button btnAdd_description = new Button(descriptionComposite, SWT.NONE);
		btnAdd_description.setText("Add");
		btnAdd_description.setBounds(150, 451, 90, 30);
		btnAdd_description.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String[] str = {String.valueOf(descriptionTable.getItemCount()), "attribute", "unknown"};
				TableItem item = new TableItem (descriptionTable, SWT.NONE);
    			item.setText(str);
		     }
		});
		
		Button btnDelete_description = new Button(descriptionComposite, SWT.NONE);
		btnDelete_description.setText("Delete");
		btnDelete_description.setBounds(331, 451, 90, 30);
		btnDelete_description.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				for(int i =
						0; i <descriptionTable.getItemCount(); i++){
					TableItem item = descriptionTable.getItem(i);
					if(item.getChecked()){
						descriptionTable.remove(i);
					}            			
        		}
		     }
		});
		
		Button btnSave_description = new Button(descriptionComposite, SWT.CENTER);
		btnSave_description.setBounds(512, 451, 90, 30);
		btnSave_description.setText("Save");
		btnSave_description.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String[] attributeArray = new String[ddc.getCount()];
				String[] typeArray = new String[ddc.getCount()];
				for(int i =0; i < ddc.getCount(); i++){
					attributeArray[i] = descriptionTable.getItem(i).getText(1);
					typeArray[i] = descriptionTable.getItem(i).getText(2);
				}
			ddc.generate(attributeArray, typeArray, dfs);
		     }
		});*/
		
		
		TabItem tbtmPreprocess = new TabItem(tabFolder, SWT.NONE);
		tbtmPreprocess.setText("Preprocess");
		
		Composite preprocessComposite = new Composite(tabFolder, SWT.NONE);
		tbtmPreprocess.setControl(preprocessComposite);
		
		Group grpCurrentRelation = new Group(preprocessComposite, SWT.NONE);
		grpCurrentRelation.setText("Current relation");
		grpCurrentRelation.setBounds(10, 10, 728, 68);
		
		Label lblRelation_preprocess = new Label(grpCurrentRelation, SWT.NONE);
		lblRelation_preprocess.setBounds(10, 28, 70, 30);
		lblRelation_preprocess.setText("Relation:");
		
		Label lblInstances = new Label(grpCurrentRelation, SWT.NONE);
		lblInstances.setText("Instances:");
		lblInstances.setBounds(228, 28, 70, 30);
		
		Label lblAttributes_preprocess = new Label(grpCurrentRelation, SWT.NONE);
		lblAttributes_preprocess.setText("Attributes");
		lblAttributes_preprocess.setBounds(469, 28, 70, 30);
		
		Group grpAttributes = new Group(preprocessComposite, SWT.NONE);
		grpAttributes.setText("Attributes");
		grpAttributes.setBounds(10, 84, 363, 397);
		
		Button btnAll = new Button(grpAttributes, SWT.NONE);
		btnAll.setBounds(10, 24, 91, 29);
		btnAll.setText("All");
		
		Button btnNone = new Button(grpAttributes, SWT.NONE);
		btnNone.setBounds(140, 24, 91, 29);
		btnNone.setText("None");
		
		Button btnInvert = new Button(grpAttributes, SWT.NONE);
		btnInvert.setBounds(262, 24, 91, 29);
		btnInvert.setText("Invert");
		
		Button btnRemove = new Button(grpAttributes, SWT.NONE);
		btnRemove.setBounds(10, 358, 91, 29);
		btnRemove.setText("Remove");
		
		Button btnSave_preprocess = new Button(grpAttributes, SWT.NONE);
		btnSave_preprocess.setBounds(140, 358, 91, 29);
		btnSave_preprocess.setText("Save");
		
		Button btnVisualize = new Button(grpAttributes, SWT.NONE);
		btnVisualize.setBounds(262, 358, 91, 29);
		btnVisualize.setText("Visualize");
		
		preprocessTable_attr = new Table(grpAttributes, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
		preprocessTable_attr.setBounds(10, 59, 343, 292);
		preprocessTable_attr.setHeaderVisible(true);
		preprocessTable_attr.setLinesVisible(true);
		
		TableColumn tblclmnNumber_preprocess = new TableColumn(preprocessTable_attr, SWT.NONE);
		tblclmnNumber_preprocess.setWidth(40);
		tblclmnNumber_preprocess.setText("No.");
		
		TableColumn tblclmnCheck = new TableColumn(preprocessTable_attr, SWT.NONE);
		tblclmnCheck.setWidth(40);
		
		TableColumn tblclmnName_preprocess = new TableColumn(preprocessTable_attr, SWT.NONE);
		tblclmnName_preprocess.setWidth(100);
		tblclmnName_preprocess.setText("Name");
		
		Group grpSelectedAttributes = new Group(preprocessComposite, SWT.NONE);
		grpSelectedAttributes.setText("Selected attributes");
		grpSelectedAttributes.setBounds(379, 84, 359, 397);
		
		Label lblName = new Label(grpSelectedAttributes, SWT.NONE);
		lblName.setBounds(10, 30, 70, 18);
		lblName.setText("Name");
		
		Label lblType = new Label(grpSelectedAttributes, SWT.NONE);
		lblType.setBounds(279, 30, 70, 18);
		lblType.setText("Type");
		
		Label lblMissing = new Label(grpSelectedAttributes, SWT.NONE);
		lblMissing.setBounds(10, 54, 70, 18);
		lblMissing.setText("Missing");
		
		Label lblUnique = new Label(grpSelectedAttributes, SWT.NONE);
		lblUnique.setBounds(279, 54, 70, 18);
		lblUnique.setText("Unique");
		
		Label lblDistinct = new Label(grpSelectedAttributes, SWT.NONE);
		lblDistinct.setBounds(143, 54, 70, 18);
		lblDistinct.setText("Distinct");
		
		preprocessTable_selectedattr = new Table(grpSelectedAttributes, SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
		preprocessTable_selectedattr.setBounds(10, 78, 339, 309);
		preprocessTable_selectedattr.setHeaderVisible(true);
		preprocessTable_selectedattr.setLinesVisible(true);
		
		TableColumn tblclmnStatistic = new TableColumn(preprocessTable_selectedattr, SWT.NONE);
		tblclmnStatistic.setWidth(168);
		tblclmnStatistic.setText("Statistic");
		
		TableColumn tblclmnValue = new TableColumn(preprocessTable_selectedattr, SWT.NONE);
		tblclmnValue.setWidth(100);
		tblclmnValue.setText("Value");
		
		TableCursor preprocessTableCursor = new TableCursor(preprocessTable_selectedattr, SWT.NONE);
		
		shell.addDisposeListener(new  DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				//datasetsController.renewChildSystemsButton();
				MainController.getMainController().renewChildSystemsButton(ChildSystemsName.datasets);
			}
		});
		
		//---------------------------------------------------------------------------------------
		shell.layout();
		shell.open();
	}
	  
	public void work() {
			paintTheBoard();
		}
	
	public static void main(String[] args)
	{
		DatasetsMainBoard testBoard = new DatasetsMainBoard(null);
		testBoard.work();
	}
	

}

