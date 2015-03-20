package sx.neura.bts.gui.view.popups.impl.bts;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.Popup;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.display.DisplayToggleList;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0102_Counterparty;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0202_Delegates;
import sx.neura.bts.json.api.wallet.WalletAccountSetApproval;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Util;

public class ToggleApproval extends Popup {
	
	@FXML
	private LayerPane spaceUI;
	
	@FXML
	private Node progressTrackUI;
	@FXML
	private Node progressBarUI;
	
	@FXML
	private Label textUI;
	@FXML
	private DisplayToggleList<String> approvalUI;
	
	private String name;
	private Callback callback;
	private IntegerProperty approvalProperty;

	public void setName(String name) {
		this.name = name;
	}
	public void setApprovalProperty(IntegerProperty approvalProperty) {
		this.approvalProperty = approvalProperty;
	}
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		textUI.setText(String.format("Select your approval choice for '%s'", Util.crop(name, 30)));
		approvalUI.setItems(Arrays.asList(new String[] {"Negative", "Neutral", "Positive"}));
		approvalUI.selectToggle(approvalProperty.get() + 1);
	}
	
	@FXML
	private void onConfirm() {
		spaceUI.setIndex(1, () -> {
			animateProgress(1.0, 500, progressTrackUI, () -> {
				animateProgress(0.2, progressBarUI, () -> {
					int approval = approvalUI.getSelectedIndex() - 1;
					try {
						WalletAccountSetApproval.run(name, approval);
						Model.getInstance().redoAccounts();
					} catch (BTSSystemException e) {
						spaceUI.setIndex(0);
						systemException(e);
						return;
					}
					approvalProperty.set(approval);
					animateProgress(0.4, progressBarUI, () -> {
						Page_0102_Counterparty.getInstance().reloadData();
						animateProgress(0.6, progressBarUI, () -> {
							Page_0202_Delegates.getInstance().reloadData();
							animateProgress(0.8, progressBarUI, () -> {
								if (callback != null)
									callback.onConfirm();
								animateProgress(1.0, progressBarUI, () -> {
									hideItself();
								});
							});
						});
					});
				});
			});
		});
	}
	
	@Override
	protected void onCancel() {
		hideItself(() -> callback.onCancel());
	}
	
}
