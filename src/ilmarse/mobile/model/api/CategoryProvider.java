package ilmarse.mobile.model.api;

import java.util.List;
import java.util.Map;

public interface CategoryProvider {

	List<Category> getCategories();
	
	public List<? extends Map<String, ?>> getCategoriesAsMap();

	public String[] getMapKeys();
}
