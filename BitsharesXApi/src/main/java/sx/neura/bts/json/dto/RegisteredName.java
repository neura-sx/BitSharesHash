package sx.neura.bts.json.dto;

import java.util.LinkedHashMap;

public class RegisteredName implements Comparable<RegisteredName> {
	private int id;
	private String name;
	private Object public_data;
	private String owner_key;
	private Object active_key_history;
	private String registration_date;
	private String last_update;
	private DelegateInfo delegate_info;
	private Object meta_data;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getPublic_data() {
		return public_data;
	}
	public void setPublic_data(Object public_data) {
		if (public_data != null && public_data instanceof LinkedHashMap<?, ?>) {
			@SuppressWarnings("unchecked")
			LinkedHashMap<String, String> t = (LinkedHashMap<String, String>) public_data;
			PublicData p = new PublicData();
			p.setGravatarID(t.get("gravatarID"));
			p.setVersion(t.get("version"));
			this.public_data = p;
			return;
		}
		this.public_data = public_data;
	}
	public String getOwner_key() {
		return owner_key;
	}
	public void setOwner_key(String owner_key) {
		this.owner_key = owner_key;
	}
	public Object getActive_key_history() {
		return active_key_history;
	}
	public void setActive_key_history(Object active_key_history) {
		this.active_key_history = active_key_history;
	}
	public String getRegistration_date() {
		return registration_date;
	}
	public void setRegistration_date(String registration_date) {
		this.registration_date = registration_date;
	}
	public String getLast_update() {
		return last_update;
	}
	public void setLast_update(String last_update) {
		this.last_update = last_update;
	}
	public DelegateInfo getDelegate_info() {
		return delegate_info;
	}
	public void setDelegate_info(DelegateInfo delegate_info) {
		this.delegate_info = delegate_info;
	}
	public Object getMeta_data() {
		return meta_data;
	}
	public void setMeta_data(Object meta_data) {
		this.meta_data = meta_data;
	}
	@Override
	public int compareTo(RegisteredName o) {
		return getName().compareTo(o.getName());
	}
}
