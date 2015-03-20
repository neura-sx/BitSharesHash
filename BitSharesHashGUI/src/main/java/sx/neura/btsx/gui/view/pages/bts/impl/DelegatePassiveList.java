package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import sx.neura.bts.json.api.blockchain.BlockchainListDelegates;
import sx.neura.bts.json.api.wallet.WalletAccountSetApproval;
import sx.neura.bts.json.api.wallet.WalletListAccounts;
import sx.neura.bts.json.dto.RegisteredName;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Time;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.components.SearchBox;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;
import sx.neura.btsx.gui.view.popups.bts.impl.ShowPublicKey;

public class DelegatePassiveList extends Page_Bts {
	
	private static DelegatePassiveList instance;
	public static DelegatePassiveList getInstance() {
		if (!isInstance())
			instance = new DelegatePassiveList();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private DelegatePassiveList() {
	}
	
	private final double ROW_HEIGHT = 24;
	private final double ROW_OFFSET = 40;
	
	private List<RegisteredName> list;
	private List<RegisteredName> listUnfiltered;
	private ObservableList<RegisteredName> listFiltered;
	private String[] accountsPhraseList;
	
	private RegisteredName item;
	private TableView<RegisteredName> paginationTableView;
	private int itemsPerPage;
	
	private ToggleGroup toggleGroup;
	
	@FXML
	private Node alphabetUI;
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
	@FXML
	private PackerText registrationDateUI;
	@FXML
	private PackerText lastUpdateUI;
	
	@FXML
	private ImageView avatarUI;
	
	@Override
	public String getName() {
		return "Standby Delegates";
	}
	
	private TableView<RegisteredName> getPaginationTableView(Integer pageIndex) {
		if (paginationTableView == null) {
			paginationTableView = new TableView<RegisteredName>();
			paginationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			paginationTableView.setRowFactory(new Callback<TableView<RegisteredName>, TableRow<RegisteredName>>() {
				@Override
				public TableRow<RegisteredName> call(TableView<RegisteredName> param) {
					TableRow<RegisteredName> row = new TableRow<RegisteredName>() {
						@Override
		                protected void updateItem(RegisteredName item, boolean empty) {
		                    super.updateItem(item, empty);
		                    if (item != null) {
		                    	getStyleClass().remove("delegate-passive-positive");
		                    	getStyleClass().remove("delegate-passive-negative");
		                    	getStyleClass().remove("delegate-neutral");
		                    	int approval = getAccountApproval(item);
		                    	getStyleClass().add(approval > 0 ? "delegate-passive-positive" : (approval < 0 ? "delegate-passive-negative" : "delegate-neutral"));
		                    }
		                }
					};
		            return row;
				}
			});
			
			TableColumn<RegisteredName, String> column01 = new TableColumn<RegisteredName, String>();
			column01.setText("Name");
			column01.setPrefWidth(150.0);
			column01.setMinWidth(120.0);
			column01.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getName()));
			
			TableColumn<RegisteredName, String> column02 = new TableColumn<RegisteredName, String>();
			column02.setText("Support");
			column02.setPrefWidth(50.0);
			column02.setMinWidth(40.0);
			column02.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%.2f%s", getDelegateSupport(item.getValue()) * 100, "%")));
			
			TableColumn<RegisteredName, String> column03 = new TableColumn<RegisteredName, String>();
			column03.setText("Reliability");
			column03.setPrefWidth(50.0);
			column03.setMinWidth(40.0);
			column03.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%.2f%s", getDelegateReliability(item.getValue()) * 100, "%")));
			
			TableColumn<RegisteredName, String> column04 = new TableColumn<RegisteredName, String>();
			column04.setText("Blocks");
			column04.setPrefWidth(80.0);
			column04.setMinWidth(60.0);
			column04.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%d", item.getValue().getDelegate_info().getBlocks_produced())));
			
			TableColumn<RegisteredName, String> column05 = new TableColumn<RegisteredName, String>();
			column05.setText("Pay Rate");
			column05.setPrefWidth(50.0);
			column05.setMinWidth(40.0);
			column05.setCellValueFactory(item -> new SimpleStringProperty(
					String.format("%d%s", item.getValue().getDelegate_info().getPay_rate(), "%")));
			
			TableColumn<RegisteredName, String> column06 = new TableColumn<RegisteredName, String>();
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

			paginationTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<RegisteredName>() {
				@Override
				public void changed(ObservableValue<? extends RegisteredName> observable, RegisteredName oldValue, RegisteredName newValue) {
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
		ObservableList<RegisteredName> sublist = FXCollections.observableArrayList();
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
			listUnfiltered.clear();
			if (firstSymbol != null) {
				for (RegisteredName name : list) {
					if (firstSymbol == "" || name.getName().startsWith(firstSymbol))
						listUnfiltered.add(name);
				}
			}
			Platform.runLater(() -> {
				listFiltered.setAll(listUnfiltered);
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
		
		list = new ArrayList<RegisteredName>();
		listUnfiltered = new ArrayList<RegisteredName>();
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
			try {
				list.clear();
				for (RegisteredName name : BlockchainListDelegates.run(0, Model.DELEGATES_ALL_LIMIT)) {
					if (!isActiveDelegate(name))
						list.add(name);
				}
			} catch (BTSSystemException e) {
				systemException(e);
			}
			toggleGroup.selectToggle(allUI);
			onAll();
		}
		super.loadData();
	}
	
	private void resetPageFactory() {
		paginationUI.setPageFactory((Integer pageIndex) -> {
			if (itemsPerPage <= 0)
				return null;
			return getPaginationTableView(pageIndex);
		});
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
			List<RegisteredName> temp = BlockchainListDelegates.run(0, Model.DELEGATES_ALL_LIMIT);
			for (RegisteredName t : temp) {
				if (t.getName().equals(item.getName())) {
					paginationTableView.getItems().set(paginationTableView.getItems().indexOf(item), t);
					break;
				}
			}
		} catch (BTSSystemException e) {
			systemException(e);
		}
		DelegateVotedList.getInstance().reloadData();
	}
	
	@Override
	protected SearchBox createSearchBox() {
		return createSearchBox("Search delegates", 90.0, 150.0);
	}
	
	private synchronized void applyAccountSearch() {
		if (accountsPhraseList != null) {
			final int size = listFiltered.size();
			for (int i = size - 1; i >= 0; i--) {
				if (!isSearchMatch(listFiltered.get(i), accountsPhraseList))
					listFiltered.remove(listFiltered.get(i));
			}
			for (RegisteredName item : listUnfiltered) {
				if (!listFiltered.contains(item)
						&& isSearchMatch(item, accountsPhraseList)) {
					listFiltered.add(item);
				}
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
