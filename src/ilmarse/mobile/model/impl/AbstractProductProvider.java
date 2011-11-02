package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.ProductsProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class AbstractProductProvider implements ProductsProvider{
	  // names of the XML tags
    static final String CODE = "code";
    static final  String NAME = "name";
    static final  String CATEGORY = "category";
    static final String SUBCATEGORY = "subcategory";
    static final  String ID = "id";
    
	final URL categoriesUrl;
	
	protected AbstractProductProvider(){
		categoriesUrl= null;
	}
	
	protected AbstractProductProvider(String productsUrl){
		try {
			this.categoriesUrl = new URL(productsUrl);
		} catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
	}
	
	protected InputStream getInputStream() {
        try {
            return categoriesUrl.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
