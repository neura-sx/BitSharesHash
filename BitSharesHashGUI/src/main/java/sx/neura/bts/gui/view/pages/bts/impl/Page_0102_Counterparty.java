package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
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
import sx.neura.bts.gui.view.pages.bts.comp.tile.AccountTile;
import sx.neura.bts.json.api.blockchain.BlockchainListAccounts;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.RegisteredName;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Util;

public class Page_0102_Counterparty extends Page_Bts {
	
	private static Page_0102_Counterparty instance;

	public static Page_0102_Counterparty getInstance() {
		if (!isInstance())
			instance = new Page_0102_Counterparty();
		return instance;
	}

	public static boolean isInstance() {
		return (instance != null);
	}

	private Page_0102_Counterparty() {
	}
	
	@Override
	protected Page getHomePage() {
		return Page_0100_Payments.getInstance();
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
	private SearchBox<Account> searchBox01UI;
	@FXML
	private SearchBox<RegisteredName> searchBox02UI;
	
	@FXML
	private Node progressIndicatorUI;
	
	private List<Account> list01;
	private List<RegisteredName> list02;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		Util.manageToggleGroup(panoramaToggleGroupUI);
		
		searchBox01UI.setHost(new SearchBox.Host<Account>() {
			@Override
			public List<Account> getList() {
				return list01;
			}
			@Override
			public boolean isSearchMatch(Account item, String[] phraseList) {
				for (String phrase : phraseList) {
					if (!c(item.getName(), phrase))
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
		searchBox02UI.setHost(new SearchBox.Host<RegisteredName>() {
			@Override
			public List<RegisteredName> getList() {
				return list02;
			}
			@Override
			public boolean isSearchMatch(RegisteredName item, String[] phraseList) {
				for (String phrase : phraseList) {
					if (!c(item.getName(), phrase))
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
				return 110;
			}
			@Override
			public double getPrefRowHeight() {
				return 110;
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
		        return createTilePane(new AccountTile<Account>(), list01, pagination01UI, index);
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
				return 110;
			}
			@Override
			public double getPrefRowHeight() {
				return 110;
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
				return createTilePane(new AccountTile<RegisteredName>(), list02, pagination02UI, index);
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
		
		list01 = new ArrayList<Account>();
		list02 = new ArrayList<RegisteredName>();
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded)
			doLoadData(true);
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		doLoadData(false);
	}
	
	private void doLoadData(boolean initial) {
		{
			List<Account> items = Model.getInstance().getFavoriteAccounts();
			list01.clear();
			list01.addAll(items);
			searchBox01UI.setItems(items);
		}
		pagination01UI.reset();
		
		if (initial) {
			new Thread(new RegisteredNamesLoadingTask("")).start();
		} else {
			pagination02UI.reset();
		}
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
	
	private class RegisteredNamesLoadingTask extends Task<Void> {
		private String firstSymbol;
		private RegisteredNamesLoadingTask(String firstSymbol) {
			this.firstSymbol = firstSymbol;
		}
		@Override
		protected Void call() {
			Platform.runLater(() -> {
				progressIndicatorUI.setVisible(true);
            });
			List<RegisteredName> items = new ArrayList<RegisteredName>();
			if (firstSymbol != null) {
				try {
					if (firstSymbol != "") {
						items.addAll(BlockchainListAccounts.run(firstSymbol, Model.REGISTERED_NAMES_LIMIT));
						for (int i = items.size() - 1; i >= 0; i--) {
							if (items.get(i).getName().startsWith(firstSymbol))
								break;
							items.remove(i);
						}
					} else {
						items.addAll(BlockchainListAccounts.run("", Model.REGISTERED_NAMES_LIMIT_ALL));
					}
				} catch (BTSSystemException e) {
					Platform.runLater(() -> {
						progressIndicatorUI.setVisible(false);
						systemException(e);
		            });
				}
			}
			Platform.runLater(() -> {
				list02.clear();
				list02.addAll(items);
				searchBox02UI.setItems(items);
				pagination02UI.reset();
				progressIndicatorUI.setVisible(false);
            });
			return null;
		}
	}
	
//	@FXML
//	private void onAlphabet() {
//		
//	}
//	
//	@FXML
//	private void onAction00() {
//		module.jump(new Wizzard_MakeTransfer());
//	}
//	
//	@FXML
//	private void onAction01() {
//		featureNotImplemented();
//	}
//	@FXML
//	private void onAction02() {
//		userException("Not enough funds");
//	}
//	@FXML
//	private void onAction03() {
//		CommandJson.Error e = new CommandJson.Error();
//		e.setMessage("It's hard to explain what went wrong");
//		systemException(new BTSSystemException(e));
//	}
//	@FXML
//	private void onAction04() {
//		ReadBoolean readBoolean = new ReadBoolean();
//		readBoolean.setMessage("Are you sure you want to do this?");
//		readBoolean.setConfirm("Maybe");
//		readBoolean.setCancel("Cancel");
//		readBoolean.setCallback(new ReadBoolean.Callback() {
//			@Override
//			public void onConfirm() {
//			}
//			@Override
//			public void onCancel() {
//			}
//		});
//		readBoolean.show(getModalPane());
//	}
}
