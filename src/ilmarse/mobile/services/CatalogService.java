package ilmarse.mobile.services;
import ilmarse.mobile.model.api.Category;
import ilmarse.mobile.model.api.CategoryProvider;
import ilmarse.mobile.model.impl.CategoryImpl;
import ilmarse.mobile.model.impl.CategoryProviderImpl;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
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

	  // names of the XML tags
    static final String CODE = "code";
    static final  String NAME = "name";
    static final  String CATEGORY = "category";
    static final String CATEGORIES = "categories";
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
		
//	     Document doc = db.parse(new InputSource(new StringReader(response)));
		final String xmlToParse = EntityUtils.toString(response.getEntity());

		b.putSerializable("return", (Serializable)fromXMLtoCategories(xmlToParse));
		receiver.send(STATUS_OK, b);
	}
	
	
	private List<Category> fromXMLtoCategories(String xmlToParse) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Category> retCategories = new ArrayList<Category>();
        try {
        	
//        	NodeList websiteList = fstElmnt.getElementsByTagName("website");
//        	Element websiteElement = (Element) websiteList.item(0);
//        	websiteList = websiteElement.getChildNodes();
//        	website[i].setText("Website = "
//        	+ ((Node) websiteList.item(0)).getNodeValue());
//        	category[i].setText("Website Category = "
//        	+ websiteElement.getAttribute("category"));
        	
        	DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlToParse)));
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName(CATEGORIES);
            Node categoriesTag =nodeList.item(0);
            for (int i=0;i<categoriesTag.getChildNodes().getLength(); i++) {
            	//TODO get the id and subcategories
            	Category category = new CategoryImpl();		
                Node item = categoriesTag.getChildNodes().item(i);
//                category.setId( Integer.parseInt( item.getAttribute("id") ) );
//                category.setId( 1 );
                NodeList properties = item.getChildNodes();
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
			Log.e(TAG, "here!");
        } 
        Log.d(TAG, retCategories.toString());
        return retCategories;
		
	}
}
	