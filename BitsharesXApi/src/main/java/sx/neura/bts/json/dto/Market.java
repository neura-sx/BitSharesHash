package sx.neura.bts.json.dto;

import sx.neura.bts.json.CommandJson.Error;

public class Market implements Comparable<Market> {
	private int quote_id;
	private int base_id;
	private double current_feed_price;
	private double last_valid_feed_price;
	private Error last_error;
	private long bid_depth;
	private long ask_depth;
	private Price center_price;
	
	public int getQuote_id() {
		return quote_id;
	}
	public void setQuote_id(int quote_id) {
		this.quote_id = quote_id;
	}
	public int getBase_id() {
		return base_id;
	}
	public void setBase_id(int base_id) {
		this.base_id = base_id;
	}
	public double getCurrent_feed_price() {
		return current_feed_price;
	}
	public void setCurrent_feed_price(double current_feed_price) {
		this.current_feed_price = current_feed_price;
	}
	public double getLast_valid_feed_price() {
		return last_valid_feed_price;
	}
	public void setLast_valid_feed_price(double last_valid_feed_price) {
		this.last_valid_feed_price = last_valid_feed_price;
	}
	public Error getLast_error() {
		return last_error;
	}
	public void setLast_error(Error last_error) {
		this.last_error = last_error;
	}
	public long getBid_depth() {
		return bid_depth;
	}
	public void setBid_depth(long bid_depth) {
		this.bid_depth = bid_depth;
	}
	public long getAsk_depth() {
		return ask_depth;
	}
	public void setAsk_depth(long ask_depth) {
		this.ask_depth = ask_depth;
	}
	public Price getCenter_price() {
		return center_price;
	}
	public void setCenter_price(Price center_price) {
		this.center_price = center_price;
	}
	@Override
	public int compareTo(Market o) {
		return new Integer(quote_id).compareTo(o.quote_id);
	}
}
