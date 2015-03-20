package sx.neura.bts.json.dto;

import sx.neura.bts.json.dto.MarketIndex;
import sx.neura.bts.json.dto.MarketState;

public class MarketOrder {
	private String type;
	private MarketIndex market_index;
	private MarketState state;
	private long collateral;
	private Price interest_rate;
	private String expiration;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public MarketIndex getMarket_index() {
		return market_index;
	}
	public void setMarket_index(MarketIndex market_index) {
		this.market_index = market_index;
	}
	public MarketState getState() {
		return state;
	}
	public void setState(MarketState state) {
		this.state = state;
	}
	public long getCollateral() {
		return collateral;
	}
	public void setCollateral(long collateral) {
		this.collateral = collateral;
	}
	public Price getInterest_rate() {
		return interest_rate;
	}
	public void setInterest_rate(Price interest_rate) {
		this.interest_rate = interest_rate;
	}
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
}
