package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.dto.Transaction;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory.LedgerEntry;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Time;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.components.CellHyperlink;
import sx.neura.btsx.gui.view.components.packer.PackerChoice;
import sx.neura.btsx.gui.view.components.packer.PackerInput;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;
import sx.neura.btsx.gui.view.popups.bts.impl.MakeTransfer;

public class TransactionList extends Page_Bts {
	
	private static TransactionList instance;
	public static TransactionList getInstance() {
		if (!isInstance())
			instance = new TransactionList();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private TransactionList() {
	}
	
	private final double ROW_HEIGHT = 24;
	private final double ROW_OFFSET = 40;
	
	private enum TransactionType {
		All("ALL"), External("external"), Internal("internal"), Market("market"), Cancelation("cancelation"), Virtual("virtual");
		private TransactionType(String label) {
			this.label = label;
		}
		private String label;
		private String getLabel() {
			return label;
		}
	}
	
	private enum TransactionSide {
		All("ALL"), Inflow("inflow"), Outflow("outflow"), Neutral("neutral");
		private TransactionSide(String label) {
			this.label = label;
		}
		private String label;
		private String getLabel() {
			return label;
		}
	}
	
	private enum TransactionTime {
		All("ALL", null),
		today("today", 0),
		yesterday("yesterday", 1),
		D07(null, 7),
		D14(null, 14),
		D30(null, 30),
		D60(null, 60),
		D90(null, 90);
		private TransactionTime(String label, Integer days) {
			this.label = label;
			this.days = days;
		}
		private String label;
		private Integer days;
		private String getLabel() {
			if (label != null)
				return label;
			return String.format("%d %s", days, "days ago");
		}
		private Integer getDays() {
			return days;
		}
	}
	
	@FXML
	private PackerInput fromAccountUI;
	@FXML
	private PackerInput toAccountUI;
	@FXML
	private PackerInput assetUI;
	@FXML
	private PackerInput memoUI;
	@FXML
	private PackerChoice<TransactionType> typeUI;
	@FXML
	private PackerChoice<TransactionSide> sideUI;
	@FXML
	private PackerChoice<TransactionTime> timeFromUI;
	@FXML
	private PackerChoice<TransactionTime> timeToUI;
	
	
//	@FXML
//	private TableView<Transaction> transactionsUI;
//	
//	@FXML
//	private TableColumn<Transaction, String> transactions01UI;
//	@FXML
//	private TableColumn<Transaction, String> transactions02UI;
//	@FXML
//	private TableColumn<Transaction, String> transactions03UI;
//	@FXML
//	private TableColumn<Transaction, String> transactions04UI;
//	@FXML
//	private TableColumn<Transaction, String> transactions05UI;
//	@FXML
//	private TableColumn<Transaction, String> transactions06UI;
//	@FXML
//	private TableColumn<Transaction, String> transactions07UI;
//	@FXML
//	private TableColumn<Transaction, Number> transactions08UI;
	
	@FXML
	private Node emptyResultsUI;
	@FXML
	private Node resultsUI;
	
	@FXML
	private Pagination paginationUI;
	
	private int itemsPerPage;
	private TableView<Transaction> paginationTableView;
	
	private List<Transaction> listUnfiltered;
	private ObservableList<Transaction> listFiltered;
	
	@Override
	public String getName() {
		return "Transactions";
	}
	
	private TableView<Transaction> getPaginationTableView(Integer pageIndex) {
		if (paginationTableView == null) {
			paginationTableView = new TableView<Transaction>();
			paginationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			
			TableColumn<Transaction, String> column01 = new TableColumn<Transaction, String>();
			column01.setText("Trxn Id");
			column01.setPrefWidth(60.0);
			column01.setMinWidth(50.0);
			column01.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getTrx_id()));
			
			TableColumn<Transaction, String> column02 = new TableColumn<Transaction, String>();
			column02.setText("Timestamp");
			column02.setPrefWidth(120.0);
			column02.setMinWidth(100.0);
			column02.setCellValueFactory(item -> new SimpleStringProperty(Time.format(item.getValue().getTimestamp(), Time.Format.DATE_AND_TIME_SHORT_FORMAT)));
			
			TableColumn<Transaction, String> column03A = new TableColumn<Transaction, String>();
			column03A.setText("From");
			column03A.setPrefWidth(90.0);
			column03A.setMinWidth(70.0);
			column03A.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getFrom_account()));
			
			TableColumn<Transaction, String> column03B = new TableColumn<Transaction, String>();
			column03B.setText("To");
			column03B.setPrefWidth(90.0);
			column03B.setMinWidth(70.0);
			column03B.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getTo_account()));
			
			TableColumn<Transaction, String> column04A = new TableColumn<Transaction, String>();
			column04A.setText("Asset");
			column04A.setPrefWidth(40.0);
			column04A.setMinWidth(40.0);
			column04A.setCellValueFactory(item -> new SimpleStringProperty(getAmountPair(item.getValue().getAmount())[0]));
			
			TableColumn<Transaction, String> column04B = new TableColumn<Transaction, String>();
			column04B.setText("Amount");
			column04B.setPrefWidth(100.0);
			column04B.setMinWidth(80.0);
			column04B.setCellValueFactory(item -> new SimpleStringProperty(getAmountPair(item.getValue().getAmount())[1]));
			
			TableColumn<Transaction, String> column05 = new TableColumn<Transaction, String>();
			column05.setText("Memo");
			column05.setPrefWidth(120.0);
			column05.setMinWidth(100.0);
			column05.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getMemo()));
			
			TableColumn<Transaction, Transaction> column06 = new TableColumn<Transaction, Transaction>();
			column06.setText("Block");
			column06.setPrefWidth(50.0);
			column06.setMinWidth(40.0);
			column06.setCellValueFactory(item -> new SimpleObjectProperty<Transaction>(item.getValue()));
			
			TableColumn<Transaction, Number> column03 = new TableColumn<Transaction, Number>();
			column03.setText("Accounts");
			column03.getColumns().add(column03A);
			column03.getColumns().add(column03B);
			
			TableColumn<Transaction, Number> column04 = new TableColumn<Transaction, Number>();
			column04.setText("Transaction");
			column04.getColumns().add(column04A);
			column04.getColumns().add(column04B);
			
			column01.setCellFactory(item -> {
				TableCell<Transaction, String> cell = new TableCell<Transaction, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                	super.updateItem(item, empty);
	                	setText(null);
	                    if (item != null && !empty) {
	                    	CellHyperlink link = new CellHyperlink();
	                    	link.setText(item);
	                    	link.setOnMouseClicked(event -> { 
	                    		jump(new TransactionDetail(listFiltered.get(getTableRow().getIndex())));
	                    	});
	                    	setGraphic(link);
	                    } else {
	                    	setGraphic(null);
	                    }
	                }
	            };
	            return cell;
			});
			
			column03A.setCellFactory(item -> {
				TableCell<Transaction, String> cell = new TableCell<Transaction, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                	super.updateItem(item, empty);
	                    if (item != null && !empty) {
	                    	if (Model.isVirtualName(item)) {
	            	            setText(empty ? null : (item == null ? "" : item.toString()));
	            	            setGraphic(null);
	            	            return;
	                    	}
	                    	CellHyperlink link = new CellHyperlink();
	                    	link.setText(item);
	                    	link.setOnMouseClicked(event -> {
	                    		Account account = getAccountByName(item);
	                    		if (account.isIs_my_account())
	                    			jump(new MyAccountDetail(account));
	                    		else
	                    			jump(new TheirAccountDetail(account));
	                    	});
	                    	setText(null);
	                    	setGraphic(link);
	                    } else {
	                    	setText(null);
	                    	setGraphic(null);
	                    }
	                }
	            };
	            return cell;
			});
			
			column03B.setCellFactory(item -> {
				TableCell<Transaction, String> cell = new TableCell<Transaction, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                	super.updateItem(item, empty);
	                	if (item != null && !empty) {
	                		if (Model.isVirtualName(item)) {
	            	            setText(empty ? null : (item == null ? "" : item.toString()));
	            	            setGraphic(null);
	            	            return;
	                    	}
	                    	CellHyperlink link = new CellHyperlink();
	                    	link.setText(item);
	                    	link.setOnMouseClicked(event -> { 
	                    		Account account = getAccountByName(item);
	                    		if (account.isIs_my_account())
	                    			jump(new MyAccountDetail(account));
	                    		else
	                    			jump(new TheirAccountDetail(account));
	                    	});
	                    	setText(null);
	                    	setGraphic(link);
	                	} else {
	                    	setText(null);
	                    	setGraphic(null);
	                    }
	                }
	            };
	            return cell;
			});
			
			column04A.setCellFactory(item -> {
				TableCell<Transaction, String> cell = new TableCell<Transaction, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                	super.updateItem(item, empty);
	                	setText(null);
	                    if (item != null && !empty) {
	                    	CellHyperlink link = new CellHyperlink();
	                    	link.setText(item);
	                    	link.setOnMouseClicked(event -> { 
	                    		jump(new MyAmountDetail(listFiltered.get(getTableRow().getIndex()).getAmount().getAsset_id()));
	                    	});
	                    	setGraphic(link);
	                    } else {
	                    	setGraphic(null);
	                    }
	                }
	            };
	            return cell;
			});
			
			column06.setCellFactory(item -> {
				TableCell<Transaction, Transaction> cell = new TableCell<Transaction, Transaction>() {
	                @Override
	                public void updateItem(Transaction item, boolean empty) {
	                	super.updateItem(item, empty);
	                    if (item != null && !empty) {
	                    	if (new Integer(item.getBlock_num()) <= 0) {
	                    		setText(item.getBlock_num());
						    	setGraphic(null);
						    	return;
	                    	}
	                    	CellHyperlink link = new CellHyperlink();
	                    	link.setText(item.getBlock_num());
	                    	link.setOnMouseClicked(event -> { 
	                    		jump(new BlockDetail(getItem().getBlock_num()));
	                    	});
	                    	setText(null);
	                    	setGraphic(link);
	                    } else {
	                    	setText(null);
					    	setGraphic(null);
	                    }
	                }
	            };
	            return cell;
			});
			
			adjustTableColumn(column04B, Pos.CENTER_RIGHT);
			
			paginationTableView.getColumns().add(column01);
			paginationTableView.getColumns().add(column02);
			paginationTableView.getColumns().add(column03);
			paginationTableView.getColumns().add(column04);
			paginationTableView.getColumns().add(column05);
			paginationTableView.getColumns().add(column06);
			
			paginationTableView.getStyleClass().add("narrow");
			paginationTableView.getStyleClass().add("unselectable");
		}
		ObservableList<Transaction> sublist = FXCollections.observableArrayList();
		int fromIndex = pageIndex * itemsPerPage;
		int toIndex = Math.min((pageIndex + 1) * itemsPerPage, listFiltered.size());
		sublist.addAll(listFiltered.subList(fromIndex, toIndex));
		paginationTableView.setItems(sublist);
		paginationTableView.getSelectionModel().select(0);
		return paginationTableView;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		listUnfiltered = new ArrayList<Transaction>();
		listFiltered = FXCollections.observableArrayList();
		
		fromAccountUI.setResponder(() -> {
			applySearch();
		});
		toAccountUI.setResponder(() -> {
			applySearch();
		});
		assetUI.setResponder(() -> {
			applySearch();
		});
		memoUI.setResponder(() -> {
			applySearch();
		});
		
		typeUI.setList(Arrays.asList(TransactionType.values()));
		typeUI.setValue(TransactionType.All);
		typeUI.setRenderer((TransactionType i) -> {
			return i.getLabel();
		});
		typeUI.setResponder((TransactionType oldValue, TransactionType newValue) -> {
			applySearch();
		});
		
		sideUI.setList(Arrays.asList(TransactionSide.values()));
		sideUI.setValue(TransactionSide.All);
		sideUI.setRenderer((TransactionSide i) -> {
			return i.getLabel();
		});
		sideUI.setResponder((TransactionSide oldValue, TransactionSide newValue) -> {
			applySearch();
		});
		
		timeFromUI.setList(Arrays.asList(TransactionTime.values()));
		timeFromUI.setValue(TransactionTime.All);
		timeFromUI.setRenderer((TransactionTime i) -> {
			return i.getLabel();
		});
		timeFromUI.setResponder((TransactionTime oldValue, TransactionTime newValue) -> {
			applySearch();
		});
		
		timeToUI.setList(Arrays.asList(TransactionTime.values()));
		timeToUI.setValue(TransactionTime.All);
		timeToUI.setRenderer((TransactionTime i) -> {
			return i.getLabel();
		});
		timeToUI.setResponder((TransactionTime oldValue, TransactionTime newValue) -> {
			applySearch();
		});
		
//		transactionsUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//		transactionsUI.setPlaceholder(new Label("There are no transactions"));
//		transactionsUI.setItems(listFiltered);
//		
//		transactions01UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getTrx_id()));
//		transactions02UI.setCellValueFactory(item -> new SimpleStringProperty(Time.format(item.getValue().getTimestamp(), FormatStyle.SHORT)));
//		
//		transactions03UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getFrom_account()));
//		transactions04UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getTo_account()));
//		
//		transactions05UI.setCellValueFactory(item -> new SimpleStringProperty(getAmountPair(item.getValue().getAmount())[0]));
//		transactions06UI.setCellValueFactory(item -> new SimpleStringProperty(getAmountPair(item.getValue().getAmount())[1]));
//		
//		transactions07UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getMemo()));
//		transactions08UI.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getBlock_num()));
//		
//				
//		transactions01UI.setCellFactory(item -> {
//			TableCell<Transaction, String> cell = new TableCell<Transaction, String>() {
//                @Override
//                public void updateItem(String item, boolean empty) {
//                	super.updateItem(item, empty);
//                	setText(null);
//                    if (item != null && !empty) {
//                    	CellHyperlink link = new CellHyperlink();
//                    	link.setText(item);
//                    	link.setOnMouseClicked(event -> { 
//                    		jump(new TransactionDetail(listFiltered.get(getTableRow().getIndex())));
//                    	});
//                    	setGraphic(link);
//                    } else {
//                    	setGraphic(null);
//                    }
//                }
//            };
//            return cell;
//		});
//		
//		transactions03UI.setCellFactory(item -> {
//			TableCell<Transaction, String> cell = new TableCell<Transaction, String>() {
//                @Override
//                public void updateItem(String item, boolean empty) {
//                	super.updateItem(item, empty);
//                    if (item != null && !empty) {
//                    	if (Model.isVirtualName(item)) {
//            	            setText(empty ? null : (item == null ? "" : item.toString()));
//            	            setGraphic(null);
//            	            return;
//                    	}
//                    	CellHyperlink link = new CellHyperlink();
//                    	link.setText(item);
//                    	link.setOnMouseClicked(event -> {
//                    		Account account = getAccountByName(item);
//                    		if (account.isIs_my_account())
//                    			jump(new MyAccountDetail(account));
//                    		else
//                    			jump(new TheirAccountDetail(account));
//                    	});
//                    	setText(null);
//                    	setGraphic(link);
//                    } else {
//                    	setText(null);
//                    	setGraphic(null);
//                    }
//                }
//            };
//            return cell;
//		});
//		
//		transactions04UI.setCellFactory(item -> {
//			TableCell<Transaction, String> cell = new TableCell<Transaction, String>() {
//                @Override
//                public void updateItem(String item, boolean empty) {
//                	super.updateItem(item, empty);
//                	if (item != null && !empty) {
//                		if (Model.isVirtualName(item)) {
//            	            setText(empty ? null : (item == null ? "" : item.toString()));
//            	            setGraphic(null);
//            	            return;
//                    	}
//                    	CellHyperlink link = new CellHyperlink();
//                    	link.setText(item);
//                    	link.setOnMouseClicked(event -> { 
//                    		Account account = getAccountByName(item);
//                    		if (account.isIs_my_account())
//                    			jump(new MyAccountDetail(account));
//                    		else
//                    			jump(new TheirAccountDetail(account));
//                    	});
//                    	setText(null);
//                    	setGraphic(link);
//                	} else {
//                    	setText(null);
//                    	setGraphic(null);
//                    }
//                }
//            };
//            return cell;
//		});
//		
//		transactions05UI.setCellFactory(item -> {
//			TableCell<Transaction, String> cell = new TableCell<Transaction, String>() {
//                @Override
//                public void updateItem(String item, boolean empty) {
//                	super.updateItem(item, empty);
//                	setText(null);
//                    if (item != null && !empty) {
//                    	CellHyperlink link = new CellHyperlink();
//                    	link.setText(item);
//                    	link.setOnMouseClicked(event -> { 
//                    		jump(new MyAmountDetail(listFiltered.get(getTableRow().getIndex()).getAmount().getAsset_id()));
//                    	});
//                    	setGraphic(link);
//                    } else {
//                    	setGraphic(null);
//                    }
//                }
//            };
//            return cell;
//		});
//		
//		transactions08UI.setCellFactory(item  -> {
//			TableCell<Transaction, Number> cell = new TableCell<Transaction, Number>() {
//                @Override
//                public void updateItem(Number item, boolean empty) {
//                	super.updateItem(item, empty);
//                	setText(null);
//                    if (item != null && !empty) {
//                    	CellHyperlink link = new CellHyperlink();
//                    	link.setText(item.toString());
//                    	link.setOnMouseClicked(event -> { 
//                    		jump(new BlockDetail(listFiltered.get(getTableRow().getIndex()).getBlock_num()));
//                    	});
//                    	setGraphic(link);
//                    } else {
//				    	setGraphic(null);
//                    }
//                }
//            };
//            return cell;
//		});
//		
//		adjustTableColumn(transactions06UI, Pos.CENTER_RIGHT);
		
		itemsPerPage = 0;
		
		paginationUI.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				itemsPerPage = getItemsPerPage();
				paginationUI.setPageCount(getPageCount());
			}
		});
		
		manageButton(action01UI, (MouseEvent event) -> {
			makeTransfer();
		});
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			try {
				listUnfiltered.clear();
				for (WalletAccountTransactionHistory.Result i : WalletAccountTransactionHistory.run("", "", 0)) {
					for (LedgerEntry le : i.getLedger_entries()) {
						Transaction t = new Transaction();
						t.setIs_virtual(i.isIs_virtual());
						t.setIs_confirmed(i.isIs_confirmed());
						t.setIs_market(i.isIs_market());
						t.setIs_market_cancel(i.isIs_market_cancel());
						t.setTrx_id(i.getTrx_id());
						t.setBlock_num(i.getBlock_num());
						t.setFee(i.getFee());
						t.setTimestamp(i.getTimestamp());
						t.setExpiration_timestamp(i.getExpiration_timestamp());
						t.setFrom_account(le.getFrom_account());
						t.setTo_account(le.getTo_account());
						t.setAmount(le.getAmount());
						t.setMemo(le.getMemo());
						t.setRunning_balances(new ArrayList<Transaction.RunningBalance>());
						for (WalletAccountTransactionHistory.RunningBalance rb : le.getRunning_balances()) {
							Transaction.RunningBalance e = new Transaction.RunningBalance();
							e.setAmount(rb.getAmount());
							e.setName(rb.getName());
							t.getRunning_balances().add(e);
						}
						listUnfiltered.add(t);
					}
				}
			} catch (BTSSystemException e) {
				systemException(e);
			}
			listFiltered.setAll(listUnfiltered);
			itemsPerPage = getItemsPerPage();
			applySearch();
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		isDataLoaded = false;
		loadData();
	}
	
	private int getItemsPerPage() {
		return (int) ((paginationUI.getHeight() - ROW_OFFSET) / ROW_HEIGHT);
	}
	
	private int getPageCount() {
		return (listFiltered.size() / itemsPerPage) + (listFiltered.size() % itemsPerPage == 0 ? 0 : 1);
	}
	
	private void resetPagination() {
		paginationUI.setPageCount(getPageCount());
		paginationUI.setCurrentPageIndex(0);
		paginationUI.setPageFactory((Integer pageIndex) -> {
			if (itemsPerPage <= 0)
				return null;
			return getPaginationTableView(pageIndex);
		});
		resultsUI.setVisible(!listFiltered.isEmpty());
		emptyResultsUI.setVisible(listFiltered.isEmpty());
	}
	
	private void applySearch() {
		final int size = listFiltered.size();
		for (int i = size - 1; i >= 0; i--) {
			if (!isSearchMatch(listFiltered.get(i)))
				listFiltered.remove(listFiltered.get(i));
		}
		for (Transaction item : listUnfiltered) {
			if (!listFiltered.contains(item)
					&& isSearchMatch(item)) {
				listFiltered.add(item);
			}
		}
		Collections.sort(listFiltered, (Transaction t1, Transaction t2) -> {
			return t2.getTimestamp().compareTo(t1.getTimestamp());
		});
		resetPagination();
	}
	
	private boolean isSearchMatch(Transaction item) {
		if (!fromAccountUI.getText().isEmpty()
				&& !c(item.getFrom_account(), fromAccountUI.getText()))
			return false;
		if (!toAccountUI.getText().isEmpty()
				&& !c(item.getTo_account(), toAccountUI.getText()))
			return false;
		if (!assetUI.getText().isEmpty()
				&& !c(getAssetById(item.getAmount().getAsset_id()).getSymbol(), assetUI.getText()))
			return false;
		if (!memoUI.getText().isEmpty()
				&& !c(item.getMemo(), memoUI.getText()))
			return false;
		if (!typeUI.getValue().equals(TransactionType.All)) {
			if (typeUI.getValue().equals(TransactionType.External)) {
				if (item.isIs_market() || item.isIs_market_cancel() ||item.isIs_virtual())
					return false;
				Account fromAccount = getAccountByName(item.getFrom_account());
				Account toAccount = getAccountByName(item.getTo_account());
				if ((fromAccount != null && fromAccount.isIs_my_account()) && (toAccount != null && toAccount.isIs_my_account()))
					return false;
			}
			if (typeUI.getValue().equals(TransactionType.Internal)) {
				if (item.isIs_market() || item.isIs_market_cancel() ||item.isIs_virtual())
					return false;
				Account fromAccount = getAccountByName(item.getFrom_account());
				if (fromAccount == null || !fromAccount.isIs_my_account())
					return false;
				Account toAccount = getAccountByName(item.getTo_account());
				if (toAccount == null || !toAccount.isIs_my_account())
					return false;
			}	
			if (typeUI.getValue().equals(TransactionType.Market)) {
				if (!item.isIs_market())
					return false;
			}
			if (typeUI.getValue().equals(TransactionType.Cancelation)) {
				if (!item.isIs_market_cancel())
					return false;
			}
			if (typeUI.getValue().equals(TransactionType.Virtual)) {
				if (!item.isIs_virtual())
					return false;
			}	
		}
		if (!sideUI.getValue().equals(TransactionSide.All)) {
			Account fromAccount = getAccountByName(item.getFrom_account());
			Account toAccount = getAccountByName(item.getTo_account());
			if (sideUI.getValue().equals(TransactionSide.Inflow)) {
				if ((toAccount == null || !toAccount.isIs_my_account()) || (fromAccount != null && fromAccount.isIs_my_account()))
					return false;
			}
			if (sideUI.getValue().equals(TransactionSide.Outflow)) {
				if ((fromAccount == null || !fromAccount.isIs_my_account()) || (toAccount != null && toAccount.isIs_my_account()))
					return false;
			}
			if (sideUI.getValue().equals(TransactionSide.Neutral)) {
				if ((fromAccount == null || !fromAccount.isIs_my_account()) || (toAccount == null || !toAccount.isIs_my_account()))
					return false;
			}
		}
		if (!timeFromUI.getValue().equals(TransactionTime.All)) {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime cut = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0).minusDays(timeFromUI.getValue().getDays());
			if (Time.decode(item.getTimestamp()).isAfter(cut))
				return false;
		}
		if (!timeToUI.getValue().equals(TransactionTime.All)) {
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime cut = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0).minusDays(timeToUI.getValue().getDays());
			if (Time.decode(item.getTimestamp()).isBefore(cut))
				return false;
		}
		return true;
	}
	
	private void makeTransfer() {
		MakeTransfer makeTransfer = new MakeTransfer();
		makeTransfer.show(pane);
	}

}