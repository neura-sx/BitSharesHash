package sx.neura.bts.gui.view.popups;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.bts.gui.view.Popup;
import sx.neura.bts.json.exceptions.BTSException;

public class SystemException extends Popup {
	
	@FXML
	private Label commandUI;
	@FXML
	private Label messageUI;
	
	private BTSException exception;
	
	public SystemException(String message) {
		this.exception = new BTSException(message);
	}
	
	public SystemException(BTSException exception) {
		this.exception = exception;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		commandUI.setText(exception.getError().getCommand());
		messageUI.setText(exception.getError().getMessage().replace("\n", ""));
	}
	
}
