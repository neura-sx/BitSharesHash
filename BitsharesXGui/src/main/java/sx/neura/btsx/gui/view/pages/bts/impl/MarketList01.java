package sx.neura.btsx.gui.view.pages.bts.impl;

public class MarketList01 extends MarketList {
	
	private static MarketList01 instance;
	public static MarketList01 getInstance() {
		if (!isInstance())
			instance = new MarketList01();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private MarketList01() {
	}
	
	@Override
	public String getName() {
		return "Market Slot 01";
	}
}
