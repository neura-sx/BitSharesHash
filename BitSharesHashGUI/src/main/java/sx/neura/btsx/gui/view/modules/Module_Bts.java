package sx.neura.btsx.gui.view.modules;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sx.neura.bts.json.api.GetInfo;
import sx.neura.bts.json.api.Ping;
import sx.neura.bts.json.api.blockchain.BlockchainGetSecurityState;
import sx.neura.bts.json.api.blockchain.BlockchainListActiveDelegates;
import sx.neura.bts.json.api.blockchain.BlockchainListAssets;
import sx.neura.bts.json.api.blockchain.BlockchainMarketStatus;
import sx.neura.bts.json.api.network.NetworkGetConnectionCount;
import sx.neura.bts.json.api.wallet.WalletGetSetting;
import sx.neura.bts.json.api.wallet.WalletListAccounts;
import sx.neura.bts.json.api.wallet.WalletLock;
import sx.neura.bts.json.api.wallet.WalletOpen;
import sx.neura.bts.json.api.wallet.WalletUnlock;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.Market;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.dto.AssetPair;
import sx.neura.btsx.gui.view.Module;
import sx.neura.btsx.gui.view.PageManager;
import sx.neura.btsx.gui.view.Screen;
import sx.neura.btsx.gui.view.components.NavigationListView;
import sx.neura.btsx.gui.view.pages.bts.impl.AssetMarketPeggedList;
import sx.neura.btsx.gui.view.pages.bts.impl.AssetUserIssuedList;
import sx.neura.btsx.gui.view.pages.bts.impl.BlockList;
import sx.neura.btsx.gui.view.pages.bts.impl.DelegateActiveList;
import sx.neura.btsx.gui.view.pages.bts.impl.DelegatePassiveList;
import sx.neura.btsx.gui.view.pages.bts.impl.DelegateVotedList;
import sx.neura.btsx.gui.view.pages.bts.impl.FavoriteAccountList;
import sx.neura.btsx.gui.view.pages.bts.impl.MarketList01;
import sx.neura.btsx.gui.view.pages.bts.impl.MarketList02;
import sx.neura.btsx.gui.view.pages.bts.impl.MarketList03;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAccountList;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAmountList;
import sx.neura.btsx.gui.view.pages.bts.impl.RegisteredNameList;
import sx.neura.btsx.gui.view.pages.bts.impl.TransactionList;

public class Module_Bts extends Module {

	private static Module_Bts instance;
	private static final long TIMER_PERIOD = 3000;
	private static Timer timer;

	public static Module_Bts getInstance() {
		if (!isInstance())
			instance = new Module_Bts();
		return instance;
	}

	public static boolean isInstance() {
		return (instance != null);
	}

	private Module_Bts() {
	}

	@FXML
	private Accordion accordionUI;

	@FXML
	private TitledPane choice01UI;
	@FXML
	private TitledPane choice02UI;
	@FXML
	private TitledPane choice03UI;
	@FXML
	private TitledPane choice04UI;


	@FXML
	private NavigationListView navigation01UI;
	@FXML
	private NavigationListView navigation02UI;
	@FXML
	private NavigationListView navigation03UI;
	@FXML
	private NavigationListView navigation04UI;
	
	@FXML
	private Circle statusUI;
	@FXML
	private Label connectionsUI;
	@FXML
	private Label headBlockAgeUI;
	

	private BlockchainGetSecurityState.Result securityState;
	private Integer numberOfConnections;
	private Integer headBlockAge;
	
	@Override
	public String getName() {
		return "BTS";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		statusUI.setFill(Color.WHITE);
		connectionsUI.setText(String.format("connections: %s", "?"));
		headBlockAgeUI.setText(String.format("synced: %s", "?"));
	}

	@Override
	protected void initialize() {
		screens = new ArrayList<Screen>();

		screens.add(new PageManager(this, MyAmountList.getInstance()));
		screens.add(new PageManager(this, MyAccountList.getInstance()));
		screens.add(new PageManager(this, FavoriteAccountList.getInstance()));
		screens.add(new PageManager(this, TransactionList.getInstance()));
		
		screens.add(new PageManager(this, AssetMarketPeggedList.getInstance()));
		screens.add(new PageManager(this, AssetUserIssuedList.getInstance()));
		screens.add(new PageManager(this, RegisteredNameList.getInstance()));
		screens.add(new PageManager(this, BlockList.getInstance()));
		
		screens.add(new PageManager(this, DelegateVotedList.getInstance()));
		screens.add(new PageManager(this, DelegateActiveList.getInstance()));
		screens.add(new PageManager(this, DelegatePassiveList.getInstance()));

		screens.add(new PageManager(this, MarketList01.getInstance()));
		screens.add(new PageManager(this, MarketList02.getInstance()));
		screens.add(new PageManager(this, MarketList03.getInstance()));
		

		screensUI.getChildren().add(loadPage(screens.get(0)));
		screensUI.getChildren().add(loadPage(screens.get(1)));
		screensUI.getChildren().add(loadPage(screens.get(2)));
		screensUI.getChildren().add(loadPage(screens.get(3)));
		screensUI.getChildren().add(loadPage(screens.get(4)));
		screensUI.getChildren().add(loadPage(screens.get(5)));
		screensUI.getChildren().add(loadPage(screens.get(6)));
		screensUI.getChildren().add(loadPage(screens.get(7)));
		screensUI.getChildren().add(loadPage(screens.get(8)));
		screensUI.getChildren().add(loadPage(screens.get(9)));
		screensUI.getChildren().add(loadPage(screens.get(10)));
		screensUI.getChildren().add(loadPage(screens.get(11)));
		screensUI.getChildren().add(loadPage(screens.get(12)));
		screensUI.getChildren().add(loadPage(screens.get(13)));

		ObservableList<Screen> list01 = FXCollections.observableArrayList();
		list01.add(screens.get(0));
		list01.add(screens.get(1));
		list01.add(screens.get(2));
		list01.add(screens.get(3));

		ObservableList<Screen> list02 = FXCollections.observableArrayList();
		list02.add(screens.get(4));
		list02.add(screens.get(5));
		list02.add(screens.get(6));
		list02.add(screens.get(7));
		
		ObservableList<Screen> list03 = FXCollections.observableArrayList();
		list03.add(screens.get(8));
		list03.add(screens.get(9));
		list03.add(screens.get(10));

		ObservableList<Screen> list04 = FXCollections.observableArrayList();
		list04.add(screens.get(11));
		list04.add(screens.get(12));
		list04.add(screens.get(13));

		navigation01UI.setItems(list01);
		navigation01UI.getSelectionModel().select(0);

		navigation02UI.setItems(list02);
		navigation02UI.getSelectionModel().select(0);
		
		navigation03UI.setItems(list03);
		navigation03UI.getSelectionModel().select(0);
		
		navigation04UI.setItems(list04);
		navigation04UI.getSelectionModel().select(0);

		accordionUI.setExpandedPane(choice01UI);
		
		initialize(0);
	}
	
	@Override
	protected void addEventListeners() {
		addListenerForNavigation(navigation01UI);
		addListenerForNavigation(navigation02UI);
		addListenerForNavigation(navigation03UI);
		addListenerForNavigation(navigation04UI);

		accordionUI.expandedPaneProperty().addListener(
				new ChangeListener<TitledPane>() {
					@Override
					public void changed(
							ObservableValue<? extends TitledPane> property,
							final TitledPane oldPane, final TitledPane newPane) {
						if (oldPane != null) 
		        			oldPane.setCollapsible(true);
						if (newPane != null) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									newPane.setCollapsible(false);
								}
							});
							if (newPane == choice01UI) {
								showScreen(navigation01UI);
							} else if (newPane == choice02UI) {
								showScreen(navigation02UI);
							} else if (newPane == choice03UI) {
								showScreen(navigation03UI);
							} else if (newPane == choice04UI) {
								showScreen(navigation04UI);
							} 
						}
					}
				});
	}

	@Override
	protected void loadStaticData() {
		try {
			openWallet();
			Model.getInstance().setAssets(BlockchainListAssets.run());
			Model.getInstance().setAccounts(WalletListAccounts.run());
			Model.getInstance().setActiveDelegates(BlockchainListActiveDelegates.run(0, Model.DELEGATES_ACTIVE_LIMIT));
			
			Asset bts = Model.getInstance().getAssetById(0);
			List<AssetPair> recentMarkets = new ArrayList<AssetPair>();
			String value = WalletGetSetting.run("recent_markets").getValue();
			String[] values = value.substring(1, value.length() - 1).split(",");
			for (String item : values) {
				String[] items = item.substring(1, item.length() - 1).split(":");
				Asset asset1 = getAssetByName(items[0]);
				Asset asset2 = getAssetByName(items[1]);
				if (asset1 != null && asset2 != null && verifyMarket(asset1, asset2))
					recentMarkets.add(new AssetPair(asset1, asset2));
			}
			Model.getInstance().setRecentMarkets(recentMarkets);
			
			List<AssetPair> btsMarkets = new ArrayList<AssetPair>();
			for (Asset asset : Model.getInstance().getAssets()) {
				if (!asset.equals(bts) && verifyMarket(bts, asset))
					btsMarkets.add(new AssetPair(bts, asset));
			}
			Model.getInstance().setBtsMarkets(btsMarkets);
			
			List<AssetPair> bitAssetMarkets = new ArrayList<AssetPair>();
			for (Asset asset1 : Model.getInstance().getAssets()) {
				if (isBitAsset(asset1) && !asset1.equals(bts))
					for (Asset asset2 : Model.getInstance().getAssets())
						if (isBitAsset(asset2) && !asset2.equals(bts) && !asset2.equals(asset1))
							bitAssetMarkets.add(new AssetPair(asset1, asset2));
			}
			Model.getInstance().setBitAssetMarkets(bitAssetMarkets);
			
			List<AssetPair> userAssetMarkets = new ArrayList<AssetPair>();
			for (Asset asset1 : Model.getInstance().getAssets()) {
				if (!isBitAsset(asset1) && !asset1.equals(bts))
					for (Asset asset2 : Model.getInstance().getAssets())
						if (!isBitAsset(asset2) && !asset2.equals(bts) && !asset2.equals(asset1))
							userAssetMarkets.add(new AssetPair(asset1, asset2));
			}
			Model.getInstance().setUserAssetMarkets(userAssetMarkets);
			
		} catch (BTSSystemException e) {
			systemException(e);
		}
	}

	@Override
	protected boolean unlock(String password) {
		try {
			WalletUnlock.run(Model.WALLET_UNLOCK_TIMEOUT, password);
		} catch (BTSUserException e) {
			return false;
		} catch (BTSSystemException e) {
			systemException(e);
			return false;
		}
		pingStart();
		return true;
	}

	@Override
	protected void lock() {
		try {
			WalletLock.run();
		} catch (BTSSystemException e) {
			systemException(e);
			return;
		}
		pingStop();
	}

	@Override
	protected String getHack() {
		return "southpark";
	}

	private void openWallet() {
		try {
			WalletOpen.run();
		} catch (BTSSystemException e) {
			systemException(e);
		}
	}
	
	
	
	@Override
	public void unloadData() {
		pingStop();
	}
	
	private void pingStart() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				updateNetworkStatus();
				Platform.runLater(() -> {
					screens.get(index).ping();
				});
				if (isReloadNeeded()) {
					System.out.println("reloading..");
					Platform.runLater(() -> {
						for (Screen page : screens)
							page.reloadData();
					});
				}
			}
		}, TIMER_PERIOD, TIMER_PERIOD);
		System.out.println("PING: process started");
	}
	
	private void pingStop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
			System.out.println("PING: process stopped");
		}
	}
	
	private void updateNetworkStatus() {
		BlockchainGetSecurityState.Result securityState = null;
		Integer numberOfConnections = null;
		Integer headBlockAge = null;
		try {
			securityState = BlockchainGetSecurityState.run();
			numberOfConnections = NetworkGetConnectionCount.run();
			headBlockAge = GetInfo.run().getBlockchain_head_block_age();
		} catch (BTSSystemException e) {
			systemException(e);
		}
		if (this.securityState == null || !this.securityState.getAlert_level().equals(securityState.getAlert_level())) {
			this.securityState = securityState;
			Platform.runLater(() -> {
				if (this.securityState.getAlert_level().equals("green"))
					statusUI.setFill(Color.LIGHTGREEN);
				else if (this.securityState.getAlert_level().equals("yellow"))
					statusUI.setFill(Color.ORANGE);
				else if (this.securityState.getAlert_level().equals("red"))
					statusUI.setFill(Color.RED);
				else if (this.securityState.getAlert_level().equals("grey"))
					statusUI.setFill(Color.GREY);
				else statusUI.setFill(Color.WHITE);
			});
		}
		if (this.numberOfConnections == null || !this.numberOfConnections.equals(numberOfConnections)) {
			this.numberOfConnections = numberOfConnections;
			Platform.runLater(() -> {
				connectionsUI.setText(String.format("connections: %d", this.numberOfConnections));
			});
		}
		if (this.headBlockAge == null || !this.headBlockAge.equals(headBlockAge)) {
			this.headBlockAge = headBlockAge;
			Platform.runLater(() -> {
				headBlockAgeUI.setText(String.format("synced: %ds", this.headBlockAge));
			});
		}
	}
	
	private boolean isReloadNeeded() {
		try {
			return Ping.run();
		} catch (BTSSystemException e) {
			systemException(e);
		}
		return false;
	}
	
	
	protected static boolean verifyMarket(Asset asset1, Asset asset2) {
		Market market = null;
		try {
			market = BlockchainMarketStatus.run(asset2.getSymbol(), asset1.getSymbol());
    	} catch (BTSSystemException e) {
			return false;
		}
		return (market.getLast_error() == null);
	}
	
	protected static boolean isBitAsset(Asset asset) {
    	return (asset.getIssuer_account_id() == -2);
    }
	
	protected static Asset getAssetByName(String symbol) {
		 return Model.getInstance().getAssetBySymbol(
				 (symbol.length() > 3 && symbol.substring(0, 3).equals("Bit")) ? symbol.substring(3) : symbol);
	}
}
