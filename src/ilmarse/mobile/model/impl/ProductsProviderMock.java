package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.api.ProductsProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductsProviderMock implements ProductsProvider {

	@Override
	public List<Product> getProducts(int catid, int subcatid) {
		List<Product> ret = new ArrayList<Product>();
		String bookURL = "http://1.bp.blogspot.com/_mbWThvBk2kA/S43ftG3N6MI/AAAAAAAANws/FmT_6iWv8iE/s320/books+2.gif";
		String dvdURL = "http://www.flvdvdconverter.net/images/flv-to-dvd-converter.jpg";
		switch(catid){
		case 1: //books
			Product book1 =  new Book(1, catid, subcatid, "book1", 12, 12, bookURL, "Dan brown", "11/11/2011", 
					"esperanto, latin, greek");
			ret.add(book1);
			break;
		case 2:
			Product dvd1 = new Dvd(1, catid, subcatid, "dvd1", 13, 13, dvdURL, "Facundo Arana(best actor ever...)", 
					"esperanto, latin, greek", "esperanto, latin, greek", "Third World", "1899 minutes", "11/12/2004", 
					"2:1");
			ret.add(dvd1);
			break;
		}
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
