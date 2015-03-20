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
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.dto.AccountAndAmounts;
import sx.neura.bts.json.api.wallet.WalletAccountBalance;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.Page;
import sx.neura.btsx.gui.view.components.SearchBox;
import sx.neura.btsx.gui.view.components.flow.Flow;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;
import sx.neura.btsx.gui.view.pages.bts.comp.flow.AccountAndAmountsFlowRenderer;
import sx.neura.btsx.gui.view.popups.bts.impl.CreateAccount;

public class MyAccountList extends Page_Bts {
	
	private static MyAccountList instance;
	public static MyAccountList getInstance() {
		if (!isInstance())
			instance = new MyAccountList();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private MyAccountList() {
	}
	
	@FXML
	protected Node emptyResultsUI;
	@FXML
	protected Node validResultsUI;
	
	@FXML
	private Flow<AccountAndAmounts> listUI;
	
	private List<AccountAndAmounts> listUnfiltered;
	private ObservableList<AccountAndAmounts> listFiltered;
	private String[] accountsPhraseList;
	
	@Override
	public String getName() {
		return "My Accounts";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		listUnfiltered = new ArrayList<AccountAndAmounts>();
		listFiltered = FXCollections.observableArrayList();
		
		listUI.setCellFactory((AccountAndAmounts i) -> {
			AccountAndAmountsFlowRenderer renderer = new AccountAndAmountsFlowRenderer();
			renderer.setItem(i);
			renderer.setActor((AccountAndAmounts j) -> {
				jump(new MyAccountDetail(j.getAccount()));
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
		
		manageButton(action01UI, (MouseEvent event) -> {
			createAccount();
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
				List<Account> accounts = Model.getInstance().getMyAccounts();
				List<WalletAccountBalance.Result> balances = WalletAccountBalance.run();
				for (Account account : accounts) {
					AccountAndAmounts item = new AccountAndAmounts();
					item.setAccount(account);
					item.setAmounts(new ArrayList<Amount>());
					boolean flag = false;
					for (WalletAccountBalance.Result balance : balances) {
						if (balance.getName().equals(account.getName())) {
							for (Amount amount : balance.getAmounts())
								item.getAmounts().add(amount);
							flag = true;
							break;
						}
					}
					if (!flag) {
						Amount amount = new Amount();
						amount.setAsset_id(0);
						amount.setValue(0);
						item.getAmounts().add(amount);
					}
					listUnfiltered.add(item);
				}
			} catch (BTSSystemException e) {
				systemException(e);
			}
			listFiltered.setAll(listUnfiltered);
			if (accountsPhraseList != null)
				applyAccountSearch();
			applyItems();
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		isDataLoaded = false;
		loadData();
	}
	
	private void createAccount() {
		CreateAccount createAccount = new CreateAccount();
		createAccount.setCallback(() -> {
			reloadData();
		});
		createAccount.show(pane);
	}
	
	@Override
	protected SearchBox createSearchBox() {
		return createSearchBox("Search my accounts", 75.0, 150.0);
	}
	
	private synchronized void applyAccountSearch() {
		final int size = listFiltered.size();
		for (int i = size - 1; i >= 0; i--) {
			if (!isSearchMatch(listFiltered.get(i), accountsPhraseList))
				listFiltered.remove(listFiltered.get(i));
		}
		for (AccountAndAmounts item : listUnfiltered) {
			if (!listFiltered.contains(item)
					&& isSearchMatch(item, accountsPhraseList)) {
				listFiltered.add(item);
			}
		}
	}
	
	private synchronized void cancelAccountSearch() {
		for (AccountAndAmounts item : listUnfiltered) {
			if (!listFiltered.contains(item))
				listFiltered.add(item);
		}
	}
	
	private static boolean isSearchMatch(AccountAndAmounts item, String[] phraseList) {
		for (String phrase : phraseList) {
			if (!c(item.getAccount().getName(), phrase))
				return false;
		}
		return true;
	}
}