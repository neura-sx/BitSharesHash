package sx.neura.btsx.gui.view.popups;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.btsx.gui.view.Popup;

public class FeatureNotImplemented extends Popup {
	
	@FXML
	private Label messageUI;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		setTitle(t("AAXA.B.f8bd_4729_a791"));
		messageUI.setText(t("AAXA.B.6bac_4691_84bb"));
	}
}
