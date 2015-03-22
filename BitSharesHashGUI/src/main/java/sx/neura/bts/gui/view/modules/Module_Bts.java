package sx.neura.bts.gui.view.modules;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import sx.neura.bts.gui.Main;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.AmountAndAccounts;
import sx.neura.bts.gui.dto.DelegateAnnouncement;
import sx.neura.bts.gui.dto.MarketNews;
import sx.neura.bts.gui.dto.Transaction;
import sx.neura.bts.gui.view.Module;
import sx.neura.bts.gui.view.Page;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.SlidePane;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Asset;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Status;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0100_Payments;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0101_Myself;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0102_Counterparty;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0103_Transactions;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0200_Community;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0201_DelegateAnnouncements;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0202_Delegates;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0300_Exchange;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0301_MarketNews;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0302_Markets;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0400_System;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0401_Assets;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0402_Blocks;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0500_Settings;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0501_Preferences;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0502_Wallet;
import sx.neura.bts.gui.view.pages.bts.impl.Wizard_AddToFavorites;
import sx.neura.bts.gui.view.pages.bts.impl.Wizard_CreateAccount;
import sx.neura.bts.gui.view.pages.bts.impl.Wizard_MakeTransfer;
import sx.neura.bts.gui.view.pages.bts.impl.Wizard_UpdateVote;
import sx.neura.bts.json.api.About;
import sx.neura.bts.json.api.GetInfo;
import sx.neura.bts.json.api.Ping;
import sx.neura.bts.json.api.blockchain.BlockchainGetSecurityState;
import sx.neura.bts.json.api.network.NetworkGetConnectionCount;
import sx.neura.bts.json.api.wallet.WalletLock;
import sx.neura.bts.json.api.wallet.WalletUnlock;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.LabeledOutputStream;
import sx.neura.bts.util.Time;
import sx.neura.bts.util.Util;


public class Module_Bts extends Module {
	
	private static Module_Bts instance;
	
	private static final long TIMER_PERIOD = 3000;
	
	private static final double ASSET_ITEM_WIDTH = 150.0;
	private static final double ACCOUNT_ITEM_WIDTH = 80.0;
	private static final double ITEM_SPACING = 5.0;
	
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
	private Label outputUI;
	
	@FXML
	private ToggleGroup subMenu01ToggleGroupUI;
	@FXML
	private ToggleGroup subMenu02ToggleGroupUI;
	@FXML
	private ToggleGroup subMenu03ToggleGroupUI;
	@FXML
	private ToggleGroup subMenu04ToggleGroupUI;
	@FXML
	private ToggleGroup subMenu05ToggleGroupUI;
	
	@FXML
	private LayerPane subAbout01UI;
	@FXML
	private LayerPane subAbout02UI;
	@FXML
	private LayerPane subAbout03UI;
	@FXML
	private LayerPane subAbout04UI;
	@FXML
	private LayerPane subAbout05UI;
	
	@FXML
	private SlidePane myAssetsSliderUI;
	@FXML
	private Button myAssetsScrollFwdUI;
	@FXML
	private Button myAssetsScrollBckUI;
	@FXML
	private GridPane myAssetsUI;
	
	@FXML
	private SlidePane myAccountsSliderUI;
	@FXML
	private Button myAccountsScrollFwdUI;
	@FXML
	private Button myAccountsScrollBckUI;
	@FXML
	private GridPane myAccountsUI;
	
	@FXML
	private SlidePane favoritesSliderUI;
	@FXML
	private Button favoritesScrollFwdUI;
	@FXML
	private Button favoritesScrollBckUI;
	@FXML
	private GridPane favoritesUI;
	
	@FXML
	private Pane transactionsUI;
	@FXML
	private Pane delegateAnnouncementsUI;
	@FXML
	private Pane marketNewsUI;
	@FXML
	private Label connectionsUI;
	@FXML
	private Label headBlockAgeUI;
	
	@FXML
	private Circle statusUI;
	
	
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
		
		Main.setPrintStream(LabeledOutputStream.getPrintStream(outputUI));
		
		statusUI.visibleProperty().bind(workspaceUI.getIndexProperty().greaterThan(0));
		
		Util.manageToggleGroup(subMenu01ToggleGroupUI, () -> hideMenu());
		Util.manageToggleGroup(subMenu02ToggleGroupUI, () -> hideMenu());
		Util.manageToggleGroup(subMenu03ToggleGroupUI, () -> hideMenu());
		Util.manageToggleGroup(subMenu04ToggleGroupUI, () -> hideMenu());
		Util.manageToggleGroup(subMenu05ToggleGroupUI, () -> hideMenu());
		
		subMenu01ToggleGroupUI.selectedToggleProperty().addListener(
				(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
					subAbout01UI.setIndex(subMenu01ToggleGroupUI.getToggles().indexOf(subMenu01ToggleGroupUI.getSelectedToggle()));
		});
		subMenu02ToggleGroupUI.selectedToggleProperty().addListener(
				(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
					subAbout02UI.setIndex(subMenu02ToggleGroupUI.getToggles().indexOf(subMenu02ToggleGroupUI.getSelectedToggle()));
		});
		subMenu03ToggleGroupUI.selectedToggleProperty().addListener(
				(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
					subAbout03UI.setIndex(subMenu03ToggleGroupUI.getToggles().indexOf(subMenu03ToggleGroupUI.getSelectedToggle()));
		});
		subMenu04ToggleGroupUI.selectedToggleProperty().addListener(
				(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
					subAbout04UI.setIndex(subMenu04ToggleGroupUI.getToggles().indexOf(subMenu04ToggleGroupUI.getSelectedToggle()));
		});
		subMenu05ToggleGroupUI.selectedToggleProperty().addListener(
				(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
					subAbout05UI.setIndex(subMenu05ToggleGroupUI.getToggles().indexOf(subMenu05ToggleGroupUI.getSelectedToggle()));
		});
		
		menuToggleGroupUI.getToggles().get(0).setUserData(subMenu01ToggleGroupUI);
		menuToggleGroupUI.getToggles().get(1).setUserData(subMenu02ToggleGroupUI);
		menuToggleGroupUI.getToggles().get(2).setUserData(subMenu03ToggleGroupUI);
		menuToggleGroupUI.getToggles().get(3).setUserData(subMenu04ToggleGroupUI);
		menuToggleGroupUI.getToggles().get(4).setUserData(subMenu05ToggleGroupUI);
		
		subMenu01ToggleGroupUI.getToggles().get(0).setUserData(new ToggleMap(0, menuToggleGroupUI.getToggles().get(0)));
		subMenu01ToggleGroupUI.getToggles().get(1).setUserData(new ToggleMap(1, menuToggleGroupUI.getToggles().get(0)));
		subMenu01ToggleGroupUI.getToggles().get(2).setUserData(new ToggleMap(2, menuToggleGroupUI.getToggles().get(0)));

		subMenu02ToggleGroupUI.getToggles().get(0).setUserData(new ToggleMap(3, menuToggleGroupUI.getToggles().get(1)));
		subMenu02ToggleGroupUI.getToggles().get(1).setUserData(new ToggleMap(4, menuToggleGroupUI.getToggles().get(1)));

		subMenu03ToggleGroupUI.getToggles().get(0).setUserData(new ToggleMap(5, menuToggleGroupUI.getToggles().get(2)));
		subMenu03ToggleGroupUI.getToggles().get(1).setUserData(new ToggleMap(6, menuToggleGroupUI.getToggles().get(2)));

		subMenu04ToggleGroupUI.getToggles().get(0).setUserData(new ToggleMap(7, menuToggleGroupUI.getToggles().get(3)));
		subMenu04ToggleGroupUI.getToggles().get(1).setUserData(new ToggleMap(8, menuToggleGroupUI.getToggles().get(3)));

		subMenu05ToggleGroupUI.getToggles().get(0).setUserData(new ToggleMap(9, menuToggleGroupUI.getToggles().get(4)));
		subMenu05ToggleGroupUI.getToggles().get(1).setUserData(new ToggleMap(10, menuToggleGroupUI.getToggles().get(4)));
		
		myAssetsSliderUI.setItemWidth(ASSET_ITEM_WIDTH + ITEM_SPACING);
		myAssetsSliderUI.setNodeFwd(myAssetsScrollFwdUI);
		myAssetsSliderUI.setNodeBck(myAssetsScrollBckUI);
		
		myAccountsSliderUI.setItemWidth(ACCOUNT_ITEM_WIDTH + ITEM_SPACING);
		myAccountsSliderUI.setNodeFwd(myAccountsScrollFwdUI);
		myAccountsSliderUI.setNodeBck(myAccountsScrollBckUI);
		
		favoritesSliderUI.setItemWidth(ACCOUNT_ITEM_WIDTH + ITEM_SPACING);
		favoritesSliderUI.setNodeFwd(favoritesScrollFwdUI);
		favoritesSliderUI.setNodeBck(favoritesScrollBckUI);

		statusUI.setFill(Color.WHITE);
		connectionsUI.setText(String.format("connections: %s", "?"));
		headBlockAgeUI.setText(String.format("synced: %s", "?"));
	}
	
	
	
	@Override
	protected void initialize(Runnable runnable) {
		animateLoadingProgress(() -> {
			animateLoadingProgress(0.2, () -> {
				try {
					Model.getInstance().initialize();
				} catch (BTSSystemException e) {
					systemException(e);
					return;
				}
				animateLoadingProgress(0.3, () -> {
					initialize01();
					animateLoadingProgress(0.4, () -> {
						initialize02();
						animateLoadingProgress(0.5, () -> {
							initialize03();
							animateLoadingProgress(0.6, () -> {
								initialize04();
								animateLoadingProgress(0.8, () -> {
									initialize05();
									animateLoadingProgress(1.0, () -> {
										progressTrackUI.setVisible(false);
										progressBarUI.setVisible(false);
										selectToggle(menuToggleGroupUI, 0);
										showOverview();
										doLoadData(true);
										runnable.run();
									});
								});
							});
						});
					});
				});
			});
		});
//		{
//			List<Page> peers = new ArrayList<Page>();
//			addSubPage(peers, Page_0101_Myself.getInstance(), subMenu01ToggleGroupUI.getToggles().get(0));
//			addSubPage(peers, Page_0102_Counterparty.getInstance(), subMenu01ToggleGroupUI.getToggles().get(1));
//			addSubPage(peers, Page_0103_Transactions.getInstance(), subMenu01ToggleGroupUI.getToggles().get(2));
//			addPage(Page_0100_Payments.getInstance(), peers);
//		}
//		{
//			List<Page> peers = new ArrayList<Page>();
//			addSubPage(peers, Page_0201_DelegateAnnouncements.getInstance(), subMenu02ToggleGroupUI.getToggles().get(0));
//			addSubPage(peers, Page_0202_Delegates.getInstance(), subMenu02ToggleGroupUI.getToggles().get(1));
//			addPage(Page_0200_Community.getInstance(), peers);
//		}
//		{
//			List<Page> peers = new ArrayList<Page>();
//			addSubPage(peers, Page_0301_MarketNews.getInstance(), subMenu03ToggleGroupUI.getToggles().get(0));
//			addSubPage(peers, Page_0302_Markets.getInstance(), subMenu03ToggleGroupUI.getToggles().get(1));
//			addPage(Page_0300_Exchange.getInstance(), peers);
//		}
//		{
//			List<Page> peers = new ArrayList<Page>();
//			addSubPage(peers, Page_0401_Assets.getInstance(), subMenu04ToggleGroupUI.getToggles().get(0));
//			addSubPage(peers, Page_0402_Blocks.getInstance(), subMenu04ToggleGroupUI.getToggles().get(1));
//			addPage(Page_0400_System.getInstance(), peers);
//		}
//		{
//			List<Page> peers = new ArrayList<Page>();
//			addSubPage(peers, Page_0501_Preferences.getInstance(), subMenu05ToggleGroupUI.getToggles().get(0));
//			addSubPage(peers, Page_0502_Wallet.getInstance(), subMenu05ToggleGroupUI.getToggles().get(1));
//			addPage(Page_0500_Settings.getInstance(), peers);
//		}
//		selectToggle(menuToggleGroupUI, 0);
//		showOverview();
//		doLoadData(true);
	}
	
	private void initialize01() {
		List<Page> peers = new ArrayList<Page>();
		addSubPage(peers, Page_0101_Myself.getInstance(), subMenu01ToggleGroupUI.getToggles().get(0));
		addSubPage(peers, Page_0102_Counterparty.getInstance(), subMenu01ToggleGroupUI.getToggles().get(1));
		addSubPage(peers, Page_0103_Transactions.getInstance(), subMenu01ToggleGroupUI.getToggles().get(2));
		addPage(Page_0100_Payments.getInstance(), peers);
	}
	private void initialize02() {
		List<Page> peers = new ArrayList<Page>();
		addSubPage(peers, Page_0201_DelegateAnnouncements.getInstance(), subMenu02ToggleGroupUI.getToggles().get(0));
		addSubPage(peers, Page_0202_Delegates.getInstance(), subMenu02ToggleGroupUI.getToggles().get(1));
		addPage(Page_0200_Community.getInstance(), peers);
	}
	private void initialize03() {
		List<Page> peers = new ArrayList<Page>();
		addSubPage(peers, Page_0301_MarketNews.getInstance(), subMenu03ToggleGroupUI.getToggles().get(0));
		addSubPage(peers, Page_0302_Markets.getInstance(), subMenu03ToggleGroupUI.getToggles().get(1));
		addPage(Page_0300_Exchange.getInstance(), peers);
	}
	private void initialize04() {
		List<Page> peers = new ArrayList<Page>();
		addSubPage(peers, Page_0401_Assets.getInstance(), subMenu04ToggleGroupUI.getToggles().get(0));
		addSubPage(peers, Page_0402_Blocks.getInstance(), subMenu04ToggleGroupUI.getToggles().get(1));
		addPage(Page_0400_System.getInstance(), peers);
	}
	private void initialize05() {
		List<Page> peers = new ArrayList<Page>();
		addSubPage(peers, Page_0501_Preferences.getInstance(), subMenu05ToggleGroupUI.getToggles().get(0));
		addSubPage(peers, Page_0502_Wallet.getInstance(), subMenu05ToggleGroupUI.getToggles().get(1));
		addPage(Page_0500_Settings.getInstance(), peers);
	}
	
	private void addPage(Page page, List<Page> peers) {
		pages.add(page);
		page.setModule(this);
		page.setPeers(peers);
		pagesUI.getChildren().add(loadScreen(page));
		page.loadUI();
		page.loadData();
	}
	
	private void addSubPage(List<Page> peers, Page page, Toggle toggle) {
		subPages.add(page);
		peers.add(page);
		page.setModule(this);
		page.setToggle(toggle);
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
	
	@Override
	protected void onApplicationExit() {
		Model.getInstance().closeWallet();
	}
	
	private void doLoadData(boolean initial) {
		if (initial)
			popoulateBtsVersion();
		populateMyAssets();
		populateMyAccounts();
		populateFavorites();
		populateTransactions();
		populateDelegateAnnouncements();
		populateMarketNews();
	}
	
	@Override
	public void reloadData() {
		doLoadData(false);
	}
	
	@Override
	public void unloadData() {
		pingStop();
		super.unloadData();
	}
	
	private void pingStart() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!updateNetworkStatus())
					return;
				Platform.runLater(() -> {
					if (isInitialized())
						getCurrentPage().ping();
					if (!stack.isEmpty())
						stack.get(stack.size() - 1).ping();
				});
				if (isReloadNeeded()) {
					System.out.println("reloading..");
					try {
						Model.getInstance().redoAccounts();
						Model.getInstance().redoBalance();
						Model.getInstance().redoTransactions();
					} catch (BTSSystemException e) {
						Platform.runLater(() -> systemException(e));
					}
					Platform.runLater(() -> {
						reloadData();
						for (Page page : subPages)
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
	
	private boolean updateNetworkStatus() {
		if (!Model.getInstance().isWalletOpen()) {
			try {
				Model.getInstance().openWallet();
			} catch (BTSSystemException e) {
				return false;
			}
			Platform.runLater(() -> lockWorkspace());
		}
		BlockchainGetSecurityState.Result securityState;
		Integer numberOfConnections;
		Integer headBlockAge;
		try {
			securityState = BlockchainGetSecurityState.run();
			numberOfConnections = NetworkGetConnectionCount.run();
			headBlockAge = GetInfo.run().getBlockchain_head_block_age();
		} catch (BTSSystemException e) {
			Model.getInstance().setWalletOpen(false);
			this.securityState = null;
			this.numberOfConnections = null;
			this.headBlockAge = null;
			Platform.runLater(() -> {
				statusUI.setFill(Color.WHITE);
				connectionsUI.setText("");
				headBlockAgeUI.setText("Unavailable due to API error");
			});
			return false;
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
		return true;
	}
	
	private boolean isReloadNeeded() {
		try {
			return Ping.run();
		} catch (BTSSystemException e) {
			//Platform.runLater(() -> systemException(e));
		}
		return false;
	}
	
	@Override
	protected Page getDefaultPage() {
		return Page_0101_Myself.getInstance();
	}
	
	@FXML
	private void onMakeTransfer() {
		jump(new Wizard_MakeTransfer());
	}
	@FXML
	private void onAddToFavorites() {
		jump(new Wizard_AddToFavorites());
	}
	@FXML
	private void onCreateAccount() {
		jump(new Wizard_CreateAccount());
	}
	@FXML
	private void onUpdateVote() {
		jump(new Wizard_UpdateVote());
	}
	
	@FXML
	private void onMyAssets() {
		if (!isInitialized()) {
			togglePage(Page_0101_Myself.getInstance());
			hideOverview(() -> Page_0101_Myself.getInstance().setPanoramaIndex(0));
			return;
		}
		hideOverview(() -> {
			togglePage(Page_0101_Myself.getInstance(),
					() -> Page_0101_Myself.getInstance().setPanoramaIndex(0));
		});
	}
	@FXML
	private void onMyAccounts() {
		if (!isInitialized()) {
			togglePage(Page_0101_Myself.getInstance());
			hideOverview(() -> Page_0101_Myself.getInstance().setPanoramaIndex(1));
			return;
		}
		hideOverview(() -> {
			togglePage(Page_0101_Myself.getInstance(),
					() -> Page_0101_Myself.getInstance().setPanoramaIndex(1));
		});
	}
	@FXML
	private void onFavorites() {
		if (!isInitialized()) {
			togglePage(Page_0102_Counterparty.getInstance());
			hideOverview(() -> Page_0102_Counterparty.getInstance().setPanoramaIndex(0));
			return;
		}
		hideOverview(() -> {
			togglePage(Page_0102_Counterparty.getInstance(),
					() -> Page_0102_Counterparty.getInstance().setPanoramaIndex(0));
		});
	}
	@FXML
	private void onTransactions() {
		if (!isInitialized()) {
			togglePage(Page_0103_Transactions.getInstance());
			hideOverview();
			return;
		}
		hideOverview(() -> togglePage(Page_0103_Transactions.getInstance()));
	}
	@FXML
	private void onDelegates() {
		if (!isInitialized()) {
			togglePage(Page_0202_Delegates.getInstance());
			hideOverview();
			return;
		}
		hideOverview(() -> togglePage(Page_0202_Delegates.getInstance()));
	}
	@FXML
	private void onDelegateAnnouncements() {
		if (!isInitialized()) {
			togglePage(Page_0201_DelegateAnnouncements.getInstance());
			hideOverview();
			return;
		}
		hideOverview(() -> togglePage(Page_0201_DelegateAnnouncements.getInstance()));
	}
	@FXML
	private void onMarkets() {
		if (!isInitialized()) {
			togglePage(Page_0302_Markets.getInstance());
			hideOverview();
			return;
		}
		hideOverview(() -> togglePage(Page_0302_Markets.getInstance()));
	}
	@FXML
	private void onMarketNews() {
		if (!isInitialized()) {
			togglePage(Page_0301_MarketNews.getInstance());
			hideOverview();
			return;
		}
		hideOverview(() -> togglePage(Page_0301_MarketNews.getInstance()));
	}
	@FXML
	private void onNetworkStatus() {
		jump(new Details_Status());
	}
	
	private void popoulateBtsVersion() {
		String clientVersion;
		try {
			clientVersion = About.run().getClient_version();
		} catch (BTSSystemException e) {
			systemException(e);
			return;
		}
		clientVersionUI.setText(String.format("%s %s", "version", clientVersion));
	}
	
	private void populateMyAssets() {
		int size = Model.getInstance().getMyAssetSplit().size();
		myAssetsUI.getChildren().clear();
		myAssetsUI.getColumnConstraints().clear();
		myAssetsSliderUI.setListSize(size);
		for (int i = 0; i < size; i++) {
			ColumnConstraints c = new ColumnConstraints();
			c.setHgrow(Priority.ALWAYS);
			c.setMinWidth(10);
			myAssetsUI.getColumnConstraints().add(c);
		}
		for (int i = 0; i < size ; i++) {
			AmountAndAccounts item = Model.getInstance().getMyAssetSplit().get(i);
			AnchorPane box = new AnchorPane();
			box.setPrefWidth(ASSET_ITEM_WIDTH);
			Label icon = new Label();
			icon.getStyleClass().add("sx-text-icon-asset-overview");
			AnchorPane.setLeftAnchor(icon, 0.0);
			AnchorPane.setTopAnchor(icon, 0.0);
			icon.setText(item.getAsset().getSymbol());
			icon.setOnMouseClicked((MouseEvent event) -> { 
				jump(new Details_Asset(item.getAsset()));
				event.consume();
	    	});
			box.getChildren().add(icon);
			Label amount = new Label();
			amount.getStyleClass().add("sx-text-express-big");
			AnchorPane.setLeftAnchor(amount, 3.0);
			AnchorPane.setTopAnchor(amount, 62.0);
			amount.setText(Model.getInstance().getAmount(item.getAsset(), item.getAmount()));
			amount.setMouseTransparent(true);
			box.getChildren().add(amount);
			myAssetsUI.getChildren().add(box);
			
			box.setOnDragDetected((MouseEvent event) -> {
				Dragboard db = box.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putString(item.getAsset().getSymbol());
				db.setContent(content);
				db.setDragView(icon.snapshot(null, null), 10.0, 10.0);
				event.consume();
			});
			box.setOnDragDone((DragEvent event) -> {});
			box.setOnDragOver((DragEvent event) -> {
				if (event.getDragboard().hasString() && event.getDragboard().hasUrl())
					event.acceptTransferModes(TransferMode.ANY);
			});
			box.setOnDragEntered((DragEvent event) -> {
				if (event.getDragboard().hasString() && event.getDragboard().hasUrl())
					box.setId("sx-drag-accept");
			});
			box.setOnDragExited((DragEvent event) -> {
				box.setId(null);
			});
			box.setOnDragDropped((DragEvent event) -> {
				Dragboard db = event.getDragboard();
				if (db.hasString()) {
					makeTransferFromMyAccount(item.getAsset(), Model.getInstance().getAccountByName(db.getString()));
					event.setDropCompleted(true);
				} else {
					event.setDropCompleted(false);
				}
			});
			GridPane.setColumnIndex(box, i);
			GridPane.setHalignment(box, HPos.LEFT);
		}
	}
	
	private void populateMyAccounts() {
		int size = Model.getInstance().getMyAccounts().size();
		myAccountsUI.getChildren().clear();
		myAccountsUI.getColumnConstraints().clear();
		myAccountsSliderUI.setListSize(size);
		for (int i = 0; i < size; i++) {
			ColumnConstraints c = new ColumnConstraints();
			c.setHgrow(Priority.ALWAYS);
			c.setMinWidth(10);
			myAccountsUI.getColumnConstraints().add(c);
		}
		for (int i = 0; i < size ; i++) {
			Account item = Model.getInstance().getMyAccounts().get(i);
			AnchorPane box = new AnchorPane();
			box.getStyleClass().add("sx-cell");
			box.getStyleClass().add("sx-cell-box");
			box.setPrefWidth(ACCOUNT_ITEM_WIDTH);
			box.setPrefHeight(100.0);
			IdenticonCanvas avatar = new IdenticonCanvas();
			avatar.setWidth(70.0);
			avatar.setHeight(70.0);
			//avatar.setPadding(1.0);
			avatar.setName(item.getName());
//			avatar.setPreserveRatio(true);
//			avatar.setSmooth(true);
//			avatar.setFitWidth(ACCOUNT_ITEM_WIDTH);
//			avatar.setFitHeight(ACCOUNT_ITEM_WIDTH);
//			avatar.setImage(Model.getInstance().getAvatarImage(item.getName()));
			AnchorPane.setLeftAnchor(avatar, 5.0);
			AnchorPane.setTopAnchor(avatar, 5.0);
			box.setOnMouseClicked((MouseEvent event) -> { 
				jump(new Details_Account(item));
				event.consume();
	    	});
			box.getChildren().add(avatar);
			Label name = new Label();
			name.getStyleClass().add("sx-text-small");
			AnchorPane.setLeftAnchor(name, 5.0);
			AnchorPane.setRightAnchor(name, 5.0);
			AnchorPane.setBottomAnchor(name, 5.0);
			name.setText(Util.crop(item.getName(), 24));
			box.getChildren().add(name);
			myAccountsUI.getChildren().add(box);
			
			box.setOnDragDetected((MouseEvent event) -> {
				Dragboard db = box.startDragAndDrop(TransferMode.ANY);
				ClipboardContent content = new ClipboardContent();
				content.putString(item.getName());
				content.putUrl(item.getName());
				db.setContent(content);
				db.setDragView(avatar.snapshot(null, null), 10.0, 10.0);
				event.consume();
			});
			box.setOnDragDone((DragEvent event) -> {});
			
			box.setOnDragOver((DragEvent event) -> {
				if (event.getDragboard().hasString() && !event.getDragboard().hasUrl())
					event.acceptTransferModes(TransferMode.ANY);
			});
			box.setOnDragEntered((DragEvent event) -> {
				if (event.getDragboard().hasString() && !event.getDragboard().hasUrl())
					box.setId("sx-drag-accept");
			});
			box.setOnDragExited((DragEvent event) -> {
				box.setId(null);
			});
			box.setOnDragDropped((DragEvent event) -> {
				Dragboard db = event.getDragboard();
				if (db.hasString()) {
					makeTransferFromMyAccount(Model.getInstance().getAssetBySymbol(db.getString()), item);
					event.setDropCompleted(true);
				} else {
					event.setDropCompleted(false);
				}
			});
			
			GridPane.setColumnIndex(box, i);
			GridPane.setHalignment(box, HPos.LEFT);
		}
	}
	
	private void populateFavorites() {
		int size = Model.getInstance().getFavoriteAccounts().size();
		favoritesUI.getChildren().clear();
		favoritesUI.getColumnConstraints().clear();
		favoritesSliderUI.setListSize(size);
		for (int i = 0; i < size; i++) {
			ColumnConstraints c = new ColumnConstraints();
			c.setHgrow(Priority.ALWAYS);
			c.setMinWidth(10);
			favoritesUI.getColumnConstraints().add(c);
		}
		for (int i = 0; i < size ; i++) {
			Account item = Model.getInstance().getFavoriteAccounts().get(i);
			AnchorPane box = new AnchorPane();
			box.getStyleClass().add("sx-cell");
			box.getStyleClass().add("sx-cell-box");
			box.setPrefWidth(ACCOUNT_ITEM_WIDTH);
			box.setPrefHeight(100.0);
			IdenticonCanvas avatar = new IdenticonCanvas();
			avatar.setWidth(70.0);
			avatar.setHeight(70.0);
			//avatar.setPadding(1.0);
			avatar.setName(item.getName());
//			ImageView avatar = new ImageView();
//			avatar.setPreserveRatio(true);
//			avatar.setSmooth(true);
//			avatar.setFitWidth(ACCOUNT_ITEM_WIDTH);
//			avatar.setFitHeight(ACCOUNT_ITEM_WIDTH);
//			avatar.setImage(Model.getInstance().getAvatarImage(item.getName()));
			AnchorPane.setLeftAnchor(avatar, 10.0);
			AnchorPane.setTopAnchor(avatar, 5.0);
			box.setOnMouseClicked((MouseEvent event) -> { 
				jump(new Details_Account(item));
				event.consume();
	    	});
			box.getChildren().add(avatar);
			Label name = new Label();
			name.getStyleClass().add("sx-text-small");
			AnchorPane.setLeftAnchor(name, 10.0);
			AnchorPane.setRightAnchor(name, 5.0);
			AnchorPane.setBottomAnchor(name, 5.0);
			name.setText(Util.crop(item.getName(), 24));
			box.getChildren().add(name);
			favoritesUI.getChildren().add(box);
			
			box.setOnDragOver((DragEvent event) -> {
				if (event.getDragboard().hasString())
					event.acceptTransferModes(TransferMode.ANY);
			});
			box.setOnDragEntered((DragEvent event) -> {
				if (event.getDragboard().hasString())
					box.setId("sx-drag-accept");
			});
			box.setOnDragExited((DragEvent event) -> {
				box.setId(null);
			});
			box.setOnDragDropped((DragEvent event) -> {
				Dragboard db = event.getDragboard();
				if (db.hasString()) {
					if (db.hasUrl())
						makeTransferToFavorite(Model.getInstance().getAccountByName(db.getString()), item);
					else
						makeTransferToFavorite(Model.getInstance().getAssetBySymbol(db.getString()), item);
					event.setDropCompleted(true);
				} else {
					event.setDropCompleted(false);
				}
			});
			
			GridPane.setColumnIndex(box, i);
			GridPane.setHalignment(box, HPos.LEFT);
		}
	}
	
	private void populateTransactions() {
		transactionsUI.getChildren().clear();
		final int max = Math.min(Model.getInstance().getTransactions().size(), 3);
		for (int i = 0; i < max; i++) {
			Transaction item = Model.getInstance().getTransactions().get(i);
			GridPane box = new GridPane();
			double[] columns = new double[]{25.0, 35.0, 15.0, 25.0};
			for (double column : columns) {
				ColumnConstraints c = new ColumnConstraints();
				c.setPercentWidth(column);
				c.setHgrow(Priority.ALWAYS);
				c.setMinWidth(10);
				box.getColumnConstraints().add(c);
			}
			RowConstraints c = new RowConstraints();
			c.setMinHeight(10);
			box.getRowConstraints().add(c);
			
			Label column01 = new Label();
			column01.getStyleClass().add("sx-text-small");
			column01.setText(Time.format(item.getTimestamp(), Time.Format.DATE_AND_TIME_MEDIUM_FORMAT));
			GridPane.setColumnIndex(column01, 0);
			
			Label column02 = new Label();
			column02.getStyleClass().add("sx-text-small");
			column02.setText(String.format("%s >> %s",
					item.getFrom_account(), item.getTo_account()));
			GridPane.setColumnIndex(column02, 1);
			
			Label column03 = new Label();
			column03.getStyleClass().add("sx-text-small");
			String[] pair = Model.getInstance().getAmountPair(item.getAmount());
			column03.setText(String.format("%s %s", pair[0], pair[1]));
			GridPane.setColumnIndex(column03, 2);
			
			Label column04 = new Label();
			column04.getStyleClass().add("sx-text-small");
			column04.setText(item.getMemo());
			GridPane.setColumnIndex(column04, 3);
			
			box.getChildren().addAll(column01, column02, column03, column04);
			transactionsUI.getChildren().add(box);
		}
	}
	
	private void populateDelegateAnnouncements() {
		delegateAnnouncementsUI.getChildren().clear();
		final int max = Math.min(Model.getInstance().getDelegateAnnouncements().size(), 3);
		for (int i = 0; i < max; i++) {
			DelegateAnnouncement item = Model.getInstance().getDelegateAnnouncements().get(i);
			GridPane box = new GridPane();
			double[] columns = new double[]{25.0, 20.0, 55.0};
			for (double column : columns) {
				ColumnConstraints c = new ColumnConstraints();
				c.setPercentWidth(column);
				c.setHgrow(Priority.ALWAYS);
				c.setMinWidth(10);
				box.getColumnConstraints().add(c);
			}
			RowConstraints c = new RowConstraints();
			c.setMinHeight(10);
			box.getRowConstraints().add(c);
			
			Label column01 = new Label();
			column01.getStyleClass().add("sx-text-small");
			column01.setText(Time.format(item.getTimestamp(), Time.Format.DATE_AND_TIME_MEDIUM_FORMAT));
			GridPane.setColumnIndex(column01, 0);
			
			Label column02 = new Label();
			column02.getStyleClass().add("sx-text-small");
			column02.setText(item.getDelegate());
			GridPane.setColumnIndex(column02, 1);
			
			Label column03 = new Label();
			column03.getStyleClass().add("sx-text-small");
			column03.setText(item.getText());
			GridPane.setColumnIndex(column03, 2);
			
			box.getChildren().addAll(column01, column02, column03);
			delegateAnnouncementsUI.getChildren().add(box);
		}
	}
	
	private void populateMarketNews() {
		marketNewsUI.getChildren().clear();
		final int max = Math.min(Model.getInstance().getMarketNews().size(), 3);
		for (int i = 0; i < max; i++) {
			MarketNews item = Model.getInstance().getMarketNews().get(i);
			GridPane box = new GridPane();
			double[] columns = new double[]{25.0, 20.0, 55.0};
			for (double column : columns) {
				ColumnConstraints c = new ColumnConstraints();
				c.setPercentWidth(column);
				c.setHgrow(Priority.ALWAYS);
				c.setMinWidth(10);
				box.getColumnConstraints().add(c);
			}
			RowConstraints c = new RowConstraints();
			c.setMinHeight(10);
			box.getRowConstraints().add(c);
			
			Label column01 = new Label();
			column01.getStyleClass().add("sx-text-small");
			column01.setText(Time.format(item.getTimestamp(), Time.Format.DATE_AND_TIME_MEDIUM_FORMAT));
			GridPane.setColumnIndex(column01, 0);
			
			Label column02 = new Label();
			column02.getStyleClass().add("sx-text-small");
			column02.setText(Model.getInstance().getMarketLabel(item.getMarket()));
			GridPane.setColumnIndex(column02, 1);
			
			Label column03 = new Label();
			column03.getStyleClass().add("sx-text-small");
			column03.setText(item.getText());
			GridPane.setColumnIndex(column03, 2);
			
			box.getChildren().addAll(column01, column02, column03);
			marketNewsUI.getChildren().add(box);
		}
	}
	
	private void makeTransferFromMyAccount(Asset asset, Account myAccount) {
		Wizard_MakeTransfer wizard = new Wizard_MakeTransfer();
		wizard.setAsset(asset);
		wizard.setFromAccount(myAccount);
		jump(wizard);
	}
	
	private void makeTransferToFavorite(Asset asset, Account favoriteAccount) {
		Wizard_MakeTransfer wizard = new Wizard_MakeTransfer();
		wizard.setAsset(asset);
		wizard.setToFavoriteAccount(favoriteAccount);
		jump(wizard);
	}
	
	private void makeTransferToFavorite(Account myAccount, Account favoriteAccount) {
		Wizard_MakeTransfer wizard = new Wizard_MakeTransfer();
		wizard.setFromAccount(myAccount);
		wizard.setToFavoriteAccount(favoriteAccount);
		jump(wizard);
	}

}
