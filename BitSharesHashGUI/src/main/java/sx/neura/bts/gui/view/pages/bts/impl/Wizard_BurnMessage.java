package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.AmountAndAccounts;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.display.DisplayChoice;
import sx.neura.bts.gui.view.components.display.DisplayInput;
import sx.neura.bts.gui.view.components.display.DisplayOutput;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.json.api.wallet.WalletBurn;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.enumerations.BurnMethod;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Util;
import sx.neura.bts.util.Validator;

public class Wizard_BurnMessage extends Page_Bts {
	
	public interface Callback {
		public void onActionComplete();
	}
	
	@FXML
	private LayerPane spaceUI;
	
	@FXML
	private Node progressTrackUI;
	@FXML
	private Node progressBarUI;
	
	@FXML
	private ToggleButton step01UI;
	@FXML
	private ToggleButton step02UI;
	
	@FXML
	private LayerPane panoramaUI;
	
	@FXML
	private Button backUI;
	@FXML
	private Button nextUI;
	

	@FXML
	private DisplayOutput toAccountUI;
	@FXML
	private DisplayChoice<Account> fromAccountUI;
	@FXML
	private DisplayChoice<Asset> assetUI;
	@FXML
	private DisplayOutput transactionFeeUI;
	@FXML
	private DisplayOutput availableVolumeUI;
	@FXML
	private DisplayInput volumeUI;
	@FXML
	private DisplayInput messageUI;
	
	
	@FXML
	private Label confirmationIntroUI;
	@FXML
	private Label confirmationHeadersUI;
	@FXML
	private Label confirmationValuesUI;
	
	
	private Account toAccount;
	private Account fromAccount;
	private Asset asset;
	private Long transactionFee;
	private Double volume;
	private String message;
	
	
	private Callback callback;
	private Status status;
	private Validator.IsDouble isDouble;
	
	public Wizard_BurnMessage(Account toAccount) {
		this.toAccount = toAccount;
	}
	
	private enum Status {
		PHASE_1("Next", 0),
		PHASE_2("Finish", 1);
		private Status(String next, int index) {
			this.next = next;
			this.index = index;
		}
		private String next;
		private int index;
	}
	
	private void setStatus(Status status) {
		this.status = status;
		nextUI.setText(status.next);
		panoramaUI.setIndex(status.index);
		manageBindings();
	}
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	@Override
	public String getTitle() {
		return "Burn Message";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		step01UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(0));
		step02UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(1));
		
		backUI.visibleProperty().bind(panoramaUI.getIndexProperty().greaterThan(0).and(isOverlay.not()));
		nextUI.visibleProperty().bind(isOverlay.not());
		
		toAccountUI.setText(toAccount.getName());
		
		fromAccountUI.setList(Model.getInstance().getMyAccounts());
		fromAccountUI.setRenderer((Account i) -> {
			return Util.crop(i.getName(), 34);
		});
		fromAccountUI.setResponder((Account oldValue, Account newValue) -> {
			if (assetUI.getItem() != null)
				availableVolumeUI.setText(Model.getInstance().getAmount(assetUI.getItem(), h.getAvailableAmount(fromAccountUI.getItem().getName(), assetUI.getItem())));
		});
		
		assetUI.setList(getAvailableAssets());
		assetUI.setRenderer((Asset i) -> {
			return i.getSymbol();
		});
		assetUI.setResponder((Asset oldValue, Asset newValue) -> {
			transactionFeeUI.setLabel(String.format("%s (%s)", "Transaction Fee", assetUI.getItem().getSymbol()));
			transactionFeeUI.setText(Model.getInstance().getAmount(assetUI.getItem(), h.getTransactionFee(assetUI.getItem())));
			availableVolumeUI.setLabel(String.format("%s (%s)", "Available Amount", assetUI.getItem().getSymbol()));
			volumeUI.setLabel(String.format("%s (%s)", "Amount", assetUI.getItem().getSymbol()));
			if (fromAccountUI.getItem() != null)
				availableVolumeUI.setText(Model.getInstance().getAmount(assetUI.getItem(), h.getAvailableAmount(fromAccountUI.getItem().getName(), assetUI.getItem())));
		});
		
		isDouble = new Validator.IsDouble();
    	
    	setStatus(Status.PHASE_1);
	}
	
	@FXML
	private void onNext() {
		if (status.equals(Status.PHASE_1)) {
			
			if (fromAccountUI.getItem() == null) {
				userException("From account is not set");
				return;
			}
			fromAccount = fromAccountUI.getItem();
			
			asset = assetUI.getItem();
			
			if (!isDouble.validate(volumeUI.getText())) {
				userException("Wrong amount");
				return;
			}
			volume = new Double(volumeUI.getText());
			
			transactionFee = h.getTransactionFee(asset);
			if (Model.getInstance().getRealValue(asset, h.getAvailableAmount(fromAccount.getName(), asset) - transactionFee) < volume) { 
				userException("Not enough funds");
				return;
			}
			
			if (messageUI.getText().isEmpty()) {
				userException("Message is empty");
				return;
			}
			message = messageUI.getText();

	    	confirmationIntroUI.setText(String.format("%s:", "You are about to burn the following message"));
	    	String h = "";
			h += String.format("%s\n", "To Account");
			h += String.format("%s\n", "Message");
			h += String.format("%s\n", "Amount to Burn");
			h += String.format("%s\n", "Transaction Fee");
			h += String.format("%s\n", "Pay From");
			confirmationHeadersUI.setText(h);
			String v = "";
			v += String.format("%s\n", toAccount.getName());
			v += String.format("%s\n", message);
			v += String.format("%s %s\n", asset.getSymbol(), Model.getInstance().getAmount(asset, volume));
			v += String.format("%s %s\n", asset.getSymbol(), Model.getInstance().getAmount(asset, transactionFee));
			v += String.format("%s\n", fromAccount.getName());
			confirmationValuesUI.setText(v);
			setStatus(Status.PHASE_2);
		} else if (status.equals(Status.PHASE_2)) {
			module.isProcessing().set(true);
			spaceUI.setIndex(1, () -> {
				animateProgress(1.0, 500, progressTrackUI, () -> {
					animateProgress(0.3, progressBarUI, () -> {
						try {
							WalletBurn.run(volume, asset.getSymbol(), fromAccount.getName(),
									toAccount.getName(), message, BurnMethod.FOR, false);
				    	} catch (BTSUserException e) {
				    		module.isProcessing().set(false);
				    		spaceUI.setIndex(0);
							userException(e);
							return;
						} catch (BTSSystemException e) {
							module.isProcessing().set(false);
				    		spaceUI.setIndex(0);
							systemException(e);
							return;
						}
						animateProgress(0.6, progressBarUI, () -> {
							if (callback != null)
								callback.onActionComplete();
							animateProgress(1.0, progressBarUI, () -> {
								module.removePageFromStack();
							});
						});
					});
				});
			});
		}
	}
	
	@FXML
	private void onBack() {
		if (status.equals(Status.PHASE_2))
			setStatus(Status.PHASE_1);
	}
	
	private void manageBindings() {
		if (status.equals(Status.PHASE_1)) {
			nextUI.disableProperty().unbind();
			nextUI.disableProperty().bind(fromAccountUI.valueProperty().isNull().or(
					assetUI.valueProperty().isNull().or(
					volumeUI.textProperty().isEmpty().or(
					messageUI.textProperty().isEmpty()))));
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();	
		}
	}
	
	private static List<Asset> getAvailableAssets() {
		List<Asset> availableAssets = new ArrayList<Asset>();
		for (AmountAndAccounts item : Model.getInstance().getMyAssetSplit())
			availableAssets.add(item.getAsset());
		return availableAssets;
	}
}
