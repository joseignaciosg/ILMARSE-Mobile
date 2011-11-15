package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.User;

public class UserImpl implements User{

	private String username;
	private int id;
	private String name;
	private String last_login_date;
	private String token;
	
	
	
	public UserImpl(String username, int id, String name,
			String last_login_date, String token) {
		super();
		this.username = username;
		this.id = id;
		this.name = name;
		this.last_login_date = last_login_date;
		this.token = token;
	}
	
	
	
	public UserImpl() {
		super();
		username = null;
		id = -1;
		name = null;
		last_login_date = null;
		token = null;
	}



	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
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
	
	public String getLast_login_date() {
		return last_login_date;
	}
	
	public void setLast_login_date(String last_login_date) {
		this.last_login_date = last_login_date;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	@Override
	public String toString() {
		return "UserImpl [username=" + username + ", id=" + id + ", name="
				+ name + ", last_login_date=" + last_login_date + ", token="
				+ token + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result
				+ ((last_login_date == null) ? 0 : last_login_date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		UserImpl other = (UserImpl) obj;
		if (id != other.id)
			return false;
		if (last_login_date == null) {
			if (other.last_login_date != null)
				return false;
		} else if (!last_login_date.equals(other.last_login_date))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	
	
	
}
