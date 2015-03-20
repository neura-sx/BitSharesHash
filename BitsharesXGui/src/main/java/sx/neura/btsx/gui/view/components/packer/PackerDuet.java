package sx.neura.btsx.gui.view.components.packer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

public class PackerDuet extends Packer {
	
	public interface Callback {
		public void showIdentity(String name, String identity);
	}
	
	@FXML
	private Label textUI;
	
	@FXML
	private Hyperlink identityUI;

	public String getText() {
		return textUI.getText();
	}
	public void setText(String text) {
		textUI.setText(text);
	}
	
	private String identity;
	private Callback callback;
	
	public void setIdentity(String identity) {
		this.identity = identity;
		identityUI.setText(identity);
	}
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
//	@Override
//	public void initialize(URL location, ResourceBundle resources) {
//		super.initialize(location, resources);
//		showIdentityUI.visibleProperty().bind(identityUI.textProperty().isNotEqualTo(""));
//	}
	

	@FXML
	private void onShowIdentity(ActionEvent event) {
		callback.showIdentity(textUI.getText(), identity);
	}
}
