package toolBox.gui.platform;

import org.apache.hadoop.hdfs.DistributedFileSystem.DiskStatus;
import org.apache.hadoop.mapred.ClusterStatus;
import org.apache.hadoop.mapred.JobTracker.State;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;
import org.jdom.Element;
import org.jdom.JDOMException;
import toolBox.core.system.DodoSystem;
import toolBox.core.system.MainController;
import toolBox.core.utility.UtilityParameters;
import toolBox.core.utility.UtilityParameters.ChildSystemsName;
import toolBox.core.utility.XMLUtility;
import toolBox.platformManager.applications.PlatformMainController;

import java.io.IOException;
import java.util.*;
import java.util.List;

public class PlatformMainBoard {
	
	PlatformMainController platformController = null;
	final Display display = Display.getDefault();
	Shell shell = null;
	
	Text jobTrackerIPText = null;
	Text jobTrackerPortText = null;
	Text hdfsNodeIPText = null;
	Text hdfsNodePortText = null;
	Text sysStateInfo = null;
	Text startOrStopInfo = null;
	Text fileName = null;
	String HADOOP_HOME = null;
	
	Button connectButton = null; 
	Button disconnectButton = null;
	
	Label connectTipLabel = null;
	Label tipsLabel = null;
	
	String datasetsPath = null;
	String xmlPath = null;
	String resultPath = null;
	String sql = null;
	
	Text[] nam = new Text[50];
	Text[] val = new Text[50];
	
	List propertyList = null;
	XMLUtility xml = null;
	
	int h = 200;
	int k = -1;
	
	public PlatformMainBoard(PlatformMainController controller)
	{
		platformController = controller;
	}
	
	public void setController(PlatformMainController controller)
	{
		platformController = controller;
	}
	
	//-------------------------------------------------------------------------------------------

	public void paintTheBoard()
	{
		shell =  new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setSize(UtilityParameters.CHILD_BOARD_WIDTH, UtilityParameters.CHILD_BOARD_HEIGHT);
		shell.setText("Platform Manager");
		
		shell.addDisposeListener(new  DisposeListener(){
			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				//platformController.renewChildSystemsButton();
				k = -1;
				h = 200;
				MainController.getMainController().renewChildSystemsButton(ChildSystemsName.platform);
			}
		});
		//--------------------------------------------------------------------------------------
		
		TabFolder tabFolder = new TabFolder(shell,SWT.NONE);
		tabFolder.setBounds(20,10, UtilityParameters.TAB_WINDOW_WIDTH, UtilityParameters.TAB_WINDOW_HEIGHT);
		
		createStartOrStopHadoop(tabFolder);
		createConnectItem(tabFolder);
		createConfigureItem(tabFolder);
		createStatesMonitorItem(tabFolder);
		
		if(DodoSystem.getDodoSystem().getJobClient() != null && DodoSystem.getDodoSystem().getDFSClient()!= null)
		{
			connectTipLabel.setText("The master has already been connected");
			jobTrackerIPText.setEnabled(false);
			jobTrackerPortText.setEnabled(false);
			hdfsNodeIPText.setEnabled(false);
			hdfsNodePortText.setEnabled(false);
			connectButton.setEnabled(false);
		}
		else
		{
			connectTipLabel.setText("No master connected");
			disconnectButton.setEnabled(false);
		}
		
		//---------------------------------------------------------------------------------------
		shell.layout();
		shell.open();
	}

	private void createStartOrStopHadoop(TabFolder tabFolder) 
	{
		TabItem item = new TabItem(tabFolder,SWT.NONE);
		item.setText("StartOrStop Hadoop");
		Composite composite = new Composite(tabFolder,SWT.NONE);	
		item.setControl(composite);
		
		Group group = new Group(composite,SWT.NONE);
		group.setBounds(50, 30, 200, 60);
		
		Button button = new Button(group,SWT.BORDER);
		button.setBounds(5, 5, 190, 50);
		button.setText("connect master");
		button.setEnabled(false);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				startOrStopInfo.append("===============================================\n");
				Date date = new Date();
				startOrStopInfo.append(date.toString()+"  ");
				startOrStopInfo.append("ssh master Called...\n");
				String[] bufferedReader = platformController.excuteCommand("ssh -tt master");
				startOrStopInfo.append(bufferedReader[0]);
				startOrStopInfo.append(bufferedReader[1]);
			}
			
		});
		
		group = new Group(composite,SWT.NONE);
		group.setBounds(50, 100, 200, 60);
		
		button = new Button(group,SWT.BORDER);
		button.setBounds(5, 5, 190, 50);
		button.setText("Start DFS");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				startOrStopInfo.append("===============================================\n");
				Date date = new Date();
				startOrStopInfo.append(date.toString()+"  ");
				startOrStopInfo.append("start DFS Called...\n");
				String[] bufferedReader = platformController.excuteCommand("./hadoop/bin/start-dfs.sh");
				startOrStopInfo.append(bufferedReader[0]);
				startOrStopInfo.append(bufferedReader[1]);
			}
			
		});
		
		group = new Group(composite,SWT.NONE);
		group.setBounds(50, 170, 200, 60);
		
		button = new Button(group,SWT.BORDER);
		button.setBounds(5, 5, 190, 50);
		button.setText("Start MapRed");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				startOrStopInfo.append("===============================================\n");
				Date date = new Date();
				startOrStopInfo.append(date.toString()+"  ");
				startOrStopInfo.append("start mapred Called...\n");
				String[] bufferedReader = platformController.excuteCommand("./hadoop/bin/start-mapred.sh");
				startOrStopInfo.append(bufferedReader[0]);
				startOrStopInfo.append(bufferedReader[1]);
			}
			
		});
		
		group = new Group(composite,SWT.NONE);
		group.setBounds(50, 240, 200, 60);
		
		button = new Button(group,SWT.BORDER);
		button.setBounds(5, 5, 190, 50);
		button.setText("Stop DFS");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if(DodoSystem.getDodoSystem().getDFSClient()!= null)
				{
					try {
						DodoSystem.getDodoSystem().getDFSClient().close();
						DodoSystem.getDodoSystem().setDFSClient(null);
						System.out.println("....dfsClient.close().....");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				connectTipLabel.setText("DFS closed,please reconnect!");
				jobTrackerIPText.setEnabled(true);
				jobTrackerPortText.setEnabled(true);
				hdfsNodeIPText.setEnabled(true);
				hdfsNodePortText.setEnabled(true);
				connectButton.setEnabled(true);
				disconnectButton.setEnabled(false);
				startOrStopInfo.append("===============================================\n");
				Date date = new Date();
				startOrStopInfo.append(date.toString()+"  ");
				startOrStopInfo.append("stop dfs Called...\n");
				String[] bufferedReader = platformController.excuteCommand("./hadoop/bin/stop-dfs.sh");
				startOrStopInfo.append(bufferedReader[0]);
				startOrStopInfo.append(bufferedReader[1]);
			}
			
		});
		
		group = new Group(composite,SWT.NONE);
		group.setBounds(50, 310, 200, 60);
		
		button = new Button(group,SWT.BORDER);
		button.setBounds(5, 5, 190, 50);
		button.setText("Stop MapRed");
		
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
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
				connectTipLabel.setText("JobTracker closed,please reconnect!");
				jobTrackerIPText.setEnabled(true);
				jobTrackerPortText.setEnabled(true);
				hdfsNodeIPText.setEnabled(true);
				hdfsNodePortText.setEnabled(true);
				connectButton.setEnabled(true);
				disconnectButton.setEnabled(false);
				startOrStopInfo
						.append("===============================================\n");
				Date date = new Date();
				startOrStopInfo.append(date.toString() + "  ");
				startOrStopInfo.append("stop mapred Called...\n");
				String[] bufferedReader = platformController
						.excuteCommand("./hadoop/bin/stop-mapred.sh");
				startOrStopInfo.append(bufferedReader[0]);
				startOrStopInfo.append(bufferedReader[1]);
			}
		});
		
		group = new Group(composite,SWT.NONE);
		group.setBounds(300, 30, 550, 400);
		
		startOrStopInfo = new Text(group,SWT.WRAP|SWT.V_SCROLL);
		startOrStopInfo.setBounds(5, 5, 540, 390);
		startOrStopInfo.setEditable(false);
	}

	private void createStatesMonitorItem(TabFolder tabFolder) {
		TabItem item = new TabItem(tabFolder,SWT.NONE);
		item.setText("States Monitor");
		Composite composite = new Composite(tabFolder,SWT.NONE);	
		item.setControl(composite);
		
		Group group = new Group(composite,SWT.NONE);
		group.setBounds(50, 20, 200, 50);
		
		Button button = new Button(group,SWT.BORDER);
		button.setText("Get SysStateInfo");
		button.setBounds(5, 5, 190, 40);
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				sysStateInfo.setText("");
				if(DodoSystem.getDodoSystem().getJobClient() != null && DodoSystem.getDodoSystem().getDFSClient() != null)
				{
					try {
						ClusterStatus clusterStatus = DodoSystem.getDodoSystem().getJobClient().getClusterStatus();
						int mapTasks = clusterStatus.getMapTasks();
						int maxMapTasks = clusterStatus.getMaxMapTasks();
						long maxmemory = clusterStatus.getMaxMemory();
						int maxReduceTasks = clusterStatus.getMaxReduceTasks();
						int reduceTasks = clusterStatus.getReduceTasks();
						int taskTrackers = clusterStatus.getTaskTrackers();
						long usedMemory = clusterStatus.getUsedMemory();
						Collection<String> activeTrackerNames = clusterStatus.getActiveTrackerNames();
						Collection<String> blacklistedTrackerNames = clusterStatus.getBlacklistedTrackerNames();
						int blacklistedTrackers = clusterStatus.getBlacklistedTrackers();
						State jobTrackerState = clusterStatus.getJobTrackerState();
						int numExcludenodes = clusterStatus.getNumExcludedNodes();
						long corruptBlocksCount = DodoSystem.getDodoSystem().getDFSClient().getCorruptBlocksCount();
						long defaultBlockSize = DodoSystem.getDodoSystem().getDFSClient().getDefaultBlockSize();
						short defaultReplication = DodoSystem.getDodoSystem().getDFSClient().getDefaultReplication();
						sysStateInfo.append("................System State Information.............\n");
						sysStateInfo.append("MapTasks:            \t\t\t"+String.valueOf(mapTasks)+'\n');
						sysStateInfo.append("maxMapTasks:      \t\t"+String.valueOf(maxMapTasks)+'\n');
						sysStateInfo.append("maxmemory:          \t\t"+String.valueOf(maxmemory)+'\n');
						sysStateInfo.append("maxReduceTasks:\t\t"+String.valueOf(maxReduceTasks)+'\n');
						sysStateInfo.append("reduceTasks:      \t\t\t"+String.valueOf(reduceTasks)+'\n');
						sysStateInfo.append("taskTrackers:    \t\t\t"+String.valueOf(taskTrackers)+'\n');
						sysStateInfo.append("usedMemory:    \t\t\t"+String.valueOf(usedMemory)+'\n');
						Iterator string = activeTrackerNames.iterator();
						if(string.hasNext())
						{
							sysStateInfo.append("activeTrackerNames:\n");
							while(string.hasNext())
							{
								String stringName = string.next().toString();
								sysStateInfo.append(stringName+'\n');
							}
						}
						string = blacklistedTrackerNames.iterator();
						if(string.hasNext())
						{
							sysStateInfo.append("blacklistedTrackerNames:\n");
							while(string.hasNext())
							{
								String stringName = string.next().toString();
								sysStateInfo.append(stringName+'\n');
							}
						}
						sysStateInfo.append("blacklistedTrackers:    \t"+String.valueOf(blacklistedTrackers)+'\n');
						sysStateInfo.append("jobTrackerState:    \t\t"+jobTrackerState.toString()+'\n');
						sysStateInfo.append("numExcludenodes:    \t\t"+String.valueOf(numExcludenodes)+'\n');
						sysStateInfo.append("corruptBlocksCount:    \t"+String.valueOf(corruptBlocksCount)+'\n');
						sysStateInfo.append("defaultBlockSize:    \t\t"+String.valueOf(defaultBlockSize)+'\n');
						sysStateInfo.append("defaultReplication:    \t\t"+String.valueOf(defaultReplication)+'\n');
						sysStateInfo.append(".........................................................................");
						
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else{
					sysStateInfo.setText("please connect first");
				}
			}
		});
		
		
		group = new Group(composite,SWT.NONE);
		group.setBounds(270, 20, 200, 50);
		
		button = new Button(group,SWT.BORDER);
		button.setText("DetailSysStateInfo");
		button.setBounds(5, 5, 190, 40);
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				sysStateInfo.setText("");
				if(DodoSystem.getDodoSystem().getJobClient() != null && DodoSystem.getDodoSystem().getDFSClient() != null)
				{
					ClusterStatus clusterStatus = null;
					try {
						clusterStatus = DodoSystem.getDodoSystem().getJobClient().getClusterStatus();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					int mapTasks = clusterStatus.getMapTasks();
					int maxMapTasks = clusterStatus.getMaxMapTasks();
					long maxmemory = clusterStatus.getMaxMemory();
					int maxReduceTasks = clusterStatus.getMaxReduceTasks();
					int reduceTasks = clusterStatus.getReduceTasks();
					int taskTrackers = clusterStatus.getTaskTrackers();
					long usedMemory = clusterStatus.getUsedMemory();
					Collection<String> activeTrackerNames = clusterStatus.getActiveTrackerNames();
					Collection<String> blacklistedTrackerNames = clusterStatus.getBlacklistedTrackerNames();
					int blacklistedTrackers = clusterStatus.getBlacklistedTrackers();
					State jobTrackerState = clusterStatus.getJobTrackerState();
					int numExcludenodes = clusterStatus.getNumExcludedNodes();
					long corruptBlocksCount = 0;
					try {
						corruptBlocksCount = DodoSystem.getDodoSystem().getDFSClient().getCorruptBlocksCount();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					long defaultBlockSize = DodoSystem.getDodoSystem().getDFSClient().getDefaultBlockSize();
					short defaultReplication = DodoSystem.getDodoSystem().getDFSClient().getDefaultReplication();
					DiskStatus diskStatus = null;
					try {
						diskStatus = DodoSystem.getDodoSystem().getDFSClient().getDiskStatus();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					long missingBlocksCount = 0;
					try {
						missingBlocksCount = DodoSystem.getDodoSystem().getDFSClient().getMissingBlocksCount();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					sysStateInfo.append("................System State Information.............\n");
					sysStateInfo.append("MapTasks:            \t\t\t"+String.valueOf(mapTasks)+'\n');
					sysStateInfo.append("maxMapTasks:      \t\t"+String.valueOf(maxMapTasks)+'\n');
					sysStateInfo.append("maxmemory:          \t\t"+String.valueOf(maxmemory)+'\n');
					sysStateInfo.append("maxReduceTasks:\t\t"+String.valueOf(maxReduceTasks)+'\n');
					sysStateInfo.append("reduceTasks:      \t\t\t"+String.valueOf(reduceTasks)+'\n');
					sysStateInfo.append("taskTrackers:    \t\t\t"+String.valueOf(taskTrackers)+'\n');
					sysStateInfo.append("usedMemory:    \t\t\t"+String.valueOf(usedMemory)+'\n');
					Iterator string = activeTrackerNames.iterator();
					if(string.hasNext())
					{
						sysStateInfo.append("activeTrackerNames:\n");
						while(string.hasNext())
						{
							String stringName = string.next().toString();
							sysStateInfo.append(stringName+'\n');
						}
					}
					string = blacklistedTrackerNames.iterator();
					if(string.hasNext())
					{
						sysStateInfo.append("blacklistedTrackerNames:\n");
						while(string.hasNext())
						{
							String stringName = string.next().toString();
							sysStateInfo.append(stringName+'\n');
						}
					}
					sysStateInfo.append("blacklistedTrackers:    \t"+String.valueOf(blacklistedTrackers)+'\n');
					sysStateInfo.append("jobTrackerState:    \t\t"+jobTrackerState.toString()+'\n');
					sysStateInfo.append("numExcludenodes:    \t\t"+String.valueOf(numExcludenodes)+'\n');
					sysStateInfo.append("corruptBlocksCount:    \t"+String.valueOf(corruptBlocksCount)+'\n');
					sysStateInfo.append("defaultBlockSize:    \t\t"+String.valueOf(defaultBlockSize)+'\n');
					sysStateInfo.append("defaultReplication:    \t\t"+String.valueOf(defaultReplication)+'\n');
					long capacity = diskStatus.getCapacity();
					long remaining = diskStatus.getRemaining();
					long used = diskStatus.getDfsUsed();
					sysStateInfo.append("diskCapacity:    \t\t\t"+capacity+'\n');
					sysStateInfo.append("diskRemaining:    \t\t\t"+remaining+'\n');
					sysStateInfo.append("diskUsed:    	\t\t\t"+used+'\n');
					sysStateInfo.append("missingBlocksCount:    \t"+String.valueOf(missingBlocksCount)+'\n');
					sysStateInfo.append(".........................................................................");
				}else{
					sysStateInfo.setText("please connect first");
				}
				
			}
		});
		
		
		group = new Group(composite,SWT.NONE);
		group.setBounds(30, 80, 800, 400);
		
		sysStateInfo = new Text(group,SWT.WRAP|SWT.V_SCROLL);
		sysStateInfo.setBounds(5, 5, 790, 390);
		sysStateInfo.setEditable(false);
		
	}

	private void createConfigureItem(TabFolder tabFolder) {
		
		TabItem item = new TabItem(tabFolder,SWT.NONE);
		item.setText("Configure Hadoop");
		Composite parentComposite = new Composite(tabFolder,SWT.NONE);
		parentComposite.setLayout(new FillLayout());
		item.setControl(parentComposite);
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(parentComposite,SWT.H_SCROLL|SWT.V_SCROLL);
		final Composite composite = new Composite(scrolledComposite,SWT.NONE);
		scrolledComposite.setContent(composite);
		
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinWidth(500);
		scrolledComposite.setMinHeight(1000);
		
		Group group = new Group(composite,SWT.NONE);
		group.setBounds(700, 30, 140, 60);
		
		Map m = System.getenv();
		HADOOP_HOME = (String) m.get("HADOOP_HOME");
		
		Button button = new Button(group,SWT.BORDER);
		button.setText("Restart Hadoop");
		button.setBounds(5, 5, 130, 50);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
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
				connectTipLabel.setText("DFS and JobTracker closed,please reconnect!");
				jobTrackerIPText.setEnabled(true);
				jobTrackerPortText.setEnabled(true);
				hdfsNodeIPText.setEnabled(true);
				hdfsNodePortText.setEnabled(true);
				connectButton.setEnabled(true);
				disconnectButton.setEnabled(false);
				startOrStopInfo.append("===============================================\n");
				Date date = new Date();
				startOrStopInfo.append(date.toString()+"  ");
				startOrStopInfo.append("Restart Hadoop Called...\n");
				String[] bufferedReader = platformController.excuteCommand("./hadoop/bin/stop-dfs.sh");
				startOrStopInfo.append(bufferedReader[0]);
				startOrStopInfo.append(bufferedReader[1]);
				bufferedReader = platformController.excuteCommand("./hadoop/bin/stop-mapred.sh");
				startOrStopInfo.append(bufferedReader[0]);
				startOrStopInfo.append(bufferedReader[1]);
				bufferedReader = platformController.excuteCommand("./hadoop/bin/start-dfs.sh");
				startOrStopInfo.append(bufferedReader[0]);
				startOrStopInfo.append(bufferedReader[1]);
				bufferedReader = platformController.excuteCommand("./hadoop/bin/start-mapred.sh");
				startOrStopInfo.append(bufferedReader[0]);
				startOrStopInfo.append(bufferedReader[1]);
			}
			
		});
		
		group = new Group(composite,SWT.NONE);
		group.setBounds(20, 30, 650, 60);
		
		Label label = new Label(group,SWT.NONE);
		label.setText("FileName: ");
		label.setBounds(5, 20, 150, 30);
		
		fileName = new Text(group,SWT.BORDER);
		fileName.setBounds(90, 10, 550, 40);
		
		group = new Group(composite,SWT.NONE);
		group.setBounds(350, 100, 140, 60);
		
		button = new Button(group,SWT.BORDER);
		button.setText("Read Setttings");
		button.setBounds(5, 5, 130, 50);
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e){
				xml = new XMLUtility();
				propertyList = xml.readXml("./hadoop/conf/"+fileName.getText());
				Iterator i = propertyList.iterator();
				
				while(k != -1) {
					nam[k].dispose();
					val[k].dispose();
					k--;
					h -= 40;
				}
				
				while(i.hasNext())
				{
					Element propertyElement = (Element) i.next();
					String name = propertyElement.getChild("name").getValue();
					String value = propertyElement.getChild("value").getValue();
					++k;
					nam[k] = new Text(composite, SWT.BORDER);
					nam[k].setBounds(20, h, 400, 30);
					nam[k].setText(name);
					val[k] = new Text(composite, SWT.BORDER);
					val[k].setBounds(440, h, 400, 30);
					val[k].setText(value);
					h += 40;
				}
			}
		});
		
		group = new Group(composite,SWT.NONE);
		group.setBounds(500, 100, 140, 60);
		
		button = new Button(group,SWT.BORDER);
		button.setText("Save Setttings");
		button.setBounds(5, 5, 130, 50);
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e){
				int i = 0;
				while(i <= k)
				{
					String dnd = nam[i].getText().trim();
					String dndValue = val[i].getText().trim();
					if(!dnd.equals("") && !dndValue.equals("")) {
						try {
							xml.update(propertyList, dnd, dndValue);
						} catch (JDOMException e1) {
							e1.printStackTrace();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					i++;
				}
				
				xml.saveXml("./hadoop/conf/"+fileName.getText());
				
			}
		});
	
		group = new Group(composite,SWT.NONE);
		group.setBounds(650, 100, 140, 60);
		
		button = new Button(group,SWT.BORDER);
		button.setText("Add NewItem");
		button.setBounds(5, 5, 130, 50);
		button.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e){
				++k;
				nam[k] = new Text(composite,SWT.BORDER);
				nam[k].setBounds(20, h, 400, 30);
				val[k] = new Text(composite,SWT.BORDER);
				val[k].setBounds(440, h, 400, 30);
				h += 40;
			}
		});
	}

	private void createConnectItem(TabFolder tabFolder) {
		
		TabItem item = new TabItem(tabFolder,SWT.NONE);
		item.setText("Connect Hadoop");
		Composite composite = new Composite(tabFolder,SWT.NONE);	
		item.setControl(composite);
		
		Group group = new Group(composite,SWT.NONE);
		group.setText("IP:Port");
		group.setBounds(100, 100, 710, 200);
		
		Label label = new Label(group,SWT.NONE);
		label.setText("JobTracker's IP address:");
		label.setBounds(35,50,160,30);
		
		jobTrackerIPText = new Text(group,SWT.BORDER);
		jobTrackerIPText.setBounds(210, 45, 300, 30);
		jobTrackerIPText.setText("localhost");
		
		label = new Label(group,SWT.NONE);
		label.setText("Port:");
		label.setBounds(535, 50, 50, 30);
		
		jobTrackerPortText = new Text(group,SWT.BORDER);
		jobTrackerPortText.setBounds(590, 45, 90, 30);
		jobTrackerPortText.setText("9001");
		
		
		label = new Label(group,SWT.NONE);
		label.setText("Hdfs's IP address:");
		label.setBounds(35,100,160,30);
		
		hdfsNodeIPText = new Text(group,SWT.BORDER);
		hdfsNodeIPText.setBounds(210, 95, 300, 30);
		hdfsNodeIPText.setText("localhost");
		
		label = new Label(group,SWT.NONE);
		label.setText("Port:");
		label.setBounds(535, 100, 50, 30);
		
		hdfsNodePortText = new Text(group,SWT.BORDER);
		hdfsNodePortText.setBounds(590, 95, 90, 30);
		hdfsNodePortText.setText("9000");
		
		group = new Group(composite,SWT.NONE);
		group.setBounds(500, 350, 330, 50);
		
		connectTipLabel = new Label(composite,SWT.WRAP|SWT.BORDER);
		connectTipLabel.setBounds(650, 30, 200, 50);
		
		connectButton = new Button(group,SWT.BORDER);
		connectButton.setText("connect");
		connectButton.setBounds(10,5,150,40);
		connectButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e){
				if(platformController.connect(jobTrackerIPText.getText(), jobTrackerPortText.getText(),
						hdfsNodeIPText.getText(),hdfsNodePortText.getText()))
				{
					connectTipLabel.setText("The master is connected!");
					jobTrackerIPText.setEnabled(false);
					jobTrackerPortText.setEnabled(false);
					hdfsNodeIPText.setEnabled(false);
					hdfsNodePortText.setEnabled(false);
					connectButton.setEnabled(false);
					disconnectButton.setEnabled(true);
				}
				else
				{
					connectTipLabel.setText("Connect Failure!");
				}
			}
		});
		
		disconnectButton = new Button(group,SWT.BORDER);
		disconnectButton.setText("disconnect");
		disconnectButton.setBounds(170,5,150,40);
		disconnectButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e){
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
				connectTipLabel.setText("The master is disconnected!");
				jobTrackerIPText.setEnabled(true);
				jobTrackerPortText.setEnabled(true);
				hdfsNodeIPText.setEnabled(true);
				hdfsNodePortText.setEnabled(true);
				connectButton.setEnabled(true);
				disconnectButton.setEnabled(false);
			}
		});
	}

	public void work() {
		paintTheBoard();
	}

	public void revisiable() {
		shell.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		PlatformMainBoard testBoard = new PlatformMainBoard(null);
		testBoard.work();
	}
}
