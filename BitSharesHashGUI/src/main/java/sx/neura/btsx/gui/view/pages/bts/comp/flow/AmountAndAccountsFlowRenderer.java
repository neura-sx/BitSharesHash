package sx.neura.btsx.gui.view.pages.bts.comp.flow;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sx.neura.bts.gui.dto.AccountAndAmount;
import sx.neura.bts.gui.dto.AmountAndAccounts;
import sx.neura.btsx.gui.view.components.flow.FlowRenderer;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAccountDetail;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAmountDetail;

public class AmountAndAccountsFlowRenderer extends FlowRenderer<AmountAndAccounts> {
	
	@FXML
	private Label avatarUI;
	@FXML
	private Label symbolUI;
	@FXML
	private Label amountUI;
	@FXML
	private VBox listUI;
	
	private static final double OPACITY_OFF = 0.2;
	private static final double OPACITY_ON = 0.5;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		avatarUI.setCursor(Cursor.HAND);
		symbolUI.setCursor(Cursor.HAND);
		symbolUI.setOnMouseClicked(event -> { 
			jump(new MyAmountDetail(item));
			event.consume();
    	});
		avatarUI.setCursor(Cursor.HAND);
		avatarUI.setOpacity(OPACITY_OFF);
		backgroundUI.setOnMouseEntered((MouseEvent event) -> {
			avatarUI.setOpacity(OPACITY_ON);
		});
		backgroundUI.setOnMouseExited((MouseEvent event) -> {
			avatarUI.setOpacity(OPACITY_OFF);
		});
	}
	
	@Override
	protected void applyItem() {
		avatarUI.setText(item.getAsset().getSymbol());
		symbolUI.setText(item.getAsset().getSymbol());
		amountUI.setText(getAmount(item.getAsset(), item.getAmount()));
		listUI.getChildren().clear();
		for (AccountAndAmount i : item.getSplit()) {
			HBox box = new HBox();
        	box.setAlignment(Pos.TOP_RIGHT);
        	box.setSpacing(4.0);
    		Label accountUI = new Label();
    		accountUI.setCursor(Cursor.HAND);
    		accountUI.setUnderline(true);
    		accountUI.setText(i.getAccount().getName());
    		accountUI.setOnMouseClicked(event -> { 
        		jump(new MyAccountDetail(i.getAccount()));
        		event.consume();
        	});
    		Label amountUI = new Label(getAmount(item.getAsset(), i.getAmount().getValue()));
    		box.getChildren().add(accountUI);
    		box.getChildren().add(amountUI);
    		listUI.getChildren().add(box);
    	}
	}
}
