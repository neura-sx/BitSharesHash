package sx.neura.bts.gui.dto;


public class DelegateAnnouncement implements Comparable<DelegateAnnouncement> {
	
	public DelegateAnnouncement() {
		
	}
	public DelegateAnnouncement(String timestamp, String delegate, String text, String link) {
		this.timestamp = timestamp;
		this.delegate = delegate;
		this.text = text;
		this.link = link;
	}

	private String timestamp;
	private String delegate;
	private String text;
	private String link;
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getDelegate() {
		return delegate;
	}
	public void setDelegate(String delegate) {
		this.delegate = delegate;
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
	public int compareTo(DelegateAnnouncement o) {
		return timestamp.compareTo(o.timestamp);
	}

}
