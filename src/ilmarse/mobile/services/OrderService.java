package ilmarse.mobile.services;

import ilmarse.mobile.activities.R;
import ilmarse.mobile.model.api.Order;
import ilmarse.mobile.model.api.OrdersProvider;
import ilmarse.mobile.model.impl.Book;
import ilmarse.mobile.model.impl.OrderImpl;
import ilmarse.mobile.model.impl.OrdersProviderMock;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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

public class OrderService extends IntentService {

	private final String TAG = getClass().getSimpleName();
	private final String APIurl = "http://eiffel.itba.edu.ar/hci/service/";

	private OrdersProvider ordersProvider = new OrdersProviderMock();
	public static final String GET_ORDERS_CMD = "GetOrders";

	public static final int STATUS_CONNECTION_ERROR = -1;
	public static final int STATUS_ERROR = -2;
	public static final int STATUS_ILLEGAL_ARGUMENT = -3;
	public static final int STATUS_OK = 0;

	public OrderService() {
		super("OrderService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.d(TAG, "inside onHandleIntent");

		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		final String command = intent.getStringExtra("command");
		final Bundle b = new Bundle();
		if (command.equals(GET_ORDERS_CMD)) { // command for receiving available
												// categories
			String username = intent.getStringExtra("username");
			String token = intent.getStringExtra("token");
			Log.d(TAG, username +"-"+token);
			b.putString("username",username);
			b.putString("token",token);
			getOrders(receiver, b);
		}

		this.stopSelf();
	}

	private void getOrders(ResultReceiver receiver, Bundle b) {
		Log.d(TAG, "inside getOrders");

		// the user must be signed in
		 String username = b.getString("username");
		 String token = b.getString("token");
//		String username = "hci";
//		String token = "702818a8294e9d554acaf62fafa4563";
		String xmlToParse = null;

		final DefaultHttpClient client = new DefaultHttpClient();
		/* gets the phone current language */
		final HttpResponse response;
		try {
			response = client.execute(new HttpGet(APIurl
					+ "Order.groovy?method=GetOrderList&username=" + username
					+ "&authentication_token=" + token));
			xmlToParse = EntityUtils.toString(response.getEntity());
			Log.d(TAG, xmlToParse.toString());
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Order> list;
		// list = ordersProvider.getOrders(username,token);
		list = fromXMLtoOrders(xmlToParse, username, token);

		b.putSerializable("return", (Serializable) list);
		receiver.send(STATUS_OK, b);

	}

	private List<Order> fromXMLtoOrders(String xmlToParse, String username,
			String token) {
		List<Order> ret = new ArrayList<Order>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(xmlToParse));
			Document doc = builder.parse(inStream);
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("order");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Order order;
				Node node;
				Element orderE;
				order = new OrderImpl();
				node = nodeList.item(i);
				orderE = (Element) node;
				String id = orderE.getAttribute("id");
				System.out.println("orderid:"+id);
				order.setId(Integer.parseInt(id));
				Log.d("TAG", "id=" + String.valueOf(order.getId()));
				NodeList properties = orderE.getChildNodes();
				Log.d(TAG, "properties lenght:"+properties.getLength());
				for (int j = 1; j < properties.getLength()-1; j++) {
					Node property = properties.item(j);
					String name = property.getNodeName();
					Log.d(TAG, name);
					if (name.equalsIgnoreCase("address_id")) {
//						order.set(Integer.valueOf(property
//								.getFirstChild().getNodeValue()));
					} else if (name.equalsIgnoreCase("status")) {
						order.setStatus(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("created_date")) {
						order.setCreated_date(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("confirmed_date")) {
//						order.setConfirmed_date(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("shipped_date")) {
//						order.setShipped_date(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("delivered_date")) {
//						order.setDelivered_date(property.getFirstChild()
//								.getNodeValue());
					} else if (name.equalsIgnoreCase("latitude")) {
						order.setLatitude(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("longitude")) {
						order.setLongitude(property.getFirstChild()
								.getNodeValue());
					} 
				}
//				Random 
//				order.setLatitude(latitude)
//				order.serLo
				ret.add(order);
				Log.d("TAG", "asd" + order.toString());
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			Log.e(TAG, "here!");
		}
		return ret;

	}

}
