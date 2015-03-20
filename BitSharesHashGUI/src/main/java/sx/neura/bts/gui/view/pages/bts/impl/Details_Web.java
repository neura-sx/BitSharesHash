package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import sx.neura.bts.gui.view.pages.bts.PageDetails_Bts;
import sx.neura.bts.util.Util;

public class Details_Web extends PageDetails_Bts<String> {
	
	@FXML
	private TextField urlUI;
	@FXML
	private WebView webUI;
	
	public Details_Web(String item) {
		super(item);
	}
	
	@Override
	public String getTitle() {
		return Util.crop(item, 30);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}	
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			urlUI.setText(item);
			webUI.getEngine().load(item);
		}
		super.loadData();
	}
	
}
