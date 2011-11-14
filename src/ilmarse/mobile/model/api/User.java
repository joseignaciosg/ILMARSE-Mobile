package ilmarse.mobile.model.api;


public interface User {

	public String getUsername();
	
	public void setUsername(String username);
	
	public int getId();
	
	public void setId(int id);
	
	public String getName();
	
	public void setName(String name);
	
	public String getLast_login_date();
	
	public void setLast_login_date(String last_login_date);
	
	public String getToken();
	
	public void setToken(String token);
	
	public String toString();
	
	public int hashCode();
	
	public boolean equals(Object obj);
	
	
}
