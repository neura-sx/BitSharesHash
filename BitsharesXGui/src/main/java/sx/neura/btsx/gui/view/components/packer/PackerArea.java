package sx.neura.btsx.gui.view.components.packer;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class PackerArea extends Packer {
	
	@FXML
	private TextArea textUI;

	public String getText() {
		return textUI.getText();
	}
	
	public void setText(String text) {
		textUI.setText(text);
	}

}
