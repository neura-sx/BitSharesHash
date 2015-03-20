package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.display.DisplayInput;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.json.api.wallet.WalletAccountCreate;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class Wizard_CreateAccount extends Page_Bts {
	
	@FXML
	private LayerPane spaceUI;
	
	@FXML
	private Node progressTrackUI;
	@FXML
	private Node progressBarUI;
	
	@FXML
	private ToggleButton step01UI;
	@FXML
	private ToggleButton step02UI;
	
	@FXML
	private LayerPane panoramaUI;
	
	@FXML
	private Button backUI;
	@FXML
	private Button nextUI;
	
	@FXML
	private Label introUI;
	@FXML
	private IdenticonCanvas avatarUI;
	@FXML
	private DisplayInput nameUI;
	
	@FXML
	private IdenticonCanvas avatarConfirmationUI;
	@FXML
	private Label nameConfirmationUI;
	
	@FXML
	private Label confirmationIntroUI;
//	@FXML
//	private Label confirmationHeadersUI;
//	@FXML
//	private Label confirmationValuesUI;
	
	private String name;
	
	private Status status;
	
	private enum Status {
		PHASE_1("Next", 0),
		PHASE_2("Finish", 1);
		private Status(String next, int index) {
			this.next = next;
			this.index = index;
		}
		private String next;
		private int index;
	}
	
	private void setStatus(Status status) {
		this.status = status;
		nextUI.setText(status.next);
		panoramaUI.setIndex(status.index);
		manageBindings();
	}
	
	@Override
	public String getTitle() {
		return "Create Account";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		step01UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(0));
		step02UI.selectedProperty().bind(panoramaUI.getIndexProperty().greaterThanOrEqualTo(1));
		
		backUI.visibleProperty().bind(panoramaUI.getIndexProperty().greaterThan(0).and(isOverlay.not()));
		nextUI.visibleProperty().bind(isOverlay.not());
		
		introUI.setText("Please note that account names are not transferable.\nOnly lowercase alphanumeric characters, dots and dashes are allowed.\nThe name must start with a letter and cannot end with a dash.");
		
		nameUI.setResponder(() -> {
			avatarUI.setName(nameUI.getText());
		});
		
    	setStatus(Status.PHASE_1);
	}
	
	@FXML
	private void onNext() {
		if (status.equals(Status.PHASE_1)) {
			if (nameUI.getText().isEmpty()) {
				userException("Name is not set");
				return;
			}
			name = nameUI.getText();
	    	confirmationIntroUI.setText(String.format("%s:", "You are about to create the following account"));
//	    	String h = "";
//			h += String.format("%s\n", "Name");
//			confirmationHeadersUI.setText(h);
//			String v = "";
//			v += String.format("%s\n", name);
//			confirmationValuesUI.setText(v);
			avatarConfirmationUI.setName(name);
			nameConfirmationUI.setText(name);
			setStatus(Status.PHASE_2);
		} else if (status.equals(Status.PHASE_2)) {
			module.isProcessing().set(true);
			spaceUI.setIndex(1, () -> {
				animateProgress(1.0, 500, progressTrackUI, () -> {
					animateProgress(0.2, progressBarUI, () -> {
						try {
							WalletAccountCreate.run(name);
				    	} catch (BTSSystemException e) {
				    		module.isProcessing().set(false);
				    		spaceUI.setIndex(0);
							systemException(e);
							return;
						}
						animateProgress(0.4, progressBarUI, () -> {
							try {
								Model.getInstance().redoAccounts();
								Model.getInstance().redoBalance();
					    	} catch (BTSSystemException e) {
					    		module.isProcessing().set(false);
					    		spaceUI.setIndex(0);
								systemException(e);
								return;
							}
							animateProgress(0.6, progressBarUI, () -> {
								Page_0101_Myself.getInstance().reloadData();
								animateProgress(0.8, progressBarUI, () -> {
									module.reloadData();
									animateProgress(1.0, progressBarUI, () -> {
										module.removePageFromStack();
									});
								});
							});
						});
					});
				});
			});
		}
	}
	
	@FXML
	private void onBack() {
		if (status.equals(Status.PHASE_2))
			setStatus(Status.PHASE_1);
	}
	
	private void manageBindings() {
		if (status.equals(Status.PHASE_1)) {
			nextUI.disableProperty().unbind();
			nextUI.disableProperty().bind(nameUI.textProperty().isEmpty());
		} else if (status.equals(Status.PHASE_2)) {
			nextUI.disableProperty().unbind();	
		}
	}
}