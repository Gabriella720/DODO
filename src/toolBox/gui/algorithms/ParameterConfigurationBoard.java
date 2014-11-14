package toolBox.gui.algorithms;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.*;

public class ParameterConfigurationBoard {

	protected Shell shlParameterConfiguration;
	private Text nameText;
	private Text valueText;
	private Text descriptionText;
	private Text numberText;
	Combo typeCombo;
	/**
	 * Open the window.
	 */
	public void open(TableItem item) {
		Display display = Display.getDefault();
		createContents(item);
		shlParameterConfiguration.open();
		shlParameterConfiguration.layout();
		while (!shlParameterConfiguration.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(final TableItem item) {
		shlParameterConfiguration = new Shell();
		shlParameterConfiguration.setSize(436, 451);
		shlParameterConfiguration.setText("Parameter Configuration");
		
		Label nameLabel = new Label(shlParameterConfiguration, SWT.NONE);
		nameLabel.setBounds(70, 82, 70, 18);
		nameLabel.setText("Name");
		
		nameText = new Text(shlParameterConfiguration, SWT.BORDER);
		nameText.setBounds(201, 82, 190, 27);
		
		Label typeLabel = new Label(shlParameterConfiguration, SWT.NONE);
		typeLabel.setBounds(70, 149, 70, 18);
		typeLabel.setText("Type");
		
		typeCombo = new Combo(shlParameterConfiguration, SWT.READ_ONLY);
		typeCombo.setBounds(201, 149, 190, 30);
		typeCombo.add("int");
		typeCombo.add("double");
		typeCombo.add("String");
		
		Label valueLabel = new Label(shlParameterConfiguration, SWT.NONE);
		valueLabel.setBounds(70, 217, 70, 18);
		valueLabel.setText("Value");
		
		valueText = new Text(shlParameterConfiguration, SWT.BORDER);
		valueText.setBounds(201, 217, 190, 27);
		
		Label descriptionLabel = new Label(shlParameterConfiguration, SWT.NONE);
		descriptionLabel.setBounds(70, 280, 78, 18);
		descriptionLabel.setText("Description");
		
		descriptionText = new Text(shlParameterConfiguration, SWT.MULTI|SWT.BORDER);
		descriptionText.setBounds(201, 280, 190, 60);
		
		Button OKButton = new Button(shlParameterConfiguration, SWT.NONE);
		OKButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				item.setText(new String[]{numberText.getText(),nameText.getText(),typeCombo.getText(),valueText.getText(),descriptionText.getText()});
				shlParameterConfiguration.close();
			}
		});
		OKButton.setBounds(100, 353, 91, 29);
		OKButton.setText("OK");
		
		Button cancelButton = new Button(shlParameterConfiguration, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlParameterConfiguration.close();
			}
		});
		cancelButton.setBounds(247, 353, 91, 29);
		cancelButton.setText("Cancel");
		
		Label numberButton = new Label(shlParameterConfiguration, SWT.NONE);
		numberButton.setText("No.");
		numberButton.setBounds(70, 22, 70, 18);
		
		numberText = new Text(shlParameterConfiguration, SWT.BORDER);
		numberText.setBounds(201, 22, 190, 27);

	}

}
