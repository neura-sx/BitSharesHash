package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.display.DisplayChoice;
import sx.neura.bts.gui.view.components.display.DisplayOutput;
import sx.neura.bts.gui.view.components.display.DisplayToggleList;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.json.api.wallet.WalletTransfer;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.enumerations.VoteMethod;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Util;

public class Wizard_UpdateVote extends Page_Bts {
	
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
	private Label introUI;
	@FXML
	private DisplayChoice<Account> accountUI;
	@FXML
	private DisplayOutput transactionFeeUI;
	@FXML
	private DisplayOutput availableVolumeUI;
	@FXML
	private DisplayToggleList<VoteMethod> voteMethodUI;
	
	@FXML
	private Label confirmationIntroUI;
	@FXML
	private Label confirmationHeadersUI;
	@FXML
	private Label confirmationValuesUI;
	
	@FXML
	private IdenticonCanvas avatarFromUI;
	@FXML
	private IdenticonCanvas avatarToUI;
	@FXML
	private Label nameFromUI;
	@FXML
	private Label nameToUI;
	@FXML
	private Label amountAssetUI;
	@FXML
	private Label amountValueUI;
	
	private Asset asset;
	private Account account;
	private Long transactionFee;
	private Double volume;
	private String memo;
	private VoteMethod voteMethod;
	
	private Status status;
	
	public void setAccount(Account account) {
		this.account = account;
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
	
	@Override
	public String getTitle() {
		return "Update Vote";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		step01UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(0));
		step02UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(1));

		backUI.visibleProperty().bind(panoramaUI.getIndexProperty().greaterThan(0).and(isOverlay.not()));
		nextUI.visibleProperty().bind(isOverlay.not());
		
		asset = getAssetById(0);
		transactionFee = getTransactionFee(asset);

		introUI.setText("You update your vote by transfering your funds to yourself. Your funds will remain as they are except for the transaction fee.");
		
		accountUI.setList(Model.getInstance().getMyAccounts());
		accountUI.setRenderer((Account i) -> {
			return Util.crop(i.getName(), 34);
		});
		accountUI.setResponder((Account oldValue, Account newValue) -> {
			availableVolumeUI.setText(getAmount(asset, getAvailableAmount(accountUI.getItem().getName(), asset)));
		});
		
		transactionFeeUI.setText(getAmount(asset, transactionFee));
		
		voteMethodUI.setItems(Arrays.asList(VoteMethod.values()), (VoteMethod i) -> {
			return getVoteMethodLabel(i);
		});
		voteMethodUI.selectItem(VoteMethod.ALL);
    	
		if (account != null) {
			accountUI.setItem(Model.getInstance().getAccountFromList(account, accountUI.getList()));
			accountUI.setEnabled(false);
		}
		
    	setStatus(Status.PHASE_1);
	}
	
	@FXML
	private void onNext() {
		if (status.equals(Status.PHASE_1)) {
			if (accountUI.getItem() == null) {
				userException("My account is not set");
				return;
			}
			account = accountUI.getItem();
			
			volume = getRealValue(asset, getAvailableAmount(account.getName(), asset) - transactionFee);
			if (volume <= 0) { 
				userException("Not enough funds");
				return;
			}
			
			voteMethod = voteMethodUI.getSelectedItem();
			memo = "vote update";
			
			confirmationIntroUI.setText(String.format("%s:", "You are about to execute the following transfer"));
			String h = "";
			h += String.format("%s\n", "From");
			h += String.format("%s\n", "To");
			h += String.format("%s\n", "Amount");
			h += String.format("%s\n", "Transaction Fee");
			h += String.format("%s\n", "Memo");
			h += String.format("%s\n", "Vote Method");
			confirmationHeadersUI.setText(h);
			String v = "";
			v += String.format("%s\n", account.getName());
			v += String.format("%s\n", account.getName());
			v += String.format("%s %s\n", asset.getSymbol(), getAmount(asset, volume));
			v += String.format("%s %s\n", asset.getSymbol(), getAmount(asset, transactionFee));
			v += String.format("%s\n", memo);
			v += String.format("%s\n", getVoteMethodLabel(voteMethod));
			confirmationValuesUI.setText(v);
			
			avatarFromUI.setName(account.getName());
			avatarToUI.setName(account.getName());
			nameFromUI.setText(account.getName());
			nameToUI.setText(account.getName());
			amountAssetUI.setText(asset.getSymbol());
			amountValueUI.setText(getAmount(asset, volume));
			
			setStatus(Status.PHASE_2);
		} else if (status.equals(Status.PHASE_2)) {
			module.isProcessing().set(true);
			spaceUI.setIndex(1, () -> {
				animateProgress(1.0, 500, progressTrackUI, () -> {
					animateProgress(0.3, progressBarUI, () -> {
						try {
							WalletTransfer.run(volume, asset.getSymbol(), account.getName(), account.getName(), memo, voteMethod);
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
							Page_0103_Transactions.getInstance().reloadData();
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
			nextUI.disableProperty().bind(accountUI.valueProperty().isNull());
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();	
		}
	}
}