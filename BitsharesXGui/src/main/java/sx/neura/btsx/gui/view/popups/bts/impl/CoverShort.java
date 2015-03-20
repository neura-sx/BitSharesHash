package sx.neura.btsx.gui.view.popups.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.json.api.wallet.WalletMarketCover;
import sx.neura.bts.json.api.wallet.WalletMarketOrderList;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Util;
import sx.neura.bts.util.Validator;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.components.packer.PackerChoice;
import sx.neura.btsx.gui.view.components.packer.PackerInput;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.popups.RunProcess;
import sx.neura.btsx.gui.view.popups.bts.Popup_Bts;

public class CoverShort extends Popup_Bts {
	
	public interface Callback {
		public void onActionComplete();
		public void onActionCancelled();
	}
	
	@FXML
	private LayerPane layerPaneUI;
	
	@FXML
	private Button backUI;
	@FXML
	private Button nextUI;
	
	@FXML
	private Label confirmationHeaderUI;
	@FXML
	private Label confirmationDetailsUI;
	
	@FXML
	private PackerText principalDueUI;
	@FXML
	private PackerText interestDueUI;
	@FXML
	private PackerText transactionFeeUI;
	@FXML
	private PackerText totalDueUI;
	@FXML
	private PackerInput paymentUI;
	
	@FXML
	private PackerChoice<Account> fromAccountUI;
	@FXML
	private PackerText availableVolumeUI;
	
	
	private long principalDue;
	private long interestDue;
	private long transactionFee;
	private long totalDue;
	
	private WalletMarketOrderList.Result order;
	private Asset asset;
	private Account fromAccount;
	private Double payment;
	
	private Callback callback;
	private Status status;
	private Validator.IsDouble isDouble;
	
	private enum Status {
		PHASE_1(t("YMNS.L.a78f_4564_9b4d"), 0),
		PHASE_2(t("YMNS.L.9504_4de0_b75d"), 1);
		private Status(String next, int index) {
			this.next = next;
			this.index = index;
		}
		private String next;
		private int index;
	}
	
	public void setOrder(WalletMarketOrderList.Result order) {
		this.order = order;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	private void setStatus(Status status) {
		this.status = status;
		backUI.setVisible(status.index > 0);
		nextUI.setText(status.next);
		layerPaneUI.setIndex(status.index);
		manageBindings();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		setTitle("Cover Short Position");
		
		principalDue = order.getOrder().getState().getBalance();
		interestDue = (long) (order.getOrder().getInterest_rate().getRatio() * order.getOrder().getState().getBalance());
		transactionFee = getTransactionFee(asset);
		totalDue = principalDue + interestDue + transactionFee;
		
		principalDueUI.setLabel(String.format("%s (%s)", "Principal Due", asset.getSymbol()));
		principalDueUI.setText(getAmount(asset, principalDue));
		
		interestDueUI.setLabel(String.format("%s (%s)", "Interest Due", asset.getSymbol()));
		interestDueUI.setText(getAmount(asset, interestDue));
		
		transactionFeeUI.setLabel(String.format("%s (%s)", "Transaction Fee", asset.getSymbol()));
		transactionFeeUI.setText(getAmount(asset, transactionFee));
		
		totalDueUI.setLabel(String.format("%s (%s)", "Total Due", asset.getSymbol()));
		totalDueUI.setText(getAmount(asset, totalDue));
		
		paymentUI.setLabel(String.format("%s (%s)", "Payment", asset.getSymbol()));
		paymentUI.setText(getAmount(asset, totalDue));
		
		fromAccountUI.setLabel("Pay From Account");
		fromAccountUI.setList(Model.getInstance().getMyAccounts());
		fromAccountUI.setRenderer(new PackerChoice.Renderer<Account>() {
			@Override
			public String render(Account i) {
				return Util.crop(i.getName(), 34);
			}
		});
		fromAccountUI.setResponder(new PackerChoice.Responder<Account>() {
			@Override
			public void onChange(Account oldValue, Account newValue) {
				availableVolumeUI.setText(getAmount(asset, getAvailableAmount()));
			}
		});
		fromAccountUI.setValue(Model.getInstance().getAccountFromList(fromAccount, fromAccountUI.getList()));
		
		availableVolumeUI.setLabel(String.format("%s (%s)", "Available Volume", asset.getSymbol()));
		
		isDouble = new Validator.IsDouble();
		
		setStatus(Status.PHASE_1);
	}
	
	@FXML
	private void onNext(ActionEvent event) {
		if (status.equals(Status.PHASE_1)) {
			
			if (fromAccountUI.getValue() == null) {
				userException(t("YMNS.N.6dad_4a61_84ee"));
				return;
			}
			fromAccount = fromAccountUI.getValue();
			
			if (!isDouble.validate(paymentUI.getText())) {
				userException(t("YMNS.N.b2ca_48e2_912a"));
				return;
			}
			payment = new Double(paymentUI.getText());
			
			if (getRealValue(asset, getAvailableAmount()) < payment) { 
				userException("Not enough funds");
				return;
			}
			if (payment > getRealValue(asset, totalDue)) { 
				userException("The payment is bigger than total due");
				return;
			}
			
			confirmationHeaderUI.setText(String.format("%s:", "You are about to cover the following short position"));
			String confirmation = "";
			confirmation += String.format("%30s: %s %s\n", "Total Due", asset.getSymbol(), getAmount(asset, totalDue));
			confirmation += String.format("%30s: %s %s\n", "Payment", asset.getSymbol(), getAmount(asset, payment));
			confirmation += String.format("%30s: %s\n", "Pay From", fromAccount.getName());
			confirmationDetailsUI.setText(confirmation);
			setStatus(Status.PHASE_2);
		} else if (status.equals(Status.PHASE_2)) {
			final RunProcess runProcess = new RunProcess();
			runProcess.setMessage(String.format("%s..", t("YMNS.O.72c9_4108_b95d")));
			runProcess.setCallback(new RunProcess.Callback() {
				@Override
				public void run() {
					try {
						WalletMarketCover.run(fromAccountUI.getValue().getName(), payment.toString(), asset.getSymbol(), order.getId());
						if (callback != null)
							callback.onActionComplete();
						runProcess.hide();
						hideItself();
					} catch (BTSUserException e) {
						runProcess.hide();
						if (callback != null)
							callback.onActionCancelled();
						userException(e);
					} catch (BTSSystemException e) {
						runProcess.hide();
						if (callback != null)
							callback.onActionCancelled();
						systemException(e);
					}
				}
			});
			runProcess.show(pane);
		}
	}
	
	@FXML
	private void onBack(ActionEvent event) {
		if (status.equals(Status.PHASE_2))
			setStatus(Status.PHASE_1);
	}
	
	@Override
	protected void onCancel(ActionEvent event) {
		if (callback != null)
			callback.onActionCancelled();
		super.onCancel(event);
	}
	
	private void manageBindings() {
		if (status.equals(Status.PHASE_1)) {
			nextUI.disableProperty().bind(paymentUI.getTextProperty().isNull());
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();
		}
	}
	
	private long getAvailableAmount() {
		return getAvailableAmount(fromAccountUI.getValue().getName(), asset);
	}
}
