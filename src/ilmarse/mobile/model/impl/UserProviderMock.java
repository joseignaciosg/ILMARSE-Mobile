package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.User;
import ilmarse.mobile.model.api.UserProvider;

import java.util.HashMap;
import java.util.Map;

public class UserProviderMock implements UserProvider{

	Map<UserPass,User> map;
	
	public UserProviderMock(){
		super();
		map = new HashMap<UserPass,User>();
		initializeMap();
		System.out.println(map.toString());
		
	}
	
	private void initializeMap() {
		UserPass userpass1 = new UserPass("hci","itba1234");
		UserPass userpass2 = new UserPass("chino","chino");
		UserPass userpass3 = new UserPass("fefo","fefo");
		UserPass userpass4 = new UserPass("ilmarse","ilmarse");
		UserPass userpass5 = new UserPass("peron","peron");
		
		User user1 = new UserImpl("hci", 12, "hci", "11/11/2011", "hdjaskdjh6278udfhuidf");
		User user2 = new UserImpl("chino", 13, "chino", "11/11/2011", "hdjaskdjh68dfsdhfoudfhuidf");
		User user3 = new UserImpl("fefo", 14, "fefo", "14/12/2011", "hjskdfl898347897");
		User user4 = new UserImpl("ilmarse", 15, "ilmarse", "12/11/2011", "jkhdkljfshy3647r374628");
		User user5 = new UserImpl("peron", 16, "peron", "15/11/2011", "jlefhdfjsdnf6237849dfbhsjk");

		map.put(userpass1, user1);
		map.put(userpass2, user2);
		map.put(userpass3, user3);
		map.put(userpass4, user4);
		map.put(userpass5, user5);
		
	}

	public User getuser(final String username , final String password){
		UserPass user = new UserPass(username, password);
		System.out.println(user);
		if ( map.containsKey(user) ){
			System.out.println("Hola");
			return map.get(user);
		}
		System.out.println("cahu");

		return null;
	}





	private class UserPass{
		
		String username;
		String password;
		
		public UserPass(String username, String password){
			super();
			this.username = username;
			this.password = password;
		}

		@Override
		public String toString() {
			return "UserPass [username=" + username + ", password=" + password
					+ "]";
		}

		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result
					+ ((password == null) ? 0 : password.hashCode());
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
			UserPass other = (UserPass) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (password == null) {
				if (other.password != null)
					return false;
			} else if (!password.equals(other.password))
				return false;
			if (username == null) {
				if (other.username != null)
					return false;
			} else if (!username.equals(other.username))
				return false;
			return true;
		}

		private UserProviderMock getOuterType() {
			return UserProviderMock.this;
		}
		
		
		
		
	}
}
