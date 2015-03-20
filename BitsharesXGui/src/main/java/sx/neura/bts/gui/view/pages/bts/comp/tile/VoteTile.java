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
import sx.neura.bts.gui.dto.Vote;
import sx.neura.bts.gui.view.components.IdenticonCanvas;
import sx.neura.bts.gui.view.components.Tile;
import sx.neura.bts.gui.view.components.display.DisplayDuet;
import sx.neura.bts.gui.view.components.display.DisplayText;
import sx.neura.bts.gui.view.pages.bts.impl.Details_Account;
import sx.neura.bts.gui.view.popups.impl.bts.ToggleApproval;
import sx.neura.bts.gui.view.popups.impl.bts.ToggleFavorite;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.util.Util;

public class VoteTile extends Tile<Vote> {

	@FXML
	private Node zoneUI;
	
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
	
	@FXML
	private DisplayText supportUI;
	@FXML
	private DisplayText reliabilityUI;
	
	@FXML
	private DisplayText blocksProducedUI;
	@FXML
	private DisplayText blocksMissedUI;
	
	@FXML
	private DisplayText payRateUI;
	@FXML
	private DisplayDuet payBalanceUI;
	
	@FXML
	private DisplayText myApprovalUI;
	@FXML
	private DisplayText myVotesUI;
	
	private BooleanProperty favoriteProperty;
	private IntegerProperty approvalProperty;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		if (item != null) {
			favoriteProperty = new SimpleBooleanProperty(isAccountFavorite(item.getAccount().getName()));
			approvalProperty = new SimpleIntegerProperty(getAccountApproval(item.getAccount().getName()));
			
			zoneUI.setOnMouseEntered((MouseEvent event) -> {
				if (!favoriteProperty.get())
					favoriteNeutralUI.setVisible(true);
				if (approvalProperty.get() == 0)
					approvalNeutralUI.setVisible(true);
			});
			zoneUI.setOnMouseExited((MouseEvent event) -> {
				if (!favoriteProperty.get())
					favoriteNeutralUI.setVisible(false);
				if (approvalProperty.get() == 0)
					approvalNeutralUI.setVisible(false);
			});
			
			favoritePositiveUI.visibleProperty().bind(favoriteProperty);
			approvalNegativeUI.visibleProperty().bind(approvalProperty.lessThan(0));
			approvalPositiveUI.visibleProperty().bind(approvalProperty.greaterThan(0));
			
			avatarUI.setName(item.getAccount().getName());
			nameUI.setText(Util.crop(item.getAccount().getName(), 20));
			
			if (isDelegate(item.getAccount())) {
				supportUI.setText(String.format("%.2f%s", getDelegateSupport(item.getAccount()) * 100, "%"));
				reliabilityUI.setText(String.format("%.2f%s", getDelegateReliability(item.getAccount()) * 100, "%"));
				
				blocksProducedUI.setText(String.format("%d", item.getAccount().getDelegate_info().getBlocks_produced()));
				blocksMissedUI.setText(String.format("%d", item.getAccount().getDelegate_info().getBlocks_missed()));
				
				payRateUI.setText(String.format("%d%s", item.getAccount().getDelegate_info().getPay_rate(), "%"));
				payBalanceUI.setText(getAmountPair(0, item.getAccount().getDelegate_info().getPay_balance()));
			} else {
				supportUI.setText("n/a");
				reliabilityUI.setText("n/a");
				
				blocksProducedUI.setText("n/a");
				blocksMissedUI.setText("n/a");
				
				payRateUI.setText("n/a");
				payBalanceUI.setText01("n/a");
			}
			
			myApprovalUI.setText(String.format("%s", getAccountApprovalLabel(item.getAccount())));
			
			Asset asset = getAssetById(0);
			myVotesUI.setLabel(String.format("%s (%s)", "My Votes", asset.getSymbol()));
			myVotesUI.setText(getAmount(asset, item.getVotes()));
			
			setOnMouseClicked((MouseEvent event) -> {
				module.jump(new Details_Account(item.getAccount()));
				event.consume();
	    	});
		}
	}
	
	@FXML
	private void onToggleFavorite() {
		ToggleFavorite toggleFavorite = new ToggleFavorite();
		toggleFavorite.setName(item.getAccount().getName());
		toggleFavorite.setFavoriteProperty(favoriteProperty);
		toggleFavorite.show(getModalPane(), module);
	}
	
	@FXML
	private void onToggleApproval() {
		ToggleApproval toggleApproval = new ToggleApproval();
		toggleApproval.setName(item.getAccount().getName());
		toggleApproval.setApprovalProperty(approvalProperty);
		toggleApproval.show(getModalPane(), module);
	}
}
