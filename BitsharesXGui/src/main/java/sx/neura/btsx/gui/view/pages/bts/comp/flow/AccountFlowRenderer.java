package sx.neura.btsx.gui.view.pages.bts.comp.flow;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.json.dto.Account;
import sx.neura.btsx.gui.view.components.flow.FlowRenderer;
import sx.neura.btsx.gui.view.pages.bts.impl.TheirAccountDetail;

public class AccountFlowRenderer extends FlowRenderer<Account> {
	
	@FXML
	private ImageView avatarUI;
	@FXML
	private Label nameUI;
	
	private static final double OPACITY_OFF = 0.5;
	private static final double OPACITY_ON = 1.0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		nameUI.setCursor(Cursor.HAND);
		nameUI.setOnMouseClicked(event -> { 
			jump(new TheirAccountDetail(item));
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
		avatarUI.setImage(getAvatarImage(item.getName()));
		nameUI.setText(item.getName());
	}
}
