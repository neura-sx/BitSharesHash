package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;
import sx.neura.bts.json.dto.Asset;
import sx.neura.btsx.gui.view.components.SearchBox;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;

public abstract class AssetList extends Page_Bts {
	
	private final double ROW_HEIGHT = 24;
	private final double ROW_OFFSET = 40;
	
	@FXML
	private Node emptyResultsUI;
	@FXML
	private Node resultsUI;
	
	@FXML
	private Pagination paginationUI;
	
	private int itemsPerPage;
	private TableView<Asset> paginationTableView;
	
	private List<Asset> listUnfiltered;
	private ObservableList<Asset> listFiltered;
	private String[] assetsPhraseList;
	
	protected abstract List<Asset> getAssets();
	protected abstract TableView<Asset> createPaginationTableView();
	
	private TableView<Asset> getPaginationTableView(Integer pageIndex) {
		if (paginationTableView == null)
			paginationTableView = createPaginationTableView();
		ObservableList<Asset> sublist = FXCollections.observableArrayList();
		int fromIndex = pageIndex * itemsPerPage;
		int toIndex = Math.min((pageIndex + 1) * itemsPerPage, listFiltered.size());
		sublist.addAll(listFiltered.subList(fromIndex, toIndex));
		paginationTableView.setItems(sublist);
		paginationTableView.getSelectionModel().select(0);
		return paginationTableView;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		listUnfiltered = new ArrayList<Asset>();
		listFiltered = FXCollections.observableArrayList();
		
		itemsPerPage = 0;
//		paginationUI.setPageFactory((Integer pageIndex) -> {
//			if (itemsPerPage <= 0)
//				return null;
//			return getPaginationTableView(pageIndex);
//		});
		paginationUI.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				itemsPerPage = getItemsPerPage();
				paginationUI.setPageCount(getPageCount());
			}
		});
		
		searchBoxUI.setTarget(new SearchBox.Target() {
			@Override
			public void applySearch(String[] phraseList) {
				assetsPhraseList = phraseList;
				applyAssetSearch();
				resetPagination();
			}
			@Override
			public void cancelSearch() {
				assetsPhraseList = null;
				cancelAssetSearch();
				resetPagination();
			}
		});
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			listUnfiltered.clear();
			listUnfiltered.addAll(getAssets());
			listFiltered.setAll(listUnfiltered);
			applyAssetSearch();
			itemsPerPage = getItemsPerPage();
			resetPagination();
		}
		super.loadData();
	}
	
	private int getItemsPerPage() {
		return (int) ((paginationUI.getHeight() - ROW_OFFSET) / ROW_HEIGHT);
	}
	
	private int getPageCount() {
		return (listFiltered.size() / itemsPerPage) + (listFiltered.size() % itemsPerPage == 0 ? 0 : 1);
	}
	
	private void resetPagination() {
		paginationUI.setPageCount(getPageCount());
		paginationUI.setCurrentPageIndex(0);
		paginationUI.setPageFactory((Integer pageIndex) -> {
			if (itemsPerPage <= 0)
				return null;
			return getPaginationTableView(pageIndex);
		});
		resultsUI.setVisible(!listFiltered.isEmpty());
		emptyResultsUI.setVisible(listFiltered.isEmpty());
	}
	
	@Override
	protected SearchBox createSearchBox() {
		return createSearchBox("Search assets", 75.0, 150.0);
	}
	
	private synchronized void applyAssetSearch() {
		if (assetsPhraseList != null) {
			final int size = listFiltered.size();
			for (int i = size - 1; i >= 0; i--) {
				if (!isSearchMatch(listFiltered.get(i), assetsPhraseList))
					listFiltered.remove(listFiltered.get(i));
			}
			for (Asset item : listUnfiltered) {
				if (!listFiltered.contains(item)
						&& isSearchMatch(item, assetsPhraseList)) {
					listFiltered.add(item);
				}
			}
		}
		Collections.sort(listFiltered);
	}
	
	private synchronized void cancelAssetSearch() {
		for (Asset item : listUnfiltered) {
			if (!listFiltered.contains(item))
				listFiltered.add(item);
		}
		Collections.sort(listFiltered);
	}
	
	private static boolean isSearchMatch(Asset item, String[] phraseList) {
		for (String phrase : phraseList) {
			if (!c(item.getSymbol(), phrase))
				return false;
		}
		return true;
	}
}
