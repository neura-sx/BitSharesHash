package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import sx.neura.bts.gui.dto.BurnRecord;
import sx.neura.bts.gui.view.components.Tile;
import sx.neura.bts.gui.view.components.display.DisplayText;

public class BurnRecordTile extends Tile<BurnRecord> {
	
	@FXML
	private DisplayText messageUI;
	@FXML
	private DisplayText amountUI;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			messageUI.setText(item.getMessage());
			String[] pair = getAmountPair(item.getAmount());
			amountUI.setText(String.format("%s %s", pair[0], pair[1]));
		}
	}

}
