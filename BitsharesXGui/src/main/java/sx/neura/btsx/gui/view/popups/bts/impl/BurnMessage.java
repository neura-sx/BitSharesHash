package sx.neura.btsx.gui.view.popups.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sx.neura.bts.gui.dto.AmountAndAccounts;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.json.api.wallet.WalletBurn;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.enumerations.BurnMethod;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Util;
import sx.neura.bts.util.Validator;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.components.packer.PackerChoice;
import sx.neura.btsx.gui.view.components.packer.PackerInput;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAccountList;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAmountList;
import sx.neura.btsx.gui.view.popups.RunProcess;
import sx.neura.btsx.gui.view.popups.bts.Popup_Bts;

public class BurnMessage extends Popup_Bts {
	
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
	private PackerChoice<Account> fromAccountUI;
	@FXML
	private PackerChoice<Asset> assetUI;
	@FXML
	private PackerText availableVolumeUI;
	@FXML
	private PackerInput volumeUI;
	@FXML
	private PackerInput messageUI;
	
	private Account toAccount;
	private Account fromAccount;
	private Asset asset;
	private Double volume;
	private String message;
	
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
	
	public void setToAccount(Account toAccount) {
		this.toAccount = toAccount;
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
		
		setTitle(String.format("%s :: %s", "Burn Message" , toAccount.getName()));
		
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
				if (assetUI.getValue() != null)
					availableVolumeUI.setText(getAmount(assetUI.getValue(), getAvailableAmount()));
			}
		});
		
		assetUI.setLabel("Asset");
		assetUI.setList(getAvailableAssets());
		assetUI.setRenderer(new PackerChoice.Renderer<Asset>() {
			@Override
			public String render(Asset i) {
				return i.getSymbol();
			}
		});
		assetUI.setResponder(new PackerChoice.Responder<Asset>() {
			@Override
			public void onChange(Asset oldValue, Asset newValue) {
				volumeUI.setLabel(String.format("%s (%s)", "Volume", assetUI.getValue().getSymbol()));
				availableVolumeUI.setLabel(String.format("%s (%s)", "Available Volume", assetUI.getValue().getSymbol()));
				if (fromAccountUI.getValue() != null)
					availableVolumeUI.setText(getAmount(assetUI.getValue(), getAvailableAmount()));
			}
		});
		
		availableVolumeUI.setLabel("Available Volume");
		volumeUI.setLabel("Volume");
		messageUI.setLabel("Message");
		
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
			
			if (assetUI.getValue() == null) {
				userException(t("YMNS.N.6dad_4a61_84ee"));
				return;
			}
			asset = assetUI.getValue();
			
			if (!isDouble.validate(volumeUI.getText())) {
				userException(t("YMNS.N.b2ca_48e2_912a"));
				return;
			}
			volume = new Double(volumeUI.getText());
			long transactionFee = getTransactionFee(asset);
			if (getRealValue(asset, getAvailableAmount() - transactionFee) < volume) { 
				userException("Not enough funds");
				return;
			}
			
			message = messageUI.getText();
			
			confirmationHeaderUI.setText(String.format("%s:", "You are about to burn the following message"));
			String confirmation = "";
			confirmation += String.format("%30s: %s\n", "To Account", toAccount.getName());
			confirmation += String.format("%30s: %s\n", "Message", message);
			confirmation += String.format("%30s: %s %s\n", "Amount to Burn", asset.getSymbol(), getAmount(asset, volume));
			confirmation += String.format("%30s: %s %s\n", "Transaction Fee", asset.getSymbol(), getAmount(asset, transactionFee));
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
						WalletBurn.run(volume, asset.getSymbol(), fromAccount.getName(),
								toAccount.getName(), message, BurnMethod.FOR, false);
						MyAccountList.getInstance().reloadData();
						if (callback != null)
							callback.onActionComplete();
						runProcess.hide();
						hideItself();
					} catch (BTSUserException e) {
						runProcess.hide();
						userException(e);
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
		return getAvailableAmount(fromAccountUI.getValue().getName(), assetUI.getValue());
	}
	
	private static List<Asset> getAvailableAssets() {
		List<Asset> availableAssets = new ArrayList<Asset>();
		for (AmountAndAccounts item : MyAmountList.getInstance().getList())
			availableAssets.add(item.getAsset());
		return availableAssets;
	}
}
