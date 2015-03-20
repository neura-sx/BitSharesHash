package sx.neura.bts.gui.view.components.display;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;


public class DisplayOutput extends Display {
	
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
		textUI.setAlignment(pos);
	}
	public Pos getAlignment() {
		return textUI.getAlignment();
	}
	
	public StringProperty textProperty() {
		return textUI.textProperty();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}
	
}
