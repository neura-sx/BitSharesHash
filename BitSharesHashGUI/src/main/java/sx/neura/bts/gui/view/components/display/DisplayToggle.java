package sx.neura.bts.gui.view.components.display;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import sx.neura.bts.gui.view.FXMLComponent;

public abstract class DisplayToggle extends FXMLComponent {
	
	@FXML
	protected ToggleButton toggleButtonUI;
	@FXML
	protected Label textUI;
	
	public void setToggleGroup(ToggleGroup toggleGroup) {
		toggleButtonUI.setToggleGroup(toggleGroup);
	}
	
	public void setText(String text) {
		textUI.setText(text);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}

