package toolBox.gui.datasets;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import toolBox.core.utility.UtilityParameters;
import toolBox.datasetsManager.applications.DataDescribe;
import toolBox.datasetsManager.applications.XMLReader;
import toolBox.gui.HdfsTreeBoard;

import java.io.IOException;
import java.util.Hashtable;

public class DatasetsTree {
	private Composite boardComposite ;
	private Path workDir;
	private DistributedFileSystem dfs;
	private Shell shell;
	private DataDescribe ddc ;
	private Text relationText;
	private Table descriptionTable;
	
	public DatasetsTree(Composite boardComposite, Path workDir, DistributedFileSystem dfs, 
			Text relationText, Table descriptionTable, DataDescribe ddc, Shell shell){
		this.boardComposite = boardComposite;
		this.workDir = workDir;
		this.dfs = dfs;
		this.ddc = ddc;
		this.shell = shell;
		this.relationText = relationText;
		this.descriptionTable = descriptionTable;	
	}
	
	public void paintTree(){
		final Tree tree = new Tree(boardComposite, SWT.SINGLE | SWT.BORDER);
		tree.setTouchEnabled(true);
		tree.setLinesVisible(true);
		tree.setBounds(10, 10, 200, 522);
		Path dst = new Path(workDir + "/");
		FileStatus[] fList = null;
		TreeItem root = new TreeItem(tree, SWT.NONE);
		FileStatus rootStatus = null;
		try {
			rootStatus = dfs.getFileStatus(workDir);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		root.setText(rootStatus.getPath().getName());
		root.setData(rootStatus.getPath().toString());
		root.setImage(new Image(null,"source/images/hadmin.gif"));


		try {
			fList = dfs.listStatus(dst);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	    for (int i = 0; i < fList.length; i++) {
		      FileStatus file = fList[i];
		      TreeItem treeItem = new TreeItem(root, SWT.NONE);
		      
		      treeItem.setText(file.getPath().getName());
		      treeItem.setData(file.getPath().toString());
		      try {
				if (!dfs.isFile(file.getPath())){
					  treeItem.setImage(new Image(null,UtilityParameters.IMAGE_FOLDER_PATH));
				  }
				  else{
					  treeItem.setImage(new Image(null,UtilityParameters.IMAGE_FILE_PATH));
				  }
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		     // System.out.println(treeItem.getData());
		      new TreeItem(treeItem, SWT.NONE);
		    }
	    tree.addListener(SWT.Expand, new Listener() {
		      public void handleEvent(Event e) {
		        TreeItem item = (TreeItem) e.item;
		        if (item == null)
		          return;
		        if (item.getItemCount() == 1) {
		          TreeItem firstItem = item.getItems()[0];		          
		          if (firstItem.getData() != null)
		            return;
		          firstItem.dispose();
		        } else {
		          return;
		        }
		        
				try {
					String path = (String) item.getData();
					HdfsTreeBoard.readFolder(path, item);
				} catch (IOException event) {
					// TODO Auto-generated catch block
					event.printStackTrace();
				}
				
		          
		        
		      }
		    });
		//鼠标右键单击事件
	    tree.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				Point point = event.widget.getDisplay().map(null, (Control) event.widget, event.x, event.y);
				final TreeItem item = tree.getItem(point);
				createEditPopup(event, item);
		     }
		});
	}

	public Menu createEditPopup(Event event,  TreeItem treeItem) {  
        Menu popMenu = new Menu(shell, SWT.POP_UP);
        final TreeItem item = treeItem;
        popMenu.setLocation(event.x, event.y);
        popMenu.setVisible(true);
        
        MenuItem downloadItem = new MenuItem(popMenu, SWT.PUSH);  
        downloadItem.setText("&Download from DFS");  
        
        MenuItem createDirItem = new MenuItem(popMenu, SWT.PUSH);  
        createDirItem.setText("&Create new directory");  
        
        MenuItem uploadFileItem = new MenuItem(popMenu, SWT.PUSH);  
        uploadFileItem.setText("&Upload files to DFS"); 
         
        MenuItem uploadDirItem = new MenuItem(popMenu, SWT.PUSH);  
        uploadDirItem.setText("&Upload directory to DFS"); 
        
        MenuItem createFileItem = new MenuItem(popMenu, SWT.PUSH);  
        createFileItem.setText("&Create data description file"); 
        
        MenuItem editFileItem = new MenuItem(popMenu, SWT.PUSH);  
        editFileItem.setText("&Edit data description file");
        
        MenuItem analyzeFileItem = new MenuItem(popMenu, SWT.PUSH);  
        analyzeFileItem.setText("&Analyze data description file"); 
        
        MenuItem renameItem = new MenuItem(popMenu, SWT.PUSH);  
        renameItem.setText("&Rename"); 
        
        MenuItem deleteItem = new MenuItem(popMenu, SWT.PUSH);  
        deleteItem.setText("&Delete"); 
        
        MenuItem refreshItem = new MenuItem(popMenu, SWT.PUSH);  
        refreshItem.setText("&Refresh"); 
                
        downloadItem.addListener(SWT.Selection, new Listener() {  
        	public void handleEvent(Event event) {
                // TODO Auto-generated method stub  
            	DirectoryDialog dd = new DirectoryDialog(shell);
            	dd.setMessage("choose file to save");
            	dd.setText("Directory Dialog");
            	dd.setFilterPath("/");
            	Path dst = new Path(dd.open()+"/");
            	System.out.println("dst="+dst);
            	//TreeItem item = (TreeItem)event.item;
            	Path src = new Path((String)item.getData());
            	System.out.println("src="+src);
            	
            	try {
					dfs.copyToLocalFile(src, dst);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}

        	});  

        
	    createDirItem.addListener(SWT.Selection, new Listener() {  
        	public void handleEvent(Event event) {
        		InputDialog inputDialog = new InputDialog(null,"Create subfolder","Enter the name of subfolder",null,null);
        		Path dst = null;
        		 if(inputDialog.open() == InputDialog.OK){
        			 dst = new Path((String)item.getData()+"/"+inputDialog.getValue());
        			 System.out.println(dst);
						try {
							dfs.mkdirs(dst);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        		     }       	



        	}

        	}); 
        
        uploadFileItem.addListener(SWT.Selection, new Listener() {  
        	public void handleEvent(Event event) {
                // TODO Auto-generated method stub  
            	FileDialog fd = new FileDialog(shell,SWT.SINGLE);
            	fd.setText("选择文件");
            	fd.setFilterNames(new String[]{"所有文件"});
            	fd.setFilterExtensions(new String[]{"*.*;"});
            	Path src = new Path(fd.open());
            	//System.out.println("dst="+dst);
            	//TreeItem item = (TreeItem)event.item;
            	Path dst = new Path((String)item.getData());
            	//System.out.println("src="+src);
            	
            	try {
					dfs.copyFromLocalFile(src, dst);
					TreeItem newTreeItem = new TreeItem(item, SWT.NONE);
					newTreeItem.setText(src.getName());
					Path newPath = new Path(dst+"/"+src.getName()); 
					System.out.println(newPath.toString());
					newTreeItem.setData(newPath.toString());
					newTreeItem.setImage(new Image(null,UtilityParameters.IMAGE_FILE_PATH));
						  }						
				catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}

        	});  
        
        uploadDirItem.addListener(SWT.Selection, new Listener() {  
        	public void handleEvent(Event event) {
                // TODO Auto-generated method stub  
            	DirectoryDialog dd = new DirectoryDialog(shell);
            	dd.setMessage("choose directory to upload");
            	dd.setText("Directory Dialog");
            	dd.setFilterPath("/");
            	Path src = new Path(dd.open()+"/");
            	//System.out.println("dst="+dst);
            	//TreeItem item = (TreeItem)event.item;
            	Path dst = new Path((String)item.getData());
            	//System.out.println("src="+src);
            	
            	try {
					dfs.copyFromLocalFile(src, dst);
					TreeItem newTreeItem = new TreeItem(item, SWT.NONE);
					newTreeItem.setText(src.getName());
					Path newPath = new Path(dst+"/"+src.getName()); 
					System.out.println(newPath.toString());
					newTreeItem.setData(newPath.toString());
					newTreeItem.setImage(new Image(null,UtilityParameters.IMAGE_FOLDER_PATH));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}

        	});  
                
        /*createFileItem.addListener(SWT.Selection, new Listener() {  
        	public void handleEvent(Event event) {
                // TODO Auto-generated method stub  
            	Path src = new Path((String)item.getData());	            	
            	System.out.println("src="+src);	 
            	FSDataInputStream input = null;
            	try {
            		input = dfs.open(src);
            		ddc = new DataDescribe(src,input);
            		int countColumn = ddc.getCount();
            		String relationName = ddc.getName();
            		relationText.setText(relationName);
            		for(int i = 0; i <countColumn; i++){
            			String[] str = {String.valueOf(i), "attribute"+i, "unknown"};
            			TableItem item = new TableItem (descriptionTable, SWT.NONE);
            			item.setText(str);	            			
            		}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}

        	});*/
        
        createFileItem.addListener(SWT.Selection, new Listener() {  
        	public void handleEvent(Event event) {
                // TODO Auto-generated method stub  
            	Path src = new Path((String)item.getData());
            	//System.out.println("src="+src);
            	ddc.setPath(src);
            	String relationName = ddc.getRelationName(src);
        		relationText.setText(relationName);
        		//descriptionTable.setEnabled(true);
        	}
        	});
        
        editFileItem.addListener(SWT.Selection, new Listener() {  
        	public void handleEvent(Event event) {
                // TODO Auto-generated method stub  
            	Path src = new Path((String)item.getData());
            	SAXReader reader = new SAXReader();
            	FSDataInputStream in;
				try {
					in = dfs.open(src);
					Document doc =reader.read(in);
					Element root = doc.getRootElement();
					XMLReader rd = new XMLReader(root);
					String relationName = rd.getName();
					relationText.setText(relationName);
					int count = rd.getCount();
					descriptionTable.removeAll();
					String[] attributeArray = rd.getAttributeArray();
					String[] typeArray = rd.getTypeArray();
					Hashtable<TableItem, TableItemControls> tablecontrols = new Hashtable<TableItem, TableItemControls>();
					for(int i = 0; i <count; i++){
						String[] str = {String.valueOf(i), attributeArray[i], typeArray[i]};
						TableItem item = new TableItem (descriptionTable, SWT.NONE);
						item.setText(str);	     
						final TableEditor editor1 = new TableEditor(descriptionTable);
						// 创建一个文本框，用于输入文字
						final Text text = new Text(descriptionTable, SWT.NONE);
						// 将文本框当前值，设置为表格中的值
						text.setText(item.getText(1));
						// 设置编辑单元格水平填充
						editor1.grabHorizontal = true;
						// 关键方法，将编辑单元格与文本框绑定到表格的第一列
						editor1.setEditor(text, item, 1);
						// 当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
						// 否则即使改变的文本框的值，对表格中的数据也不会影响
						text.addModifyListener(new ModifyListener() {
							public void modifyText(ModifyEvent e) {
								editor1.getItem().setText(1, text.getText());
							}

						});
						// 同理，为第二列绑定下来框CCombo
						final TableEditor editor2 = new TableEditor(descriptionTable);
						final CCombo combo = new CCombo(descriptionTable, SWT.DROP_DOWN| SWT.READ_ONLY);
						combo.removeAll();
						combo.add("Unkonwn");
						combo.add("Numeric");
						combo.add("Nominal");
						System.out.println(typeArray[i]);
						if(typeArray[i].equals("Unkonwn")){
							combo.select(0);
						}
						else {
							if(typeArray[i].equals("Numeric")){
								combo.select(1);
							}
							else{
								combo.select(2);
							}
						}
						
						editor2.grabHorizontal = true;
						editor2.setEditor(combo, item, 2);
						editor2.getItem().setText(2, combo.getText());
						combo.addModifyListener(new ModifyListener() {
							public void modifyText(ModifyEvent e) {
								editor2.getItem().setText(2, combo.getText());
							}

						});
						// 保存TableItem与绑定Control的对应关系，删除TableItem时使用
						TableItemControls cons = new TableItemControls(text, combo,
								editor1, editor2);
						tablecontrols.put(item, cons);
					}
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
        	}
        	});
                        
        analyzeFileItem.addSelectionListener(new SelectionListener() {  
  
            @Override  
            public void widgetSelected(SelectionEvent arg0) {  
                // TODO Auto-generated method stub  

            }  
  
            @Override  
            public void widgetDefaultSelected(SelectionEvent arg0) {  
                // TODO Auto-generated method stub  
  
            }  
        });
                        
        renameItem.addListener(SWT.Selection, new Listener() {  
        	public void handleEvent(Event event) {
        		InputDialog inputDialog = new InputDialog(null,"Rename","Enter the new name ",null,null);
        		String src = (String)item.getData();
        		int index = src.lastIndexOf("/");
        		System.out.println(src);
        		String dst = src.substring(0, index);
                System.out.println(dst);
        		Path srcPath = new Path(src);
        		Path dstPath = new Path(dst);
        		if(inputDialog.open() == InputDialog.OK){
        			dstPath = new Path(dstPath+"/"+inputDialog.getValue());
        			 System.out.println(dstPath);
						try {
							dfs.rename(srcPath, dstPath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        		     }       	



        	}

        	}); 
                        
        deleteItem.addListener(SWT.Selection, new Listener() {  
        	public void handleEvent(Event event) {
            	Path src = new Path((String)item.getData());         	
					try {
						boolean b = MessageDialog.openConfirm(shell, "Confirm delete from DFS", 
								
								"Are sure to delete this file or directory from DFS?");
						if(b){
							dfs.delete(src, true);
							item.dispose();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        	}

        	});  
                        
        refreshItem.addListener(SWT.Selection, new Listener() {  
        	public void handleEvent(Event event) {
        		item.getParent().dispose();
        		paintTree();

        	}

        	});  
 
 
        return popMenu;  
    }
}
