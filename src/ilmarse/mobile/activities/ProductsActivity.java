package ilmarse.mobile.activities;


import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.api.ProductsProvider;
import ilmarse.mobile.model.impl.ProductProviderImpl;
import ilmarse.mobile.services.CatalogService;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;


public class ProductsActivity extends ListActivity {

	private String TAG = getClass().getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Asociamos la vista del search list con la activity */
		setContentView(R.layout.products);

		Intent intent = new Intent(Intent.ACTION_SYNC, null, this, CatalogService.class);

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
					Log.d(TAG,list.toString());
					populateList( new ProductProviderImpl(list) );
					Log.d(TAG, "inside product receiver");

				} else if (resultCode == CatalogService.STATUS_CONNECTION_ERROR) {
					Log.d(TAG, "Connection error.");
				} else {
					Log.d(TAG, "Unknown error.");
				}
			}

		});
		startService(intent);
	}
	
	private void populateList(ProductsProvider prov) {
		Log.d(TAG, "OK  populating category list");
		ListAdapter adapter = new SimpleAdapter(this,
				prov.getProductsAsMap(), R.layout.products_item,
				prov.getMapKeys(), new int[] {  R.id.code,R.id.name,R.id.id });
		setListAdapter(adapter);	
	}
	
	public void didclick(View v) {

		System.out.println(((Button)v).getId());
        Log.d("asd","You clicked btn2 - uses an anonymouse inner class");
		Intent intent = new Intent( ProductsActivity.this, SubcategoriesActivity.class );
		startActivity(intent);
    }

}
