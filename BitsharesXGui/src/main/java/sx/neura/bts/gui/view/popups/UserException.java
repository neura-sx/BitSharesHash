package sx.neura.bts.gui.view.popups;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.bts.gui.view.Popup;
import sx.neura.bts.json.exceptions.BTSUserException;

public class UserException extends Popup {
	
	@FXML
	private Label messageUI;
	
	private String message;
	
	public UserException(BTSUserException exception) {
		this(exception.getError().getMessage());
	}

	public UserException(String message) {
		this.message = message;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		messageUI.setText(message);
	}
}
