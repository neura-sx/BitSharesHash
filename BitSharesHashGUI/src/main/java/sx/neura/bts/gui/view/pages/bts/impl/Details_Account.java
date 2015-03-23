package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.AccountAndAmounts;
import sx.neura.bts.gui.dto.AmountAndAccount;
import sx.neura.bts.gui.dto.BurnRecord;
import sx.neura.bts.gui.dto.Transaction;
import sx.neura.bts.gui.dto.Vote;
import sx.neura.bts.gui.view.Page;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.MenuPane;
import sx.neura.bts.gui.view.components.Pagination;
import sx.neura.bts.gui.view.components.SearchBox;
import sx.neura.bts.gui.view.components.display.DisplayDuet;
import sx.neura.bts.gui.view.components.display.DisplayField;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.PageDetails_Bts;
import sx.neura.bts.gui.view.pages.bts.comp.tile.AmountAndAccountTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.BurnRecordTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.MarketFeedByAssetTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.TransactionTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.VoteTile;
import sx.neura.bts.gui.view.popups.impl.bts.ToggleApproval;
import sx.neura.bts.gui.view.popups.impl.bts.ToggleFavorite;
import sx.neura.bts.json.api.blockchain.BlockchainGetAccountWall;
import sx.neura.bts.json.api.blockchain.BlockchainGetFeedsFromDelegate;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory;
import sx.neura.bts.json.api.wallet.WalletAccountVoteSummary;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.MarketFeed;
import sx.neura.bts.json.dto.RegisteredName;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Time;
import sx.neura.bts.util.Util;

public class Details_Account extends PageDetails_Bts<Account> {
	
	@FXML
	private Node myAccountUI;
	@FXML
	private Label accountTypeUI;
	@FXML
	private Label unregisteredUI;
	
	@FXML
	private IdenticonCanvas avatarUI;
	
	@FXML
	private Node favoriteNeutralUI;
	@FXML
	private Node favoritePositiveUI;
	
	@FXML
	private Node approvalNeutralUI;
	@FXML
	private Node approvalNegativeUI;
	@FXML
	private Node approvalPositiveUI;
	
	
	@FXML
	private MenuPane panoramaMenuUI;
	@FXML
	private LayerPane panoramaUI;
	
	@FXML
	private MenuPane panoramaMenu03UI;
	@FXML
	private LayerPane panorama03UI;
	
	@FXML
	private ToggleGroup panoramaToggleGroupUI;
	
	@FXML
	private Pagination pagination01UI;
	@FXML
	private Label pagination01StatusUI;
	@FXML
	private Node pagination01FwdUI;
	@FXML
	private Node pagination01BckUI;
	
	@FXML
	private Pagination pagination02UI;
	@FXML
	private Label pagination02StatusUI;
	@FXML
	private Node pagination02FwdUI;
	@FXML
	private Node pagination02BckUI;
	
	@FXML
	private Pagination pagination04UI;
	@FXML
	private Label pagination04StatusUI;
	@FXML
	private Node pagination04FwdUI;
	@FXML
	private Node pagination04BckUI;
	
	@FXML
	private Pagination pagination05UI;
	@FXML
	private Label pagination05StatusUI;
	@FXML
	private Node pagination05FwdUI;
	@FXML
	private Node pagination05BckUI;
	
	
	@FXML
	private Pagination pagination06UI;
	@FXML
	private Label pagination06StatusUI;
	@FXML
	private Node pagination06FwdUI;
	@FXML
	private Node pagination06BckUI;
	
	@FXML
	private SearchBox<AmountAndAccount> searchBox01UI;
	@FXML
	private SearchBox<Vote> searchBox02UI;
	@FXML
	private SearchBox<BurnRecord> searchBox04UI;
	@FXML
	private SearchBox<Transaction> searchBox05UI;
	@FXML
	private SearchBox<MarketFeed> searchBox06UI;
	
	private List<AmountAndAccount> list01;
	private List<Vote> list02;
	private List<BurnRecord> list04;
	private List<Transaction> list05;
	private List<MarketFeed> list06;
	
	
	@FXML
	private DisplayText nameUI;
	@FXML
	private DisplayText registrationDateUI;
	@FXML
	private DisplayText lastUpdateUI;
	@FXML
	private DisplayField publicKeyUI;
	
	@FXML
	private Label delegateInfoUI;
	@FXML
	private DisplayText supportUI;
	@FXML
	private DisplayText reliabilityUI;
	@FXML
	private DisplayText blocksProducedUI;
	@FXML
	private DisplayText blocksMissedUI;
	@FXML
	private DisplayText payRateUI;
	@FXML
	private DisplayDuet payBalanceUI;
	
	private BooleanProperty registeredProperty;
	private BooleanProperty favoriteProperty;
	private IntegerProperty approvalProperty;
	
	private boolean isFreshBurnRecordExpected;
	private boolean isFreshTransactionExpected;
	
	
	public Details_Account(Account item) {
		super(item);
	}
	public Details_Account(RegisteredName item) {
		super();
		this.item = h.getAccount(item.getName());
	}
	public Details_Account(String item) {
		super();
		this.item = h.getAccount(item);
	}
	
	private Details_Account getInstance() {
		return this;
	}
	
	@Override
	public String getTitle() {
		return Util.crop(item.getName(), 19);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		registeredProperty = new SimpleBooleanProperty(!Model.getInstance().isAccountUnregistered(item));
		favoriteProperty = new SimpleBooleanProperty(item.isIs_favorite());
		approvalProperty = new SimpleIntegerProperty(item.getApproved());
		
//		registerAccountUI.disableProperty().bind(registeredProperty);
		unregisteredUI.visibleProperty().bind(registeredProperty.not());
		
		favoriteNeutralUI.visibleProperty().bind(favoriteProperty.not());
		favoritePositiveUI.visibleProperty().bind(favoriteProperty);
		
		approvalNeutralUI.visibleProperty().bind(approvalProperty.isEqualTo(0));
		approvalNegativeUI.visibleProperty().bind(approvalProperty.lessThan(0));
		approvalPositiveUI.visibleProperty().bind(approvalProperty.greaterThan(0));

		myAccountUI.setVisible(item.isIs_my_account());
		unregisteredUI.setText(String.format("(%s)", "unregistered"));

		int index = item.isIs_my_account() ? 0 : 2;
		panoramaUI.setIndex(index);
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(index));
		
		Util.manageToggleGroup(panoramaToggleGroupUI);
		
		searchBox01UI.setHost(new SearchBox.Host<AmountAndAccount>() {
			@Override
			public List<AmountAndAccount> getList() {
				return list01;
			}
			@Override
			public boolean isSearchMatch(AmountAndAccount item, String[] phraseList) {
				for (String phrase : phraseList) {
					if (!c(Model.getInstance().getAssetById(item.getAmount().getAsset_id()).getSymbol(), phrase))
						return false;
				}
				return true;
			}
			@Override
			public void apply() {
				Collections.sort(list01, (AmountAndAccount a1, AmountAndAccount a2) -> {
					return Model.getInstance().getAssetById(a1.getAmount().getAsset_id()).getSymbol().compareTo(
							Model.getInstance().getAssetById(a2.getAmount().getAsset_id()).getSymbol());
				});
				pagination01UI.reset();
			}
		});
		searchBox02UI.setHost(new SearchBox.Host<Vote>() {
			@Override
			public List<Vote> getList() {
				return list02;
			}
			@Override
			public boolean isSearchMatch(Vote item, String[] phraseList) {
				for (String phrase : phraseList) {
					if (!c(item.getAccount().getName(), phrase))
						return false;
				}
				return true;
			}
			@Override
			public void apply() {
				Collections.sort(list02);
				pagination02UI.reset();
			}
		});
		searchBox04UI.setHost(new SearchBox.Host<BurnRecord>() {
			@Override
			public List<BurnRecord> getList() {
				return list04;
			}
			@Override
			public boolean isSearchMatch(BurnRecord item, String[] phraseList) {
				for (String phrase : phraseList) {
					if (!c(item.getMessage(), phrase))
						return false;
				}
				return true;
			}
			@Override
			public void apply() {
				Collections.sort(list04);
				pagination04UI.reset();
			}
		});
		searchBox05UI.setHost(new SearchBox.Host<Transaction>() {
			@Override
			public List<Transaction> getList() {
				return list05;
			}
			@Override
			public boolean isSearchMatch(Transaction item, String[] phraseList) {
				for (String phrase : phraseList) {
					if (!c(item.getMemo(), phrase))
						return false;
				}
				return true;
			}
			@Override
			public void apply() {
				Collections.sort(list05);
				pagination05UI.reset();
			}
		});
		searchBox06UI.setHost(new SearchBox.Host<MarketFeed>() {
			@Override
			public List<MarketFeed> getList() {
				return list06;
			}
			@Override
			public boolean isSearchMatch(MarketFeed item, String[] phraseList) {
				for (String phrase : phraseList) {
					if (!c(item.getAsset_symbol(), phrase))
						return false;
				}
				return true;
			}
			@Override
			public void apply() {
				Collections.sort(list06);
				pagination06UI.reset();
			}
		});
		
		pagination01UI.setHost(new Pagination.Host() {
			@Override
			public Page getPage() {
				return getInstance();
			}
			@Override
			public double getPrefColumnWidth() {
				return 155;
			}
			@Override
			public double getPrefRowHeight() {
				return 100;
			}
			@Override
			public Label getStatusUI() {
				return pagination01StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination01FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination01BckUI;
			}
			@Override
			public int getListSize() {
				return list01.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new AmountAndAccountTile(), list01, pagination01UI, index);
			}
			@Override
			public boolean isSearchOn() {
				return searchBox01UI.isSearchOn();
			}
			@Override
			public String getEmptyText() {
				return null;
			}
		});
		pagination02UI.setHost(new Pagination.Host() {
			@Override
			public Page getPage() {
				return getInstance();
			}
			@Override
			public double getPrefColumnWidth() {
				return 0;
			}
			@Override
			public double getPrefRowHeight() {
				return 110;
			}
			@Override
			public Label getStatusUI() {
				return pagination02StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination02FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination02BckUI;
			}
			@Override
			public int getListSize() {
				return list02.size();
			}
			@Override
			public Pane getTilePane(int index) {
				return createTilePane(new VoteTile(), list02, pagination02UI, index);
			}
			@Override
			public boolean isSearchOn() {
				return searchBox02UI.isSearchOn();
			}
			@Override
			public String getEmptyText() {
				return null;
			}
		});
		pagination04UI.setHost(new Pagination.Host() {
			@Override
			public Page getPage() {
				return getInstance();
			}
			@Override
			public double getPrefColumnWidth() {
				return 0;
			}
			@Override
			public double getPrefRowHeight() {
				return 40;
			}
			@Override
			public Label getStatusUI() {
				return pagination04StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination04FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination04BckUI;
			}
			@Override
			public int getListSize() {
				return list04.size();
			}
			@Override
			public Pane getTilePane(int index) {
				return createTilePane(new BurnRecordTile(), list04, pagination04UI, index);
			}
			@Override
			public boolean isSearchOn() {
				return searchBox04UI.isSearchOn();
			}
			@Override
			public String getEmptyText() {
				return null;
			}
		});
		pagination05UI.setHost(new Pagination.Host() {
			@Override
			public Page getPage() {
				return getInstance();
			}
			@Override
			public double getPrefColumnWidth() {
				return 0;
			}
			@Override
			public double getPrefRowHeight() {
				return 90;
			}
			@Override
			public Label getStatusUI() {
				return pagination05StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination05FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination05BckUI;
			}
			@Override
			public int getListSize() {
				return list05.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new TransactionTile(), list05, pagination05UI, index);
			}
			@Override
			public boolean isSearchOn() {
				return searchBox05UI.isSearchOn();
			}
			@Override
			public String getEmptyText() {
				return null;
			}
		});
		pagination06UI.setHost(new Pagination.Host() {
			@Override
			public Page getPage() {
				return getInstance();
			}
			@Override
			public double getPrefColumnWidth() {
				return 0;
			}
			@Override
			public double getPrefRowHeight() {
				return 80;
			}
			@Override
			public Label getStatusUI() {
				return pagination06StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination06FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination06BckUI;
			}
			@Override
			public int getListSize() {
				return list06.size();
			}
			@Override
			public Pane getTilePane(int index) {
				return createTilePane(new MarketFeedByAssetTile(), list06, pagination06UI, index);
			}
			@Override
			public boolean isSearchOn() {
				return searchBox06UI.isSearchOn();
			}
			@Override
			public String getEmptyText() {
				return null;
			}
		});
		
		list01 = new ArrayList<AmountAndAccount>();
		list02 = new ArrayList<Vote>();
		list04 = new ArrayList<BurnRecord>();
		list05 = new ArrayList<Transaction>();
		list06 = new ArrayList<MarketFeed>();
		
		nameUI.setText(item.getName());
		publicKeyUI.setText(item.getOwner_key());
		accountTypeUI.setText(Model.getInstance().isDelegate(item) ? (Model.getInstance().isActiveDelegate(item) ? "Active Delegate" : "Stand-by Delegate") : "Standard Account");
		registrationDateUI.setText(Time.format(item.getRegistration_date(), "unregistered"));
		lastUpdateUI.setText(Time.format(item.getLast_update(), "never"));
		if (Model.getInstance().isDelegate(item)) {
			delegateInfoUI.setVisible(true);
			supportUI.setText(String.format("%.2f%s", Model.getInstance().getDelegateSupport(item) * 100, "%"));
			reliabilityUI.setText(String.format("%.2f%s", Model.getInstance().getDelegateReliability(item) * 100, "%"));
			blocksProducedUI.setText(String.format("%d", item.getDelegate_info().getBlocks_produced()));
			blocksMissedUI.setText(String.format("%d", item.getDelegate_info().getBlocks_missed()));
			payRateUI.setText(String.format("%d%s", item.getDelegate_info().getPay_rate(), "%"));
			payBalanceUI.setText(Model.getInstance().getAmountPair(0, item.getDelegate_info().getPay_balance()));
			delegateInfoUI.setText(String.format("%s: %s, %s: %s", "Support", supportUI.getText(), "Pay Rate", payRateUI.getText()));
			
		} else {
			delegateInfoUI.setVisible(false);
			delegateInfoUI.setText("");
			supportUI.setText("");
			reliabilityUI.setText("");
			blocksProducedUI.setText("");
			blocksMissedUI.setText("");
			payRateUI.setText("");
			payBalanceUI.setText(null);
		}
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			avatarUI.setName(item.getName());
			{
				List<AmountAndAccount> items = new ArrayList<AmountAndAccount>();
				if (item.isIs_my_account()) {
					for (AccountAndAmounts i : Model.getInstance().getMyAccountSplit()) {
						if (i.getAccount().getName().equals(item.getName())) {
							for (Amount j : i.getAmounts()) {
								AmountAndAccount k = new AmountAndAccount();
								k.setAmount(j);
								k.setAccount(item);
								items.add(k);
							}
							break;
						}
					}
				}
				searchBox01UI.setItems(items);
				list01.clear();
				list01.addAll(items);
				pagination01UI.reset();
			}
			{
				List<Vote> items = new ArrayList<Vote>();
				if (item.isIs_my_account()) {
					List<WalletAccountVoteSummary.Result> results;
					try {
						results = WalletAccountVoteSummary.run(item.getName());
					} catch (BTSSystemException e) {
						systemException(e);
						return;
					}
					for (Account account : Model.getInstance().getAccounts()) {
						if (account.getApproved() != 0)
							items.add(new Vote(account, getVotes(account, results)));
					}
				}
				searchBox02UI.setItems(items);
				list02.clear();
				list02.addAll(items);
				pagination02UI.reset();
			}
			{
				List<BurnRecord> items = new ArrayList<BurnRecord>();
				if (!Model.getInstance().isAccountUnregistered(item)) {
					List<BlockchainGetAccountWall.Result> results;
					try {
						results = BlockchainGetAccountWall.run(item.getName());
					} catch (BTSSystemException e) {
						systemException(e);
						return;
					}
					for (BlockchainGetAccountWall.Result r : results) {
						BurnRecord item = new BurnRecord();
						item.setAmount(r.getAmount());
						item.setMessage(r.getMessage());
						items.add(item);
					}
				}
				searchBox04UI.setItems(items);
				list04.clear();
				list04.addAll(items);
				pagination04UI.reset();
			}
			{
				List<WalletAccountTransactionHistory.Result> results;
				try {
					results = WalletAccountTransactionHistory.run(item.getName(), "", 0);
				} catch (BTSSystemException e) {
					systemException(e);
					return;
				}
				List<Transaction> items = Model.getTransactions(results);
				searchBox05UI.setItems(items);
				list05.clear();
				list05.addAll(items);
				pagination05UI.reset();
			}
			{
				List<MarketFeed> items = new ArrayList<MarketFeed>();
				if (Model.getInstance().isDelegate(item)) {
					List<MarketFeed> results = null;
					try {
						results = BlockchainGetFeedsFromDelegate.run(item.getName());
					} catch (BTSSystemException e) {
						systemException(e);
						return;
					}
					for (MarketFeed r : results) {
						if (r.getPrice() > 0)
							items.add(r);
					}
				}
				searchBox06UI.setItems(items);
				list06.clear();
				list06.addAll(items);
				pagination06UI.reset();
			}
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		if (item.isIs_my_account()) {
			isDataLoaded = false;
			loadData();
		}
	}
	
	@Override
	public void ping() {
		if (!isDataLoaded)
			return;
		if (isFreshBurnRecordExpected && (!item.isIs_my_account() || !Model.getInstance().isAccountUnregistered(item))) {
			List<BlockchainGetAccountWall.Result> results;
			try {
				results = BlockchainGetAccountWall.run(item.getName());
			} catch (BTSSystemException e) {
				systemException(e);
				return;
			}
			if (results.size() != searchBox04UI.getItems().size()) {
				List<BurnRecord> items = new ArrayList<BurnRecord>();
				for (BlockchainGetAccountWall.Result r : results) {
					BurnRecord item = new BurnRecord();
					item.setAmount(r.getAmount());
					item.setMessage(r.getMessage());
					items.add(item);
				}
				searchBox04UI.setItems(items);
				list04.clear();
				list04.addAll(items);
				pagination04UI.reset();
				isFreshBurnRecordExpected = false;
			}
		}
		if (isFreshTransactionExpected && item.isIs_my_account()) {
			List<WalletAccountTransactionHistory.Result> results;
			try {
				results = WalletAccountTransactionHistory.run(item.getName(), "", 0);
			} catch (BTSSystemException e) {
				systemException(e);
				return;
			}
			if (results.size() != searchBox05UI.getItems().size()) {
				List<Transaction> items = Model.getTransactions(results);
				searchBox05UI.setItems(items);
				list05.clear();
				list05.addAll(items);
				pagination05UI.reset();
				isFreshTransactionExpected = false;
			}
		}
		super.ping();
	}
	
	@FXML
	private void onRegisterAccount() {
		Wizard_RegisterAccount wizard = new Wizard_RegisterAccount(item);
		wizard.setCallback(() -> registeredProperty.set(true));
		module.jump(wizard);
	}
	@FXML
	private void onMakeTransfer() {
		Wizard_MakeTransfer wizard = new Wizard_MakeTransfer();
		if (item.isIs_my_account())
			wizard.setFromAccount(item);
		else if (item.isIs_favorite())
			wizard.setToFavoriteAccount(item);
		else
			wizard.setToExternalAccount(item);
		wizard.setCallback(() -> isFreshTransactionExpected = true);
		module.jump(wizard);
	}
	@FXML
	private void onUpdateVote() {
		Wizard_UpdateVote wizard = new Wizard_UpdateVote();
		wizard.setAccount(item);
		module.jump(wizard);
	}
	
	@FXML
	private void onToggleFavorite() {
		ToggleFavorite toggleFavorite = new ToggleFavorite();
		toggleFavorite.setName(item.getName());
		toggleFavorite.setFavoriteProperty(favoriteProperty);
		toggleFavorite.setCallback(new ToggleApproval.Callback() {
			@Override
			public void onConfirm() {
				item.setIs_favorite(favoriteProperty.get());
			}
			@Override
			public void onCancel() {
			}
		});
		toggleFavorite.show(getModalPane(), module, isOverlay);
	}
	
	@FXML
	private void onToggleApproval() {
		ToggleApproval toggleApproval = new ToggleApproval();
		toggleApproval.setName(item.getName());
		toggleApproval.setApprovalProperty(approvalProperty);
		toggleApproval.setCallback(new ToggleApproval.Callback() {
			@Override
			public void onConfirm() {
				item.setApproved(approvalProperty.get());
			}
			@Override
			public void onCancel() {
			}
		});
		toggleApproval.show(getModalPane(), module, isOverlay);
	}
	
	@FXML
	private void onBurnMessage() {
		Wizard_BurnMessage wizard = new Wizard_BurnMessage(item);
		wizard.setCallback(() -> isFreshBurnRecordExpected = true);
		module.jump(wizard);
	}
	
	
	@FXML
	private void onPanoramaToggle01() {
		panoramaMenuUI.setIndex(0);
		applyPanoramaIndex(0);
	}
	@FXML
	private void onPanoramaToggle02() {
		panoramaMenuUI.setIndex(1);
		applyPanoramaIndex(1);
	}
	@FXML
	private void onPanoramaToggle03() {
		panoramaMenuUI.setIndex(2);
		applyPanoramaIndex(2);
	}
	@FXML
	private void onPanoramaToggle04() {
		panoramaMenuUI.setIndex(3);
		applyPanoramaIndex(3);
	}
	@FXML
	private void onPanoramaToggle05() {
		panoramaMenuUI.setIndex(4);
		applyPanoramaIndex(4);
	}
	@FXML
	private void onPanoramaToggle06() {
		panoramaMenuUI.setIndex(5);
		applyPanoramaIndex(5);
	}
	
	
	@FXML
	private void onPanoramaMenu01() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(0));
		applyPanoramaIndex(0);
	}
	@FXML
	private void onPanoramaMenu02() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(1));
		applyPanoramaIndex(1);
	}
	@FXML
	private void onPanoramaMenu03() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(2));
		applyPanoramaIndex(2);
	}
	@FXML
	private void onPanoramaMenu04() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(3));
		applyPanoramaIndex(3);
	}
	@FXML
	private void onPanoramaMenu05() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(4));
		applyPanoramaIndex(4);
	}
	@FXML
	private void onPanoramaMenu06() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(5));
		applyPanoramaIndex(5);
	}
	
	
	@FXML
	private void onPanoramaMenu0301() {
		panorama03UI.setIndex(0);
	}
	@FXML
	private void onPanoramaMenu0302() {
		panorama03UI.setIndex(1);
	}
	
	
	private void applyPanoramaIndex(int index) {
		panoramaUI.setIndex(index);
		turnOffSearchBoxes();
	}
	
	
	
	@FXML
	private void onPagination01Bck() {
		pagination01UI.turnPageBck();
		turnOffSearchBoxes();
	}
	@FXML
	private void onPagination01Fwd() {
		pagination01UI.turnPageFwd();
		turnOffSearchBoxes();
	}
		
	@FXML
	private void onPagination02Bck() {
		pagination02UI.turnPageBck();
		turnOffSearchBoxes();
	}
	@FXML
	private void onPagination02Fwd() {
		pagination02UI.turnPageFwd();
		turnOffSearchBoxes();
	}
	
	@FXML
	private void onPagination04Bck() {
		pagination04UI.turnPageBck();
		turnOffSearchBoxes();
	}
	@FXML
	private void onPagination04Fwd() {
		pagination04UI.turnPageFwd();
		turnOffSearchBoxes();
	}
	
	@FXML
	private void onPagination05Bck() {
		pagination05UI.turnPageBck();
		turnOffSearchBoxes();
	}
	@FXML
	private void onPagination05Fwd() {
		pagination05UI.turnPageFwd();
		turnOffSearchBoxes();
	}
	
	@FXML
	private void onPagination06Bck() {
		pagination06UI.turnPageBck();
		turnOffSearchBoxes();
	}
	@FXML
	private void onPagination06Fwd() {
		pagination06UI.turnPageFwd();
		turnOffSearchBoxes();
	}
	
	@FXML
	private void onMouseClicked() {
		turnOffSearchBoxes();
	}
	
	private void turnOffSearchBoxes() {
		searchBox01UI.turnOff();
		searchBox02UI.turnOff();
		searchBox04UI.turnOff();
		searchBox05UI.turnOff();
		searchBox06UI.turnOff();
	}
	
	private static long getVotes(Account account, List<WalletAccountVoteSummary.Result> results) {
		for (WalletAccountVoteSummary.Result r : results) {
			if (r.getName().equals(account.getName()))
				return r.getVotes();
		}
		return 0;
	}

}
