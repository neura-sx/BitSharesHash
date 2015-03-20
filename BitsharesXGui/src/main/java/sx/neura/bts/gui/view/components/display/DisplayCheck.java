package sx.neura.bts.gui.view.components.display;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import sx.neura.bts.gui.view.FXMLComponent;

public abstract class DisplayCheck extends FXMLComponent {
	
	@FXML
	protected ToggleButton toggleButtonUI;
	@FXML
	protected Label textUI;
	
	public void setRunnable(Runnable runnable) {
		toggleButtonUI.setOnAction((ActionEvent) -> {
			runnable.run();
		});
	}
	
	public void setText(String text) {
		textUI.setText(text);
	}
	
	public void setSelected(boolean selected) {
		toggleButtonUI.setSelected(selected);
	}
	public boolean isSelected() {
		return toggleButtonUI.isSelected();
	}
	
	public ToggleButton getToggleButtonUI() {
		return toggleButtonUI;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
}

