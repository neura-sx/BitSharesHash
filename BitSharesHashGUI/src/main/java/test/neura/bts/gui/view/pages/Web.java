package test.neura.bts.gui.view.pages;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import sx.neura.btsx.gui.view.Page;

public class Web extends Page {
	
//	private static Web instance;
//	public static Web getInstance() {
//		if (!isInstance())
//			instance = new Web();
//		return instance;
//	}
//	public static boolean isInstance() {
//		return (instance != null);
//	}
//	private Web() {
//	}
	public Web(String url) {
		setUrl(url);
	}
	
	@Override
	public String getName() {
		return url;
	}
	
	@FXML
	private WebView webUI;
	@FXML
	private Button nextLevelUI;
	
	private String url;
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		nextLevelUI.setVisible(pageManager != null);
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded && url != null)
			webUI.getEngine().load("http://en.wikipedia.org/wiki/" + url);
		super.loadData();
	}
	
	@FXML
	private void onNextLevel() {
		Web web = new Web(getNextLevelUrl());
		web.setPageManager(pageManager);
		web.setIndex(index + 1);
		pageManager.addNextLevel(web, index + 1);
	}
	
	private String getNextLevelUrl() {
		if (index == 0)
			return "Gift";
		if (index == 1)
			return "Into_the_Wild_(film)";
		if (index == 2)
			return "Evolutionary_psychology";
		if (index == 3)
			return "Memory";
		if (index == 4)
			return "Consciousness";
		if (index == 5)
			return "Experience";
		return null;
	}
}
