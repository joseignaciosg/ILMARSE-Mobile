package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.api.ProductDetailProvider;

public class ProductDetailProviderMock implements ProductDetailProvider {

	@Override
	public Product getProduct(int prodid, int catid) {
		String bookURL = "http://1.bp.blogspot.com/_mbWThvBk2kA/S43ftG3N6MI/AAAAAAAANws/FmT_6iWv8iE/s320/books+2.gif";
		String dvdURL = "http://www.flvdvdconverter.net/images/flv-to-dvd-converter.jpg";
		Product product=null;

		switch(catid){
		case 1://book
			product =  new Book(prodid, catid, 1, "book1", 12, 12, bookURL, "Dan brown", "11/11/2011", "esperanto, latin, greek");
			break;
		case 2://dvd
			product = new Dvd(prodid, catid, 2, "dvd1", 13, 13, dvdURL, "Facundo Arana(best actor ever...)", 
					"esperanto, latin, greek", "esperanto, latin, greek", "Third World", "1899 minutes", "11/12/2004", 
					"2:1");
		}
		
		return product;
	}

}
