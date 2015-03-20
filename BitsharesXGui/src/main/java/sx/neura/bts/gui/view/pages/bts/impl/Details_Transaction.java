package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.Transaction;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.MenuPane;
import sx.neura.bts.gui.view.components.display.DisplayDuet;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.PageDetails_Bts;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.util.Time;
import sx.neura.bts.util.Util;

public class Details_Transaction extends PageDetails_Bts<Transaction> {
	
	@FXML
	private Label headerUI;
	@FXML
	private IdenticonCanvas avatarFromUI;
	@FXML
	private Label nameFromUI;
	
	@FXML
	private IdenticonCanvas avatarToUI;
	@FXML
	private Label nameToUI;
	
	@FXML
	private Label amountAssetUI;
	@FXML
	private Label amountValueUI;
	
	@FXML
	private MenuPane panoramaMenuUI;
	@FXML
	private LayerPane panoramaUI;
	@FXML
	private ToggleGroup panoramaToggleGroupUI;
	
	@FXML
	private DisplayText transactionNumberUI;
	@FXML
	private DisplayText blockNumberUI;
	
	@FXML
	private DisplayText timestampUI;
	@FXML
	private DisplayText expirationUI;
	
	@FXML
	private DisplayText typeUI;
	@FXML
	private DisplayText directionUI;
	
	@FXML
	private DisplayText accountFromUI;
	@FXML
	private DisplayText accountToUI;
	
	@FXML
	private DisplayDuet amountUI;
	@FXML
	private DisplayDuet feeUI;
	
	@FXML
	private DisplayText memoUI;
	@FXML
	private DisplayText statusUI;
	
	
	public Details_Transaction(Transaction item) {
		super(item);
	}
	
	@Override
	public String getTitle() {
		return String.format("%s>>%s", Util.crop(item.getFrom_account(), 12), Util.crop(item.getTo_account(), 12));
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		panoramaUI.setIndex(0);
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(0));
		
		Util.manageToggleGroup(panoramaToggleGroupUI);
	}	
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			if (item.getDirection() != null) {
				if (item.getDirection() > 0)
					headerUI.setText("Inflow Transaction");
				else if (item.getDirection() < 0)
					headerUI.setText("Outflow Transaction");
				else
					headerUI.setText("Internal Transaction");
			} else {
				headerUI.setText("System Transaction");
			}
			
			Asset asset = getAssetById(item.getAmount().getAsset_id());
			amountAssetUI.setText(asset.getSymbol());
			amountValueUI.setText(getAmount(asset, item.getAmount().getValue()));
			amountAssetUI.setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Asset(asset));
				event.consume();
	    	});
			
			transactionNumberUI.setText(item.getTrx_id());
			
			blockNumberUI.setText(item.getBlock_num());
			if (new Integer(item.getBlock_num()) > 0) {
				blockNumberUI.setOnMouseClicked((MouseEvent event) -> {
					module.jump(new Details_Block(item.getBlock_num()));
					event.consume();
		    	});
			} else {
				blockNumberUI.setEnabled(false);
			}
			
			timestampUI.setText(Time.format(item.getTimestamp()));
			expirationUI.setText(Time.format(item.getExpiration_timestamp(), "none"));
			
			String s = "";
			if (item.isIs_virtual())
				s = Util.addAttribute(s, "virtual");
			if (item.isIs_market_cancel())
				s = Util.addAttribute(s, "market cancel");
			else if (item.isIs_market())
				s = Util.addAttribute(s, "market");
			if (s.isEmpty())
				s = Util.addAttribute(s, "transfer");
			typeUI.setText(s);
			
			if (item.getDirection() != null) {
				if (item.getDirection() > 0)
					directionUI.setText("inflow");
				else if (item.getDirection() < 0)
					directionUI.setText("outflow");
				else
					directionUI.setText("internal");
			} else {
				directionUI.setText("system");
			}
			
			accountFromUI.setText(item.getFrom_account());
			if (!Model.getInstance().isVirtualName(item.getFrom_account())) {
				accountFromUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
					module.jump(new Details_Account(item.getFrom_account()));
					event.consume();
		    	});
				avatarFromUI.setVisible(true);
				avatarFromUI.setName(item.getFrom_account());
				avatarFromUI.setOnMouseClicked((MouseEvent event) -> {
					module.jump(new Details_Account(item.getFrom_account()));
					event.consume();
		    	});
			} else {
				accountFromUI.setEnabled(false);
				avatarFromUI.setVisible(false);
			}
			nameFromUI.setText(Util.crop(item.getFrom_account(), 24));
			
			accountToUI.setText(item.getTo_account());
			if (!Model.getInstance().isVirtualName(item.getTo_account())) {
				accountToUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
					module.jump(new Details_Account(item.getTo_account()));
					event.consume();
		    	});
				avatarToUI.setVisible(true);
				avatarToUI.setName(item.getTo_account());
				avatarToUI.setOnMouseClicked((MouseEvent event) -> {
					module.jump(new Details_Account(item.getTo_account()));
					event.consume();
		    	});
			} else {
				accountToUI.setEnabled(false);
				avatarToUI.setVisible(false);
			}
			nameToUI.setText(Util.crop(item.getTo_account(), 24));
			
			amountUI.setText(getAmountPair(item.getAmount()));
			amountUI.getText01UI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Asset(item.getAmount().getAsset_id()));
				event.consume();
	    	});
			feeUI.setText(getAmountPair(item.getFee()));
			feeUI.getText01UI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Asset(item.getFee().getAsset_id()));
				event.consume();
	    	});
			
			memoUI.setText(item.getMemo());
			statusUI.setText(item.isIs_confirmed() ? "confirmed" : "pending");
		}
		super.loadData();
	}
	
	@FXML
	private void onPanoramaToggle01() {
		panoramaMenuUI.setIndex(0);
		applyPanoramaIndex(0);
	}
	@FXML
	private void onPanoramaToggle02() {
		panoramaMenuUI.setIndex(1);
		applyPanoramaIndex(1);
	}
	
	@FXML
	private void onPanoramaMenu01() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(0));
		applyPanoramaIndex(0);
	}
	@FXML
	private void onPanoramaMenu02() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(1));
		applyPanoramaIndex(1);
	}
	
	private void applyPanoramaIndex(int index) {
		panoramaUI.setIndex(index);
	}
}
