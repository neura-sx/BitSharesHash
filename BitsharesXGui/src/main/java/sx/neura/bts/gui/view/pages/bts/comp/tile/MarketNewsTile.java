package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.dto.MarketNews;
import sx.neura.bts.gui.view.components.Tile;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Market;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Web;
import sx.neura.bts.util.Time;

public class MarketNewsTile extends Tile<MarketNews> {
	
	@FXML
	private DisplayText timestampUI;
	@FXML
	private DisplayText marketUI;
	@FXML
	private DisplayText textUI;
	@FXML
	private DisplayText linkUI;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			timestampUI.setText(Time.format(item.getTimestamp(), Time.Format.DATE_AND_TIME_MEDIUM_FORMAT));
			marketUI.setText(getMarketLabel(item.getMarket()));
			textUI.setText(item.getText());
			linkUI.setText(item.getLink());
			marketUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Market(item.getMarket()));
				event.consume();
	    	});
			if (item.getLink() != null && !item.getLink().isEmpty()) {
				linkUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
					module.jump(new Details_Web(item.getLink()));
					event.consume();
		    	});
			}
		}
	}

}
