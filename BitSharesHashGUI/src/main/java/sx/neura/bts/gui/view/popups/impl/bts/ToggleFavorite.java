package sx.neura.bts.gui.view.popups.impl.bts;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.Popup;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0102_Counterparty;
import sx.neura.bts.gui.view.pages.bts.impl.Page_0202_Delegates;
import sx.neura.bts.json.api.wallet.WalletAccountSetFavorite;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Util;

public class ToggleFavorite extends Popup {
	
	@FXML
	private LayerPane spaceUI;
	
	@FXML
	private Node progressTrackUI;
	@FXML
	private Node progressBarUI;
	
	@FXML
	private Label textUI;
	
	private String name;
	private Callback callback;
	private BooleanProperty favoriteProperty;
	
	public void setName(String name) {
		this.name = name;
	}
	public void setFavoriteProperty(BooleanProperty favoriteProperty) {
		this.favoriteProperty = favoriteProperty;
	}
	public void setCallback(Callback callback) {
		this.callback = callback;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		textUI.setText(favoriteProperty.get() ?
				String.format("Are you sure you want to do remove '%s' from favorites?", Util.crop(name, 30)) :
				String.format("Are you sure you want to do add '%s' to favorites?", Util.crop(name, 30)));
	}
	
	@FXML
	private void onConfirm() {
		spaceUI.setIndex(1, () -> {
			animateProgress(1.0, 500, progressTrackUI, () -> {
				animateProgress(0.2, progressBarUI, () -> {
					try {
						WalletAccountSetFavorite.run(name, !favoriteProperty.get());
						Model.getInstance().redoAccounts();
					} catch (BTSSystemException e) {
						spaceUI.setIndex(0);
						systemException(e);
						return;
					}
					favoriteProperty.set(!favoriteProperty.get());
					animateProgress(0.4, progressBarUI, () -> {
						module.reloadData();
						animateProgress(0.6, progressBarUI, () -> {
							Page_0102_Counterparty.getInstance().reloadData();
							animateProgress(0.8, progressBarUI, () -> {
								Page_0202_Delegates.getInstance().reloadData();
								animateProgress(0.9, progressBarUI, () -> {
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
		});
	}
	
	@Override
	protected void onCancel() {
		hideItself(() -> callback.onCancel());
	}
	
}
