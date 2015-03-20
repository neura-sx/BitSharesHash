package sx.neura.bts.gui.view.components.display;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class DisplayDuet extends Display {
	
	@FXML
	private HBox boxUI;
	@FXML
	private Label text01UI;
	@FXML
	private Label text02UI;
	
	public Node getText01UI() {
		return text01UI;
	}
	public Node getText02UI() {
		return text02UI;
	}
	
	public void setText(String[] text) {
		if (text != null) {
			text01UI.setText(text[0]);
			text02UI.setText(text[1]);
		} else {
			text01UI.setText("");
			text02UI.setText("");
		}
	}
	
	public String getText01() {
		return text01UI.getText();
	}
	public void setText01(String text) {
		text01UI.setText(text);
	}
	
	public String getText02() {
		return text02UI.getText();
	}
	public void setText02(String text) {
		text02UI.setText(text);
	}
	
	public void setAlignment(Pos pos) {
		boxUI.setAlignment(pos);
	}
	public Pos getAlignment() {
		return boxUI.getAlignment();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}
	
}
