package test.neura.bts.gui.util;

import java.time.LocalDateTime;

import sx.neura.bts.util.Time;
import sx.neura.bts.util.Util;



public class Tester {

//	public static void main(String[] args) {
//		int i = 1221381234;
//		int p = 1000000;
//		double f = (double) i / p;
//		System.out.println(String.format("%,." + String.format("%d", (int) Math.log10(p)) + "f", f));
//		System.out.println();
//	}
	
//	public static void main(String[] args) {
//		String x = "{type=ask_order, market_index={order_price={ratio=0.002564102564102564, quote_asset_id=22, base_asset_id=0}, owner=BTSX8Q73m6CMd25bDfqP4BnuyreY7UeDRKRVk}, state={balance=1170000, short_price_limit=null, last_update=20141020T144630}, collateral=null, expiration=null}";
//		x = x.replaceAll("\\{", "\\{\"");
//		x = x.replaceAll("\\}", "\\}\"");
//		x = x.replaceAll("\\=", "\"\\:\"");
//		x = x.replaceAll("\\, ", "\"\\,\"");
//		
//		//String s = "{\"type\":\"bid_order\", \"market_index\":{\"order_price\":{\"ratio\":\"0.002114164904862579\", \"quote_asset_id\":22, \"base_asset_id\":0}, \"owner\":\"BTSX4u5qZywWybF4gXK7viHkNwVZU66mUamJq\"}, \"state\":{\"balance\":29499, \"short_price_limit\":null, \"last_update\":\"20141020T151310\"}, \"collateral\":null, \"expiration\":null}";
//		
//		ObjectMapper mapper = new ObjectMapper();
//		mapper.configure(
//				DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
//				.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//				.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
//				.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
//		MarketOrder mo = null;
//		try {
//			mo = mapper.readValue(x, MarketOrder.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(mo);
//	}
	
//	public static void main(String[] args) {
//		//String s = "\"public_data\":\"Giving back to the bitshares community with a 0% pay rate.\",\"owner_key\":\"BTSX7nUkxXPrCqzBZp9WE9ADAfBW4a8D6H4eSAgzbMXeVyzKTST5NL\"";
//		//String s = "\"public_data\":[],\"owner_key\":\"BTSX8HEnCGY1KaRtYmrBMmUg9SKEoG8uZwg1DdhzNvx8dGiLJtpU2c\"";
//		//String s = "<pre>Giving back to the bitshares community</pre>";
//		String s = "{\"id\":0,\"result\":[{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.000862068965517241\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSXMyY8TYrGZYYJqaHNg9c1LCoWijEZR9sgt\"},\"state\":{\"balance\":438470756655,\"short_price_limit\":null,\"last_update\":\"20141018T221020\"},\"collateral\":null,\"expiration\":null},{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.000869565217391304\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSXNLqs5cS33r8FSwLDDbbjo9Vjk5TFhwBAL\"},\"state\":{\"balance\":575000000,\"short_price_limit\":{\"ratio\":\"0.0025\",\"quote_asset_id\":22,\"base_asset_id\":0},\"last_update\":\"20141018T213340\"},\"collateral\":null,\"expiration\":null},{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.000876347384103058\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSXrRDqkASk8vYvVjeRek8Ts3G4LHRBevxb\"},\"state\":{\"balance\":2105342053,\"short_price_limit\":null,\"last_update\":\"20141018T190430\"},\"collateral\":null,\"expiration\":null},{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.000876347384103058\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSX2E9ijgjPcPg6bxByKu9VYNZBuWm7diCXr\"},\"state\":{\"balance\":102696734917,\"short_price_limit\":null,\"last_update\":\"20141018T172150\"},\"collateral\":null,\"expiration\":null},{\"type\":\"short_order\",\"market_index\":{\"order_price\":{\"ratio\":\"0.000877185287848352\",\"quote_asset_id\":22,\"base_asset_id\":0},\"owner\":\"BTSX8wcvgJwMXRH5xPE6825G3A8s4GmShvoT7\"},\"state\":{\"balance\":22800200000,\"short_price_limit\":null,\"last_update\":\"20141018T164510\"},\"collateral\":null,\"expiration\":null}]}";
//		System.out.println(s);
//		//s = s.replaceAll("\"public_data\":\\[\\]", "\"public_data\":null");
//		//s = s.replaceAll("\"public_data\":\"(.*?)\"", "\"public_data\":null");
//		s = s.replaceAll("\"short_price_limit\":null", "\"short_price_limit\":0");
//		//s = s.replaceAll("<pre>(.*?)</pre>", "<pre>null</pre>");
//		System.out.println(s);
//	}
	
//	public static void main(String[] args) {
//		
//		int SIZE = 998;
//		int ITEMS_PER_PAGE = 100;
//		
//		int numberOfPages = (SIZE / ITEMS_PER_PAGE) + (SIZE % ITEMS_PER_PAGE == 0 ? 0 : 1);
//		System.out.println(numberOfPages);
//	}
	
//	public static void main(String[] args) {
//		//int[] xxx = new int[]{34,4234,43,6532,345,5};
//		{
//			List<Integer> list = new ArrayList<Integer>(Arrays.asList(34, 4234, 43, 6532, 43, 345, 5));
//			int index = list.indexOf(43);
//			System.out.println(index);
//		}
//		{
//			List<String> list = new ArrayList<String>(Arrays.asList("aaa", "xxxw", "exxx", "sdfgd"));
//			int index = list.indexOf("xxx");
//			System.out.println(index);
//		}
//	}
	
	public static void main(String[] args) {
		//LocalDateTime c = LocalDateTime.ofEpochSecond(System.currentTimeMillis() / 1000 , 0, ZoneOffset.UTC);
		//System.out.println(Time.format(c));
		
		LocalDateTime t = LocalDateTime.now();
		LocalDateTime c = LocalDateTime.of(t.getYear(), t.getMonth(), t.getDayOfMonth(), 0, 0);
		System.out.println(Time.format(c));
		
		LocalDateTime p = c.minusDays(1);
		System.out.println(Time.format(p));
		
		System.out.println(Util.c("jakub", "ki"));
		
		//System.out.println(Time.encode(p));
		//System.out.println(t.toEpochSecond(ZoneOffset.UTC) - p.toEpochSecond(ZoneOffset.UTC));
		
		//LocalDateTime t = LocalDateTime.of(2014, Month.OCTOBER, 17, 14, 28, 19);
		//System.out.println(t.format(BASIC_ISO_LOCAL_DATE_TIME));
		//String s = "20141017T142819";
		//LocalDateTime ldt2 = LocalDateTime.parse(s, BASIC_ISO_LOCAL_DATE_TIME);
		//System.out.println(ldt2.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
	}
	
	
//	public static void main(String[] args) {
//		final int numberOfDaysBack = 10;
//		final int numberOfMinutesPeriod = 60;
//		
//		LocalDateTime n = LocalDateTime.ofEpochSecond(System.currentTimeMillis() / 1000, 0, ZoneOffset.UTC);
//    	LocalDateTime p = n.minusDays(numberOfDaysBack);
//    	LocalDateTime t = p.truncatedTo(ChronoUnit.DAYS);
//    	long duration = n.toEpochSecond(ZoneOffset.UTC) - t.toEpochSecond(ZoneOffset.UTC);
//    	
//    	System.out.println(Time.format(n));
//    	System.out.println(Time.format(p));
//    	System.out.println(Time.format(t));
//    	System.out.println(duration);
//    	
//		try {
//			Double level = BlockchainMarketPriceHistory.run("USD", "BTSX", Time.encode(t.minusDays(1L)), duration, HistoryGranularity.EACH_DAY).get(0).getClosing_price();
//			List<BlockchainMarketPriceHistory.Result> list = BlockchainMarketPriceHistory.run("USD", "BTSX", Time.encode(t), duration, HistoryGranularity.EACH_BLOCK);
//			
//			LocalDateTime u = t.plusMinutes(numberOfMinutesPeriod);
//			List<PriceCandle> candles = new ArrayList<PriceCandle>();
//			List<BlockchainMarketPriceHistory.Result> temp = new ArrayList<BlockchainMarketPriceHistory.Result>();
//			
//			for (BlockchainMarketPriceHistory.Result result : list) {
//				LocalDateTime x = Time.decode(result.getTimestamp());
//				while (!x.isBefore(u)) {
//					System.out.println(String.format("-----%s-----", Time.format(u)));
//					PriceCandle candle = (temp.isEmpty() ? new PriceCandle(u, level) : new PriceCandle(u, temp));
//					candles.add(candle);
//					level = candle.getClose();
//					u = u.plusMinutes(numberOfMinutesPeriod);
//					temp.clear();
//				}
//				temp.add(result);
//				System.out.println(String.format("[%s]: %12.8f %12.8f %12.8f %12.8f %12.8f %d",
//						result.getTimestamp(),
//						result.getOpening_price(),
//						result.getClosing_price(),
//						result.getHighest_bid(),
//						result.getLowest_ask(),
//						result.getRecent_average_price(),
//						result.getVolume()));
//			}	
//			System.out.println(String.format("-----%s-----", Time.format(u)));
//			candles.add(temp.isEmpty() ? new PriceCandle(u, level) : new PriceCandle(u, temp));
//			System.out.println();
//			for (PriceCandle candle : candles)
//				System.out.println(candle);
//			
//		} catch (BTSXSystemException e) {
//			System.err.println(e.getError().getMessage());
//		}
//	}
	
//	public static void main(String[] args) {
//		System.out.println(new String("BTSX5acEdizrzTSHsHENUyFip5Pf61MSaFLpMN6YF455sMeRWR1s26").length());
//		System.out.println(getNameForPublicKey("BTSX5acEdizrzTSHsHENUyFip5Pf61MSaFLpMN6YF455sMeRWR1s26"));
//		System.out.println(String.format("%s%d", "temp", new String("BTSX7p9rHkT8TZ9NfQaRgbaAeVLZFqW1E33r62PPayU5ezhDxceEbW").hashCode()));
//	}
//	private static final int PUBLIC_KEY_SUBSTRING_SIZE = 8;
//	private static String getNameForPublicKey(String publicKey) {
//		return String.format("%s-%s",
//				publicKey.substring(0, PUBLIC_KEY_SUBSTRING_SIZE),
//				publicKey.substring(publicKey.length() - PUBLIC_KEY_SUBSTRING_SIZE, publicKey.length())).toLowerCase();
//	}
}
