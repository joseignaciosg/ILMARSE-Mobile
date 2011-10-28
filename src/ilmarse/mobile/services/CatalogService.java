package ilmarse.mobile.services;
import ilmarse.mobile.model.api.Category;
import ilmarse.mobile.model.api.CategoryProvider;
import ilmarse.mobile.model.impl.CategoryImpl;
import ilmarse.mobile.model.impl.CategoryProviderImpl;

import java.io.IOException;
import java.io.Serializable;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.sax.Element;
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
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpResponse response = client.execute(new HttpGet("http://eiffel.itba.edu.ar/hci/service/Catalog.groovy" +
				"?method=GetCategoryList&language_id=1"));
		if ( response.getStatusLine().getStatusCode() != 200 ) {
			throw new IllegalArgumentException(response.getStatusLine().toString());
		}
		
		Log.d(TAG, "OK in getCategories ");

		b.putSerializable("reuturn", (Serializable)fromXMLtoCategories(response));
		receiver.send(STATUS_OK, b);
	}
	
	
	private List<Category> fromXMLtoCategories(HttpResponse response) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<Category> retCategories = new ArrayList<Category>();
        try {
        	DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(response.getEntity().getContent());
            Element root = (Element) doc.getElementsByTagName(CATEGORIES);
            NodeList categories = ((Document) root).getElementsByTagName(CATEGORY);
            for (int i=0;i<categories.getLength();i++){
            	//TODO get the id and subcategories
                Category category = new CategoryImpl();		
                Node item = categories.item(i);
                NodeList properties = item.getChildNodes();
                for (int j=0;j<properties.getLength();j++){
                    Node property = properties.item(j);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase(CODE)){
                        category.setCode(property.getFirstChild().getNodeValue());
                    } else if (name.equalsIgnoreCase(NAME)){
                        category.setName(property.getFirstChild().getNodeValue());
                    }
                    retCategories.add(category);
                }
           }
        } catch (Exception e) {
			Log.e(TAG, e.getMessage());
//            throw new RuntimeException(e);
        } 
        Log.d(TAG, retCategories.toString());
        return retCategories;
		
	}
	
	
//	private List<Tweet> fromJSONtoTweets(final String jsonToParse) throws JSONException {
//		List<Tweet> tweets = new ArrayList<Tweet>();
//		
//		Log.d(TAG, "Json received: " + jsonToParse);
//		
//		JSONObject parsedJson = new JSONObject(jsonToParse);
//		if ( !parsedJson.has("results")) {
//			throw new JSONException("results not found");
//		}
//		
//		JSONArray results = parsedJson.getJSONArray("results");
//		for ( int i = 0; i < results.length(); i++ ) {
//			JSONObject bornToBeTweet = results.getJSONObject(i);
//			tweets.add(TweetImpl.fromJSON(bornToBeTweet));			
//		}
//		
//		return tweets;
//	}

}