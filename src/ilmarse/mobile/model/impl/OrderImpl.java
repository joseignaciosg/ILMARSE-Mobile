package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Order;

public class OrderImpl implements Order {
	
	private int id;
	private String status;
	private String created_date;
	private String confirmed_date;
	private String shipped_date;
	private String delivered_date;
	private int latitude;
	private int longitude;
	
	public OrderImpl(){
		super();
		this.id = -1;
		this.status = null;
		this.created_date = null;
		this.confirmed_date = null;
		this.shipped_date = null;
		this.delivered_date = null;
		this.latitude = -1;
		this.longitude = -1;
	}
	
	public OrderImpl(int id, String status, String created_date,
			String confirmed_date, String shipped_date, String delivered_date,
			int latitude, int longitude) {
		super();
		this.id = id;
		this.status = status;
		this.created_date = created_date;
		this.confirmed_date = confirmed_date;
		this.shipped_date = shipped_date;
		this.delivered_date = delivered_date;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getConfirmed_date() {
		return confirmed_date;
	}
	public void setConfirmed_date(String confirmed_date) {
		this.confirmed_date = confirmed_date;
	}
	public String getShipped_date() {
		return shipped_date;
	}
	public void setShipped_date(String shipped_date) {
		this.shipped_date = shipped_date;
	}
	public String getDelivered_date() {
		return delivered_date;
	}
	public void setDelivered_date(String delivered_date) {
		this.delivered_date = delivered_date;
	}
	public int getLatitude() {
		return latitude;
	}
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	public int getLongitude() {
		return longitude;
	}
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return "OrderImpl [id=" + id + ", status=" + status + ", created_date="
				+ created_date + ", confirmed_date=" + confirmed_date
				+ ", shipped_date=" + shipped_date + ", delivered_date="
				+ delivered_date + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((confirmed_date == null) ? 0 : confirmed_date.hashCode());
		result = prime * result
				+ ((created_date == null) ? 0 : created_date.hashCode());
		result = prime * result
				+ ((delivered_date == null) ? 0 : delivered_date.hashCode());
		result = prime * result + id;
		result = prime * result + latitude;
		result = prime * result + longitude;
		result = prime * result
				+ ((shipped_date == null) ? 0 : shipped_date.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderImpl other = (OrderImpl) obj;
		if (confirmed_date == null) {
			if (other.confirmed_date != null)
				return false;
		} else if (!confirmed_date.equals(other.confirmed_date))
			return false;
		if (created_date == null) {
			if (other.created_date != null)
				return false;
		} else if (!created_date.equals(other.created_date))
			return false;
		if (delivered_date == null) {
			if (other.delivered_date != null)
				return false;
		} else if (!delivered_date.equals(other.delivered_date))
			return false;
		if (id != other.id)
			return false;
		if (latitude != other.latitude)
			return false;
		if (longitude != other.longitude)
			return false;
		if (shipped_date == null) {
			if (other.shipped_date != null)
				return false;
		} else if (!shipped_date.equals(other.shipped_date))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	
	

}
