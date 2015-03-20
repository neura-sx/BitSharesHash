package sx.neura.btsx.gui.view.popups.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.json.api.wallet.WalletAccountCreate;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.btsx.gui.view.components.packer.PackerInput;
import sx.neura.btsx.gui.view.popups.RunProcess;
import sx.neura.btsx.gui.view.popups.bts.Popup_Bts;

public class CreateAccount extends Popup_Bts {
	
	public interface Callback {
		public void onActionComplete();
	}
	
	@FXML
	private LayerPane layerPaneUI;
	
	@FXML
	private Button backUI;
	@FXML
	private Button nextUI;
	
	@FXML
	private Label confirmationHeaderUI;
	@FXML
	private Label confirmationDetailsUI;
	@FXML
	private Label confirmationFooterUI;
	
	@FXML
	private Label introductionUI;
	@FXML
	private PackerInput nameUI;
		
	private String name;

	private Callback callback;
	private Status status;
	
	private enum Status {
		PHASE_1(t("YMNS.L.a78f_4564_9b4d"), 0),
		PHASE_2(t("YMNS.L.9504_4de0_b75d"), 1);
		private Status(String next, int index) {
			this.next = next;
			this.index = index;
		}
		private String next;
		private int index;
	}
	
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	private void setStatus(Status status) {
		this.status = status;
		backUI.setVisible(status.index > 0);
		nextUI.setText(status.next);
		layerPaneUI.setIndex(status.index);
		manageBindings();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		setTitle("Create Account");
		introductionUI.setText("Only lowercase alphanumeric characters, dots and dashes are allowed. The name must start with a letter and cannot end with a dash.");
		setStatus(Status.PHASE_1);
	}
	
	@FXML
	private void onNext(ActionEvent event) {
		if (status.equals(Status.PHASE_1)) {
			if (!validateAccountName(nameUI.getText())) {
				userException("This name is not valid");
				return;
			}
			name = nameUI.getText();
			confirmationHeaderUI.setText(String.format("%s:", "You are about to create the following account"));
			confirmationDetailsUI.setText(name);
			confirmationFooterUI.setText("Please note that account names are not transferable.");
			setStatus(Status.PHASE_2);
		} else if (status.equals(Status.PHASE_2)) {
			final RunProcess runProcess = new RunProcess();
			runProcess.setMessage(String.format("%s..", t("YMNS.O.72c9_4108_b95d")));
			runProcess.setCallback(new RunProcess.Callback() {
				@Override
				public void run() {
					try {
						WalletAccountCreate.run(name);
						if (callback != null)
							callback.onActionComplete();
						runProcess.hide();
						hideItself();
					} catch (BTSSystemException e) {
						runProcess.hide();
						systemException(e);
					}
				}
			});
			runProcess.show(pane);
		}
	}
	
	@FXML
	private void onBack(ActionEvent event) {
		if (status.equals(Status.PHASE_2))
			setStatus(Status.PHASE_1);
	}
	
	private void manageBindings() {
		if (status.equals(Status.PHASE_1)) {
			nextUI.disableProperty().bind(nameUI.getTextProperty().isEmpty());
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();
		}
	}
	

	private boolean validateAccountName(String s) {
		if (s.length() < 8)
			return false;
		return true;
	}
}
