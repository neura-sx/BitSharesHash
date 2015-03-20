package sx.neura.bts.gui.view.popups;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.bts.gui.view.Popup;

public class FeatureNotImplemented extends Popup {
	
	@FXML
	private Label messageUI;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		messageUI.setText("This feature is not implemented yet");
	}
	
}
