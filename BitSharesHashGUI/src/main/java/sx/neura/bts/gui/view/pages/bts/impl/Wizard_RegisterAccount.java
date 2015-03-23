package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.display.DisplayChoice;
import sx.neura.bts.gui.view.components.display.DisplayOutput;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.json.api.wallet.WalletAccountRegister;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Util;

public class Wizard_RegisterAccount extends Page_Bts {
	
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
	private DisplayOutput myAccountUI;
	@FXML
	private DisplayChoice<Account> fromAccountUI;
	@FXML
	private DisplayOutput transactionFeeUI;
	@FXML
	private DisplayOutput availableVolumeUI;
	
	
	@FXML
	private Label confirmationIntroUI;
	@FXML
	private Label confirmationHeadersUI;
	@FXML
	private Label confirmationValuesUI;
	
	
	private Account myAccount;
	private Asset asset;
	private Account fromAccount;
	private Long transactionFee;
	
	private Callback callback;
	private Status status;
	
	public Wizard_RegisterAccount(Account myAccount) {
		this.myAccount = myAccount;
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
		return "Register Account";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		step01UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(0));
		step02UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(1));

		backUI.visibleProperty().bind(panoramaUI.getIndexProperty().greaterThan(0).and(isOverlay.not()));
		nextUI.visibleProperty().bind(isOverlay.not());
		
		asset = Model.getInstance().getAssetById(0);
		transactionFee = h.getTransactionFee(asset);
		
		myAccountUI.setText(myAccount.getName());
		
		fromAccountUI.setList(Model.getInstance().getMyAccounts());
		fromAccountUI.setRenderer((Account i) -> {
			return Util.crop(i.getName(), 34);
		});
		fromAccountUI.setResponder((Account oldValue, Account newValue) -> {
			availableVolumeUI.setText(Model.getInstance().getAmount(asset, h.getAvailableAmount(fromAccountUI.getItem().getName(), asset)));
		});
		
		transactionFeeUI.setText(Model.getInstance().getAmount(asset, transactionFee));
		
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
			
			if (h.getAvailableAmount(fromAccount.getName(), asset) < transactionFee) { 
				userException("Not enough funds");
				return;
			}
			
	    	confirmationIntroUI.setText(String.format("%s:", "You are about to register the following account"));
	    	String h = "";
			h += String.format("%s\n", "Account to Register");
			h += String.format("%s\n", "Registration Fee");
			h += String.format("%s\n", "Pay From");
			confirmationHeadersUI.setText(h);
			String v = "";
			v += String.format("%s\n", myAccount.getName());
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
							WalletAccountRegister.run(myAccount.getName(), fromAccount.getName());
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
			nextUI.disableProperty().bind(fromAccountUI.valueProperty().isNull());
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();	
		}
	}
}