package ilmarse.mobile.model.api;

import java.util.List;
import java.util.Map;

public interface OrdersProvider {


	List<Order> getOrders(String username, String token);
	
	public List<? extends Map<String, ?>> getOrdersAsMap();

	public String[] getMapKeys();
	
}
