package sx.neura.bts.gui.view.pages.bts;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.bts.util.Util;

public abstract class PageDetails_Bts<T> extends Page_Bts {
	
	private static final int TITLE_CROP = 25;
	
	@FXML
	protected Label titleUI;
	
	protected T item;
	
	public PageDetails_Bts(T item) {
		this.item = item;
	}
	protected PageDetails_Bts() {
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (titleUI != null)
			titleUI.setText(Util.crop(getTitle(), TITLE_CROP));
	}
	
}
