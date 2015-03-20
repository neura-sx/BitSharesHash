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
import sx.neura.bts.gui.dto.AccountAndAmount;
import sx.neura.bts.gui.dto.AmountAndAccounts;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.components.Tile;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Asset;
import sx.neura.bts.gui.view.pages.bts.impl.Wizard_MakeTransfer;
import sx.neura.bts.util.Util;

public class AmountAndAccountsTile extends Tile<AmountAndAccounts> {
	
	private static final double WIDTH_OFFSET = 180.0;
	private static final double PREF_COLUMN_WIDTH = 105.0;
	
	@FXML
	private Label nameUI;
	@FXML
	private Label iconUI;
	@FXML
	private Label amountUI;
	
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
			nameUI.setText(Util.crop(item.getAsset().getName(), 20));
			iconUI.setText(item.getAsset().getSymbol());
			amountUI.setText(getAmount(item.getAsset(), item.getAmount()));
			iconUI.setOnMouseClicked((MouseEvent event) -> { 
				module.jump(new Details_Asset(item.getAsset()));
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
		scrollFwdUI.setVisible(numberOfColumns + scrollOffset < item.getSplit().size());
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
		final int max = this.item.getSplit().size() - scrollOffset;
		for (int i = 0; i < numberOfColumns; i++) {
			AnchorPane box = new AnchorPane();
			box.setMaxWidth(100.0);
			box.getStyleClass().add("sx-list-grid-tile");
			if (i < max) {
				box.getStyleClass().add("sx-cell");
				box.getStyleClass().add("sx-cell-list-grid");
				AccountAndAmount item = this.item.getSplit().get(i + scrollOffset);
				IdenticonCanvas avatar = new IdenticonCanvas();
				avatar.setWidth(56.0);
				avatar.setHeight(56.0);
				avatar.setName(item.getAccount().getName());
//				ImageView avatar = new ImageView();
//				avatar.setPreserveRatio(true);
//				avatar.setSmooth(true);
//				avatar.setFitWidth(75.0);
//				avatar.setFitHeight(75.0);
//				avatar.setImage(Model.getInstance().getAvatarImage(item.getAccount().getName()));
				AnchorPane.setLeftAnchor(avatar, 5.0);
				AnchorPane.setBottomAnchor(avatar, 25.0);
				box.getChildren().add(avatar);
				Label name = new Label();
				name.getStyleClass().add("sx-text-small");
				AnchorPane.setLeftAnchor(name, 5.0);
				AnchorPane.setBottomAnchor(name, 85.0);
				name.setText(Util.crop(item.getAccount().getName(), 24));
				box.getChildren().add(name);
				Label amount = new Label();
				amount.getStyleClass().add("sx-text-small");
				AnchorPane.setLeftAnchor(amount, 5.0);
				AnchorPane.setBottomAnchor(amount, 5.0);
				amount.setText(getAmount(item.getAmount()));
				amount.setOnMouseClicked((MouseEvent event) -> { 
					Wizard_MakeTransfer wizard = new Wizard_MakeTransfer();
					wizard.setFromAccount(item.getAccount());
					wizard.setAsset(this.item.getAsset());
					module.jump(wizard);
					event.consume();
		    	});
				box.getChildren().add(amount);
				box.setOnMouseClicked((MouseEvent event) -> { 
					module.jump(new Details_Account(item.getAccount()));
					event.consume();
		    	});
			}
			GridPane.setColumnIndex(box, i);
			GridPane.setHalignment(box, HPos.CENTER);
			gridUI.getChildren().add(box);
		}
	}

}
