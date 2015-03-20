package sx.neura.btsx.gui.view.pages.bts.impl;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import org.controlsfx.control.RangeSlider;

import sx.neura.bts.gui.view.components.candle.CandleChart;
import sx.neura.bts.gui.view.components.candle.CandleExtraValues;
import sx.neura.bts.json.api.blockchain.BlockchainMarketListAsks;
import sx.neura.bts.json.api.blockchain.BlockchainMarketListBids;
import sx.neura.bts.json.api.blockchain.BlockchainMarketListCovers;
import sx.neura.bts.json.api.blockchain.BlockchainMarketListShorts;
import sx.neura.bts.json.api.blockchain.BlockchainMarketOrderHistory;
import sx.neura.bts.json.api.blockchain.BlockchainMarketPriceHistory;
import sx.neura.bts.json.api.blockchain.BlockchainMarketStatus;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory;
import sx.neura.bts.json.api.wallet.WalletMarketCancelOrder;
import sx.neura.bts.json.api.wallet.WalletMarketOrderList;
import sx.neura.bts.json.api.wallet.WalletMarketSubmitAsk;
import sx.neura.bts.json.api.wallet.WalletMarketSubmitBid;
import sx.neura.bts.json.api.wallet.WalletSetSetting;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Market;
import sx.neura.bts.json.dto.MarketOrder;
import sx.neura.bts.json.enumerations.HistoryGranularity;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Time;
import sx.neura.bts.util.Util;
import sx.neura.btsx.gui.Model;
import sx.neura.btsx.gui.dto.AssetPair;
import sx.neura.btsx.gui.view.components.packer.PackerInput;
import sx.neura.btsx.gui.view.components.packer.PackerText;
import sx.neura.btsx.gui.view.pages.bts.Page_Bts;
import sx.neura.btsx.gui.view.popups.ConfirmExecution;
import sx.neura.btsx.gui.view.popups.ReadBoolean;
import sx.neura.btsx.gui.view.popups.bts.impl.CoverShort;

public class MarketDetail extends Page_Bts {
	
	private static final double AXIS_RANGE_FACTOR = 0.05;

    @FXML
	private Label titleUI;
    
    @FXML
	private Node statusBoxUI;
    
	@FXML
	private Node contentBoxUI;
	
	@FXML
	private TabPane tradeBoxUI;
    
    @FXML
	private Label statusUI;
    
    @FXML
	private TabPane tabPaneUI;
    
    @FXML
	private Node optionsBoxUI;
    
    
    @FXML
	private ComboBox<Account> accountsUI;
    
	
    @FXML
	private CandleChart priceHistoryChartUI;
	
	@FXML
    private NumberAxis priceHistoryChartXAxisUI;
	@FXML
    private NumberAxis priceHistoryChartYAxisUI;
	
	@FXML
    private RangeSlider priceHistoryRangeSliderUI;
	@FXML
	private ComboBox<Period> priceHistoryPeriodsUI;
	
	
	@FXML
	private AreaChart<Number, Number> marketDepthChartUI;
	
	@FXML
    private NumberAxis marketDepthChartXAxisUI;
	@FXML
    private NumberAxis marketDepthChartYAxisUI;
	
	@FXML
    private Slider marketDepthSliderUI;
	
	
	@FXML
	private TableView<MarketOrder> marketBidOrdersUI;
	@FXML
	private TableColumn<MarketOrder, String> marketBidOrders01UI;
	@FXML
	private TableColumn<MarketOrder, String> marketBidOrders03UI;
	@FXML
	private TableColumn<MarketOrder, String> marketBidOrders05UI;
	
	@FXML
	private TableView<MarketOrder> marketAskOrdersUI;
	@FXML
	private TableColumn<MarketOrder, String> marketAskOrders01UI;
	@FXML
	private TableColumn<MarketOrder, String> marketAskOrders03UI;
	@FXML
	private TableColumn<MarketOrder, String> marketAskOrders05UI;
	
	@FXML
	private TableView<MarketOrder> marketShortOrdersUI;
	@FXML
	private TableColumn<MarketOrder, String> marketShortOrders01UI;
	@FXML
	private TableColumn<MarketOrder, String> marketShortOrders03UI;
	@FXML
	private TableColumn<MarketOrder, String> marketShortOrders05UI;
	@FXML
	private TableColumn<MarketOrder, String> marketShortOrders07UI;
	
	@FXML
	private TableView<MarketOrder> marketCoverOrdersUI;
	@FXML
	private TableColumn<MarketOrder, String> marketCoverOrders01UI;
	@FXML
	private TableColumn<MarketOrder, String> marketCoverOrders03UI;
	@FXML
	private TableColumn<MarketOrder, String> marketCoverOrders05UI;
	@FXML
	private TableColumn<MarketOrder, String> marketCoverOrders07UI;
	@FXML
	private TableColumn<MarketOrder, String> marketCoverOrders09UI;
	
	@FXML
	private TableView<BlockchainMarketOrderHistory.Result> marketOrdersHistoryUI;
	@FXML
	private TableColumn<BlockchainMarketOrderHistory.Result, String> marketOrdersHistory01UI;
	@FXML
	private TableColumn<BlockchainMarketOrderHistory.Result, String> marketOrdersHistory02UI;
	@FXML
	private TableColumn<BlockchainMarketOrderHistory.Result, String> marketOrdersHistory03UI;
	@FXML
	private TableColumn<BlockchainMarketOrderHistory.Result, String> marketOrdersHistory04UI;
	@FXML
	private TableColumn<BlockchainMarketOrderHistory.Result, String> marketOrdersHistory05UI;
	
	@FXML
	private TableView<WalletMarketOrderList.Result> myBidOrdersUI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myBidOrders01UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myBidOrders03UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myBidOrders05UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result> myBidOrders07UI;
	
	@FXML
	private TableView<WalletMarketOrderList.Result> myAskOrdersUI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myAskOrders01UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myAskOrders03UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myAskOrders05UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result> myAskOrders07UI;
	
	@FXML
	private TableView<WalletMarketOrderList.Result> myShortOrdersUI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myShortOrders01UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myShortOrders03UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myShortOrders05UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myShortOrders07UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result> myShortOrders09UI;
	
	@FXML
	private TableView<WalletMarketOrderList.Result> myCoverOrdersUI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myCoverOrders01UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myCoverOrders03UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myCoverOrders05UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myCoverOrders07UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, String> myCoverOrders09UI;
	@FXML
	private TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result> myCoverOrders11UI;
	
	@FXML
	private TableView<WalletAccountTransactionHistory.Result> myOrdersHistoryUI;
	@FXML
	private TableColumn<WalletAccountTransactionHistory.Result, String> myOrdersHistory01UI;
	@FXML
	private TableColumn<WalletAccountTransactionHistory.Result, String> myOrdersHistory02UI;
	@FXML
	private TableColumn<WalletAccountTransactionHistory.Result, String> myOrdersHistory03UI;
	@FXML
	private TableColumn<WalletAccountTransactionHistory.Result, String> myOrdersHistory04UI;
	
	@FXML
	private PackerText buyAvailableBalanceUI;
	@FXML
	private PackerInput buyQuantityUI;
	@FXML
	private PackerInput buyPriceUI;
	@FXML
	private PackerText buyLowestAskUI;
	@FXML
	private PackerText buyTransactionFeeUI;
	@FXML
	private PackerText buyTotalUI;
	@FXML
	private Button buyExecuteUI;
	
	@FXML
	private PackerText sellAvailableBalanceUI;
	@FXML
	private PackerInput sellQuantityUI;
	@FXML
	private PackerInput sellPriceUI;
	@FXML
	private PackerText sellHighestBidUI;
	@FXML
	private PackerText sellTransactionFeeUI;
	@FXML
	private PackerText sellTotalUI;
	@FXML
	private Button sellExecuteUI;
	
	@FXML
	private PackerText shortAvailableBalanceUI;
	@FXML
	private PackerInput shortCollateralUI;
	@FXML
	private PackerInput shortInterestRateUI;
	@FXML
	private PackerInput shortPriceLimitUI;
	@FXML
	private PackerText shortFeedPriceUI;
	@FXML
	private PackerInput shortQuantityUI;
	@FXML
	private PackerText shortTransactionFeeUI;
	@FXML
	private Button shortExecuteUI;
	
	
    private List<PriceCandle> priceHistoryFeed;
    private List<MarketOrder> marketBidOrdersFeed;
	private List<MarketOrder> marketAskOrdersFeed;
	private List<MarketOrder> marketShortOrdersFeed;
	private List<MarketOrder> marketCoverOrdersFeed;
	private List<BlockchainMarketOrderHistory.Result> marketOrdersHistoryFeed;
    
	private XYChart.Series<Number, Number> priceHistorySeries;
	
	private AreaChart.Series<Number, Number> marketDepthBidSeries;
	private AreaChart.Series<Number, Number> marketDepthAskSeries;
	
	private ChangeListener<Number> priceHistoryLowerBoundListener;
	private ChangeListener<Number> priceHistoryUpperBoundListener;
	
	private AssetPair item;
	private Market market;
	
	private ObservableList<MarketOrder> marketBidOrders;
	private ObservableList<MarketOrder> marketAskOrders;
	private ObservableList<MarketOrder> marketShortOrders;
	private ObservableList<MarketOrder> marketCoverOrders;
	private ObservableList<BlockchainMarketOrderHistory.Result> marketOrdersHistory;
	
	private List<WalletMarketOrderList.Result> myOrdersFeed;
	private List<WalletAccountTransactionHistory.Result> myOrdersHistoryFeed;
	
	private ObservableList<WalletMarketOrderList.Result> myBidOrders;
	private ObservableList<WalletMarketOrderList.Result> myAskOrders;
	private ObservableList<WalletMarketOrderList.Result> myShortOrders;
	private ObservableList<WalletMarketOrderList.Result> myCoverOrders;
	private ObservableList<WalletAccountTransactionHistory.Result> myOrdersHistory;
	
	private long transactionFeeAsset1;
	private long transactionFeeAsset2;
	
	private enum Period {
		M5("5M", 5, 1), M30("30M", 30, 5), H1("1H", 60, 10), H6("6H", 360, 50), H12("12H", 720, 100), D1("1D", 1440, 200);
		private Period(String value, int minutes, int days) {
			this.value = value;
			this.minutes = minutes;
			this.days = days;
		}
		private String value;
		private int days;
		private int minutes;
		
		private int getDays() {
			return days;
		}
		private int getMinutes() {
			return minutes;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	public MarketDetail(AssetPair item) {
		this.item = item;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		priceHistoryLowerBoundListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				priceHistoryChartXAxisUI.setLowerBound(priceHistoryRangeSliderUI.getLowValue() + priceHistoryFeed.size());
				adjustPriceHistoryYAxisBounds();
			}
		};
		priceHistoryUpperBoundListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				priceHistoryChartXAxisUI.setUpperBound(priceHistoryRangeSliderUI.getHighValue() + priceHistoryFeed.size());
				adjustPriceHistoryYAxisBounds();
			}
		};
		
		marketDepthSliderUI.setMin(0.4);
		marketDepthSliderUI.setMax(0.8);
		marketDepthSliderUI.setValue(0.5);
		marketDepthSliderUI.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				populateMarketDepthChart(false);
			}
		});
		
		titleUI.setText(String.format("%s:%s", item.getAsset1().getSymbol(), item.getAsset2().getSymbol()));
		tradeBoxUI.tabMinWidthProperty().bind(tradeBoxUI.widthProperty().subtract(60).divide(3));
		statusUI.setText("Loading..");
		
		priceHistoryPeriodsUI.visibleProperty().bind(tabPaneUI.getSelectionModel().selectedIndexProperty().isEqualTo(0));
		priceHistoryPeriodsUI.setItems(FXCollections.observableArrayList(Period.values()));
		priceHistoryPeriodsUI.getSelectionModel().select(Period.H6);
		priceHistoryPeriodsUI.setOnAction((ActionEvent event) -> {
			if (buildPriceHistoryFeed())
				populatePriceHistoryChart();
		});
		
		accountsUI.minWidthProperty().bind(tradeBoxUI.widthProperty());
		accountsUI.setItems(FXCollections.observableArrayList(Model.getInstance().getMyAccounts()));
		accountsUI.setButtonCell(new AccountListCell());
		accountsUI.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
			@Override
			public ListCell<Account> call(ListView<Account> param) {
				return new AccountListCell();
			}
		});
		accountsUI.getSelectionModel().select(0);
		accountsUI.setOnAction((ActionEvent event) -> {
			populateMyOrders(false);
		});
    	
		{
			priceHistoryChartXAxisUI.setTickUnit(10);
			priceHistoryChartXAxisUI.setMinorTickCount(0);
			
			priceHistoryChartYAxisUI.setTickUnit(0.0010);
			priceHistoryChartYAxisUI.setMinorTickCount(2);
			
			priceHistoryChartXAxisUI.setTickLabelFormatter(new StringConverter<Number>() {
				@Override
				public String toString(Number n) {
					if (n.intValue() < n.doubleValue())
						return "";
					int d = priceHistoryFeed.size() - n.intValue();
					if (d < 0)
						return "";
					if (d == 0)
						return "*";
					return String.format("%02d", d);
				}
				@Override
				public Number fromString(String s) {
					return null;
				}
			});
	        
	        priceHistoryChartYAxisUI.setTickLabelFormatter(new StringConverter<Number>() {
				@Override
				public String toString(Number n) {
					return String.format("%8.4f", n.doubleValue());
				}
				@Override
				public Number fromString(String s) {
					return null;
				}
			});
		}
		{
			priceHistoryFeed = new ArrayList<PriceCandle>();
			marketBidOrdersFeed = new ArrayList<MarketOrder>();
        	marketAskOrdersFeed = new ArrayList<MarketOrder>();
        	marketShortOrdersFeed = new ArrayList<MarketOrder>();
        	marketCoverOrdersFeed = new ArrayList<MarketOrder>();
        	marketOrdersHistoryFeed = new ArrayList<BlockchainMarketOrderHistory.Result>();
		}
        {
    		priceHistorySeries = new XYChart.Series<Number, Number>();
        	ObservableList<XYChart.Series<Number, Number>> data = FXCollections.observableArrayList();
	        data.add(priceHistorySeries);
	        priceHistoryChartUI.setData(data);
        }
        {
        	marketDepthBidSeries = new AreaChart.Series<Number, Number>();
        	marketDepthAskSeries = new AreaChart.Series<Number, Number>();
        	ObservableList<AreaChart.Series<Number, Number>> data = FXCollections.observableArrayList();
        	data.add(marketDepthBidSeries);
        	data.add(marketDepthAskSeries);
        	marketDepthChartUI.setData(data);
        }
        {
        	marketBidOrdersUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        	marketBidOrdersUI.setPlaceholder(new Label("There are no bid orders"));
    		
        	marketBidOrders01UI.setText(String.format("%s (%s/%s)", "Price", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	marketBidOrders01UI.setCellValueFactory(i -> new SimpleStringProperty(
        			String.format("%.8f", getRealPrice(i.getValue().getMarket_index().getOrder_price()))));
        	
        	marketBidOrders03UI.setText(String.format("%s (%s)", "Quantity", item.getAsset1().getSymbol()));
        	marketBidOrders03UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset1(),
        					(long) (i.getValue().getState().getBalance() / i.getValue().getMarket_index().getOrder_price().getRatio()))));
        	
        	marketBidOrders05UI.setText(String.format("%s (%s)", "Total", item.getAsset2().getSymbol()));
        	marketBidOrders05UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset2(),	i.getValue().getState().getBalance())));
        	
        	adjustTableColumn(marketBidOrders01UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketBidOrders03UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketBidOrders05UI, Pos.CENTER_RIGHT);
        	
        	marketBidOrders = FXCollections.observableArrayList();
        	marketBidOrdersUI.setItems(marketBidOrders);
        }
        {
        	marketAskOrdersUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        	marketAskOrdersUI.setPlaceholder(new Label("There are no ask orders"));
    		
        	marketAskOrders01UI.setText(String.format("%s (%s/%s)", "Price", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	marketAskOrders01UI.setCellValueFactory(i -> new SimpleStringProperty(
        			String.format("%.8f", getRealPrice(i.getValue().getMarket_index().getOrder_price()))));
        	
        	marketAskOrders03UI.setText(String.format("%s (%s)", "Quantity", item.getAsset1().getSymbol()));
        	marketAskOrders03UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset1(),	i.getValue().getState().getBalance())));
        	
        	marketAskOrders05UI.setText(String.format("%s (%s)", "Total", item.getAsset2().getSymbol()));
        	marketAskOrders05UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset2(),
        					(long) (i.getValue().getState().getBalance() * i.getValue().getMarket_index().getOrder_price().getRatio()))));

        	adjustTableColumn(marketAskOrders01UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketAskOrders03UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketAskOrders05UI, Pos.CENTER_RIGHT);
        	
        	marketAskOrders = FXCollections.observableArrayList();
        	marketAskOrdersUI.setItems(marketAskOrders);
        }
        {
        	marketShortOrdersUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        	marketShortOrdersUI.setPlaceholder(new Label("There are no short orders"));
    		
        	marketShortOrders01UI.setText(String.format("%s (%s)", "Collateral", item.getAsset1().getSymbol()));
        	marketShortOrders01UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset1(),	i.getValue().getState().getBalance())));
        	
        	marketShortOrders03UI.setText(String.format("%s (%s)", "Interest", "%"));
        	marketShortOrders03UI.setCellValueFactory(i -> new SimpleStringProperty(
        			String.format("%.8f", i.getValue().getMarket_index().getOrder_price().getRatio() * 100)));
        	
        	marketShortOrders05UI.setText(String.format("%s (%s)", "Quantity", item.getAsset2().getSymbol()));
        	marketShortOrders05UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset2(),
        					(long) (i.getValue().getState().getBalance() * i.getValue().getMarket_index().getOrder_price().getRatio() * 0.5))));
        	
        	marketShortOrders07UI.setText(String.format("%s (%s/%s)", "Limit", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	marketShortOrders07UI.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getState().getLimit_price() != null ?
        			String.format("%.8f", getRealPrice(i.getValue().getState().getLimit_price())) : "None"));
        	
        	adjustTableColumn(marketShortOrders01UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketShortOrders03UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketShortOrders05UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketShortOrders07UI, Pos.CENTER_RIGHT);
        	
        	marketShortOrders = FXCollections.observableArrayList();
        	marketShortOrdersUI.setItems(marketShortOrders);
        }
        {
        	marketCoverOrdersUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        	marketCoverOrdersUI.setPlaceholder(new Label("There are no margin call orders"));
    		
        	marketCoverOrders01UI.setText(String.format("%s (%s/%s)", "Call price", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	marketCoverOrders01UI.setCellValueFactory(i -> new SimpleStringProperty(
        			String.format("%.8f", getRealPrice(i.getValue().getMarket_index().getOrder_price()))));
        	
        	marketCoverOrders03UI.setText(String.format("%s (%s)", "Interest", "%"));
        	marketCoverOrders03UI.setCellValueFactory(i -> new SimpleStringProperty(
        			String.format("%.8f", i.getValue().getInterest_rate().getRatio() * 100)));
        	
        	marketCoverOrders05UI.setText(String.format("%s (%s)", "Owed", item.getAsset2().getSymbol()));
        	marketCoverOrders05UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset2(), i.getValue().getState().getBalance())));
        	
        	marketCoverOrders07UI.setText(String.format("%s (%s)", "Collateral", item.getAsset1().getSymbol()));
        	marketCoverOrders07UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset1(),	i.getValue().getCollateral())));
        	
        	marketCoverOrders09UI.setText(String.format("%s", "Expiration"));
        	marketCoverOrders09UI.setCellValueFactory(i -> new SimpleStringProperty(
        			Time.format(i.getValue().getExpiration(), Time.Format.DATE_LONG_FORMAT)));
        	
        	adjustTableColumn(marketCoverOrders01UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketCoverOrders03UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketCoverOrders05UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketCoverOrders07UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketCoverOrders09UI, Pos.CENTER_RIGHT);
        	
        	marketCoverOrders = FXCollections.observableArrayList();
        	marketCoverOrdersUI.setItems(marketCoverOrders);
        }        
        {
        	marketOrdersHistoryUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        	marketOrdersHistoryUI.setPlaceholder(new Label("There is no history"));
        	
        	marketOrdersHistory01UI.setText("Type");
        	marketOrdersHistory01UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getOrderTypeLabel(i.getValue().getBid_type(), i.getValue().getAsk_type())));
        	
        	marketOrdersHistory02UI.setText(String.format("%s (%s/%s)", "Price", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	marketOrdersHistory02UI.setCellValueFactory(i -> new SimpleStringProperty(
        			String.format("%.8f", getRealPrice(i.getValue().getBid_price()))));
        	
        	marketOrdersHistory03UI.setText(String.format("%s (%s)", "Paid", item.getAsset1().getSymbol()));
        	marketOrdersHistory03UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmountPair(i.getValue().getAsk_paid())[1]));
        	
        	marketOrdersHistory04UI.setText(String.format("%s (%s)", "Received", item.getAsset2().getSymbol()));
        	marketOrdersHistory04UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmountPair(i.getValue().getAsk_received())[1]));
        	
        	marketOrdersHistory05UI.setText("Time");
        	marketOrdersHistory05UI.setCellValueFactory(i -> new SimpleStringProperty(
        			Time.format(i.getValue().getTimestamp())));
        	
        	adjustTableColumn(marketOrdersHistory02UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketOrdersHistory03UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(marketOrdersHistory04UI, Pos.CENTER_RIGHT);
        	
        	marketOrdersHistory = FXCollections.observableArrayList();
        	marketOrdersHistoryUI.setItems(marketOrdersHistory);
        }
        {
        	myOrdersFeed = new ArrayList<WalletMarketOrderList.Result>();
        	myOrdersHistoryFeed = new ArrayList<WalletAccountTransactionHistory.Result>();
        }
        {
        	myBidOrdersUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        	myBidOrdersUI.setPlaceholder(new Label("There are no buy orders"));
        	
        	myBidOrders01UI.setText(String.format("%s (%s/%s)", "Price", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	myBidOrders01UI.setCellValueFactory(i -> new SimpleStringProperty(
        			String.format("%.8f", getRealPrice(i.getValue().getOrder().getMarket_index().getOrder_price()))));
        	
        	myBidOrders03UI.setText(String.format("%s (%s)", "Quantity", item.getAsset1().getSymbol()));
        	myBidOrders03UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(i.getValue().getOrder().getMarket_index().getOrder_price().getBase_asset_id(),
        					(long) (i.getValue().getOrder().getState().getBalance() / i.getValue().getOrder().getMarket_index().getOrder_price().getRatio()))));
        	
        	myBidOrders05UI.setText(String.format("%s (%s)", "Total", item.getAsset2().getSymbol()));
        	myBidOrders05UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(i.getValue().getOrder().getMarket_index().getOrder_price().getQuote_asset_id(),
        					i.getValue().getOrder().getState().getBalance())));
        	
        	myBidOrders07UI.setText("Action");
        	myBidOrders07UI.setCellValueFactory(i -> new SimpleObjectProperty<WalletMarketOrderList.Result>(
        			i.getValue()));
        	
        	myBidOrders07UI.setCellFactory(new Callback<TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result>,
        			TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result>>() {
				@Override
				public TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result> call(
						TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result> param) {
					return new TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result>() {
						private StackPane stackPane;
						private String id;
						{
				    		setAlignment(Pos.CENTER);
				    		stackPane = new StackPane();
			            	Button button = new Button("Remove");
			            	button.setPrefWidth(60.0);
			            	button.getStyleClass().add("tableViewActionButtonSC");
			            	button.setOnAction(ActionEvent -> {
			            		removeOrder(getItem(), new ReadBoolean.Callback() {
									@Override
									public void onConfirm() {
										setStatus(false);
									}
									@Override
									public void onCancel() {
										setStatus(true);
									}
			            		});
			            	});
			            	Label label = new Label("Removing..");
				            stackPane.getChildren().addAll(button, label);
				            setStatus(true);
				    	}
						private void setStatus(boolean status) {
							stackPane.getChildren().get(0).setVisible(status);
							stackPane.getChildren().get(1).setVisible(!status);
						}
						@Override
				        public void updateItem(WalletMarketOrderList.Result item, boolean empty) {
				            super.updateItem(item, empty);
				            if (item == null || empty) {
				                setText(null);
				                setGraphic(null);
				            } else {
				            	if (id == null || !id.equals(item.getId())) {
				            		setStatus(true);
				            		id = item.getId();
				            	}
				            	setGraphic(stackPane);
				            	setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				            }
				        }
					};
				}
			});
        	

        	adjustTableColumn(myBidOrders01UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(myBidOrders03UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(myBidOrders05UI, Pos.CENTER_RIGHT);
        	
        	myBidOrders = FXCollections.observableArrayList();
        	myBidOrdersUI.setItems(myBidOrders);
        }
        {
        	myAskOrdersUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        	myAskOrdersUI.setPlaceholder(new Label("There are no sell orders"));
        	
        	myAskOrders01UI.setText(String.format("%s (%s/%s)", "Price", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	myAskOrders01UI.setCellValueFactory(i -> new SimpleStringProperty(
        			String.format("%.8f", getRealPrice(i.getValue().getOrder().getMarket_index().getOrder_price()))));
        	
        	myAskOrders03UI.setText(String.format("%s (%s)", "Quantity", item.getAsset1().getSymbol()));
        	myAskOrders03UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(i.getValue().getOrder().getMarket_index().getOrder_price().getBase_asset_id(),
        					i.getValue().getOrder().getState().getBalance())));
        	
        	myAskOrders05UI.setText(String.format("%s (%s)", "Total", item.getAsset2().getSymbol()));
        	myAskOrders05UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(i.getValue().getOrder().getMarket_index().getOrder_price().getQuote_asset_id(),
        					(long) (i.getValue().getOrder().getState().getBalance() * i.getValue().getOrder().getMarket_index().getOrder_price().getRatio()))));
        	
        	myAskOrders07UI.setText("Action");
        	myAskOrders07UI.setCellValueFactory(i -> new SimpleObjectProperty<WalletMarketOrderList.Result>(
        			i.getValue()));
        	
        	myAskOrders07UI.setCellFactory(new Callback<TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result>,
        			TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result>>() {
				@Override
				public TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result> call(
						TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result> param) {
					return new TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result>() {
						private StackPane stackPane;
						private String id;
						{
				    		setAlignment(Pos.CENTER);
				    		stackPane = new StackPane();
			            	Button button = new Button("Remove");
			            	button.setPrefWidth(60.0);
			            	button.getStyleClass().add("tableViewActionButtonSC");
			            	button.setOnAction(ActionEvent -> {
			            		removeOrder(getItem(), new ReadBoolean.Callback() {
									@Override
									public void onConfirm() {
										setStatus(false);
									}
									@Override
									public void onCancel() {
										setStatus(true);
									}
			            		});
			            	});
			            	Label label = new Label("Removing..");
				            stackPane.getChildren().addAll(button, label);
				            setStatus(true);
				    	}
						private void setStatus(boolean status) {
							stackPane.getChildren().get(0).setVisible(status);
							stackPane.getChildren().get(1).setVisible(!status);
						}
						@Override
				        public void updateItem(WalletMarketOrderList.Result item, boolean empty) {
				            super.updateItem(item, empty);
				            if (item == null || empty) {
				                setText(null);
				                setGraphic(null);
				            } else {
				            	if (id == null || !id.equals(item.getId())) {
				            		setStatus(true);
				            		id = item.getId();
				            	}
				            	setGraphic(stackPane);
				            	setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				            }
				        }
					};
				}
			});

        	adjustTableColumn(myAskOrders01UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(myAskOrders03UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(myAskOrders05UI, Pos.CENTER_RIGHT);
        	
        	myAskOrders = FXCollections.observableArrayList();
        	myAskOrdersUI.setItems(myAskOrders);
        }
        {
        	myShortOrdersUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        	myShortOrdersUI.setPlaceholder(new Label("There are no short orders"));
        	
        	myShortOrders01UI.setText(String.format("%s (%s)", "Collateral", item.getAsset1().getSymbol()));
        	myShortOrders01UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset1(),	i.getValue().getOrder().getState().getBalance())));
        	
        	myShortOrders03UI.setText(String.format("%s (%s)", "Interest", "%"));
        	myShortOrders03UI.setCellValueFactory(i -> new SimpleStringProperty(
        			String.format("%.8f", i.getValue().getOrder().getMarket_index().getOrder_price().getRatio() * 100)));
        	
        	myShortOrders05UI.setText(String.format("%s (%s)", "Quantity", item.getAsset2().getSymbol()));
        	myShortOrders05UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset2(),
        					(long) (i.getValue().getOrder().getState().getBalance() * i.getValue().getOrder().getMarket_index().getOrder_price().getRatio() * 0.5))));
        	
        	myShortOrders07UI.setText(String.format("%s (%s/%s)", "Limit", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	myShortOrders07UI.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().getOrder().getState().getLimit_price() != null ?
        			String.format("%.8f", getRealPrice(i.getValue().getOrder().getState().getLimit_price())) : "None"));
        	
        	myShortOrders09UI.setText("Action");
        	myShortOrders09UI.setCellValueFactory(i -> new SimpleObjectProperty<WalletMarketOrderList.Result>(
        			i.getValue()));
        	
        	myShortOrders09UI.setCellFactory(new Callback<TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result>,
        			TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result>>() {
				@Override
				public TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result> call(
						TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result> param) {
					return new TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result>() {
						private StackPane stackPane;
						private String id;
						{
				    		setAlignment(Pos.CENTER);
				    		stackPane = new StackPane();
			            	Button button = new Button("Remove");
			            	button.setPrefWidth(60.0);
			            	button.getStyleClass().add("tableViewActionButtonSC");
			            	button.setOnAction(ActionEvent -> {
			            		removeOrder(getItem(), new ReadBoolean.Callback() {
									@Override
									public void onConfirm() {
										setStatus(false);
									}
									@Override
									public void onCancel() {
										setStatus(true);
									}
			            		});
			            	});
			            	Label label = new Label("Removing..");
				            stackPane.getChildren().addAll(button, label);
				            setStatus(true);
				    	}
						private void setStatus(boolean status) {
							stackPane.getChildren().get(0).setVisible(status);
							stackPane.getChildren().get(1).setVisible(!status);
						}
						@Override
				        public void updateItem(WalletMarketOrderList.Result item, boolean empty) {
				            super.updateItem(item, empty);
				            if (item == null || empty) {
				                setText(null);
				                setGraphic(null);
				            } else {
				            	if (id == null || !id.equals(item.getId())) {
				            		setStatus(true);
				            		id = item.getId();
				            	}
				            	setGraphic(stackPane);
				            	setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				            }
				        }
					};
				}
			});
        	
        	adjustTableColumn(myShortOrders01UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(myShortOrders03UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(myShortOrders05UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(myShortOrders07UI, Pos.CENTER_RIGHT);
        	
        	myShortOrders = FXCollections.observableArrayList();
        	myShortOrdersUI.setItems(myShortOrders);
        }
        {
        	myCoverOrdersUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        	myCoverOrdersUI.setPlaceholder(new Label("There are no margin call orders"));
        	
        	myCoverOrders01UI.setText(String.format("%s (%s/%s)", "Call price", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	myCoverOrders01UI.setCellValueFactory(i -> new SimpleStringProperty(
        			String.format("%.8f", getRealPrice(i.getValue().getOrder().getMarket_index().getOrder_price()))));
        	
        	myCoverOrders03UI.setText(String.format("%s (%s)", "Interest", "%"));
        	myCoverOrders03UI.setCellValueFactory(i -> new SimpleStringProperty(
        			String.format("%.8f", i.getValue().getOrder().getInterest_rate().getRatio() * 100)));
        	
        	myCoverOrders05UI.setText(String.format("%s (%s)", "Owed", item.getAsset2().getSymbol()));
        	myCoverOrders05UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset2(), i.getValue().getOrder().getState().getBalance())));
        	
        	myCoverOrders07UI.setText(String.format("%s (%s)", "Collateral", item.getAsset1().getSymbol()));
        	myCoverOrders07UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmount(item.getAsset1(),	i.getValue().getOrder().getCollateral())));
        	
        	myCoverOrders09UI.setText(String.format("%s", "Expiration"));
        	myCoverOrders09UI.setCellValueFactory(i -> new SimpleStringProperty(
        			Time.format(i.getValue().getOrder().getExpiration(), Time.Format.DATE_LONG_FORMAT)));
        	
        	myCoverOrders11UI.setText("Action");
        	myCoverOrders11UI.setCellValueFactory(i -> new SimpleObjectProperty<WalletMarketOrderList.Result>(
        			i.getValue()));
        	
        	myCoverOrders11UI.setCellFactory(new Callback<TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result>,
        			TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result>>() {
				@Override
				public TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result> call(
						TableColumn<WalletMarketOrderList.Result, WalletMarketOrderList.Result> param) {
					return new TableCell<WalletMarketOrderList.Result, WalletMarketOrderList.Result>() {
						private StackPane stackPane;
						private String id;
						{
				    		setAlignment(Pos.CENTER);
				    		stackPane = new StackPane();
			            	Button button = new Button("Cover");
			            	button.setPrefWidth(60.0);
			            	button.getStyleClass().add("tableViewActionButtonSC");
			            	button.setOnAction(ActionEvent -> {
			            		coverShort(getItem(), new CoverShort.Callback() {
									@Override
									public void onActionComplete() {
										setStatus(false);
									}
									@Override
									public void onActionCancelled() {
										setStatus(true);
									}
			            		});
			            	});
			            	Label label = new Label("Covering..");
				            stackPane.getChildren().addAll(button, label);
				            setStatus(true);
				    	}
						private void setStatus(boolean status) {
							stackPane.getChildren().get(0).setVisible(status);
							stackPane.getChildren().get(1).setVisible(!status);
						}
						@Override
				        public void updateItem(WalletMarketOrderList.Result item, boolean empty) {
				            super.updateItem(item, empty);
				            if (item == null || empty) {
				                setText(null);
				                setGraphic(null);
				            } else {
				            	if (id == null || !id.equals(item.getId())) {
				            		setStatus(true);
				            		id = item.getId();
				            	}
				            	setGraphic(stackPane);
				            	setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
				            }
				        }
					};
				}
			});
        	
        	adjustTableColumn(myCoverOrders01UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(myCoverOrders03UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(myCoverOrders05UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(myCoverOrders07UI, Pos.CENTER_RIGHT);
        	adjustTableColumn(myCoverOrders09UI, Pos.CENTER_RIGHT);
        	
        	myCoverOrders = FXCollections.observableArrayList();
        	myCoverOrdersUI.setItems(myCoverOrders);
        }
        {
        	myOrdersHistoryUI.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        	myOrdersHistoryUI.setPlaceholder(new Label("There is no history"));
        	
        	myOrdersHistory01UI.setCellValueFactory(i -> new SimpleStringProperty(
        			i.getValue().getLedger_entries().get(0).getMemo()));
        	
        	myOrdersHistory02UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmountPair(i.getValue().getLedger_entries().get(0).getAmount())[1]));
        	
        	myOrdersHistory03UI.setCellValueFactory(i -> new SimpleStringProperty(
        			getAmountPair(i.getValue().getLedger_entries().get(0).getAmount())[0]));
        	
        	myOrdersHistory04UI.setCellValueFactory(i -> new SimpleStringProperty(
        			Time.format(i.getValue().getTimestamp())));
        	
        	adjustTableColumn(myOrdersHistory02UI, Pos.CENTER_RIGHT);
        	
        	myOrdersHistory = FXCollections.observableArrayList();
        	myOrdersHistoryUI.setItems(myOrdersHistory);
        }
        {
        	buyAvailableBalanceUI.setLabel(String.format("%s (%s)", "Available Balance", item.getAsset2().getSymbol()));
        	buyQuantityUI.setLabel(String.format("%s (%s)", "Quantity", item.getAsset1().getSymbol()));
        	buyQuantityUI.setResponder(() -> {
				populateBuyTotal();
			});
        	buyPriceUI.setLabel(String.format("%s (%s/%s)", "Price", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	buyPriceUI.setResponder(() -> {
				populateBuyTotal();
			});
        	buyLowestAskUI.setLabel(String.format("%s (%s/%s)", "Lowest Ask", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	buyLowestAskUI.setCallback(() -> {
        		buyPriceUI.setText(buyLowestAskUI.getText());
        		populateBuyTotal();
			});
        	buyTransactionFeeUI.setLabel(String.format("%s (%s)", "Fee", item.getAsset2().getSymbol()));
        	buyTotalUI.setLabel(String.format("%s (%s)", "Total", item.getAsset2().getSymbol()));
        	buyExecuteUI.setText(String.format("%s %s", "Buy", item.getAsset1().getSymbol()));
        	buyExecuteUI.disableProperty().bind(buyTotalUI.getTextProperty().isEqualTo(""));
        }
        {
        	sellAvailableBalanceUI.setLabel(String.format("%s (%s)", "Available Balance", item.getAsset1().getSymbol()));
        	sellQuantityUI.setLabel(String.format("%s (%s)", "Quantity", item.getAsset1().getSymbol()));
        	sellQuantityUI.setResponder(() -> {
				populateSellTotal();
			});
        	sellPriceUI.setLabel(String.format("%s (%s/%s)", "Price", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	sellPriceUI.setResponder(() -> {
				populateSellTotal();
			});
        	sellHighestBidUI.setLabel(String.format("%s (%s/%s)", "Highest Bid", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	sellHighestBidUI.setCallback(() -> {
        		sellPriceUI.setText(sellHighestBidUI.getText());
        		populateSellTotal();
			});
        	sellTransactionFeeUI.setLabel(String.format("%s (%s)", "Fee", item.getAsset2().getSymbol()));
        	sellTotalUI.setLabel(String.format("%s (%s)", "Total", item.getAsset2().getSymbol()));
        	sellExecuteUI.setText(String.format("%s %s", "Sell", item.getAsset1().getSymbol()));
        	sellExecuteUI.disableProperty().bind(sellTotalUI.getTextProperty().isEqualTo(""));
        }
        {
        	shortAvailableBalanceUI.setLabel(String.format("%s (%s)", "Available Balance", item.getAsset1().getSymbol()));
        	shortCollateralUI.setLabel(String.format("%s (%s)", "Collateral", item.getAsset1().getSymbol()));
        	shortInterestRateUI.setLabel(String.format("%s (%s)", "Interest Rate", "APR"));
        	shortPriceLimitUI.setLabel(String.format("%s (%s/%s)", "Price Limit", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	shortPriceLimitUI.setPrompt(String.format("(%s)", "Optional"));
        	shortFeedPriceUI.setLabel(String.format("%s (%s/%s)", "Feed Price", item.getAsset2().getSymbol(), item.getAsset1().getSymbol()));
        	shortQuantityUI.setLabel(String.format("%s (%s)", "Quantity", item.getAsset2().getSymbol()));
        	shortTransactionFeeUI.setLabel(String.format("%s (%s)", "Fee", item.getAsset1().getSymbol()));
        	shortExecuteUI.setText(String.format("%s %s", "Short", item.getAsset2().getSymbol()));
        }
	}

	@Override
	public void loadData() {
		if (!isDataLoaded) {
			if (buildPriceHistoryFeed()) {
				populatePriceHistoryChart();
				populateMarketOrders(false);
				populateMarketDepthChart(false);
				populateMarketOrdersHistory(false);
				populateMyAvailableBalance(false);
				populateMyOrders(false);
				populateMyOrdersHistory(false);
		    	transactionFeeAsset1 = getTransactionFee(item.getAsset1());
		    	transactionFeeAsset2 = getTransactionFee(item.getAsset2());
		    	buyTransactionFeeUI.setText(getAmount(item.getAsset2(), transactionFeeAsset2));
	    		sellTransactionFeeUI.setText(getAmount(item.getAsset2(), transactionFeeAsset2));
	    		shortTransactionFeeUI.setText(getAmount(item.getAsset1(), transactionFeeAsset1));
			}
		}
		super.loadData();
	}
	
	@Override
	public void ping() {
		if (isDataLoaded) {
			if (updateFeed())
				updatePriceHistoryChart();
			if (populateMarketOrders(true))
				populateMarketDepthChart(true);
			populateMarketOrdersHistory(true);
			populateMyAvailableBalance(true);
			populateMyOrders(true);
			populateMyOrdersHistory(true);
		}
		super.ping();
	}
	
	private double getFeedPrice() {
		return market.getCurrent_feed_price();
	}
    
    private void populatePriceHistoryChart() {
    	priceHistoryRangeSliderUI.lowValueProperty().removeListener(priceHistoryLowerBoundListener);
		priceHistoryRangeSliderUI.highValueProperty().removeListener(priceHistoryUpperBoundListener);
    	
		priceHistoryRangeSliderUI.setMax(0);
		priceHistoryRangeSliderUI.setMin(-priceHistoryFeed.size());
		priceHistoryRangeSliderUI.setLowValue((int) (-priceHistoryFeed.size() * 0.33));
		priceHistoryRangeSliderUI.setHighValue(0);
		
		priceHistoryChartXAxisUI.setLowerBound(priceHistoryRangeSliderUI.getLowValue() + priceHistoryFeed.size());
		priceHistoryChartXAxisUI.setUpperBound(priceHistoryRangeSliderUI.getHighValue() + priceHistoryFeed.size());
		
		priceHistoryRangeSliderUI.lowValueProperty().addListener(priceHistoryLowerBoundListener);
		priceHistoryRangeSliderUI.highValueProperty().addListener(priceHistoryUpperBoundListener);
		
		adjustPriceHistoryYAxisBounds();
		
		priceHistorySeries.getData().clear();
    	for (int i = 0; i < priceHistoryFeed.size(); i++) {
    		PriceCandle c = priceHistoryFeed.get(i);
    		priceHistorySeries.getData().add(
                  new XYChart.Data<Number, Number>(i, c.getOpen(),
                		  new CandleExtraValues(c.getClose(), c.getHigh(), c.getLow(), c.getAverage())));
        }
    	
    	statusBoxUI.setVisible(false);
    	contentBoxUI.setVisible(true);
    	optionsBoxUI.setVisible(true);
	}
    
    private void updatePriceHistoryChart() {
    	for (int i = 0; i < priceHistoryFeed.size(); i++) {
    		PriceCandle c = priceHistoryFeed.get(i);
    		priceHistorySeries.getData().get(i).setYValue(c.getOpen());
    		priceHistorySeries.getData().get(i).setExtraValue(
    				new CandleExtraValues(c.getClose(), c.getHigh(), c.getLow(), c.getAverage()));
    	}
    	adjustPriceHistoryYAxisBounds();
    }
    
    private boolean buildPriceHistoryFeed() {
    	Period period = priceHistoryPeriodsUI.getValue();
    	try {
    		market = BlockchainMarketStatus.run(item.getAsset2().getSymbol(), item.getAsset1().getSymbol());
    		if (market.getLast_error() != null) {
    			statusUI.setText(String.format("%s (%s)", "This market does not exist", market.getLast_error().getMessage()));
    			return false;
    		}
    	} catch (BTSSystemException e) {
			systemException(e);
			return false;
		}
		LocalDateTime n = LocalDateTime.ofEpochSecond(System.currentTimeMillis() / 1000, 0, ZoneOffset.UTC);
    	LocalDateTime p = n.minusDays(period.getDays());
    	LocalDateTime t = p.truncatedTo(ChronoUnit.DAYS);
    	long duration = n.toEpochSecond(ZoneOffset.UTC) - t.toEpochSecond(ZoneOffset.UTC);
    	System.out.println(Time.format(n));
    	System.out.println(Time.format(p));
    	System.out.println(Time.format(t));
    	System.out.println(duration);
    	priceHistoryFeed.clear();
    	try {
    		Double level = null;
    		{
	    		List<BlockchainMarketPriceHistory.Result> list = BlockchainMarketPriceHistory.run(
	    				item.getAsset2().getSymbol(), item.getAsset1().getSymbol(),
	    				Time.encode(t.minusDays(1L)), duration, HistoryGranularity.EACH_DAY);
	    		if (list.size() == 0) {
	    			statusUI.setText("There is no price history");
	    			return false;
	    		}
	    		level = list.get(0).getClosing_price();
    		}
    		{
    			if (!verifyRecentMarket(item)) {
    				Model.getInstance().getRecentMarkets().add(item);
    				String value = "";
    				for (AssetPair i : Model.getInstance().getRecentMarkets())
    					value += String.format("\"%s:%s\",", i.getAsset1().getSymbol(), i.getAsset2().getSymbol());
    				value = String.format("[%s]", value.substring(0, value.length() - 1));
    				WalletSetSetting.run("recent_markets", value);
    				MarketList01.getInstance().reloadData();
    				MarketList02.getInstance().reloadData();
    				MarketList03.getInstance().reloadData();
    			}
    		}
    		
			List<BlockchainMarketPriceHistory.Result> list = BlockchainMarketPriceHistory.run(
					item.getAsset2().getSymbol(), item.getAsset1().getSymbol(),
					Time.encode(t), duration, HistoryGranularity.EACH_BLOCK);
			
			LocalDateTime u = t.plusMinutes(period.getMinutes());
			List<BlockchainMarketPriceHistory.Result> temp = new ArrayList<BlockchainMarketPriceHistory.Result>();
			
			for (BlockchainMarketPriceHistory.Result result : list) {
				LocalDateTime x = Time.decode(result.getTimestamp());
				while (!x.isBefore(u)) {
					System.out.println(String.format("-----%s-----", Time.format(u)));
					PriceCandle candle = (temp.isEmpty() ? new PriceCandle(u, level) : new PriceCandle(u, temp));
					priceHistoryFeed.add(candle);
					level = candle.getClose();
					u = u.plusMinutes(period.getMinutes());
					temp.clear();
				}
				temp.add(result);
				System.out.println(String.format("[%s]: %12.8f %12.8f %12.8f %12.8f %12.8f %d",
						result.getTimestamp(),
						result.getOpening_price(),
						result.getClosing_price(),
						result.getHighest_bid(),
						result.getLowest_ask(),
						result.getRecent_average_price(),
						result.getVolume()));
			}	
			System.out.println(String.format("-----%s-----", Time.format(u)));
			priceHistoryFeed.add(temp.isEmpty() ? new PriceCandle(u, level) : new PriceCandle(u, temp));
			System.out.println();
			for (PriceCandle candle : priceHistoryFeed)
				System.out.println(candle);
    	} catch (BTSSystemException e) {
			systemException(e);
			return false;
		}
    	return true;
    }
    
    private boolean updateFeed() {
    	return false;
    }
    
    private boolean populateMarketOrders(boolean ping) {
		boolean isDifferent = false;
    	try {
    		List<MarketOrder> marketBidOrdersFeed = BlockchainMarketListBids.run(item.getAsset2().getSymbol(), item.getAsset1().getSymbol(), -1);
    		if (isDifferentMarketOrderList(marketBidOrdersFeed, this.marketBidOrdersFeed)) {
    			this.marketBidOrdersFeed.clear();
    			this.marketBidOrdersFeed.addAll(marketBidOrdersFeed);
    			isDifferent = true;
    		}
    		List<MarketOrder> marketAskOrdersFeed = BlockchainMarketListAsks.run(item.getAsset2().getSymbol(), item.getAsset1().getSymbol(), -1);
    		if (isDifferentMarketOrderList(marketAskOrdersFeed, this.marketAskOrdersFeed)) {
    			this.marketAskOrdersFeed.clear();
    			this.marketAskOrdersFeed.addAll(marketAskOrdersFeed);
    			isDifferent = true;
    		}
    		List<MarketOrder> marketShortOrdersFeed = BlockchainMarketListShorts.run(item.getAsset2().getSymbol(), -1);
    		if (isDifferentMarketOrderList(marketShortOrdersFeed, this.marketShortOrdersFeed)) {
    			this.marketShortOrdersFeed.clear();
    			this.marketShortOrdersFeed.addAll(marketShortOrdersFeed);
    			isDifferent = true;
    		}
    		List<MarketOrder> marketCoverOrdersFeed = BlockchainMarketListCovers.run(item.getAsset2().getSymbol(), -1);
    		if (isDifferentMarketOrderList(marketCoverOrdersFeed, this.marketCoverOrdersFeed)) {
    			this.marketCoverOrdersFeed.clear();
    			this.marketCoverOrdersFeed.addAll(marketCoverOrdersFeed);
    			isDifferent = true;
    		}
    	} catch (BTSSystemException e) {
			if (!ping)
				systemException(e);
			return false;
		}
    	if (!isDifferent)
    		return false;
    	
		marketBidOrders.setAll(marketBidOrdersFeed);
		if (!ping)
			Util.resetTableViewScroll(marketBidOrdersUI);
		
		marketAskOrders.setAll(marketAskOrdersFeed);
		if (!ping)
			Util.resetTableViewScroll(marketAskOrdersUI);
		
		marketShortOrders.setAll(marketShortOrdersFeed);
		if (!ping)
			Util.resetTableViewScroll(marketShortOrdersUI);
		
		marketCoverOrders.setAll(marketCoverOrdersFeed);
		if (!ping)
			Util.resetTableViewScroll(marketCoverOrdersUI);
		
		if (marketAskOrdersFeed.size() > 0)
			buyLowestAskUI.setText(String.format("%.8f",
					getRealPrice(marketAskOrdersFeed.get(0).getMarket_index().getOrder_price())));
		else
			buyLowestAskUI.setText("");
		
		if (marketBidOrdersFeed.size() > 0)
			sellHighestBidUI.setText(String.format("%.8f",
					getRealPrice(marketBidOrdersFeed.get(0).getMarket_index().getOrder_price())));
		else
			sellHighestBidUI.setText("");
		return true;
    }
    
    private void populateMarketOrdersHistory(boolean ping) {
		boolean isDifferent = false;
		try {
			List<BlockchainMarketOrderHistory.Result> marketOrdersHistoryFeed = BlockchainMarketOrderHistory.run(item.getAsset2().getSymbol(), item.getAsset1().getSymbol(), 0, 100, "");
			if (isDifferentMarketOrderHistoryList(marketOrdersHistoryFeed, this.marketOrdersHistoryFeed)) {
				this.marketOrdersHistoryFeed.clear();
				this.marketOrdersHistoryFeed.addAll(marketOrdersHistoryFeed);
				isDifferent = true;
			}
		} catch (BTSSystemException e) {
			if (!ping)
				systemException(e);
		}
		if (!isDifferent)
			return;
		marketOrdersHistory.setAll(marketOrdersHistoryFeed);
		if (!ping)
			Util.resetTableViewScroll(marketOrdersHistoryUI);
    }
    
    private void populateMarketDepthChart(boolean ping) {
		if (marketShortOrdersFeed.size() > 0 || marketBidOrdersFeed.size() > 0) {
			double shortsTotal = 0;
			for (MarketOrder order : marketShortOrdersFeed) {
				double delta = getRealValue(order.getMarket_index().getOrder_price().getBase_asset_id(),
						order.getState().getBalance());
				shortsTotal += delta;
			}
			double total = 0;
			double subtotal = 0;
			Double price = null;
			Double bestPrice = getRealPrice(marketBidOrdersFeed.get(0).getMarket_index().getOrder_price());
			List<AreaChart.Data<Number, Number>> list = new ArrayList<AreaChart.Data<Number, Number>>();
			if (marketBidOrdersFeed.size() > 0) {
				for (MarketOrder order : marketBidOrdersFeed) {
					double p = getRealPrice(order.getMarket_index().getOrder_price());
					if (price == null)
						price = p;
					else if (p < price) {
						total += subtotal;
						list.add(new AreaChart.Data<Number, Number>(price, total));
						price = p;
						subtotal = 0;
					}
					if (price < bestPrice * marketDepthSliderUI.getValue()) {
						break;
					}
					if (shortsTotal > 0 && price <= getFeedPrice()) {
						if (price < getFeedPrice()) {
							total += shortsTotal;
							list.add(new AreaChart.Data<Number, Number>(getFeedPrice(), total));
							shortsTotal = 0;
						} else {
							subtotal += shortsTotal;
							shortsTotal = 0;
						}
					}
					double delta = getRealValue(order.getMarket_index().getOrder_price().getBase_asset_id(),
							(long) (order.getState().getBalance() / order.getMarket_index().getOrder_price().getRatio()));
					subtotal += delta;
				}
				if (subtotal > 0) {
					total += subtotal;
					list.add(new AreaChart.Data<Number, Number>(price, total));
					price = null;
					subtotal = 0;
				}
				Collections.reverse(list);
			}
			if (shortsTotal > 0 && getFeedPrice() < bestPrice * marketDepthSliderUI.getValue())
				list.add(new AreaChart.Data<Number, Number>(getFeedPrice(), shortsTotal));
			marketDepthChartXAxisUI.setLowerBound((1 - AXIS_RANGE_FACTOR) * bestPrice * marketDepthSliderUI.getValue());
			marketDepthBidSeries.getData().setAll(list);
		}
		if (marketAskOrdersFeed.size() > 0) {
			double total = 0;
			double subtotal = 0;
			Double price = null;
			Double bestPrice = getRealPrice(marketAskOrdersFeed.get(0).getMarket_index().getOrder_price());
			List<AreaChart.Data<Number, Number>> list = new ArrayList<AreaChart.Data<Number, Number>>();
			for (MarketOrder order : marketAskOrdersFeed) {
				double p = getRealPrice(order.getMarket_index().getOrder_price());
				if (price == null)
					price = p;
				else if (p > price) {
					total += subtotal;
					list.add(new AreaChart.Data<Number, Number>(price, total));
					price = p;
					subtotal = 0;
				}
				if (price > bestPrice / marketDepthSliderUI.getValue())
					break;
				double delta = getRealValue(order.getMarket_index().getOrder_price().getBase_asset_id(),
						order.getState().getBalance());
				subtotal += delta;
			}
			if (subtotal > 0) {
				total += subtotal;
				list.add(new AreaChart.Data<Number, Number>(price, total));
				price = null;
				subtotal = 0;
			}
			marketDepthChartXAxisUI.setUpperBound((1 + AXIS_RANGE_FACTOR) * bestPrice / marketDepthSliderUI.getValue());
			marketDepthAskSeries.getData().setAll(list);
		}
		double span = marketDepthChartXAxisUI.getUpperBound() - marketDepthChartXAxisUI.getLowerBound();
		marketDepthChartXAxisUI.setTickUnit(span / 10);
    }
    
    private void populateMyAvailableBalance(boolean ping) {
		buyAvailableBalanceUI.setText(getAmount(item.getAsset2(),
				getAvailableAmount(accountsUI.getValue().getName(), item.getAsset2()) - transactionFeeAsset2));
		sellAvailableBalanceUI.setText(getAmount(item.getAsset1(),
				getAvailableAmount(accountsUI.getValue().getName(), item.getAsset1())));
		shortAvailableBalanceUI.setText(sellAvailableBalanceUI.getText());
    }
    
    private void populateMyOrders(boolean ping) {
    	boolean isDifferent = false;
		try {
			List<WalletMarketOrderList.Result> myOrdersFeed  = WalletMarketOrderList.run(item.getAsset2().getSymbol(), item.getAsset1().getSymbol(), -1, accountsUI.getValue().getName());
			if (isDifferentMyOrderList(myOrdersFeed, this.myOrdersFeed)) {
				this.myOrdersFeed.clear();
				this.myOrdersFeed.addAll(myOrdersFeed);
				isDifferent = true;
			}
		} catch (BTSSystemException e) {
			if (!ping)
				systemException(e);
		}
		if (!isDifferent)
			return;
		myBidOrders.clear();
		myAskOrders.clear();
		myShortOrders.clear();
		myCoverOrders.clear();
		for (WalletMarketOrderList.Result result : myOrdersFeed) {
			if (result.getOrder().getType().equals("bid_order"))
				myBidOrders.add(result);
			else if (result.getOrder().getType().equals("ask_order"))
				myAskOrders.add(result);
			else if (result.getOrder().getType().equals("short_order"))
				myShortOrders.add(result);
			else if (result.getOrder().getType().equals("cover_order"))
				myCoverOrders.add(result);
		}
    	if (!ping) {
    		Util.resetTableViewScroll(myBidOrdersUI);
    		Util.resetTableViewScroll(myAskOrdersUI);
    		Util.resetTableViewScroll(myShortOrdersUI);
    		Util.resetTableViewScroll(myCoverOrdersUI);
    	}
    }
		
	private void populateMyOrdersHistory(boolean ping) {
		boolean isDifferent = false;
		try {
			List<WalletAccountTransactionHistory.Result> myOrdersHistoryFeed = new ArrayList<WalletAccountTransactionHistory.Result>();
			for (WalletAccountTransactionHistory.Result result : WalletAccountTransactionHistory.run(
					accountsUI.getValue().getName(), item.getAsset1().getSymbol(), 0)) {
				if (result.isIs_confirmed() && result.isIs_market() && !result.isIs_market_cancel() && !result.isIs_virtual())
					myOrdersHistoryFeed.add(result);
			}
			if (isDifferentMyOrderHistoryList(myOrdersHistoryFeed, this.myOrdersHistoryFeed)) {
				this.myOrdersHistoryFeed.clear();
				this.myOrdersHistoryFeed.addAll(myOrdersHistoryFeed);
				isDifferent = true;
			}
		} catch (BTSSystemException e) {
			if (!ping)
				systemException(e);
		}
		if (!isDifferent)
			return;
		myOrdersHistory.setAll(myOrdersHistoryFeed);
		if (!ping)
			Util.resetTableViewScroll(myOrdersHistoryUI);
    }
    
    private void populateBuyTotal() {
    	if (!Util.isValidString(buyQuantityUI.getText()) || !Util.isValidString(buyPriceUI.getText())) {
    		buyTotalUI.setText("");
    		return;
    	}
    	Double quantity = null;
    	Double price =  null;
    	try {
    		quantity = new Double(buyQuantityUI.getText());
    		price = new Double(buyPriceUI.getText());
    	} catch (Exception e) {
    		buyTotalUI.setText("");
	    	return;
		}
    	buyTotalUI.setText(getAmount(item.getAsset2(), quantity * price));
    }
    
    private void populateSellTotal() {
    	if (!Util.isValidString(sellQuantityUI.getText()) || !Util.isValidString(sellPriceUI.getText())) {
    		sellTotalUI.setText("");
    		return;
    	}
    	Double quantity = null;
    	Double price =  null;
    	try {
    		quantity = new Double(sellQuantityUI.getText());
    		price = new Double(sellPriceUI.getText());
	    } catch (Exception e) {
	    	sellTotalUI.setText("");
	    	return;
		}
    	sellTotalUI.setText(getAmount(item.getAsset2(), quantity * price));
    }
    
    private void adjustPriceHistoryYAxisBounds() {
    	double minValue = Double.MAX_VALUE;
        double maxValue = -Double.MAX_VALUE;
        int beg = (int) priceHistoryChartXAxisUI.getLowerBound();
        int end = (int) priceHistoryChartXAxisUI.getUpperBound();
    	for (int i = beg; i < end; i++) {
    		PriceCandle c = priceHistoryFeed.get(i);
    		minValue = Math.min(minValue, c.getLow());
    		maxValue = Math.max(maxValue, c.getHigh());
        }
    	priceHistoryChartYAxisUI.setLowerBound((1 - AXIS_RANGE_FACTOR) * minValue);
    	priceHistoryChartYAxisUI.setUpperBound((1 + AXIS_RANGE_FACTOR) * maxValue);
    }
    
    @FXML
    private void onSubmitBid() {
    	Double quantity = null;
    	Double price = null;
    	try {
    		quantity = new Double(buyQuantityUI.getText());
    		price = new Double(buyPriceUI.getText());
    	} catch (Exception e) {
    		userException("Error in quantity or price");
	    	return;
		}
    	if (quantity <= 0 || price <= 0) {
    		userException("Negative values not allowed");
	    	return;
		}
    	double total = quantity * price;
    	if (total > getRealValue(item.getAsset2(), getAvailableAmount(accountsUI.getValue().getName(), item.getAsset2()) - transactionFeeAsset2)) {
    		userException("Not enough funds");
	    	return;
		}
    	
    	String details = "";
		details += String.format("%8s: %s\n", "Type", "bid_order");
		details += String.format("%8s: %s %s/%s\n", "Price", String.format("%.8f", price), item.getAsset2().getSymbol(), item.getAsset1().getSymbol());
		details += String.format("%8s: %s %s\n", "Quantity", item.getAsset1().getSymbol(), format(quantity, item.getAsset1().getPrecision()));
		details += String.format("%8s: %s %s\n", "Total", item.getAsset2().getSymbol(), format(total, item.getAsset2().getPrecision()));
		details += String.format("%8s: %s %s\n", "Fee", item.getAsset2().getSymbol(), getAmount(item.getAsset2(), transactionFeeAsset2));
		
		ConfirmExecution confirmExecution = new ConfirmExecution();
		confirmExecution.setTitle("Submit Order");
		confirmExecution.setHeader("Are you sure you want to submit this order?");
		confirmExecution.setDetails(details);
		confirmExecution.setConfirm("Yes");
		confirmExecution.setCancel("Cancel");
		confirmExecution.setCallback(new ConfirmExecution.Callback() {
			@Override
			public void onConfirm() {
				try {
		    		WalletMarketSubmitBid.run(accountsUI.getValue().getName(), buyQuantityUI.getText(),
		    				item.getAsset1().getSymbol(), buyPriceUI.getText(), item.getAsset2().getSymbol(), false);
		    	} catch (BTSUserException e) {
					userException(e);
				} catch (BTSSystemException e) {
					systemException(e);
				}
			}
			@Override
			public void onCancel() {
			}
		});
		confirmExecution.show(pane);
    }
    
    @FXML
    private void onSubmitAsk() {
    	Double quantity = null;
    	Double price = null;
    	try {
    		quantity = new Double(sellQuantityUI.getText());
    		price = new Double(sellPriceUI.getText());
    	} catch (Exception e) {
    		userException("Error in quantity or price");
	    	return;
		}
    	if (quantity <= 0 || price <= 0) {
    		userException("Negative values not allowed");
	    	return;
		}
    	double total = quantity * price;
    	if (quantity > getRealValue(item.getAsset1(), getAvailableAmount(accountsUI.getValue().getName(), item.getAsset1()))) {
    		userException("You are trying to sell more than you have");
	    	return;
		}
    	
    	String details = "";
		details += String.format("%8s: %s\n", "Type", "ask_order");
		details += String.format("%8s: %s %s/%s\n", "Price", String.format("%.8f", price), item.getAsset2().getSymbol(), item.getAsset1().getSymbol());
		details += String.format("%8s: %s %s\n", "Quantity", item.getAsset1().getSymbol(), format(quantity, item.getAsset1().getPrecision()));
		details += String.format("%8s: %s %s\n", "Total", item.getAsset2().getSymbol(), format(total, item.getAsset2().getPrecision()));
		details += String.format("%8s: %s %s\n", "Fee", item.getAsset2().getSymbol(), getAmount(item.getAsset2(), transactionFeeAsset2));
		
		ConfirmExecution confirmExecution = new ConfirmExecution();
		confirmExecution.setTitle("Submit Order");
		confirmExecution.setHeader("Are you sure you want to submit this order?");
		confirmExecution.setDetails(details);
		confirmExecution.setConfirm("Yes");
		confirmExecution.setCancel("Cancel");
		confirmExecution.setCallback(new ConfirmExecution.Callback() {
			@Override
			public void onConfirm() {
				try {
					WalletMarketSubmitAsk.run(accountsUI.getValue().getName(), sellQuantityUI.getText(),
		    				item.getAsset1().getSymbol(), sellPriceUI.getText(), item.getAsset2().getSymbol(), false);
		    	} catch (BTSUserException e) {
					userException(e);
				} catch (BTSSystemException e) {
					systemException(e);
				}
			}
			@Override
			public void onCancel() {
			}
		});
		confirmExecution.show(pane);
    }
    
    @FXML
    private void onSubmitShort() {
    	
    }
    
    private void removeOrder(WalletMarketOrderList.Result order, ReadBoolean.Callback callback) {
		String details = "";
		if (order.getOrder().getType().equals("bid_order")) {
			details += String.format("%8s: %s %s/%s\n", "Price", String.format("%.8f",
					getRealPrice(order.getOrder().getMarket_index().getOrder_price())), item.getAsset2().getSymbol(), item.getAsset1().getSymbol());
			details += String.format("%8s: %s %s\n", "Quantity", item.getAsset1().getSymbol(), getAmount(order.getOrder().getMarket_index().getOrder_price().getBase_asset_id(),
					(long) (order.getOrder().getState().getBalance() / order.getOrder().getMarket_index().getOrder_price().getRatio())));
		} else if (order.getOrder().getType().equals("ask_order")) {
			details += String.format("%8s: %s %s/%s\n", "Price", String.format("%.8f",
					getRealPrice(order.getOrder().getMarket_index().getOrder_price())), item.getAsset2().getSymbol(), item.getAsset1().getSymbol());
			details += String.format("%8s: %s %s\n", "Quantity", item.getAsset1().getSymbol(), getAmount(order.getOrder().getMarket_index().getOrder_price().getBase_asset_id(),
					order.getOrder().getState().getBalance()));
		} else if (order.getOrder().getType().equals("short_order")) {
			details += String.format("%8s: %s %s\n", "Collateral", item.getAsset1().getSymbol(), getAmount(item.getAsset1(), order.getOrder().getState().getBalance()));     	
			details += String.format("%8s: %.8f %s\n", "Interest", order.getOrder().getMarket_index().getOrder_price().getRatio() * 100, "%");
			details += String.format("%8s: %s %s\n", "Quantity", item.getAsset2().getSymbol(), getAmount(item.getAsset2(),
					(long) (order.getOrder().getState().getBalance() * order.getOrder().getMarket_index().getOrder_price().getRatio() * 0.5)));
		}
		
		ConfirmExecution confirmExecution = new ConfirmExecution();
		confirmExecution.setTitle("Remove Order");
		confirmExecution.setHeader("Are you sure you want to remove this order?");
		confirmExecution.setDetails(details);
		confirmExecution.setConfirm("Yes");
		confirmExecution.setCancel("Cancel");
		confirmExecution.setCallback(new ConfirmExecution.Callback() {
			@Override
			public void onConfirm() {
				try {
					if (callback != null)
						callback.onConfirm();
					WalletMarketCancelOrder.run(order.getId());
				} catch (BTSSystemException e) {
					if (callback != null)
						callback.onCancel();
					systemException(e);
				}
			}
			@Override
			public void onCancel() {
				if (callback != null)
					callback.onCancel();
			}
		});
		confirmExecution.show(pane);
	}
    
    private void coverShort(WalletMarketOrderList.Result order, CoverShort.Callback callback) {
    	CoverShort coverShort = new CoverShort();
    	coverShort.setOrder(order);
    	coverShort.setAsset(item.getAsset2());
    	coverShort.setFromAccount(accountsUI.getValue());
    	coverShort.setCallback(callback);
    	coverShort.show(pane);
	}
    
    private static boolean isDifferentMarketOrderList(List<MarketOrder> list1, List<MarketOrder> list2) {
    	if (list1.size() != list2.size())
    		return true;
    	for (int i = 0; i < list1.size(); i++) {
    		if (list1.get(i).getState().getBalance() != list2.get(i).getState().getBalance())
    			return true;
    	}
    	return false;
    }
    
    private static boolean isDifferentMarketOrderHistoryList(List<BlockchainMarketOrderHistory.Result> list1, List<BlockchainMarketOrderHistory.Result> list2) {
    	if (list1.size() != list2.size())
    		return true;
    	for (int i = 0; i < list1.size(); i++) {
    		if (!list1.get(i).getTimestamp().equals(list2.get(i).getTimestamp()))
    			return true;
    	}
    	return false;
    }
    
    private static boolean isDifferentMyOrderList(List<WalletMarketOrderList.Result> list1, List<WalletMarketOrderList.Result> list2) {
    	if (list1.size() != list2.size())
    		return true;
    	for (int i = 0; i < list1.size(); i++) {
    		if (list1.get(i).getOrder().getState().getBalance() != list2.get(i).getOrder().getState().getBalance())
    			return true;
    	}
    	return false;
    }
    
    private static boolean isDifferentMyOrderHistoryList(List<WalletAccountTransactionHistory.Result> list1, List<WalletAccountTransactionHistory.Result> list2) {
    	if (list1.size() != list2.size())
    		return true;
    	for (int i = 0; i < list1.size(); i++) {
    		if (!list1.get(i).getTrx_id().equals(list2.get(i).getTrx_id()))
    			return true;
    	}
    	return false;
    }
    
    private static boolean verifyRecentMarket(AssetPair assetPair) {
		for (AssetPair item : Model.getInstance().getRecentMarkets())
			if (item.getAsset1().getId() == assetPair.getAsset1().getId()
					&& item.getAsset2().getId() == assetPair.getAsset2().getId())
				return true;
		return false;
	}
    
	private static String getOrderTypeLabel(String bidType, String askType) {
		if (bidType.equals("bid_order"))
			return "bid";
		if (bidType.equals("ask_order"))
			return "ask";
		if (bidType.equals("short_order"))
			return "short";
		if (bidType.equals("cover_order"))
			return "cover";
		return null;
	}
    
    private static class AccountListCell extends ListCell<Account> {
    	@Override
        protected void updateItem(Account item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null)
            	setText(item.getName());
        }
    }
    
	public static class PriceCandle {
		public PriceCandle(LocalDateTime time, double open, double close, double high, double low, long volume) {
			this.time = time;
			this.open = open;
			this.close = close;
			this.high = high;
			this.low = low;
			this.volume = volume;
		}
		public PriceCandle(LocalDateTime time, double level) {
			this.time = time;
			this.open = level;
			this.close = level;
			this.high = level;
			this.low = level;
			this.volume = 0;
		}
		public PriceCandle(LocalDateTime time, List<BlockchainMarketPriceHistory.Result> list) {
			double minValue = Double.MAX_VALUE;
	        double maxValue = -Double.MAX_VALUE;
	        long volume = 0;
	        for (BlockchainMarketPriceHistory.Result result : list) {
	    		minValue = Math.min(minValue, Math.min(result.getOpening_price(), result.getClosing_price()));
	    		maxValue = Math.max(maxValue, Math.min(result.getOpening_price(), result.getClosing_price()));
	    		volume += result.getVolume();
	        }
	        this.time = time;
	        this.open = list.get(0).getOpening_price();
	        this.close = list.get(list.size() - 1).getClosing_price();
	        this.high = maxValue;
			this.low = minValue;
			this.volume = volume;
		}
		
		private LocalDateTime time;
		private double open;
		private double close;
		private double high;
		private double low;
		private long volume;
		
		public LocalDateTime getTime() {
			return time;
		}
		public void setTime(LocalDateTime time) {
			this.time = time;
		}
		public double getOpen() {
			return open;
		}
		public void setOpen(double open) {
			this.open = open;
		}
		public double getClose() {
			return close;
		}
		public void setClose(double close) {
			this.close = close;
		}
		public double getHigh() {
			return high;
		}
		public void setHigh(double high) {
			this.high = high;
		}
		public double getLow() {
			return low;
		}
		public void setLow(double low) {
			this.low = low;
		}
		public long getVolume() {
			return volume;
		}
		public void setVolume(long volume) {
			this.volume = volume;
		}
		
		public double getAverage() {
			return (high + low) * 0.5;
		}
		
		@Override
		public String toString() {
			return String.format("%s: [%8.6f %8.6f %8.6f %8.6f] %d", Time.format(time), open, close, high, low, volume);
		}
	}
    
}
