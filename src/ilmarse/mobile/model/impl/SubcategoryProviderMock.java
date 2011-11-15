package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Subcategory;
import ilmarse.mobile.model.api.SubcategoryProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SubcategoryProviderMock implements SubcategoryProvider {

	
	@Override
	public List<Subcategory> getSubcategories(int category_id) {
		List<Subcategory> ret = new ArrayList<Subcategory>();
		
		//books subcategories
		Subcategory novels = new SubcategoryImpl(1, category_id, "novels", "Novels");
		Subcategory biographies = new SubcategoryImpl(2, category_id, "biographies","Biographies");
		
		//dvds subcategories
		Subcategory action = new SubcategoryImpl(1, category_id, "action", "Action");
		Subcategory romance = new SubcategoryImpl(1, category_id, "romance", "Romance");
		
		
		if ( category_id == 1){ //books
			ret.add(novels);
			ret.add(biographies);
		}else { //dvds
			ret.add(action);
			ret.add(romance);
		}
		return ret;
	}

	@Override
	public List<? extends Map<String, ?>> getSubcategoriesAsMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getMapKeys() {
		// TODO Auto-generated method stub
		return null;
	}

}
