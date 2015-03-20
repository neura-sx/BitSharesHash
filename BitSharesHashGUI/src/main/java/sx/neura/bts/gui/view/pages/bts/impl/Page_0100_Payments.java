package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Toggle;
import sx.neura.bts.gui.view.Page;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.MenuPane;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;

public class Page_0100_Payments extends Page_Bts {
	
	private static Page_0100_Payments instance;

	public static Page_0100_Payments getInstance() {
		if (!isInstance())
			instance = new Page_0100_Payments();
		return instance;
	}

	public static boolean isInstance() {
		return (instance != null);
	}

	private Page_0100_Payments() {
	}
	
	
	@FXML
	private MenuPane panoramaMenuUI;
	@FXML
	private LayerPane panoramaUI;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		for (Page page : peers) {
			panoramaUI.getChildren().add(loadScreen(page));
			page.loadUI();
			page.loadData();
		}
	}	
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			
		}
		super.loadData();
	}
	
	@Override
	public Toggle getToggle() {
		return getPeer().getToggle();
	}
	
	@Override
	public Page getPeer() {
		return peers.get(panoramaUI.getIndex());
	}
	
	@Override
	public void setPeer(Page peer, Runnable runnable) {
		int index = peers.indexOf(peer);
		panoramaMenuUI.setIndex(index);
		panoramaUI.setIndex(index, () -> {
			peer.makeSnapshot();
			runnable.run();
		});
	}
	
	@FXML
	private void onPanoramaMenu01() {
		module.togglePage(peers.get(0));
	}
	@FXML
	private void onPanoramaMenu02() {
		module.togglePage(peers.get(1));
	}
	@FXML
	private void onPanoramaMenu03() {
		module.togglePage(peers.get(2));
	}
	
}
