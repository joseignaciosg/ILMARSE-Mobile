package ilmarse.mobile.services;
import ilmarse.mobile.model.api.Category;
import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.api.Subcategory;
import ilmarse.mobile.model.impl.CategoryImpl;
import ilmarse.mobile.model.impl.ProductImpl;
import ilmarse.mobile.model.impl.SubcategoryImpl;

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

	private final String TAG = getClass().getSimpleName();

	public static final String GET_CAT_CMD = "GetCategories";
	public static final String GET_SUBCAT_CMD = "GetSubCategories";
	public static final String GET_PRODUCTS_CMD = "GetProducts";

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
		super("CategorySearchService");
	}

	/*
	 * Evento que se ejecuta cuando se invocó el servicio por medio de un
	 * Intent.
	 */
	@Override
	protected void onHandleIntent(final Intent intent) {
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		final String command = intent.getStringExtra("command");

		final Bundle b = new Bundle();
		try {
			if (command.equals(GET_CAT_CMD)) { // command for receiving available categories
				getCategories(receiver, b);
			}else if ( command.equals(GET_SUBCAT_CMD)){
				getSubCategories(receiver, b);
			}else if ( command.equals(GET_PRODUCTS_CMD)){
				getProductsSub(receiver, b);
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
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpResponse response = client.execute(new HttpGet("http://eiffel.itba.edu.ar/hci/service/Catalog.groovy" +
				"?method=GetCategoryList&language_id=1"));
		if ( response.getStatusLine().getStatusCode() != 200 ) {
			throw new IllegalArgumentException(response.getStatusLine().toString());
		}
		
		final String xmlToParse = EntityUtils.toString(response.getEntity());

		b.putSerializable("return", (Serializable)fromXMLtoCategories(xmlToParse));
		receiver.send(STATUS_OK, b);
	}
	



	private void getSubCategories(ResultReceiver receiver, Bundle b) throws ClientProtocolException, IOException, Exception{
		Log.d(TAG, "OK in getSubCategories ");
		// TODO not hardcode
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpResponse response = client.execute(new HttpGet("http://eiffel.itba.edu.ar/hci/service/Catalog.groovy" +
				"?method=GetSubcategoryList&language_id=1&category_id=1"));
		
		if ( response.getStatusLine().getStatusCode() != 200 ) {
			throw new IllegalArgumentException(response.getStatusLine().toString());
		}
		final String xmlToParse = EntityUtils.toString(response.getEntity());
		
		b.putSerializable("return", (Serializable)fromXMLtoSubCategories(xmlToParse));
		receiver.send(STATUS_OK, b);
		
	}
	
	private void getProductsSub(ResultReceiver receiver, Bundle b) throws ClientProtocolException, IOException, Exception {
		Log.d(TAG, "OK in getProducts ");
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpResponse response = client.execute(new HttpGet("http://eiffel.itba.edu.ar/hci/service/Catalog.groovy" +
				"?method=GetProductListBySubcategory&language_id=1&category_id=1&subcategory_id=1"));
		if ( response.getStatusLine().getStatusCode() != 200 ) {
			throw new IllegalArgumentException(response.getStatusLine().toString());
		}
		
		final String xmlToParse = EntityUtils.toString(response.getEntity());

		b.putSerializable("return", (Serializable)fromXMLtoProduct(xmlToParse));
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
	
	
	private List<Product> fromXMLtoProduct(String xmlToParse) {
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
            	//TODO get the id and subcategories
            	Product product = new ProductImpl();		
                Node node = nodeList.item(i);
                Element productE = (Element) node;
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
	
	
}
	