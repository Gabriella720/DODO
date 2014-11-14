package toolBox.gui.datasets;

import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.*;
import toolBox.datasetsManager.applications.DataDescribe;

import java.util.Hashtable;

public class DatasetsDescriptionBoard {

	private Text relationText;
	public Text getRelationText() {
		return relationText;
	}

	private Table descriptionTable;
	public Table getDescriptionTable() {
		return descriptionTable;
	}

	private TabFolder tabFolder;
	private DataDescribe ddc;
	private Composite descriptionComposite;
	private DistributedFileSystem dfs;
	private Hashtable<TableItem, TableItemControls> tablecontrols = new Hashtable<TableItem, TableItemControls>();

	public DatasetsDescriptionBoard(TabFolder tabFolder, DataDescribe ddc, DistributedFileSystem dfs) {			
		this.tabFolder = tabFolder;
		this.ddc = ddc;	
		this.dfs = dfs;
	}

	public void paint() {
		TabItem tbtmDataDescription = new TabItem(tabFolder, SWT.NONE);
		tbtmDataDescription.setText("Data description");

		descriptionComposite = new Composite(tabFolder, SWT.NONE);
		tbtmDataDescription.setControl(descriptionComposite);

		Label lblRelation_description = new Label(descriptionComposite,
				SWT.NONE);
		lblRelation_description.setBounds(10, 10, 80, 30);
		lblRelation_description.setText("Relation:");

		relationText = new Text(descriptionComposite, SWT.BORDER);
		relationText.setBounds(96, 10, 642, 30);

		Label lblAttributes_description = new Label(descriptionComposite,
				SWT.NONE);
		lblAttributes_description.setBounds(10, 46, 80, 20);
		lblAttributes_description.setText("Attributes:");

		descriptionTable = new Table(descriptionComposite, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.CHECK);
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

		// 添加可编辑的单元格
		// /******************************************************
		TableItem[] items = descriptionTable.getItems();
		for (int i = 0; i < items.length; i++) {
			// 第一列设置，创建TableEditor对象
			final TableEditor editor = new TableEditor(descriptionTable);
			// 创建一个文本框，用于输入文字
			final Text text = new Text(descriptionTable, SWT.NONE);
			// 将文本框当前值，设置为表格中的值
			text.setText(items[i].getText(1));
			// 设置编辑单元格水平填充
			editor.grabHorizontal = true;
			// 关键方法，将编辑单元格与文本框绑定到表格的第一列
			editor.setEditor(text, items[i], 1);
			// 当文本框改变值时，注册文本框改变事件，该事件改变表格中的数据。
			// 否则即使改变的文本框的值，对表格中的数据也不会影响
			text.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					editor.getItem().setText(1, text.getText());
				}

			});
			// 同理，为第二列绑定下来框CCombo
			final TableEditor editor1 = new TableEditor(descriptionTable);
			final CCombo combo = new CCombo(descriptionTable, SWT.DROP_DOWN | SWT.READ_ONLY);
			combo.add("Unkonwn");
			combo.add("Numeric");
			combo.add("Nominal");
			combo.setText(items[i].getText(2));
			editor1.grabHorizontal = true;
			editor1.setEditor(combo, items[i], 2);
			combo.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					editor1.getItem().setText(1, combo.getText());
				}

			});

			// 保存TableItem与绑定Control的对应关系，删除TableItem时使用
			TableItemControls cons = new TableItemControls(text, combo,
					editor1, editor1);
			tablecontrols.put(items[i], cons);

		}
		
		/*final TableCursor cursor = new TableCursor(descriptionTable, SWT.NONE);
		   //创建可编辑的控件
		   final ControlEditor editor = new ControlEditor(cursor);
		   editor.grabHorizontal = true;
		   editor.grabVertical = true;
		   //为TableCursor对象注册事件
		   cursor.addSelectionListener( new SelectionAdapter() {
		    //但移动光标，在单元格上单击回车所触发的事件
		    public void widgetDefaultSelected(SelectionEvent e) {
		     //创建一个文本框控件
		     final Text text = new Text(cursor, SWT.NONE);
		     //获得当前光标所在的行TableItem对象
		     TableItem row = cursor.getRow();
		     //获得当前光标所在的列数
		     int column = cursor.getColumn();
		     //当前光标所在单元格的值赋给文本框
		     text.setText(row.getText(column));
		     //为文本框注册键盘事件
		     text.addKeyListener(new KeyAdapter() {
		      public void keyPressed(KeyEvent e) {
		       //此时在文本框上单击回车后，这是表格中的数据为修改后文本框中的数据
		       //然后释放文本框资源
		       if (e.character == SWT.CR) {
		        TableItem row = cursor.getRow();
		        int column = cursor.getColumn();
		        row.setText(column, text.getText());
		        text.dispose();
		       }
		       //如果在文本框中单击了ESC键，则并不对表格中的数据进行修改
		       if (e.character == SWT.ESC) {
		        text.dispose();
		       }
		      }
		     });
		     //注册焦点事件
		     text.addFocusListener(new FocusAdapter() {
		      //当该文本框失去焦点时，释放文本框资源
		      public void focusLost(FocusEvent e) {
		       text.dispose();
		      }
		     });
		     //将该文本框绑定到可编辑的控件上
		     editor.setEditor(text);
		     //设置文本框的焦点
		     text.setFocus();
		    }
		    //移动光标到一个单元格上所触发的事件
		    public void widgetSelected(SelectionEvent e) {
		     descriptionTable.setSelection(new TableItem[] { cursor.getRow()});
		    }
		   });*/
		
		   
		   /*final TableEditor tableEditor = new TableEditor (descriptionTable);
		   tableEditor.horizontalAlignment = SWT.LEFT;
		   tableEditor.grabHorizontal = true;
		   tableEditor.grabVertical = true;
		   descriptionTable.addListener (SWT.MouseDown, new Listener () {
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
								tableEditor.setEditor (text, item, i);
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
			});*/

		Button btnAdd_description = new Button(descriptionComposite, SWT.NONE);
		btnAdd_description.setText("Add");
		btnAdd_description.setBounds(150, 451, 90, 30);
		btnAdd_description.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String[] str = {
						String.valueOf(descriptionTable.getItemCount()), "",
						"unknown" };

				TableItem item = new TableItem(descriptionTable, SWT.NONE);
				item.setText(str);
				// 第一列设置，创建TableEditor对象
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
				final CCombo combo = new CCombo(descriptionTable, SWT.DROP_DOWN | SWT.READ_ONLY);
				combo.removeAll();
				combo.add("Unkonwn");
				combo.add("Numeric");
				combo.add("Nominal");
				combo.select(0);
				editor2.grabHorizontal = true;
				editor2.setEditor(combo, item, 2);
				//editor2.getItem().setText(2, combo.getText());
				combo.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						editor2.getItem().setText(2, combo.getText());
					}

				});
				// 保存TableItem与绑定Control的对应关系，删除TableItem时使用
				TableItemControls cons = new TableItemControls(text, combo,
						editor1, editor2);
				tablecontrols.put(item, cons);
				descriptionTable.pack();
				descriptionTable.setBounds(10, 72, 728, 373);
				descriptionTable.setHeaderVisible(true);
				descriptionTable.setLinesVisible(true);
			}
		});

		Button btnDelete_description = new Button(descriptionComposite, SWT.NONE);
		btnDelete_description.setText("Delete");
		btnDelete_description.setBounds(331, 451, 90, 30);
		btnDelete_description.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				// 首先获得表格中所有的行
				TableItem[] items = descriptionTable.getItems();
				// 循环所有行
				for (int i = items.length - 1; i >= 0; i--) {
					// 如果该行没有被选中，继续循环
					if (!items[i].getChecked())
						continue;
					// 否则选中，查找该表格中是否有该行
					int index = descriptionTable.indexOf(items[i]);
					// 如果没有该行，继续循环
					if (index < 0)
						continue;
					// 删除绑定的控件
					TableItemControls cons = tablecontrols.get(items[index]);
					if (cons != null) {
						cons.dispose();
						tablecontrols.remove(items[index]);
						System.out.println("dispose " + index);
					}
					// 如果有该行，删除该行
					// items[index].dispose();
					descriptionTable.remove(index);
					System.out.println("i=" + i + ", index=" + index);
					System.out.println("行数:" + descriptionTable.getItemCount());
					// table.pack();
					descriptionTable.pack();
					descriptionTable.setBounds(10, 72, 728, 373);
					descriptionTable.setHeaderVisible(true);
					descriptionTable.setLinesVisible(true);
				}
			}
		});

		Button btnSave_description = new Button(descriptionComposite,
				SWT.CENTER);
		btnSave_description.setBounds(512, 451, 90, 30);
		btnSave_description.setText("Save");
		btnSave_description.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(relationText.getText().equals("")){
					Shell shell =Display.getCurrent().getActiveShell();
					MessageDialog.openWarning(shell, "Warning", "Relation name can not be null!");
					return;
				}
				else{
					ddc.setRelationName(relationText.getText());
					ddc.setSavePath();
					ddc.setCount(descriptionTable.getItemCount());
					String[] attributeArray = new String[ddc.getCount()];
					String[] typeArray = new String[ddc.getCount()];
					for (int i = 0; i < ddc.getCount(); i++) {
						attributeArray[i] = descriptionTable.getItem(i).getText(1);
						typeArray[i] = descriptionTable.getItem(i).getText(2);
					}
					ddc.generate(attributeArray, typeArray, dfs);
				}
				}		
		});
	}

	

}
