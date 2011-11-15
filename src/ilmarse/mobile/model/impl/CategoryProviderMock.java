package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Category;
import ilmarse.mobile.model.api.CategoryProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryProviderMock implements CategoryProvider {

	@Override
	public List<Category> getCategories() {
		List<Category> ret = new ArrayList<Category>();
		Category books = new CategoryImpl(1,"books","Books");
		Category dvds  = new CategoryImpl(2,"dbds","Dvds");
		ret.add(books);
		ret.add(dvds);
		return ret;
	}

	@Override
	public List<? extends Map<String, ?>> getCategoriesAsMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getMapKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
