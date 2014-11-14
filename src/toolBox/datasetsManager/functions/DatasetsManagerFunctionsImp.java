package toolBox.datasetsManager.functions;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;



public class DatasetsManagerFunctionsImp implements DatasetsManagerFunctions{
	
	
    public void listFile(DistributedFileSystem dfs, TreeItem treeItem, String path) {  
        Path workDir = dfs.getWorkingDirectory();  
        Path dst;  
        if (null == path || "".equals(path)) {  
            dst = workDir;  
        } else {  
            dst = new Path(path);  
        }  
        try {  
            String relativePath = "";  
            FileStatus[] fList = dfs.listStatus(dst);  
           for (FileStatus f : fList) {  
              if (null != f) {  
                  relativePath = new StringBuffer()  
                           .append(f.getPath().getParent()).append("/")  
                           .append(f.getPath().getName()).toString();  
                    treeItem.setText (f.getPath().getName()); 
                    treeItem.setData(f); 		
                    TreeItem newTreeItem = new TreeItem(treeItem, SWT.NONE);   
                    if (f.isDir()) {  
                        listFile(dfs, treeItem, relativePath);  
 //   		for (FileStatus fileStatus : fileStatusArray) {
 //  			DefaultMutableTreeNode node=new DefaultMutableTreeNode(fileStatus.getPath().getName());
 //   			Node.add(node);
 //   			if (!dfs.isFile(fileStatus.getPath()))
 //   				listFile(dfs,newTreeItem,f.getPath().toString());
 //   		}
                    } 
                      
                }  
            }  
        } catch (Exception e) {  

        } finally {  
        }  
    }  

 
     
	
	  public static Menu createEditPopup(Shell shell) {  
	        Menu popMenu = new Menu(shell, SWT.POP_UP);  
	        
	        MenuItem downloadItem = new MenuItem(popMenu, SWT.PUSH);  
	        downloadItem.setText("&Download from DFS");  
	        
	        MenuItem createDirItem = new MenuItem(popMenu, SWT.PUSH);  
	        createDirItem.setText("&Create new directory");  
	        
	        MenuItem uploadFileItem = new MenuItem(popMenu, SWT.PUSH);  
	        uploadFileItem.setText("&Upload files to DFS"); 
	         
	        MenuItem uploadDirItem = new MenuItem(popMenu, SWT.PUSH);  
	        uploadDirItem.setText("&Upload directory to DFS"); 
	        
	        MenuItem openFileItem = new MenuItem(popMenu, SWT.PUSH);  
	        openFileItem.setText("&Open data file"); 
	        
	        MenuItem analyzeFileItem = new MenuItem(popMenu, SWT.PUSH);  
	        analyzeFileItem.setText("&Analyze data description file"); 
	        
	        MenuItem renameItem = new MenuItem(popMenu, SWT.PUSH);  
	        renameItem.setText("&Rename"); 
	        
	        MenuItem deleteItem = new MenuItem(popMenu, SWT.PUSH);  
	        deleteItem.setText("&Delete"); 
	        
	        MenuItem refreshItem = new MenuItem(popMenu, SWT.PUSH);  
	        refreshItem.setText("&Refresh"); 
	                
	        downloadItem.addSelectionListener(new SelectionListener() {  
	  
	            @Override  
	            public void widgetSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  

	            }  
	  
	            @Override  
	            public void widgetDefaultSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  
	  
	            }  
	        });  
	        
		    createDirItem.addSelectionListener(new SelectionListener() {  
	  
	            @Override  
	            public void widgetSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  

	            }  
	  
	            @Override  
	            public void widgetDefaultSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  
	  
	            }  
	        }); 
	        
	        uploadFileItem.addSelectionListener(new SelectionListener() {  
	  
	            @Override  
	            public void widgetSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  

	            }  
	  
	            @Override  
	            public void widgetDefaultSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  
	  
	            }  
	        }); 
	        
	        uploadDirItem.addSelectionListener(new SelectionListener() {  
	  
	            @Override  
	            public void widgetSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  

	            }  
	  
	            @Override  
	            public void widgetDefaultSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  
	  
	            }  
	        });
	                
	        openFileItem.addSelectionListener(new SelectionListener() {  
	  
	            @Override  
	            public void widgetSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  

	            }  
	  
	            @Override  
	            public void widgetDefaultSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  
	  
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
	                        
	        renameItem.addSelectionListener(new SelectionListener() {  
	  
	            @Override  
	            public void widgetSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  

	            }  
	  
	            @Override  
	            public void widgetDefaultSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  
	  
	            }  
	        });
	                        
	        deleteItem.addSelectionListener(new SelectionListener() {  
	  
	            @Override  
	            public void widgetSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  

	            }  
	  
	            @Override  
	            public void widgetDefaultSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  
	  
	            }  
	        });
	                        
	        refreshItem.addSelectionListener(new SelectionListener() {  
	  
	            @Override  
	            public void widgetSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  

	            }  
	  
	            @Override  
	            public void widgetDefaultSelected(SelectionEvent arg0) {  
	                // TODO Auto-generated method stub  
	  
	            }  
	        });
	 
	 
	        return popMenu;  
	    }  
	
	

}


