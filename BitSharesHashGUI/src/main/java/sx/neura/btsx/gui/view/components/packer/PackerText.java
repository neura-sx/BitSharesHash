package sx.neura.btsx.gui.view.components.packer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class PackerText extends Packer {
	
	@FXML
	private Label textUI;
	private Callback callback;
	
	public interface Callback {
		public void onAction();
	}

	public Node getTextUI() {
		return textUI;
	}
	
	public String getText() {
		return textUI.getText();
	}
	public void setText(String text) {
		textUI.setText(text);
	}
	
	public void setAlignment(Pos pos) {
		textUI.setAlignment(pos);
	}
	public Pos getAlignment() {
		return textUI.getAlignment();
	}
	
	public void setCallback(Callback callback) {
		this.callback = callback;
		applyCallback();
	}
	
	public StringProperty getTextProperty() {
		return textUI.textProperty();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		applyCallback();
	}
	
	private void applyCallback() {
		if (textUI != null && callback != null) {
			textUI.setCursor(Cursor.HAND);
			textUI.setUnderline(true);
			textUI.setOnMouseClicked((MouseEvent event) -> {
				callback.onAction();
			});
		}
	}
}
