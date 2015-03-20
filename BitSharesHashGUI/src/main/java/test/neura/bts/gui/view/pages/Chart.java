package test.neura.bts.gui.view.pages;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import sx.neura.btsx.gui.view.Screen;

public class Chart extends Screen {
	
//	private static Chart instance;
//	public static Chart getInstance() {
//		if (!isInstance())
//			instance = new Chart();
//		return instance;
//	}
//	public static boolean isInstance() {
//		return (instance != null);
//	}
//	private Chart() {
//	}
	public Chart (Node node) {
		setNode(node);
	}
	
	@Override
	public String getName() {
		return node.getClass().getSimpleName();
	}
	
	@FXML
	private AnchorPane containerUI;
	
	private Node node;
	
	public void setNode(Node node) {
		this.node = node;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AnchorPane.setLeftAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
		containerUI.getChildren().add(node);
	}

}
