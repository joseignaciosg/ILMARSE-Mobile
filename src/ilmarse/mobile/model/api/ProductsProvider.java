package ilmarse.mobile.model.api;

import java.util.List;
import java.util.Map;

public interface ProductsProvider {

	List<Product> getProducts(int catid, int subcatid);
	
	public List<? extends Map<String, ?>> getProductsAsMap();

	public String[] getMapKeys();
}
