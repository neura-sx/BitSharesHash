package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.dto.MyOrder;
import sx.neura.bts.gui.view.components.Tile;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.pages.bts.impl.Wizard_MarketCoverShort;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.util.Time;

public class MyOrderCoverTile extends Tile<MyOrder> {
	
	private Asset assetQuote;
	private Asset assetBase;
	
	@FXML
	private DisplayText callPriceUI;
	@FXML
	private DisplayText interestUI;
	@FXML
	private DisplayText owedUI;
	@FXML
	private DisplayText collateralUI;
	@FXML
	private DisplayText expirationUI;
	@FXML
	private DisplayText accountUI;
	@FXML
	private Button coverUI;
	
	public MyOrderCoverTile(Asset assetBase, Asset assetQuote) {
		super();
		this.assetBase = assetBase;
		this.assetQuote = assetQuote;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			callPriceUI.setLabel(String.format("%s (%s/%s)", "Call price", assetQuote.getSymbol(), assetBase.getSymbol()));
			callPriceUI.setText(String.format("%.8f", getRealPrice(item.getOrder().getMarket_index().getOrder_price())));
        	
			interestUI.setLabel(String.format("%s (%s)", "Interest", "%"));
			interestUI.setText(String.format("%.2f", item.getOrder().getInterest_rate().getRatio() * 100));
        	
			owedUI.setLabel(String.format("%s (%s)", "Owed", assetQuote.getSymbol()));
			owedUI.setText(getAmount(assetQuote, item.getOrder().getState().getBalance()));
        	
			collateralUI.setLabel(String.format("%s (%s)", "Collateral", assetBase.getSymbol()));
			collateralUI.setText(getAmount(assetBase, item.getOrder().getCollateral()));
        	
			expirationUI.setLabel(String.format("%s", "Expiration"));
			expirationUI.setText(Time.format(item.getOrder().getExpiration(), Time.Format.DATE_LONG_FORMAT));
			
			accountUI.setText(item.getAccount().getName());
			accountUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Account(item.getAccount()));
				event.consume();
	    	});
			
			coverUI.setOnAction((ActionEvent event) -> coverItself());
		}
	}
	
	private void coverItself() {
		Wizard_MarketCoverShort wizard = new Wizard_MarketCoverShort(item);
		wizard.setAsset(assetQuote);
		wizard.setFromAccount(item.getAccount());
		wizard.setCallback(() -> coverUI.setVisible(false));
		module.jump(wizard);
	}

}
