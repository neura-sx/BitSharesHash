package sx.neura.btsx.gui.view.popups.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import sx.neura.bts.gui.dto.AmountAndAccounts;
import sx.neura.bts.gui.view.components.LayerPane;
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
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.components.packer.PackerChoice;
import sx.neura.btsx.gui.view.components.packer.PackerInput;
import sx.neura.btsx.gui.view.components.packer.PackerRadio;
import sx.neura.btsx.gui.view.components.packer.PackerRadioH;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAmountList;
import sx.neura.btsx.gui.view.pages.bts.impl.TransactionList;
import sx.neura.btsx.gui.view.popups.RunProcess;
import sx.neura.btsx.gui.view.popups.bts.Popup_Bts;

public class MakeTransfer extends Popup_Bts {
	
	public interface Callback {
		public void onActionComplete();
	}
	
	private static final int PUBLIC_KEY_SIZE = 54;
	private static final int PUBLIC_KEY_SUBSTRING_SIZE = 8;
	
	@FXML
	private LayerPane layerPaneUI;
	
	@FXML
	private Button backUI;
	@FXML
	private Button nextUI;
	
	@FXML
	protected Label introductionUI;
	
	@FXML
	private Pane boxUI;
	
	@FXML
	private Label confirmationHeaderUI;
	@FXML
	private Label confirmationDetailsUI;
	
	private PackerChoice<Account> fromAccountUI;
	private PackerRadio transferTypeUI;
	private PackerChoice<Account> toInternalAccountUI;
	private PackerChoice<Account> toExternalAccountUI;
	private PackerInput toPublicKeyUI;
	private PackerChoice<Asset> assetUI;
	private PackerText availableVolumeUI;
	private PackerInput volumeUI;
	private PackerInput memoUI;
	private PackerChoice<VoteMethod> voteMethodUI;
	
	private RadioButton internalRB;
	private RadioButton externalRB;
	private RadioButton publicKeyRB;
	
	private Account fromAccount;
	private Account toAccount;
	private Account toInternalAccount;
	private Account toExternalAccount;
	private String toPublicKey;
	private Asset asset;
	private Double volume;
	private String memo;
	private VoteMethod voteMethod;
	
	private Callback callback;
	protected Status status;
	private Validator.IsDouble isDouble;
	
	protected enum Status {
		PHASE_0(t("YMNS.L.a78f_4564_9b4d"), 0),
		PHASE_1(t("YMNS.L.a78f_4564_9b4d"), 1),
		PHASE_2(t("YMNS.L.9504_4de0_b75d"), 2);
		private Status(String next, int index) {
			this.next = next;
			this.index = index;
		}
		private String next;
		private int index;
	}
	
	public void setFromAccount(Account fromAccount) {
		this.fromAccount = fromAccount;
	}
	public void setToInternalAccount(Account toInternalAccount) {
		this.toInternalAccount = toInternalAccount;
	}
	public void setToExternalAccount(Account toExternalAccount) {
		this.toExternalAccount = toExternalAccount;
	}
	public void setAsset(Asset asset) {
		this.asset = asset;
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
	
	protected void setStatus(Status status) {
		this.status = status;
		backUI.setVisible(status.index > getInitialStatus().index);
		nextUI.setText(status.next);
		layerPaneUI.setIndex(status.index);
		manageBindings();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		internalRB = new RadioButton("Internal");
		externalRB = new RadioButton("External");
		publicKeyRB = new RadioButton("Public Key");
		transferTypeUI = new PackerRadioH();
		transferTypeUI.setLabel("Transfer Type");
		transferTypeUI.setRadioButtons(new RadioButton[] {internalRB, externalRB, publicKeyRB});
		transferTypeUI.setCallback(new PackerRadio.Callback() {
			@Override
			public void onChange(Toggle oldValue, Toggle newValue) {
				if (newValue.equals(internalRB)) {
					toInternalAccountUI.setVisible(true);
					toExternalAccountUI.setVisible(false);
					toPublicKeyUI.setVisible(false);
					toInternalAccountUI.requestFocus();
				} else if (newValue.equals(externalRB)) {
					toInternalAccountUI.setVisible(false);
					toExternalAccountUI.setVisible(true);
					toPublicKeyUI.setVisible(false);
					toExternalAccountUI.requestFocus();
				} else if (newValue.equals(publicKeyRB)) {
					toInternalAccountUI.setVisible(false);
					toExternalAccountUI.setVisible(false);
					toPublicKeyUI.setVisible(true);
					toPublicKeyUI.requestFocus();
				}
			}
		});
		
		fromAccountUI = new PackerChoice<Account>();
		fromAccountUI.setLabel("From Account");
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
		
		toInternalAccountUI = new PackerChoice<Account>();
		toInternalAccountUI.setLabel("To Internal Account");
		toInternalAccountUI.setList(Model.getInstance().getMyAccounts());
		toInternalAccountUI.setRenderer(new PackerChoice.Renderer<Account>() {
			@Override
			public String render(Account i) {
				return Util.crop(i.getName(), 34);
			}
		});
		
		toExternalAccountUI = new PackerChoice<Account>();
		toExternalAccountUI.setLabel("To External Account");
		toExternalAccountUI.setList(Model.getInstance().getFavoriteAccounts());
		toExternalAccountUI.setRenderer(new PackerChoice.Renderer<Account>() {
			@Override
			public String render(Account i) {
				return Util.crop(i.getName(), 34);
			}
		});
		
		toPublicKeyUI = new PackerInput();
		toPublicKeyUI.setLabel("To Public Key");
		
		assetUI = new PackerChoice<Asset>();
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
		
		availableVolumeUI = new PackerText();
		availableVolumeUI.setLabel("Available Volume");
		
		volumeUI = new PackerInput();
		HBox.setHgrow(volumeUI, Priority.ALWAYS);
		volumeUI.setLabel("Volume");
		
		memoUI = new PackerInput();
		HBox.setHgrow(memoUI, Priority.ALWAYS);
		memoUI.setLabel(t("YMNS.M.9440_4167_be2b"));
		
		voteMethodUI = new PackerChoice<VoteMethod>();
		voteMethodUI.setLabel("Vote Method");
		voteMethodUI.setList(Arrays.asList(VoteMethod.values()));
		voteMethodUI.setRenderer(new PackerChoice.Renderer<VoteMethod>() {
			@Override
			public String render(VoteMethod i) {
				return getVoteMethodLabel(i);
			}
		});
		voteMethodUI.setValue(VoteMethod.ALL);
		
		if (fromAccount != null) {
			fromAccountUI.setValue(Model.getInstance().getAccountFromList(fromAccount, fromAccountUI.getList()));
			fromAccountUI.setEnabled(false);
		}
		if (toInternalAccount != null) {
			transferTypeUI.selectToggle(internalRB);
			transferTypeUI.setEnabled(false);
			toInternalAccountUI.setValue(Model.getInstance().getAccountFromList(toInternalAccount, toInternalAccountUI.getList()));
			toInternalAccountUI.setEnabled(false);
		}
		if (toExternalAccount != null) {
			transferTypeUI.selectToggle(externalRB);
			transferTypeUI.setEnabled(false);
			toExternalAccountUI.setValue(Model.getInstance().getAccountFromList(toExternalAccount, toExternalAccountUI.getList()));
			toExternalAccountUI.setEnabled(false);
		}
		if (asset != null) {
			assetUI.setValue(Model.getInstance().getAssetFromList(asset, assetUI.getList()));
			assetUI.setEnabled(false);
		}
		if (volume != null) {
			volumeUI.setText(getAmount(asset, volume));
			volumeUI.setEnabled(false);
		}
		if (memo != null) {
			memoUI.setText(memo);
			memoUI.setEnabled(false);
		}
		
		isDouble = new Validator.IsDouble();
		if (toInternalAccount == null && toExternalAccount == null)
			transferTypeUI.selectToggle(internalRB);
		
		boxUI.getChildren().addAll(
				fromAccountUI,
				transferTypeUI,
				new StackPane(toInternalAccountUI, toExternalAccountUI, toPublicKeyUI),
				assetUI,
				availableVolumeUI,
				new HBox(10.0, volumeUI, memoUI),
				voteMethodUI);
		
		setTitle(getTitle());
		setStatus(getInitialStatus());
	}
	
	protected String getTitle() {
		return "Make Transfer";
	}
	
	protected Status getInitialStatus() {
		return Status.PHASE_1;
	}
	
	@FXML
	private void onNext(ActionEvent event) {
		if (status.equals(Status.PHASE_0)) {
			setStatus(Status.PHASE_1);
		} else if (status.equals(Status.PHASE_1)) {
			if (fromAccountUI.getValue() == null) {
				userException(t("YMNS.N.6dad_4a61_84ee"));
				return;
			}
			fromAccount = fromAccountUI.getValue();
			
			if (isPublicKey()) {
				if (toPublicKeyUI.getText().length() != PUBLIC_KEY_SIZE) {
					userException("The public key does not seem to be correct");
					return;
				}
				toPublicKey = toPublicKeyUI.getText();
			} else {
				if (getToAccountUI().getValue() == null) {
					userException(t("YMNS.N.6dad_4a61_84ee"));
					return;
				}
				toAccount = getToAccountUI().getValue();
			}

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
			
			memo = memoUI.getText();
			voteMethod = voteMethodUI.getValue();
			
			confirmationHeaderUI.setText(String.format("%s:", t("YMNS.I.0dc0_4243_b580")));
			String confirmation = "";
			confirmation += String.format("%30s: %s\n", t("YMNS.J.4ba7_47a0_a468"), fromAccount.getName());
			confirmation += String.format("%30s: %s\n", t("YMNS.J.2789_4cb9_8e5a"), isPublicKey() ? getPublicKeySubstring(toPublicKey, "..", 10) : toAccount.getName());
			confirmation += String.format("%30s: %s %s\n", t("YMNS.J.35ca_484d_9cdb"), asset.getSymbol(), getAmount(asset, volume));
			confirmation += String.format("%30s: %s %s\n", "Transaction Fee", asset.getSymbol(), getAmount(asset, transactionFee));
			confirmation += String.format("%30s: %s\n", t("YMNS.J.d756_4afd_9b45"), memo);
			confirmation += String.format("%30s: %s\n", "Vote Method", getVoteMethodLabel(voteMethod));
			confirmationDetailsUI.setText(confirmation);
			setStatus(Status.PHASE_2);
		} else if (status.equals(Status.PHASE_2)) {
			final RunProcess runProcess = new RunProcess();
			runProcess.setMessage(String.format("%s..", t("YMNS.O.72c9_4108_b95d")));
			runProcess.setCallback(new RunProcess.Callback() {
				@Override
				public void run() {
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
						TransactionList.getInstance().reloadData();
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
	protected void onBack(ActionEvent event) {
		if (status.equals(Status.PHASE_2))
			setStatus(Status.PHASE_1);
	}
	
	private void manageBindings() {
		if (status.equals(Status.PHASE_1)) {
			nextUI.disableProperty().bind(fromAccountUI.getValueProperty().isNull()
					.or(transferTypeUI.getSelectedToggleProperty().isEqualTo(internalRB).and(toInternalAccountUI.getValueProperty().isNull())
					.or(transferTypeUI.getSelectedToggleProperty().isEqualTo(externalRB).and(toExternalAccountUI.getValueProperty().isNull())
					.or(transferTypeUI.getSelectedToggleProperty().isEqualTo(publicKeyRB).and(toPublicKeyUI.getTextProperty().isEmpty())
					.or(assetUI.getValueProperty().isNull()
					.or(volumeUI.getTextProperty().isEmpty()))))));
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();
		}
	}
	
	private boolean isPublicKey() {
		return transferTypeUI.getSelectedToggle().equals(publicKeyRB);
	}
	
	private PackerChoice<Account> getToAccountUI() {
		if (transferTypeUI.getSelectedToggle().equals(internalRB))
			return toInternalAccountUI;
		if (transferTypeUI.getSelectedToggle().equals(externalRB))
			return toExternalAccountUI;
		return null;
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
	
	private static String getVoteMethodLabel(VoteMethod voteMethod) {
		if (voteMethod.equals(VoteMethod.ALL))
			return "all";
		if (voteMethod.equals(VoteMethod.NONE))
			return "none";
		if (voteMethod.equals(VoteMethod.RANDOM))
			return "random";
		if (voteMethod.equals(VoteMethod.RECOMMENDED))
			return "recommended";
		return null;
	}
	
	private static String getPublicKeySubstring(String publicKey, String separator, int size) {
		return String.format("%s%s%s",
				publicKey.substring(0, size), separator,
				publicKey.substring(publicKey.length() - size, publicKey.length()));
	}
}
