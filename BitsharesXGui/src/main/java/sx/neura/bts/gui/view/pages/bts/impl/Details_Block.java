package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.PageDetails_Bts;
import sx.neura.bts.json.dto.Block;
import sx.neura.bts.util.Time;

public class Details_Block extends PageDetails_Bts<Block> {
	
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
	
	
	public Details_Block(Block item) {
		super(item);
	}
	public Details_Block(String blockNumber) {
		super();
		this.item = getBlock(blockNumber);
	}
	
	@Override
	public String getTitle() {
		return item.getBlock_num();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
	}	
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			blockNumberUI.setText(item.getBlock_num());
			timestampUI.setText(Time.format(item.getTimestamp()));
			
			numberOfTransactionsUI.setText(new Integer(item.getUser_transaction_ids().size()).toString());
			String delegate = getBlockSignee(item.getBlock_num());
			delegateUI.setText(delegate);
			
			latencyUI.setText(String.format("%d%s", item.getLatency() / 1000000, "s"));
			sizeUI.setText(new Integer(item.getBlock_size()).toString());
			
			delegateUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Account(delegate));
				event.consume();
	    	});
		}
		super.loadData();
	}
	
}
