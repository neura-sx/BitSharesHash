package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.CumulativeMarketOrder;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.util.Time;

public class MarketOrderCoverTile extends Tile_Bts<CumulativeMarketOrder> {
	
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
	private DisplayText cumulativeUI;
	
	public MarketOrderCoverTile(Asset assetBase, Asset assetQuote) {
		super();
		this.assetBase = assetBase;
		this.assetQuote = assetQuote;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			callPriceUI.setLabel(String.format("%s (%s/%s)", "Call price", assetQuote.getSymbol(), assetBase.getSymbol()));
			callPriceUI.setText(String.format("%.8f", Model.getInstance().getRealPrice(item.getOrder().getMarket_index().getOrder_price())));
        	
			interestUI.setLabel(String.format("%s (%s)", "Interest", "%"));
			interestUI.setText(String.format("%.2f", item.getOrder().getInterest_rate().getRatio() * 100));
        	
			owedUI.setLabel(String.format("%s (%s)", "Owed", assetQuote.getSymbol()));
			owedUI.setText(Model.getInstance().getAmount(assetQuote, item.getOrder().getState().getBalance()));
        	
			collateralUI.setLabel(String.format("%s (%s)", "Collateral", assetBase.getSymbol()));
			collateralUI.setText(Model.getInstance().getAmount(assetBase, item.getOrder().getCollateral()));
        	
			expirationUI.setLabel(String.format("%s", "Expiration"));
			expirationUI.setText(Time.format(item.getOrder().getExpiration(), Time.Format.DATE_LONG_FORMAT));
			
			cumulativeUI.setLabel(String.format("%s (%s)", "Cumulative", assetBase.getSymbol()));
			cumulativeUI.setText(Model.getInstance().getAmount(assetBase, item.getCumulative()));
		}
	}

}
