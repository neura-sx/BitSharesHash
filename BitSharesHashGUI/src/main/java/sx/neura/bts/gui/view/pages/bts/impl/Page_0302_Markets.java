package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.view.Page;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.MenuPane;
import sx.neura.bts.gui.view.components.Pagination;
import sx.neura.bts.gui.view.components.SearchBox;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.gui.view.pages.bts.comp.tile.MarketTile;
import sx.neura.bts.json.dto.Market;
import sx.neura.bts.util.Util;

public class Page_0302_Markets extends Page_Bts {
	
	private static Page_0302_Markets instance;

	public static Page_0302_Markets getInstance() {
		if (!isInstance())
			instance = new Page_0302_Markets();
		return instance;
	}

	public static boolean isInstance() {
		return (instance != null);
	}

	private Page_0302_Markets() {
	}
	
	@Override
	protected Page getHomePage() {
		return Page_0300_Exchange.getInstance();
	}
	
	@FXML
	private MenuPane panoramaMenuUI;
	@FXML
	private LayerPane panoramaUI;
	
	@FXML
	private ToggleGroup panoramaToggleGroupUI;
	
	@FXML
	private Pagination pagination01UI;
	@FXML
	private Label pagination01StatusUI;
	@FXML
	private Node pagination01FwdUI;
	@FXML
	private Node pagination01BckUI;
	
	@FXML
	private Pagination pagination02UI;
	@FXML
	private Label pagination02StatusUI;
	@FXML
	private Node pagination02FwdUI;
	@FXML
	private Node pagination02BckUI;
	
	@FXML
	private SearchBox<Market> searchBox01UI;
	@FXML
	private SearchBox<Market> searchBox02UI;
	
	private List<Market> list01;
	private List<Market> list02;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		Util.manageToggleGroup(panoramaToggleGroupUI);
		
		searchBox01UI.setHost(new SearchBox.Host<Market>() {
			@Override
			public List<Market> getList() {
				return list01;
			}
			@Override
			public boolean isSearchMatch(Market item, String[] phraseList) {
				for (String phrase : phraseList) {
					if (!c(getAssetById(item.getQuote_id()).getSymbol(), phrase))
						return false;
				}
				return true;
			}
			@Override
			public void apply() {
				Collections.sort(list01);
				pagination01UI.reset();
			}
		});
		searchBox02UI.setHost(new SearchBox.Host<Market>() {
			@Override
			public List<Market> getList() {
				return list02;
			}
			@Override
			public boolean isSearchMatch(Market item, String[] phraseList) {
				for (String phrase : phraseList) {
					if (!c(getMarketLabel(item), phrase))
						return false;
				}
				return true;
			}
			@Override
			public void apply() {
				Collections.sort(list02);
				pagination02UI.reset();
			}
		});
		
		pagination01UI.setHost(new Pagination.Host() {
			@Override
			public Page getPage() {
				return getInstance();
			}
			@Override
			public double getPrefColumnWidth() {
				return 0;
			}
			@Override
			public double getPrefRowHeight() {
				return 100;
			}
			@Override
			public Label getStatusUI() {
				return pagination01StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination01FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination01BckUI;
			}
			@Override
			public int getListSize() {
				return list01.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new MarketTile(), list01, pagination01UI, index);
			}
			@Override
			public boolean isSearchOn() {
				return searchBox01UI.isSearchOn();
			}
			@Override
			public String getEmptyText() {
				return null;
			}
		});
		pagination02UI.setHost(new Pagination.Host() {
			@Override
			public Page getPage() {
				return getInstance();
			}
			@Override
			public double getPrefColumnWidth() {
				return 0;
			}
			@Override
			public double getPrefRowHeight() {
				return 100;
			}
			@Override
			public Label getStatusUI() {
				return pagination02StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination02FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination02BckUI;
			}
			@Override
			public int getListSize() {
				return list02.size();
			}
			@Override
			public Pane getTilePane(int index) {
				return createTilePane(new MarketTile(), list02, pagination02UI, index);
			}
			@Override
			public boolean isSearchOn() {
				return searchBox02UI.isSearchOn();
			}
			@Override
			public String getEmptyText() {
				return null;
			}
		});
		
		list01 = new ArrayList<Market>();
		list02 = new ArrayList<Market>();
	}	
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			{
				List<Market> items = Model.getInstance().getRecentMarkets();
				searchBox01UI.setItems(items);
				list01.clear();
				list01.addAll(items);
				pagination01UI.reset();
			}
			{
				List<Market> items = Model.getInstance().getBtsMarkets();
				searchBox02UI.setItems(items);
				list02.clear();
				list02.addAll(items);
				pagination02UI.reset();
			}
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		isDataLoaded = false;
		loadData();
	}
	
	@FXML
	private void onPanoramaToggle01() {
		panoramaMenuUI.setIndex(0);
		applyPanoramaIndex(0);
	}
	@FXML
	private void onPanoramaToggle02() {
		panoramaMenuUI.setIndex(1);
		applyPanoramaIndex(1);
	}
	
	@FXML
	private void onPanoramaMenu01() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(0));
		applyPanoramaIndex(0);
	}
	@FXML
	private void onPanoramaMenu02() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(1));
		applyPanoramaIndex(1);
	}
	
	public void setPanoramaIndex(int index) {
		panoramaMenuUI.setIndex(index);
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(index));
		applyPanoramaIndex(index);
	}
	
	private void applyPanoramaIndex(int index) {
		panoramaUI.setIndex(index, () -> makeSnapshot());
		turnOffSearchBoxes();
	}
	
	@FXML
	private void onPagination01Bck() {
		pagination01UI.turnPageBck();
		turnOffSearchBoxes();
	}
	@FXML
	private void onPagination01Fwd() {
		pagination01UI.turnPageFwd();
		turnOffSearchBoxes();
	}
	
	@FXML
	private void onPagination02Bck() {
		pagination02UI.turnPageBck();
		turnOffSearchBoxes();
	}
	@FXML
	private void onPagination02Fwd() {
		pagination02UI.turnPageFwd();
		turnOffSearchBoxes();
	}
	
	@FXML
	private void onMouseClicked() {
		turnOffSearchBoxes();
	}
	
	private void turnOffSearchBoxes() {
		searchBox01UI.turnOff();
		searchBox02UI.turnOff();
	}

}