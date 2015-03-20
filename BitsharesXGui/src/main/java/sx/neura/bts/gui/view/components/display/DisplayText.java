package sx.neura.bts.gui.view.components.display;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DisplayText extends Display {
	
	@FXML
	private VBox boxUI;
	@FXML
	private Label textUI;
	
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
		boxUI.setAlignment(pos);
	}
	public Pos getAlignment() {
		return boxUI.getAlignment();
	}
	
	public void setEnabled(boolean enabled) {
		textUI.setDisable(!enabled);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}
	
}
