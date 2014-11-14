package toolBox.gui;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import toolBox.core.system.DodoSystem;
import toolBox.core.utility.UtilityParameters;

import java.io.IOException;

public class HdfsTreeBoard {
	final Shell shell;
	final Display display = Display.getDefault();
	private static DistributedFileSystem hdfs=DodoSystem.getDodoSystem().getDistributedFileSystem();
	private Tree tree=null;
	String dst=null;
	
	public HdfsTreeBoard(final Text dataSetDirText,String position){
		
		shell =  new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setSize(400, 500);
		shell.setText("HDFS Directory");
		String path=hdfs.getUri().toString()+"/"+position;
		tree=new Tree(shell,SWT.SINGLE);
		tree.setBounds(10, 10, 350, 350);
		TreeItem root = new TreeItem(tree, SWT.NONE);
		root.setText("DFS");
		
		try {
			readFolder(path, root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tree.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent evt) { //

			}

			public void mouseDown(MouseEvent evt) {
				TreeItem selected = tree.getItem(new Point(evt.x, evt.y)); // 取节点控件
				if (selected != null && evt.button == 1
						&& selected.getParentItem() != null) // 如果取到节点控件不是根节点，且是鼠标右键
				{
					dst = selected.getData().toString();
				}
				//
			}

		}); 
		Button button=new Button(shell,SWT.NONE);
		button.setText("CANCLE");
		button.setBounds(100, 400, 80, 30);
		button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	shell.close();
            }
        });
		
		button=new Button(shell,SWT.NONE);
		button.setText("OK");
		button.setBounds(220,400, 80, 30);
		button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	dataSetDirText.setText(getDst());
            	shell.close();
            }
        });
		shell.setLayout(null);
		shell.open();
	}
	
	public static void readFolder(String path, TreeItem Node)
			throws IOException {
		FileStatus[] fileStatusArray = hdfs.listStatus(new Path(path));
		for (FileStatus fileStatus : fileStatusArray) {
			//System.out.println(fileStatus.getPath().toString());
			TreeItem node = new TreeItem(Node, SWT.NONE);
			node.setText(fileStatus.getPath().getName());
			node.setData(fileStatus.getPath().toString());
			//System.out.println(fileStatus.getPath().getName());
			if (!hdfs.isFile(fileStatus.getPath())){
				node.setImage(new Image(null,UtilityParameters.IMAGE_FOLDER_PATH));
				readFolder(fileStatus.getPath().toString(), node);
			}
			else{
				node.setImage(new Image(null,UtilityParameters.IMAGE_FILE_PATH));
			}	
		}
	}
	
	
	public String getDst(){
		return dst;
	}
}
