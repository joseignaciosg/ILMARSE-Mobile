package ilmarse.mobile.model.api;

import ilmarse.mobile.model.impl.OrderImpl;

public interface Order {
	
	public int getId();

	public void setId(int id);

	public String getStatus();

	public void setStatus(String status);

	public String getCreated_date();

	public void setCreated_date(String created_date);
	
	public String getConfirmed_date();

	public void setConfirmed_date(String confirmed_date);

	public String getShipped_date();

	public void setShipped_date(String shipped_date);

	public String getDelivered_date();
	
	public void setDelivered_date(String delivered_date);
	
	public int getLatitude();

	public void setLatitude(int latitude);

	public int getLongitude();

	public void setLongitude(int longitude);

	public String toString();

	public int hashCode();
	
	public boolean equals(Object obj);
}
