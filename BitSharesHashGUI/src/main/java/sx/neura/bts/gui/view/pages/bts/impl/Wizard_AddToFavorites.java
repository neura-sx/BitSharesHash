package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.display.DisplayInput;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.json.api.wallet.WalletAccountSetFavorite;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class Wizard_AddToFavorites extends Page_Bts {
	
	@FXML
	private LayerPane spaceUI;
	
	@FXML
	private Node progressTrackUI;
	@FXML
	private Node progressBarUI;

	@FXML
	private Button nextUI;

	@FXML
	private IdenticonCanvas avatarUI;
	@FXML
	private DisplayInput accountUI;
	
	@Override
	public String getTitle() {
		return "AddToFavorites";
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		nextUI.visibleProperty().bind(isOverlay.not());
		
		accountUI.setResponder(() -> {
			avatarUI.setName(accountUI.getText());
		});
		nextUI.setText("Finish");
		nextUI.disableProperty().bind(accountUI.textProperty().isEmpty());
	}
	
	@FXML
	private void onNext() {
		if (accountUI.getText().isEmpty()) {
			userException("Account is not set");
			return;
		}
		Account account = h.getAccount(accountUI.getText());
		if (account == null) {
			userException(String.format("Account '%s' does not exist", accountUI.getText()));
			return;
		}
		if (account.isIs_favorite()) {
			userException(String.format("Account '%s' is already set as favorite", accountUI.getText()));
			return;
		}
		module.isProcessing().set(true);
		spaceUI.setIndex(1, () -> {
			animateProgress(1.0, 500, progressTrackUI, () -> {
				animateProgress(0.2, progressBarUI, () -> {
					try {
						WalletAccountSetFavorite.run(account.getName(), true);
			    	} catch (BTSSystemException e) {
			    		module.isProcessing().set(false);
			    		spaceUI.setIndex(0);
						systemException(e);
						return;
					}
					animateProgress(0.4, progressBarUI, () -> {
						try {
							Model.getInstance().redoAccounts();
				    	} catch (BTSSystemException e) {
				    		module.isProcessing().set(false);
				    		spaceUI.setIndex(0);
							systemException(e);
							return;
						}
						animateProgress(0.6, progressBarUI, () -> {
							Page_0102_Counterparty.getInstance().reloadData();
							animateProgress(0.7, progressBarUI, () -> {
								Page_0202_Delegates.getInstance().reloadData();
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
		});
	}
}
