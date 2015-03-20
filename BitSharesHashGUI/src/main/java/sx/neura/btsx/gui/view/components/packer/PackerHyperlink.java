package sx.neura.btsx.gui.view.components.packer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PackerHyperlink extends Packer {
	
	public interface Callback {
		public void onAction();
	}
	
	@FXML
	private Label textUI;
	
	@FXML
	private ImageView avatarUI;
	
	public String getText() {
		return textUI.getText();
	}
	public void setText(String text) {
		textUI.setText(text);
	}
	
	private boolean isPassive;
	private Callback callback;
	private Image image;
	
	public void setIsPassive(boolean isPassive) {
		this.isPassive = isPassive;
		if (textUI != null) {
			textUI.setCursor(isPassive ? Cursor.DEFAULT : Cursor.HAND);
			textUI.setUnderline(!isPassive);
		}
	}
	
	public void setCallback(Callback callback) {
		this.callback = callback;
		if (textUI != null) {
			textUI.setCursor(Cursor.HAND);
			textUI.setUnderline(true);
		}
	}
	
	public void setImage(Image image) {
		this.image = image;
		if (avatarUI != null)
			avatarUI.setImage(image);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (callback != null && !isPassive) {
			textUI.setCursor(Cursor.HAND);
			textUI.setUnderline(true);
		}
		if (image != null)
			avatarUI.setImage(image);
	}
	
	@FXML
	private void onAction() {
		callback.onAction();
	}
}
