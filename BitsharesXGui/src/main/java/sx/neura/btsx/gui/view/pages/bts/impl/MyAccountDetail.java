package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import sx.neura.bts.gui.dto.BurnRecord;
import sx.neura.bts.json.api.blockchain.BlockchainGetAccountWall;
import sx.neura.bts.json.api.blockchain.BlockchainGetFeedsFromDelegate;
import sx.neura.bts.json.api.wallet.WalletAccountBalance;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory;
import sx.neura.bts.json.api.wallet.WalletAccountVoteSummary;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.MarketFeed;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Time;
import sx.neura.bts.util.Util;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.Page;
import sx.neura.btsx.gui.view.components.CellHyperlink;
import sx.neura.btsx.gui.view.components.flow.Flow;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;
import sx.neura.btsx.gui.view.pages.bts.comp.Transactions;
import sx.neura.btsx.gui.view.pages.bts.comp.flow.AmountFlowRenderer;
import sx.neura.btsx.gui.view.pages.bts.comp.flow.BurnRecordFlowRenderer;
import sx.neura.btsx.gui.view.popups.bts.impl.MakeTransfer;
import sx.neura.btsx.gui.view.popups.bts.impl.RegisterAccount;
import sx.neura.btsx.gui.view.popups.bts.impl.ShowPublicKey;
import sx.neura.btsx.gui.view.popups.bts.impl.UpdateVote;

public class MyAccountDetail extends Page_Bts {
	
	private Account item;
	
	@FXML
	private Transactions transactionsUI;
	
	@FXML
	private Node emptyBurnRecordsUI;
	@FXML
	private Node validBurnRecordsUI;
	
	@FXML
	private Flow<BurnRecord> burnRecordsUI;
	
	@FXML
	private PackerText nameUI;
	@FXML
	private PackerText publicKeyUI;
	@FXML
	private PackerText typeUI;
	@FXML
	private PackerText registrationDateUI;
	@FXML
	private PackerText lastUpdateUI;
	
	@FXML
	private PackerText approvalUI;
	@FXML
	private PackerText reliabilityUI;
	@FXML
	private PackerText blocksProducedUI;
	@FXML
	private PackerText blocksMissedUI;
	@FXML
	private PackerText payRateUI;
	@FXML
	private PackerText payBalanceUI;
	
	@FXML
	private TabPane topTabPaneUI;
	@FXML
	private TabPane bottomTabPaneUI;
	@FXML
	private Tab delegateInfoTabUI;
	@FXML
	private Tab marketFeedTabUI;
	
	
	@FXML
	private Label avatarNameUI;
	@FXML
	private ImageView avatarUI;
	
	@FXML
	private Node emptyBalancesUI;
	@FXML
	private Node validBalancesUI;
	
	@FXML
	private Flow<Amount> balancesUI;
	
//	@FXML
//	private TableView<Amount> balancesUI;
//	@FXML
//	private TableColumn<Amount, Amount> balances01UI;
//	@FXML
//	private TableColumn<Amount, String> balances02UI;
	
	@FXML
	private TableView<Account> votesUI;
	
	@FXML
	private TableColumn<Account, String> votes01UI;
	@FXML
	private TableColumn<Account, String> votes02UI;
	@FXML
	private TableColumn<Account, String> votes03UI;
	@FXML
	private TableColumn<Account, String> votes04UI;
	@FXML
	private TableColumn<Account, String> votes05UI;
	@FXML
	private TableColumn<Account, String> votes06UI;
	@FXML
	private TableColumn<Account, String> votes07UI;
	
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
	@FXML
	private TableColumn<MarketFeed, String> marketFeed05UI;
	
//	private ObservableList<Amount> amountList;
	private ObservableList<Account> accountList;
	private List<WalletAccountVoteSummary.Result> votes;
	
	public MyAccountDetail(Account item) {
		this.item = item;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		publicKeyUI.setCallback(new PackerText.Callback() {
			@Override
			public void onAction() {
				ShowPublicKey showPublicKey = new ShowPublicKey();
				showPublicKey.setName(item.getName());
				showPublicKey.setPublicKey(item.getOwner_key());
				showPublicKey.show(pane);
			}
		});
		
		balancesUI.setCellFactory((Amount item) -> {
			AmountFlowRenderer renderer = new AmountFlowRenderer();
			renderer.setItem(item);
			renderer.setActor((Amount i) -> {
				jump(new MyAmountDetail(i.getAsset_id()));
			});
			renderer.setJumper((Page page) -> {
				jump(page);
			});
			return renderer;
		});
		
		emptyBalancesUI.visibleProperty().bind(validBalancesUI.visibleProperty().not());
		
		burnRecordsUI.setCellFactory((BurnRecord item) -> {
			BurnRecordFlowRenderer renderer = new BurnRecordFlowRenderer();
			renderer.setItem(item);
			renderer.setActor((BurnRecord i) -> {
				jump(new MyAmountDetail(i.getAmount().getAsset_id()));
			});
			renderer.setJumper((Page page) -> {
				jump(page);
			});
			return renderer;
		});
		
		emptyBurnRecordsUI.visibleProperty().bind(validBurnRecordsUI.visibleProperty().not());
		
//		{
//			balancesUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//			balancesUI.setPlaceholder(new Label("There are no balances"));
//			
//			balances01UI.setCellValueFactory(item -> new SimpleObjectProperty<Amount>(item.getValue()));
//			balances02UI.setCellValueFactory(item -> new SimpleStringProperty(getAmount(item.getValue().getAsset_id(), item.getValue().getAmount())));
//			
//			balances01UI.setCellFactory(item  -> {
//				TableCell<Amount, Amount> cell = new TableCell<Amount, Amount>() {
//	                @Override
//	                public void updateItem(Amount item, boolean empty) {
//	                	super.updateItem(item, empty);
//	                	setText(null);
//	                    if (item != null && !empty) {
//	                    	CellHyperlink link = new CellHyperlink();
//	                    	link.setText(getAssetById(item.getAsset_id()).getSymbol());
//	                    	link.setOnMouseClicked(event -> {
//	                    		jump(new MyAmountDetail(getAssetById(getItem().getAsset_id())));
//	                    	});
//	                    	setGraphic(link);
//	                    } else
//	                    	setGraphic(null);
//	                }
//	            };
//	            return cell;
//			});
//			
//			adjustTableColumn(balances02UI, Pos.CENTER_RIGHT);
//			
//			amountList = FXCollections.observableArrayList();
//			balancesUI.setItems(amountList);
//		}
		
		{
			votesUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			votesUI.setPlaceholder(new Label("There are no votes"));
			votesUI.setRowFactory(new Callback<TableView<Account>, TableRow<Account>>() {
				@Override
				public TableRow<Account> call(TableView<Account> param) {
					TableRow<Account> row = new TableRow<Account>() {
						@Override
		                protected void updateItem(Account item, boolean empty) {
		                    super.updateItem(item, empty);
		                    if (item != null) {
		                    	getStyleClass().remove("delegate-active-positive");
		                    	getStyleClass().remove("delegate-active-negative");
		                    	getStyleClass().remove("delegate-passive-positive");
		                    	getStyleClass().remove("delegate-passive-negative");
		                    	if (isActiveDelegate(item))
		                    		getStyleClass().add(item.getApproved() > 0 ? "delegate-active-positive" : "delegate-active-negative");
		                    	else
		                    		getStyleClass().add(item.getApproved() > 0 ? "delegate-passive-positive" : "delegate-passive-negative");
		                    }
		                }
					};
		            return row;
				}
			});
			
			votes01UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getName()));
			votes02UI.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%.2f%s", getDelegateSupport(item.getValue()) * 100, "%")));
			votes03UI.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%.2f%s", getDelegateReliability(item.getValue()) * 100, "%")));
			votes04UI.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%d", item.getValue().getDelegate_info().getBlocks_produced())));
			votes05UI.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%d%s", item.getValue().getDelegate_info().getPay_rate(), "%")));
			votes06UI.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%s", getAccountApprovalLabel(item.getValue()))));
			votes07UI.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%s", getAmount(0, getVotes(item.getValue())))));
			
			votes07UI.setText(String.format("%s (%s)", "Votes", getAssetById(0).getSymbol()));
			
			votes01UI.setCellFactory(item  -> {
				TableCell<Account, String> cell = new TableCell<Account, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                	super.updateItem(item, empty);
	                	setText(null);
	                    if (item != null && !empty) {
	                    	CellHyperlink link = new CellHyperlink();
	                    	link.setText(item);
	                    	link.setOnMouseClicked(event -> {
	                    		jump(new TheirAccountDetail(accountList.get(getTableRow().getIndex())));
	                    	});
	                    	setGraphic(link);
	                    } else
	                    	setGraphic(null);
	                }
	            };
	            return cell;
			});
			
			adjustTableColumn(votes02UI, Pos.CENTER_RIGHT);
			adjustTableColumn(votes03UI, Pos.CENTER_RIGHT);
			adjustTableColumn(votes04UI, Pos.CENTER_RIGHT);
			adjustTableColumn(votes05UI, Pos.CENTER_RIGHT);
			adjustTableColumn(votes06UI, Pos.CENTER);
			adjustTableColumn(votes07UI, Pos.CENTER_RIGHT);
			
			accountList = FXCollections.observableArrayList();
			votesUI.setItems(accountList);
		}
		
		{
			marketFeedUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			marketFeedUI.setPlaceholder(new Label("There is no market feed"));
			
			marketFeed01UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getAsset_symbol()));
			marketFeed02UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getAsset_symbol()));
			marketFeed03UI.setCellValueFactory(item -> new SimpleStringProperty(Time.format(item.getValue().getLast_update())));
			marketFeed04UI.setCellValueFactory(item -> new SimpleStringProperty(getAmount(getAssetBySymbol(item.getValue().getAsset_symbol()), item.getValue().getPrice())));
			marketFeed05UI.setCellValueFactory(item -> new SimpleStringProperty(getAmount(getAssetBySymbol(item.getValue().getAsset_symbol()), item.getValue().getMedian_price())));
			
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
	                    		jump(new MyAmountDetail(getAssetBySymbol(item)));
	                    	});
	                    	setGraphic(link);
	                    } else {
	                    	setGraphic(null);
	                    }
	                }
	            };
	            return cell;
			});
			
			marketFeed02UI.setCellFactory(item -> {
				TableCell<MarketFeed, String> cell = new TableCell<MarketFeed, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                	super.updateItem(item, empty);
	                	setText(null);
	                    if (item != null && !empty) {
	                    	CellHyperlink link = new CellHyperlink();
	                    	link.setText(getAssetBySymbol(item).getName());
	                    	link.setOnMouseClicked(event -> { 
	                    		jump(new MyAmountDetail(getAssetBySymbol(item)));
	                    	});
	                    	setGraphic(link);
	                    } else {
	                    	setGraphic(null);
	                    }
	                }
	            };
	            return cell;
			});
			
			adjustTableColumn(marketFeed04UI, Pos.CENTER_RIGHT);
			adjustTableColumn(marketFeed05UI, Pos.CENTER_RIGHT);
		}
		
		transactionsUI.setPage(this);
		
		manageButton(action01UI, (MouseEvent event) -> {
			registerAccount();
		});
		manageButton(action02UI, (MouseEvent event) -> {
			makeTransfer();
		});
		manageButton(action03UI, (MouseEvent event) -> {
			updateVote();
		});
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			nameUI.setText(item.getName());
			publicKeyUI.setText(item.getOwner_key());
			typeUI.setText(isDelegate(item) ? (isActiveDelegate(item) ? "Active Delegate" : "Standby Delegate") : "Standard User");
			registrationDateUI.setText(Time.format(item.getRegistration_date(), "unregistered"));
			lastUpdateUI.setText(Time.format(item.getLast_update(), "never"));
			if (isDelegate(item)) {
				approvalUI.setText(String.format("%.2f%s", getDelegateSupport(item) * 100, "%"));
				reliabilityUI.setText(String.format("%.2f%s", getDelegateReliability(item) * 100, "%"));
				blocksProducedUI.setText(String.format("%d", item.getDelegate_info().getBlocks_produced()));
				blocksMissedUI.setText(String.format("%d", item.getDelegate_info().getBlocks_missed()));
				payRateUI.setText(String.format("%d%s", item.getDelegate_info().getPay_rate(), "%"));
				payBalanceUI.setText(getAmount(0, item.getDelegate_info().getPay_balance()));
			} else {
				approvalUI.setText("");
				reliabilityUI.setText("");
				blocksProducedUI.setText("");
				blocksMissedUI.setText("");
				payRateUI.setText("");
				payBalanceUI.setText("");
			}
			try {
				transactionsUI.setAll(WalletAccountTransactionHistory.run(item.getName(), "", 0));
				
				List<WalletAccountBalance.Result> balances = WalletAccountBalance.run(item.getName());
				validBalancesUI.setVisible(!balances.isEmpty());
				balancesUI.setItems(balances.isEmpty() ? null : balances.get(0).getAmounts());
				balancesUI.applyItems();
//				amountList.setAll(balances.isEmpty() ? null : balances.get(0).getAmounts());
				
				votes = WalletAccountVoteSummary.run(item.getName());
				accountList.clear();
				for (Account account : Model.getInstance().getAccounts()) {
					if (account.getApproved() != 0)
						accountList.add(account);
				}
				Collections.sort(accountList, (Account a1, Account a2) -> {
					return a1.getName().compareTo(a2.getName());
				});
				Util.resetTableViewScroll(votesUI);
			} catch (BTSSystemException e) {
				systemException(e);
			}
			try {
				List<BlockchainGetAccountWall.Result> wall = BlockchainGetAccountWall.run(item.getName());
				validBurnRecordsUI.setVisible(!wall.isEmpty());
				List<BurnRecord> burnRecords = new ArrayList<BurnRecord>();
				for (BlockchainGetAccountWall.Result w : wall) {
					BurnRecord r = new BurnRecord();
					r.setAmount(w.getAmount());
					r.setMessage(w.getMessage());
					burnRecords.add(r);
				}
				burnRecordsUI.setItems(burnRecords);
				burnRecordsUI.applyItems();
			} catch (BTSSystemException e) {
				systemException(e);
			}
			marketFeedUI.getItems().clear();
			if (isDelegate(item)) {
				try {
					List<MarketFeed> feed = BlockchainGetFeedsFromDelegate.run(item.getName());
					Collections.sort(feed, (MarketFeed r1, MarketFeed r2) -> {
						return r1.getAsset_symbol().compareTo(r2.getAsset_symbol());
					});
					
					for (MarketFeed f : feed)
						if (f.getPrice() > 0)
							marketFeedUI.getItems().add(f);
				} catch (BTSSystemException e) {
					systemException(e);
				}
			}
			avatarNameUI.setText(Util.crop(item.getName(), 20));
			avatarUI.setImage(getAvatarImage(item.getName()));
			action01UI.setDisable(!Time.isUndefined(item.getRegistration_date()));
			
			if (!isDelegate(item)) {
				topTabPaneUI.getTabs().remove(delegateInfoTabUI);
				bottomTabPaneUI.getTabs().remove(marketFeedTabUI);
			} else {
				if (!topTabPaneUI.getTabs().contains(delegateInfoTabUI))
					topTabPaneUI.getTabs().add(delegateInfoTabUI);
				if (!bottomTabPaneUI.getTabs().contains(marketFeedTabUI))
					bottomTabPaneUI.getTabs().add(marketFeedTabUI);
			}
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		isDataLoaded = false;
		loadData();
	}
	
	private void registerAccount() {
		RegisterAccount registerAccount = new RegisterAccount();
		registerAccount.setNameToRegister(item.getName());
		registerAccount.setCallback(() -> {
			reloadData();
		});
		registerAccount.show(pane);
	}
	
	private void makeTransfer() {
		MakeTransfer makeTransfer = new MakeTransfer();
		makeTransfer.setFromAccount(item);
		makeTransfer.setCallback(() -> {
			reloadData();
		});
		makeTransfer.show(pane);
	}
	
	private void updateVote() {
		Asset asset = getAssetById(0);
		UpdateVote updateVote = new UpdateVote();
		updateVote.setFromAccount(item);
		updateVote.setToInternalAccount(item);
		updateVote.setAsset(asset);
		updateVote.setVolume(getRealValue(asset, getAvailableAmount(item.getName(), asset) - getTransactionFee(asset)));
		updateVote.setMemo("vote update");
		updateVote.setCallback(() -> {
			reloadData();
		});
		updateVote.show(pane);
	}
	
	private long getVotes(Account account) {
		for (WalletAccountVoteSummary.Result vote : votes) {
			if (vote.getName().equals(account.getName()))
				return vote.getVotes();
		}
		return 0;
	}
}
