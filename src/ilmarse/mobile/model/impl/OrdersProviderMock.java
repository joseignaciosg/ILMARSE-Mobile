package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Order;
import ilmarse.mobile.model.api.OrdersProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrdersProviderMock implements OrdersProvider {

	@Override
	public List<Order> getOrders(String username, String token) {
		List<Order> ret = new ArrayList<Order>();
		Order order1 = new OrderImpl("chino","hdjaskdjh68dfsdhfoudfhuidf",1123, "created", "11/11/2011", null, null, null, "-54.836242", "-68.349295");
		Order order2 = new OrderImpl("chino","hdjaskdjh68dfsdhfoudfhuidf",3211, "confirmed", "11/11/2011", "12/11/2011", null, null, "-40.702147", "-74.015794");
		Order order3 = new OrderImpl("chino","hdjaskdjh68dfsdhfoudfhuidf",4651, "shipped", "11/11/2011", "12/11/2011", "13/11/2011", null, "-74.015794", "-74.015794");
		Order order4 = new OrderImpl("chino","hdjaskdjh68dfsdhfoudfhuidf",4561, "delivered", "11/11/2011", "12/11/2011", "13/11/2011", "14/11/2011", "53.015794", "53.015794");
		Order order5 = new OrderImpl("chino","hdjaskdjh68dfsdhfoudfhuidf",1566, "created", "11/11/2011", null, null, null, "40.711614", "-74.012318");
		ret.add(order1);
		ret.add(order2);
		ret.add(order3);
		ret.add(order4);
		ret.add(order5);
		return ret;
	}

	@Override
	public List<? extends Map<String, ?>> getOrdersAsMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getMapKeys() {
		// TODO Auto-generated method stub
		return null;
	}
	

	
}
