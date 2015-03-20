package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.DelegateAnnouncement;
import sx.neura.bts.gui.view.Page;
import sx.neura.bts.gui.view.components.Pagination;
import sx.neura.bts.gui.view.components.SearchBox;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.gui.view.pages.bts.comp.tile.DelegateAnnouncementTile;

public class Page_0201_DelegateAnnouncements extends Page_Bts {
	
	private static Page_0201_DelegateAnnouncements instance;

	public static Page_0201_DelegateAnnouncements getInstance() {
		if (!isInstance())
			instance = new Page_0201_DelegateAnnouncements();
		return instance;
	}

	public static boolean isInstance() {
		return (instance != null);
	}

	private Page_0201_DelegateAnnouncements() {
	}
	
	@Override
	protected Page getHomePage() {
		return Page_0200_Community.getInstance();
	}
	
	@FXML
	private Pagination paginationUI;
	@FXML
	private Label paginationStatusUI;
	@FXML
	private Node paginationFwdUI;
	@FXML
	private Node paginationBckUI;
	
	@FXML
	private SearchBox<DelegateAnnouncement> searchBoxUI;
	private List<DelegateAnnouncement> list;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		searchBoxUI.setHost(new SearchBox.Host<DelegateAnnouncement>() {
			@Override
			public List<DelegateAnnouncement> getList() {
				return list;
			}
			@Override
			public boolean isSearchMatch(DelegateAnnouncement item, String[] phraseList) {
				for (String phrase : phraseList) {
					if (!c(item.getDelegate(), phrase))
						return false;
				}
				return true;
			}
			@Override
			public void apply() {
				Collections.sort(list);
				paginationUI.reset();
			}
		});
		
		paginationUI.setHost(new Pagination.Host() {
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
				return 40;
			}
			@Override
			public Label getStatusUI() {
				return paginationStatusUI;
			}
			@Override
			public Node getFwdUI() {
				return paginationFwdUI;
			}
			@Override
			public Node getBckUI() {
				return paginationBckUI;
			}
			@Override
			public int getListSize() {
				return list.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new DelegateAnnouncementTile(), list, paginationUI, index);
			}
			@Override
			public boolean isSearchOn() {
				return searchBoxUI.isSearchOn();
			}
			@Override
			public String getEmptyText() {
				return null;
			}
		});
		
		list = new ArrayList<DelegateAnnouncement>();
	}	
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			List<DelegateAnnouncement> items = Model.getInstance().getDelegateAnnouncements();
			searchBoxUI.setItems(items);
			list.clear();
			list.addAll(items);
			paginationUI.reset();
		}
		super.loadData();
	}

	@FXML
	private void onPaginationBck() {
		paginationUI.turnPageBck();
	}
	@FXML
	private void onPaginationFwd() {
		paginationUI.turnPageFwd();
	}
	
}
