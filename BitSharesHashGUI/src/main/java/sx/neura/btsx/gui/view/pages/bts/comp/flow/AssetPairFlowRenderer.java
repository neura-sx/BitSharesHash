package sx.neura.btsx.gui.view.pages.bts.comp.flow;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.util.Util;
import sx.neura.btsx.gui.dto.AssetPair;
import sx.neura.btsx.gui.view.components.flow.FlowRenderer;
import sx.neura.btsx.gui.view.pages.bts.impl.MyAmountDetail;

public class AssetPairFlowRenderer extends FlowRenderer<AssetPair> {
	
	@FXML
	private Label avatarUI;
	@FXML
	private Label symbol1UI;
	@FXML
	private Label symbol2UI;
	
	private static final double OPACITY_OFF = 0.2;
	private static final double OPACITY_ON = 0.5;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		symbol1UI.setCursor(Cursor.HAND);
		symbol1UI.setOnMouseClicked(event -> {
			jump(new MyAmountDetail(item.getAsset1()));
			event.consume();
    	});
		symbol2UI.setCursor(Cursor.HAND);
		symbol2UI.setOnMouseClicked(event -> {
			jump(new MyAmountDetail(item.getAsset2()));
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
		avatarUI.setText(String.format("%s:%s",
				Util.getSubstring(item.getAsset1().getSymbol(), 6),
				Util.getSubstring(item.getAsset2().getSymbol(), 6)));
		symbol1UI.setText(item.getAsset1().getSymbol());
		symbol2UI.setText(item.getAsset2().getSymbol());
	}
}
