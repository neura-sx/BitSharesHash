package sx.neura.btsx.gui.view.popups.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.json.api.wallet.WalletAccountRegister;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Util;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.components.packer.PackerChoice;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAccountList;
import sx.neura.btsx.gui.view.popups.RunProcess;
import sx.neura.btsx.gui.view.popups.bts.Popup_Bts;

public class RegisterAccount extends Popup_Bts {
	
	public interface Callback {
		public void onActionComplete();
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
	private Label transactionFeeUI;
	@FXML
	private PackerChoice<Account> fromAccountUI;
	@FXML
	private PackerText availableVolumeUI;
	
	private Asset asset;
	private long transactionFee;
	
	private String nameToRegister;
	private Account fromAccount;
	
	private Callback callback;
	private Status status;
	
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
	
	public void setNameToRegister(String nameToRegister) {
		this.nameToRegister = nameToRegister;
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
		
		setTitle(String.format("%s :: %s", "Register Account" , nameToRegister));
		
		asset = getAssetById(0);
		transactionFee = getTransactionFee(asset);
		
		transactionFeeUI.setText(String.format("%s: %s %s", "Transaction fee for registration",
				asset.getSymbol(), getAmount(asset, transactionFee)));
		
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
		
		availableVolumeUI.setLabel(String.format("%s (%s)", "Available Volume", asset.getSymbol()));
		
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
			
			if (getAvailableAmount() < transactionFee) { 
				userException("Not enough funds");
				return;
			}
			
			confirmationHeaderUI.setText(String.format("%s:", "You are about to register the following account"));
			String confirmation = "";
			confirmation += String.format("%30s: %s\n", "Name to Register", nameToRegister);
			confirmation += String.format("%30s: %s %s\n", "Registration Fee", asset.getSymbol(), getAmount(asset, transactionFee));
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
						WalletAccountRegister.run(nameToRegister, fromAccount.getName());
						MyAccountList.getInstance().reloadData();
						if (callback != null)
							callback.onActionComplete();
						runProcess.hide();
						hideItself();
					} catch (BTSSystemException e) {
						runProcess.hide();
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
	
	private void manageBindings() {
		if (status.equals(Status.PHASE_1)) {
			nextUI.disableProperty().bind(fromAccountUI.getValueProperty().isNull());
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();
		}
	}
	
	private long getAvailableAmount() {
		return getAvailableAmount(fromAccountUI.getValue().getName(), asset);
	}
}
