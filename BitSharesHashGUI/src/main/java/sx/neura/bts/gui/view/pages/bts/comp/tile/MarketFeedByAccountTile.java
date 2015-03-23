package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.MarketFeed;
import sx.neura.bts.util.Time;

public class MarketFeedByAccountTile extends Tile_Bts<MarketFeed> {
	
	@FXML
	private DisplayText delegateUI;
	@FXML
	private DisplayText lastUpdateUI;
	@FXML
	private DisplayText priceUI;
	@FXML
	private DisplayText medianPriceUI;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			Asset asset = Model.getInstance().getAssetBySymbol(item.getAsset_symbol());
			delegateUI.setText(item.getDelegate_name());
			lastUpdateUI.setText(Time.format(item.getLast_update()));
			priceUI.setText(Model.getInstance().getAmount(asset, item.getPrice()));
			medianPriceUI.setText(Model.getInstance().getAmount(asset, item.getMedian_price()));
			delegateUI.setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Account(item.getDelegate_name()));
				event.consume();
	    	});
		}
	}

}
