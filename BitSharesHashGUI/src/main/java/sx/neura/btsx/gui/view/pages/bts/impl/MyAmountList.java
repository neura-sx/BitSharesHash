package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import sx.neura.bts.gui.dto.AccountAndAmount;
import sx.neura.bts.gui.dto.AmountAndAccounts;
import sx.neura.bts.json.api.wallet.WalletAccountBalance;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.btsx.gui.view.Page;
import sx.neura.btsx.gui.view.components.SearchBox;
import sx.neura.btsx.gui.view.components.flow.Flow;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;
import sx.neura.btsx.gui.view.pages.bts.comp.flow.AmountAndAccountsFlowRenderer;

public class MyAmountList extends Page_Bts {
	
	private static MyAmountList instance;
	public static MyAmountList getInstance() {
		if (!isInstance())
			instance = new MyAmountList();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private MyAmountList() {
	}
	
	@FXML
	protected Node emptyResultsUI;
	@FXML
	protected Node validResultsUI;
	
	@FXML
	private Flow<AmountAndAccounts> listUI;
	
	private List<AmountAndAccounts> listUnfiltered;
	private ObservableList<AmountAndAccounts> listFiltered;
	private String[] amountsPhraseList;
	
	public List<AmountAndAccounts> getList() {
		return listUnfiltered;
	}
	
	@Override
	public String getName() {
		return "My Balances";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		listUnfiltered = new ArrayList<AmountAndAccounts>();
		listFiltered = FXCollections.observableArrayList();
		
		listUI.setCellFactory((AmountAndAccounts i) -> {
			AmountAndAccountsFlowRenderer renderer = new AmountAndAccountsFlowRenderer();
			renderer.setItem(i);
			renderer.setActor((AmountAndAccounts j) -> {
				jump(new MyAmountDetail(j));
			});
			renderer.setJumper((Page page) -> {
				jump(page);
			});
			return renderer;
		});
		listUI.setItems(listFiltered);
		
		searchBoxUI.setTarget(new SearchBox.Target() {
			@Override
			public void applySearch(String[] phraseList) {
				amountsPhraseList = phraseList;
				applyAmountSearch();
				applyItems();
			}
			@Override
			public void cancelSearch() {
				amountsPhraseList = null;
				cancelAccountSearch();
				applyItems();
			}
		});
	}
	
	private void applyItems() {
		Collections.sort(listFiltered);
		listUI.applyItems();
		emptyResultsUI.setVisible(listFiltered.isEmpty());
		validResultsUI.setVisible(!listFiltered.isEmpty());
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			listUnfiltered.clear();
			try {
				List<WalletAccountBalance.Result> balances = WalletAccountBalance.run();
				for (WalletAccountBalance.Result balance : balances) {
					for (Amount amount : balance.getAmounts()) {
						AccountAndAmount s = new AccountAndAmount();
						s.setAccount(getAccountByName(balance.getName()));
						Amount a = new Amount();
						a.setAsset_id(amount.getAsset_id());
						a.setValue(amount.getValue());
						s.setAmount(a);
						int index = indexOf(amount.getAsset_id());
						if (index < 0) {
							AmountAndAccounts item = new AmountAndAccounts();
							item.setAmount(amount.getValue());
							item.setAsset(getAssetById(amount.getAsset_id()));
							item.setSplit(new ArrayList<AccountAndAmount>());
							item.getSplit().add(s);
							listUnfiltered.add(item);
						} else {
							AmountAndAccounts item = listUnfiltered.get(index);
							item.setAmount(item.getAmount() + amount.getValue());
							item.getSplit().add(s);
						}
					}
				}
			} catch (BTSSystemException e) {
				systemException(e);
			}
			listFiltered.setAll(listUnfiltered);
			if (amountsPhraseList != null)
				applyAmountSearch();
			applyItems();
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		isDataLoaded = false;
		loadData();
	}
	
	@Override
	protected SearchBox createSearchBox() {
		return createSearchBox("Search balances", 75.0, 150.0);
	}
	
	private synchronized void applyAmountSearch() {
		final int size = listFiltered.size();
		for (int i = size - 1; i >= 0; i--) {
			if (!isSearchMatch(listFiltered.get(i), amountsPhraseList))
				listFiltered.remove(listFiltered.get(i));
		}
		for (AmountAndAccounts item : listUnfiltered) {
			if (!listFiltered.contains(item)
					&& isSearchMatch(item, amountsPhraseList)) {
				listFiltered.add(item);
			}
		}
	}
	
	private synchronized void cancelAccountSearch() {
		for (AmountAndAccounts item : listUnfiltered) {
			if (!listFiltered.contains(item))
				listFiltered.add(item);
		}
	}
	
	private static boolean isSearchMatch(AmountAndAccounts item, String[] phraseList) {
		for (String phrase : phraseList) {
			if (!c(item.getAsset().getSymbol(), phrase))
				return false;
		}
		return true;
	}
	
	private int indexOf(int assetId) {
		for (int i = 0; i < listUnfiltered.size(); i++) {
			if (listUnfiltered.get(i).getAsset().getId() == assetId)
				return i;
		}
		return -1;
	}
}