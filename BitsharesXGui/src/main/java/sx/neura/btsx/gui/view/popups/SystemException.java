package sx.neura.btsx.gui.view.popups;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.bts.json.exceptions.BTSException;
import sx.neura.btsx.gui.view.Popup;

public class SystemException extends Popup {
	
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
		setTitle(t("AAXA.B.35c8_48df_bc61"));
		messageUI.setText(exception.getError().getMessage());
	}
}
