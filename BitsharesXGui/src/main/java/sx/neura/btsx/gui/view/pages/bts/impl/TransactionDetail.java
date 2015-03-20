package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import sx.neura.bts.gui.dto.Transaction;
import sx.neura.bts.util.Time;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.components.packer.PackerAmount;
import sx.neura.btsx.gui.view.components.packer.PackerHyperlink;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;

public class TransactionDetail extends Page_Bts {
	
	private Transaction item;
	
	public TransactionDetail(Transaction item) {
		this.item = item;
	}
	
	
	@FXML
	private PackerText transactionIdUI;
	@FXML
	private PackerHyperlink blockNumberUI;
	
	@FXML
	private PackerHyperlink fromAccountUI;
	@FXML
	private PackerHyperlink toAccountUI;
	@FXML
	private PackerAmount amountUI;
	@FXML
	private PackerText memoUI;
	
	@FXML
	private PackerHyperlink runningBalance1nameUI;
	@FXML
	private PackerAmount runningBalance1AmountUI;
	
	@FXML
	private PackerHyperlink runningBalance2nameUI;
	@FXML
	private PackerAmount runningBalance2AmountUI;
	
	@FXML
	private PackerAmount feeAmountUI;
	
	@FXML
	private PackerText timestampUI;
	@FXML
	private PackerText expirationTimestampUI;
	
	
	@FXML
	private PackerText isVirtualUI;
	@FXML
	private PackerText isConfirmedUI;
	@FXML
	private PackerText isMarketUI;
	@FXML
	private PackerText isMarketCancelUI;
	
//	@FXML
//	private PackerText errorUI;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		amountUI.setCallback(() -> {
    		jump(new MyAmountDetail(item.getAmount().getAsset_id()));
    	});
		fromAccountUI.setCallback(() -> {
			jump(new MyAccountDetail(getAccountByName(item.getFrom_account())));
    	});
		toAccountUI.setCallback(() -> {
			jump(new MyAccountDetail(getAccountByName(item.getTo_account())));
    	});
		runningBalance1nameUI.setCallback(() -> {
			jump(new MyAccountDetail(getAccountByName(item.getRunning_balances().get(0).getName())));
    	});
		runningBalance2nameUI.setCallback(() -> {
			if (item.getRunning_balances().size() > 1)
				jump(new MyAccountDetail(getAccountByName(item.getRunning_balances().get(1).getName())));
    	});
		runningBalance1AmountUI.setCallback(() -> {
    		jump(new MyAmountDetail(item.getRunning_balances().get(0).getAmount().getAsset_id()));
    	});
		runningBalance2AmountUI.setCallback(() -> {
    		jump(new MyAmountDetail(item.getRunning_balances().get(1).getAmount().getAsset_id()));
    	});
		feeAmountUI.setCallback(() -> {
    		jump(new MyAmountDetail(item.getFee().getAsset_id()));
    	});
		blockNumberUI.setCallback(() -> {
    		jump(new BlockDetail(item.getBlock_num()));
    	});
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			transactionIdUI.setText(item.getTrx_id());
			blockNumberUI.setText(item.getBlock_num());
			blockNumberUI.setIsPassive(new Integer(item.getBlock_num()) <= 0);
			
			fromAccountUI.setText(item.getFrom_account());
			fromAccountUI.setIsPassive(Model.isVirtualName(fromAccountUI.getText()));
			fromAccountUI.setImage(getAvatarImage(item.getFrom_account()));
			
			toAccountUI.setText(item.getTo_account());
			toAccountUI.setIsPassive(Model.isVirtualName(toAccountUI.getText()));
			toAccountUI.setImage(getAvatarImage(item.getTo_account()));
			
			amountUI.setAmount(getAmountPair(item.getAmount()));
			memoUI.setText(item.getMemo());
			
			runningBalance1nameUI.setText(item.getRunning_balances().get(0).getName());
			runningBalance1nameUI.setImage(getAvatarImage(item.getRunning_balances().get(0).getName()));
			runningBalance1AmountUI.setAmount(getAmountPair(item.getRunning_balances().get(0).getAmount()));
			
			if (item.getRunning_balances().size() > 1) {
				runningBalance2nameUI.setText(item.getRunning_balances().get(1).getName());
				runningBalance2nameUI.setImage(getAvatarImage(item.getRunning_balances().get(1).getName()));
				runningBalance2AmountUI.setAmount(getAmountPair(item.getRunning_balances().get(1).getAmount()));
			} else {
				runningBalance2nameUI.setText("");
				runningBalance2nameUI.setImage(null);
				runningBalance2AmountUI.setAmount(null);
			}
			
			feeAmountUI.setAmount(getAmountPair(item.getFee()));
	
			timestampUI.setText(Time.format(item.getTimestamp()));
			expirationTimestampUI.setText(Time.format(item.getExpiration_timestamp(), "none"));
			
			isVirtualUI.setText(new Boolean(item.isIs_virtual()).toString());
			isConfirmedUI.setText(new Boolean(item.isIs_confirmed()).toString());
			isMarketUI.setText(new Boolean(item.isIs_market()).toString());
			isMarketCancelUI.setText(new Boolean(item.isIs_market_cancel()).toString());
			
//			errorUI.setText(item.getError() != null ? item.getError().getMessage() : "");
		}
		super.loadData();
	}
}
