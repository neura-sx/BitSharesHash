package sx.neura.btsx.gui.view.components.packer;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class PackerAmount extends Packer {
	
	public interface Callback {
		public void onAction();
	}
	
	@FXML
	private Label assetUI;
	
	@FXML
	private Label amountUI;

	private Callback callback;
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	public void setAmount(String[] amount) {
		if (amount != null) {
			assetUI.setText(amount[0]);
			amountUI.setText(amount[1]);
		} else {
			assetUI.setText("");
			amountUI.setText("");
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		assetUI.setCursor(Cursor.HAND);
		assetUI.setUnderline(true);
		assetUI.setOnMouseClicked((MouseEvent event) -> {
			callback.onAction();
		});
	}
}
