package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Category;
import ilmarse.mobile.model.api.Subcategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCategoryProviderImpl extends AbstractCategoryProvider{
	
	List<Subcategory> subcategories;
	

	public SubCategoryProviderImpl(String subcategoriesUrl) {
		super(subcategoriesUrl);
	}
	
	public SubCategoryProviderImpl(List<Subcategory> subcategories) {
		super();
		if ( subcategories == null ) {
			throw new IllegalArgumentException("categories cannot be null");
		}
		
		this.subcategories = subcategories; 
	}
	

	public List<Subcategory> getSubCategories() {
        return subcategories;
	}
	
	//TODO add id and subcategories
	public static final String[] fields = { "code", "name","id"};

	@Override
	public List<? extends Map<String, ?>> getCategoriesAsMap() {
		List<Subcategory> subcategories = getSubCategories();
		List<Map<String, String>> transformedCats = new ArrayList<Map<String, String>>();
		for (Subcategory t : subcategories) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(fields[0], t.getCode().toString());
			map.put(fields[1], t.getName().toString());
			map.put(fields[2], String.valueOf(t.getId()));
			transformedCats.add(map);
		}
		return transformedCats;
	}

	@Override
	public String[] getMapKeys() {
		return fields;
	}

	@Override
	public List<Category> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}

}
