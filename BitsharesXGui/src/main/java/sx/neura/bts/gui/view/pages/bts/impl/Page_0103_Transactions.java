package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.Transaction;
import sx.neura.bts.gui.view.Page;
import sx.neura.bts.gui.view.components.Pagination;
import sx.neura.bts.gui.view.components.display.DisplayChoice;
import sx.neura.bts.gui.view.components.display.DisplayDatePicker;
import sx.neura.bts.gui.view.components.display.DisplayInput;
import sx.neura.bts.gui.view.pages.bts.Page_Bts;
import sx.neura.bts.gui.view.pages.bts.comp.tile.TransactionTile;
import sx.neura.bts.util.Time;

public class Page_0103_Transactions extends Page_Bts {
	
	private static Page_0103_Transactions instance;

	public static Page_0103_Transactions getInstance() {
		if (!isInstance())
			instance = new Page_0103_Transactions();
		return instance;
	}

	public static boolean isInstance() {
		return (instance != null);
	}

	private Page_0103_Transactions() {
	}
	
	@Override
	protected Page getHomePage() {
		return Page_0100_Payments.getInstance();
	}
	
	@FXML
	private DisplayDatePicker timeFromUI;
	@FXML
	private DisplayDatePicker timeToUI;
	
	@FXML
	private DisplayChoice<TransactionType> typeUI;
	@FXML
	private DisplayChoice<TransactionDirection> directionUI;
	
	@FXML
	private DisplayInput accountFromUI;
	@FXML
	private DisplayInput accountToUI;
	
	@FXML
	private DisplayInput assetUI;
	@FXML
	private DisplayInput memoUI;
	
	
	@FXML
	private Pagination paginationUI;
	@FXML
	private Label paginationStatusUI;
	@FXML
	private Node paginationFwdUI;
	@FXML
	private Node paginationBckUI;
	
	private List<Transaction> items;
	private List<Transaction> list;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		timeFromUI.setResponder((LocalDate date) -> applySearch());
		timeToUI.setResponder((LocalDate date) -> applySearch());
		
		typeUI.setList(Arrays.asList(TransactionType.values()));
		typeUI.setRenderer((TransactionType i) -> {return i.getLabel();});
		typeUI.setResponder((TransactionType oldValue, TransactionType newValue) -> applySearch());
		typeUI.isFirstItemEmpty(true);
		
		directionUI.setList(Arrays.asList(TransactionDirection.values()));
		directionUI.setRenderer((TransactionDirection i) -> {return i.getLabel();});
		directionUI.setResponder((TransactionDirection oldValue, TransactionDirection newValue) -> applySearch());
		directionUI.isFirstItemEmpty(true);
		
		accountFromUI.setResponder(() -> applySearch());
		accountToUI.setResponder(() -> applySearch());
		assetUI.setResponder(() -> applySearch());
		memoUI.setResponder(() -> applySearch());
		
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
				return 90;
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
		        return createTilePane(new TransactionTile(), list, paginationUI, index);
			}
			@Override
			public boolean isSearchOn() {
				return false;
			}
			@Override
			public String getEmptyText() {
				return null;
			}
		});
		
		items = Model.getInstance().getTransactions();
		list = new ArrayList<Transaction>();
	}	
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			list.clear();
			list.addAll(items);
			Collections.sort(list);
			paginationUI.reset();
		}
		super.loadData();
	}
	
	@Override
	public void reloadData() {
		isDataLoaded = false;
		loadData();
	}
	
	@FXML
	private void onPaginationBck() {
		paginationUI.turnPageBck();
	}
	@FXML
	private void onPaginationFwd() {
		paginationUI.turnPageFwd();
	}
	
	private void applySearch() {
		final int size = list.size();
		for (int i = size - 1; i >= 0; i--) {
			if (!isSearchMatch(list.get(i)))
				list.remove(list.get(i));
		}
		for (Transaction item : items) {
			if (!list.contains(item) && isSearchMatch(item))
				list.add(item);
		}
		Collections.sort(list);
		paginationUI.reset();
	}
	
	private boolean isSearchMatch(Transaction item) {
		if (timeFromUI.getItem() != null) {
			LocalDateTime limit = LocalDateTime.of(timeFromUI.getItem(), LocalTime.MIN);
			if (!Time.decode(item.getTimestamp()).isAfter(limit))
				return false;
		}
		if (timeToUI.getItem() != null) {
			LocalDateTime limit = LocalDateTime.of(timeToUI.getItem(), LocalTime.MAX);
			if (!Time.decode(item.getTimestamp()).isBefore(limit))
				return false;
		}
		
		if (typeUI.getItem() != null) {
			if (typeUI.getItem().equals(TransactionType.Transfer)) {
				if (item.isIs_market() || item.isIs_market_cancel() ||item.isIs_virtual())
					return false;
			} else if (typeUI.getItem().equals(TransactionType.Market)) {
				if (!item.isIs_market())
					return false;
			} else if (typeUI.getItem().equals(TransactionType.MarketCancel)) {
				if (!item.isIs_market_cancel())
					return false;
			} else if (typeUI.getItem().equals(TransactionType.Virtual)) {
				if (!item.isIs_virtual())
					return false;
			}	
		}
		if (directionUI.getItem() != null) {
			if (directionUI.getItem().equals(TransactionDirection.Inflow)) {
				if (item.getDirection() == null || item.getDirection() <= 0)
					return false;
			} else if (directionUI.getItem().equals(TransactionDirection.Outflow)) {
				if (item.getDirection() == null || item.getDirection() >= 0)
					return false;
			} else if (directionUI.getItem().equals(TransactionDirection.Internal)) {
				if (item.getDirection() == null || item.getDirection() != 0)
					return false;
			} else if (directionUI.getItem().equals(TransactionDirection.System)) {
				if (item.getDirection() != null)
					return false;
			}
		}
		
		if (!accountFromUI.getText().isEmpty()
				&& !c(item.getFrom_account(), accountFromUI.getText()))
			return false;
		if (!accountToUI.getText().isEmpty()
				&& !c(item.getTo_account(), accountToUI.getText()))
			return false;
		if (!assetUI.getText().isEmpty()
				&& !c(getAssetById(item.getAmount().getAsset_id()).getSymbol(), assetUI.getText()))
			return false;
		if (!memoUI.getText().isEmpty()
				&& !c(item.getMemo(), memoUI.getText()))
			return false;
		
		
//		if (timeFromUI.getItem() != null) {
//			LocalDateTime now = LocalDateTime.now();
//			LocalDateTime cut = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0).minusDays(timeFromUI.getItem().getDays());
//			if (Time.decode(item.getTimestamp()).isBefore(cut))
//				return false;
//		}
//		if (timeToUI.getItem() != null) {
//			LocalDateTime now = LocalDateTime.now();
//			LocalDateTime cut = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0).minusDays(timeToUI.getItem().getDays());
//			if (Time.decode(item.getTimestamp()).isAfter(cut))
//				return false;
//		}
		return true;
	}
	
	
	private enum TransactionType {
		All("ALL"), Transfer("transfer"), Market("market"), MarketCancel("market cancel"), Virtual("virtual");
		private TransactionType(String label) {
			this.label = label;
		}
		private String label;
		private String getLabel() {
			return label;
		}
	}
	
	private enum TransactionDirection {
		All("ALL"), Inflow("inflow"), Outflow("outflow"), Internal("internal"), System("system");
		private TransactionDirection(String label) {
			this.label = label;
		}
		private String label;
		private String getLabel() {
			return label;
		}
	}
	
//	private enum TransactionTime {
//		All("ALL", null),
//		today("today", 0),
//		yesterday("yesterday", 1),
//		D07(null, 7),
//		D14(null, 14),
//		D30(null, 30),
//		D60(null, 60),
//		D90(null, 90);
//		private TransactionTime(String label, Integer days) {
//			this.label = label;
//			this.days = days;
//		}
//		private String label;
//		private Integer days;
//		private String getLabel() {
//			if (label != null)
//				return label;
//			return String.format("%d %s", days, "days ago");
//		}
//		private Integer getDays() {
//			return days;
//		}
//	}
}
