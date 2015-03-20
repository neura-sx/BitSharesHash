package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import sx.neura.bts.json.api.blockchain.BlockchainListBlocks;
import sx.neura.bts.json.dto.Block;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Time;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.components.CellHyperlink;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;

public class BlockList extends Page_Bts {
	
	private static BlockList instance;
	public static BlockList getInstance() {
		if (!isInstance())
			instance = new BlockList();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private BlockList() {
	}
	
	private final double ROW_HEIGHT = 24;
	private final double ROW_OFFSET = 40;
	
	private ObservableList<Block> list;
	
	@FXML
	private Pagination paginationUI;
	
	private int itemsPerPage;
	private TableView<Block> paginationTableView;
	
	private CheckBox checkBoxUI;
	
	@Override
	public String getName() {
		return "Blockchain Explorer";
	}
	
	private TableView<Block> getPaginationTableView(Integer pageIndex) {
		if (paginationTableView == null) {
			paginationTableView = new TableView<Block>();
			paginationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			paginationTableView.setRowFactory(new Callback<TableView<Block>, TableRow<Block>>() {
				@Override
				public TableRow<Block> call(TableView<Block> param) {
					TableRow<Block> row = new TableRow<Block>() {
						@Override
		                protected void updateItem(Block item, boolean empty) {
		                    super.updateItem(item, empty);
		                    if (item != null) {
		                    	getStyleClass().remove("block-active");
		                    	getStyleClass().remove("block-passive");
		                    	getStyleClass().add(item.getUser_transaction_ids().size() > 0 ? "block-active" : "block-passive");
		                    }
		                }
					};
		            return row;
				}
			});
			
			TableColumn<Block, String> column01 = new TableColumn<Block, String>();
			column01.setText("Height");
			column01.setPrefWidth(60.0);
			column01.setMinWidth(50.0);
			column01.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getBlock_num()));
			
			TableColumn<Block, String> column02 = new TableColumn<Block, String>();
			column02.setText("Time");
			column02.setPrefWidth(120.0);
			column02.setMinWidth(100.0);
			column02.setCellValueFactory(item -> new SimpleStringProperty(Time.format(item.getValue().getTimestamp())));
			
			TableColumn<Block, Number> column03 = new TableColumn<Block, Number>();
			column03.setText("Transactions");
			column03.setPrefWidth(40.0);
			column03.setMinWidth(40.0);
			column03.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getUser_transaction_ids().size()));
			
			TableColumn<Block, String> column04 = new TableColumn<Block, String>();
			column04.setText("Delegate");
			column04.setPrefWidth(150.0);
			column04.setMinWidth(130.0);
			column04.setCellValueFactory(item -> new SimpleStringProperty(getBlockSignee(item.getValue().getBlock_num())));
			
			TableColumn<Block, String> column05 = new TableColumn<Block, String>();
			column05.setText("Latency");
			column05.setPrefWidth(40.0);
			column05.setMinWidth(40.0);
			column05.setCellValueFactory(item -> new SimpleStringProperty(String.format("%d%s", item.getValue().getLatency() / 1000000, "s")));
			
			TableColumn<Block, Number> column06 = new TableColumn<Block, Number>();
			column06.setText("Size");
			column06.setPrefWidth(40.0);
			column06.setMinWidth(40.0);
			column06.setCellValueFactory(item -> new SimpleIntegerProperty(item.getValue().getBlock_size()));
			
			column01.setCellFactory(item -> {
				TableCell<Block, String> cell = new TableCell<Block, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                	super.updateItem(item, empty);
	                	setText(null);
	                    if (item != null && !empty) {
	                    	CellHyperlink link = new CellHyperlink();
	                    	link.setText(item);
	                    	link.setOnMouseClicked(event -> {
	                    		jump(new BlockDetail(paginationTableView.getItems().get(getTableRow().getIndex())));
	                    	});
	                    	setGraphic(link);
	                    } else {
	                    	setGraphic(null);
	                    }
	                }
	            };
	            return cell;
			});
			
			column04.setCellFactory(item -> {
				TableCell<Block, String> cell = new TableCell<Block, String>() {
	                @Override
	                public void updateItem(String item, boolean empty) {
	                	super.updateItem(item, empty);
	                	setText(null);
	                    if (item != null && !empty) {
	                    	CellHyperlink link = new CellHyperlink();
	                    	link.setText(item);
	                    	link.setOnMouseClicked(event -> {
	                    		jump(new TheirAccountDetail(getAccount(getBlockSignee(paginationTableView.getItems().get(getTableRow().getIndex()).getBlock_num()))));
	                    	});
	                    	setGraphic(link);
	                    } else {
	                    	setGraphic(null);
	                    }
	                }
	            };
	            return cell;
			});
			
			adjustTableColumn(column03, Pos.CENTER_RIGHT);
			adjustTableColumn(column05, Pos.CENTER_RIGHT);
			adjustTableColumn(column06, Pos.CENTER_RIGHT);
			
			paginationTableView.getColumns().add(column01);
			paginationTableView.getColumns().add(column02);
			paginationTableView.getColumns().add(column03);
			paginationTableView.getColumns().add(column04);
			paginationTableView.getColumns().add(column05);
			paginationTableView.getColumns().add(column06);
			
			paginationTableView.getStyleClass().add("narrow");
			paginationTableView.getStyleClass().add("unselectable");
		}
		ObservableList<Block> sublist = FXCollections.observableArrayList();
		int fromIndex = pageIndex * itemsPerPage;
		int toIndex = Math.min((pageIndex + 1) * itemsPerPage, list.size());
		sublist.addAll(list.subList(fromIndex, toIndex));
		paginationTableView.setItems(sublist);
		paginationTableView.getSelectionModel().select(0);
		return paginationTableView;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		checkBoxUI = new CheckBox();
		checkBoxUI.setFocusTraversable(false);
		checkBoxUI.setText("Show only blocks with transactions");
		checkBoxUI.setSelected(true);
		checkBoxUI.setOnAction((ActionEvent event) -> {
			reloadData();
		});
		AnchorPane.setBottomAnchor(checkBoxUI, 6.0);
		AnchorPane.setLeftAnchor(checkBoxUI, 75.0);
		
		itemsPerPage = 0;
		paginationUI.setPageFactory((Integer pageIndex) -> {
			if (itemsPerPage <= 0)
				return null;
			return getPaginationTableView(pageIndex);
		});
		paginationUI.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				itemsPerPage = getItemsPerPage();
				paginationUI.setPageCount(getPageCount());
			}
		});
		
		list = FXCollections.observableArrayList();
	}
	
	private int getItemsPerPage() {
		return (int) ((paginationUI.getHeight() - ROW_OFFSET) / ROW_HEIGHT);
	}
	
	private int getPageCount() {
		return (list.size() / itemsPerPage) + (list.size() % itemsPerPage == 0 ? 0 : 1);
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			try {
				List<Block> blocks = BlockchainListBlocks.run(0, -Model.BLOCKS_LIMIT);
				if (!checkBoxUI.isSelected())
					list.setAll(blocks);
				else {
					list.clear();
					for (Block block : blocks)
						if (block.getUser_transaction_ids().size() > 0)
							list.add(block);
				}
			} catch (BTSSystemException e) {
				systemException(e);
			}
			itemsPerPage = getItemsPerPage();
			paginationUI.setPageCount(getPageCount());
			paginationUI.setCurrentPageIndex(0);
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		isDataLoaded = false;
		loadData();
	}
	
	@Override
	public void loadUI() {
		super.loadUI();
		pageManager.getFooter().getChildren().add(checkBoxUI);
	}
	
	@Override
	public void unloadUI() {
		super.unloadUI();
		pageManager.getFooter().getChildren().remove(checkBoxUI);
	}
}
