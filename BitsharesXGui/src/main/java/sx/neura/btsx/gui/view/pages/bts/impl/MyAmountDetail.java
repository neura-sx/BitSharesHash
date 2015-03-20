package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.dto.AccountAndAmount;
import sx.neura.bts.gui.dto.AmountAndAccounts;
import sx.neura.bts.json.api.blockchain.BlockchainGetFeedsForAsset;
import sx.neura.bts.json.api.wallet.WalletAccountBalance;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.MarketFeed;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Time;
import sx.neura.bts.util.Util;
import sx.neura.btsx.gui.view.Page;
import sx.neura.btsx.gui.view.components.CellHyperlink;
import sx.neura.btsx.gui.view.components.flow.Flow;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;
import sx.neura.btsx.gui.view.pages.bts.comp.Transactions;
import sx.neura.btsx.gui.view.pages.bts.comp.flow.AccountAndAmountFlowRenderer;
import sx.neura.btsx.gui.view.popups.bts.impl.MakeTransfer;

public class MyAmountDetail extends Page_Bts {
	
	private AmountAndAccounts item;
	private Asset asset;
	
	@FXML
	private Label avatarUI;
	@FXML
	private Label symbolUI;
	@FXML
	private Label amountUI;
	
	@FXML
	private PackerText issuerUI;
	@FXML
	private PackerText precisionUI;
	@FXML
	private PackerText collectedFeesUI;
	@FXML
	private PackerText nameUI;
	@FXML
	private PackerText descriptionUI;
	@FXML
	private PackerText typeUI;
//	@FXML
//	private PackerText publicDataUI;
	@FXML
	private PackerText registrationDateUI;
	@FXML
	private PackerText lastUpdateUI;
	@FXML
	private PackerText currentShareSupplyUI;
	@FXML
	private PackerText maximumShareSupplyUI;
	
	@FXML
	private Node emptyBalancesUI;
	@FXML
	private Node validBalancesUI;
	
	@FXML
	private Flow<AccountAndAmount> balancesUI;
	
	@FXML
	private Transactions transactionsUI;
	
	@FXML
	private TabPane topTabPaneUI;
	@FXML
	private TabPane bottomTabPaneUI;
	@FXML
	private Tab marketFeedTabUI;
	
	@FXML
	private TableView<MarketFeed> marketFeedUI;
	
	@FXML
	private TableColumn<MarketFeed, String> marketFeed01UI;
	@FXML
	private TableColumn<MarketFeed, String> marketFeed02UI;
	@FXML
	private TableColumn<MarketFeed, String> marketFeed03UI;
	@FXML
	private TableColumn<MarketFeed, String> marketFeed04UI;
	
	
	public MyAmountDetail(int assetId) {
		this(getAssetById(assetId));
	}
	
	public MyAmountDetail(Asset asset) {
		this.asset = asset;
	}
	
	public MyAmountDetail(AmountAndAccounts item) {
		this.item = item;
		this.asset = item.getAsset();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		balancesUI.setCellFactory((AccountAndAmount item) -> {
			AccountAndAmountFlowRenderer renderer = new AccountAndAmountFlowRenderer();
			renderer.setItem(item);
			renderer.setActor((AccountAndAmount i) -> {
				jump(new MyAccountDetail(i.getAccount()));
			});
			renderer.setJumper((Page page) -> {
				jump(page);
			});
			return renderer;
		});
		
		issuerUI.setCallback(new PackerText.Callback() {
			@Override
			public void onAction() {
				jump(new TheirAccountDetail(getAccount(issuerUI.getText())));
			}
		});
		
		avatarUI.setText(Util.getSubstring(asset.getSymbol(), 4));
		
		emptyBalancesUI.visibleProperty().bind(validBalancesUI.visibleProperty().not());
		
		transactionsUI.setPage(this);
		
		marketFeedUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		marketFeedUI.setPlaceholder(new Label("There is no market feed"));
		
		marketFeed01UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDelegate_name()));
		marketFeed02UI.setCellValueFactory(item -> new SimpleStringProperty(getAmount(asset, item.getValue().getPrice())));
		marketFeed03UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getPrice() > 0 ? getAmount(getAssetById(0), 1 / item.getValue().getPrice()): "n/a"));
		marketFeed04UI.setCellValueFactory(item -> new SimpleStringProperty(Time.format(item.getValue().getLast_update())));
		
		marketFeed01UI.setCellFactory(item -> {
			TableCell<MarketFeed, String> cell = new TableCell<MarketFeed, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                	super.updateItem(item, empty);
                	setText(null);
                    if (item != null && !empty) {
                    	CellHyperlink link = new CellHyperlink();
                    	link.setText(item);
                    	link.setOnMouseClicked(event -> { 
                    		jump(new TheirAccountDetail(getAccount(item)));
                    	});
                    	setGraphic(link);
                    } else {
                    	setGraphic(null);
                    }
                }
            };
            return cell;
		});
		
		adjustTableColumn(marketFeed02UI, Pos.CENTER_RIGHT);
		adjustTableColumn(marketFeed03UI, Pos.CENTER_RIGHT);
		
		manageButton(action01UI, (MouseEvent event) -> {
			makeTransfer();
		});
		
		if (asset.getId() == 0)
			bottomTabPaneUI.getTabs().remove(marketFeedTabUI);
		else if (!bottomTabPaneUI.getTabs().contains(marketFeedTabUI))
			bottomTabPaneUI.getTabs().add(marketFeedTabUI);
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			if (item == null) {
				try {
					item = new AmountAndAccounts();
					item.setAsset(asset);
					item.setAmount(0);
					item.setSplit(new ArrayList<AccountAndAmount>());
					List<WalletAccountBalance.Result> balances = WalletAccountBalance.run();
					for (WalletAccountBalance.Result balance : balances) {
						for (Amount amount : balance.getAmounts()) {
							if (amount.getAsset_id() != asset.getId())
								continue;
							AccountAndAmount s = new AccountAndAmount();
							s.setAccount(getAccountByName(balance.getName()));
							Amount a = new Amount();
							a.setAsset_id(amount.getAsset_id());
							a.setValue(amount.getValue());
							s.setAmount(a);
							item.setAmount(item.getAmount() + amount.getValue());
							item.getSplit().add(s);
						}
					}
				} catch (BTSSystemException e) {
					systemException(e);
				}
			}
			amountUI.setText(getAmount(asset, item.getAmount()));
			symbolUI.setText(asset.getSymbol());
			Account issuer = getAccount(asset.getIssuer_account_id());
			issuerUI.setText(issuer != null ? issuer.getName() : "");
			issuerUI.setDisable(issuer == null);
			precisionUI.setText(new Integer(asset.getPrecision()).toString());
			collectedFeesUI.setText(getAmount(asset, asset.getCollected_fees()));
			nameUI.setText(asset.getName());
			descriptionUI.setText(asset.getDescription());
			typeUI.setText(asset.getIssuer_account_id() <= 0 ? "Market-pegged Asset" : "User-issued Asset");
			//publicDataUI.setText(asset.getPublic_data());
			registrationDateUI.setText(Time.format(asset.getRegistration_date()));
			lastUpdateUI.setText(Time.format(asset.getLast_update(), "never"));
			currentShareSupplyUI.setText(new Long(asset.getCurrent_share_supply()).toString());
			maximumShareSupplyUI.setText(new Long(asset.getMaximum_share_supply()).toString());
			validBalancesUI.setVisible(!item.getSplit().isEmpty());
			balancesUI.setItems(item.getSplit());
			balancesUI.applyItems();
			try {
				transactionsUI.setAll(WalletAccountTransactionHistory.run("", asset.getSymbol(), 0), asset);
			} catch (BTSSystemException e) {
				systemException(e);
			}
			try {
				List<MarketFeed> feed = BlockchainGetFeedsForAsset.run(asset.getSymbol());
				Collections.sort(feed, (MarketFeed r1, MarketFeed r2) -> {
					return r2.getLast_update().compareTo(r1.getLast_update());
				});
				marketFeedUI.getItems().clear();
				for (MarketFeed f : feed)
					if (f.getPrice() > 0)
						marketFeedUI.getItems().add(f);
			} catch (BTSSystemException e) {
				systemException(e);
			}
			marketFeed02UI.setText(String.format("Price of %s in %s", getAssetById(0).getSymbol(), asset.getSymbol()));
			marketFeed03UI.setText(String.format("Price of %s in %s", asset.getSymbol(), getAssetById(0).getSymbol()));
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		isDataLoaded = false;
		loadData();
	}
	
	private void makeTransfer() {
		MakeTransfer makeTransfer = new MakeTransfer();
		makeTransfer.setAsset(asset);
		makeTransfer.setCallback(() -> {
			reloadData();
		});
		makeTransfer.show(pane);
	}
}
