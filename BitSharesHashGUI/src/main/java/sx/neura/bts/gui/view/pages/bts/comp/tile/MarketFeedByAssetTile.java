package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Asset;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.MarketFeed;
import sx.neura.bts.util.Time;

public class MarketFeedByAssetTile extends Tile_Bts<MarketFeed> {
	
	@FXML
	private Label iconUI;
	@FXML
	private Label nameUI;
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
			iconUI.setText(asset.getSymbol());
			nameUI.setText(asset.getName());
			lastUpdateUI.setText(Time.format(item.getLast_update()));
			priceUI.setText(Model.getInstance().getAmount(asset, item.getPrice()));
			medianPriceUI.setText(Model.getInstance().getAmount(asset, item.getMedian_price()));
			iconUI.setOnMouseClicked((MouseEvent event) -> { 
				module.jump(new Details_Asset(asset));
				event.consume();
	    	});
		}
	}

}
