package ilmarse.mobile.model.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import ilmarse.mobile.model.api.CategoryProvider;

public abstract class AbtractCategoryProvider implements CategoryProvider{
	  // names of the XML tags
    static final String CODE = "code";
    static final  String NAME = "name";
    static final  String CATEGORY = "category";
    static final  String ID = "id";
    
	final URL categoriesUrl;
	
	protected AbtractCategoryProvider(){
		
		categoriesUrl= null;
		
	}
	
	protected AbtractCategoryProvider(String categoriesUrl){
		try {
			this.categoriesUrl = new URL(categoriesUrl);
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
