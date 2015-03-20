package sx.neura.btsx.gui.view.components;

import javafx.scene.Cursor;
import javafx.scene.control.Label;

public class CellHyperlink extends Label {
	public CellHyperlink() {
		setCursor(Cursor.HAND);
		setUnderline(true);
		getStyleClass().add("tableCellRenderer");
	}
}
