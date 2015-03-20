package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.AccountAndAmount;
import sx.neura.bts.gui.dto.AmountAndAccounts;
import sx.neura.bts.gui.dto.Transaction;
import sx.neura.bts.gui.view.Page;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.MenuPane;
import sx.neura.bts.gui.view.components.Pagination;
import sx.neura.bts.gui.view.components.SearchBox;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.PageDetails_Bts;
import sx.neura.bts.gui.view.pages.bts.comp.tile.AccountAndAmountTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.MarketFeedByAccountTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.TransactionTile;
import sx.neura.bts.json.api.blockchain.BlockchainGetFeedsForAsset;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.MarketFeed;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Time;
import sx.neura.bts.util.Util;

public class Details_Asset extends PageDetails_Bts<Asset> {
	
	@FXML
	private Node marketPeggedAssetUI;
	@FXML
	private Node userIssuedAssetUI;
	@FXML
	private Label subTitleUI;
	@FXML
	private Label amountUI;
	
	@FXML
	private MenuPane panoramaMenuUI;
	@FXML
	private LayerPane panoramaUI;
	
	@FXML
	private MenuPane panoramaMenu01UI;
	@FXML
	private LayerPane panorama01UI;
	
	@FXML
	private ToggleGroup panoramaToggleGroupUI;

	
	@FXML
	private Pagination pagination02UI;
	@FXML
	private Label pagination02StatusUI;
	@FXML
	private Node pagination02FwdUI;
	@FXML
	private Node pagination02BckUI;
	
	@FXML
	private Pagination pagination03UI;
	@FXML
	private Label pagination03StatusUI;
	@FXML
	private Node pagination03FwdUI;
	@FXML
	private Node pagination03BckUI;
	
	@FXML
	private Pagination pagination04UI;
	@FXML
	private Label pagination04StatusUI;
	@FXML
	private Node pagination04FwdUI;
	@FXML
	private Node pagination04BckUI;
	
	@FXML
	private SearchBox<AccountAndAmount> searchBox02UI;
	@FXML
	private SearchBox<Transaction> searchBox03UI;
	@FXML
	private SearchBox<MarketFeed> searchBox04UI;
	
	
	private List<AccountAndAmount> list02;
	private List<Transaction> list03;
	private List<MarketFeed> list04;
	
	
	@FXML
	private DisplayText nameUI;
	@FXML
	private DisplayText descriptionUI;
	@FXML
	private DisplayText typeUI;
	@FXML
	private DisplayText precisionUI;
	
	@FXML
	private DisplayText registrationDateUI;
	@FXML
	private DisplayText lastUpdateUI;
	
	@FXML
	private DisplayText currentShareSupplyUI;
	@FXML
	private DisplayText maximumShareSupplyUI;
	@FXML
	private DisplayText collectedFeesUI;
	
	@FXML
	private DisplayText issuerUI;
	@FXML
	private DisplayText publicDataUI;
	
	
	public Details_Asset(Asset item) {
		super(item);
	}
	public Details_Asset(int assetId) {
		super();
		this.item = getAssetById(assetId);
	}
	
	private Details_Asset getInstance() {
		return this;
	}
	
	@Override
	public String getTitle() {
		return item.getSymbol();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		marketPeggedAssetUI.setVisible(isMarketPeggedAsset(item));
		
		panoramaUI.setIndex(0);
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(0));
		
		Util.manageToggleGroup(panoramaToggleGroupUI);
		
		
		searchBox02UI.setHost(new SearchBox.Host<AccountAndAmount>() {
			@Override
			public List<AccountAndAmount> getList() {
				return list02;
			}
			@Override
			public boolean isSearchMatch(AccountAndAmount item, String[] phraseList) {
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
		searchBox03UI.setHost(new SearchBox.Host<Transaction>() {
			@Override
			public List<Transaction> getList() {
				return list03;
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
				Collections.sort(list03);
				pagination03UI.reset();
			}
		});
		searchBox04UI.setHost(new SearchBox.Host<MarketFeed>() {
			@Override
			public List<MarketFeed> getList() {
				return list04;
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
				Collections.sort(list04, (MarketFeed r1, MarketFeed r2) -> {
					return r2.getLast_update().compareTo(r1.getLast_update());
				});
				pagination04UI.reset();
			}
		});
		
		pagination02UI.setHost(new Pagination.Host() {
			@Override
			public Page getPage() {
				return getInstance();
			}
			@Override
			public double getPrefColumnWidth() {
				return 125;
			}
			@Override
			public double getPrefRowHeight() {
				return 125;
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
				return createTilePane(new AccountAndAmountTile(), list02, pagination02UI, index);
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
		pagination03UI.setHost(new Pagination.Host() {
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
				return pagination03StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination03FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination03BckUI;
			}
			@Override
			public int getListSize() {
				return list03.size();
			}
			@Override
			public Pane getTilePane(int index) {
				return createTilePane(new TransactionTile(), list03, pagination03UI, index);
			}
			@Override
			public boolean isSearchOn() {
				return searchBox03UI.isSearchOn();
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
				return createTilePane(new MarketFeedByAccountTile(), list04, pagination04UI, index);
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
		
		list02 = new ArrayList<AccountAndAmount>();
		list03 = new ArrayList<Transaction>();
		list04 = new ArrayList<MarketFeed>();
		
		subTitleUI.setText(item.getName());
		
		if (isMarketPeggedAsset(item)) {
			issuerUI.setText("SYSTEM");
			issuerUI.setEnabled(false);
		} else {
			Account issuer = getAccount(item.getIssuer_account_id());
			if (issuer != null) {
				issuerUI.setText(issuer.getName());
				issuerUI.setEnabled(true);
				issuerUI.getTextUI().setOnMouseClicked((MouseEvent event) -> {
					module.jump(new Details_Account(issuer));
					event.consume();
				});
			} else {
				issuerUI.setText("n/a");
				issuerUI.setEnabled(false);
			}
		}
		precisionUI.setText(new Integer(item.getPrecision()).toString());
		collectedFeesUI.setText(getAmount(item, item.getCollected_fees()));
		nameUI.setText(item.getName());
		descriptionUI.setText(item.getDescription());
		typeUI.setText(isMarketPeggedAsset(item) ? "Market-Pegged Asset" : "User-Issued Asset");
		publicDataUI.setText("item.getPublic_data()");
		registrationDateUI.setText(Time.format(item.getRegistration_date()));
		lastUpdateUI.setText(Time.format(item.getLast_update(), "never"));
		currentShareSupplyUI.setText(getAmount(item, item.getCurrent_share_supply()));
		maximumShareSupplyUI.setText(getAmount(item, item.getMaximum_share_supply()));
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			{
				List<AccountAndAmount> items = new ArrayList<AccountAndAmount>();
				long amount = 0;
				for (AmountAndAccounts i : Model.getInstance().getMyAssetSplit()) {
					if (i.getAsset().getId() == item.getId()) {
						amount = i.getAmount();
						items.addAll(i.getSplit());
						break;
					}
				}
				amountUI.setText(getAmount(item, amount));
				searchBox02UI.setItems(items);
				list02.clear();
				list02.addAll(items);
				pagination02UI.reset();
			}
			{
				List<WalletAccountTransactionHistory.Result> results;
				try {
					results = WalletAccountTransactionHistory.run("", item.getSymbol(), 0);
				} catch (BTSSystemException e) {
					systemException(e);
					return;
				}
				List<Transaction> items = Model.getTransactions(results);
				searchBox03UI.setItems(items);
				list03.clear();
				list03.addAll(items);
				pagination03UI.reset();
			}
			{
				List<MarketFeed> items = new ArrayList<MarketFeed>();
				List<MarketFeed> results;
				try {
					results = BlockchainGetFeedsForAsset.run(item.getSymbol());
				} catch (BTSSystemException e) {
					systemException(e);
					return;
				}
				for (MarketFeed r : results) {
					if (r.getPrice() > 0) {
						// asset_symbol==null in feed
						r.setAsset_symbol(item.getSymbol());
						items.add(r);
					}
				}
				searchBox04UI.setItems(items);
				list04.clear();
				list04.addAll(items);
				pagination04UI.reset();
			}
		}
		super.loadData();
	}
	
	@FXML
	private void onMakeTransfer() {
		Wizard_MakeTransfer wizard = new Wizard_MakeTransfer();
		wizard.setAsset(item);
		wizard.setCallback(() -> {
			reloadData();
		});
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
	private void onPanoramaMenu0101() {
		panorama01UI.setIndex(0);
	}
	@FXML
	private void onPanoramaMenu0102() {
		panorama01UI.setIndex(1);
	}
	@FXML
	private void onPanoramaMenu0103() {
		panorama01UI.setIndex(2);
	}
	
	
	private void applyPanoramaIndex(int index) {
		panoramaUI.setIndex(index);
		turnOffSearchBoxes();
	}
	
//	@FXML
//	private void onPanorama01() {
//		panoramaUI.setIndex(0);
//		turnOffSearchBoxes();
//	}
//	@FXML
//	private void onPanorama02() {
//		panoramaUI.setIndex(1);
//		turnOffSearchBoxes();
//	}
//	@FXML
//	private void onPanorama03() {
//		panoramaUI.setIndex(2);
//		turnOffSearchBoxes();
//	}
//	@FXML
//	private void onPanorama04() {
//		panoramaUI.setIndex(3);
//		turnOffSearchBoxes();
//	}
//	
//	
//	@FXML
//	public void onPanoramaShadow01() {
//		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(0));
//		onPanorama01();
//	}
//	@FXML
//	private void onPanoramaShadow02() {
//		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(1));
//		onPanorama02();
//	}
//	@FXML
//	private void onPanoramaShadow03() {
//		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(2));
//		onPanorama03();
//	}
//	@FXML
//	private void onPanoramaShadow04() {
//		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(3));
//		onPanorama04();
//	}
//	
//	@FXML
//	public void onPanoramaShadow0101() {
//		panorama01UI.setIndex(0);
//	}
//	@FXML
//	private void onPanoramaShadow0102() {
//		panorama01UI.setIndex(1);
//	}
//	@FXML
//	private void onPanoramaShadow0103() {
//		panorama01UI.setIndex(2);
//	}
//	@FXML
//	private void onPanoramaShadow0104() {
//		panorama01UI.setIndex(3);
//	}
	
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
	private void onPagination03Bck() {
		pagination03UI.turnPageBck();
		turnOffSearchBoxes();
	}
	@FXML
	private void onPagination03Fwd() {
		pagination03UI.turnPageFwd();
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
	private void onMouseClicked() {
		turnOffSearchBoxes();
	}
	
	private void turnOffSearchBoxes() {
		searchBox02UI.turnOff();
		searchBox03UI.turnOff();
		searchBox04UI.turnOff();
	}
}
