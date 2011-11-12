package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.api.ProductsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductsProviderMock implements ProductsProvider {

	@Override
	public List<Product> getProducts(int subcatid) {
		List<Product> ret = new ArrayList<Product>();
		String imageURL = "http://1.bp.blogspot.com/_mbWThvBk2kA/S43ftG3N6MI/AAAAAAAANws/FmT_6iWv8iE/s320/books+2.gif";
		Product book1 = new ProductImpl(1, 1, subcatid, "book1", 12, 12, imageURL);
		ret.add(book1);
		return ret;
	}

	@Override
	public List<? extends Map<String, ?>> getProductsAsMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getMapKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
