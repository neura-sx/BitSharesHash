package sx.neura.btsx.gui.view.popups.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

public class UpdateVote extends MakeTransfer {
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		introductionUI.setText("You update your vote by transfering your funds to yourself.\nYour funds will remain as they are except for the transaction fee.");
	}
	
	@Override
	protected String getTitle() {
		return "Vote Update";
	}
	
	@Override
	protected Status getInitialStatus() {
		return Status.PHASE_0;
	}
	
	@Override
	protected void onBack(ActionEvent event) {
		if (status.equals(Status.PHASE_1))
			setStatus(Status.PHASE_0);
		else if (status.equals(Status.PHASE_2))
			setStatus(Status.PHASE_1);
	}
}
