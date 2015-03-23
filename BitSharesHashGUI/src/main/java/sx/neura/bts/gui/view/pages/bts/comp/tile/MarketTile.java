package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.components.display.DisplayTextLink;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Asset;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Market;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.Market;

public class MarketTile extends Tile_Bts<Market> {
	
	@FXML
	private Label iconUI;
	@FXML
	private DisplayTextLink quoteAssetUI;
	@FXML
	private DisplayTextLink baseAssetUI;
	@FXML
	private DisplayText centerPriceUI;
	@FXML
	private DisplayText currentFeedPriceUI;
	@FXML
	private DisplayText lastValidFeedPriceUI;
	@FXML
	private DisplayText bidDepthUI;
	@FXML
	private DisplayText askDepthUI;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			Asset quoteAsset = Model.getInstance().getAssetById(item.getQuote_id());
			Asset baseAsset = Model.getInstance().getAssetById(item.getBase_id());
			iconUI.setText(Model.getInstance().getMarketLabel(quoteAsset, baseAsset));
			quoteAssetUI.setText(quoteAsset.getName());
			baseAssetUI.setText(baseAsset.getName());
			centerPriceUI.setText(String.format("%.4f", item.getCenter_price().getRatio()));
			currentFeedPriceUI.setText(String.format("%.4f", item.getCurrent_feed_price()));
			lastValidFeedPriceUI.setText(String.format("%.4f", item.getLast_valid_feed_price()));
			bidDepthUI.setText(Model.getInstance().getAmount(0, item.getBid_depth()));
			askDepthUI.setText(Model.getInstance().getAmount(0, item.getAsk_depth()));
			
			setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Market(item));
				event.consume();
	    	});
			quoteAssetUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Asset(quoteAsset));
				event.consume();
	    	});
			baseAssetUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Asset(baseAsset));
				event.consume();
	    	});
		}
	}
}
