package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.AccountAndAmounts;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Asset;
import sx.neura.bts.gui.view.pages.bts.impl.Wizard_MakeTransfer;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.util.Util;

public class AccountAndAmountsTile extends Tile_Bts<AccountAndAmounts> {
	
	private static final double WIDTH_OFFSET = 140.0;
	private static final double PREF_COLUMN_WIDTH = 125.0;
	
	@FXML
	private IdenticonCanvas avatarUI;
	@FXML
	private Label nameUI;
	
	@FXML
	private GridPane gridUI;
	
	@FXML
	private Button scrollBckUI;
	@FXML
	private Button scrollFwdUI;
	
	private int numberOfColumns;
	private int scrollOffset;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			avatarUI.setName(item.getAccount().getName());
			nameUI.setText(Util.crop(item.getAccount().getName(), 20));
			boxUI.setOnMouseClicked((MouseEvent event) -> { 
				module.jump(new Details_Account(item.getAccount()));
				event.consume();
	    	});
			scrollBckUI.setOnAction((ActionEvent event) -> {
				scrollOffset--;
				updateScrollAvailability();
				populateGrid();
	        });
			scrollFwdUI.setOnAction((ActionEvent event) -> {
				scrollOffset++;
				updateScrollAvailability();
				populateGrid();
	        });
			numberOfColumns = (int) ((region.getWidth() - WIDTH_OFFSET) / PREF_COLUMN_WIDTH);
			scrollOffset = 0;
			updateScrollAvailability();
			populateGrid();
		}
	}
	
	private void updateScrollAvailability() {
		scrollBckUI.setVisible(scrollOffset > 0);
		scrollFwdUI.setVisible(numberOfColumns + scrollOffset < item.getAmounts().size());
	}
	
	private void populateGrid() {
		gridUI.getChildren().clear();
		gridUI.getColumnConstraints().clear();
		for (int i = 0; i < numberOfColumns; i++) {
			ColumnConstraints c = new ColumnConstraints();
			c.setPercentWidth(1.0 / numberOfColumns * 100);
			c.setHgrow(Priority.ALWAYS);
			c.setMinWidth(10);
			gridUI.getColumnConstraints().add(c);
		}
		final int max = this.item.getAmounts().size() - scrollOffset;
		for (int i = 0; i < numberOfColumns; i++) {
			AnchorPane box = new AnchorPane();
			box.setMaxWidth(120.0);
			box.getStyleClass().add("sx-list-grid-tile");
			if (i < max) {
				box.getStyleClass().add("sx-cell");
				box.getStyleClass().add("sx-cell-list-grid");
				Amount item = this.item.getAmounts().get(i + scrollOffset);
				Asset asset = Model.getInstance().getAssetById(item.getAsset_id());
				Label icon = new Label();
				icon.getStyleClass().add("sx-text-icon");
				icon.getStyleClass().add("sx-text-icon-asset");
				AnchorPane.setLeftAnchor(icon, 3.0);
				AnchorPane.setBottomAnchor(icon, 30.0);
				icon.setText(asset.getSymbol());
				icon.setOnMouseClicked((MouseEvent event) -> { 
					module.jump(new Details_Asset(asset));
					event.consume();
		    	});
				box.getChildren().add(icon);
				Label name = new Label();
				name.getStyleClass().add("sx-text-small");
				AnchorPane.setLeftAnchor(name, 5.0);
				AnchorPane.setBottomAnchor(name, 77.0);
				name.setText(Util.crop(asset.getName(), 24));
				box.getChildren().add(name);
				Label amount = new Label();
				amount.getStyleClass().add("sx-text-express");
				AnchorPane.setLeftAnchor(amount, 5.0);
				AnchorPane.setBottomAnchor(amount, 5.0);
				amount.setText(Model.getInstance().getAmount(asset, item.getValue()));
				amount.setOnMouseClicked((MouseEvent event) -> { 
					Wizard_MakeTransfer wizard = new Wizard_MakeTransfer();
					wizard.setFromAccount(this.item.getAccount());
					wizard.setAsset(asset);
					module.jump(wizard);
					event.consume();
		    	});
				box.getChildren().add(amount);
			}
			GridPane.setColumnIndex(box, i);
			GridPane.setHalignment(box, HPos.CENTER);
			gridUI.getChildren().add(box);
		}
	}

}
