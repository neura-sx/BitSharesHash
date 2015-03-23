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
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.popups.ConfirmExecution;
import sx.neura.bts.json.api.wallet.WalletMarketCancelOrder;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Util;

public class MyOrderBidAskTile extends Tile_Bts<MyOrder> {
	
	private Asset assetQuote;
	private Asset assetBase;
	
	@FXML
	private Label processingUI;
	
	@FXML
	private DisplayText idUI;
	@FXML
	private DisplayText priceUI;
	@FXML
	private DisplayText quantityUI;
	@FXML
	private DisplayText totalUI;
	@FXML
	private DisplayText accountUI;
	@FXML
	private Button removeUI;
	
	public MyOrderBidAskTile(Asset assetBase, Asset assetQuote) {
		super();
		this.assetBase = assetBase;
		this.assetQuote = assetQuote;
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
			
			priceUI.setLabel(String.format("%s (%s/%s)", "Price", assetQuote.getSymbol(), assetBase.getSymbol()));
			priceUI.setText(String.format("%.8f", Model.getInstance().getRealPrice(item.getOrder().getMarket_index().getOrder_price())));
			
			quantityUI.setLabel(String.format("%s (%s)", "Quantity", assetBase.getSymbol()));
			if (item.getOrder().getType().equals("bid_order"))
				quantityUI.setText(Model.getInstance().getAmount(assetBase, (long) (item.getOrder().getState().getBalance() / item.getOrder().getMarket_index().getOrder_price().getRatio())));
			else if (item.getOrder().getType().equals("ask_order"))
				quantityUI.setText(Model.getInstance().getAmount(assetBase, item.getOrder().getState().getBalance()));
			
			totalUI.setLabel(String.format("%s (%s)", "Total", assetQuote.getSymbol()));
			if (item.getOrder().getType().equals("bid_order"))
				totalUI.setText(Model.getInstance().getAmount(assetQuote, item.getOrder().getState().getBalance()));
			else if (item.getOrder().getType().equals("ask_order"))
				totalUI.setText(Model.getInstance().getAmount(assetQuote, (long) (item.getOrder().getState().getBalance() * item.getOrder().getMarket_index().getOrder_price().getRatio())));
			
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
		if (item.getOrder().getType().equals("bid_order")) {
			h += String.format("%s\n", "Order Type");
			h += String.format("%s\n", "Account");
			h += String.format("%s\n", "Price");
			h += String.format("%s\n", "Quantity");
			v += String.format("%s\n", "Bid Order");
			v += String.format("%s\n", item.getAccount().getName());
			v += String.format("%s %s/%s\n", String.format("%.8f",
					Model.getInstance().getRealPrice(item.getOrder().getMarket_index().getOrder_price())), assetQuote.getSymbol(), assetBase.getSymbol());
			v += String.format("%s %s\n", assetBase.getSymbol(), Model.getInstance().getAmount(item.getOrder().getMarket_index().getOrder_price().getBase_asset_id(),
					(long) (item.getOrder().getState().getBalance() / item.getOrder().getMarket_index().getOrder_price().getRatio())));
		} else if (item.getOrder().getType().equals("ask_order")) {
			h += String.format("%s\n", "Order Type");
			h += String.format("%s\n", "Account");
			h += String.format("%s\n", "Price");
			h += String.format("%s\n", "Quantity");
			v += String.format("%s\n", "Ask Order");
			v += String.format("%s\n", item.getAccount().getName());
			v += String.format("%s %s/%s\n", String.format("%.8f",
					Model.getInstance().getRealPrice(item.getOrder().getMarket_index().getOrder_price())), assetQuote.getSymbol(), assetBase.getSymbol());
			v += String.format("%s %s\n", assetBase.getSymbol(), Model.getInstance().getAmount(item.getOrder().getMarket_index().getOrder_price().getBase_asset_id(),
					item.getOrder().getState().getBalance()));
		}
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
