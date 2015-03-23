package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.json.api.blockchain.BlockchainMarketOrderHistory;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.util.Time;

public class MarketOrderHistoryTile extends Tile_Bts<BlockchainMarketOrderHistory.Result> {
	
	private Asset assetQuote;
	private Asset assetBase;
	
	@FXML
	private DisplayText typeUI;
	@FXML
	private DisplayText priceUI;
	@FXML
	private DisplayText paidUI;
	@FXML
	private DisplayText receivedUI;
	@FXML
	private DisplayText timeUI;
	
	public MarketOrderHistoryTile(Asset assetBase, Asset assetQuote) {
		super();
		this.assetBase = assetBase;
		this.assetQuote = assetQuote;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			typeUI.setLabel("Type");
			typeUI.setText(Model.getInstance().getOrderTypeLabel(item.getBid_type(), item.getAsk_type()));
        	
			priceUI.setLabel(String.format("%s (%s/%s)", "Price", assetQuote.getSymbol(), assetBase.getSymbol()));
			priceUI.setText(String.format("%.8f", Model.getInstance().getRealPrice(item.getBid_price())));
        	
			paidUI.setLabel(String.format("%s (%s)", "Paid", assetBase.getSymbol()));
			paidUI.setText(Model.getInstance().getAmountPair(item.getAsk_paid())[1]);
        	
			receivedUI.setLabel(String.format("%s (%s)", "Received", assetQuote.getSymbol()));
			receivedUI.setText(Model.getInstance().getAmountPair(item.getAsk_received())[1]);
        	
			timeUI.setLabel("Time");
			timeUI.setText(Time.format(item.getTimestamp()));
		}
	}

}
