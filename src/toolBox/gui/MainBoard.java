package toolBox.gui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import toolBox.core.system.DodoSystem;
import toolBox.core.system.MainController;
import toolBox.core.utility.UtilityParameters;
import toolBox.core.utility.UtilityParameters.ChildSystemsName;
import toolBox.gui.view.View;

import java.io.IOException;

public class MainBoard {
	
	final Display display = Display.getDefault();
	final Shell shell = new Shell(display, SWT.CLOSE | SWT.MIN);
	//final Shell shell = new Shell(display);

	protected MainController mainController = null;
		
	Button platformButton = null;
	Button datasetsButton = null;
	Button algorithmsButton = null;
	Button tasksButton = null;
//-------------------view button-----------------------------
	Button viewButton = null;
//-------------------------------------------------------------
	Menu mainMenu = null;
	
	public MainBoard(MainController controller)
	{
		mainController = controller;
	}
	
	public void paintBoard()
	{
		shell.setSize(UtilityParameters.MAIN_BOARD_WIDTH, UtilityParameters.MAIN_BOARD_HEIGHT);
		shell.setText("Tool Box");
		//-------------------------------------------------------
		
		createMenu(shell);
		createImage(shell,new Image(null,UtilityParameters.IMAGE_PATH));
		//createImage(shell,new Image(null,getClass().getResourceAsStream(UtilityParameters.IMAGE_PATH)));
		createChildSystemsButton(shell);
		//createSystemStatesSection(shell);
		
		//--------------------------------------------------
		shell.layout();
		shell.open();
		while(!shell.isDisposed())
			if(!display.readAndDispatch())
				display.sleep();
		//display.dispose();
		if(DodoSystem.getDodoSystem().getJobClient() != null)
		{
			try {
				DodoSystem.getDodoSystem().getJobClient().close();
				DodoSystem.getDodoSystem().setJobClient(null);
				System.out.println("....jobClient.close().....");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(DodoSystem.getDodoSystem().getDFSClient() != null)
		{
			try {
				DodoSystem.getDodoSystem().getDFSClient().close();
				DodoSystem.getDodoSystem().setDFSClient(null);
				System.out.println("....dfsClient.close().....");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(DodoSystem.getDodoSystem().getDistributedFileSystem() != null)
		{
			try {
				DodoSystem.getDodoSystem().getDistributedFileSystem().close();
				DodoSystem.getDodoSystem().setDistributedFileSystem(null);
				System.out.println("....dfs.close().....");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		System.exit(0);
	}
	
	private void createSystemStatesSection(Shell parent) {
		FormToolkit toolkit = new FormToolkit(display);
		final Composite comp = toolkit.createComposite(parent);
		
		Section section = toolkit.createSection(comp, Section.DESCRIPTION);
		section.setText("States");
	}

	private void createChildSystemsButton(Shell parent) {
		Composite buttonsComp = new Composite(parent,SWT.BORDER);
		buttonsComp.setBounds(440,15,180,390);

		platformButton = new Button(buttonsComp,SWT.NONE);
		platformButton.setText("platform");
		platformButton.setBounds(25, 20, 130, 50);
		addListenerForButton(platformButton);
		
		datasetsButton = new Button(buttonsComp,SWT.NONE);
		datasetsButton.setText("datasets");
		datasetsButton.setBounds(25, 90, 130, 50);
		addListenerForButton(datasetsButton);
		
		algorithmsButton = new Button(buttonsComp,SWT.NONE);
		algorithmsButton.setText("algorithms");
		algorithmsButton.setBounds(25, 160, 130, 50);
		addListenerForButton(algorithmsButton);
		
		tasksButton = new Button(buttonsComp,SWT.NONE);
		tasksButton.setText("tasks");
		tasksButton.setBounds(25, 230, 130, 50);
		addListenerForButton(tasksButton);
		
		viewButton = new Button(buttonsComp, SWT.NONE);
		viewButton.setText("view");
		viewButton.setBounds(25, 300, 130, 50);
		addListenerForButton(viewButton);
	}

	private void addListenerForButton(Button button) {
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				Button button = (Button)e.getSource();
				if(button.getText().equals("platform"))					
					mainController.createChildSystem(ChildSystemsName.platform);
				else if(button.getText().equals("datasets"))
					mainController.createChildSystem(ChildSystemsName.datasets);
				else if(button.getText().equals("algorithms"))
					mainController.createChildSystem(ChildSystemsName.algorithms);
				else if(button.getText().equals("view")){
					View view = new View();
				}
				else{
					if(DodoSystem.getDodoSystem().getJobClient()==null||DodoSystem.getDodoSystem().getDistributedFileSystem()==null){
						MessageBox mb = new MessageBox(shell,SWT.ICON_QUESTION | SWT.OK| SWT.CANCEL);
                        mb.setText("Tip:");
                        mb.setMessage("JobClient is null,Please connect first!");
                        mb.open();
                        button.setEnabled(true);
					}
					else{
						mainController.createChildSystem(ChildSystemsName.tasks);
					//	button.setEnabled(false);
					}
				}
			}
		});
	}

	private void createMenu(Shell parent) {
		mainMenu = new Menu(parent,SWT.BAR);
		mainMenu.setLocation(new Point(10, 10));
		parent.setMenuBar(mainMenu);
		MenuItem mItem = null;
		
		MenuItem userItem = new MenuItem(mainMenu,SWT.CASCADE);
		userItem.setText("User(&U)");
		
		MenuItem helpItem = new MenuItem(mainMenu,SWT.CASCADE);
		helpItem.setText("Help(&H)");
		
		//--------------------------------------------------------userMenu----------------------------------------------------------
//		Menu menu = new Menu(shell,SWT.DROP_DOWN);
//		userItem.setMenu(menu);
//		String[] menuItems = {"logout","register","quit"};
//		for(String menuItem : menuItems)
//		{
//			MenuItem item = new MenuItem(menu,SWT.CASCADE);
//			item.setText(menuItem);
//		}
//		
//		//---------------------------------------------------------helpMenu--------------------------------------------------------------
//		menu = new Menu(shell,SWT.DROP_DOWN);
//		helpItem.setMenu(menu);
//		
//		mItem = new MenuItem(menu,SWT.CASCADE);
//		mItem.setText(Messages.getString("MAIN_MENU_HELP_LOG_NAME"));
//		
//		//---------------------------------------------------------viewMenu--------------------------------------------------------------
//		MenuItem viewItem = new MenuItem(mainMenu, SWT.NONE);
//		viewItem.setText("View(&V)");
//		
//		
//		mItem.addSelectionListener(new SelectionAdapter(){
//			@Override
//			public void widgetSelected(SelectionEvent e)
//			{
//				if(!Logger.setLogWindowVisiable())
//					MessageDialog.openError(shell, Messages.getString("MESSAGE_BOX_TITLE_ERROR"), Messages.getString("LOG_FUNCTION_OFF_MESSAGE"));
//			}
//		});
//		
//		menuItems =new String[]{"Help Contents","All about Dodo","Developers"};
//		for(String menuItem : menuItems)
//		{
//			MenuItem item = new MenuItem(menu,SWT.CASCADE);
//			item.setText(menuItem);
//		}
	}

	private void createImage(Composite parent, Image image) {
		Composite imageComp = new Composite(parent,SWT.BORDER);
		imageComp.setBounds(10, 15, 400 ,390);
		imageComp.setBackgroundImage(image);
	}

	public static void main(String[] args)
	{
		new MainBoard(null).paintBoard();
	}

	public void renewChildSystemsButton(ChildSystemsName systemName) {
		switch(systemName)
		{
		case platform:platformButton.setEnabled(true);break;
		case datasets:datasetsButton.setEnabled(true);break;
		case algorithms:algorithmsButton.setEnabled(true);break;
		case tasks:tasksButton.setEnabled(true);break;
		}
	}

	public void work() {
		paintBoard();		
	}
}
