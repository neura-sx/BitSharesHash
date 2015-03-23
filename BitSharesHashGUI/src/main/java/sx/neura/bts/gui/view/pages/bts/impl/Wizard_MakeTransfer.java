package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.AmountAndAccounts;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.display.DisplayChoice;
import sx.neura.bts.gui.view.components.display.DisplayInput;
import sx.neura.bts.gui.view.components.display.DisplayOutput;
import sx.neura.bts.gui.view.components.display.DisplayToggleList;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.json.api.wallet.WalletAddContactAccount;
import sx.neura.bts.json.api.wallet.WalletRemoveContactAccount;
import sx.neura.bts.json.api.wallet.WalletTransfer;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.enumerations.VoteMethod;
import sx.neura.bts.json.exceptions.BTSException;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Util;
import sx.neura.bts.util.Validator;

public class Wizard_MakeTransfer extends Page_Bts {
	
	public interface Callback {
		public void onActionComplete();
	}
	
	private static final int PUBLIC_KEY_SIZE = 54;
	private static final int PUBLIC_KEY_SUBSTRING_SIZE = 8;
	
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
	private ToggleButton step03UI;
	
	@FXML
	private LayerPane panoramaUI;
	
	@FXML
	private Button backUI;
	@FXML
	private Button nextUI;
	
	
	@FXML
	private IdenticonCanvas avatarInternalUI;
	@FXML
	private IdenticonCanvas avatarFavoriteUI;
	@FXML
	private IdenticonCanvas avatarExternalUI;
	
	@FXML
	private DisplayChoice<Account> fromAccountUI;
	@FXML
	private DisplayToggleList<TransferType> transferTypeUI;
	@FXML
	private DisplayChoice<Account> toInternalAccountUI;
	@FXML
	private DisplayChoice<Account> toFavoriteAccountUI;
	@FXML
	private DisplayInput toExternalAccountUI;
	@FXML
	private DisplayInput toPublicKeyUI;
	@FXML
	private DisplayChoice<Asset> assetUI;
	@FXML
	private DisplayOutput transactionFeeUI;
	@FXML
	private DisplayOutput availableVolumeUI;
	@FXML
	private DisplayInput volumeUI;
	@FXML
	private DisplayInput memoUI;
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
	private Account fromAccount;
	private Account toAccount;
	private Account toInternalAccount;
	private Account toFavoriteAccount;
	private Account toExternalAccount;
	private String toPublicKey;
	private Long transactionFee;
	private Double volume;
	private String memo;
	private VoteMethod voteMethod;
	
	private Callback callback;
	private Status status;
	private Validator.IsDouble isDouble;
	
	public void setAsset(Asset asset) {
		this.asset = asset;
	}
	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}
	public void setToInternalAccount(Account toInternalAccount) {
		this.toInternalAccount = toInternalAccount;
	}
	public void setToFavoriteAccount(Account toFavoriteAccount) {
		this.toFavoriteAccount = toFavoriteAccount;
	}
	public void setToExternalAccount(Account toExternalAccount) {
		this.toExternalAccount = toExternalAccount;
	}
	public void setVolume(Double volume) {
		this.volume = volume;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	@Override
	public String getTitle() {
		return "MakeTransfer";
	}
	
	private enum Status {
		PHASE_1("Next", 0),
		PHASE_2("Next", 1),
		PHASE_3("Finish", 2);
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
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		step01UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(0));
		step02UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(1));
		step03UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(2));
		
		backUI.visibleProperty().bind(panoramaUI.getIndexProperty().greaterThan(0).and(isOverlay.not()));
		nextUI.visibleProperty().bind(isOverlay.not());
		
		transferTypeUI.setItems(Arrays.asList(TransferType.values()), (TransferType item) -> {
			return item.label;
		});
		transferTypeUI.setCallback((TransferType item) -> {
			if (item.equals(TransferType.INTERNAL)) {
				toInternalAccountUI.setVisible(true);
				toFavoriteAccountUI.setVisible(false);
				toExternalAccountUI.setVisible(false);
				toPublicKeyUI.setVisible(false);
				toInternalAccountUI.requestFocus();
			} else if (item.equals(TransferType.FAVORITE)) {
				toInternalAccountUI.setVisible(false);
				toFavoriteAccountUI.setVisible(true);
				toExternalAccountUI.setVisible(false);
				toPublicKeyUI.setVisible(false);
				toFavoriteAccountUI.requestFocus();
			} else if (item.equals(TransferType.EXTERNAL)) {
				toInternalAccountUI.setVisible(false);
				toFavoriteAccountUI.setVisible(false);
				toExternalAccountUI.setVisible(true);
				toPublicKeyUI.setVisible(false);
				toExternalAccountUI.requestFocus();
			} else if (item.equals(TransferType.PUBLIC_KEY)) {
				toInternalAccountUI.setVisible(false);
				toFavoriteAccountUI.setVisible(false);
				toExternalAccountUI.setVisible(false);
				toPublicKeyUI.setVisible(true);
				toPublicKeyUI.requestFocus();
			}
		});
		
		toInternalAccountUI.setList(Model.getInstance().getMyAccounts());
		toInternalAccountUI.setRenderer((Account i) -> {
			return Util.crop(i.getName(), 34);
		});
		toInternalAccountUI.setResponder((Account oldValue, Account newValue) -> {
			avatarInternalUI.setName(toInternalAccountUI.getItem().getName());
		});
		avatarInternalUI.setOnMouseClicked((MouseEvent event) -> {
			module.jump(new Details_Account(toInternalAccountUI.getItem()));
			event.consume();
    	});
		
		toFavoriteAccountUI.setList(Model.getInstance().getFavoriteAccounts());
		toFavoriteAccountUI.setRenderer((Account i) -> {
			return Util.crop(i.getName(), 34);
		});
		toFavoriteAccountUI.setResponder((Account oldValue, Account newValue) -> {
			avatarFavoriteUI.setName(toFavoriteAccountUI.getItem().getName());
		});
		avatarFavoriteUI.setOnMouseClicked((MouseEvent event) -> {
			module.jump(new Details_Account(toFavoriteAccountUI.getItem()));
			event.consume();
    	});
		
		toExternalAccountUI.setResponder(() -> {
			avatarExternalUI.setName(toExternalAccountUI.getText());
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
		
		fromAccountUI.setList(Model.getInstance().getMyAccounts());
		fromAccountUI.setRenderer((Account i) -> {
			return Util.crop(i.getName(), 34);
		});
		fromAccountUI.setResponder((Account oldValue, Account newValue) -> {
			if (assetUI.getItem() != null)
				availableVolumeUI.setText(Model.getInstance().getAmount(assetUI.getItem(), h.getAvailableAmount(fromAccountUI.getItem().getName(), assetUI.getItem())));
		});
		
		voteMethodUI.setItems(Arrays.asList(VoteMethod.values()), (VoteMethod i) -> {
			return Model.getInstance().getVoteMethodLabel(i);
		});
		
		avatarFromUI.setOnMouseClicked((MouseEvent event) -> {
			module.jump(new Details_Account(fromAccount));
			event.consume();
    	});
		avatarToUI.setOnMouseClicked((MouseEvent event) -> {
			module.jump(new Details_Account(toAccount));
			event.consume();
    	});
		amountAssetUI.setOnMouseClicked((MouseEvent event) -> {
			module.jump(new Details_Asset(asset));
			event.consume();
    	});
		
		voteMethodUI.selectItem(VoteMethod.ALL);
		
		if (toInternalAccount != null) {
			transferTypeUI.selectToggle(0);
			transferTypeUI.setEnabled(false);
			toInternalAccountUI.setItem(Model.getInstance().getAccountFromList(toInternalAccount, toInternalAccountUI.getList()));
			toInternalAccountUI.setEnabled(false);
		}
		if (toFavoriteAccount != null) {
			transferTypeUI.selectToggle(1);
			transferTypeUI.setEnabled(false);
			toFavoriteAccountUI.setItem(Model.getInstance().getAccountFromList(toFavoriteAccount, toFavoriteAccountUI.getList()));
			toFavoriteAccountUI.setEnabled(false);
		}
		if (toExternalAccount != null) {
			transferTypeUI.selectToggle(2);
			transferTypeUI.setEnabled(false);
			toExternalAccountUI.setText(toExternalAccount.getName());
			toExternalAccountUI.setEnabled(false);
			avatarExternalUI.setName(toExternalAccount.getName());
			avatarExternalUI.setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Account(toExternalAccount));
				event.consume();
	    	});
		}
		if (fromAccount != null) {
			fromAccountUI.setItem(Model.getInstance().getAccountFromList(fromAccount, fromAccountUI.getList()));
			//fromAccountUI.setEnabled(false);
		}
		if (asset != null) {
			assetUI.setItem(Model.getInstance().getAssetFromList(asset, assetUI.getList()));
			//assetUI.setEnabled(false);
		}
		if (volume != null) {
			volumeUI.setText(Model.getInstance().getAmount(asset, volume));
			//volumeUI.setEnabled(false);
		}
		if (memo != null) {
			memoUI.setText(memo);
			//memoUI.setEnabled(false);
		}
		
		isDouble = new Validator.IsDouble();
		if (toInternalAccount == null && toFavoriteAccount == null && toExternalAccount == null)
			transferTypeUI.selectToggle(1);
		
		setStatus(Status.PHASE_1);
	}
	
	@FXML
	private void onNext() {
		if (status.equals(Status.PHASE_1)) {
			if (isPublicKey()) {
				if (toPublicKeyUI.getText().length() != PUBLIC_KEY_SIZE) {
					userException("The public key does not seem to be correct");
					return;
				}
				toPublicKey = toPublicKeyUI.getText();
			} else if (transferTypeUI.getSelectedIndex().equals(2)) {
				if (toExternalAccount != null) {
					toAccount = toExternalAccount;
				} else {
					if (toExternalAccountUI.getText().isEmpty()) {
						userException("External account is empty");
						return;
					}
					toAccount = h.getAccount(toExternalAccountUI.getText());
					if (toAccount == null) {
						userException(String.format("Account '%s' does not exist", toExternalAccountUI.getText()));
						return;
					}
				}
			} else {
				if (transferTypeUI.getSelectedIndex().equals(0))
					toAccount = toInternalAccountUI.getItem();
				else if (transferTypeUI.getSelectedIndex().equals(1))
					toAccount = toFavoriteAccountUI.getItem();	
				if (toAccount == null) {
					userException("Recipient account is not set");
					return;
				}
			}
			if (fromAccountUI.getItem() == null) {
				userException("My account is not set");
				return;
			}
			fromAccount = fromAccountUI.getItem();
			if (assetUI.getItem() == null) {
				userException("Asset is not set");
				return;
			}
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
			setStatus(Status.PHASE_2);
		} else if (status.equals(Status.PHASE_2)) {
			memo = memoUI.getText();
			voteMethod = voteMethodUI.getSelectedItem();
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
			v += String.format("%s\n", fromAccount.getName());
			v += String.format("%s\n", isPublicKey() ? getPublicKeySubstring(toPublicKey, "..", 10) : toAccount.getName());
			v += String.format("%s %s\n", asset.getSymbol(), Model.getInstance().getAmount(asset, volume));
			v += String.format("%s %s\n", asset.getSymbol(), Model.getInstance().getAmount(asset, transactionFee));
			v += String.format("%s\n", memo);
			v += String.format("%s\n", Model.getInstance().getVoteMethodLabel(voteMethod));
			confirmationValuesUI.setText(v);
			
			avatarFromUI.setName(fromAccount.getName());
			avatarToUI.setName(toAccount.getName());
			nameFromUI.setText(fromAccount.getName());
			nameToUI.setText(toAccount.getName());
			amountAssetUI.setText(asset.getSymbol());
			amountValueUI.setText(Model.getInstance().getAmount(asset, volume));

			setStatus(Status.PHASE_3);
		} else if (status.equals(Status.PHASE_3)) {
			module.isProcessing().set(true);
			spaceUI.setIndex(1, () -> {
				animateProgress(1.0, 500, progressTrackUI, () -> {
					animateProgress(0.3, progressBarUI, () -> {
						try {
							if (isPublicKey()) {
								String toAccountName = getPublicKeySubstring(toPublicKey, "-", PUBLIC_KEY_SUBSTRING_SIZE).toLowerCase();
								WalletAddContactAccount.run(toAccountName, toPublicKey);
								try {
									WalletTransfer.run(volume, asset.getSymbol(), fromAccount.getName(),
											toAccountName, memo, voteMethod);
								} catch (BTSException e) {
									WalletRemoveContactAccount.run(toAccountName);
									throw e;
								}
								WalletRemoveContactAccount.run(toAccountName);
							} else {
								WalletTransfer.run(volume, asset.getSymbol(), fromAccount.getName(),
										toAccount.getName(), memo, voteMethod);
							}
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
		else if (status.equals(Status.PHASE_3))
			setStatus(Status.PHASE_2);
	}
	
	private void manageBindings() {
		if (status.equals(Status.PHASE_1)) {
			nextUI.disableProperty().unbind();
			nextUI.disableProperty().bind(
					transferTypeUI.selectedToggleProperty().isEqualTo(transferTypeUI.getToggle(0)).and(toInternalAccountUI.valueProperty().isNull()).or(
					transferTypeUI.selectedToggleProperty().isEqualTo(transferTypeUI.getToggle(1)).and(toFavoriteAccountUI.valueProperty().isNull()).or(
					transferTypeUI.selectedToggleProperty().isEqualTo(transferTypeUI.getToggle(2)).and(toExternalAccountUI.textProperty().isEmpty()).or(
					transferTypeUI.selectedToggleProperty().isEqualTo(transferTypeUI.getToggle(3)).and(toPublicKeyUI.textProperty().isEmpty()).or(
					fromAccountUI.valueProperty().isNull().or(
					assetUI.valueProperty().isNull().or(
					volumeUI.textProperty().isEmpty())))))));
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();
		} else if (status.equals(Status.PHASE_3)) {
			nextUI.disableProperty().unbind();
		}
	}
	
	private boolean isPublicKey() {
		return transferTypeUI.getSelectedIndex().equals(3);
	}

	private enum TransferType {
		INTERNAL("Internal"),
		FAVORITE("Favorite"),
		EXTERNAL("Account Name"),
		PUBLIC_KEY("Public Key");
		private TransferType(String label) {
			this.label = label;
		}
		private String label;
	}
	
	private static List<Asset> getAvailableAssets() {
		List<Asset> availableAssets = new ArrayList<Asset>();
		for (AmountAndAccounts item : Model.getInstance().getMyAssetSplit())
			availableAssets.add(item.getAsset());
		return availableAssets;
	}
	
	private static String getPublicKeySubstring(String publicKey, String separator, int size) {
		return String.format("%s%s%s",
				publicKey.substring(0, size), separator,
				publicKey.substring(publicKey.length() - size, publicKey.length()));
	}
}
