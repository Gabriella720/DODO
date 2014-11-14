package toolBox.gui.datasets;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.widgets.Text;

public class TableItemControls {
			Text name;
			CCombo type;
			TableEditor tableeditor1;
			TableEditor tableeditor2;

			public TableItemControls(Text name, CCombo type,
					TableEditor tableeditor1, TableEditor tableeditor2) {
				this.name = name;
				this.type = type;
				this.tableeditor1 = tableeditor1;
				this.tableeditor2 = tableeditor2;
			}

			public void dispose() {
				name.dispose();
				type.dispose();
				tableeditor1.dispose();
				tableeditor2.dispose();
			}

}
