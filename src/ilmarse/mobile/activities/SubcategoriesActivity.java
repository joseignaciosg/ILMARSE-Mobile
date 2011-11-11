package ilmarse.mobile.activities;


import ilmarse.mobile.model.api.Category;
import ilmarse.mobile.model.api.CategoryProvider;
import ilmarse.mobile.model.api.Subcategory;
import ilmarse.mobile.model.impl.CategoryProviderImpl;
import ilmarse.mobile.model.impl.SubCategoryProviderImpl;
import ilmarse.mobile.services.CatalogService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class SubcategoriesActivity extends ListActivity {

	private String TAG = getClass().getSimpleName();
	HashMap<String, Subcategory> subcategoriesMap = new HashMap<String, Subcategory>();
	List<String> subcatNames = new ArrayList<String>();
	int catId;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG,"inside onCreate");
		
		/* Asociamos la vista del search list con la activity */
		setContentView(R.layout.categories);

		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,CatalogService.class);
		
		String catName = this.getIntent().getExtras().getString("catname");
		catId = new Integer(this.getIntent().getExtras().getString("catid"));
		Bundle bundle = new Bundle();
		bundle.putString("catid", catId+"");
		bundle.putString("catname", catName);
		intent.putExtras(bundle);
		
//		setTitle(getString(R.string.products) + " > " + catName);

		intent.putExtra("command", CatalogService.GET_SUBCAT_CMD);
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
					List<Subcategory> list = (List<Subcategory>) resultData.getSerializable("return");
					Log.d(TAG,list.toString());
//					populateList( new SubCategoryProviderImpl(list) );
					/*change this TODO*/
					for(Subcategory c: list){
						subcategoriesMap.put(c.getName(),c);
						subcatNames.add(c.getName());
					}
					populateList( new SubCategoryProviderImpl(list) );
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
		Log.d(TAG, "OK  populating subcategory list");
		SubcategoriesActivity.this.setListAdapter(new ArrayAdapter<String>(
				SubcategoriesActivity.this,
				R.layout.categories_item, subcatNames));
		Log.d(TAG, subcatNames.toString());

		/*ListAdapter adapter = new SimpleAdapter(this,
				prov.getCategoriesAsMap(), R.layout.categories_item,
				prov.getMapKeys(), new int[] {  R.id.code,R.id.name,R.id.id });
		setListAdapter(adapter);	*/
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.d(TAG, "Inside onListItemClick!.");
		Object o = this.getListAdapter().getItem(position);
		Log.d(TAG, "Leaving onListItemClick!.");
		String cat = o.toString();
		Bundle bundle = new Bundle();
		bundle.putString("catid", catId+"");
		bundle.putString("subcatid", subcategoriesMap.get(cat).getId() + "");
		
		Intent newIntent = new Intent(SubcategoriesActivity.this,
				ProductsActivity.class);
		newIntent.putExtras(bundle);
		SubcategoriesActivity.this.startActivity(newIntent);

	}
	
}

