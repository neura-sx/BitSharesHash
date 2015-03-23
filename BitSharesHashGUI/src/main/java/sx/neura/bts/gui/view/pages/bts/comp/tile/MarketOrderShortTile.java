package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.CumulativeMarketOrder;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.json.dto.Asset;

public class MarketOrderShortTile extends Tile_Bts<CumulativeMarketOrder> {
	
	private Asset assetQuote;
	private Asset assetBase;
	private double feedPrice;
	private double feedRatio;
	
	@FXML
	private DisplayText limitUI;
	@FXML
	private DisplayText interestUI;
	@FXML
	private DisplayText collateralUI;
	@FXML
	private DisplayText totalUI;
	@FXML
	private DisplayText cumulativeUI;
	
	public MarketOrderShortTile(Asset assetBase, Asset assetQuote, double feedPrice) {
		super();
		this.assetBase = assetBase;
		this.assetQuote = assetQuote;
		this.feedPrice = feedPrice;
		this.feedRatio = feedPrice * ((double) assetQuote.getPrecision() / assetBase.getPrecision());
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			limitUI.setLabel(String.format("%s (%s/%s)", "Limit", assetQuote.getSymbol(), assetBase.getSymbol()));
			limitUI.setText(String.format("%.8f", (item.getOrder().getState().getLimit_price() != null ? Model.getInstance().getRealPrice(item.getOrder().getState().getLimit_price()) : feedPrice)));
			
			interestUI.setLabel(String.format("%s (%s)", "Interest", "%"));
			interestUI.setText(String.format("%.2f", item.getOrder().getMarket_index().getOrder_price().getRatio() * 100));
			
			collateralUI.setLabel(String.format("%s (%s)", "Collateral", assetBase.getSymbol()));
			collateralUI.setText(Model.getInstance().getAmount(assetBase, item.getOrder().getState().getBalance()));
			
			totalUI.setLabel(String.format("%s (%s)", "Total", assetQuote.getSymbol()));
			totalUI.setText(Model.getInstance().getAmount(assetQuote, (long) (item.getOrder().getState().getBalance() * feedRatio * Model.SHORT_COLLATERAL_FACTOR)));		
			
			cumulativeUI.setLabel(String.format("%s (%s)", "Cumulative", assetBase.getSymbol()));
			cumulativeUI.setText(Model.getInstance().getAmount(assetBase, item.getCumulative()));
		}
	}

}
