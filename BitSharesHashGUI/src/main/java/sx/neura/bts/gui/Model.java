package sx.neura.bts.gui;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sx.neura.bts.gui.dto.AccountAndAmount;
import sx.neura.bts.gui.dto.AccountAndAmounts;
import sx.neura.bts.gui.dto.AmountAndAccounts;
import sx.neura.bts.gui.dto.DelegateAnnouncement;
import sx.neura.bts.gui.dto.MarketNews;
import sx.neura.bts.gui.dto.Transaction;
import sx.neura.bts.json.api.blockchain.BlockchainListActiveDelegates;
import sx.neura.bts.json.api.blockchain.BlockchainListAssets;
import sx.neura.bts.json.api.blockchain.BlockchainMarketStatus;
import sx.neura.bts.json.api.wallet.WalletAccountBalance;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory;
import sx.neura.bts.json.api.wallet.WalletAccountTransactionHistory.LedgerEntry;
import sx.neura.bts.json.api.wallet.WalletClose;
import sx.neura.bts.json.api.wallet.WalletCreate;
import sx.neura.bts.json.api.wallet.WalletGetSetting;
import sx.neura.bts.json.api.wallet.WalletListAccounts;
import sx.neura.bts.json.api.wallet.WalletOpen;
import sx.neura.bts.json.api.wallet.WalletSetSetting;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.Market;
import sx.neura.bts.json.dto.Price;
import sx.neura.bts.json.dto.RegisteredName;
import sx.neura.bts.json.enumerations.VoteMethod;
import sx.neura.bts.json.exceptions.BTSSystemException;
import sx.neura.bts.json.exceptions.BTSUserException;
import sx.neura.bts.util.Time;

public class Model {
	
	private static Model instance;
	public static Model getInstance() {
		if (!isInstance())
			instance = new Model();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private Model() {
	}
    
	private static final String DEFAULT_COLOR_SET = "Set05";
	private static final String COLOR_SET_KEY = "sx-color-set";
	
    public static final int WALLET_UNLOCK_TIMEOUT = 10000;
    public static final int REGISTERED_NAMES_LIMIT = 10000;
    public static final int REGISTERED_NAMES_LIMIT_ALL = 100000;
    public static final int DELEGATES_ACTIVE_LIMIT = 101;
    public static final int DELEGATES_ALL_LIMIT = 201;
    public static final int BLOCKS_LIMIT = 1000;
    public static final double SHORT_COLLATERAL_FACTOR = 0.5;
    
//    private static final String AVATAR_GENERATOR = "http://robohash.org";
    private static final String[] VIRTUAL_NAMES = new String[] {"UNKNOWN", "NETWORK", "ASK-", "BID-", "SHORT-", "MARGIN-", "MARKET"};
    
    private PrintStream printStream;
	public PrintStream getPrintStream() {
		return printStream;
	}
	public void setPrintStream(PrintStream printStream) {
		this.printStream = printStream;
	}
    
    private boolean isWalletOpen;
	public boolean isWalletOpen() {
		return isWalletOpen;
	}
	public void setWalletOpen(boolean isWalletOpen) {
		this.isWalletOpen = isWalletOpen;
	}
	
    public void openWallet() throws BTSSystemException {
    	try {
    		WalletOpen.run();
		} catch (BTSUserException e) {
			WalletCreate.run();
		}
    	isWalletOpen = true;
    }
    public void closeWallet() {
    	try {
			WalletClose.run();
		} catch (BTSSystemException e) {
			return;
		}
    	isWalletOpen = false;
    }
    
    private String colorSetName;
    public void setColorSetName(String colorSetName) {
    	if (colorSetName.equals(this.colorSetName))
    		return;
    	if (this.colorSetName != null)
    		Main.unloadColorSet(this.colorSetName);
    	Main.loadColorSet(colorSetName);
    	setDefaultColorSet(colorSetName);
    	this.colorSetName = colorSetName;
    }
    public String getColorSetName() {
    	if (colorSetName == null)
    		colorSetName = getDefaultColorSet();
    	return colorSetName;
    }
    
    public void initialize() throws BTSSystemException {
		{
			this.assets = BlockchainListAssets.run();
			this.assetsIdIndex = new ArrayList<Integer>();
			this.assetsSymbolIndex = new ArrayList<String>();
			for (Asset asset : assets) {
				assetsIdIndex.add(asset.getId());
				assetsSymbolIndex.add(asset.getSymbol());
			}
		}
		{
			Asset bts = getAssetById(0);
			this.currentShareSupply = new Double(bts.getCurrent_share_supply());
    	}
		{
			this.activeDelegates = BlockchainListActiveDelegates.run(0, Model.DELEGATES_ACTIVE_LIMIT);
			this.activeDelegatesIndex = new ArrayList<String>();
			for (Account account : activeDelegates)
				activeDelegatesIndex.add(account.getName());
		}
		{
			Asset bts = getAssetById(0);
			this.btsMarkets = new ArrayList<Market>();
			for (Asset asset : getAssets()) {
				if (!asset.equals(bts) && isMarketPeggedAsset(asset)) {
					Market market = getMarket(asset, bts);
					if (market != null)
						btsMarkets.add(market);
				}
			}
		}
		{
			this.delegateAnnouncements = new ArrayList<DelegateAnnouncement>();
			{
				DelegateAnnouncement item = new DelegateAnnouncement();
				item.setDelegate("liondani");
				item.setTimestamp("2015-01-12T05:59:00");
				item.setText("Proin sit amet metus dapibus, placerat est a, interdum nibh.");
				item.setLink("http://bytemaster.bitshares.org/article/2013/08/28/Own-Your-Identity-with-BitShares/");
				delegateAnnouncements.add(item);
			}
			{
				DelegateAnnouncement item = new DelegateAnnouncement();
				item.setDelegate("enterprise");
				item.setTimestamp("2014-12-28T13:00:00");
				item.setText("Sed ac fringilla massa. Phasellus nec dictum elit.");
				item.setLink("http://bytemaster.bitshares.org/article/2014/12/21/Provably-Honest-Online-Elections/");
				delegateAnnouncements.add(item);
			}
			{
				DelegateAnnouncement item = new DelegateAnnouncement();
				item.setDelegate("xeroc");
				item.setTimestamp("2014-12-17T18:40:00");
				item.setText("Praesent sit amet elit nec dolor auctor cursus.");
				item.setLink("http://bytemaster.bitshares.org/update/2014/12/18/What-is-BitShares/");
				delegateAnnouncements.add(item);
			}
		}
		{
			this.marketNews = new ArrayList<MarketNews>();
			{
				{
					Market market = getMarket(getAssetById(22), getAssetById(0));
					if (market != null) {
						MarketNews item = new MarketNews();
						item.setMarket(market);
						item.setTimestamp("2015-01-12T23:17:00");
						item.setText("Pellentesque pretium commodo erat, nec cursus urna mollis ac.");
						item.setLink("http://bytemaster.bitshares.org/article/2014/12/18/What-are-BitShares-Market-Pegged-Assets/");
						marketNews.add(item);
					}
				}
				{
					Market market = getMarket(getAssetById(14), getAssetById(0));
					if (market != null) {
						MarketNews item = new MarketNews();
						item.setMarket(market);
						item.setTimestamp("2015-01-07T09:11:00");
						item.setText("Cras volutpat tortor eros, ac tincidunt mi elementum ut.");
						item.setLink("http://bytemaster.bitshares.org/article/2014/12/22/BitShares-Login/");
						marketNews.add(item);
					}
				}
				{
					Market market = getMarket(getAssetById(7), getAssetById(0));
					if (market != null) {
						MarketNews item = new MarketNews();
						item.setMarket(market);
						item.setTimestamp("2015-01-07T16:22:00");
						item.setText("Nunc vitae eros at dolor vehicula egestas ac in sapien.");
						item.setLink("https://bitsharestalk.org/index.php?board=3.0");
						marketNews.add(item);
					}
				}
			}
		}
		{
			this.accounts = new ArrayList<Account>();
			this.accountsIndex = new ArrayList<String>();
			this.myAccounts = new ArrayList<Account>();
			this.favoriteAccounts = new ArrayList<Account>();
			redoAccounts();
			this.myAssetSplit = new ArrayList<AmountAndAccounts>();
			this.myAccountSplit = new ArrayList<AccountAndAmounts>();
			redoBalance();
			this.transactions = new ArrayList<Transaction>();
			redoTransactions();
			this.recentMarkets = new ArrayList<Market>();
			redoRecentMarkets();
		}
    }
    
    public void redoAccounts() throws BTSSystemException {
    	this.accounts.clear();
    	this.accountsIndex.clear();
    	this.myAccounts.clear();
    	this.favoriteAccounts.clear();
    	
		this.accounts.addAll(WalletListAccounts.run());
		for (Account account : accounts) {
			this.accountsIndex.add(account.getName());
			if (account.isIs_my_account())
				this.myAccounts.add(account);
			if (account.isIs_favorite())
				this.favoriteAccounts.add(account);
		}
    }
    
    public void redoBalance() throws BTSSystemException {
    	this.myAssetSplit.clear();
    	this.myAccountSplit.clear();
    	
		List<WalletAccountBalance.Result> results = WalletAccountBalance.run();
		if (results.isEmpty()) {
			AmountAndAccounts item = new AmountAndAccounts();
			item.setAmount(0);
			item.setAsset(getAssetById(0));
			item.setSplit(new ArrayList<AccountAndAmount>());
			this.myAssetSplit.add(item);
		} else {
			for (WalletAccountBalance.Result r : results) {
				for (Amount amount : r.getAmounts()) {
					AccountAndAmount s = new AccountAndAmount();
					s.setAccount(getAccountByName(r.getName()));
					Amount a = new Amount();
					a.setAsset_id(amount.getAsset_id());
					a.setValue(amount.getValue());
					s.setAmount(a);
					int index = -1;
					for (int i = 0; i < this.myAssetSplit.size(); i++) {
						if (this.myAssetSplit.get(i).getAsset().getId() == amount.getAsset_id()) {
							index = i;
							break;
						}
					}
					if (index < 0) {
						AmountAndAccounts item = new AmountAndAccounts();
						item.setAmount(amount.getValue());
						item.setAsset(getAssetById(amount.getAsset_id()));
						item.setSplit(new ArrayList<AccountAndAmount>());
						item.getSplit().add(s);
						this.myAssetSplit.add(item);
					} else {
						AmountAndAccounts item = this.myAssetSplit.get(index);
						item.setAmount(item.getAmount() + amount.getValue());
						item.getSplit().add(s);
					}
				}
			}
		}
		for (Account account : myAccounts) {
			AccountAndAmounts item = new AccountAndAmounts();
			item.setAccount(account);
			item.setAmounts(new ArrayList<Amount>());
			boolean flag = false;
			for (WalletAccountBalance.Result r : results) {
				if (r.getName().equals(account.getName())) {
					for (Amount amount : r.getAmounts())
						item.getAmounts().add(amount);
					flag = true;
					break;
				}
			}
			if (!flag) {
				Amount amount = new Amount();
				amount.setAsset_id(0);
				amount.setValue(0);
				item.getAmounts().add(amount);
			}
			this.myAccountSplit.add(item);
		}
    }
    
    public void redoTransactions() throws BTSSystemException {
    	this.transactions.clear();
    	this.transactions.addAll(getTransactions(WalletAccountTransactionHistory.run("", "", 0)));
    }
    
    public void redoRecentMarkets() throws BTSSystemException {
    	this.recentMarkets.clear();
    	
    	WalletGetSetting.Result result = WalletGetSetting.run("recent_markets");
    	if (result != null) {
			String value = WalletGetSetting.run("recent_markets").getValue();
			String[] values = value.substring(1, value.length() - 1).split(",");
			for (String item : values) {
				String[] items = item.substring(1, item.length() - 1).split(":");
				Asset quoteAsset = getAssetBySymbol(trimSymbol(items[1]));
				Asset baseAsset = getAssetBySymbol(trimSymbol(items[0]));
				if (quoteAsset != null && baseAsset != null) {
					Market market = getMarket(quoteAsset, baseAsset);
					if (market != null)
						this.recentMarkets.add(market);
				}
			}
    	}
    }
    
    
//	private static boolean isBitAsset(Asset asset) {
//		return (asset.getIssuer_account_id() == -2);
//	}
    
    private static void setDefaultColorSet(String name) {
    	new Thread(() -> {
			try {
				WalletSetSetting.run(COLOR_SET_KEY, name);
			} catch (BTSSystemException e) {
			}
		}).start();
    }
    
    private static String getDefaultColorSet() {
    	WalletGetSetting.Result result;
		try {
			result = WalletGetSetting.run(COLOR_SET_KEY);
		} catch (BTSSystemException e) {
			return DEFAULT_COLOR_SET;
		}
		if (result != null && result.getValue() != null && !result.getValue().isEmpty())
			return result.getValue();
		return DEFAULT_COLOR_SET;
	}
	
    private static String trimSymbol(String symbol) {
		 if (symbol.length() > 3 && symbol.substring(0, 3).equalsIgnoreCase("bit"))
			 return symbol.substring(3);
		return symbol;
	}
    
	private static Market getMarket(Asset quoteAsset, Asset baseAsset) {
		Market market = null;
		try {
			market = BlockchainMarketStatus.run(quoteAsset.getSymbol(), baseAsset.getSymbol());
    	} catch (BTSSystemException e) {
			return null;
		}
		return market;
	}
	
	public static List<Transaction> getTransactions(List<WalletAccountTransactionHistory.Result> results) {
		List<Transaction> transactions = new ArrayList<Transaction>();
		for (WalletAccountTransactionHistory.Result i : results) {
			for (LedgerEntry le : i.getLedger_entries()) {
				Transaction t = new Transaction();
				t.setIs_virtual(i.isIs_virtual());
				t.setIs_confirmed(i.isIs_confirmed());
				t.setIs_market(i.isIs_market());
				t.setIs_market_cancel(i.isIs_market_cancel());
				t.setTrx_id(i.getTrx_id());
				t.setBlock_num(i.getBlock_num());
				t.setFee(i.getFee());
				t.setTimestamp(i.getTimestamp());
				t.setExpiration_timestamp(i.getExpiration_timestamp());
				t.setFrom_account(le.getFrom_account());
				t.setTo_account(le.getTo_account());
				t.setAmount(le.getAmount());
				t.setMemo(le.getMemo());
				
				Account fromAccount = getInstance().getAccountByName(le.getFrom_account());
				Account toAccount = getInstance().getAccountByName(le.getTo_account());
				
				if ((toAccount != null && toAccount.isIs_my_account()) && (fromAccount == null || !fromAccount.isIs_my_account()))
					t.setDirection(1);
				else if ((fromAccount != null && fromAccount.isIs_my_account()) && (toAccount == null || !toAccount.isIs_my_account()))
					t.setDirection(-1);
				else if ((fromAccount != null && fromAccount.isIs_my_account()) && (toAccount != null && toAccount.isIs_my_account()))
					t.setDirection(0);
				
				t.setRunning_balances(new ArrayList<Transaction.RunningBalance>());
				for (WalletAccountTransactionHistory.RunningBalance rb : le.getRunning_balances()) {
					Transaction.RunningBalance e = new Transaction.RunningBalance();
					e.setAmount(rb.getAmount());
					e.setName(rb.getName());
					t.getRunning_balances().add(e);
				}
				transactions.add(t);
			}
		}
		Collections.sort(transactions);
		return transactions;
	}
	
	/**********************************************************************/
    
    
    private Double currentShareSupply;
    public Double getCurrentShareSupply() {
    	return currentShareSupply;
    }
    
    
    private List<Asset> assets;
    private List<Integer> assetsIdIndex;
    private List<String> assetsSymbolIndex;
	public List<Asset> getAssets() {
		return assets;
	}
	
	
	private List<Account> accounts;
	private List<String> accountsIndex;
	public List<Account> getAccounts() {
		return accounts;
	}
	
	
	private List<AmountAndAccounts> myAssetSplit;
	public List<AmountAndAccounts> getMyAssetSplit() {
		return myAssetSplit;
	}
	
	
	private List<AccountAndAmounts> myAccountSplit;
	public List<AccountAndAmounts> getMyAccountSplit() {
		return myAccountSplit;
	}
	
	
	private List<Account> myAccounts;
	public List<Account> getMyAccounts() {
		return myAccounts;
	}
	
	
	private List<Account> favoriteAccounts;
	public List<Account> getFavoriteAccounts() {
		return favoriteAccounts;
	}
	
	
	private List<Account> activeDelegates;
	private List<String> activeDelegatesIndex;
	public List<Account> getActiveDelegates() {
		return activeDelegates;
	}
	
	
	private List<Market> recentMarkets;
	public List<Market> getRecentMarkets() {
		return recentMarkets;
	}
	
	
	private List<Market> btsMarkets;
	public List<Market> getBtsMarkets() {
		return btsMarkets;
	}
	
	
	private List<Transaction> transactions;
	public List<Transaction> getTransactions() {
		return transactions;
	}
	
	
	private List<DelegateAnnouncement> delegateAnnouncements;
	public List<DelegateAnnouncement> getDelegateAnnouncements() {
		return delegateAnnouncements;
	}
	
	private List<MarketNews> marketNews;
	public List<MarketNews> getMarketNews() {
		return marketNews;
	}

	
//    private Map<String, Image> avatars = new HashMap<String, Image>();
//    public Image getAvatarImage(String name) {
//    	if (avatars.containsKey(name))
//    		return avatars.get(name);
//    	Image image;
//    	if (AVATAR_GENERATOR != null) {
//	    	String url = String.format("%s/%s", AVATAR_GENERATOR, name);
//	    	image = new Image(url, true);
//    	} else {
//    		image = new Image("/styles/robots/power.png");
//    	}
//    	avatars.put(name, image);
//    	return image;
//    }
    
    
//    private List<AssetPair> allMarkets;
//    public List<AssetPair> getAllMarkets() {
//    	if (allMarkets == null) {
//	    	allMarkets = new ArrayList<AssetPair>(assets.size() * assets.size());
//	    	for (Asset asset1 : assets) {
//	    		for (Asset asset2 : assets) {
//	    			//if (asset1.getId() > asset2.getId())
//	    				allMarkets.add(new AssetPair(asset1, asset2));
//	               	//else if (asset2.getId() > asset1.getId())
//	               	//	allMarkets.add(new AssetPair(asset2, asset1));
//	    		}
//	    	}
//    	}
//    	return allMarkets;
//    }
	
	
    public Asset getAssetById(int id) {
    	int index = assetsIdIndex.indexOf(id);
    	if (index >= 0)
    		return assets.get(index);
    	return null;
    }
    
    public Asset getAssetBySymbol(String symbol) {
    	int index = assetsSymbolIndex.indexOf(symbol);
    	if (index >= 0)
    		return assets.get(index);
    	return null;
    }
    
    public Account getAccountByName(String name) {
    	int index = accountsIndex.indexOf(name);
    	if (index >= 0)
    		return accounts.get(index);
    	return null;
    }
    
    public int getAccountApproval(String name) {
    	Account account = getAccountByName(name);
    	if (account != null)
    		return account.getApproved();
    	return 0;
    }
    
    public boolean isAccountFavorite(String name) {
		Account account = getAccountByName(name);
    	if (account != null)
    		return account.isIs_favorite();
    	return false;
	}
    
    public boolean isAccountUnregistered(Account account) {
    	return Time.isUndefined(account.getRegistration_date());
    }
    
    public Account getActiveDelegateByName(String name) {
    	int index = activeDelegatesIndex.indexOf(name);
    	if (index >= 0)
    		return activeDelegates.get(index);
    	return null;
    }
    
    public Asset getAssetFromList(Asset asset, List<Asset> list) {
    	for (Asset item : list) {
    		if (item.getSymbol().equals(asset.getSymbol()))
    			return item;
    	}
    	return null;
    }
    
    public Account getAccountFromList(Account account, List<Account> list) {
    	for (Account item : list) {
    		if (item.getName().equals(account.getName()))
    			return item;
    	}
    	return null;
    }
    
    public boolean isMarketPeggedAsset(Asset asset) {
		return (asset.getIssuer_account_id() <= 0);
	}
    
    public boolean isDelegate(RegisteredName registeredName) {
		return registeredName.getDelegate_info() != null;
	}
    
    public boolean isActiveDelegate(RegisteredName registeredName) {
    	return (getActiveDelegateByName(registeredName.getName()) != null);
    }
    
    public double getDelegateSupport(RegisteredName item) {
		return item.getDelegate_info().getVotes_for() / getCurrentShareSupply();
	}
	
    public double getDelegateReliability(RegisteredName item) {
		return item.getDelegate_info().getBlocks_produced() / (double) (item.getDelegate_info().getBlocks_produced() + item.getDelegate_info().getBlocks_missed());
	}
    
    public String getAccountApprovalLabel(RegisteredName item) {
		return getAccountApprovalLabel(getAccountApproval(item.getName()));
	}
	
    public String getAccountApprovalLabel(int approval) {
		if (approval == 1)
			return "positive";
		if (approval == -1)
			return "negative";
		return "neutral";
	}
    
    public String getOrderTypeLabel(String bidType, String askType) {
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
    
    public String getVoteMethodLabel(VoteMethod voteMethod) {
		if (voteMethod.equals(VoteMethod.ALL))
			return "all";
		if (voteMethod.equals(VoteMethod.NONE))
			return "none";
		if (voteMethod.equals(VoteMethod.RANDOM))
			return "random";
		if (voteMethod.equals(VoteMethod.RECOMMENDED))
			return "recommended";
		return null;
	}
    
    public String getMarketLabel(Market market) {
    	Asset quoteAsset = getAssetById(market.getQuote_id());
		Asset baseAsset = getAssetById(market.getBase_id());
		return getMarketLabel(quoteAsset, baseAsset);
    }
    
    public String getMarketLabel(Asset quoteAsset, Asset baseAsset) {
    	return String.format("%s:%s", baseAsset.getSymbol(), quoteAsset.getSymbol());
    }
    
    public String[] getAmountPair(int assetId, long value) {
    	String[] list = new String[2];
    	Asset asset = getAssetById(assetId);
    	list[0] = asset.getSymbol();
    	list[1] = getAmount(asset, value);
    	return list;
    }
    
    public String[] getAmountPair(Amount amount) {
    	return getAmountPair(amount.getAsset_id(), amount.getValue());
    }
    
    public String getAmount(Amount amount) {
    	return getAmount(amount.getAsset_id(), amount.getValue());
    }
    
    public String getAmount(Asset asset, Double volume) {
    	long amount = (long) (volume * asset.getPrecision());
    	return getAmount(asset, amount);
    }
    
    public String getAmount(Asset asset, long value) {
    	return getAmount(asset, value, asset.getPrecision());
    }
    
    public String getAmount(int assetId, long value) {
    	Asset asset = getAssetById(assetId);
    	return getAmount(asset, value);
    }
    
    public String getAmount(int assetId, long value, int precision) {
    	Asset asset = getAssetById(assetId);
    	return getAmount(asset, value, precision);
    }
    
    public String getAmount(Asset asset, long value, int precision) {
    	return format(getRealValue(asset, value), precision);
    }
    
    public String format(double realValue, int precision) {
    	return String.format("%,." + String.format("%d", (int) Math.log10(precision)) + "f", realValue);
    }
    
    public double getRealValue(int assetId, long value) {
    	Asset asset = getAssetById(assetId);
    	return getRealValue(asset, value);
    }
    
    public double getRealValue(Asset asset, long value) {
    	return ((double) value) / asset.getPrecision();
    }
    
    public double getRealPrice(Price price) {
    	return price.getRatio() * ((double) getAssetById(price.getBase_asset_id()).getPrecision() / getAssetById(price.getQuote_asset_id()).getPrecision());
    }
    
    public boolean isVirtualName(String name) {
    	for (String item : VIRTUAL_NAMES)
    		if (name.startsWith(item))
    			return true;
    	return false;
    }
	
}
