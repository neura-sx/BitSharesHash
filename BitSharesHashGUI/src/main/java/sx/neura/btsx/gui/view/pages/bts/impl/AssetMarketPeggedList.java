package sx.neura.btsx.gui.view.pages.bts.impl;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sx.neura.bts.json.dto.Asset;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.view.components.CellHyperlink;

public class AssetMarketPeggedList extends AssetList {
	private static AssetMarketPeggedList instance;
	public static AssetMarketPeggedList getInstance() {
		if (!isInstance())
			instance = new AssetMarketPeggedList();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private AssetMarketPeggedList() {
	}
	
	@Override
	public String getName() {
		return "Market-Pegged Assets";
	}
	
	@Override
	protected List<Asset> getAssets() {
		List<Asset> list = new ArrayList<Asset>();
		for (Asset asset : Model.getInstance().getAssets())
			if (asset.getIssuer_account_id() <= 0)
				list.add(asset);
		return list;
	}
	
	@Override
	protected TableView<Asset> createPaginationTableView() {
		TableView<Asset> paginationTableView = new TableView<Asset>();
		paginationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		TableColumn<Asset, Asset> column01 = new TableColumn<Asset, Asset>();
		column01.setText("Symbol");
		column01.setPrefWidth(60.0);
		column01.setMinWidth(60.0);
		column01.setCellValueFactory(item -> new SimpleObjectProperty<Asset>(item.getValue()));
		
		TableColumn<Asset, Asset> column02 = new TableColumn<Asset, Asset>();
		column02.setText("Name");
		column02.setPrefWidth(120.0);
		column02.setMinWidth(100.0);
		column02.setCellValueFactory(item -> new SimpleObjectProperty<Asset>(item.getValue()));
		
		TableColumn<Asset, String> column03 = new TableColumn<Asset, String>();
		column03.setText("Description");
		column03.setPrefWidth(200.0);
		column03.setMinWidth(100.0);
		column03.setCellValueFactory(item -> new SimpleStringProperty(item.getValue().getDescription()));
		
		TableColumn<Asset, String> column04 = new TableColumn<Asset, String>();
		column04.setText("Collected Fees");
		column04.setPrefWidth(100.0);
		column04.setMinWidth(80.0);
		column04.setCellValueFactory(item -> new SimpleStringProperty(getAmount(item.getValue().getId(), item.getValue().getCollected_fees())));
		
		TableColumn<Asset, String> column05A = new TableColumn<Asset, String>();
		column05A.setText("Current");
		column05A.setPrefWidth(140.0);
		column05A.setMinWidth(120.0);
		column05A.setCellValueFactory(item -> new SimpleStringProperty(getAmount(item.getValue().getId(), item.getValue().getCurrent_share_supply(), 1)));
		
		TableColumn<Asset, String> column05B = new TableColumn<Asset, String>();
		column05B.setText("Maximum");
		column05B.setPrefWidth(140.0);
		column05B.setMinWidth(120.0);
		column05B.setCellValueFactory(item -> new SimpleStringProperty(getAmount(item.getValue().getId(), item.getValue().getMaximum_share_supply(), 1)));
		
		TableColumn<Asset, Number> column05 = new TableColumn<Asset, Number>();
		column05.setText("Share Supply");
		column05.getColumns().add(column05A);
		column05.getColumns().add(column05B);
		
		column01.setCellFactory(item -> {
			TableCell<Asset, Asset> cell = new TableCell<Asset, Asset>() {
                @Override
                public void updateItem(Asset item, boolean empty) {
                	super.updateItem(item, empty);
                	setText(null);
                    if (item != null && !empty) {
                    	CellHyperlink link = new CellHyperlink();
                    	link.setText(item.getSymbol());
                    	link.setOnMouseClicked(event -> {
                    		jump(new MyAmountDetail(getItem()));
                    	});
                    	setGraphic(link);
                    } else {
                    	setGraphic(null);
                    }
                }
            };
            return cell;
		});
		
		column02.setCellFactory(item -> {
			TableCell<Asset, Asset> cell = new TableCell<Asset, Asset>() {
                @Override
                public void updateItem(Asset item, boolean empty) {
                	super.updateItem(item, empty);
                	setText(null);
                    if (item != null && !empty) {
                    	CellHyperlink link = new CellHyperlink();
                    	link.setText(item.getName());
                    	link.setOnMouseClicked(event -> {
                    		jump(new MyAmountDetail(getItem()));
                    	});
                    	setGraphic(link);
                    } else {
                    	setGraphic(null);
                    }
                }
            };
            return cell;
		});
		
		adjustTableColumn(column04, Pos.CENTER_RIGHT);
		adjustTableColumn(column05A, Pos.CENTER_RIGHT);
		adjustTableColumn(column05B, Pos.CENTER_RIGHT);
		
		paginationTableView.getColumns().add(column01);
		paginationTableView.getColumns().add(column02);
		paginationTableView.getColumns().add(column03);
		paginationTableView.getColumns().add(column04);
		paginationTableView.getColumns().add(column05);
		
		paginationTableView.getStyleClass().add("narrow");
		paginationTableView.getStyleClass().add("unselectable");
		return paginationTableView;
	}
}
