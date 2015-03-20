package sx.neura.btsx.gui.view.pages.bts.comp.flow;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sx.neura.bts.gui.dto.BurnRecord;
import sx.neura.btsx.gui.view.components.flow.FlowRenderer;

public class BurnRecordFlowRenderer extends FlowRenderer<BurnRecord> {
	
	@FXML
	private Label messageUI;
//	@FXML
//	private Label nameUI;
	@FXML
	private Label symbolUI;
	@FXML
	private Label amountUI;
	
//	private static final double OPACITY_OFF = 0.5;
//	private static final double OPACITY_ON = 1.0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
//		nameUI.setCursor(Cursor.HAND);
//		nameUI.setOnMouseClicked(event -> {
//			if (item.getAccount().isIs_my_account())
//				jump(new MyAccountDetail(item.getAccount()));
//			else
//				jump(new TheirAccountDetail(item.getAccount()));
//			event.consume();
//    	});
//		avatarUI.setCursor(Cursor.HAND);
//		avatarUI.setOpacity(OPACITY_OFF);
//		backgroundUI.setOnMouseEntered((MouseEvent event) -> {
//			avatarUI.setOpacity(OPACITY_ON);
//		});
//		backgroundUI.setOnMouseExited((MouseEvent event) -> {
//			avatarUI.setOpacity(OPACITY_OFF);
//		});
	}
	
	@Override
	protected void applyItem() {
//		avatarUI.setImage(getAvatarImage(item.getAccount().getName()));
		messageUI.setText(item.getMessage());
//		nameUI.setText(item.getAccount() != null ? item.getAccount().getName() : "n/a");
		String[] s = getAmount(item.getAmount());
		symbolUI.setText(s[0]);
		amountUI.setText(s[1]);
	}
}
