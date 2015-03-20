package sx.neura.btsx.gui.view.pages.bts.comp;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sx.neura.bts.gui.dto.Transaction;
import sx.neura.bts.gui.view.FXMLComponent;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory.LedgerEntry;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.util.Time;
import sx.neura.bts.util.Util;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.Page;
import sx.neura.btsx.gui.view.components.CellHyperlink;
import sx.neura.btsx.gui.view.pages.bts.impl.BlockDetail;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAccountDetail;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAmountDetail;
import sx.neura.btsx.gui.view.pages.bts.impl.TheirAccountDetail;
import sx.neura.btsx.gui.view.pages.bts.impl.TransactionDetail;

public class Transactions extends FXMLComponent implements Initializable {
	
	private Page page;
	private ObservableList<Transaction> list;
	
	public void setPage(Page page) {
		this.page = page;
	}
	
	public void setAll(List<WalletAccountTransactionHistory.Result> items) {
		setAll(items, null);
	}
	
	public void setAll(List<WalletAccountTransactionHistory.Result> items, Asset asset) {
		list.clear();
		for (WalletAccountTransactionHistory.Result i : items) {
			if (i.isIs_virtual())
				continue;
			for (LedgerEntry le : i.getLedger_entries()) {
				if (asset != null && le.getAmount().getAsset_id() != asset.getId())
					continue;
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
				list.add(t);
			}
		}
		Collections.sort(list, (Transaction t1, Transaction t2) -> {
			return t2.getTimestamp().compareTo(t1.getTimestamp());
		});
		Util.resetTableViewScroll(transactionsUI);
	}
	
	@FXML
	private TableView<Transaction> transactionsUI;
	
	@FXML
	private TableColumn<Transaction, String> transactions01UI;
	@FXML
	private TableColumn<Transaction, String> transactions02UI;
	@FXML
	private TableColumn<Transaction, String> transactions03UI;
	@FXML
	private TableColumn<Transaction, String> transactions04UI;
	@FXML
	private TableColumn<Transaction, String> transactions05UI;
	@FXML
	private TableColumn<Transaction, String> transactions06UI;
	@FXML
	private TableColumn<Transaction, String> transactions07UI;
	@FXML
	private TableColumn<Transaction, Transaction> transactions08UI;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		list = FXCollections.observableArrayList();
		
		transactionsUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		transactionsUI.setPlaceholder(new Label("There are no transactions"));
		transactionsUI.setItems(list);
		
		transactions01UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getTrx_id()));
		transactions02UI.setCellValueFactory(item -> new SimpleStringProperty(Time.format(item.getValue().getTimestamp(), Time.Format.DATE_AND_TIME_SHORT_FORMAT)));
		
		transactions03UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getFrom_account()));
		transactions04UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getTo_account()));
		
		transactions05UI.setCellValueFactory(item -> new SimpleStringProperty(Model.getInstance().getAmountPair(item.getValue().getAmount())[0]));
		transactions06UI.setCellValueFactory(item -> new SimpleStringProperty(Model.getInstance().getAmountPair(item.getValue().getAmount())[1]));
		
		transactions07UI.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getMemo()));
		transactions08UI.setCellValueFactory(item -> new SimpleObjectProperty<Transaction>(item.getValue()));
		
				
		transactions01UI.setCellFactory(item  -> {
			TableCell<Transaction, String> cell = new TableCell<Transaction, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                	super.updateItem(item, empty);
                	setText(null);
                    if (item != null && !empty) {
                    	CellHyperlink link = new CellHyperlink();
                    	link.setText(item);
                    	link.setOnMouseClicked(event -> { 
                    		page.jump(new TransactionDetail(list.get(getTableRow().getIndex())));
                    	});
                    	setGraphic(link);
                    } else {
                    	setGraphic(null);
                    }
                }
            };
            return cell;
		});
		
		transactions03UI.setCellFactory(item  -> {
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
                    		Account account = Model.getInstance().getAccountByName(item);
                    		if (account.isIs_my_account())
                    			page.jump(new MyAccountDetail(account));
                    		else
                    			page.jump(new TheirAccountDetail(account));
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
		
		transactions04UI.setCellFactory(item  -> {
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
                    		Account account = Model.getInstance().getAccountByName(item);
                    		if (account.isIs_my_account())
                    			page.jump(new MyAccountDetail(account));
                    		else
                    			page.jump(new TheirAccountDetail(account));
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
		
		transactions05UI.setCellFactory(item  -> {
			TableCell<Transaction, String> cell = new TableCell<Transaction, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                	super.updateItem(item, empty);
                	setText(null);
                    if (item != null && !empty) {
                    	CellHyperlink link = new CellHyperlink();
                    	link.setText(item);
                    	link.setOnMouseClicked(event -> { 
                    		page.jump(new MyAmountDetail(list.get(getTableRow().getIndex()).getAmount().getAsset_id()));
                    	});
                    	setGraphic(link);
                    } else {
                    	setGraphic(null);
                    }
                }
            };
            return cell;
		});
		
		transactions08UI.setCellFactory(item -> {
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
                    		page.jump(new BlockDetail(getItem().getBlock_num()));
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
		
//		Util.adjustTableColumn(transactions06UI, Pos.CENTER_RIGHT);
		
//		List<TableColumn<Transaction, ?>> sort = new ArrayList<TableColumn<Transaction, ?>>();
//		sort.add(transactions02UI);
//		transactions02UI.setSortType(TableColumn.SortType.DESCENDING);
//		transactionsUI.getSortOrder().addAll(sort);
	}
}