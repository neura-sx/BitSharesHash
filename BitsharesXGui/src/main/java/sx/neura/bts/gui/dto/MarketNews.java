package sx.neura.bts.gui.dto;

import sx.neura.bts.json.dto.Market;


public class MarketNews implements Comparable<MarketNews> {
	public MarketNews() {
		
	}
	public MarketNews(String timestamp, Market market, String text, String link) {
		this.timestamp = timestamp;
		this.market = market;
		this.text = text;
		this.link = link;
	}

	private String timestamp;
	private Market market;
	private String text;
	private String link;
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public Market getMarket() {
		return market;
	}
	public void setMarket(Market market) {
		this.market = market;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	@Override
	public int compareTo(MarketNews o) {
		return timestamp.compareTo(o.timestamp);
	}

}
