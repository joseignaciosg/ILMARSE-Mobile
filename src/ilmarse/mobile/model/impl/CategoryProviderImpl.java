package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryProviderImpl extends AbstractCategoryProvider{
	
	List<Category> categories;
	

	public CategoryProviderImpl(String categoriesUrl) {
		super(categoriesUrl);
	}
	
	public CategoryProviderImpl(List<Category> categories) {
		super();
		if ( categories == null ) {
			throw new IllegalArgumentException("categories cannot be null");
		}
		
		this.categories = categories; 
	}
	

	@Override
	public List<Category> getCategories() {
        return categories;
	}
	
	//TODO add id and subcategories
	public static final String[] fields = { "code", "name","id"};

	@Override
	public List<? extends Map<String, ?>> getCategoriesAsMap() {
		List<Category> categories = getCategories();
		List<Map<String, String>> transformedCats = new ArrayList<Map<String, String>>();
		for (Category t : categories) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(fields[0], t.getCode().toString());
			map.put(fields[1], t.getName().toString());
			map.put(fields[2], t.getId().toString());
			transformedCats.add(map);
		}
		return transformedCats;
	}

	@Override
	public String[] getMapKeys() {
		return fields;
	}

}
