package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.MyOrder;
import sx.neura.bts.gui.view.components.Tile;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.popups.ConfirmExecution;
import sx.neura.bts.json.api.wallet.WalletMarketCancelOrder;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Util;

public class MyOrderShortTile extends Tile<MyOrder> {
	
	private Asset assetQuote;
	private Asset assetBase;
	private double feedRatio;
	
	@FXML
	private Label processingUI;
	
	@FXML
	private DisplayText idUI;
	@FXML
	private DisplayText collateralUI;
	@FXML
	private DisplayText interestUI;
	@FXML
	private DisplayText quantityUI;
	@FXML
	private DisplayText limitUI;
	@FXML
	private DisplayText accountUI;
	@FXML
	private Button removeUI;
	
	public MyOrderShortTile(Asset assetBase, Asset assetQuote, double feedPrice) {
		super();
		this.assetBase = assetBase;
		this.assetQuote = assetQuote;
		this.feedRatio = feedPrice * ((double) assetQuote.getPrecision() / assetBase.getPrecision());
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			
			if (item.getId() == null) {
				processingUI.setText(String.format("Processing order placed by '%s'..", item.getAccount().getName()));
				processingUI.setVisible(true);
				return;
			}
			
			idUI.setText(Util.crop(item.getId(), 20));
			
			collateralUI.setLabel(String.format("%s (%s)", "Collateral", assetBase.getSymbol()));
			collateralUI.setText(getAmount(assetBase, item.getOrder().getState().getBalance()));
			
			interestUI.setLabel(String.format("%s (%s)", "Interest", "%"));
			interestUI.setText(String.format("%.2f", item.getOrder().getMarket_index().getOrder_price().getRatio() * 100));
			
			quantityUI.setLabel(String.format("%s (%s)", "Quantity", assetQuote.getSymbol()));
			quantityUI.setText(getAmount(assetQuote, (long) (item.getOrder().getState().getBalance() * feedRatio * Model.SHORT_COLLATERAL_FACTOR)));
			
			limitUI.setLabel(String.format("%s (%s/%s)", "Price Limit", assetQuote.getSymbol(), assetBase.getSymbol()));
			limitUI.setText(item.getOrder().getState().getLimit_price() != null ? String.format("%.8f", getRealPrice(item.getOrder().getState().getLimit_price())) : "None");
			
			accountUI.setText(item.getAccount().getName());
			accountUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Account(item.getAccount()));
				event.consume();
	    	});
			
			removeUI.setOnAction((ActionEvent event) -> removeItself());
		}
	}

	private void removeItself() {
		String h = "";
		String v = "";
		h += String.format("%s\n", "Order Type");
		h += String.format("%s\n", "Account");
		h += String.format("%s\n", "Collateral"); 
		h += String.format("%s\n", "Quantity");
		h += String.format("%s\n", "Interest");
		v += String.format("%s\n", "Short Order");
		v += String.format("%s\n", item.getAccount().getName());
		v += String.format("%s %s\n", assetBase.getSymbol(), getAmount(assetBase, item.getOrder().getState().getBalance()));     	
		v += String.format("%s %s\n", assetQuote.getSymbol(), getAmount(assetQuote,
				(long) (item.getOrder().getState().getBalance() * item.getOrder().getMarket_index().getOrder_price().getRatio() * Model.SHORT_COLLATERAL_FACTOR)));
		v += String.format("%.2f %s\n", item.getOrder().getMarket_index().getOrder_price().getRatio() * 100, "%");
		
		ConfirmExecution confirmExecution = new ConfirmExecution();
		confirmExecution.setText("Are you sure you want to remove this order?");
		confirmExecution.setConfimationHeaders(h);
		confirmExecution.setConfimationValues(v);
		confirmExecution.setConfirm("Yes");
		confirmExecution.setCancel("Cancel");
		confirmExecution.setCallback(new ConfirmExecution.Callback() {
			@Override
			public void onConfirm() {
				try {
					removeUI.setVisible(false);
					WalletMarketCancelOrder.run(item.getId());
				} catch (BTSSystemException e) {
					removeUI.setVisible(true);
					module.systemException(e);
				}
			}
			@Override
			public void onCancel() {
				removeUI.setVisible(true);
			}
		});
		confirmExecution.show(getModalPane(), module);
	}
}
