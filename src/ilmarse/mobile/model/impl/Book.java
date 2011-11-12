package ilmarse.mobile.model.impl;

public class Book extends AbstractProduct{
	
	private String authors;
	private String publisher;
	private String published_date;
	private String language;


	public Book(int id, int category_id, int subcategory_id,String name,
				int sales_rank, double price, String image_url, String authors, 
				String published_date, String language){
		super(id,category_id, subcategory_id, name, sales_rank,  price, image_url);
		this.authors = authors;
		this.published_date = published_date;
		this.language = language;
	}
	
	public Book(){
		super();
		this.authors = null;
		this.published_date = null;
		this.language = null;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}
	
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublished_date() {
		return published_date;
	}

	public void setPublished_date(String published_date) {
		this.published_date = published_date;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "Book [authors=" + authors + ", published_date="
				+ published_date + ", language=" + language + ", getAuthors()="
				+ getAuthors() + ", getPublished_date()=" + getPublished_date()
				+ ", getLanguage()=" + getLanguage() + ", getId()=" + getId()
				+ ", getCategory_id()=" + getCategory_id()
				+ ", getSubcategory_id()=" + getSubcategory_id()
				+ ", getName()=" + getName() + ", getSales_rank()="
				+ getSales_rank() + ", getPrice()=" + getPrice()
				+ ", getImage_url()=" + getImage_url() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + "]";
	}

	

}
