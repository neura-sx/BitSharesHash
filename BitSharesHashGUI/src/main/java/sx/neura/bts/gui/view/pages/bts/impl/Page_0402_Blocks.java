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
import sx.neura.bts.gui.view.Page;
import sx.neura.bts.gui.view.components.Pagination;
import sx.neura.bts.gui.view.components.SearchBox;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.gui.view.pages.bts.comp.tile.BlockTile;
import sx.neura.bts.json.api.blockchain.BlockchainListBlocks;
import sx.neura.bts.json.dto.Block;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class Page_0402_Blocks extends Page_Bts {
	
	private static Page_0402_Blocks instance;

	public static Page_0402_Blocks getInstance() {
		if (!isInstance())
			instance = new Page_0402_Blocks();
		return instance;
	}

	public static boolean isInstance() {
		return (instance != null);
	}

	private Page_0402_Blocks() {
	}
	
	@Override
	protected Page getHomePage() {
		return Page_0400_System.getInstance();
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
	private SearchBox<Block> searchBoxUI;
	private List<Block> list;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		searchBoxUI.setHost(new SearchBox.Host<Block>() {
			@Override
			public List<Block> getList() {
				return list;
			}
			@Override
			public boolean isSearchMatch(Block item, String[] phraseList) {
				for (String phrase : phraseList) {
					if (!c(item.getBlock_num(), phrase))
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
				return 80;
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
		        return createTilePane(new BlockTile(), list, paginationUI, index);
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
		
		list = new ArrayList<Block>();
	}	
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			List<Block> results;
			try {
				results = BlockchainListBlocks.run(-1, Model.BLOCKS_LIMIT);
			} catch (BTSSystemException e) {
				systemException(e);
				return;
			}
			List<Block> items = new ArrayList<Block>();
			for (Block item : results) {
				if (item.getUser_transaction_ids().size() > 0)
					items.add(item);
			}
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
