package ilmarse.mobile.model.impl;

public class Dvd extends AbstractProduct{
	
	private String actors;
	private String language;
	private String subtitles;
	private String region;
	private String run_time;
	private String release_date;
	private String aspect_ratio;
	
	public Dvd(int id, int category_id, int subcategory_id,String name, int sales_rank, 
				double price, String image_url,String actors,String language,
				String subtitles, String region, String run_time, String release_date,
				String aspect_ratio){
		super(id,category_id, subcategory_id, name, sales_rank,  price, image_url);
		this.actors = actors;
		this.language = language;
		this.subtitles = subtitles;
		this.region = region;
		this.run_time = run_time;
		this.release_date = release_date;
		this.aspect_ratio = aspect_ratio;
	}
	
	public Dvd(){
		super();
		this.actors = null;
		this.language = null;
		this.subtitles = null;
		this.region = null;
		this.run_time = null;
		this.release_date = null;
		this.aspect_ratio = null;
	}
	
	public String getActors() {
		return actors;
	}
	public void setActors(String actors) {
		this.actors = actors;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getSubtitles() {
		return subtitles;
	}
	public void setSubtitles(String subtitles) {
		this.subtitles = subtitles;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRun_time() {
		return run_time;
	}
	public void setRun_time(String run_time) {
		this.run_time = run_time;
	}
	public String getRelease_date() {
		return release_date;
	}
	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}
	public String getAspect_ratio() {
		return aspect_ratio;
	}
	public void setAspect_ratio(String aspect_ratio) {
		this.aspect_ratio = aspect_ratio;
	}
	@Override
	public String toString() {
		return "Dvd [actors=" + actors + ", language=" + language
				+ ", subtitles=" + subtitles + ", region=" + region
				+ ", rub_time=" + run_time + ", release_date=" + release_date
				+ ", aspect_ratio=" + aspect_ratio + ", getActors()="
				+ getActors() + ", getLanguage()=" + getLanguage()
				+ ", getSubtitles()=" + getSubtitles() + ", getRegion()="
				+ getRegion() + ", getRub_time()=" + getRun_time()
				+ ", getRelease_date()=" + getRelease_date()
				+ ", getAspect_ratio()=" + getAspect_ratio() + ", getId()="
				+ getId() + ", getCategory_id()=" + getCategory_id()
				+ ", getSubcategory_id()=" + getSubcategory_id()
				+ ", getName()=" + getName() + ", getSales_rank()="
				+ getSales_rank() + ", getPrice()=" + getPrice()
				+ ", getImage_url()=" + getImage_url() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + "]";
	}
	
	

}
