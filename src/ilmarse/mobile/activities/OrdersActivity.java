package ilmarse.mobile.activities;

import ilmarse.mobile.model.api.Order;
import ilmarse.mobile.services.OrderService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class OrdersActivity extends ListActivity {

	private String TAG = getClass().getSimpleName();
	HashMap<String, Order> orderMap = new HashMap<String, Order>();
	List<String> ordersIds = new ArrayList<String>();
	private List<Order> orders = null;
	private OrderAdapter o_adapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Asociamos la vista del search list con la activity */
		setContentView(R.layout.orders);

		Log.d(TAG, "inside onCreate");
		
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this, OrderService.class);
//		Bundle bundle = new Bundle();
//		intent.putExtras(bundle);

		intent.putExtra("command", OrderService.GET_ORDERS_CMD);
		/* Se pasa un callback (ResultReceiver), con el cual se procesará la
		 * respuesta del servicio. Si se le pasa null como parámetro del constructor
		 * se usa uno de los threads disponibles del proceso. Dado que en el procesamiento
		 * del mismo se debe modificar la UI, es necesario que ejecute con el thread de UI.
		 * Es por eso que se lo instancia con un objeto Handler (usando el el thread de UI
		 * para ejecutarlo).
		 */
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				if (resultCode == OrderService.STATUS_OK) {
					
					Log.d(TAG, "OK received info");

					@SuppressWarnings("unchecked")
					List<Order> list = (List<Order>) resultData.getSerializable("return");
					orders = list;
					Log.d(TAG, orders.toString());
					Log.d(TAG, "inside receiver");
					Log.d(TAG,list.toString());
//					populateList( new ProductProviderImpl(list) );
					/*change this TODO*/
					for(Order c: list){
						orderMap.put(c.getId()+"",c);
						ordersIds.add(c.getId()+"");
					}
					o_adapter = new OrderAdapter(OrdersActivity.this,
							R.layout.order_list_item, orders);
					Log.d(TAG, "inside onCreate");
					setListAdapter(o_adapter);
					Log.d(TAG, "setListAdapter");
					


//					populateList( new ProductsProviderImpl(list) );

				} else if (resultCode == OrderService.STATUS_CONNECTION_ERROR) {
					Log.d(TAG, "Connection error.");
				} else {
					Log.d(TAG, "Unknown error.");
				}
			}


		});
		startService(intent);
	}
	
	
	
	
	private class OrderAdapter extends ArrayAdapter<Order> {
		private List<Order> order_items;


		public OrderAdapter(Context context, int textViewResourceId,
				List<Order> items) {
			super(context, textViewResourceId, items);
			this.order_items = items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.order_list_item, null);
			}
			Order o =  order_items.get(position);
			if (o != null) {
//				TextView order_idView = (TextView) v.findViewById(R.id.o_order_id);
//				TextView creation_dateView = (TextView) v.findViewById(R.id.o_creation_date);
//				TextView order_statusView = (TextView) v.findViewById(R.id.o_order_status);
//		        ImageView image_statusView = (ImageView) v.findViewById(R.id.o_image_status);
//
//
//				if (order_idView != null) {
//					order_idView.setText(o.getId());
//				}
//				if (creation_dateView != null) {
//					creation_dateView.setText( o.getCreated_date());
//				}
//				if (order_statusView != null) {
//					order_statusView.setText( o.getStatus());
//				}
//				if(image_statusView != null){
//					Log.d(TAG, "inside getView " + o.getStatus());
//					downloadFile(image_statusView,o.getStatus());
//				}
			}
			return v;
		}
	}

	void downloadFile(ImageView image_place,String status) {
		URL myFileUrl = null;
		String progressbar1 = "http://i.ebayimg.com/t/20-Bar-Husqvarna-353-440E-444-445-450-UHLX20-58PJ-/04/!Bwjo3y!EGk~$(KGrHqIOKj4Ewg)g9yY7BMJiuFj5cw~~_35.GIF";
		String progressbar2 = "http://i.ebayimg.com/t/20-Bar-Husqvarna-353-440E-444-445-450-UHLX20-58PJ-/04/!Bwjo3y!EGk~$(KGrHqIOKj4Ewg)g9yY7BMJiuFj5cw~~_35.GIF";
		String progressbar3 = "http://i.ebayimg.com/t/20-Bar-Husqvarna-353-440E-444-445-450-UHLX20-58PJ-/04/!Bwjo3y!EGk~$(KGrHqIOKj4Ewg)g9yY7BMJiuFj5cw~~_35.GIF";
		String progressbar4 = "http://i.ebayimg.com/t/20-Bar-Husqvarna-353-440E-444-445-450-UHLX20-58PJ-/04/!Bwjo3y!EGk~$(KGrHqIOKj4Ewg)g9yY7BMJiuFj5cw~~_35.GIF";
		String fileUrl =null;
		
		if (status == "created"){
			fileUrl = progressbar1;
		}else if(status == "confirmed"){
			fileUrl = progressbar2;
		}else if(status == "shipped"){
			fileUrl = progressbar3;
		}else {
			fileUrl = progressbar4;
		}
		
		try {
			myFileUrl = new URL(fileUrl);
			Log.d(TAG, fileUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			Bitmap bmImg = BitmapFactory.decodeStream(is);
			image_place.setImageBitmap(bmImg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
