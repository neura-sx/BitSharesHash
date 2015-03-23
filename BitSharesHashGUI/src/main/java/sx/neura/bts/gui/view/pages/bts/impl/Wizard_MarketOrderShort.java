package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.display.DisplayChoice;
import sx.neura.bts.gui.view.components.display.DisplayInput;
import sx.neura.bts.gui.view.components.display.DisplayOutput;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.json.api.wallet.WalletMarketSubmitShort;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Util;

public class Wizard_MarketOrderShort extends Page_Bts {
	
	public interface Callback {
		public void onConfirm(Account account);
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
	private DisplayChoice<Account> accountsUI;
	
	@FXML
	private DisplayOutput availableBalanceUI;
	@FXML
	private DisplayInput collateralUI;
	@FXML
	private DisplayInput quantityUI;
	
	@FXML
	private DisplayInput interestRateUI;
	
	@FXML
	private DisplayOutput feedPriceUI;
	@FXML
	private DisplayInput priceLimitUI;
		
	@FXML
	private DisplayOutput transactionFeeUI;
	
	@FXML
	private Label confirmationIntroUI;
	@FXML
	private Label confirmationHeadersUI;
	@FXML
	private Label confirmationValuesUI;
	
	
	private Asset assetQuote;
	private Asset assetBase;
	private double feedPrice;
	private long transactionFee;
	
	private Callback callback;
	private Status status;
	
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
	
	public Wizard_MarketOrderShort(Asset assetBase, Asset assetQuote) {
		super();
		this.assetBase = assetBase;
		this.assetQuote = assetQuote;
	}
	
	@Override
	public String getTitle() {
		return "MarketOrderShort";
	}
	
	public void setFeedPrice(double feedPrice) {
		this.feedPrice = feedPrice;
	}
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		step01UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(0));
		step02UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(1));
		
		backUI.visibleProperty().bind(panoramaUI.getIndexProperty().greaterThan(0).and(isOverlay.not()));
		nextUI.visibleProperty().bind(isOverlay.not());
		
    	transactionFee = h.getTransactionFee(assetBase);
		
		accountsUI.setList(Model.getInstance().getMyAccounts());
		accountsUI.setRenderer(new DisplayChoice.Renderer<Account>() {
			@Override
			public String render(Account item) {
				return item.getName();
			}
		});
		accountsUI.getTextUI().setOnAction((ActionEvent event) -> {
			populateAvailableBalance();
		});
		
		availableBalanceUI.setLabel(String.format("%s (%s)", "Available Balance", assetBase.getSymbol()));
		
		collateralUI.setLabel(String.format("%s (%s)", "Collateral", assetBase.getSymbol()));
		collateralUI.setResponder(() -> {
			populateQuantity();
		});
		
		quantityUI.setLabel(String.format("%s (%s)", "Quantity", assetQuote.getSymbol()));
		quantityUI.setResponder(() -> {
			populateCollateral();
		});
		
		interestRateUI.setLabel(String.format("%s (%s)",  "Interest Rate", "APR"));
		interestRateUI.setText("0.0");
		
		feedPriceUI.setLabel(String.format("%s (%s/%s)", "Feed Price", assetQuote.getSymbol(), assetBase.getSymbol()));
		feedPriceUI.setText(String.format("%.8f", feedPrice));
		feedPriceUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
			priceLimitUI.setText(feedPriceUI.getText());
		});
		
		priceLimitUI.setLabel(String.format("%s (%s/%s)", "Price Limit", assetQuote.getSymbol(), assetBase.getSymbol()));
		priceLimitUI.setPrompt(String.format("(%s)", "Optional"));
    	
		transactionFeeUI.setLabel(String.format("%s (%s)", "Fee", assetBase.getSymbol()));
    	transactionFeeUI.setText(Model.getInstance().getAmount(assetBase, transactionFee));
    	
		setStatus(Status.PHASE_1);
	}
	
	@FXML
	private void onNext() {
		if (status.equals(Status.PHASE_1)) {
			Double collateral = null;
			Double quantity = null;
			Double interestRate = null;
	    	Double priceLimit = null;
	    	try {
	    		collateral = new Double(collateralUI.getText());
	    		quantity = new Double(quantityUI.getText());
	    		interestRate = new Double(interestRateUI.getText());
	    		priceLimit = new Double(priceLimitUI.getText().isEmpty() ? "0" : priceLimitUI.getText());
	    	} catch (Exception e) {
	    		userException("Error in collateral, interest rate or price limit");
		    	return;
			}
	    	if (collateral <= 0 || interestRate < 0 || priceLimit < 0) {
	    		userException("Negative values not allowed");
		    	return;
			}
	    	if (collateral > Model.getInstance().getRealValue(assetBase, h.getAvailableAmount(accountsUI.getItem().getName(), assetBase))) {
	    		userException("You are trying to short more than you have");
		    	return;
			}
	    	confirmationIntroUI.setText(String.format("%s:", "You are about to add the following order"));
	    	String h = "";
			h += String.format("%s\n", "Order Type");
			h += String.format("%s\n", "Account");
			h += String.format("%s\n", "Collateral");
			h += String.format("%s\n", "Quantity");
			h += String.format("%s\n", "Interest Rate");
			h += String.format("%s\n", "Price Limit");
			h += String.format("%s\n", "Fee");
			confirmationHeadersUI.setText(h);
			String v = "";
			v += String.format("%s\n", "Short Order");
			v += String.format("%s\n", accountsUI.getItem().getName());
			v += String.format("%s %s\n", assetBase.getSymbol(), Model.getInstance().format(collateral, assetBase.getPrecision()));
			v += String.format("%s %s\n", assetQuote.getSymbol(), Model.getInstance().format(quantity, assetQuote.getPrecision()));
			v += String.format("%s %s\n", String.format("%.2f", interestRate), "%");
			if (priceLimit > 0)
				v += String.format("%s %s/%s\n", String.format("%.8f", priceLimit), assetQuote.getSymbol(), assetBase.getSymbol());
			else
				v += String.format("%s\n", "none");
			v += String.format("%s %s\n", assetBase.getSymbol(), Model.getInstance().getAmount(assetBase, transactionFee));
			confirmationValuesUI.setText(v);
			setStatus(Status.PHASE_2);
		} else if (status.equals(Status.PHASE_2)) {
			module.isProcessing().set(true);
			spaceUI.setIndex(1, () -> {
				animateProgress(1.0, 500, progressTrackUI, () -> {
					animateProgress(0.3, progressBarUI, () -> {
						try {
							WalletMarketSubmitShort.run(accountsUI.getItem().getName(), collateralUI.getText(),
				    				assetBase.getSymbol(), interestRateUI.getText(), assetQuote.getSymbol(),
				    				priceLimitUI.getText().isEmpty() ? "0" : priceLimitUI.getText());
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
								callback.onConfirm(accountsUI.getItem());
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
			nextUI.disableProperty().bind(accountsUI.valueProperty().isNull()
					.or(collateralUI.textProperty().isEmpty()
							.or(interestRateUI.textProperty().isEmpty()
									.or(quantityUI.textProperty().isEmpty()))));
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();	
		}
	}
	
	private void populateAvailableBalance() {
		availableBalanceUI.setText(Model.getInstance().getAmount(assetBase,
				h.getAvailableAmount(accountsUI.getItem().getName(), assetBase)));
    }
	 
	private void populateQuantity() {
    	if (!Util.isValidString(collateralUI.getText())) {
    		quantityUI.setText("");
    		return;
    	}
    	Double collateral = null;
    	try {
    		collateral = new Double(collateralUI.getText());
    	} catch (Exception e) {
    		quantityUI.setText("");
	    	return;
		}
    	quantityUI.setText(Model.getInstance().getAmount(assetQuote, collateral * Model.SHORT_COLLATERAL_FACTOR * feedPrice));
    }
	
	private void populateCollateral() {
    	if (!Util.isValidString(quantityUI.getText())) {
    		collateralUI.setText("");
    		return;
    	}
    	Double quantity = null;
    	try {
    		quantity = new Double(quantityUI.getText());
    	} catch (Exception e) {
    		collateralUI.setText("");
	    	return;
		}
    	collateralUI.setText(Model.getInstance().getAmount(assetBase, quantity / (Model.SHORT_COLLATERAL_FACTOR * feedPrice)));
    }
}
