package ilmarse.mobile.model.api;

import java.util.List;
import java.util.Map;

public interface SubcategoryProvider {

	List<Subcategory> getSubcategories(int category_id);
	
	public List<? extends Map<String, ?>> getSubcategoriesAsMap();

	public String[] getMapKeys();
}
