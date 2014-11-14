package toolBox.gui;


import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import toolBox.core.system.DodoSystem;

import java.io.File;
import java.io.IOException;
public class TreeBoard {
	Shell shell=null;
	Display display = Display.getDefault();
	private static DistributedFileSystem hdfs=DodoSystem.getDodoSystem().getDistributedFileSystem();
	private Tree tree=null;
	String dst=null;
	String path=null;
	TreeItem root=null;
	
	public TreeBoard(){
		
	}
	
	public TreeBoard(final Text text,String position,boolean isHDFS){
		
		shell =  new Shell(display, SWT.CLOSE | SWT.MIN);
		shell.setSize(400, 500);
		shell.setText("HDFS Directory");
		tree=new Tree(shell,SWT.SINGLE);
		tree.setBounds(10, 10, 350, 350);
		root = new TreeItem(tree, SWT.NONE);
		if(isHDFS){
			path=hdfs.getUri().toString()+"/"+position;
			root.setText("HDFS");
			try {
				readFolderOnHDFS(path, root);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			path=position;
			root.setText("Local");
			File file=new File(path);
			readFolderOnLocal(file, root);
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
            	text.setText(getDst());
            	shell.close();
            }
        });
		shell.setLayout(null);
		shell.open();
	}
	
	public static void readFolderOnHDFS(String path, TreeItem Node)
			throws IOException {
		FileStatus[] fileStatusArray = hdfs.listStatus(new Path(path));
		for (FileStatus fileStatus : fileStatusArray) {
			TreeItem node = new TreeItem(Node, SWT.NONE);
			node.setText(fileStatus.getPath().getName());
			node.setData(fileStatus.getPath().toString());
			if (!hdfs.isFile(fileStatus.getPath()))
				readFolderOnHDFS(fileStatus.getPath().toString(),node);
		}
	}
	
	public void readFolderOnLocal(File file,TreeItem Node){
		if(file.isFile()){
			return;
		}
		else{
			File[] fileList=file.listFiles();
			for (int i=0;i<fileList.length;i++){
				TreeItem child = new TreeItem(Node, SWT.NONE);
				child.setText(fileList[i].getName());
				child.setData(fileList[i].getPath());
				readFolderOnLocal(fileList[i],child);
			}
		}
	}
	
	public String getDst(){
		return dst;
	}
}
