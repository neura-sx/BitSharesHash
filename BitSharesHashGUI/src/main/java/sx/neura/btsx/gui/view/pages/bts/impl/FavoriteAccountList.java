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
import sx.neura.bts.json.api.wallet.WalletListAccounts;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.Page;
import sx.neura.btsx.gui.view.components.SearchBox;
import sx.neura.btsx.gui.view.components.flow.Flow;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;
import sx.neura.btsx.gui.view.pages.bts.comp.flow.AccountFlowRenderer;

public class FavoriteAccountList extends Page_Bts {
	
	private static FavoriteAccountList instance;
	public static FavoriteAccountList getInstance() {
		if (!isInstance())
			instance = new FavoriteAccountList();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private FavoriteAccountList() {
	}
	
	@FXML
	protected Node emptyResultsUI;
	@FXML
	protected Node validResultsUI;
	
	@FXML
	private Flow<Account> listUI;
	
	private List<Account> listUnfiltered;
	private ObservableList<Account> listFiltered;
	private String[] accountsPhraseList;
	
	@Override
	public String getName() {
		return "Favorite Accounts";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		listUnfiltered = new ArrayList<Account>();
		listFiltered = FXCollections.observableArrayList();
		
		listUI.setCellFactory((Account i) -> {
			AccountFlowRenderer renderer = new AccountFlowRenderer();
			renderer.setItem(i);
			renderer.setActor((Account j) -> {
				jump(new TheirAccountDetail(j));
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
				accountsPhraseList = phraseList;
				applyAccountSearch();
				applyItems();
			}
			@Override
			public void cancelSearch() {
				accountsPhraseList = null;
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
			listUnfiltered.addAll(Model.getInstance().getFavoriteAccounts());
			listFiltered.setAll(listUnfiltered);
			if (accountsPhraseList != null)
				applyAccountSearch();
			applyItems();
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		try {
			Model.getInstance().setAccounts(WalletListAccounts.run());
		} catch (BTSSystemException e) {
			systemException(e);
		}
		isDataLoaded = false;
		loadData();
	}
	
	@Override
	protected SearchBox createSearchBox() {
		return createSearchBox("Search favorite accounts", 75.0, 150.0);
	}
	
	private synchronized void applyAccountSearch() {
		final int size = listFiltered.size();
		for (int i = size - 1; i >= 0; i--) {
			if (!isSearchMatch(listFiltered.get(i), accountsPhraseList))
				listFiltered.remove(listFiltered.get(i));
		}
		for (Account item : listUnfiltered) {
			if (!listFiltered.contains(item)
					&& isSearchMatch(item, accountsPhraseList)) {
				listFiltered.add(item);
			}
		}
	}
	
	private synchronized void cancelAccountSearch() {
		for (Account item : listUnfiltered) {
			if (!listFiltered.contains(item))
				listFiltered.add(item);
		}
	}
	
	private static boolean isSearchMatch(Account item, String[] phraseList) {
		for (String phrase : phraseList) {
			if (!c(item.getName(), phrase))
				return false;
		}
		return true;
	}
	
}
