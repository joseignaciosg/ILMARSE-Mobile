package ilmarse.mobile.model.impl;

import java.util.ArrayList;
import java.util.List;

import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.api.Subcategory;

public class SubcategoryImpl implements Subcategory {

	private int id;
	private int category_id;
	private String code;
	private String name;
	private List<Product> products;
	
	public SubcategoryImpl() {
		super();
		this.id = -1;
		this.category_id = -1;
		this.code = null;
		this.name = null;
		this.products = null;
	}
	
	public SubcategoryImpl(int id, int category_id, String code, String name) {
		super();
		this.id = id;
		this.category_id = category_id;
		this.code = code;
		this.name = name;
		this.products = new ArrayList<Product>();
	}
	
	public SubcategoryImpl(int id, int category_id, String code, String name,
			List<Product> products) {
		super();
		this.id = id;
		this.category_id = category_id;
		this.code = code;
		this.name = name;
		this.products = products;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSubCategory_id() {
		return category_id;
	}

	public void setSubCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + category_id;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((products == null) ? 0 : products.hashCode());
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
		SubcategoryImpl other = (SubcategoryImpl) obj;
		if (category_id != other.category_id)
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (products == null) {
			if (other.products != null)
				return false;
		} else if (!products.equals(other.products))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SubcategoryImpl [id=" + id + ", category_id=" + category_id
				+ ", code=" + code + ", name=" + name + "]";
	}
	
	
	
	
	
	
	
}
