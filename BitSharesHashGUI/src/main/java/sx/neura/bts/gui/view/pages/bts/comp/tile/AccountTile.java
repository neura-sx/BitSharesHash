package sx.neura.bts.gui.view.pages.bts.comp.tile;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.pages.bts.Tile_Bts;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.popups.impl.bts.ToggleApproval;
import sx.neura.bts.gui.view.popups.impl.bts.ToggleFavorite;
import sx.neura.bts.json.dto.RegisteredName;
import sx.neura.bts.util.Util;

public class AccountTile<T extends RegisteredName> extends Tile_Bts<T> {
	
	@FXML
	private IdenticonCanvas avatarUI;
	@FXML
	private Label nameUI;
	
	@FXML
	private Node favoriteNeutralUI;
	@FXML
	private Node favoritePositiveUI;
	
	@FXML
	private Node approvalNeutralUI;
	@FXML
	private Node approvalNegativeUI;
	@FXML
	private Node approvalPositiveUI;
	
	private BooleanProperty favoriteProperty;
	private IntegerProperty approvalProperty;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			favoriteProperty = new SimpleBooleanProperty(Model.getInstance().isAccountFavorite(item.getName()));
			approvalProperty = new SimpleIntegerProperty(Model.getInstance().getAccountApproval(item.getName()));
			
			setOnMouseEntered((MouseEvent event) -> {
				if (!favoriteProperty.get())
					favoriteNeutralUI.setVisible(true);
				if (approvalProperty.get() == 0)
					approvalNeutralUI.setVisible(true);
			});
			setOnMouseExited((MouseEvent event) -> {
				if (!favoriteProperty.get())
					favoriteNeutralUI.setVisible(false);
				if (approvalProperty.get() == 0)
					approvalNeutralUI.setVisible(false);
			});
			
			favoritePositiveUI.visibleProperty().bind(favoriteProperty);
			approvalNegativeUI.visibleProperty().bind(approvalProperty.lessThan(0));
			approvalPositiveUI.visibleProperty().bind(approvalProperty.greaterThan(0));

			avatarUI.setName(item.getName());
			
			nameUI.setText(Util.crop(item.getName(), 24));
			nameUI.setId(Model.getInstance().isDelegate(item) ? 
					(Model.getInstance().isActiveDelegate(item) ? "sx-delegate-active" : "sx-delegate-pasive") : null);
			
			setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Account(item));
				event.consume();
	    	});
		}
	}
	
	@FXML
	private void onToggleFavorite() {
		ToggleFavorite toggleFavorite = new ToggleFavorite();
		toggleFavorite.setName(item.getName());
		toggleFavorite.setFavoriteProperty(favoriteProperty);
		toggleFavorite.show(getModalPane(), module);
	}
	
	@FXML
	private void onToggleApproval() {
		ToggleApproval toggleApproval = new ToggleApproval();
		toggleApproval.setName(item.getName());
		toggleApproval.setApprovalProperty(approvalProperty);
		toggleApproval.show(getModalPane(), module);
	}

}
