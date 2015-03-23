package sx.neura.bts.gui.view;

import java.net.URL;
import java.util.ResourceBundle;

import sx.neura.bts.json.exceptions.BTSSystemException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public abstract class Tile<T> extends AnchorPane implements Initializable {
	
	@FXML
	protected Node boxUI;
	
	protected T item;
	protected Module module;
	protected Region region;
	
	public void setItem(T item) {
		this.item = item;
	}
	public void setModule(Module module) {
		this.module = module;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (item != null && boxUI != null)
			boxUI.setVisible(true);
	}
	
	protected Pane getModalPane() {
		return module.getPane();
	}
	
	public void systemException(BTSSystemException e) {
		module.systemException(e);
	}
	
}
