package ilmarse.mobile.services;

import ilmarse.mobile.activities.OrdersActivity;
import ilmarse.mobile.model.api.Order;
import ilmarse.mobile.model.impl.OrderImpl;

import java.io.IOException;
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
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.R;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class NotificationsService extends IntentService {

	private final String TAG = getClass().getSimpleName();

	private static final String apiURL = "http://eiffel.itba.edu.ar/hci/service/";
	public static final int STATUS_OK = 0;
	public static final String LOGIN = "Login";

	public NotificationsService() {
		super("NotificationsService");
	}

	@Override
	protected void onHandleIntent(final Intent intent) {
		Log.d(TAG, "inside Onhandle intent");
		try {
			checkForOrderUpdates();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Es importante terminar el servicio lo antes posible
		// No se llega aca salvo que se interrumpa
		this.stopSelf();
	}

	private void checkForOrderUpdates() throws ClientProtocolException,
			IOException, JSONException {

		SharedPreferences settings = getSharedPreferences("LOGIN", 0);

		Editor editor = settings.edit();
		if (!settings.contains("Orders")) {
			editor.putString("Orders", getOrdersAsString());
			editor.commit();
		}

		List<Order> oldOrders = getOrders(settings.getString("Orders", ""));
		String phoneLanguage = this.getResources().getConfiguration().locale
				.getLanguage();

		while (true) {

			// Toast.makeText(this, "This is a notification!",
			// Toast.LENGTH_LONG).show();
			Log.d(TAG, "Checking for orders updates");

			String newOrdersStr = getOrdersAsString();
			List<Order> newOrders = getOrders(newOrdersStr);
			checkForOrderChanges(newOrders, oldOrders, phoneLanguage);

			editor.putString("Orders", newOrdersStr);
			editor.commit();
			oldOrders = newOrders;

			int time = Integer.parseInt(settings.getString("timeTillCheck","30000"));//30seconds
			Log.d(TAG, "timeTillCheck:"+time);

			try {
				Thread.sleep(time);
			} catch (Exception e) {
				// interrupted
				Log.d(TAG, "interrupted");
			}

		}

	}

	private List<Order> getOrders(String xmlToParse) {
		Log.d(TAG, "inside getOrders");

		List<Order> list = new ArrayList<Order>();
		// list = ordersProvider.getOrders(username,token);
		list = fromXMLtoOrders(xmlToParse);

		return list;

	}

	private List<Order> fromXMLtoOrders(String xmlToParse) {
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
				System.out.println("orderid:" + id);
				order.setId(Integer.parseInt(id));
				Log.d("TAG", "id=" + String.valueOf(order.getId()));
				NodeList properties = orderE.getChildNodes();
				Log.d(TAG, "properties lenght:" + properties.getLength());
				for (int j = 1; j < properties.getLength() - 1; j++) {
					Node property = properties.item(j);
					String name = property.getNodeName();
					Log.d(TAG, name);
					if (name.equalsIgnoreCase("address_id")) {
						// order.set(Integer.valueOf(property
						// .getFirstChild().getNodeValue()));
					} else if (name.equalsIgnoreCase("status")) {
						order.setStatus(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("created_date")) {
						order.setCreated_date(property.getFirstChild()
								.getNodeValue());
					} else if (name.equalsIgnoreCase("confirmed_date")) {
						// order.setConfirmed_date(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("shipped_date")) {
						// order.setShipped_date(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("delivered_date")) {
						// order.setDelivered_date(property.getFirstChild()
						// .getNodeValue());
					} else if (name.equalsIgnoreCase("latitude")) {
						order.setLatitude(property.getFirstChild()
								.getNodeValue());
					} else if (name.equalsIgnoreCase("longitude")) {
						order.setLongitude(property.getFirstChild()
								.getNodeValue());
					}
				}
				// Random
				// order.setLatitude(latitude)
				// order.serLo
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

	private String getOrdersAsString() throws IOException,
			ClientProtocolException {
		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpResponse response;
		// Get the phone actual language
		SharedPreferences settings = getSharedPreferences("LOGIN", 0);
		String user = settings.getString("user", "NOUSER");
		String token = settings.getString("token", "NOTOKEN");

		String com = apiURL + "Order.groovy?method=GetOrderList";
		com += "&username=" + user;
		com += "&authentication_token=" + token;
		response = client.execute(new HttpGet(com));
		final String xmlToParse = EntityUtils.toString(response.getEntity(),
				"UTF-8");
		return xmlToParse;
	}

	private void checkForOrderChanges(List<Order> newOrders, List<Order> oldOrders,
			String phoneLanguage) {
		for( int i=0; i<newOrders.size();i++){
			OrderImpl order = (OrderImpl)newOrders.get(i);
			int orderID = order.getId();
			for( int j=0; j<oldOrders.size();j++){
				OrderImpl oldOrder = (OrderImpl)oldOrders.get(j);
				if ( oldOrder.getId() == orderID 
						&& !oldOrder.getStatus().equals(order.getStatus())) {

					String tText, cTitle, cText;
					if (phoneLanguage.equals("en")) {
						tText = "An order has changed!";
						cTitle = "Order " + order.getId()  + " has changed";
						cText = "Order " + order.getId() + " ";
						cText += "is now " + order.getStatus() +". ";
						if (!(order.getLatitude().equals("") || order.getLongitude().equals(""))) {
							cText += "Lattitude: " + order.getLatitude() + "Longitude: "
									+ order.getLongitude() + ".";
						}

					} else {
						tText = "Una orden ha cambiado!";
						cTitle = "La orden " + order.getId() + " ha cambiado";
						cText = "La  orden " + order.getId() + " ";
						cText += "ahora esta " + order.getStatus() + ". ";
						if (!(order.getLatitude().equals("") || order.getLongitude().equals(""))) {
							cText += "Latitud: " + order.getLatitude() + "Longitud: "
									+ order.getLongitude() + ".";
						}
					}
					sendNotification(tText, cTitle, cText);
				}
			}
		}
	}

	private void sendNotification(String tText, String cTitle, String cText) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
//		int icon = R.drawable.aboutmicon;
		int icon = 0x7f020000;
		CharSequence tickerText = tText;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		Context context = getApplicationContext();
		CharSequence contentTitle = cTitle;
		CharSequence contentText = cText;
		Intent notificationIntent = new Intent(this, OrdersActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		mNotificationManager.notify(-1, notification);
	}

}