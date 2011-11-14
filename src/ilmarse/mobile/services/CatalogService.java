package ilmarse.mobile.services;
import ilmarse.mobile.model.api.Category;
import ilmarse.mobile.model.api.CategoryProvider;
import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.api.ProductDetailProvider;
import ilmarse.mobile.model.api.ProductsProvider;
import ilmarse.mobile.model.api.Subcategory;
import ilmarse.mobile.model.api.SubcategoryProvider;
import ilmarse.mobile.model.impl.Book;
import ilmarse.mobile.model.impl.CategoryImpl;
import ilmarse.mobile.model.impl.CategoryProviderMock;
import ilmarse.mobile.model.impl.Dvd;
import ilmarse.mobile.model.impl.ProductDetailProviderMock;
import ilmarse.mobile.model.impl.AbstractProduct;
import ilmarse.mobile.model.impl.ProductsProviderMock;
import ilmarse.mobile.model.impl.SubcategoryImpl;
import ilmarse.mobile.model.impl.SubcategoryProviderMock;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class CatalogService extends IntentService {
	
	private CategoryProvider categoryProvider = new CategoryProviderMock();
	private SubcategoryProvider subcategoryProvider = new SubcategoryProviderMock();
	private ProductsProvider productsProvider = new ProductsProviderMock();
	private ProductDetailProvider productDetailProvider = new ProductDetailProviderMock();


	private final String TAG = getClass().getSimpleName();
	private final String APIurl = "http://eiffel.itba.edu.ar/hci/service/";

	public static final String GET_CAT_CMD = "GetCategories";
	public static final String GET_SUBCAT_CMD = "GetSubCategories";
	public static final String GET_PRODUCTS_CMD = "GetProducts";
	public static final String GET_PRODUCT_CMD = "GetProduct";

	  // names of the XML category tags
    static final String CODE = "code";
    static final  String NAME = "name";
    static final  String CATEGORY = "category";
    static final  String SUBCATEGORY = "subcategory";

    static final String CATEGORIES = "categories";

    static final String SUBCATEGORIES = "subcategories";
    static final String CAT_ID = "subcategory_id";

	
	
    static final  String ID = "id";
	public static final int STATUS_CONNECTION_ERROR = -1;
	public static final int STATUS_ERROR = -2;
	public static final int STATUS_ILLEGAL_ARGUMENT = -3;
	public static final int STATUS_OK = 0;

	/*
	 * Se debe crear un constructor sin parametros y asignarle un nombre al
	 * servicio.
	 */
	public CatalogService() {
		super("CatalogService");
	}

	/*
	 * Evento que se ejecuta cuando se invocó el servicio por medio de un
	 * Intent.
	 */
	@Override
	protected void onHandleIntent(final Intent intent) {
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		final String command = intent.getStringExtra("command");
		final int catId,subcatId,productId;
		final Bundle b = new Bundle();
		try {
			if (command.equals(GET_CAT_CMD)) { // command for receiving available categories
				getCategories(receiver, b);
			}else if ( command.equals(GET_SUBCAT_CMD)){
				catId = Integer.parseInt(intent.getStringExtra("catid"));
				b.putString("catid",catId+"");
				getSubCategories(receiver, b);
			}else if ( command.equals(GET_PRODUCTS_CMD) ){
				catId = Integer.parseInt(intent.getStringExtra("catid"));
				subcatId = Integer.parseInt(intent.getStringExtra("subcatid"));
				Log.d(TAG, catId +"-"+subcatId);
				b.putString("catid",catId+"");
				b.putString("subcatid",subcatId+"");
				getProductsSub(receiver, b);
			}else if ( command.equals(GET_PRODUCT_CMD)  ){
				Log.d(TAG,"GET_PRODUCT_CMD");
				catId = Integer.parseInt(intent.getStringExtra("categoryid"));
				productId = Integer.parseInt(intent.getStringExtra("productid"));
				Log.d(TAG, catId +"-"+productId);
				b.putString("categoryid",catId+"");
				b.putString("productid",productId+"");
				getProduct(receiver,b);
			}
		} catch (SocketTimeoutException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_CONNECTION_ERROR, b);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ERROR, b);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ERROR, b);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ILLEGAL_ARGUMENT, b);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ERROR, b);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

		// Es importante terminar el servicio lo antes posible.
		this.stopSelf();
	}


	private void getCategories(ResultReceiver receiver, Bundle b) throws ClientProtocolException, IOException, Exception {
		Log.d(TAG, "OK in getCategories ");
		List<Category> list;
		final DefaultHttpClient client = new DefaultHttpClient();
		
		/*gets the phone current language*/
		String phoneLanguage = this.getResources().getConfiguration().locale.getLanguage();
		final HttpResponse response;
		if(phoneLanguage.equals("en")){
			response = client.execute(new HttpGet( APIurl + "Catalog.groovy?method=GetCategoryList&language_id=1"));
		}
		else{
			response = client.execute(new HttpGet( APIurl + "Catalog.groovy?method=GetCategoryList&language_id=2"));
		}
		
		
		if ( response.getStatusLine().getStatusCode() != 200 ) {
			throw new IllegalArgumentException(response.getStatusLine().toString());
		}else {
			final String xmlToParse = EntityUtils.toString(response.getEntity());
			list =  fromXMLtoCategories(xmlToParse);
			b.putSerializable("return", (Serializable)list);
		}
		
//		list = categoryProvider.getCategories();
		b.putSerializable("return", (Serializable)list);
		receiver.send(STATUS_OK, b);
	}
	



	private void getSubCategories(ResultReceiver receiver, Bundle b) throws ClientProtocolException, IOException, Exception{
		Log.d(TAG, "OK in getSubCategories ");
		int catId = new Integer(b.getString("catid"));
		Log.d(TAG, "category id "+catId);
		final DefaultHttpClient client = new DefaultHttpClient();
		
		/*gets the phone current language*/
		String phoneLanguage = this.getResources().getConfiguration().locale.getLanguage();
		final HttpResponse response;
		if(phoneLanguage.equals("en")){
			response = client.execute(new HttpGet( APIurl + "Catalog.groovy?method=GetSubcategoryList&language_id=1&category_id="+catId));
		}
		else{
			response = client.execute(new HttpGet( APIurl + "Catalog.groovy?method=GetSubcategoryList&language_id=2&category_id="+catId));
		}
				
		
		if ( response.getStatusLine().getStatusCode() != 200 ) {
			throw new IllegalArgumentException(response.getStatusLine().toString());
		}
		
		final String xmlToParse = EntityUtils.toString(response.getEntity());
		List<Subcategory> list;
		list =fromXMLtoSubCategories(xmlToParse);
//		list =  subcategoryProvider.getSubcategories(catId);
		
		b.putSerializable("return", (Serializable)list);
		receiver.send(STATUS_OK, b);
		
	}
	
	private void getProductsSub(ResultReceiver receiver, Bundle b) throws ClientProtocolException, IOException, Exception {
		int catId = new Integer(b.getString("catid"));
		int subcatId = new Integer(b.getString("subcatid"));

		Log.d(TAG, "OK in getProducts "+catId +"/"+subcatId);
		final DefaultHttpClient client = new DefaultHttpClient();
		/*gets the phone current language*/
		String phoneLanguage = this.getResources().getConfiguration().locale.getLanguage();
		final HttpResponse response;
		if(phoneLanguage.equals("en")){
			response = client.execute(new HttpGet(APIurl + "Catalog.groovy" +
					"?method=GetProductListBySubcategory&language_id=1&category_id="+catId+"&subcategory_id="+subcatId));
		}else{
			response = client.execute(new HttpGet(APIurl + "Catalog.groovy" +
					"?method=GetProductListBySubcategory&language_id=2&category_id="+catId+"&subcategory_id="+subcatId));;
		}
		
		if ( response.getStatusLine().getStatusCode() != 200 ) {
			throw new IllegalArgumentException(response.getStatusLine().toString());
		}
		
		final String xmlToParse = EntityUtils.toString(response.getEntity());
		Log.d(TAG, xmlToParse.toString());
		List<Product> list;
//		list = productsProvider.getProducts(catId,subcatId);
		list = fromXMLtoProducts(xmlToParse,catId);

		b.putSerializable("return", (Serializable)list);
		receiver.send(STATUS_OK, b);
	}
	
	private void getProduct(ResultReceiver receiver, Bundle b) throws ClientProtocolException, IOException, Exception {
		Log.d(TAG, "inside getproduct");
		int prodId = new Integer(b.getString("productid"));
		int catId = new Integer(b.getString("categoryid"));
//		int prodId =1;
//		int catId = 2;
		Log.d(TAG, "inside getproduct / prodid="+prodId+ "/ catId=" +catId);
		
		final DefaultHttpClient client = new DefaultHttpClient();
		/*gets the phone current language*/
		final HttpResponse response;
		response = client.execute(new HttpGet(APIurl + "Catalog.groovy?method=GetProduct&product_id="+prodId));
		final String xmlToParse = EntityUtils.toString(response.getEntity());
		Log.d(TAG, xmlToParse.toString());
		Product product; 
		product = fromXMLtoProduct(xmlToParse, catId);
//		product = (Product) productDetailProvider.getProduct(prodId,catId);
		Log.d(TAG, "leaving getproduct");
		List<Product> aux = new ArrayList<Product>();
		aux.add(product);
		b.putSerializable("return", (Serializable)aux);
		receiver.send(STATUS_OK, b);
	}
	
	

	
	private List<Category> fromXMLtoCategories(String xmlToParse) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Category> retCategories = new ArrayList<Category>();
        try {
        	DocumentBuilder builder = factory.newDocumentBuilder();
            /*Document doc = builder.parse(new InputSource(new StringReader(xmlToParse)));*/
    		InputSource inStream = new InputSource();
    		inStream.setCharacterStream(new StringReader(xmlToParse));
    		Document doc = builder.parse(inStream);
            doc.getDocumentElement().normalize();
            
            NodeList nodeList = doc.getElementsByTagName(CATEGORY);
            for (int i=0;i<nodeList.getLength(); i++) {
            	//TODO get the id and subcategories
            	Category category = new CategoryImpl();		
                Node node = nodeList.item(i);
                Element categoryE = (Element) node;
                String id = categoryE.getAttribute("id");
                System.out.println(id);
                category.setId( Integer.parseInt( id ) );
                Log.d("TAG","id=" + String.valueOf( category.getId() ));
                NodeList properties = categoryE.getChildNodes();
                for (int j=0;j<properties.getLength();j++){
                    Node property = properties.item(j);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase(CODE)){
                        category.setCode(property.getFirstChild().getNodeValue());
                    } else if (name.equalsIgnoreCase(NAME)){
                        category.setName(property.getFirstChild().getNodeValue());
                    }
                }
                Log.d("TAG",category.toString());
                if (category.getCode() != null && category.getName() != null )
                	retCategories.add(category);
           }
        } catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			Log.e(TAG, "here!");
        } 
        Log.d(TAG, retCategories.toString());
        return retCategories;
		
	}
	
	private List<Subcategory> fromXMLtoSubCategories(String xmlToParse) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Subcategory> retSubcategories = new ArrayList<Subcategory>();
        try {
        	DocumentBuilder builder = factory.newDocumentBuilder();
            /*Document doc = builder.parse(new InputSource(new StringReader(xmlToParse)));*/
    		InputSource inStream = new InputSource();
    		inStream.setCharacterStream(new StringReader(xmlToParse));
    		Document doc = builder.parse(inStream);
            doc.getDocumentElement().normalize();
            
            NodeList nodeList = doc.getElementsByTagName(SUBCATEGORY);
            for (int i=0;i<nodeList.getLength(); i++) {
            	//TODO get the id and subcategories
            	Subcategory subcategory = new SubcategoryImpl();		
                Node node = nodeList.item(i);
                Element subcategoryE = (Element) node;
                String id = subcategoryE.getAttribute("id");
                System.out.println(id);
                subcategory.setId( Integer.parseInt( id ) );
                Log.d("TAG","id=" + String.valueOf( subcategory.getId() ));
                NodeList properties = subcategoryE.getChildNodes();
                for (int j=0;j<properties.getLength();j++){
                    Node property = properties.item(j);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase(CODE)){
                    	subcategory.setCode(property.getFirstChild().getNodeValue());
                    } else if (name.equalsIgnoreCase(NAME)){
                    	subcategory.setName(property.getFirstChild().getNodeValue());
                    }else if (name.equalsIgnoreCase("category_id")){
                    	subcategory.setSubCategory_id(Integer.parseInt(property.getFirstChild().getNodeValue()));
                    }
                }
                Log.d("TAG",subcategory.toString());
                if (subcategory.getCode() != null && subcategory.getName() != null )
                	retSubcategories.add(subcategory);
           }
        } catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			Log.e(TAG, "here!");
        } 
        Log.d(TAG, retSubcategories.toString());
        return retSubcategories;
	}
	
	
	private List<Product> fromXMLtoProducts(String xmlToParse, int catId) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Product> retProduct = new ArrayList<Product>();
        try {
        	DocumentBuilder builder = factory.newDocumentBuilder();
            /*Document doc = builder.parse(new InputSource(new StringReader(xmlToParse)));*/
    		InputSource inStream = new InputSource();
    		inStream.setCharacterStream(new StringReader(xmlToParse));
    		Document doc = builder.parse(inStream);
            doc.getDocumentElement().normalize();
            
            NodeList nodeList = doc.getElementsByTagName("product");
            for (int i=0;i<nodeList.getLength(); i++) {
            	Product product;
            	Node node;
            	Element productE;
            	if ( catId == 2 ){ //book
            		product = new Book();		
            		node = nodeList.item(i);
            		productE = (Element) node;
            		String id = productE.getAttribute("id");
            		System.out.println(id);
            		product.setId( Integer.parseInt( id ) );
            		Log.d("TAG","id=" + String.valueOf( product.getId() ));
            		NodeList properties = productE.getChildNodes();
            		for (int j=0;j<properties.getLength();j++){
            			Node property = properties.item(j);
            			String name = property.getNodeName();
            			if (name.equalsIgnoreCase("category_id")){
            				product.setCategory_id(Integer.valueOf(property.getFirstChild().getNodeValue()));
            			} else if (name.equalsIgnoreCase("subcategory_id")){
            				product.setSubcategory_id(Integer.valueOf(property.getFirstChild().getNodeValue()));
            			}else if (name.equalsIgnoreCase("name")){
            				product.setName(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("sales_rank")){
            				product.setSales_rank(Integer.parseInt(property.getFirstChild().getNodeValue()));
            			}else if (name.equalsIgnoreCase("price")){
            				product.setPrice(Double.valueOf(property.getFirstChild().getNodeValue()));
            			}else if (name.equalsIgnoreCase("image_url")){
            				product.setImage_url(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("authors")){
            				((Book)product).setAuthors(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("publisher")){
            				((Book)product).setPublisher(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("published_date")){
            				((Book)product).setPublished_date(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("language")){
            				((Book)product).setLanguage(property.getFirstChild().getNodeValue());
            			}
            		}
            	}else { //dvd
            		product = new Dvd();		
            		node = nodeList.item(i);
            		productE = (Element) node;
            		String id = productE.getAttribute("id");
            		System.out.println(id);
            		product.setId( Integer.parseInt( id ) );
            		Log.d("TAG","id=" + String.valueOf( product.getId() ));
            		NodeList properties = productE.getChildNodes();
            		for (int j=0;j<properties.getLength();j++){
            			Node property = properties.item(j);
            			String name = property.getNodeName();
            			if (name.equalsIgnoreCase("category_id")){
            				product.setCategory_id(Integer.valueOf(property.getFirstChild().getNodeValue()));
            			} else if (name.equalsIgnoreCase("subcategory_id")){
            				product.setSubcategory_id(Integer.valueOf(property.getFirstChild().getNodeValue()));
            			}else if (name.equalsIgnoreCase("name")){
            				product.setName(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("sales_rank")){
            				product.setSales_rank(Integer.parseInt(property.getFirstChild().getNodeValue()));
            			}else if (name.equalsIgnoreCase("price")){
            				product.setPrice(Double.valueOf(property.getFirstChild().getNodeValue()));
            			}else if (name.equalsIgnoreCase("image_url")){
            				product.setImage_url(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("actors")){
            				((Dvd)product).setActors(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("subtitles")){
            				((Dvd)product).setSubtitles(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("region")){
            				((Dvd)product).setRegion(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("language")){
            				((Dvd)product).setLanguage(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("run_time")){
            				((Dvd)product).setRun_time(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("release_date")){
            				((Dvd)product).setRelease_date(property.getFirstChild().getNodeValue());
            			}else if (name.equalsIgnoreCase("aspect_ratio")){
            				((Dvd)product).setAspect_ratio(property.getFirstChild().getNodeValue());
            			}
            			
            		}
            	}

                retProduct.add(product);
                Log.d("TAG","asd"+product.toString());
           }
        } catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			Log.e(TAG, "here!");
        } 
        Log.d(TAG, "asddd2"+retProduct.toString());
        return retProduct;
	}
	
	private Product fromXMLtoProduct(String xmlToParse, int catId) {
		Log.d(TAG,"inside fromXMLtoProduct");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Product product=null;
        try {
        	DocumentBuilder builder = factory.newDocumentBuilder();
            /*Document doc = builder.parse(new InputSource(new StringReader(xmlToParse)));*/
    		InputSource inStream = new InputSource();
    		inStream.setCharacterStream(new StringReader(xmlToParse));
    		Document doc = builder.parse(inStream);
            doc.getDocumentElement().normalize();
            
            NodeList nodeList = doc.getElementsByTagName("product");
            Log.d(TAG, "NodeList node after");
            Element productE;
            if ( catId == 2 ){//book
            	product = new Book();	
            	Node node = nodeList.item(0);
        		productE = (Element) node;
        		String id = productE.getAttribute("id");
        		System.out.println(id);
        		product.setId( Integer.parseInt( id ) );
        		Log.d("TAG","id=" + String.valueOf( product.getId() ));
        		NodeList properties = productE.getChildNodes();
        		System.out.println("BOOK properties length :" + properties.getLength());
        		for (int j=0;j<properties.getLength();j++){
        			Node property = properties.item(j);
        			String name = property.getNodeName();
        			System.out.println(name);
        			if (name.equalsIgnoreCase("category_id")){
        				product.setCategory_id(Integer.valueOf(property.getFirstChild().getNodeValue()));
        			} else if (name.equalsIgnoreCase("subcategory_id")){
        				product.setSubcategory_id(Integer.valueOf(property.getFirstChild().getNodeValue()));
        			}else if (name.equalsIgnoreCase("name")){
        				product.setName(property.getFirstChild().getNodeValue());
        			}else if (name.equalsIgnoreCase("sales_rank")){
        				product.setSales_rank(Integer.parseInt(property.getFirstChild().getNodeValue()));
        			}else if (name.equalsIgnoreCase("price")){
        				product.setPrice(Double.valueOf(property.getFirstChild().getNodeValue()));
        			}else if (name.equalsIgnoreCase("image_url")){
        				product.setImage_url(property.getFirstChild().getNodeValue());
        			}else if (name.equalsIgnoreCase("authors")){
        				((Book)product).setAuthors(property.getFirstChild().getNodeValue());
        			}else if (name.equalsIgnoreCase("publisher")){
        				((Book)product).setPublisher(property.getFirstChild().getNodeValue());
        			}else if (name.equalsIgnoreCase("published_date")){
        				((Book)product).setPublished_date(property.getFirstChild().getNodeValue());
        			}else if (name.equalsIgnoreCase("language")){
        				((Book)product).setLanguage(property.getFirstChild().getNodeValue());
        			}
        		}
            }else{//dvd
            	product = new Dvd();	
            	Node node = nodeList.item(0);
        		productE = (Element) node;
        		String id = productE.getAttribute("id");
        		System.out.println(id);
        		product.setId( Integer.parseInt( id ) );
        		Log.d("TAG","id=" + String.valueOf( product.getId() ));
        		NodeList properties = productE.getChildNodes();
        		System.out.println("DVD properties length :" + properties.getLength());
        		for (int j=0;j<properties.getLength();j++){
        			Node property = properties.item(j);
        			String name = property.getNodeName();
        			if (name.equalsIgnoreCase("category_id")){
        				product.setCategory_id(Integer.valueOf(property.getFirstChild().getNodeValue()));
        			} else if (name.equalsIgnoreCase("subcategory_id")){
        				product.setSubcategory_id(Integer.valueOf(property.getFirstChild().getNodeValue()));
        			}else if (name.equalsIgnoreCase("name")){
        				product.setName(property.getFirstChild().getNodeValue());
        				Log.d(TAG,product.getName());
        			}else if (name.equalsIgnoreCase("sales_rank")){
        				product.setSales_rank(Integer.parseInt(property.getFirstChild().getNodeValue()));
        				Log.d(TAG,product.getSales_rank()+"" );
        			}else if (name.equalsIgnoreCase("price")){
        				product.setPrice(Double.valueOf(property.getFirstChild().getNodeValue()));
        				Log.d(TAG,product.getPrice() +"");
        			}else if (name.equalsIgnoreCase("image_url")){
        				product.setImage_url(property.getFirstChild().getNodeValue());
        				Log.d(TAG,product.getImage_url() );
        			}else if (name.equalsIgnoreCase("actors")){
        				((Dvd)product).setActors(property.getFirstChild().getNodeValue());
        				Log.d(TAG,((Dvd)product).getActors() );
        			}else if (name.equalsIgnoreCase("subtitles")){
        				((Dvd)product).setSubtitles(property.getFirstChild().getNodeValue());
        				Log.d(TAG,((Dvd)product).getSubtitles() );
        			}else if (name.equalsIgnoreCase("region")){
        				((Dvd)product).setRegion(property.getFirstChild().getNodeValue());
        				Log.d(TAG,((Dvd)product).getRegion() );
        			}else if (name.equalsIgnoreCase("language")){
        				((Dvd)product).setLanguage(property.getFirstChild().getNodeValue());
        				Log.d(TAG,((Dvd)product).getLanguage() );
        			}else if (name.equalsIgnoreCase("run_time")){
        				((Dvd)product).setRun_time(property.getFirstChild().getNodeValue());
        				Log.d(TAG,((Dvd)product).getRun_time() );
        			}else if (name.equalsIgnoreCase("release_date")){
        				((Dvd)product).setRelease_date(property.getFirstChild().getNodeValue());
        				Log.d(TAG,((Dvd)product).getRelease_date() );
        			}else if (name.equalsIgnoreCase("aspect_ratio")){
        				((Dvd)product).setAspect_ratio(property.getFirstChild().getNodeValue());
        				Log.d(TAG,((Dvd)product).getAspect_ratio() );
        			}
        			
        		}
            }
             Log.d("TAG","Catalog.groovy?method=GetProduct&product_id Product: "+product.toString());
        } catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			Log.e(TAG, "here!");
        } 
        return product;
	}

	
	
}
	