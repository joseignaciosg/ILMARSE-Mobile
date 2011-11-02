package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductProviderImpl extends AbstractProductProvider{
	
	List<Product> products;
	

	public ProductProviderImpl(String productsUrl) {
		super(productsUrl);
	}
	
	public ProductProviderImpl(List<Product> products) {
		super();
		if ( products == null ) {
			throw new IllegalArgumentException("products cannot be null");
		}
		
		this.products = products; 
	}
	

	@Override
	public List<Product> getProducts() {
        return products;
	}
	
	//TODO add id and subcategories
	public static final String[] fields = { "id", "category_id","subcategory_id", "name", "sales_rank", "price", "image_url"};

	@Override
	public List<? extends Map<String, ?>> getProductsAsMap() {
		List<Product> products = getProducts();
		List<Map<String, String>> transformedProds = new ArrayList<Map<String, String>>();
		for (Product t : products) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(fields[0], String.valueOf(t.getId()));
			map.put(fields[1], String.valueOf(t.getCategory_id()));
			map.put(fields[2], String.valueOf(t.getSubcategory_id()));
			map.put(fields[3], String.valueOf(t.getName()));
			map.put(fields[4], String.valueOf(t.getSales_rank()));
			map.put(fields[5], String.valueOf(t.getPrice()));
			map.put(fields[6], String.valueOf(t.getImage_url()));
			transformedProds.add(map);
		}
		return transformedProds;
	}

	@Override
	public String[] getMapKeys() {
		return fields;
	}

}
