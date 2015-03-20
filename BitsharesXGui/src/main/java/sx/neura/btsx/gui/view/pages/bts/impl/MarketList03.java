package sx.neura.btsx.gui.view.pages.bts.impl;

public class MarketList03 extends MarketList {
	
	private static MarketList03 instance;
	public static MarketList03 getInstance() {
		if (!isInstance())
			instance = new MarketList03();
		return instance;
	}
	public static boolean isInstance() {
		return (instance != null);
	}
	private MarketList03() {
	}
	
	@Override
	public String getName() {
		return "Market Slot 03";
	}
}
