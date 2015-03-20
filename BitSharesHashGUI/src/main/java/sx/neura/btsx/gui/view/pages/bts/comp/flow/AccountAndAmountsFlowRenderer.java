package sx.neura.btsx.gui.view.pages.bts.comp.flow;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sx.neura.bts.gui.dto.AccountAndAmounts;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.util.Time;
import sx.neura.btsx.gui.view.components.flow.FlowRenderer;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAccountDetail;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAmountDetail;

public class AccountAndAmountsFlowRenderer extends FlowRenderer<AccountAndAmounts> {
	
	@FXML
	private ImageView avatarUI;
	@FXML
	private Label nameUI;
	@FXML
	private Label registrationDateUI;
	@FXML
	private VBox listUI;
	
	private static final double OPACITY_OFF = 0.5;
	private static final double OPACITY_ON = 1.0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		nameUI.setCursor(Cursor.HAND);
		nameUI.setOnMouseClicked(event -> { 
			jump(new MyAccountDetail(item.getAccount()));
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
		avatarUI.setImage(getAvatarImage(item.getAccount().getName()));
		nameUI.setText(item.getAccount().getName());
		registrationDateUI.setText(Time.isUndefined(item.getAccount().getRegistration_date()) ? "unregistered" : "");
		listUI.getChildren().clear();
		for (Amount amount : item.getAmounts()) {
    		String[] s = getAmount(amount);
    		HBox box = new HBox();
        	box.setAlignment(Pos.TOP_RIGHT);
        	box.setSpacing(4.0);
    		Label assetUI = new Label();
    		assetUI.setCursor(Cursor.HAND);
			assetUI.setUnderline(true);
        	assetUI.setText(s[0]);
        	assetUI.setOnMouseClicked(event -> { 
        		jump(new MyAmountDetail(amount.getAsset_id()));
        		event.consume();
        	});
        	Label amountUI = new Label(s[1]);
    		box.getChildren().add(assetUI);
    		box.getChildren().add(amountUI);
    		listUI.getChildren().add(box);
    	}
	}
}
