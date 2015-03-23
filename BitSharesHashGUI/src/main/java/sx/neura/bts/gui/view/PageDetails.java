package sx.neura.bts.gui.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.bts.util.Util;

public abstract class PageDetails<T> extends Page implements ModelHelper.Host {
	
	private static final int TITLE_CROP = 25;
	
	@FXML
	protected Label titleUI;
	
	protected T item;
	
	public PageDetails(T item) {
		this.item = item;
	}
	protected PageDetails() {
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (titleUI != null)
			titleUI.setText(Util.crop(getTitle(), TITLE_CROP));
	}
	
}
