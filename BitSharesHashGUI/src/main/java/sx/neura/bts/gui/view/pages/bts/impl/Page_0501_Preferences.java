package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.Page;
import sx.neura.bts.gui.view.components.display.DisplayToggleList;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;

public class Page_0501_Preferences extends Page_Bts {
	
	private static Page_0501_Preferences instance;

	public static Page_0501_Preferences getInstance() {
		if (!isInstance())
			instance = new Page_0501_Preferences();
		return instance;
	}

	public static boolean isInstance() {
		return (instance != null);
	}

	private Page_0501_Preferences() {
	}
	
	@Override
	protected Page getHomePage() {
		return Page_0500_Settings.getInstance();
	}
	
	@FXML
	private DisplayToggleList<ColorSet> colorSetUI;
//	
//	@FXML
//	private DisplayInput logoutTimeoutUI;
//	@FXML
//	private DisplayInput transactionFeeUI;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		colorSetUI.setItems(Arrays.asList(ColorSet.values()), (ColorSet item) -> {
			return item.label;
		});
		colorSetUI.selectToggle(getColorSetIndex(Model.getInstance().getColorSetName()));
		colorSetUI.setCallback((ColorSet colorSet) -> {
			Model.getInstance().setColorSetName(colorSet.name);
		});
		
//		logoutTimeoutUI.setText(String.format("%d", Model.WALLET_UNLOCK_TIMEOUT));
		
//		Asset bts = getAssetById(0);
//		transactionFeeUI.setText(getAmount(bts, getTransactionFee(bts)));
	}	
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			
		}
		super.loadData();
	}

	private enum ColorSet {
		S01("Set01", "violet hill"),
		S02("Set02", "red district"),
		S03("Set03", "juicy green"),
		S04("Set04", "ocean blue"),
		S05("Set05", "purple rain");
		private ColorSet(String name, String label) {
			this.name = name;
			this.label = label;
		}
		private String name;
		private String label;
	}
	
	private int getColorSetIndex(String name) {
		for (int i = 0; i < ColorSet.values().length; i++) {
			if (ColorSet.values()[i].name.equals(name))
				return i;
		}
		return -1;
	}
}
