package sx.neura.btsx.gui.view.popups;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.btsx.gui.view.Popup;

public class RunProcess extends Popup {
	
	public interface Callback {
		public void run();
	}
	
	@FXML
	private Label messageUI;
	
	private Callback callback;
	private String message;
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		setTitle(t("AAXA.B.37cb_49ea_9734"));
		messageUI.setText(message);
	}
	
	@Override
	protected void onCreationComplete() {
		super.onCreationComplete();
		callback.run();
	}
	
	public void hide() {
		hideItself();
	}
}
