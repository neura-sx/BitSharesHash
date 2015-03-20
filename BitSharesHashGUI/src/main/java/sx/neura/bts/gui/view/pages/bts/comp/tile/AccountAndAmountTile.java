package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.dto.AccountAndAmount;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.components.Tile;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.pages.bts.impl.Wizard_MakeTransfer;
import sx.neura.bts.util.Util;

public class AccountAndAmountTile extends Tile<AccountAndAmount> {
	
	@FXML
	private IdenticonCanvas avatarUI;
	@FXML
	private Label nameUI;
	@FXML
	private Label amountUI;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			avatarUI.setName(item.getAccount().getName());
			nameUI.setText(Util.crop(item.getAccount().getName(), 24));
			amountUI.setText(getAmount(item.getAmount()));
			
			setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Account(item.getAccount()));
				event.consume();
	    	});
//			avatarUI.setOnMouseClicked((MouseEvent event) -> {
//				module.jump(new Details_Account(item.getAccount()));
//				event.consume();
//	    	});
//			nameUI.setOnMouseClicked((MouseEvent event) -> { 
//				module.jump(new Details_Account(item.getAccount()));
//				event.consume();
//	    	});
			amountUI.setOnMouseClicked((MouseEvent event) -> { 
				Wizard_MakeTransfer wizard = new Wizard_MakeTransfer();
				wizard.setFromAccount(item.getAccount());
				wizard.setAsset(getAssetById(item.getAmount().getAsset_id()));
				module.jump(wizard);
				event.consume();
	    	});
		}
	}

}
