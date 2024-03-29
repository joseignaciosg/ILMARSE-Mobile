package ilmarse.mobile.activities;

import ilmarse.mobile.model.api.Order;
import ilmarse.mobile.model.impl.OrderImpl;
import ilmarse.mobile.services.OrderService;
import ilmarse.mobile.services.SecurityService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



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
		String phoneLanguage = getResources().getConfiguration().locale
				.getLanguage();
		if (phoneLanguage.equals("en"))
			setTitle("Orders");
		else
			setTitle("Ordenes");
		SharedPreferences settings  = getSharedPreferences("LOGIN",0);
		Map<String, ?> map = settings.getAll();
		String username = (String)map.get("username");
		String token = (String)map.get("token");
		Log.d(TAG, username +"-"+token);
		Bundle bundle = new Bundle();
		bundle.putString("username", username);
		bundle.putString("token", token);
		intent.putExtras(bundle);
		

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
				String phoneLanguage = getResources().getConfiguration().locale
						.getLanguage();
				
				if (order_statusView != null) {
					String status = o.getStatus();
					if (status.equals("1")){
						if (phoneLanguage.equals("es"))
							order_statusView.setText("creada");
						else
							order_statusView.setText("created");
					}else if(status.equals("2")){
						if (phoneLanguage.equals("es"))
							order_statusView.setText("confirmada");
						else
							order_statusView.setText("confirmed");
					}else if(status.equals("3")){
						if (phoneLanguage.equals("es"))
							order_statusView.setText( "despachada");
						else
							order_statusView.setText( "shipped");
					}else {
						if (phoneLanguage.equals("es"))
							order_statusView.setText( "entregada" );
						else
							order_statusView.setText( "delivered" );
					}
				}
				if(image_statusView != null){
					Log.d(TAG, "inside getView " + o.toString());
					downloadFile(image_statusView,o);
				}
			}
			return v;
		}
	}

	void downloadFile(ImageView image_place,Order o) {
		String status = o.getStatus();
		
		if (status.equals("1")){
			image_place.setImageResource(R.drawable.pb1);
		}else if(status.equals("2")){
			image_place.setImageResource(R.drawable.pb2);
		}else if(status.equals("3")){
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
            new AlertDialog.Builder(this)
            .setTitle(getString(R.string.about_option_menu))
            .setMessage(getString(R.string.about_us_dialog))
            .setPositiveButton(android.R.string.ok, null)
            .show();
			break;
		case R.id.logout_option_menu:
			Log.d(TAG, "logout_option_menu pressed");
			/*intent for log out petition*/
			Intent logoutIntent = new Intent(OrdersActivity.this,
					SecurityService.class);
			
			/*log out progress dialog*/
			final ProgressDialog logoutDialog = ProgressDialog
					.show(OrdersActivity.this, "",
							getString(R.string.log_out_validation), true);
			
			/*looking for current user and token*/
			SharedPreferences settings  = getSharedPreferences("LOGIN",0);
			Map<String, ?> map = settings.getAll();
			String username = (String)map.get("user");
			String token = (String)map.get("token");
			Log.d(TAG, username +"-"+token);

			
			Bundle b = new Bundle();
			b.putString("username", username);
			b.putString("token", token);
			
			logoutIntent.putExtras(b);
			logoutIntent.putExtra("command",SecurityService.LOGOUT_CMD);
			logoutIntent.putExtra("receiver", new ResultReceiver(		new Handler()) {
				@Override
				protected void onReceiveResult(int resultCode,	Bundle resultData) {
					
					super.onReceiveResult(resultCode, resultData);

					logoutDialog.dismiss();
					if (resultCode == SecurityService.STATUS_OK) {

						/*TODO REMOVE SharedPreferences settings*/
						Intent loadLogInView = new Intent(
								OrdersActivity.this, LoginActivity.class);
						startActivity(loadLogInView);
						Toast.makeText(OrdersActivity.this,
								 getString(R.string.log_out_msg),
								Toast.LENGTH_SHORT / Toast.LENGTH_LONG)
								.show();


					} else {
						Log.d(TAG, "unknown error");
					}
				}
				});
			startService(logoutIntent);
			break;
		
			
		case R.id.home_option_menu:
			Intent showHomeView = new Intent(OrdersActivity.this,
					MainActivity.class);
			startActivity(showHomeView);
			break;
		}
		return true;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.d(TAG, "Inside onListItemClick!.");
		Object o = this.getListAdapter().getItem(position);
		Log.d(TAG, "Leaving onListItemClick!.");
		String orderid = ((OrderImpl)o).getId()+"";
		String username = ((OrderImpl)o).getUsername();
		String token = ((OrderImpl)o).getToken();
		String location = ((OrderImpl)o).getLocation();
		String created_date = ((OrderImpl)o).getCreated_date();
		String status = ((OrderImpl)o).getStatus();
		Bundle bundle = new Bundle();
		bundle.putString("username",username);
		bundle.putString("token",token);
		bundle.putString("order_id",orderid);
		bundle.putString("location",location);
		bundle.putString("created_date",created_date);
		bundle.putString("status",status);


		Intent newIntent = new Intent(OrdersActivity.this,
				OrderDetailActivity.class);
		newIntent.putExtras(bundle);
		OrdersActivity.this.startActivity(newIntent);

	}
	
}
