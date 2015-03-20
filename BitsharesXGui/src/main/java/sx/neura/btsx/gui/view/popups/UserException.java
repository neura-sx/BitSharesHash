package sx.neura.btsx.gui.view.popups;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.btsx.gui.view.Popup;

public class UserException extends Popup {
	
	@FXML
	private Label messageUI;
	
	private String message;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		setTitle(t("AAXA.B.1e18_49d0_b0ed"));
		messageUI.setText(message);
	}
	
	public UserException(BTSUserException exception) {
		this(exception.getError().getMessage());
	}

	public UserException(String message) {
		this.message = message;
	}
}
