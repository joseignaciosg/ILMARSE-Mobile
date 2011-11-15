package ilmarse.mobile.model.api;


public interface Product {
	
	public int getId() ;


	public void setId(int id);


	public int getCategory_id();


	public void setCategory_id(int category_id);


	public int getSubcategory_id();


	public void setSubcategory_id(int subcategory_id);


	public String getName() ;


	public void setName(String name);


	public int getSales_rank() ;


	public void setSales_rank(int sales_rank) ;


	public double getPrice() ;


	public void setPrice(double price);


	public String getImage_url() ;


	public void setImage_url(String image_url);


	public int hashCode();


	public boolean equals(Object obj) ;


	public String toString() ;

}
