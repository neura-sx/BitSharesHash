package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.List;
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
import sx.neura.bts.json.api.wallet.WalletMarketSubmitAsk;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.MarketOrder;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Util;

public class Wizard_MarketOrderAsk extends Page_Bts {
	
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
	private DisplayInput quantityUI;
	
	@FXML
	private DisplayOutput highestBidUI;
	@FXML
	private DisplayInput priceUI;
	
	@FXML
	private DisplayOutput transactionFeeUI;
	@FXML
	private DisplayOutput totalUI;
	
	@FXML
	private Label confirmationIntroUI;
	@FXML
	private Label confirmationHeadersUI;
	@FXML
	private Label confirmationValuesUI;
	
	
	private Asset assetQuote;
	private Asset assetBase;
	private List<MarketOrder> orders;
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
	
	public Wizard_MarketOrderAsk(Asset assetBase, Asset assetQuote) {
		super();
		this.assetBase = assetBase;
		this.assetQuote = assetQuote;
	}
	
	@Override
	public String getTitle() {
		return "MarketOrderSell";
	}
	
	public void setOrders(List<MarketOrder> orders) {
		this.orders = orders;
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
		
    	transactionFee = h.getTransactionFee(assetQuote);
		
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
		
    	quantityUI.setLabel(String.format("%s (%s)", "Quantity", assetBase.getSymbol()));
    	quantityUI.setResponder(() -> {
			populateTotal();
		});
    	
    	priceUI.setLabel(String.format("%s (%s/%s)", "Price", assetQuote.getSymbol(), assetBase.getSymbol()));
    	priceUI.setResponder(() -> {
			populateTotal();
		});
    	
    	highestBidUI.setLabel(String.format("%s (%s/%s)", "Highest Bid", assetQuote.getSymbol(), assetBase.getSymbol()));
    	if (!orders.isEmpty()) {
	    	highestBidUI.setText(String.format("%.8f", Model.getInstance().getRealPrice(orders.get(0).getMarket_index().getOrder_price())));
	    	highestBidUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
	    		priceUI.setText(highestBidUI.getText());
	    		populateTotal();
			});
    	}
    	
    	transactionFeeUI.setLabel(String.format("%s (%s)", "Fee", assetQuote.getSymbol()));
    	transactionFeeUI.setText(Model.getInstance().getAmount(assetQuote, transactionFee));
    	
    	totalUI.setLabel(String.format("%s (%s)", "Total", assetQuote.getSymbol()));
		
    	setStatus(Status.PHASE_1);
	}
	
	@FXML
	private void onNext() {
		if (status.equals(Status.PHASE_1)) {
			Double quantity = null;
	    	Double price = null;
	    	try {
	    		quantity = new Double(quantityUI.getText());
	    		price = new Double(priceUI.getText());
	    	} catch (Exception e) {
	    		userException("Error in quantity or price");
		    	return;
			}
	    	if (quantity <= 0 || price <= 0) {
	    		userException("Negative values not allowed");
		    	return;
			}
	    	double total = quantity * price;
	    	if (quantity > Model.getInstance().getRealValue(assetBase, h.getAvailableAmount(accountsUI.getItem().getName(), assetBase))) {
	    		userException("You are trying to sell more than you have");
		    	return;
			}
	    	confirmationIntroUI.setText(String.format("%s:", "You are about to add the following order"));
	    	String h = "";
			h += String.format("%s\n", "Order Type");
			h += String.format("%s\n", "Account");
			h += String.format("%s\n", "Price");
			h += String.format("%s\n", "Quantity");
			h += String.format("%s\n", "Total");
			h += String.format("%s\n", "Fee");
			confirmationHeadersUI.setText(h);
			String v = "";
			v += String.format("%s\n", "Ask Order");
			v += String.format("%s\n", accountsUI.getItem().getName());
			v += String.format("%s %s/%s\n", String.format("%.8f", price), assetQuote.getSymbol(), assetBase.getSymbol());
			v += String.format("%s %s\n", assetBase.getSymbol(), Model.getInstance().format(quantity, assetBase.getPrecision()));
			v += String.format("%s %s\n", assetQuote.getSymbol(), Model.getInstance().format(total, assetQuote.getPrecision()));
			v += String.format("%s %s\n", assetQuote.getSymbol(), Model.getInstance().getAmount(assetQuote, transactionFee));
			confirmationValuesUI.setText(v);
			setStatus(Status.PHASE_2);
		} else if (status.equals(Status.PHASE_2)) {
			module.isProcessing().set(true);
			spaceUI.setIndex(1, () -> {
				animateProgress(1.0, 500, progressTrackUI, () -> {
					animateProgress(0.3, progressBarUI, () -> {
						try {
							WalletMarketSubmitAsk.run(accountsUI.getItem().getName(), quantityUI.getText(),
				    				assetBase.getSymbol(), priceUI.getText(), assetQuote.getSymbol(), false);
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
			nextUI.disableProperty().bind(accountsUI.valueProperty().isNull().or(totalUI.textProperty().isEmpty()));
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();	
		}
	}
	
	private void populateAvailableBalance() {
		availableBalanceUI.setText(Model.getInstance().getAmount(assetBase,
				h.getAvailableAmount(accountsUI.getItem().getName(), assetBase)));
    }
	
	private void populateTotal() {
    	if (!Util.isValidString(quantityUI.getText()) || !Util.isValidString(priceUI.getText())) {
    		totalUI.setText("");
    		return;
    	}
    	Double quantity = null;
    	Double price =  null;
    	try {
    		quantity = new Double(quantityUI.getText());
    		price = new Double(priceUI.getText());
    	} catch (Exception e) {
    		totalUI.setText("");
	    	return;
		}
    	totalUI.setText(Model.getInstance().getAmount(assetQuote, quantity * price));
    }
}
