package sx.neura.btsx.gui.view.pages.bts.comp.flow;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.btsx.gui.view.components.flow.FlowRenderer;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAmountDetail;

public class AmountFlowRenderer extends FlowRenderer<Amount> {
	
	@FXML
	private Label avatarUI;
	@FXML
	private Label symbolUI;
	@FXML
	private Label amountUI;
	
	private Asset asset;
	
	private static final double OPACITY_OFF = 0.2;
	private static final double OPACITY_ON = 0.5;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		symbolUI.setCursor(Cursor.HAND);
		symbolUI.setOnMouseClicked(event -> { 
			jump(new MyAmountDetail(asset));
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
		asset = getAssetById(item.getAsset_id());
		avatarUI.setText(asset.getSymbol());
		String[] s = getAmount(item);
		symbolUI.setText(s[0]);
		amountUI.setText(s[1]);
	}
}
