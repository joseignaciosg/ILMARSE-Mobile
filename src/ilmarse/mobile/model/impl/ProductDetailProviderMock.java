package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.api.ProductDetailProvider;

public class ProductDetailProviderMock implements ProductDetailProvider {

	@Override
	public Product getProduct(int prodid) {
		String imageURL = "http://1.bp.blogspot.com/_mbWThvBk2kA/S43ftG3N6MI/AAAAAAAANws/FmT_6iWv8iE/s320/books+2.gif";
		Product product = new ProductImpl(prodid, 1, 1, "book1", 12, 12, imageURL);

		return product;
	}

}
