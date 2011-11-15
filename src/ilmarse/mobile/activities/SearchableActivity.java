package ilmarse.mobile.activities;

import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.api.ProductsProvider;
import ilmarse.mobile.model.impl.ProductsProviderImpl;
import ilmarse.mobile.services.CatalogService;
import ilmarse.mobile.services.SearchService;
import ilmarse.mobile.services.SecurityService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class SearchableActivity extends ListActivity{

	

	HashMap<String, Product> productMap = new HashMap<String, Product>();
	List<String> productNames = new ArrayList<String>();
	List<Product> p_list;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.products);

	    // Get the intent, verify the action and get the query
	    Intent intent = getIntent();
	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	      String query = intent.getStringExtra(SearchManager.QUERY);
	      

			Intent i = new Intent(Intent.ACTION_SYNC, null, this, SearchService.class);

			i.putExtra("command", SearchService.GET_PRODUCTS_CMD);
			i.putExtra("query", query);
			/* Se pasa un callback (ResultReceiver), con el cual se procesará la
			 * respuesta del servicio. Si se le pasa null como parámetro del constructor
			 * se usa uno de los threads disponibles del proceso. Dado que en el procesamiento
			 * del mismo se debe modificar la UI, es necesario que ejecute con el thread de UI.
			 * Es por eso que se lo instancia con un objeto Handler (usando el el thread de UI
			 * para ejecutarlo).
			 */
			i.putExtra("receiver", new ResultReceiver(new Handler()) {
				@Override
				protected void onReceiveResult(int resultCode, Bundle resultData) {
					super.onReceiveResult(resultCode, resultData);
					if (resultCode == SearchService.STATUS_OK) {

						Log.d("searching", "OK received info");

						@SuppressWarnings("unchecked")
						List<Product> list = (List<Product>) resultData.getSerializable("return");
						p_list = list;
						
						for(Product c: list){
							productMap.put(c.getName(),c);
							productNames.add(c.getName());
						}
						populateList( new ProductsProviderImpl(list) );

					} else if (resultCode == CatalogService.STATUS_CONNECTION_ERROR) {
						Log.d("searching", "Connection error.");
					} else {
						Log.d("searching", "Unknown error.");
					}
				}

			});
			startService(i);
	    }
	}
	
	
	private void populateList(ProductsProvider prov) {
//		Log.d("products", "OK  populating category list");
//		ListAdapter adapter = new SimpleAdapter(this,
//				prov.getProductsAsMap(), R.layout.products_item,
//				prov.getMapKeys(), new int[] {  R.id.code,R.id.name,R.id.id });
//		setListAdapter(adapter);	
		Log.d("searchactivity", "OK  populating product list");
		SearchableActivity.this.setListAdapter(new ProductAdapter(
				SearchableActivity.this,
				R.layout.product_list_item, p_list));
		Log.d("searchactivity", productNames.toString());
	}
	
	
	
	private class ProductAdapter extends ArrayAdapter<Product> {
		private List<Product> items;


		public ProductAdapter(Context context, int textViewResourceId,
				List<Product> items) {
			super(context, textViewResourceId, items);
			this.items = items;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.product_list_item, null);
			}
			Product o =  items.get(position);
			if (o != null) {
				TextView title_place = (TextView) v.findViewById(R.id.prod_title);
//				TextView rank_place = (TextView) v.findViewById(R.id.prod_ranking);
				TextView price_place = (TextView) v.findViewById(R.id.prod_price);
		        ImageView image_place = (ImageView) v.findViewById(R.id.prod_image);


				if (title_place != null) {
					title_place.setText(o.getName());
				}
//				if (rank_place != null) {
//					rank_place.setText("Ranking: " + o.getRank());
//				}
				if (price_place != null) {
					price_place.setText("$" + o.getPrice());
				}
				if(image_place != null){
					downloadFile(o.getImage_url(),image_place);
					Log.d("asd", "inside getView" + o.getImage_url());
//			        imageLoader.DisplayImage(o.getImageUrl(), ProductsActivity.this, image_place);
				}
			}
			return v;
		}
	}

	void downloadFile(String fileUrl,ImageView image_place) {
		URL myFileUrl = null;
		Log.d("dl", fileUrl);
		try {
			myFileUrl = new URL(fileUrl);
			Log.d("dl", fileUrl);
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
			Intent showSettingsView = new Intent(SearchableActivity.this,
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
			Log.d("pepa", "logout_option_menu pressed");
			/*intent for log out petition*/
			Intent logoutIntent = new Intent(SearchableActivity.this,
					SecurityService.class);
			
			/*log out progress dialog*/
			final ProgressDialog logoutDialog = ProgressDialog
					.show(SearchableActivity.this, "",
							getString(R.string.log_out_validation), true);
			
			/*looking for current user and token*/
			SharedPreferences settings  = getSharedPreferences("LOGIN",0);
			Map<String, ?> map = settings.getAll();
			String username = (String)map.get("user");
			String token = (String)map.get("token");
			
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
								SearchableActivity.this, LoginActivity.class);
						startActivity(loadLogInView);
						Toast.makeText(SearchableActivity.this,
								 getString(R.string.log_out_msg),
								Toast.LENGTH_SHORT / Toast.LENGTH_LONG)
								.show();


					} else {
						Log.d("pepa", "unknown error");
					}
				}
				});
			startService(logoutIntent);
			break;
		
			
		case R.id.home_option_menu:
			Intent showHomeView = new Intent(SearchableActivity.this,
					MainActivity.class);
			startActivity(showHomeView);
			break;
		}
		return true;
	}
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.d("pepa", "Inside onListItemClick!.");
		Object o = this.getListAdapter().getItem(position);
		Log.d("pepa", "Leaving onListItemClick!.");
		String productname = ((Product)o).getName().toString();
		Log.d("pepa", productname);
		Log.d("pepa","product map:"+ productMap.toString());
		Bundle bundle = new Bundle();
		bundle.putString("prodid", productMap.get(productname).getId() + "");
		bundle.putString("catid", productMap.get(productname).getCategory_id()+ "");

		Intent newIntent = new Intent(SearchableActivity.this,
				ProductActivity.class);
		newIntent.putExtras(bundle);
		SearchableActivity.this.startActivity(newIntent);

	}
	
}
