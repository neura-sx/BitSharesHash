package sx.neura.bts.json.api.blockchain;

import java.util.List;

import sx.neura.bts.json.CommandJson;
import sx.neura.bts.json.dto.MarketFeed;
import sx.neura.bts.json.exceptions.BTSSystemException;

public class BlockchainGetFeedsFromDelegate extends CommandJson {

	private List<MarketFeed> results;

	public void setResult(List<MarketFeed> results) {
		this.results = results;
	}
	
	public List<MarketFeed> getResult() {
		return results;
	}
	
	private static final String COMMAND = "blockchain_get_feeds_from_delegate";
	
	public static CommandJson test(String delegateName) {
		return doTest(COMMAND, new Object[] { delegateName });
	}
	
	public static List<MarketFeed> run(String delegateName) throws BTSSystemException {
		return run(0, delegateName);
	}
	public static List<MarketFeed> run(int id, String delegateName) throws BTSSystemException {
		BlockchainGetFeedsFromDelegate i = (BlockchainGetFeedsFromDelegate) new BlockchainGetFeedsFromDelegate().doRun(
				id, COMMAND, new Object[] { delegateName });
		i.checkSystemException();
		return i.getResult();
	}
	
	public static void main(String[] args) {
//		test("x.ebit");
		try {
			List<MarketFeed> list = run("x.ebit");
			for (MarketFeed result : list) {
				System.out.println(String.format("[%50s]: %5s %8.4f, %8.4f %s",
						result.getDelegate_name(),
						result.getAsset_symbol(),
						result.getPrice(),
						result.getMedian_price(),
						result.getLast_update()));
			}
		} catch (BTSSystemException e) {
			System.err.println(e.getError().getMessage());
		}
	}

	@Override
	protected String hack(Object[] params) {
		return "{\"id\":0,\"result\":[{\"delegate_name\":\"x.ebit\",\"price\":0.02058843639725488,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"AUD\",\"median_price\":0.020609456849080381},{\"delegate_name\":\"x.ebit\",\"price\":5.1508719112572999e-005,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"BTC\",\"median_price\":5.1441499999999999e-005},{\"delegate_name\":\"x.ebit\",\"price\":0.019440043708293019,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"CAD\",\"median_price\":0.019459891670244631},{\"delegate_name\":\"x.ebit\",\"price\":0.01652019768405141,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"CHF\",\"median_price\":0.016537064531677179},{\"delegate_name\":\"x.ebit\",\"price\":0.10413837014095492,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"CNY\",\"median_price\":0.1042446936883335},{\"delegate_name\":\"x.ebit\",\"price\":0.013702056543374501,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"EUR\",\"median_price\":0.01371604611567241},{\"delegate_name\":\"x.ebit\",\"price\":0.01074677201099615,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"GBP\",\"median_price\":0.01075774428683964},{\"delegate_name\":\"x.ebit\",\"price\":1.398151108089e-005,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"GOLD\",\"median_price\":1.3995785971633e-005},{\"delegate_name\":\"x.ebit\",\"price\":0.12975127104529643,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"HKD\",\"median_price\":0.12988374493936392},{\"delegate_name\":\"x.ebit\",\"price\":2.0142818209082187,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"JPY\",\"median_price\":2.0163383692134129},{\"delegate_name\":\"x.ebit\",\"price\":18.596137525170523,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"KRW\",\"median_price\":18.615123872916694},{\"delegate_name\":\"x.ebit\",\"price\":0.24607365345216195,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"MXN\",\"median_price\":0.24632489056789572},{\"delegate_name\":\"x.ebit\",\"price\":0.021543343912980181,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"NZD\",\"median_price\":0.021565339309529252},{\"delegate_name\":\"x.ebit\",\"price\":0.85359319787667964,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"RUB\",\"median_price\":0.85446470236338923},{\"delegate_name\":\"x.ebit\",\"price\":0.13109059685417282,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"SEK\",\"median_price\":0.13122443817766047},{\"delegate_name\":\"x.ebit\",\"price\":0.022136847169813768,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"SGD\",\"median_price\":0.02215944852333683},{\"delegate_name\":\"x.ebit\",\"price\":0.0010377226160202199,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"SILVER\",\"median_price\":0.0010387821135866},{\"delegate_name\":\"x.ebit\",\"price\":0.038746277538771037,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"TRY\",\"median_price\":0.038785836845010031},{\"delegate_name\":\"x.ebit\",\"price\":0.01671992327739948,\"last_update\":\"2014-12-27T03:24:50\",\"asset_symbol\":\"USD\",\"median_price\":0.016736994041540922}]}";
	}
}
