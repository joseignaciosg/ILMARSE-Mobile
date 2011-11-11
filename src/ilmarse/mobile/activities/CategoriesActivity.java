package ilmarse.mobile.activities;


import ilmarse.mobile.model.api.Category;
import ilmarse.mobile.model.api.CategoryProvider;
import ilmarse.mobile.model.impl.CategoryProviderImpl;
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


public class CategoriesActivity extends ListActivity {

	private String TAG = getClass().getSimpleName();
	HashMap<String, Category> categoriesMap = new HashMap<String, Category>();
	List<String> catNames = new ArrayList<String>();


	
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

					Log.d(TAG, "OK received info");

					@SuppressWarnings("unchecked")
					List<Category> list = (List<Category>) resultData.getSerializable("return");
					Log.d(TAG,list.toString());
					//populateList( new CategoryProviderImpl(list) );
					/*change this TODO*/
					for(Category c: list){
						categoriesMap.put(c.getName(),c);
						catNames.add(c.getName());
					}
					populateList( new CategoryProviderImpl(list) );
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
		Log.d(TAG, "OK  populating category list");
		CategoriesActivity.this.setListAdapter(new ArrayAdapter<String>(
				CategoriesActivity.this,
				R.layout.categories_item, catNames));
		Log.d(TAG, catNames.toString());

		/*ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setCacheColorHint(0);*/
		
	/*	ListAdapter adapter = new SimpleAdapter(this,
				prov.getCategoriesAsMap(), R.layout.categories_item,
				prov.getMapKeys(), new int[] {  R.id.code,R.id.name,R.id.id });
		setListAdapter(adapter);	*/
	}
	
	/*public void didclick(View v) {
        Log.d("asd","You clicked btn2 - uses an anonymouse inner class");
		Intent intent = new Intent( CategoriesActivity.this, SubcategoriesActivity.class );
		startActivity(intent);
    }*/
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.d(TAG, "Inside onListItemClick!.");
		Object o = this.getListAdapter().getItem(position);
		Log.d(TAG, "Leaving onListItemClick!.");
		String cat = o.toString();
		Bundle bundle = new Bundle();
		bundle.putString("catid", categoriesMap.get(cat).getId() + "");
		bundle.putString("catname", categoriesMap.get(cat).getName());
		Intent newIntent = new Intent(CategoriesActivity.this,
				SubcategoriesActivity.class);
		newIntent.putExtras(bundle);
		CategoriesActivity.this.startActivity(newIntent);

	}


}
