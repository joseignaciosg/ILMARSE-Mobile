package ilmarse.mobile.activities;


import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.api.ProductsProvider;
import ilmarse.mobile.model.impl.ProductsProviderImpl;
import ilmarse.mobile.model.impl.SimpleProduct;
import ilmarse.mobile.services.CatalogService;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class ProductsActivity extends ListActivity {

	private String TAG = getClass().getSimpleName();
	HashMap<String, Product> productMap = new HashMap<String, Product>();
	List<String> productNames = new ArrayList<String>();
	private List<Product> m_products = null;
	private ProductAdapter m_adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Asociamos la vista del search list con la activity */
		setContentView(R.layout.products);
		
		
		int catId = new Integer(this.getIntent().getExtras().getString("catid"));
		int subcatId = new Integer(this.getIntent().getExtras().getString("subcatid"));
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this, CatalogService.class);
		Bundle bundle = new Bundle();
		bundle.putString("catid", catId+"");
		bundle.putString("subcatid", subcatId+"");
		intent.putExtras(bundle);

		intent.putExtra("command", CatalogService.GET_PRODUCTS_CMD);
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
				if (resultCode == CatalogService.STATUS_OK) {
					
					Log.d(TAG, "OK received info");

					@SuppressWarnings("unchecked")
					List<Product> list = (List<Product>) resultData.getSerializable("return");
					m_products = list;
					Log.d(TAG, "inside receiver");
					Log.d(TAG,list.toString());
//					populateList( new ProductProviderImpl(list) );
					Log.d(TAG, "inside product receiver");
					/*change this TODO*/
					for(Product c: list){
						productMap.put(c.getName(),c);
						productNames.add(c.getName());
					}
					m_adapter = new ProductAdapter(ProductsActivity.this,
							R.layout.product_list_item, m_products);
					Log.d(TAG, "inside onCreate");
					setListAdapter(m_adapter);
					Log.d(TAG, "setListAdapter");

//					populateList( new ProductsProviderImpl(list) );

				} else if (resultCode == CatalogService.STATUS_CONNECTION_ERROR) {
					Log.d(TAG, "Connection error.");
				} else {
					Log.d(TAG, "Unknown error.");
				}
			}

		});
		startService(intent);
	}
	
//	private void populateList(ProductsProvider prov) {
//		/*Log.d(TAG, "OK  populating category list");
//		ListAdapter adapter = new SimpleAdapter(this,
//				prov.getProductsAsMap(), R.layout.products_item,
//				prov.getMapKeys(), new int[] {  R.id.code,R.id.name,R.id.id });
//		setListAdapter(adapter);*/	
//		Log.d(TAG, "OK  populating product list");
//		ProductsActivity.this.setListAdapter(new ArrayAdapter<String>(
//				ProductsActivity.this,
//				R.layout.product_list_item, productNames));
//		Log.d(TAG, productNames.toString());
//	}
//	
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.d(TAG, "Inside onListItemClick!.");
		Object o = this.getListAdapter().getItem(position);
		Log.d(TAG, "Leaving onListItemClick!.");
		String productname = ((Product)o).getName().toString();
		Log.d(TAG, productname);
		Log.d(TAG,"product map:"+ productMap.toString());
		Bundle bundle = new Bundle();
		bundle.putString("prodid", productMap.get(productname).getId() + "");
		bundle.putString("catid", productMap.get(productname).getCategory_id()+ "");

		Intent newIntent = new Intent(ProductsActivity.this,
				ProductActivity.class);
		newIntent.putExtras(bundle);
		ProductsActivity.this.startActivity(newIntent);

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
					Log.d(TAG, "inside getView" + o.getImage_url());
//			        imageLoader.DisplayImage(o.getImageUrl(), ProductsActivity.this, image_place);
				}
			}
			return v;
		}
	}

	void downloadFile(String fileUrl,ImageView image_place) {
		URL myFileUrl = null;
		Log.d(TAG, fileUrl);
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
			Intent showSettingsView = new Intent(ProductsActivity.this,
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
			Intent showHomeView = new Intent(ProductsActivity.this,
					MainActivity.class);
			startActivity(showHomeView);
			break;
		}
		return true;
	}
}
