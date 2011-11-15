package ilmarse.mobile.model.impl;

import ilmarse.mobile.model.api.Product;

public abstract class AbstractProduct implements Product {

	private int id;
	private int category_id;
	private int subcategory_id;
	private String name;
	private int sales_rank;
	private double price;
	private String image_url;
	
	
	public AbstractProduct(int id, int category_id, int subcategory_id,String name, int sales_rank, double price, String image_url) {
		super();
		this.id = id;
		this.category_id = category_id;
		this.subcategory_id = subcategory_id;
		this.name = name;
		this.sales_rank = sales_rank;
		this.price = price;
		this.image_url = image_url;
	}

	public AbstractProduct() {
		super();
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getCategory_id() {
		return category_id;
	}


	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}


	public int getSubcategory_id() {
		return subcategory_id;
	}


	public void setSubcategory_id(int subcategory_id) {
		this.subcategory_id = subcategory_id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getSales_rank() {
		return sales_rank;
	}


	public void setSales_rank(int sales_rank) {
		this.sales_rank = sales_rank;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getImage_url() {
		return image_url;
	}


	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + category_id;
		result = prime * result + id;
		result = prime * result
				+ ((image_url == null) ? 0 : image_url.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + sales_rank;
		result = prime * result + subcategory_id;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractProduct other = (AbstractProduct) obj;
		if (category_id != other.category_id)
			return false;
		if (id != other.id)
			return false;
		if (image_url == null) {
			if (other.image_url != null)
				return false;
		} else if (!image_url.equals(other.image_url))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(price) != Double
				.doubleToLongBits(other.price))
			return false;
		if (sales_rank != other.sales_rank)
			return false;
		if (subcategory_id != other.subcategory_id)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "ProductImpl [id=" + id + ", category_id=" + category_id
				+ ", subcategory_id=" + subcategory_id + ", name=" + name
				+ ", sales_rank=" + sales_rank + ", price=" + price
				+ ", image_url=" + image_url + "]";
	}
	
	
	
	
	
	
	
}
