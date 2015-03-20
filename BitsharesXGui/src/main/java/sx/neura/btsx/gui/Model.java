package sx.neura.btsx.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;
import sx.neura.bts.json.dto.Account;
import sx.neura.bts.json.dto.Amount;
import sx.neura.bts.json.dto.Asset;
import sx.neura.bts.json.dto.Price;
import sx.neura.bts.json.dto.RegisteredName;
import sx.neura.btsx.gui.dto.AssetPair;

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
    
    public static final int WALLET_UNLOCK_TIMEOUT = 10000;
    public static final int REGISTERED_NAMES_LIMIT = 10000;
    public static final int REGISTERED_NAMES_LIMIT_ALL = 100000;
    public static final int DELEGATES_ACTIVE_LIMIT = 101;
    public static final int DELEGATES_ALL_LIMIT = 201;
    public static final int BLOCKS_LIMIT = 1000;
    
    private static final String AVATAR_GENERATOR = "http://robohash.org";
    
    private static final String[] VIRTUAL_NAMES = new String[] {"UNKNOWN", "NETWORK", "ASK-", "BID-", "SHORT-", "MARGIN-", "MARKET"};
    
    private Double currentShareSupply;
    public Double getCurrentShareSupply() {
    	if (currentShareSupply == null)
    		currentShareSupply = new Double(getAssetById(0).getCurrent_share_supply());
    	return currentShareSupply;
    }
    
    private List<Asset> assets;
    private List<Integer> assetsIdIndex;
    private List<String> assetsSymbolIndex;
	public List<Asset> getAssets() {
		return assets;
	}
	public void setAssets(List<Asset> assets) {
		this.assets = assets;
		assetsIdIndex = new ArrayList<Integer>();
		assetsSymbolIndex = new ArrayList<String>();
		for (Asset asset : assets) {
			assetsIdIndex.add(asset.getId());
			assetsSymbolIndex.add(asset.getSymbol());
		}
	}
	
	private List<Account> accounts;
	private List<String> accountsIndex;
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
		accountsIndex = new ArrayList<String>();
		myAccounts = new ArrayList<Account>();
		favoriteAccounts = new ArrayList<Account>();
		for (Account account : accounts) {
			accountsIndex.add(account.getName());
			if (account.isIs_my_account())
				myAccounts.add(account);
			if (account.isIs_favorite())
				favoriteAccounts.add(account);
		}
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
	public void setActiveDelegates(List<Account> activeDelegates) {
		this.activeDelegates = activeDelegates;
		activeDelegatesIndex = new ArrayList<String>();
		for (Account account : activeDelegates) {
			activeDelegatesIndex.add(account.getName());
		}
	}
	
	private List<AssetPair> recentMarkets;
	public List<AssetPair> getRecentMarkets() {
		return recentMarkets;
	}
	public void setRecentMarkets(List<AssetPair> recentMarkets) {
		this.recentMarkets = recentMarkets;
	}
	
	private List<AssetPair> btsMarkets;
	public List<AssetPair> getBtsMarkets() {
		return btsMarkets;
	}
	public void setBtsMarkets(List<AssetPair> btsMarkets) {
		this.btsMarkets = btsMarkets;
	}
	
	private List<AssetPair> bitAssetMarkets;
	public List<AssetPair> getBitAssetMarkets() {
		return bitAssetMarkets;
	}
	public void setBitAssetMarkets(List<AssetPair> bitAssetMarkets) {
		this.bitAssetMarkets = bitAssetMarkets;
	}
	
	private List<AssetPair> userAssetMarkets;
	public List<AssetPair> getUserAssetMarkets() {
		return userAssetMarkets;
	}
	public void setUserAssetMarkets(List<AssetPair> userAssetMarkets) {
		this.userAssetMarkets = userAssetMarkets;
	}
	
//	private List<WalletAccountTransactionHistory.Result> transactions;
//	public List<WalletAccountTransactionHistory.Result> getTransactions() {
//		return transactions;
//	}
//	public void setTransactions(List<WalletAccountTransactionHistory.Result> transactions) {
//		this.transactions = transactions;
//	}
	
//    public Asset getAssetById(int id) {
//    	for (Asset item : assets)
//    		if (item.getId() == id)
//    			return item;
//    	return null;
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
    
    public Asset getAssetFromList(Asset asset, List<Asset> list) {
    	for (Asset item : list) {
    		if (item.getSymbol().equals(asset.getSymbol()))
    			return item;
    	}
    	return null;
    }
    
    public Account getAccountByName(String name) {
    	int index = accountsIndex.indexOf(name);
    	if (index >= 0)
    		return accounts.get(index);
    	return null;
    }
    
    public Account getAccountFromList(Account account, List<Account> list) {
    	for (Account item : list) {
    		if (item.getName().equals(account.getName()))
    			return item;
    	}
    	list.add(account);
    	return account;
    }
    
    public int getAccountApproval(String name) {
    	int index = accountsIndex.indexOf(name);
    	if (index >= 0)
    		return accounts.get(index).getApproved();
    	return 0;
    }
    
    public int getAccountApproval(RegisteredName item) {
		return getAccountApproval(item.getName());
	}
    
    private Account getActiveDelegateByName(String name) {
    	int index = activeDelegatesIndex.indexOf(name);
    	if (index >= 0)
    		return activeDelegates.get(index);
    	return null;
    }
    
    public boolean isDelegate(RegisteredName registeredName) {
		return registeredName.getDelegate_info() != null;
	}
    
    public boolean isActiveDelegate(RegisteredName registeredName) {
    	return getActiveDelegateByName(registeredName.getName()) != null;
    }
    
    public double getDelegateSupport(RegisteredName item) {
		return item.getDelegate_info().getVotes_for() / getCurrentShareSupply();
	}
	
    public double getDelegateReliability(RegisteredName item) {
		return item.getDelegate_info().getBlocks_produced() / (double) (item.getDelegate_info().getBlocks_produced() + item.getDelegate_info().getBlocks_missed());
	}
    
    public String getAccountApprovalLabel(RegisteredName item) {
		return getAccountApprovalLabel(getAccountApproval(item));
	}
	
    public String getAccountApprovalLabel(int approval) {
		if (approval == 1)
			return "positive";
		if (approval == -1)
			return "negative";
		return "neutral";
	}
    
    public String[] getAmountPair(Amount amount) {
    	String[] list = new String[2];
    	Asset asset = getAssetById(amount.getAsset_id());
    	list[0] = asset.getSymbol();
    	list[1] = getAmount(asset, amount.getValue());
    	return list;
    }
    
    public String getAmount(Amount amount) {
    	return getAmount(amount.getAsset_id(), amount.getValue());
    }
    
    public String getAmount(Asset asset, Double volume) {
    	long amount = (long) (volume * asset.getPrecision());
    	return getAmount(asset, amount);
    }
    
    public String getAmount(Asset asset, long amount) {
    	return getAmount(asset, amount, asset.getPrecision());
    }
    
    public String getAmount(int assetId, long amount) {
    	Asset asset = getAssetById(assetId);
    	return getAmount(asset, amount);
    }
    
    public String getAmount(int assetId, long amount, int precision) {
    	Asset asset = getAssetById(assetId);
    	return getAmount(asset, amount, precision);
    }
    
    public String getAmount(Asset asset, long amount, int precision) {
    	return format(getRealValue(asset, amount), precision);
    }
    
    public String format(double realValue, int precision) {
    	return String.format("%,." + String.format("%d", (int) Math.log10(precision)) + "f", realValue);
    }
    
    public double getRealValue(int assetId, long amount) {
    	Asset asset = getAssetById(assetId);
    	return getRealValue(asset, amount);
    }
    
    public double getRealValue(Asset asset, long amount) {
    	return ((double) amount) / asset.getPrecision();
    }
    
    public double getRealPrice(Price price) {
    	return price.getRatio() * (getAssetById(price.getBase_asset_id()).getPrecision() / getAssetById(price.getQuote_asset_id()).getPrecision());
    }
    
    public Image getAvatarImage(String name) {
    	String url = String.format("%s/%s", AVATAR_GENERATOR, name);
    	return new Image(url, true);
    }
    
//    public Image getSymbolImage(String name) {
//    	Image image = null;
//    	try {
//    		image = new Image(String.format("/styles/symbols/%s.png", name), true);
//    	} catch (IllegalArgumentException e) {
//    		
//    	}
//    	return image;
//    }
    
    private List<AssetPair> allMarkets;
    public List<AssetPair> getAllMarkets() {
    	if (allMarkets == null) {
	    	allMarkets = new ArrayList<AssetPair>(assets.size() * assets.size());
	    	for (Asset asset1 : assets) {
	    		for (Asset asset2 : assets) {
	    			//if (asset1.getId() > asset2.getId())
	    				allMarkets.add(new AssetPair(asset1, asset2));
	               	//else if (asset2.getId() > asset1.getId())
	               	//	allMarkets.add(new AssetPair(asset2, asset1));
	    		}
	    	}
    	}
    	return allMarkets;
    }
    
    public static boolean isVirtualName(String name) {
    	for (String item : VIRTUAL_NAMES)
    		if (name.startsWith(item))
    			return true;
    	return false;
    }
	
}
