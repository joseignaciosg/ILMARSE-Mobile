package ilmarse.mobile.services;
import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.impl.ProductImpl;

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

public class SearchService extends IntentService {

	private final String TAG = getClass().getSimpleName();

	public static final String GET_PRODUCTS_CMD = "GetCategories";

	
	
    static final  String ID = "id";
	public static final int STATUS_CONNECTION_ERROR = -1;
	public static final int STATUS_ERROR = -2;
	public static final int STATUS_ILLEGAL_ARGUMENT = -3;
	public static final int STATUS_OK = 0;

	/*
	 * Se debe crear un constructor sin parametros y asignarle un nombre al
	 * servicio.
	 */
	public SearchService() {
		super("SearchService");
	}

	/*
	 * Evento que se ejecuta cuando se invocó el servicio por medio de un
	 * Intent.
	 */
	@Override
	protected void onHandleIntent(final Intent intent) {
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		final String command = intent.getStringExtra("command");
		final String query = intent.getStringExtra("query");

		final Bundle b = new Bundle();
		try {
			if (command.equals(GET_PRODUCTS_CMD)) {
				getProducts(receiver, b, query);
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


	private void getProducts(ResultReceiver receiver, Bundle b, String query) throws ClientProtocolException, IOException, Exception {
		Log.d(TAG, "OK in getCategories ");
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpResponse response = client.execute(new HttpGet("http://eiffel.itba.edu.ar/hci/service/Catalog.groovy" +
				"?method=GetProductListByName&criteria=" + query));
		if ( response.getStatusLine().getStatusCode() != 200 ) {
			throw new IllegalArgumentException(response.getStatusLine().toString());
		}
		
		final String xmlToParse = EntityUtils.toString(response.getEntity());

		b.putSerializable("return", (Serializable)fromXMLtoProduct(xmlToParse));
		receiver.send(STATUS_OK, b);
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