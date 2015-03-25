package sx.neura.bts.gui.view.pages.bts.impl;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.util.StringConverter;

import org.controlsfx.control.RangeSlider;

import sx.neura.bts.gui.Model;
import sx.neura.bts.gui.dto.CumulativeMarketOrder;
import sx.neura.bts.gui.dto.MyOrder;
import sx.neura.bts.gui.dto.Transaction;
import sx.neura.bts.gui.view.Page;
import sx.neura.bts.gui.view.components.LayerPane;
import sx.neura.bts.gui.view.components.MenuPane;
import sx.neura.bts.gui.view.components.Pagination;
import sx.neura.bts.gui.view.components.candle.CandleChart;
import sx.neura.bts.gui.view.components.candle.CandleExtraValues;
import sx.neura.bts.gui.view.components.depth.MarketDepthChart;
import sx.neura.bts.gui.view.components.display.DisplayCheckList;
import sx.neura.bts.gui.view.components.display.DisplayToggleList;
import sx.neura.bts.gui.view.pages.bts.PageDetails_Bts;
import sx.neura.bts.gui.view.pages.bts.comp.tile.MarketOrderBidAskTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.MarketOrderCoverTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.MarketOrderHistoryTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.MarketOrderShortTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.MyOrderBidAskTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.MyOrderCoverTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.MyOrderShortTile;
import sx.neura.bts.gui.view.pages.bts.comp.tile.TransactionTile;
import sx.neura.bts.json.api.blockchain.BlockchainMarketListAsks;
import sx.neura.bts.json.api.blockchain.BlockchainMarketListBids;
import sx.neura.bts.json.api.blockchain.BlockchainMarketListCovers;
import sx.neura.bts.json.api.blockchain.BlockchainMarketListShorts;
import sx.neura.bts.json.api.blockchain.BlockchainMarketOrderHistory;
import sx.neura.bts.json.api.blockchain.BlockchainMarketPriceHistory;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory;
import sx.neura.bts.json.api.wallet.WalletMarketOrderList;
import sx.neura.bts.json.api.wallet.WalletSetSetting;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.Market;
import sx.neura.bts.json.dto.MarketOrder;
import sx.neura.bts.json.enumerations.HistoryGranularity;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.util.Time;
import sx.neura.bts.util.Util;

public class Details_Market extends PageDetails_Bts<Market> {
	
	private static final int BLOCKCHAIN_ORDER_LIMIT = 999;
	private static final int BLOCKCHAIN_ORDER_HISTORY_LIMIT = 100;
	
	private static final double AXIS_RANGE_FACTOR = 0.05;
	private static final double HORIZONTAL_OFFSET_FACTOR = 0.10;
	
	@FXML
	private Node groundUI;
	
	@FXML
	private Node progressUI;
	@FXML
	private Node progressTrackUI;
	@FXML
	private Node progressBarUI;
	@FXML
	private Label progressInfoUI;
	
	@FXML
	private Label statusUI;
	
	@FXML
	private Label feedPriceUI;
	
	@FXML
	private MenuPane panoramaMenuUI;
	@FXML
	private LayerPane panoramaUI;
	
	@FXML
	private MenuPane panoramaMenu01UI;
	@FXML
	private LayerPane panorama01UI;
	
	@FXML
	private MenuPane panoramaMenu02UI;
	@FXML
	private LayerPane panorama02UI;
	
	@FXML
	private MenuPane panoramaMenu03UI;
	@FXML
	private LayerPane panorama03UI;
	
	@FXML
	private ToggleGroup panoramaToggleGroupUI;
	
	@FXML
	private Pagination pagination0201UI;
	@FXML
	private Label pagination0201StatusUI;
	@FXML
	private Node pagination0201FwdUI;
	@FXML
	private Node pagination0201BckUI;
	
	@FXML
	private Pagination pagination0202UI;
	@FXML
	private Label pagination0202StatusUI;
	@FXML
	private Node pagination0202FwdUI;
	@FXML
	private Node pagination0202BckUI;
	
	@FXML
	private Pagination pagination0203UI;
	@FXML
	private Label pagination0203StatusUI;
	@FXML
	private Node pagination0203FwdUI;
	@FXML
	private Node pagination0203BckUI;
	
	@FXML
	private Pagination pagination0204UI;
	@FXML
	private Label pagination0204StatusUI;
	@FXML
	private Node pagination0204FwdUI;
	@FXML
	private Node pagination0204BckUI;
	
	@FXML
	private Pagination pagination0205UI;
	@FXML
	private Label pagination0205StatusUI;
	@FXML
	private Node pagination0205FwdUI;
	@FXML
	private Node pagination0205BckUI;
	
	
	@FXML
	private Pagination pagination0301UI;
	@FXML
	private Label pagination0301StatusUI;
	@FXML
	private Node pagination0301FwdUI;
	@FXML
	private Node pagination0301BckUI;
	
	@FXML
	private Pagination pagination0302UI;
	@FXML
	private Label pagination0302StatusUI;
	@FXML
	private Node pagination0302FwdUI;
	@FXML
	private Node pagination0302BckUI;
	
	@FXML
	private Pagination pagination0303UI;
	@FXML
	private Label pagination0303StatusUI;
	@FXML
	private Node pagination0303FwdUI;
	@FXML
	private Node pagination0303BckUI;
	
	@FXML
	private Pagination pagination0304UI;
	@FXML
	private Label pagination0304StatusUI;
	@FXML
	private Node pagination0304FwdUI;
	@FXML
	private Node pagination0304BckUI;
	
	@FXML
	private Pagination pagination0305UI;
	@FXML
	private Label pagination0305StatusUI;
	@FXML
	private Node pagination0305FwdUI;
	@FXML
	private Node pagination0305BckUI;
	
	
	@FXML
	private CandleChart priceHistoryChartUI;
	
	@FXML
    private NumberAxis priceHistoryChartXAxisUI;
	@FXML
    private NumberAxis priceHistoryChartYAxisUI;
	
	@FXML
    private RangeSlider priceHistoryRangeSliderUI;
	@FXML
    private DisplayToggleList<PriceHistoryPeriod> priceHistoryPeriodsUI;
	
	
	@FXML
	private MarketDepthChart marketDepthChartUI;
	
	@FXML
    private NumberAxis marketDepthChartXAxisUI;
	@FXML
    private NumberAxis marketDepthChartYAxisUI;
	
	@FXML
    private DisplayCheckList<MarketDepthComponent> marketDepthComponentsUI;
	@FXML
    private Slider marketDepthSliderUI;
	
	
	private List<PriceCandle> priceHistoryFeed;
    private List<MarketOrder> marketBidOrdersFeed;
	private List<MarketOrder> marketAskOrdersFeed;
	private List<MarketOrder> marketShortOrdersFeed;
	private List<MarketOrder> marketCoverOrdersFeed;
	private List<BlockchainMarketOrderHistory.Result> marketOrdersHistoryFeed;
	
	private List<CumulativeMarketOrder> cumulativeMarketBidOrdersFeed;
	private List<CumulativeMarketOrder> cumulativeMarketAskOrdersFeed;
	private List<CumulativeMarketOrder> cumulativeMarketShortOrdersFeed;
	private List<CumulativeMarketOrder> cumulativeMarketCoverOrdersFeed;
    
	private XYChart.Series<Number, Number> priceHistorySeries;
	private AreaChart.Series<Number, Number> marketDepthBidSeries;
	private AreaChart.Series<Number, Number> marketDepthShortSeries;
	private AreaChart.Series<Number, Number> marketDepthAskSeries;
	
	private ChangeListener<Number> priceHistoryLowerBoundListener;
	private ChangeListener<Number> priceHistoryUpperBoundListener;
	
	private Comparator<MarketOrder> shortMarketOrderComperator;
	private Comparator<MarketOrder> coverMarketOrderComperator;
	
	private List<WalletMarketOrderList.Result> myOrdersFeed;
	private List<MyOrder> myBidOrdersFeed;
	private List<MyOrder> myAskOrdersFeed;
	private List<MyOrder> myShortOrdersFeed;
	private List<MyOrder> myCoverOrdersFeed;
	private List<Transaction> myOrdersHistoryFeed;
	
	private Asset assetBase;
	private Asset assetQuote;
	
	private boolean marketDepthUpdateNeeded;
	
	
	public Details_Market(Market item) {
		super(item);
	}
	
	private Details_Market getInstance() {
		return this;
	}
	
	@Override
	public String getTitle() {
		return Model.getInstance().getMarketLabel(item);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		
		leftWingUI.disableProperty().bind(progressUI.visibleProperty());
		
		Util.manageToggleGroup(panoramaToggleGroupUI);
		
		assetBase = Model.getInstance().getAssetById(item.getBase_id());
		assetQuote = Model.getInstance().getAssetById(item.getQuote_id());
		
		if (!verifyRecentMarket(item)) {
			Model.getInstance().getRecentMarkets().add(item);
			String value = "";
			for (Market m : Model.getInstance().getRecentMarkets())
				value += String.format("\"%s:%s\",", 
						Model.getInstance().getAssetById(m.getBase_id()).getSymbol(),
						Model.getInstance().getAssetById(m.getQuote_id()).getSymbol());
			value = String.format("[%s]", value.substring(0, value.length() - 1));
			try {
				WalletSetSetting.run("recent_markets", value);
				Model.getInstance().redoRecentMarkets();
			} catch (BTSSystemException e) {
				systemException(e);
			}
			Page_0302_Markets.getInstance().reloadData();
		}
		
		priceHistoryLowerBoundListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			priceHistoryChartXAxisUI.setLowerBound(priceHistoryRangeSliderUI.getLowValue() + priceHistoryFeed.size());
			adjustPriceHistoryYAxisBounds();
		};
		priceHistoryUpperBoundListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
			double upperBound = priceHistoryRangeSliderUI.getHighValue() + priceHistoryFeed.size();
			upperBound += (priceHistoryRangeSliderUI.getHighValue() - priceHistoryRangeSliderUI.getLowValue()) * HORIZONTAL_OFFSET_FACTOR;
			priceHistoryChartXAxisUI.setUpperBound(upperBound);
			adjustPriceHistoryYAxisBounds();
		};
		
		shortMarketOrderComperator = (MarketOrder o1, MarketOrder o2) -> {
			Double price1 = Model.getInstance().getShortLimitRealPrice(o1, item.getCurrent_feed_price());
			Double price2 = Model.getInstance().getShortLimitRealPrice(o2, item.getCurrent_feed_price());
			int compare = price2.compareTo(price1);
			if (compare != 0)
				return compare;
			Double interest1 = o1.getMarket_index().getOrder_price().getRatio();
			Double interest2 = o1.getMarket_index().getOrder_price().getRatio();
			return interest2.compareTo(interest1);
		};
		
		coverMarketOrderComperator = (MarketOrder o1, MarketOrder o2) -> {
			Double price1 = o1.getMarket_index().getOrder_price().getRatio();
			Double price2 = o2.getMarket_index().getOrder_price().getRatio();
			return price2.compareTo(price1);
		};
		
		marketDepthSliderUI.setMin(0.4);
		marketDepthSliderUI.setMax(0.8);
		marketDepthSliderUI.setValue(0.5);
		marketDepthSliderUI.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				populateMarketDepthChart();
			}
		});
		
		priceHistoryPeriodsUI.setItems(Arrays.asList(PriceHistoryPeriod.values()), (PriceHistoryPeriod i) -> {
			return i.value;
		});
		priceHistoryPeriodsUI.selectItem(PriceHistoryPeriod.H6);
		priceHistoryPeriodsUI.setCallback((PriceHistoryPeriod item) -> {
			if (buildPriceHistoryFeed())
				populatePriceHistoryChart();
		});
		
		marketDepthComponentsUI.setItems(Arrays.asList(MarketDepthComponent.values()), (MarketDepthComponent i) -> {
			return i.value;
		});
		marketDepthComponentsUI.selectItem(MarketDepthComponent.Ask, true);
		marketDepthComponentsUI.selectItem(MarketDepthComponent.Bid, true);
		marketDepthComponentsUI.selectItem(MarketDepthComponent.Short, true);
		marketDepthComponentsUI.setCallback((MarketDepthComponent item) -> {
			populateMarketDepthChart();
		});
		marketDepthComponentsUI.getCheck(0).getToggleButtonUI().getStyleClass().add("sv-check1");
		marketDepthComponentsUI.getCheck(1).getToggleButtonUI().getStyleClass().add("sv-check2");
		marketDepthComponentsUI.getCheck(2).getToggleButtonUI().getStyleClass().add("sv-check3");
		
		{
			priceHistoryChartXAxisUI.setTickUnit(10);
			priceHistoryChartXAxisUI.setMinorTickCount(0);
			
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
    		priceHistorySeries = new XYChart.Series<Number, Number>();
        	ObservableList<XYChart.Series<Number, Number>> data = FXCollections.observableArrayList();
	        data.add(priceHistorySeries);
	        priceHistoryChartUI.setData(data);
        }
        {
        	marketDepthChartXAxisUI.setTickLabelFormatter(new StringConverter<Number>() {
				@Override
				public String toString(Number n) {
					return String.format("%.4f", n.doubleValue());
				}
				@Override
				public Number fromString(String s) {
					return null;
				}
			});
		}
        {
        	marketDepthBidSeries = new AreaChart.Series<Number, Number>();
        	marketDepthShortSeries = new AreaChart.Series<Number, Number>();
        	marketDepthAskSeries = new AreaChart.Series<Number, Number>();
        	ObservableList<AreaChart.Series<Number, Number>> data = FXCollections.observableArrayList();
        	data.add(marketDepthBidSeries);
        	data.add(marketDepthShortSeries);
        	data.add(marketDepthAskSeries);
        	marketDepthChartUI.setData(data);
        	marketDepthChartYAxisUI.setLowerBound(0);
        }
        
		{
			priceHistoryFeed = new ArrayList<PriceCandle>();
			
			marketBidOrdersFeed = new ArrayList<MarketOrder>();
        	marketAskOrdersFeed = new ArrayList<MarketOrder>();
        	marketShortOrdersFeed = new ArrayList<MarketOrder>();
        	marketCoverOrdersFeed = new ArrayList<MarketOrder>();
        	
        	cumulativeMarketBidOrdersFeed = new ArrayList<CumulativeMarketOrder>();
        	cumulativeMarketAskOrdersFeed = new ArrayList<CumulativeMarketOrder>();
        	cumulativeMarketShortOrdersFeed = new ArrayList<CumulativeMarketOrder>();
        	cumulativeMarketCoverOrdersFeed = new ArrayList<CumulativeMarketOrder>();
        	
        	marketOrdersHistoryFeed = new ArrayList<BlockchainMarketOrderHistory.Result>();
        	
        	myOrdersFeed = new ArrayList<WalletMarketOrderList.Result>();
        	myBidOrdersFeed = new ArrayList<MyOrder>();
        	myAskOrdersFeed = new ArrayList<MyOrder>();
        	myShortOrdersFeed = new ArrayList<MyOrder>();
        	myCoverOrdersFeed = new ArrayList<MyOrder>();
        	
        	myOrdersHistoryFeed = new ArrayList<Transaction>();
		}
		
		pagination0201UI.setHost(new Pagination.Host() {
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
				return pagination0201StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination0201FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination0201BckUI;
			}
			@Override
			public int getListSize() {
				return cumulativeMarketBidOrdersFeed.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new MarketOrderBidAskTile(assetBase, assetQuote), cumulativeMarketBidOrdersFeed, pagination0201UI, index);
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
		pagination0202UI.setHost(new Pagination.Host() {
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
				return pagination0202StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination0202FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination0202BckUI;
			}
			@Override
			public int getListSize() {
				return cumulativeMarketAskOrdersFeed.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new MarketOrderBidAskTile(assetBase, assetQuote), cumulativeMarketAskOrdersFeed, pagination0202UI, index);
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
		pagination0203UI.setHost(new Pagination.Host() {
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
				return pagination0203StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination0203FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination0203BckUI;
			}
			@Override
			public int getListSize() {
				return cumulativeMarketShortOrdersFeed.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new MarketOrderShortTile(assetBase, assetQuote, item.getCurrent_feed_price()),
		        		cumulativeMarketShortOrdersFeed, pagination0203UI, index);
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
		pagination0204UI.setHost(new Pagination.Host() {
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
				return pagination0204StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination0204FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination0204BckUI;
			}
			@Override
			public int getListSize() {
				return cumulativeMarketCoverOrdersFeed.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new MarketOrderCoverTile(assetBase, assetQuote), cumulativeMarketCoverOrdersFeed, pagination0204UI, index);
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
		pagination0205UI.setHost(new Pagination.Host() {
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
				return pagination0205StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination0205FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination0205BckUI;
			}
			@Override
			public int getListSize() {
				return marketOrdersHistoryFeed.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new MarketOrderHistoryTile(assetBase, assetQuote), marketOrdersHistoryFeed, pagination0205UI, index);
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
		
		pagination0301UI.setHost(new Pagination.Host() {
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
				return pagination0301StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination0301FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination0301BckUI;
			}
			@Override
			public int getListSize() {
				return myBidOrdersFeed.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new MyOrderBidAskTile(assetBase, assetQuote),
		        		myBidOrdersFeed, pagination0301UI, index);
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
		pagination0302UI.setHost(new Pagination.Host() {
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
				return pagination0302StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination0302FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination0302BckUI;
			}
			@Override
			public int getListSize() {
				return myAskOrdersFeed.size();
			}
			@Override
			public Pane getTilePane(int index) {
				return createTilePane(new MyOrderBidAskTile(assetBase, assetQuote),
		        		myAskOrdersFeed, pagination0302UI, index);
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
		pagination0303UI.setHost(new Pagination.Host() {
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
				return 70;
			}
			@Override
			public Label getStatusUI() {
				return pagination0303StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination0303FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination0303BckUI;
			}
			@Override
			public int getListSize() {
				return myShortOrdersFeed.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new MyOrderShortTile(assetBase, assetQuote, item.getCurrent_feed_price()),
		        		myShortOrdersFeed, pagination0303UI, index);
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
		pagination0304UI.setHost(new Pagination.Host() {
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
				return 70;
			}
			@Override
			public Label getStatusUI() {
				return pagination0304StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination0304FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination0304BckUI;
			}
			@Override
			public int getListSize() {
				return myCoverOrdersFeed.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new MyOrderCoverTile(assetBase, assetQuote),
		        		myCoverOrdersFeed, pagination0304UI, index);
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
		pagination0305UI.setHost(new Pagination.Host() {
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
				return 70;
			}
			@Override
			public Label getStatusUI() {
				return pagination0305StatusUI;
			}
			@Override
			public Node getFwdUI() {
				return pagination0305FwdUI;
			}
			@Override
			public Node getBckUI() {
				return pagination0305BckUI;
			}
			@Override
			public int getListSize() {
				return myOrdersHistoryFeed.size();
			}
			@Override
			public Pane getTilePane(int index) {
		        return createTilePane(new TransactionTile(), myOrdersHistoryFeed, pagination0305UI, index);
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
		feedPriceUI.setText(String.format("%s: %.8f", "Feed price", item.getCurrent_feed_price()));
		marketDepthChartUI.setFeedPrice(item.getCurrent_feed_price());
		progressInfoUI.setText(String.format("%s %s", "Loading", getTitle()));
	}
	
	@Override
	public void loadData() {
		if (!isDataLoaded) {
			if (module != null)
				module.isProcessing().set(true);
			groundUI.setVisible(true);
			animateProgress(1.0, 500, progressTrackUI, () -> {
				animateProgress(0.2, progressBarUI, () -> {
					if (!buildPriceHistoryFeed()) {
						progressUI.setVisible(false);
						if (module != null)
							module.isProcessing().set(false);
						return;
					}
					animateProgress(0.3, progressBarUI, () -> {
						populatePriceHistoryChart();
						animateProgress(0.4, progressBarUI, () -> {
							populateMarketOrders();
							animateProgress(0.5, progressBarUI, () -> {
								populateMarketDepthChart();
								animateProgress(0.6, progressBarUI, () -> {
									populateMarketOrdersHistory();
									animateProgress(0.8, progressBarUI, () -> {
										populateMyOrders();
										animateProgress(0.9, progressBarUI, () -> {
											populateMyOrdersHistory();
											animateProgress(1.0, progressBarUI, () -> {
												progressUI.setVisible(false);
												groundUI.setVisible(false);
												if (module != null)
													module.isProcessing().set(false);
												super.loadData();
											});
										});
									});
								});
							});
						});
					});
				});
			});
		}
	}
	
	@Override
	public void ping() {
		if (isDataLoaded) {
			if (updateFeed())
				updatePriceHistoryChart();
			if (marketDepthUpdateNeeded)
				updateMarketDepthChart();
			updateMarketOrders();
			updateMarketOrdersHistory();
			updateMyOrders();
			updateMyOrdersHistory();
		}
		super.ping();
	}
	
	
	private void populatePriceHistoryChart() {
    	priceHistoryRangeSliderUI.lowValueProperty().removeListener(priceHistoryLowerBoundListener);
		priceHistoryRangeSliderUI.highValueProperty().removeListener(priceHistoryUpperBoundListener);
    	
		priceHistoryRangeSliderUI.setMax(0);
		priceHistoryRangeSliderUI.setMin(-priceHistoryFeed.size());
		priceHistoryRangeSliderUI.setLowValue((int) (-priceHistoryFeed.size() * 0.5));
		priceHistoryRangeSliderUI.setHighValue(0);
		
		priceHistoryChartXAxisUI.setLowerBound(priceHistoryRangeSliderUI.getLowValue() + priceHistoryFeed.size());
		double upperBound = priceHistoryRangeSliderUI.getHighValue() + priceHistoryFeed.size();
		upperBound += (priceHistoryRangeSliderUI.getHighValue() - priceHistoryRangeSliderUI.getLowValue()) * HORIZONTAL_OFFSET_FACTOR;
		priceHistoryChartXAxisUI.setUpperBound(upperBound);
		
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
    	
    	PriceHistoryPeriod period = priceHistoryPeriodsUI.getSelectedItem();
    	
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
	    				assetQuote.getSymbol(), assetBase.getSymbol(),
	    				Time.encode(t.minusDays(1L)), duration, HistoryGranularity.EACH_DAY);
	    		if (list.size() == 0) {
	    			statusUI.setText("There is no price history");
	    			return false;
	    		}
	    		level = list.get(0).getClosing_price();
    		}
    		
			List<BlockchainMarketPriceHistory.Result> list = BlockchainMarketPriceHistory.run(
					assetQuote.getSymbol(), assetBase.getSymbol(),
					Time.encode(t), duration, HistoryGranularity.EACH_BLOCK);
			
			LocalDateTime u = t.plusMinutes(period.getMinutes());
			List<BlockchainMarketPriceHistory.Result> temp = new ArrayList<BlockchainMarketPriceHistory.Result>();
			
			for (BlockchainMarketPriceHistory.Result result : list) {
				LocalDateTime x = Time.decode(result.getTimestamp());
				while (!x.isBefore(u)) {
					PriceCandle candle = (temp.isEmpty() ? new PriceCandle(u, level) : new PriceCandle(u, temp));
					priceHistoryFeed.add(candle);
					level = candle.getClose();
					u = u.plusMinutes(period.getMinutes());
					temp.clear();
				}
				temp.add(result);
			}	
			priceHistoryFeed.add(temp.isEmpty() ? new PriceCandle(u, level) : new PriceCandle(u, temp));
    	} catch (BTSSystemException e) {
			systemException(e);
			return false;
		}
    	return true;
    }
    
    private boolean updateFeed() {
    	return false;
    }
	
    private void populateMarketOrders() {
    	try {
    		marketBidOrdersFeed.addAll(BlockchainMarketListBids.run(
    				assetQuote.getSymbol(), assetBase.getSymbol(), BLOCKCHAIN_ORDER_LIMIT));
    		marketAskOrdersFeed.addAll(BlockchainMarketListAsks.run(
    				assetQuote.getSymbol(), assetBase.getSymbol(), BLOCKCHAIN_ORDER_LIMIT));
    		marketShortOrdersFeed.addAll(BlockchainMarketListShorts.run(
    				assetQuote.getSymbol(), BLOCKCHAIN_ORDER_LIMIT));
    		marketCoverOrdersFeed.addAll(BlockchainMarketListCovers.run(
    				assetQuote.getSymbol(), BLOCKCHAIN_ORDER_LIMIT));
    	} catch (BTSSystemException e) {
			systemException(e);
			return;
		}
    	{
	    	long cumulative = 0;
	    	for (MarketOrder order : marketBidOrdersFeed) {
	    		cumulative += order.getState().getBalance();
	    		cumulativeMarketBidOrdersFeed.add(new CumulativeMarketOrder(order, cumulative));
	    	}
    	}
    	{
	    	long cumulative = 0;
	    	for (MarketOrder order : marketAskOrdersFeed) {
	    		cumulative += order.getState().getBalance();
	    		cumulativeMarketAskOrdersFeed.add(new CumulativeMarketOrder(order, cumulative));
	    	}
    	}
    	{
    		List<MarketOrder> marketShortOrdersFeedSorted = new ArrayList<MarketOrder>(marketShortOrdersFeed);
    		marketShortOrdersFeedSorted.sort(shortMarketOrderComperator);
    		long cumulative = 0;
	    	for (MarketOrder order : marketShortOrdersFeedSorted) {
	    		cumulative += order.getState().getBalance();
	    		cumulativeMarketShortOrdersFeed.add(new CumulativeMarketOrder(order, cumulative));
	    	}
    	}
    	{
    		List<MarketOrder> marketCoverOrdersFeedSorted = new ArrayList<MarketOrder>(marketCoverOrdersFeed);
    		marketCoverOrdersFeedSorted.sort(coverMarketOrderComperator);
    		long cumulative = 0;
	    	for (MarketOrder order : marketCoverOrdersFeedSorted) {
	    		cumulative += order.getState().getBalance();
	    		cumulativeMarketCoverOrdersFeed.add(new CumulativeMarketOrder(order, cumulative));
	    	}
    	}
    	pagination0201UI.reset();
    	pagination0202UI.reset();
    	pagination0203UI.reset();
    	pagination0204UI.reset();
    }
    
    private void updateMarketOrders() {
		new Thread(() -> {
			List<MarketOrder> feed;
			try {
	    		feed = BlockchainMarketListBids.run(
	    				assetQuote.getSymbol(), assetBase.getSymbol(), BLOCKCHAIN_ORDER_LIMIT);
	    	} catch (BTSSystemException e) {
				return;
			}
			if (isDifferentMarketOrderList(feed, this.marketBidOrdersFeed)) {
    			this.marketBidOrdersFeed.clear();
    			this.marketBidOrdersFeed.addAll(feed);
    			this.cumulativeMarketBidOrdersFeed.clear();
    			long cumulative = 0;
    	    	for (MarketOrder order : marketBidOrdersFeed) {
    	    		cumulative += order.getState().getBalance();
    	    		this.cumulativeMarketBidOrdersFeed.add(new CumulativeMarketOrder(order, cumulative));
    	    	}
    			marketDepthUpdateNeeded = true;
    			Platform.runLater(() -> {
    				pagination0201UI.reset();
    			});
    		}
		}).start();
		
		new Thread(() -> {
			List<MarketOrder> feed;
			try {
				feed = BlockchainMarketListAsks.run(
	    				assetQuote.getSymbol(), assetBase.getSymbol(), BLOCKCHAIN_ORDER_LIMIT);
	    	} catch (BTSSystemException e) {
				return;
			}
			if (isDifferentMarketOrderList(feed, this.marketAskOrdersFeed)) {
    			this.marketAskOrdersFeed.clear();
    			this.marketAskOrdersFeed.addAll(feed);
    			this.cumulativeMarketAskOrdersFeed.clear();
    			long cumulative = 0;
    	    	for (MarketOrder order : marketAskOrdersFeed) {
    	    		cumulative += order.getState().getBalance();
    	    		this.cumulativeMarketAskOrdersFeed.add(new CumulativeMarketOrder(order, cumulative));
    	    	}
    			marketDepthUpdateNeeded = true;
    			Platform.runLater(() -> {
    				pagination0202UI.reset();
    			});
    		}
		}).start();
		
		new Thread(() -> {
			List<MarketOrder> feed;
			try {
				feed = BlockchainMarketListShorts.run(
	    				assetQuote.getSymbol(), BLOCKCHAIN_ORDER_LIMIT);
	    	} catch (BTSSystemException e) {
				return;
			}
			if (isDifferentMarketOrderList(feed, this.marketShortOrdersFeed)) {
    			this.marketShortOrdersFeed.clear();
    			this.marketShortOrdersFeed.addAll(feed);
    			this.cumulativeMarketShortOrdersFeed.clear();
    			List<MarketOrder> marketShortOrdersFeedSorted = new ArrayList<MarketOrder>(this.marketShortOrdersFeed);
        		marketShortOrdersFeedSorted.sort(shortMarketOrderComperator);
        		long cumulative = 0;
    	    	for (MarketOrder order : marketShortOrdersFeedSorted) {
    	    		cumulative += order.getState().getBalance();
    	    		this.cumulativeMarketShortOrdersFeed.add(new CumulativeMarketOrder(order, cumulative));
    	    	}
    			marketDepthUpdateNeeded = true;
    			Platform.runLater(() -> {
    				pagination0203UI.reset();
    			});
    		}
		}).start();
		
		new Thread(() -> {
			List<MarketOrder> feed;
			try {
				feed = BlockchainMarketListCovers.run(
	    				assetQuote.getSymbol(), BLOCKCHAIN_ORDER_LIMIT);
	    	} catch (BTSSystemException e) {
				return;
			}
			if (isDifferentMarketOrderList(feed, this.marketCoverOrdersFeed)) {
    			this.marketCoverOrdersFeed.clear();
    			this.marketCoverOrdersFeed.addAll(feed);
    			this.cumulativeMarketCoverOrdersFeed.clear();
    			List<MarketOrder> marketCoverOrdersFeedSorted = new ArrayList<MarketOrder>(this.marketCoverOrdersFeed);
        		marketCoverOrdersFeedSorted.sort(coverMarketOrderComperator);
        		long cumulative = 0;
    	    	for (MarketOrder order : marketCoverOrdersFeedSorted) {
    	    		cumulative += order.getState().getBalance();
    	    		cumulativeMarketCoverOrdersFeed.add(new CumulativeMarketOrder(order, cumulative));
    	    	}
    			Platform.runLater(() -> {
    				pagination0204UI.reset();
    			});
    		}
		}).start();
    }
    
    private void populateMarketOrdersHistory() {
		try {
			marketOrdersHistoryFeed.addAll(BlockchainMarketOrderHistory.run
					(assetQuote.getSymbol(), assetBase.getSymbol(), 0, BLOCKCHAIN_ORDER_HISTORY_LIMIT, ""));
		} catch (BTSSystemException e) {
			systemException(e);
			return;
		}
		pagination0205UI.reset();
    }
    
    private void updateMarketOrdersHistory() {
    	List<BlockchainMarketOrderHistory.Result> feed;
		try {
			feed = BlockchainMarketOrderHistory.run
					(assetQuote.getSymbol(), assetBase.getSymbol(), 0, BLOCKCHAIN_ORDER_HISTORY_LIMIT, "");
			
		} catch (BTSSystemException e) {
			return;
		}
		if (isDifferentMarketOrderHistoryList(feed, this.marketOrdersHistoryFeed)) {
			this.marketOrdersHistoryFeed.clear();
			this.marketOrdersHistoryFeed.addAll(feed);
			Platform.runLater(() -> {
				pagination0205UI.reset();
			});
		}
    }
    
    private void populateMarketDepthChart() {
    	double bidTotal = 0;
    	Double bestBidPrice = null;
    	{
    		List<AreaChart.Data<Number, Number>> list = new ArrayList<AreaChart.Data<Number, Number>>();
    		List<AreaChart.Data<Number, Number>> list1 = new ArrayList<AreaChart.Data<Number, Number>>();
    		if (marketDepthComponentsUI.isSelectItem(MarketDepthComponent.Bid)
				|| marketDepthComponentsUI.isSelectItem(MarketDepthComponent.Short)) {
				
    			List<MarketOrder> feed = new ArrayList<MarketOrder>();
    			feed.addAll(marketBidOrdersFeed);
				feed.addAll(marketShortOrdersFeed);
				feed.sort((MarketOrder o1, MarketOrder o2) -> {
					return getRealPrice(o2).compareTo(getRealPrice(o1));
				});
				if (feed.size() > 0) {
					bestBidPrice = getRealPrice(feed.get(0));
					if (marketDepthComponentsUI.isSelectItem(MarketDepthComponent.Bid)) {
						double total = 0;
						double subtotal = 0;
						Double price = null;
						for (MarketOrder order : feed) {
							double p = getRealPrice(order);
							if (price == null)
								price = p;
							else if (p < price) {
								total += subtotal;
								list.add(new AreaChart.Data<Number, Number>(price, total));
								price = p;
								subtotal = 0;
							}
							if (price < bestBidPrice * marketDepthSliderUI.getValue())
								break;
							double delta = getRealValue(order);
							subtotal += delta;
						}
						if (subtotal > 0) {
							total += subtotal;
							list.add(new AreaChart.Data<Number, Number>(price, total));
							price = null;
							subtotal = 0;
						}
						bidTotal = Math.max(total, bidTotal);
						Collections.reverse(list);
					}
					if (marketDepthComponentsUI.isSelectItem(MarketDepthComponent.Short)) {
						double total = 0;
						double subtotal = 0;
						Double price = null;
						List<MarketOrder> marketShortOrdersFeedSorted = new ArrayList<MarketOrder>(marketShortOrdersFeed);
		        		marketShortOrdersFeedSorted.sort(shortMarketOrderComperator);
						for (MarketOrder order : marketShortOrdersFeedSorted) {
							double p = getRealPrice(order);
							if (price == null)
								price = p;
							else if (p < price) {
								total += subtotal;
								list1.add(new AreaChart.Data<Number, Number>(price, total));
								price = p;
								subtotal = 0;
							}
							if (price < bestBidPrice * marketDepthSliderUI.getValue())
								break;
							double delta = getRealValue(order);
							subtotal += delta;
						}
						if (subtotal > 0) {
							total += subtotal;
							list1.add(new AreaChart.Data<Number, Number>(price, total));
							price = null;
							subtotal = 0;
						}
						bidTotal = Math.max(total, bidTotal);
						Collections.reverse(list1);
					}
				}
			}
			marketDepthBidSeries.getData().setAll(list);
			marketDepthShortSeries.getData().setAll(list1);
    	}
		double askTotal = 0;
		Double bestAskPrice = null;
		{
			List<AreaChart.Data<Number, Number>> list = new ArrayList<AreaChart.Data<Number, Number>>();
			if (marketDepthComponentsUI.isSelectItem(MarketDepthComponent.Ask)) {
				if (marketAskOrdersFeed.size() > 0) {
					bestAskPrice = getRealPrice(marketAskOrdersFeed.get(0));
					double subtotal = 0;
					Double price = null;
					for (MarketOrder order : marketAskOrdersFeed) {
						double p = getRealPrice(order);
						if (price == null)
							price = p;
						else if (p > price) {
							askTotal += subtotal;
							list.add(new AreaChart.Data<Number, Number>(price, askTotal));
							price = p;
							subtotal = 0;
						}
						if (price > bestAskPrice / marketDepthSliderUI.getValue())
							break;
						double delta = getRealValue(order);
						subtotal += delta;
					}
					if (subtotal > 0) {
						askTotal += subtotal;
						list.add(new AreaChart.Data<Number, Number>(price, askTotal));
						price = null;
						subtotal = 0;
					}
				}
			}
			marketDepthAskSeries.getData().setAll(list);
		}
		
		if (bestBidPrice == null && bestAskPrice == null) {
			marketDepthChartUI.setVisible(false);
		} else {
			marketDepthChartUI.setVisible(true);
			if (bestBidPrice != null) {
				marketDepthChartXAxisUI.setLowerBound((1 - AXIS_RANGE_FACTOR) * bestBidPrice * marketDepthSliderUI.getValue());
				if (bestAskPrice != null)
					marketDepthChartXAxisUI.setUpperBound((1 + AXIS_RANGE_FACTOR) * bestAskPrice / marketDepthSliderUI.getValue());
				else
					marketDepthChartXAxisUI.setUpperBound((1 + AXIS_RANGE_FACTOR) * bestBidPrice);
			} else {
				marketDepthChartXAxisUI.setLowerBound((1 - AXIS_RANGE_FACTOR) * bestAskPrice);
				marketDepthChartXAxisUI.setUpperBound((1 + AXIS_RANGE_FACTOR) * bestAskPrice / marketDepthSliderUI.getValue());
			}
			{
				marketDepthChartYAxisUI.setUpperBound(Math.ceil((1 + AXIS_RANGE_FACTOR) * Math.max(bidTotal, askTotal) / 100000.0) * 100000);
				double span = marketDepthChartYAxisUI.getUpperBound() - marketDepthChartYAxisUI.getLowerBound();
				marketDepthChartYAxisUI.setTickUnit(span / 10);
			}
			{
				double span = marketDepthChartXAxisUI.getUpperBound() - marketDepthChartXAxisUI.getLowerBound();
				marketDepthChartXAxisUI.setTickUnit(span / 10);
			}
		}
		marketDepthUpdateNeeded = false;
    }
    
    private void updateMarketDepthChart() {
    	populateMarketDepthChart();
    }
    
    private void populateMyOrders() {
    	try {
    		this.myOrdersFeed.addAll(WalletMarketOrderList.run(
					assetQuote.getSymbol(), assetBase.getSymbol(), -1, ""));
		} catch (BTSSystemException e) {
			systemException(e);
			return;
		}
    	splitMyOrders();
    	pagination0301UI.reset();
    	pagination0302UI.reset();
    	pagination0303UI.reset();
    	pagination0304UI.reset();
    }
    
    private void updateMyOrders() {
    	new Thread(() -> {
	    	List<WalletMarketOrderList.Result> feed;
			try {
				feed = WalletMarketOrderList.run(
						assetQuote.getSymbol(), assetBase.getSymbol(), -1, "");
			} catch (BTSSystemException e) {
				systemException(e);
				return;
			}
			if (isDifferentMyOrderList(feed, this.myOrdersFeed)) {
		    	this.myOrdersFeed.clear();
				this.myOrdersFeed.addAll(feed);
				this.myBidOrdersFeed.clear();
				this.myAskOrdersFeed.clear();
				this.myShortOrdersFeed.clear();
				this.myCoverOrdersFeed.clear();
				splitMyOrders();
				Platform.runLater(() -> {
					pagination0301UI.reset();
			    	pagination0302UI.reset();
			    	pagination0303UI.reset();
			    	pagination0304UI.reset();
				});
			}
    	}).start();
    }
    
    private void splitMyOrders() {
		for (Account account : Model.getInstance().getMyAccounts()) {
			List<WalletMarketOrderList.Result> feed;
			try {
				feed = WalletMarketOrderList.run(
						assetQuote.getSymbol(), assetBase.getSymbol(), -1, account.getName());
			} catch (BTSSystemException e) {
				systemException(e);
				return;
			}
			for (WalletMarketOrderList.Result r : feed) {
				MyOrder order = new MyOrder(r.getId(), r.getOrder(), account);
				if (r.getOrder().getType().equals("bid_order"))
					myBidOrdersFeed.add(order);
				else if (r.getOrder().getType().equals("ask_order"))
					myAskOrdersFeed.add(order);
				else if (r.getOrder().getType().equals("short_order"))
					myShortOrdersFeed.add(order);
				else if (r.getOrder().getType().equals("cover_order"))
					myCoverOrdersFeed.add(order);
			}
		}
    }
		
	private void populateMyOrdersHistory() {
		List<Transaction> transactions;
		try {
			transactions = Model.getTransactions(WalletAccountTransactionHistory.run("", assetQuote.getSymbol(), 0));
		} catch (BTSSystemException e) {
			systemException(e);
			return;
		}
		for (Transaction t : transactions) {
			if (t.isIs_confirmed() && t.isIs_market() && !t.isIs_market_cancel())
				myOrdersHistoryFeed.add(t);
		}
		pagination0305UI.reset();
	}
	
	private void updateMyOrdersHistory() {
		new Thread(() -> {
			List<Transaction> transactions;
			try {
				transactions = Model.getTransactions(WalletAccountTransactionHistory.run("", assetQuote.getSymbol(), 0));
			} catch (BTSSystemException e) {
				return;
			}
			List<Transaction> feed = new ArrayList<Transaction>();
			for (Transaction t : transactions) {
				if (t.isIs_confirmed() && t.isIs_market() && !t.isIs_market_cancel())
					feed.add(t);
			}
			if (isDifferentMyOrderHistoryList(feed, this.myOrdersHistoryFeed)) {
				this.myOrdersHistoryFeed.clear();
				this.myOrdersHistoryFeed.addAll(feed);
				Platform.runLater(() -> {
					pagination0305UI.reset();
				});
			}
		}).start();
		
	}
	
    private void adjustPriceHistoryYAxisBounds() {
    	double minValue = Double.MAX_VALUE;
        double maxValue = -Double.MAX_VALUE;
        int beg = (int) priceHistoryChartXAxisUI.getLowerBound();
        int end = Math.min((int) priceHistoryChartXAxisUI.getUpperBound(), priceHistoryFeed.size());
    	for (int i = beg; i < end; i++) {
    		PriceCandle c = priceHistoryFeed.get(i);
    		minValue = Math.min(minValue, c.getLow());
    		maxValue = Math.max(maxValue, c.getHigh());
        }
    	priceHistoryChartYAxisUI.setLowerBound((1 - AXIS_RANGE_FACTOR) * minValue);
    	priceHistoryChartYAxisUI.setUpperBound((1 + AXIS_RANGE_FACTOR) * maxValue);
    	priceHistoryChartYAxisUI.setTickUnit(0.0010);
    }
    
    private Double getRealPrice(MarketOrder order) {
    	if (order.getType().equals("short_order"))
    		return Model.getInstance().getShortLimitRealPrice(order, item.getCurrent_feed_price());
    	if (order.getType().equals("bid_order"))
    		return Model.getInstance().getRealPrice(order.getMarket_index().getOrder_price());
    	if (order.getType().equals("ask_order"))
    		return Model.getInstance().getRealPrice(order.getMarket_index().getOrder_price());
    	return null;
    }
    
    private Double getRealValue(MarketOrder order) {
    	if (order.getType().equals("short_order"))
    		return Model.getInstance().getRealValue(assetBase, order.getState().getBalance());
    	if (order.getType().equals("bid_order"))
    		return Model.getInstance().getRealValue(assetBase, (long) (order.getState().getBalance() / order.getMarket_index().getOrder_price().getRatio()));
    	if (order.getType().equals("ask_order"))
    		return Model.getInstance().getRealValue(assetBase, order.getState().getBalance());
    	return null;
    }
	
	@FXML
	private void onOrderBuy() {
		Wizard_MarketOrderBid wizard = new Wizard_MarketOrderBid(assetBase, assetQuote);
		wizard.setOrders(marketAskOrdersFeed);
		wizard.setCallback((Account account) -> {
			panoramaUI.setIndex(2);
			panorama03UI.setIndex(0);
			myBidOrdersFeed.add(new MyOrder(null, null, account));
			pagination0301UI.reset();
		});
		module.jump(wizard);
	}
	@FXML
	private void onOrderSell() {
		Wizard_MarketOrderAsk wizard = new Wizard_MarketOrderAsk(assetBase, assetQuote);
		wizard.setOrders(marketBidOrdersFeed);
		wizard.setCallback((Account account) -> {
			panoramaUI.setIndex(2);
			panorama03UI.setIndex(1);
			myAskOrdersFeed.add(new MyOrder(null, null, account));
			pagination0302UI.reset();
		});
		module.jump(wizard);
	}
	@FXML
	private void onOrderShort() {
		Wizard_MarketOrderShort wizard = new Wizard_MarketOrderShort(assetBase, assetQuote);
		wizard.setFeedPrice(item.getCurrent_feed_price());
		wizard.setCallback((Account account) -> {
			panoramaUI.setIndex(2);
			panorama03UI.setIndex(2);
			myShortOrdersFeed.add(new MyOrder(null, null, account));
			pagination0303UI.reset();
		});
		module.jump(wizard);
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
	private void onPanoramaToggle03() {
		panoramaMenuUI.setIndex(2);
		applyPanoramaIndex(2);
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
	@FXML
	private void onPanoramaMenu03() {
		panoramaToggleGroupUI.selectToggle(panoramaToggleGroupUI.getToggles().get(2));
		applyPanoramaIndex(2);
	}
	
	
	@FXML
	private void onPanoramaMenu0101() {
		panorama01UI.setIndex(0);
	}
	@FXML
	private void onPanoramaMenu0102() {
		panorama01UI.setIndex(1);
	}
	
	@FXML
	private void onPanoramaMenu0201() {
		panorama02UI.setIndex(0);
	}
	@FXML
	private void onPanoramaMenu0202() {
		panorama02UI.setIndex(1);
	}
	@FXML
	private void onPanoramaMenu0203() {
		panorama02UI.setIndex(2);
	}
	@FXML
	private void onPanoramaMenu0204() {
		panorama02UI.setIndex(3);
	}
	@FXML
	private void onPanoramaMenu0205() {
		panorama02UI.setIndex(4);
	}
	
	@FXML
	private void onPanoramaMenu0301() {
		panorama03UI.setIndex(0);
	}
	@FXML
	private void onPanoramaMenu0302() {
		panorama03UI.setIndex(1);
	}
	@FXML
	private void onPanoramaMenu0303() {
		panorama03UI.setIndex(2);
	}
	@FXML
	private void onPanoramaMenu0304() {
		panorama03UI.setIndex(3);
	}
	@FXML
	private void onPanoramaMenu0305() {
		panorama03UI.setIndex(4);
	}
	
	private void applyPanoramaIndex(int index) {
		panoramaUI.setIndex(index);
	}
	
	
	@FXML
	private void onPagination0201Bck() {
		pagination0201UI.turnPageBck();
	}
	@FXML
	private void onPagination0201Fwd() {
		pagination0201UI.turnPageFwd();
	}
	
	@FXML
	private void onPagination0202Bck() {
		pagination0202UI.turnPageBck();
	}
	@FXML
	private void onPagination0202Fwd() {
		pagination0202UI.turnPageFwd();
	}
	
	@FXML
	private void onPagination0203Bck() {
		pagination0203UI.turnPageBck();
	}
	@FXML
	private void onPagination0203Fwd() {
		pagination0203UI.turnPageFwd();
	}
	
	@FXML
	private void onPagination0204Bck() {
		pagination0204UI.turnPageBck();
	}
	@FXML
	private void onPagination0204Fwd() {
		pagination0204UI.turnPageFwd();
	}
	
	@FXML
	private void onPagination0205Bck() {
		pagination0205UI.turnPageBck();
	}
	@FXML
	private void onPagination0205Fwd() {
		pagination0205UI.turnPageFwd();
	}
	
	
	@FXML
	private void onPagination0301Bck() {
		pagination0301UI.turnPageBck();
	}
	@FXML
	private void onPagination0301Fwd() {
		pagination0301UI.turnPageFwd();
	}
	
	@FXML
	private void onPagination0302Bck() {
		pagination0302UI.turnPageBck();
	}
	@FXML
	private void onPagination0302Fwd() {
		pagination0302UI.turnPageFwd();
	}
	
	@FXML
	private void onPagination0303Bck() {
		pagination0303UI.turnPageBck();
	}
	@FXML
	private void onPagination0303Fwd() {
		pagination0303UI.turnPageFwd();
	}
	
	@FXML
	private void onPagination0304Bck() {
		pagination0304UI.turnPageBck();
	}
	@FXML
	private void onPagination0304Fwd() {
		pagination0304UI.turnPageFwd();
	}
	
	@FXML
	private void onPagination0305Bck() {
		pagination0305UI.turnPageBck();
	}
	@FXML
	private void onPagination0305Fwd() {
		pagination0305UI.turnPageFwd();
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
    private static boolean isDifferentMyOrderHistoryList(List<Transaction> list1, List<Transaction> list2) {
    	if (list1.size() != list2.size())
    		return true;
    	for (int i = 0; i < list1.size(); i++) {
    		if (!list1.get(i).getTrx_id().equals(list2.get(i).getTrx_id()))
    			return true;
    	}
    	return false;
    }
	
    private static boolean verifyRecentMarket(Market market) {
		for (Market item : Model.getInstance().getRecentMarkets())
			if (item.getQuote_id() == market.getQuote_id()
					&& item.getBase_id() == market.getBase_id())
				return true;
		return false;
	}
    
	private static class PriceCandle {
		private PriceCandle(LocalDateTime time, double level) {
			this.time = time;
			this.open = level;
			this.close = level;
			this.high = level;
			this.low = level;
			this.volume = 0;
		}
		private PriceCandle(LocalDateTime time, List<BlockchainMarketPriceHistory.Result> list) {
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
		
		private double getOpen() {
			return open;
		}
		private double getClose() {
			return close;
		}
		private double getHigh() {
			return high;
		}
		private double getLow() {
			return low;
		}
		private double getAverage() {
			return (high + low) * 0.5;
		}
		
		@Override
		public String toString() {
			return String.format("%s: [%8.6f %8.6f %8.6f %8.6f] %d", Time.format(time), open, close, high, low, volume);
		}
	}
	
	private enum PriceHistoryPeriod {
		M5("5M", 5, 1), M30("30M", 30, 5), H1("1H", 60, 10), H6("6H", 360, 50), H12("12H", 720, 100), D1("1D", 1440, 200);
		private PriceHistoryPeriod(String value, int minutes, int days) {
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
	
	private enum MarketDepthComponent {
		Bid("Buy"), Short("Short"), Ask("Sell");
		private MarketDepthComponent(String value) {
			this.value = value;

		}
		private String value;
		
		@Override
		public String toString() {
			return value;
		}
	}

}