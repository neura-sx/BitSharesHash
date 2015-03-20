package sx.neura.bts.gui.view.popups;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sx.neura.bts.gui.view.Popup;

public class ConfirmExecution extends Popup {
	
	@FXML
	private Label textUI;
	@FXML
	private Label confirmationHeadersUI;
	@FXML
	private Label confirmationValuesUI;
	
	@FXML
	private Button confirmUI;
	@FXML
	private Button cancelUI;
	
	private Callback callback;
	private String text;
	private String confirmationHeaders;
	private String confirmationValues;
	private String confirm;
	private String cancel;
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public void setText(String text) {
		this.text = text;
	}
	public void setConfimationHeaders(String confirmationHeaders) {
		this.confirmationHeaders = confirmationHeaders;
	}
	public void setConfimationValues(String confirmationValues) {
		this.confirmationValues = confirmationValues;
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
		textUI.setText(text);
		confirmationHeadersUI.setText(confirmationHeaders);
		confirmationValuesUI.setText(confirmationValues);
		confirmUI.setText(confirm);
		cancelUI.setText(cancel);
	}
	
	@FXML
	private void onConfirm() {
		hideItself(() -> callback.onConfirm());
	}
	
	@Override
	protected void onCancel() {
		hideItself(() -> callback.onCancel());
	}
	
}
