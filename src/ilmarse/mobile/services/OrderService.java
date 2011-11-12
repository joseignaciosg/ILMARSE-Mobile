package ilmarse.mobile.services;

import ilmarse.mobile.model.api.Order;
import ilmarse.mobile.model.api.OrdersProvider;
import ilmarse.mobile.model.impl.OrdersProviderMock;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

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
		if (command.equals(GET_ORDERS_CMD)) { // command for receiving available categories
			getOrders(receiver, b);
		}

		this.stopSelf();		
	}

	private void getOrders(ResultReceiver receiver, Bundle b) {
		Log.d(TAG, "inside getOrders");
		
		//the user must be signed in
//		String username = b.getString("username");
//		String token = b.getString("token");
		String username = "Juan Domingo Per—n";
		String token = "6374hdfn7dyfhjkd7";
		
		
//		final DefaultHttpClient client = new DefaultHttpClient();
//		/*gets the phone current language*/
//		final HttpResponse response;
//		response = client.execute(new HttpGet(APIurl + "Catalog.groovy?method=GetProduct&product_id"+prodId));
//		final String xmlToParse = EntityUtils.toString(response.getEntity());
//		Log.d(TAG, xmlToParse.toString());
		
		List<Order> list;
		list = ordersProvider.getOrders(username,token);
//		list = fromXMLtoProducts(xmlToParse,catId);

		b.putSerializable("return", (Serializable)list);
		receiver.send(STATUS_OK, b);
		
	}

}
