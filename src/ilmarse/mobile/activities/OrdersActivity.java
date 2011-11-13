package ilmarse.mobile.activities;

import ilmarse.mobile.model.api.Order;
import ilmarse.mobile.services.OrderService;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class OrdersActivity extends ListActivity {

	private String TAG = getClass().getSimpleName();
	HashMap<String, Order> orderMap = new HashMap<String, Order>();
	List<String> ordersIds = new ArrayList<String>();
	private List<Order> m_orders = null;
	private OrderAdapter m_adapter;
	
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
					m_orders = list;
					Log.d(TAG, m_orders.toString());
					Log.d(TAG, "inside receiver");
					Log.d(TAG,list.toString());
//					populateList( new ProductProviderImpl(list) );
					/*change this TODO*/
					for(Order c: list){
						orderMap.put(c.getId()+"",c);
						ordersIds.add(c.getId()+"");
					}
					m_adapter = new OrderAdapter(OrdersActivity.this, R.layout.order_list_item, m_orders);
					Log.d(TAG, "inside onCreate");
					setListAdapter(m_adapter);
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
				TextView order_idView = (TextView) v.findViewById(R.id.o_order_id);
				TextView creation_dateView = (TextView) v.findViewById(R.id.o_creation_date);
				TextView order_statusView = (TextView) v.findViewById(R.id.o_order_status);
		        ImageView image_statusView = (ImageView) v.findViewById(R.id.o_image_status);

				if (order_idView != null) {
					order_idView.setText(o.getId()+"");
				}
				if (creation_dateView != null) {
					creation_dateView.setText( o.getCreated_date());
				}
				if (order_statusView != null) {
					order_statusView.setText( o.getStatus());
				}
				if(image_statusView != null){
					Log.d(TAG, "inside getView " + o.getStatus());
					downloadFile(image_statusView,o.getStatus());
				}
			}
			return v;
		}
	}

	void downloadFile(ImageView image_place,String status) {
		
		
		if (status == "created"){
			image_place.setImageResource(R.drawable.pb1);
		}else if(status == "confirmed"){
			image_place.setImageResource(R.drawable.pb2);
		}else if(status == "shipped"){
			image_place.setImageResource(R.drawable.pb3);
		}else {
			image_place.setImageResource(R.drawable.pb4);
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings_option_menu:
			Intent showSettingsView = new Intent(OrdersActivity.this,
					SettingsActivity.class);
			startActivity(showSettingsView);
			break;
			
		case R.id.about_option_menu:
//            new AlertDialog.Builder(this)
//            .setTitle(getString(R.string.about_option_menu))
//            .setMessage(getString(R.string.about_us_dialog))
//            .setPositiveButton(android.R.string.ok, null)
//            .show();
			break;
			
		case R.id.home_option_menu:
			Intent showHomeView = new Intent(OrdersActivity.this,
					MainActivity.class);
			startActivity(showHomeView);
			break;
		}
		return true;
	}
	
}
