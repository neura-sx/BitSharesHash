package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import sx.neura.bts.json.api.wallet.WalletAccountSetApproval;
import sx.neura.bts.json.api.wallet.WalletListAccounts;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Time;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.components.SearchBox;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;
import sx.neura.btsx.gui.view.popups.bts.impl.ShowPublicKey;

public class DelegateVotedList extends Page_Bts {
	
	private static DelegateVotedList instance;
	public static DelegateVotedList getInstance() {
		if (!isInstance())
			instance = new DelegateVotedList();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private DelegateVotedList() {
	}
	
	private final double ROW_HEIGHT = 24;
	private final double ROW_OFFSET = 40;
	
	private List<Account> listUnfiltered;
	private ObservableList<Account> listFiltered;
	private String[] accountsPhraseList;
	
	private Account item;
	private TableView<Account> paginationTableView;
	private int itemsPerPage;
	
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
	@FXML
	private PackerText registrationDateUI;
	@FXML
	private PackerText lastUpdateUI;
	
	@FXML
	private ImageView avatarUI;
	
	@Override
	public String getName() {
		return "My Votes";
	}
	
	private TableView<Account> getPaginationTableView(Integer pageIndex) {
		if (paginationTableView == null) {
			paginationTableView = new TableView<Account>();
			paginationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			paginationTableView.setRowFactory(new Callback<TableView<Account>, TableRow<Account>>() {
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
			
			TableColumn<Account, String> column01 = new TableColumn<Account, String>();
			column01.setText("Name");
			column01.setPrefWidth(150.0);
			column01.setMinWidth(120.0);
			column01.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getName()));
			
			TableColumn<Account, String> column02 = new TableColumn<Account, String>();
			column02.setText("Support");
			column02.setPrefWidth(50.0);
			column02.setMinWidth(40.0);
			column02.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%.2f%s", getDelegateSupport(item.getValue()) * 100, "%")));
			
			TableColumn<Account, String> column03 = new TableColumn<Account, String>();
			column03.setText("Reliability");
			column03.setPrefWidth(50.0);
			column03.setMinWidth(40.0);
			column03.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%.2f%s", getDelegateReliability(item.getValue()) * 100, "%")));
			
			TableColumn<Account, String> column04 = new TableColumn<Account, String>();
			column04.setText("Blocks");
			column04.setPrefWidth(80.0);
			column04.setMinWidth(60.0);
			column04.setCellValueFactory(item -> new SimpleStringProperty(
					item.getValue().getDelegate_info() != null ? String.format("%d", item.getValue().getDelegate_info().getBlocks_produced()) : ""));
			
			TableColumn<Account, String> column05 = new TableColumn<Account, String>();
			column05.setText("Pay Rate");
			column05.setPrefWidth(50.0);
			column05.setMinWidth(40.0);
			column05.setCellValueFactory(item -> new SimpleStringProperty(
					item.getValue().getDelegate_info() != null ? String.format("%d%s", item.getValue().getDelegate_info().getPay_rate(), "%") : ""));
			
			TableColumn<Account, String> column06 = new TableColumn<Account, String>();
			column06.setText("Approval");
			column06.setPrefWidth(50.0);
			column06.setMinWidth(40.0);
			column06.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%s", getAccountApprovalLabel(item.getValue()))));
			
			adjustTableColumn(column02, Pos.CENTER_RIGHT);
			adjustTableColumn(column03, Pos.CENTER_RIGHT);
			adjustTableColumn(column04, Pos.CENTER_RIGHT);
			adjustTableColumn(column05, Pos.CENTER_RIGHT);
			adjustTableColumn(column06, Pos.CENTER);
			
			paginationTableView.getColumns().add(column01);
			paginationTableView.getColumns().add(column02);
			paginationTableView.getColumns().add(column03);
			paginationTableView.getColumns().add(column04);
			paginationTableView.getColumns().add(column05);
			paginationTableView.getColumns().add(column06);

			paginationTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Account>() {
				@Override
				public void changed(ObservableValue<? extends Account> observable, Account oldValue, Account newValue) {
					if (newValue != null) {
						item = newValue;
						showPreview();
						int approval = getAccountApproval(item);
						if (approval == 1) {
							action01UI.setDisable(true);
							action02UI.setDisable(false);
							action03UI.setDisable(false);
						} else if (approval == 0) {
							action01UI.setDisable(false);
							action02UI.setDisable(true);
							action03UI.setDisable(false);
						} else if (approval == -1) {
							action01UI.setDisable(false);
							action02UI.setDisable(false);
							action03UI.setDisable(true);
						}
					}
				}
			});
			
			paginationTableView.getStyleClass().add("narrow");
			paginationTableView.getStyleClass().add("selectable");
		}
		ObservableList<Account> sublist = FXCollections.observableArrayList();
		paginationTableView.setItems(sublist);
		if (listFiltered.size() > 0) {
			int fromIndex = pageIndex * itemsPerPage;
			int toIndex = Math.min((pageIndex + 1) * itemsPerPage, listFiltered.size());
			sublist.addAll(listFiltered.subList(fromIndex, toIndex));
			if (item != null) {
				int index = sublist.indexOf(item);
				paginationTableView.getSelectionModel().select(index >= 0 ? index : 0);
			} else {
				paginationTableView.getSelectionModel().select(0);
			}
		}
		return paginationTableView;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		paginationUI.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				itemsPerPage = getItemsPerPage();
				paginationUI.setPageCount(getPageCount());
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
		
		listUnfiltered = new ArrayList<Account>();
		listFiltered = FXCollections.observableArrayList();
		
		searchBoxUI.setTarget(new SearchBox.Target() {
			@Override
			public void applySearch(String[] phraseList) {
				accountsPhraseList = phraseList;
				applyAccountSearch();
				item = null;
				resetPagination();
			}
			@Override
			public void cancelSearch() {
				accountsPhraseList = null;
				cancelAccountSearch();
				item = null;
				resetPagination();
			}
		});
		
		rightWingUI.disableProperty().bind(resultsUI.visibleProperty().not());
		searchBoxUI.disableProperty().bind(resultsUI.visibleProperty().not().and(emptyResultsUI.visibleProperty().not()));
		
		manageButton(action01UI, (MouseEvent event) -> {
			toggleApproval(1);
		});
		manageButton(action02UI, (MouseEvent event) -> {
			toggleApproval(0);
		});
		manageButton(action03UI, (MouseEvent event) -> {
			toggleApproval(-1);
		});
		
		paginationUI.setPageFactory((Integer pageIndex) -> {
			if (itemsPerPage <= 0)
				return null;
			return getPaginationTableView(pageIndex);
		});
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
			listUnfiltered.clear();
			for (Account account : Model.getInstance().getAccounts()) {
				if (account.getApproved() != 0)
					listUnfiltered.add(account);
			}
			listFiltered.setAll(listUnfiltered);
			applyAccountSearch();
			item = null;
			itemsPerPage = getItemsPerPage();
			paginationUI.setPageCount(getPageCount());
			resetPagination();
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		isDataLoaded = false;
		loadData();
	}
	
	private void resetPagination() {
		paginationUI.setPageCount(getPageCount());
		paginationUI.setCurrentPageIndex(0);
		resultsUI.setVisible(!listFiltered.isEmpty());
		emptyResultsUI.setVisible(listFiltered.isEmpty());
	}
	
	private void showPreview() {
		nameUI.setText(item.getName());
		publicKeyUI.setText(item.getOwner_key());
		registrationDateUI.setText(Time.format(item.getRegistration_date()));
		lastUpdateUI.setText(Time.format(item.getLast_update(), "never"));
		avatarUI.setImage(getAvatarImage(item.getName()));
	}
	
	private void toggleApproval(int approval) {
		try {
			WalletAccountSetApproval.run(item.getName(), approval);
			Model.getInstance().setAccounts(WalletListAccounts.run());
		} catch (BTSSystemException e) {
			systemException(e);
		}
		if (approval == 0) {
			listUnfiltered.remove(item);
			listFiltered.remove(item);
			paginationTableView.getItems().remove(item);
			if (!paginationTableView.getItems().isEmpty())
				paginationTableView.getSelectionModel().select(0);
			return;
		}
		for (Account t : Model.getInstance().getAccounts()) {
			if (t.getName().equals(item.getName())) {
				t.setApproved(approval);
				listUnfiltered.set(listUnfiltered.indexOf(item), t);
				listFiltered.set(listFiltered.indexOf(item), t);
				paginationTableView.getItems().set(paginationTableView.getItems().indexOf(item), t);
				item = t;
				break;
			}
		}
	}
	
	@Override
	protected SearchBox createSearchBox() {
		return createSearchBox("Search delegates", 75.0, 150.0);
	}
	
	private synchronized void applyAccountSearch() {
		if (accountsPhraseList != null) {
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
		Collections.sort(listFiltered);
	}
	
	private synchronized void cancelAccountSearch() {
		for (Account item : listUnfiltered) {
			if (!listFiltered.contains(item))
				listFiltered.add(item);
		}
		Collections.sort(listFiltered);
	}
	
	private static boolean isSearchMatch(Account item, String[] phraseList) {
		for (String phrase : phraseList) {
			if (!c(item.getName(), phrase))
				return false;
		}
		return true;
	}
}
