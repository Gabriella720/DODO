package toolBox.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.*;
import toolBox.core.utility.UtilityParameters;

public class LogWindow extends Shell {

	final static Display display = Display.getDefault();

	Text logTextArea = null;

	public LogWindow() {
		super(display, SWT.CLOSE | SWT.MIN);
		paintTheBoard();

		addShellListener(new ShellListener()
		{

			@Override
			public void shellActivated(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void shellClosed(ShellEvent arg0) {
				  arg0.doit = false;
				  setVisible(false);
			}

			@Override
			public void shellDeactivated(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void shellDeiconified(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void shellIconified(ShellEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	protected void paintTheBoard() {

		setSize(UtilityParameters.SUB_WINDOW_WIDTH,
				UtilityParameters.SUB_WINDOW_HEIGHT);
		setText(Messages.getString("LOG_WINDOW_NAME"));
		// ---------------------------------------------------------
		Group group = new Group(this, SWT.None);
		group.setText(Messages.getString("LOG_WINDOW_GRUOP_PANEL_NAME"));
		group.setBounds(10, 10, 380, 200);
		
		logTextArea = new Text(group, SWT.V_SCROLL | SWT.H_SCROLL);
		logTextArea.setBounds(5, 20, 365, 170);
		logTextArea.setEditable(false);

		group = new Group(this, SWT.NONE);
		group.setBounds(10, 220, 380, 35);

		Button button = new Button(group, SWT.NONE);
		button.setText(Messages.getString("LOG_WINDOW_CLEAR_BUTTON_NAME"));
		button.setBounds(20, 2, 100, 30);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				logTextArea.setText("");
			}
		});

		button = new Button(group, SWT.NONE);
		button.setText(Messages.getString("LOG_WINDOW_CLOSE_BUTTON_NAME"));
		button.setBounds(250, 2, 100, 30);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setVisible(false);
			}
		});
		// -----------------------------------------------------------
		setVisible(false);
		// shell.layout();
		// shell.open();
		// while(!shell.isDisposed())
		// if(!display.readAndDispatch())
		// display.sleep();
	}

	@Override
	protected void checkSubclass() {

	}

	public static void main(String[] args) {
	}

	public Text getTextArea() {
		return logTextArea;
	}
}
