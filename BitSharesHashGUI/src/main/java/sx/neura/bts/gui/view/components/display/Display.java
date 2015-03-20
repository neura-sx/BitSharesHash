package sx.neura.bts.gui.view.components.display;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.bts.gui.view.FXMLComponent;

public abstract class Display extends FXMLComponent {
	
	@FXML
	protected Label labelUI;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public String getLabel() {
		return labelUI.getText();
	}
	public void setLabel(String label) {
		labelUI.setText(label);
	}
	
//	public void hideLabel() {
//		labelUI.setOpacity(0.3);
//	}
//	public void showLabel() {
//		labelUI.setOpacity(1.0);
//	}
	
	public double getLabelPrefWidth() {
		return labelUI.getPrefWidth();
	}
	public void setLabelPrefWidth(double prefWidth) {
		labelUI.setPrefWidth(prefWidth);
	}
}
