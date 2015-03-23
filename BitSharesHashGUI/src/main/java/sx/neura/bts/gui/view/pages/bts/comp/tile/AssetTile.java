package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Asset;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;

public class AssetTile extends Tile_Bts<Asset> {
	
	@FXML
	private Label iconUI;
	@FXML
	private Label nameUI;
	
	@FXML
	private DisplayText descriptionUI;
	@FXML
	private DisplayText typeUI;
	
	@FXML
	private DisplayText currentShareSupplyUI;
	@FXML
	private DisplayText maximumShareSupplyUI;
	
	@FXML
	private DisplayText collectedFeesUI;
	@FXML
	private DisplayText issuerUI;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			iconUI.setText(item.getSymbol());
			nameUI.setText(item.getName());
			if (Model.getInstance().isMarketPeggedAsset(item)) {
				issuerUI.setText("SYSTEM");
				issuerUI.setEnabled(false);
			} else {
				Account issuer = h.getAccount(item.getIssuer_account_id());
				if (issuer != null) {
					issuerUI.setText(issuer.getName());
					issuerUI.setEnabled(true);
					issuerUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
						module.jump(new Details_Account(issuer));
						event.consume();
					});
				} else {
					issuerUI.setText("n/a");
					issuerUI.setEnabled(false);
				}
			}
			collectedFeesUI.setText(Model.getInstance().getAmount(item, item.getCollected_fees()));
			nameUI.setText(item.getName());
			descriptionUI.setText(item.getDescription());
			typeUI.setText(Model.getInstance().isMarketPeggedAsset(item) ? "Market-Pegged Asset" : "User-Issued Asset");
			currentShareSupplyUI.setText(Model.getInstance().getAmount(item, item.getCurrent_share_supply()));
			maximumShareSupplyUI.setText(Model.getInstance().getAmount(item, item.getMaximum_share_supply()));
			
			setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Asset(item));
				event.consume();
	    	});
		}
	}
}

