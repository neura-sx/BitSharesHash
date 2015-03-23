package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Block;
import sx.neura.bts.json.dto.Block;
import sx.neura.bts.util.Time;

public class BlockTile extends Tile_Bts<Block> {
	
	@FXML
	private DisplayText blockNumberUI;
	@FXML
	private DisplayText timestampUI;
	
	@FXML
	private DisplayText numberOfTransactionsUI;
	@FXML
	private DisplayText delegateUI;
	
	@FXML
	private DisplayText latencyUI;
	@FXML
	private DisplayText sizeUI;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			blockNumberUI.setText(item.getBlock_num());
			timestampUI.setText(Time.format(item.getTimestamp()));
			
			numberOfTransactionsUI.setText(new Integer(item.getUser_transaction_ids().size()).toString());
			String delegate = h.getBlockSignee(item.getBlock_num());
			delegateUI.setText(delegate);
			
			latencyUI.setText(String.format("%d%s", item.getLatency() / 1000000, "s"));
			sizeUI.setText(new Integer(item.getBlock_size()).toString());
			
			blockNumberUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Block(item));
				event.consume();
	    	});
			
			delegateUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Account(delegate));
				event.consume();
	    	});
			
			setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Block(item));
				event.consume();
	    	});
		}
	}
}
