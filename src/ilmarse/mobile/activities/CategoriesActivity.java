package ilmarse.mobile.activities;


import java.util.List;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import ilmarse.mobile.model.api.Category;
import ilmarse.mobile.model.api.CategoryProvider;
import ilmarse.mobile.model.impl.CategoryProviderImpl;
import ilmarse.mobile.services.CatalogService;


public class CategoriesActivity extends ListActivity {

	private String TAG = getClass().getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Asociamos la vista del search list con la activity */
		setContentView(R.layout.categories);

		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				CatalogService.class);

		intent.putExtra("command", CatalogService.GET_CAT_CMD);
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

					Log.d(TAG, "OK");

					@SuppressWarnings("unchecked")
					List<Category> list = (List<Category>) resultData
							.getSerializable("return");

					populateList(new CategoryProviderImpl(list));
					Log.d(TAG, "inside category receiver");

				} else if (resultCode == CatalogService.STATUS_CONNECTION_ERROR) {
					Log.d(TAG, "Connection error.");
				} else {
					Log.d(TAG, "Unknown error.");
				}
			}

		});
		startService(intent);
	}
	
	private void populateList(CategoryProvider prov) {
		ListAdapter adapter = new SimpleAdapter(this,
				prov.getCategoriesAsMap(), R.layout.categories_item,
				prov.getMapKeys(), new int[] { R.id.code,R.id.name });
		
		setListAdapter(adapter);	
	}
	
}
