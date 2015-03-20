package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.MyOrder;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.display.DisplayChoice;
import sx.neura.bts.gui.view.components.display.DisplayInput;
import sx.neura.bts.gui.view.components.display.DisplayOutput;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.json.api.wallet.WalletMarketCover;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Util;
import sx.neura.bts.util.Validator;

public class Wizard_MarketCoverShort extends Page_Bts {
	
	public interface Callback {
		public void onConfirm();
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
	private DisplayOutput principalDueUI;
	@FXML
	private DisplayOutput interestDueUI;
	@FXML
	private DisplayOutput transactionFeeUI;
	@FXML
	private DisplayOutput totalDueUI;
	
	@FXML
	private DisplayChoice<Account> fromAccountUI;
	@FXML
	private DisplayOutput availableVolumeUI;
	@FXML
	private DisplayInput paymentUI;
	
	
	@FXML
	private Label confirmationIntroUI;
	@FXML
	private Label confirmationHeadersUI;
	@FXML
	private Label confirmationValuesUI;
	
	
	private MyOrder order;
	
	private long principalDue;
	private long interestDue;
	private long transactionFee;
	private long totalDue;
	private Asset asset;
	private Account fromAccount;
	private Double payment;
	
	private Callback callback;
	private Status status;
	private Validator.IsDouble isDouble;
	
	public Wizard_MarketCoverShort(MyOrder order) {
		this.order = order;
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
	
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	@Override
	public String getTitle() {
		return "CoverShort";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		step01UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(0));
		step02UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(1));
		
		backUI.visibleProperty().bind(panoramaUI.getIndexProperty().greaterThan(0).and(isOverlay.not()));
		nextUI.visibleProperty().bind(isOverlay.not());
		
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
		totalDueUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
			paymentUI.setText(totalDueUI.getText());
		});
		
		paymentUI.setLabel(String.format("%s (%s)", "Payment", asset.getSymbol()));
		paymentUI.setText(getAmount(asset, totalDue));
		
		fromAccountUI.setList(Model.getInstance().getMyAccounts());
		fromAccountUI.setRenderer((Account i) -> {
			return Util.crop(i.getName(), 34);
		});
		fromAccountUI.setResponder((Account oldValue, Account newValue) -> {
			availableVolumeUI.setText(getAmount(asset, getAvailableAmount(fromAccountUI.getItem().getName(), asset)));
		});
		fromAccountUI.setItem(Model.getInstance().getAccountFromList(fromAccount, fromAccountUI.getList()));
		
		availableVolumeUI.setLabel(String.format("%s (%s)", "Available Amount", asset.getSymbol()));
		
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
			
			if (!isDouble.validate(paymentUI.getText())) {
				userException("Wrong amount");
				return;
			}
			payment = new Double(paymentUI.getText());
			
			if (getRealValue(asset, getAvailableAmount(fromAccount.getName(), asset)) < payment) { 
				userException("Not enough funds");
				return;
			}
			if (payment > getRealValue(asset, totalDue)) { 
				userException("The payment is bigger than total due");
				return;
			}

	    	confirmationIntroUI.setText(String.format("%s:", "You are about to cover the following short position"));
	    	String h = "";
			h += String.format("%s\n", "Total Due");
			h += String.format("%s\n", "Payment");
			h += String.format("%s\n", "Pay From");
			confirmationHeadersUI.setText(h);
			String v = "";
			v += String.format("%s %s\n", asset.getSymbol(), getAmount(asset, totalDue));
			v += String.format("%s %s\n", asset.getSymbol(), getAmount(asset, payment));
			v += String.format("%s\n", fromAccount.getName());
			confirmationValuesUI.setText(v);
			setStatus(Status.PHASE_2);
		} else if (status.equals(Status.PHASE_2)) {
			module.isProcessing().set(true);
			spaceUI.setIndex(1, () -> {
				animateProgress(1.0, 500, progressTrackUI, () -> {
					animateProgress(0.3, progressBarUI, () -> {
						try {
							WalletMarketCover.run(fromAccount.getName(), payment.toString(), asset.getSymbol(), order.getId());
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
								callback.onConfirm();
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
					paymentUI.textProperty().isEmpty()));
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();	
		}
	}
}