package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import sx.neura.bts.gui.view.Page;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;

public class Page_0502_Wallet extends Page_Bts {
	
	private static Page_0502_Wallet instance;

	public static Page_0502_Wallet getInstance() {
		if (!isInstance())
			instance = new Page_0502_Wallet();
		return instance;
	}

	public static boolean isInstance() {
		return (instance != null);
	}

	private Page_0502_Wallet() {
	}
	
	@Override
	protected Page getHomePage() {
		return Page_0500_Settings.getInstance();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}	
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			
		}
		super.loadData();
	}

}
