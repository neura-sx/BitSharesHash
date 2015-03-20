package sx.neura.btsx.gui.view.popups;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.btsx.gui.view.Popup;

public class Info extends Popup {
	
	@FXML
	private Label messageUI;
	
	private String message;
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		messageUI.setText(message);
	}
	
	@FXML
	private void onConfirm(ActionEvent event) {
		hideItself();
	}
	
}
