package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.Transaction;
import sx.neura.bts.gui.view.components.Tile;
import sx.neura.bts.gui.view.components.display.DisplayDuet;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Asset;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Transaction;
import sx.neura.bts.util.Time;
import sx.neura.bts.util.Util;

public class TransactionTile extends Tile<Transaction> {
	
//	@FXML
//	private Pane gridUI;
	
	@FXML
	private DisplayText transactionNumberUI;
	@FXML
	private DisplayText timestampUI;
	
	@FXML
	private DisplayText typeUI;
	@FXML
	private DisplayText directionUI;
	
	@FXML
	private DisplayText accountFromUI;
	@FXML
	private DisplayText accountToUI;
	
	@FXML
	private DisplayDuet amountUI;
	@FXML
	private DisplayDuet feeUI;
	
	@FXML
	private DisplayText memoUI;
	@FXML
	private DisplayText statusUI;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			transactionNumberUI.setText(Util.crop(item.getTrx_id(), 15));
			timestampUI.setText(Time.format(item.getTimestamp()));
			
			String s = "";
			if (item.isIs_virtual())
				s = Util.addAttribute(s, "virtual");
			if (item.isIs_market_cancel())
				s = Util.addAttribute(s, "market cancel");
			else if (item.isIs_market())
				s = Util.addAttribute(s, "market");
			if (s.isEmpty())
				s = Util.addAttribute(s, "transfer");
			typeUI.setText(s);
			
			if (item.getDirection() != null) {
				if (item.getDirection() > 0)
					directionUI.setText("inflow");
				else if (item.getDirection() < 0)
					directionUI.setText("outflow");
				else
					directionUI.setText("internal");
			} else {
				directionUI.setText("system");
			}
			
			accountFromUI.setText(item.getFrom_account());
			accountToUI.setText(item.getTo_account());
			
			amountUI.setText(getAmountPair(item.getAmount()));
			feeUI.setText(getAmountPair(item.getFee()));
			feeUI.getText01UI().setDisable(true);
			feeUI.getText02UI().setDisable(true);
			
			memoUI.setText(item.getMemo());
			statusUI.setText(item.isIs_confirmed() ? "confirmed" : "pending");
			
			transactionNumberUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Transaction(item));
				event.consume();
	    	});
			
//			if (new Integer(item.getBlock_num()) > 0) {
//				blockNumberUI.setOnMouseClicked((MouseEvent event) -> {
//					module.jump(new Details_Block(item.getBlock_num()));
//					event.consume();
//		    	});
//			} else {
//				blockNumberUI.setEnabled(false);
//			}
			
			if (!Model.getInstance().isVirtualName(item.getFrom_account())) {
				accountFromUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
					module.jump(new Details_Account(item.getFrom_account()));
					event.consume();
		    	});
			} else {
				accountFromUI.setEnabled(false);
			}
			
			if (!Model.getInstance().isVirtualName(item.getTo_account())) {
				accountToUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
					module.jump(new Details_Account(item.getTo_account()));
					event.consume();
		    	});
			} else {
				accountToUI.setEnabled(false);
			}
			
			amountUI.getText01UI().setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Asset(item.getAmount().getAsset_id()));
				event.consume();
	    	});
			
			setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Transaction(item));
				event.consume();
	    	});
			
//			setOnMouseEntered((MouseEvent event) -> {
//				for (Node node : gridUI.getChildren())
//					((Display) node).showLabel();
//	    	});
//			setOnMouseExited((MouseEvent event) -> {
//				for (Node node : gridUI.getChildren())
//					((Display) node).hideLabel();
//	    	});
		}
	}

}
