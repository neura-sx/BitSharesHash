package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.dto.AmountAndAccount;
import sx.neura.bts.gui.view.components.Tile;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Asset;
import sx.neura.bts.gui.view.pages.bts.impl.Wizard_MakeTransfer;
import sx.neura.bts.json.dto.Asset;

public class AmountAndAccountTile extends Tile<AmountAndAccount> {
	
	@FXML
	private Label iconUI;
	@FXML
	private Label amountUI;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			Asset asset = getAssetById(item.getAmount().getAsset_id());
			iconUI.setText(asset.getSymbol());
			amountUI.setText(getAmount(item.getAmount()));
			
			setOnMouseClicked((MouseEvent event) -> { 
				module.jump(new Details_Asset(asset));
				event.consume();
	    	});
//			iconUI.setOnMouseClicked((MouseEvent event) -> { 
//				module.jump(new Details_Asset(asset));
//				event.consume();
//	    	});
			amountUI.setOnMouseClicked((MouseEvent event) -> { 
				Wizard_MakeTransfer wizard = new Wizard_MakeTransfer();
				wizard.setFromAccount(item.getAccount());
				wizard.setAsset(asset);
				module.jump(wizard);
				event.consume();
	    	});
		}
	}

}
