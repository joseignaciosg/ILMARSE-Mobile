package ilmarse.mobile.model.impl;

import java.util.ArrayList;
import java.util.List;

import ilmarse.mobile.model.api.Category;
import ilmarse.mobile.model.api.Subcategory;


public class CategoryImpl implements Category {
	
	//look how to implement internationalization TODO
	private Integer id;
	private String code;
	private String name;
	
	public CategoryImpl() {
		super();
		this.id = -1;
		this.code = null;
		this.name = null;
	}
	
	public CategoryImpl(int id, String code, String name) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}
	
	public CategoryImpl(int id, String code, String name, List<Subcategory> subcategories) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CategoryImpl other = (CategoryImpl) obj;
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
		return true;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", code=" + code + ", name=" + name + "]";
	}
	
	
	
	
	
	

}
