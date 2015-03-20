package sx.neura.bts.gui.dto;

import sx.neura.bts.json.dto.MarketOrder;

public class CumulativeMarketOrder implements Comparable<CumulativeMarketOrder> {
	public CumulativeMarketOrder(MarketOrder order, Long cumulative) {
		this.order = order;
		this.cumulative = cumulative;
	}
	private MarketOrder order;
	private Long cumulative;
	
	public MarketOrder getOrder() {
		return order;
	}
	public void setOrder(MarketOrder order) {
		this.order = order;
	}
	
	public Long getCumulative() {
		return cumulative;
	}
	public void setCumulative(Long cumulative) {
		this.cumulative = cumulative;
	}
	
	@Override
	public int compareTo(CumulativeMarketOrder o) {
		return cumulative.compareTo(o.cumulative);
	}
}
