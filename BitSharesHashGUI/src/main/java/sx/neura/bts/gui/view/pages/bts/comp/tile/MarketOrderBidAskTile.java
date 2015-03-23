package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.CumulativeMarketOrder;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.json.dto.Asset;

public class MarketOrderBidAskTile extends Tile_Bts<CumulativeMarketOrder> {
	
	private Asset assetQuote;
	private Asset assetBase;
	
	@FXML
	private DisplayText priceUI;
	@FXML
	private DisplayText quantityUI;
	@FXML
	private DisplayText totalUI;
	@FXML
	private DisplayText cumulativeUI;
	
	public MarketOrderBidAskTile(Asset assetBase, Asset assetQuote) {
		super();
		this.assetBase = assetBase;
		this.assetQuote = assetQuote;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			
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
			
			cumulativeUI.setLabel(String.format("%s (%s)", "Cumulative", assetBase.getSymbol()));
			if (item.getOrder().getType().equals("bid_order"))
				cumulativeUI.setText(Model.getInstance().getAmount(assetBase, (long) (item.getCumulative() / item.getOrder().getMarket_index().getOrder_price().getRatio())));
			else if (item.getOrder().getType().equals("ask_order"))
				cumulativeUI.setText(Model.getInstance().getAmount(assetBase, item.getCumulative()));
		}
	}

}
