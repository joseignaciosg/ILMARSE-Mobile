package ilmarse.mobile.model.api;


import java.util.List;

public interface Subcategory {
	
	public int getId();

	public void setId(int id);

	public int getSubCategory_id();

	public void setSubCategory_id(int category_id);

	public String getCode();

	public void setCode(String code);

	public String getName();

	public void setName(String name);

	public List<Product> getProducts();

	public void setProducts(List<Product> products) ;

	public int hashCode();

	public boolean equals(Object obj);

	public String toString();

}
