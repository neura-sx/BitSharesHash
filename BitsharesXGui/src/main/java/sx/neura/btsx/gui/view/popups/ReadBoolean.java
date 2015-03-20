package sx.neura.btsx.gui.view.popups;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sx.neura.btsx.gui.view.Popup;

public class ReadBoolean extends Popup {
	
	public interface Callback {
		public void onConfirm();
		public void onCancel();
	}
	
	@FXML
	private Label messageUI;
	
	@FXML
	private Button confirmUI;
	@FXML
	private Button cancelUI;
	
	private Callback callback;
	private String message;
	private String confirm;
	private String cancel;
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	public void setCancel(String cancel) {
		this.cancel = cancel;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		messageUI.setText(message);
		confirmUI.setText(confirm);
		cancelUI.setText(cancel);
	}
	
	@FXML
	private void onConfirm(ActionEvent event) {
		callback.onConfirm();
		hideItself();
	}
	
	@Override
	protected void onCancel(ActionEvent event) {
		callback.onCancel();
		super.onCancel(event);
	}
	
}
