package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import sx.neura.bts.json.api.blockchain.BlockchainListAccounts;
import sx.neura.bts.json.api.wallet.WalletAccountSetFavorite;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.RegisteredName;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Time;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.components.SearchBox;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;
import sx.neura.btsx.gui.view.popups.ReadBoolean;
import sx.neura.btsx.gui.view.popups.bts.impl.MakeTransfer;
import sx.neura.btsx.gui.view.popups.bts.impl.ShowPublicKey;

public class RegisteredNameList extends Page_Bts {
	
	private static RegisteredNameList instance;
	public static RegisteredNameList getInstance() {
		if (!isInstance())
			instance = new RegisteredNameList();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private RegisteredNameList() {
	}
	
	private final double ROW_HEIGHT = 24;
	private final double ROW_OFFSET = 40;
	
	private List<RegisteredName> listUnfiltered;
	private ObservableList<RegisteredName> listFiltered;
	private RegisteredName item;
	
	private ToggleGroup toggleGroup;
	
	@FXML
	private ToggleButton allUI;
	@FXML
	private ToggleButton aUI;
	@FXML
	private ToggleButton bUI;
	@FXML
	private ToggleButton cUI;
	@FXML
	private ToggleButton dUI;
	@FXML
	private ToggleButton eUI;
	@FXML
	private ToggleButton fUI;
	@FXML
	private ToggleButton gUI;
	@FXML
	private ToggleButton hUI;
	@FXML
	private ToggleButton iUI;
	@FXML
	private ToggleButton jUI;
	@FXML
	private ToggleButton kUI;
	@FXML
	private ToggleButton lUI;
	@FXML
	private ToggleButton mUI;
	@FXML
	private ToggleButton nUI;
	@FXML
	private ToggleButton oUI;
	@FXML
	private ToggleButton pUI;
	@FXML
	private ToggleButton qUI;
	@FXML
	private ToggleButton rUI;
	@FXML
	private ToggleButton sUI;
	@FXML
	private ToggleButton tUI;
	@FXML
	private ToggleButton uUI;
	@FXML
	private ToggleButton vUI;
	@FXML
	private ToggleButton wUI;
	@FXML
	private ToggleButton xUI;
	@FXML
	private ToggleButton yUI;
	@FXML
	private ToggleButton zUI;
	
	@FXML
	private Node alphabetUI;
	@FXML
	private Node progressIndicatorUI;
	@FXML
	private Node emptyResultsUI;
	@FXML
	private Node resultsUI;
	
	@FXML
	private Pagination paginationUI;
	
	@FXML
	private PackerText nameUI;
	@FXML
	private PackerText publicKeyUI;
//	@FXML
//	private PackerText metaDataUI;
	@FXML
	private PackerText registrationDateUI;
	@FXML
	private PackerText lastUpdateUI;
	
	@FXML
	private ImageView avatarUI;
	
	private int itemsPerPage;
	private ListView<RegisteredName> paginationListView;
	private String[] accountPhraseList;
	
	@Override
	public String getName() {
		return "Registered Names";
	}
	
	private ListView<RegisteredName> getPaginationListView(Integer pageIndex) {
		if (paginationListView == null) {
			paginationListView = new ListView<RegisteredName>();
			paginationListView.setCellFactory(new Callback<ListView<RegisteredName>, ListCell<RegisteredName>>() {
	            @Override 
	            public ListCell<RegisteredName> call(ListView<RegisteredName> list) {
	                return new ListCell<RegisteredName>() {
	                    @Override
	                    protected void updateItem(RegisteredName item, boolean empty) {
	                        super.updateItem(item, empty);
	                        if (item != null && !empty) {
                        		getStyleClass().remove("registeredName-DelegateActiveAccountSC");
                        		getStyleClass().remove("registeredName-DelegatePassiveAccountSC");
                        		getStyleClass().remove("registeredName-FavoriteAccountSC");
	                        	if (isDelegate(item)) {
	                        		getStyleClass().add(isActiveDelegate(item) ?
	                        				"registeredName-DelegateActiveAccountSC" : "registeredName-DelegatePassiveAccountSC");
	                        	}
                        		if (isItemFavorite(item)) {
                        			getStyleClass().add("registeredName-FavoriteAccountSC");
                        			setText(String.format("%s%s", item.getName(), "*"));
                        		} else {
	                        		setText(item.getName());
	                        	}
	                        } else {
	                        	setText(null);
	                        }
	                    }
	                };
	            }
	        });
			paginationListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RegisteredName>() {
				@Override
				public void changed(ObservableValue<? extends RegisteredName> observable, RegisteredName oldValue, RegisteredName newValue) {
					if (newValue != null) {
						item = newValue;
						showPreview();
					}
				}
			});
			paginationListView.getStyleClass().add("transList");
		}
		ObservableList<RegisteredName> sublist = FXCollections.observableArrayList();
		paginationListView.setItems(sublist);
		if (listFiltered.size() > 0) {
			int fromIndex = pageIndex * itemsPerPage;
			int toIndex = Math.min((pageIndex + 1) * itemsPerPage, listFiltered.size());
			sublist.addAll(listFiltered.subList(fromIndex, toIndex));
			if (item != null) {
				int index = sublist.indexOf(item);
				paginationListView.getSelectionModel().select(index >= 0 ? index : 0);
			} else {
				paginationListView.getSelectionModel().select(0);
			}
		}
		return paginationListView;
	}
	
	private class LoadingTask extends Task<Void> {
		private String firstSymbol;
		private LoadingTask(String firstSymbol) {
			this.firstSymbol = firstSymbol;
		}
		@Override
		protected Void call() {
			Platform.runLater(() -> {
				alphabetUI.setDisable(true);
				resultsUI.setVisible(false);
				emptyResultsUI.setVisible(false);
				progressIndicatorUI.setVisible(true);
            });
			List<RegisteredName> temp = new ArrayList<RegisteredName>();
			if (firstSymbol != null) {
				try {
					if (firstSymbol != "") {
						temp.addAll(BlockchainListAccounts.run(firstSymbol, Model.REGISTERED_NAMES_LIMIT));
						for (int i = temp.size() - 1; i >= 0; i--) {
							if (temp.get(i).getName().startsWith(firstSymbol))
								break;
							temp.remove(i);
						}
					} else {
						temp.addAll(BlockchainListAccounts.run("", Model.REGISTERED_NAMES_LIMIT_ALL));
					}
				} catch (BTSSystemException e) {
					Platform.runLater(() -> {
						alphabetUI.setDisable(false);
						progressIndicatorUI.setVisible(false);
						systemException(e);
		            });
				}
			}
			Platform.runLater(() -> {
				listUnfiltered.clear();
				listUnfiltered.addAll(temp);
				listFiltered.setAll(temp);
				if (accountPhraseList != null)
					applyAccountSearch();
				item = null;
				resetPageFactory();
				resetPagination();
				alphabetUI.setDisable(false);
				progressIndicatorUI.setVisible(false);
            });
			return null;
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		toggleGroup = new ToggleGroup();
		allUI.setToggleGroup(toggleGroup);
		aUI.setToggleGroup(toggleGroup);
		bUI.setToggleGroup(toggleGroup);
		cUI.setToggleGroup(toggleGroup);
		dUI.setToggleGroup(toggleGroup);
		eUI.setToggleGroup(toggleGroup);
		fUI.setToggleGroup(toggleGroup);
		gUI.setToggleGroup(toggleGroup);
		hUI.setToggleGroup(toggleGroup);
		iUI.setToggleGroup(toggleGroup);
		jUI.setToggleGroup(toggleGroup);
		kUI.setToggleGroup(toggleGroup);
		lUI.setToggleGroup(toggleGroup);
		mUI.setToggleGroup(toggleGroup);
		nUI.setToggleGroup(toggleGroup);
		oUI.setToggleGroup(toggleGroup);
		pUI.setToggleGroup(toggleGroup);
		qUI.setToggleGroup(toggleGroup);
		rUI.setToggleGroup(toggleGroup);
		sUI.setToggleGroup(toggleGroup);
		tUI.setToggleGroup(toggleGroup);
		uUI.setToggleGroup(toggleGroup);
		vUI.setToggleGroup(toggleGroup);
		wUI.setToggleGroup(toggleGroup);
		xUI.setToggleGroup(toggleGroup);
		yUI.setToggleGroup(toggleGroup);
		zUI.setToggleGroup(toggleGroup);
		
		paginationUI.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				itemsPerPage = getItemsPerPage();
				paginationUI.setPageCount(getPageCount());
				paginationUI.setCurrentPageIndex(0);
			}
		});
		
		nameUI.setCallback(new PackerText.Callback() {
			@Override
			public void onAction() {
				jump(new TheirAccountDetail(getAccount(item.getName())));
			}
		});
		
		publicKeyUI.setCallback(new PackerText.Callback() {
			@Override
			public void onAction() {
				ShowPublicKey showPublicKey = new ShowPublicKey();
				showPublicKey.setName(item.getName());
				showPublicKey.setPublicKey(item.getOwner_key());
				showPublicKey.show(pane);
			}
		});
		
		listUnfiltered = new ArrayList<RegisteredName>();
		listFiltered = FXCollections.observableArrayList();
		
		searchBoxUI.setTarget(new SearchBox.Target() {
			@Override
			public void applySearch(String[] phraseList) {
				accountPhraseList = phraseList;
				applyAccountSearch();
				item = null;
				resetPagination();
			}
			@Override
			public void cancelSearch() {
				accountPhraseList = null;
				cancelAccountSearch();
				item = null;
				resetPagination();
			}
		});
		
		rightWingUI.disableProperty().bind(resultsUI.visibleProperty().not());
		searchBoxUI.disableProperty().bind(resultsUI.visibleProperty().not().and(emptyResultsUI.visibleProperty().not()));
		
		manageButton(action01UI, (MouseEvent event) -> {
			makeTransfer();
		});
		manageButton(action02UI, (MouseEvent event) -> {
			addToFavorites();
		});
		manageButton(action03UI, (MouseEvent event) -> {
			removeFromFavorites();
		});
	}
	
	@FXML
	private void onAll() {
		loadSection(allUI.isSelected() ? "" : null);
	}
	
	@FXML
	private void onSection(Event event) {
		ToggleButton button = (ToggleButton) event.getSource();
		String firstSymbol = button.isSelected() ? button.getText().toLowerCase() : null;
		loadSection(firstSymbol);
	}
	
	private void loadSection(String firstSymbol) {
		new Thread(new LoadingTask(firstSymbol)).start();
	}
	
	private int getItemsPerPage() {
		return (int) ((paginationUI.getHeight() - ROW_OFFSET) / ROW_HEIGHT);
	}
	
	private int getPageCount() {
		return (listFiltered.size() / itemsPerPage) + (listFiltered.size() % itemsPerPage == 0 ? 0 : 1);
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			
		}
		super.loadData();
	}
	
	private void resetPageFactory() {
		paginationUI.setPageFactory((Integer pageIndex) -> {
			return getPaginationListView(pageIndex);
		});
	}
	
	private void resetPagination() {
		paginationUI.setPageCount(getPageCount());
		paginationUI.setCurrentPageIndex(0);
		resultsUI.setVisible(!listFiltered.isEmpty());
		emptyResultsUI.setVisible(listFiltered.isEmpty());
	}
	
	private void showPreview() {
		boolean isFavorite = isItemFavorite(item);
		nameUI.getTextUI().getStyleClass().remove("registeredName-DelegateActiveAccountSC");
		nameUI.getTextUI().getStyleClass().remove("registeredName-DelegatePassiveAccountSC");
		nameUI.getTextUI().getStyleClass().remove("registeredName-FavoriteAccountSC");
		if (isDelegate(item)) {
			nameUI.getTextUI().getStyleClass().add(isActiveDelegate(item) ?
    				"registeredName-DelegateActiveAccountSC" : "registeredName-DelegatePassiveAccountSC");
    	}
		if (isFavorite) {
			nameUI.getTextUI().getStyleClass().add("registeredName-FavoriteAccountSC");
			nameUI.setText(String.format("%s%s", item.getName(), "*"));
		} else {
    		nameUI.setText(item.getName());
    	}
		
		publicKeyUI.setText(item.getOwner_key());
//		metaDataUI.setText(item.getMeta_data());
		registrationDateUI.setText(Time.format(item.getRegistration_date()));
		lastUpdateUI.setText(Time.format(item.getLast_update(), "never"));
		avatarUI.setImage(getAvatarImage(item.getName()));
		action02UI.setVisible(!isFavorite);
		action03UI.setVisible(isFavorite);
	}
	
	private void makeTransfer() {
		MakeTransfer makeTransfer = new MakeTransfer();
		makeTransfer.setToExternalAccount(getAccount(item.getName()));
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
				} catch (BTSSystemException e) {
					systemException(e);
				}
				int currentPageIndex = paginationUI.getCurrentPageIndex();
				RegisteredName currentItem = item;
				resetPageFactory();
				item = currentItem;
				paginationUI.setCurrentPageIndex(currentPageIndex);
				FavoriteAccountList.getInstance().reloadData();
			}
			@Override
			public void onCancel() {
			}
		});
		readBoolean.show(pane);
	}
	
	private void removeFromFavorites() {
		ReadBoolean readBoolean = new ReadBoolean();
		readBoolean.setTitle("Remove From Favorites");
		readBoolean.setMessage(String.format("Are you sure you want to remove %s from favorites?", item.getName()));
		readBoolean.setConfirm("Yes");
		readBoolean.setCancel("Cancel");
		readBoolean.setCallback(new ReadBoolean.Callback() {
			@Override
			public void onConfirm() {
				try {
					WalletAccountSetFavorite.run(item.getName(), false);
				} catch (BTSSystemException e) {
					systemException(e);
				}
				int currentPageIndex = paginationUI.getCurrentPageIndex();
				RegisteredName currentItem = item;
				resetPageFactory();
				item = currentItem;
				paginationUI.setCurrentPageIndex(currentPageIndex);
				FavoriteAccountList.getInstance().reloadData();
			}
			@Override
			public void onCancel() {
			}
		});
		readBoolean.show(pane);
	}
	
	private static boolean isItemFavorite(RegisteredName item) {
		Account account = getAccountByName(item.getName());
    	if (account != null)
    		return account.isIs_favorite();
    	return false;
	}
	
	@Override
	protected SearchBox createSearchBox() {
		return createSearchBox("Search names", 90.0, 150.0);
	}
	
	private synchronized void applyAccountSearch() {
		final int size = listFiltered.size();
		for (int i = size - 1; i >= 0; i--) {
			if (!isSearchMatch(listFiltered.get(i), accountPhraseList))
				listFiltered.remove(listFiltered.get(i));
		}
		for (RegisteredName item : listUnfiltered) {
			if (!listFiltered.contains(item)
					&& isSearchMatch(item, accountPhraseList)) {
				listFiltered.add(item);
			}
		}
		Collections.sort(listFiltered);
	}
	
	private synchronized void cancelAccountSearch() {
		for (RegisteredName item : listUnfiltered) {
			if (!listFiltered.contains(item))
				listFiltered.add(item);
		}
		Collections.sort(listFiltered);
	}
	
	private static boolean isSearchMatch(RegisteredName item, String[] phraseList) {
		for (String phrase : phraseList) {
			if (!c(item.getName(), phrase))
				return false;
		}
		return true;
	}
}
