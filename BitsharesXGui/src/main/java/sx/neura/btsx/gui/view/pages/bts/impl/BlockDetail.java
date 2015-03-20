package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import sx.neura.bts.json.api.blockchain.BlockchainGetBlockTransactions;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Block;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Time;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;

public class BlockDetail extends Page_Bts {
	
	private Block item;
	private ObservableList<BlockchainGetBlockTransactions.Result> transactions;
	
	@FXML
	private PackerText blockNumberUI;
	@FXML
	private PackerText timestampUI;
	@FXML
	private PackerText delegateUI;
	
	@FXML
	private PackerText latencyUI;
	@FXML
	private PackerText sizeUI;
	
	@FXML
	private TableView<BlockchainGetBlockTransactions.Result> transactionsUI;
	
	@FXML
	private TableColumn<BlockchainGetBlockTransactions.Result, String> transactions02UI;
	@FXML
	private TableColumn<BlockchainGetBlockTransactions.Result, String> transactions03UI;
	@FXML
	private TableColumn<BlockchainGetBlockTransactions.Result, BlockchainGetBlockTransactions.Result> transactions04UI;
	@FXML
	private TableColumn<BlockchainGetBlockTransactions.Result, String> transactions05UI;
	@FXML
	private TableColumn<BlockchainGetBlockTransactions.Result, String> transactions06UI;
	
	
	public BlockDetail(Block item) {
		this.item = item;
	}
	public BlockDetail(String blockNumber) {
		this.item = getBlock(blockNumber);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		delegateUI.setCallback(new PackerText.Callback() {
			@Override
			public void onAction() {
				jump(new TheirAccountDetail(getAccount(getBlockSignee(item.getBlock_num()))));
			}
		});
		
		transactions = FXCollections.observableArrayList();
		
		transactionsUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		transactionsUI.setPlaceholder(new Label("There are no transactions"));
		transactionsUI.setItems(transactions);
		
		transactions02UI.setCellValueFactory(item -> new SimpleStringProperty(parseAmount(item.getValue().getTransaction().getTrx().getWithdraws())));
		transactions03UI.setCellValueFactory(item -> new SimpleStringProperty(parseAmount(item.getValue().getTransaction().getTrx().getDeposits())));
		transactions04UI.setCellValueFactory(item -> new SimpleObjectProperty<BlockchainGetBlockTransactions.Result>(item.getValue()));
		transactions05UI.setCellValueFactory(item -> new SimpleStringProperty(parseOperations(item.getValue().getTransaction().getTrx().getOperations())));
		transactions06UI.setCellValueFactory(item -> new SimpleStringProperty(parseAmount(item.getValue().getTransaction().getTrx().getBalance())));
		
		transactions04UI.setCellFactory(item -> {
			TableCell<BlockchainGetBlockTransactions.Result, BlockchainGetBlockTransactions.Result> cell = new TableCell<BlockchainGetBlockTransactions.Result, BlockchainGetBlockTransactions.Result>() {
                @Override
                public void updateItem(BlockchainGetBlockTransactions.Result item, boolean empty) {
                	super.updateItem(item, empty);
                    if (item != null && !empty && !item.getTransaction().getTrx().getNet_delegate_votes().isEmpty()) {
                    	VBox box = new VBox();
//                    	for (NetDelegateVote vote : item.getTransaction().getTrx().getNet_delegate_votes()) {
//	                    	CellHyperlink link = new CellHyperlink();
//	                    	link.setText(getAccount(vote.getId()).getName());
//	                    	link.setOnMouseClicked(event -> {
//	                    		jump(new TheirAccountDetail(getAccount(vote.getId())));
//	                    	});
//	                    	Label label = new Label();
//	                    	label.setText(parseAmount(getDelegateAmount(vote.getVotes_for())));
//	                    	label.getStyleClass().add("tableCellRenderer");
//	                    	HBox hbox = new HBox(5.0, link, label);
//	                    	hbox.setAlignment(Pos.TOP_RIGHT);
//	                    	box.getChildren().add(hbox);
//                    	}
                    	setGraphic(box);
                    } else {
                    	setText(null);
                    	setGraphic(null);
                    }
                }
            };
            cell.setAlignment(Pos.TOP_RIGHT);
            return cell;
		});
		
		adjustTableColumn(transactions02UI, Pos.TOP_RIGHT);
		adjustTableColumn(transactions03UI, Pos.TOP_RIGHT);
		adjustTableColumn(transactions06UI, Pos.TOP_RIGHT);
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			blockNumberUI.setText(item.getBlock_num());
			timestampUI.setText(Time.format(item.getTimestamp()));
			delegateUI.setText(getBlockSignee(item.getBlock_num()));
			latencyUI.setText(String.format("%d%s", item.getLatency() / 1000000, "s"));
			sizeUI.setText(String.format("%d", item.getBlock_size()));
			try {
				transactions.setAll(BlockchainGetBlockTransactions.run(new Integer(item.getBlock_num()).toString()));
			} catch (BTSSystemException e) {
				systemException(e);
			}
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		isDataLoaded = false;
		loadData();
	}
	
	private static String parseOperations(List<BlockchainGetBlockTransactions.Operation> operations) {
		String s = "";
		List<String> types = new ArrayList<String>();
		for (BlockchainGetBlockTransactions.Operation operation : operations)
			if (!types.contains(operation.getType()))
				types.add(operation.getType());
		for (String type : types)
			s +=  (s.isEmpty() ? "" : "\n") + parseOperation(type);
		return s;
	}
	
	private static String parseOperation(String type) {
		if (type.equals("update_feed_op_type"))
			return "update feed";
		if (type.equals("withdraw_pay_op_type"))
			return "withdraw pay";
		if (type.equals("deposit_op_type"))
			return "deposit";
		if (type.equals("withdraw_op_type"))
			return "withdraw";
		if (type.equals("ask_op_type"))
			return "ask";
		if (type.equals("bid_op_type"))
			return "bid";
		if (type.equals("short_op_v2_type"))
			return "short v2";
		if (type.equals("define_delegate_slate_op_type"))
			return "delegate slate";
		return String.format("<%s>", type);
	}
	
	private static String parseAmount(List<Amount> list) {
		if (list.isEmpty())
			return "none";
		return parseAmount(list.get(0));
	}
	
	private static String parseAmount(Amount amount) {
		String[] pair = getAmountPair(amount);
		return String.format("%s %s", pair[0], pair[1]);
	}
	
//	private static Amount getDelegateAmount(long votes) {
//		Amount amount = new Amount();
//		amount.setAsset_id(0);
//		amount.setValue(votes);
//		return amount;
//	}
}
