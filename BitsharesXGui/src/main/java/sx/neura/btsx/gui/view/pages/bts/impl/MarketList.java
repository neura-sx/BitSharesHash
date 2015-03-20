package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.dto.AssetPair;
import sx.neura.btsx.gui.view.Page;
import sx.neura.btsx.gui.view.components.SearchBox;
import sx.neura.btsx.gui.view.components.flow.Flow;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;
import sx.neura.btsx.gui.view.pages.bts.comp.flow.AssetPairFlowRenderer;

public abstract class MarketList extends Page_Bts {
	
	@FXML
	protected RadioButton recentMarketsUI;
	@FXML
	protected RadioButton btsMarketsUI;
	@FXML
	protected RadioButton bitAssetMarketsUI;
	@FXML
	protected RadioButton userAssetMarketsUI;
	
	@FXML
	protected Node emptyResultsUI;
	@FXML
	protected Node validResultsUI;
	
	@FXML
	protected Flow<AssetPair> listUI;
	
	private ToggleGroup toggleGroup;
	
	private List<AssetPair> listUnfiltered;
	private ObservableList<AssetPair> listFiltered;
	private String[] marketsPhraseList;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		toggleGroup = new ToggleGroup();
		recentMarketsUI.setToggleGroup(toggleGroup);
		btsMarketsUI.setToggleGroup(toggleGroup);
		bitAssetMarketsUI.setToggleGroup(toggleGroup);
		userAssetMarketsUI.setToggleGroup(toggleGroup);
		
		bitAssetMarketsUI.setDisable(true);
		userAssetMarketsUI.setDisable(true);
		
		toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				loadSection();
			}
		});
		
		listUnfiltered = new ArrayList<AssetPair>();
		listFiltered = FXCollections.observableArrayList();
		
		listUI.setCellFactory((AssetPair i) -> {
			AssetPairFlowRenderer renderer = new AssetPairFlowRenderer();
			renderer.setItem(i);
			renderer.setActor((AssetPair j) -> {
				jump(new MarketDetail(j));
			});
			renderer.setJumper((Page page) -> {
				jump(page);
			});
			return renderer;
		});
		listUI.setItems(listFiltered);
		
		searchBoxUI.setTarget(new SearchBox.Target() {
			@Override
			public void applySearch(String[] phraseList) {
				marketsPhraseList = phraseList;
				applyMarketSearch();
				applyItems();
			}
			@Override
			public void cancelSearch() {
				marketsPhraseList = null;
				cancelMarketSearch();
				applyItems();
			}
		});
		
		manageButton(action01UI, (MouseEvent event) -> {
		});
		manageButton(action02UI, (MouseEvent event) -> {
		});
		
		toggleGroup.selectToggle(recentMarketsUI);
	}
	
	private void loadSection() {
		listUnfiltered.clear();
		if (toggleGroup.getSelectedToggle().equals(recentMarketsUI)) {
			listUnfiltered.addAll(Model.getInstance().getRecentMarkets());
		} else if (toggleGroup.getSelectedToggle().equals(btsMarketsUI)) {
			listUnfiltered.addAll(Model.getInstance().getBtsMarkets());
		} else if (toggleGroup.getSelectedToggle().equals(bitAssetMarketsUI)) {
			listUnfiltered.addAll(Model.getInstance().getBitAssetMarkets());
		} else if (toggleGroup.getSelectedToggle().equals(userAssetMarketsUI)) {
			listUnfiltered.addAll(Model.getInstance().getUserAssetMarkets());
		}
		listFiltered.setAll(listUnfiltered);
		if (marketsPhraseList != null)
			applyMarketSearch();
		applyItems();
	}
	
	private void applyItems() {
		listUI.applyItems();
		emptyResultsUI.setVisible(listFiltered.isEmpty());
		validResultsUI.setVisible(!listFiltered.isEmpty());
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		if (toggleGroup.getSelectedToggle().equals(recentMarketsUI))
			loadSection();
	}
	
	@Override
	protected SearchBox createSearchBox() {
		return createSearchBox("Search pairs", 90.0, 150.0);
	}
	
	private synchronized void applyMarketSearch() {
		final int size = listFiltered.size();
		for (int i = size - 1; i >= 0; i--) {
			if (!isSearchMatch(listFiltered.get(i), marketsPhraseList))
				listFiltered.remove(listFiltered.get(i));
		}
		for (AssetPair item : listUnfiltered) {
			if (!listFiltered.contains(item)
					&& isSearchMatch(item, marketsPhraseList)) {
				listFiltered.add(item);
			}
		}
		//Collections.sort(listFiltered);
	}
	
	private synchronized void cancelMarketSearch() {
		for (AssetPair item : listUnfiltered) {
			if (!listFiltered.contains(item))
				listFiltered.add(item);
		}
		//Collections.sort(listFiltered);
	}
	
	private static boolean isSearchMatch(AssetPair item, String[] phraseList) {
		for (String phrase : phraseList) {
			if ((!c(item.getAsset1().getSymbol(), phrase) || item.getAsset1().getId() == 0)
					&& (!c(item.getAsset2().getSymbol(), phrase) ||  item.getAsset2().getId() == 0))
				return false;
		}
		return true;
	}
	
}
