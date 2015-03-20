package sx.neura.btsx.gui.view.components.packer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.bts.gui.view.FXMLComponent;

public abstract class Packer extends FXMLComponent {
	
	@FXML
	protected Label labelUI;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public String getLabel() {
		return labelUI.getText();
	}
	public void setLabel(String value) {
		labelUI.setText(value);
	}
}
