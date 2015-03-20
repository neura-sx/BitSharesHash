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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.dto.BurnRecord;
import sx.neura.bts.json.api.blockchain.BlockchainGetAccountWall;
import sx.neura.bts.json.api.blockchain.BlockchainGetFeedsFromDelegate;
import sx.neura.bts.json.api.wallet.WalletAccountSetFavorite;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory;
import sx.neura.bts.json.dto.Account;
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
import sx.neura.btsx.gui.view.pages.bts.comp.flow.BurnRecordFlowRenderer;
import sx.neura.btsx.gui.view.popups.ReadBoolean;
import sx.neura.btsx.gui.view.popups.bts.impl.BurnMessage;
import sx.neura.btsx.gui.view.popups.bts.impl.MakeTransfer;
import sx.neura.btsx.gui.view.popups.bts.impl.ShowPublicKey;

public class TheirAccountDetail extends Page_Bts {
	
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
	
	
	public TheirAccountDetail(Account item) {
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
		
		transactionsUI.setPage(this);
		
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
		
		manageButton(action01UI, (MouseEvent event) -> {
			makeTransfer();
		});
		manageButton(action02UI, (MouseEvent event) -> {
			addToFavorites();
		});
		manageButton(action03UI, (MouseEvent event) -> {
			removeFromFavorites();
		});
		manageButton(action04UI, (MouseEvent event) -> {
			burnMessage();
		});
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
//			nameUI.getTextUI().getStyleClass().remove("registeredName-DelegateActiveAccountSC");
//			nameUI.getTextUI().getStyleClass().remove("registeredName-DelegatePassiveAccountSC");
			nameUI.getTextUI().getStyleClass().remove("registeredName-FavoriteAccountSC");
//			if (isDelegate(item)) {
//				nameUI.getTextUI().getStyleClass().add(isActiveDelegate(item) ?
//        				"registeredName-DelegateActiveAccountSC" : "registeredName-DelegatePassiveAccountSC");
//        	}
			if (item.isIs_favorite()) {
				nameUI.getTextUI().getStyleClass().add("registeredName-FavoriteAccountSC");
				nameUI.setText(String.format("%s%s", item.getName(), "*"));
			} else {
        		nameUI.setText(item.getName());
        	}
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
			action02UI.setVisible(!item.isIs_favorite());
			action03UI.setVisible(item.isIs_favorite());
			
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
	
	private void makeTransfer() {
		MakeTransfer makeTransfer = new MakeTransfer();
		makeTransfer.setToExternalAccount(item);
		makeTransfer.setCallback(() -> {
			reloadData();
		});
		makeTransfer.show(pane);
	}
	
	private void addToFavorites() {
		ReadBoolean readBoolean = new ReadBoolean();
		readBoolean.setTitle("Add to Favorites");
		readBoolean.setMessage(String.format("Are you sure you want to add %s to favorites?", item.getName()));
		readBoolean.setConfirm("Yes");
		readBoolean.setCancel("Cancel");
		readBoolean.setCallback(new ReadBoolean.Callback() {
			@Override
			public void onConfirm() {
				try {
					WalletAccountSetFavorite.run(item.getName(), true);
					item.setIs_favorite(!item.isIs_favorite());
					reloadData();
					FavoriteAccountList.getInstance().reloadData();
				} catch (BTSSystemException e) {
					systemException(e);
				}
			}
			@Override
			public void onCancel() {
			}
		});
		readBoolean.show(pane);
	}
	
	private void removeFromFavorites() {
		ReadBoolean readBoolean = new ReadBoolean();
		readBoolean.setTitle("Remove from Favorites");
		readBoolean.setMessage(String.format("Are you sure you want to remove %s from favorites?", item.getName()));
		readBoolean.setConfirm("Yes");
		readBoolean.setCancel("Cancel");
		readBoolean.setCallback(new ReadBoolean.Callback() {
			@Override
			public void onConfirm() {
				try {
					WalletAccountSetFavorite.run(item.getName(), false);
					item.setIs_favorite(!item.isIs_favorite());
					reloadData();
					FavoriteAccountList.getInstance().reloadData();
				} catch (BTSSystemException e) {
					systemException(e);
				}
			}
			@Override
			public void onCancel() {
			}
		});
		readBoolean.show(pane);
	}
	
	private void burnMessage() {
		BurnMessage burnMessage = new BurnMessage();
		burnMessage.setToAccount(item);
		burnMessage.setCallback(() -> {
			reloadData();
		});
		burnMessage.show(pane);
	}
}
