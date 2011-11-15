package ilmarse.mobile.activities;

import ilmarse.mobile.model.api.Category;
import ilmarse.mobile.model.impl.HtmlDesEncoder;
import ilmarse.mobile.services.CatalogService;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoriesActivity extends ListActivity {

	private String TAG = getClass().getSimpleName();

	public static ProgressDialog loadingCategories;
	// remove this
	HashMap<String, Category> categoriesMap = new HashMap<String, Category>();
	List<String> catNames = new ArrayList<String>();
	List<Category> categories;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* Asociamos la vista del search list con la activity */
		setContentView(R.layout.categories);

		String phoneLanguage = getResources().getConfiguration().locale
				.getLanguage();
		if (phoneLanguage.equals("en"))
			setTitle("Categories");
		else
			setTitle("Categor�as");
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				CatalogService.class);
		

		intent.putExtra("command", CatalogService.GET_CAT_CMD);
		/*
		 * Se pasa un callback (ResultReceiver), con el cual se procesará la
		 * respuesta del servicio. Si se le pasa null como parámetro del
		 * constructor se usa uno de los threads disponibles del proceso. Dado
		 * que en el procesamiento del mismo se debe modificar la UI, es
		 * necesario que ejecute con el thread de UI. Es por eso que se lo
		 * instancia con un objeto Handler (usando el el thread de UI para
		 * ejecutarlo).
		 */
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				if (resultCode == CatalogService.STATUS_OK) {

					Log.d(TAG, "OK received info");

					@SuppressWarnings("unchecked")
					List<Category> list = (List<Category>) resultData
							.getSerializable("return");
					categories = list;
					Log.d(TAG, list.toString());
					// populateList( new CategoryProviderImpl(list) );
					/* change this TODO */
					for (Category c : list) {
						categoriesMap.put(c.getName(), c);
						catNames.add(c.getName());
					}
					populateList();
					Log.d(TAG, "inside category receiver");

				} else if (resultCode == CatalogService.STATUS_CONNECTION_ERROR) {
					Intent loadLogInView = new Intent(
							CategoriesActivity.this, MainActivity.class);
					startActivity(loadLogInView);
					Toast.makeText(CategoriesActivity.this,
							getString(R.string.internet_error),
							Toast.LENGTH_SHORT / Toast.LENGTH_LONG)
							.show();
					Log.d(TAG, "Connection error.");
				} else {
					Log.d(TAG, "Unknown error.");
					Intent loadLogInView = new Intent(
							CategoriesActivity.this, MainActivity.class);
					startActivity(loadLogInView);
					Toast.makeText(CategoriesActivity.this,
							getString(R.string.internet_error),
							Toast.LENGTH_SHORT / Toast.LENGTH_LONG)
							.show();
				}
			}
		});
		startService(intent);
	}

	private void populateList() {
		Log.d(TAG, "OK  populating category list");
		// CategoriesActivity.this.setListAdapter(new ArrayAdapter<String>(
		// CategoriesActivity.this, R.layout.categories_item, catNames));
		CategoryAdapter c_adapter = new CategoryAdapter(
				CategoriesActivity.this, R.layout.categories_item, categories);
		setListAdapter(c_adapter);
		Log.d(TAG, catNames.toString());

		/*
		 * ListView lv = getListView(); lv.setTextFilterEnabled(true);
		 * lv.setCacheColorHint(0);
		 */

		/*
		 * ListAdapter adapter = new SimpleAdapter(this,
		 * prov.getCategoriesAsMap(), R.layout.categories_item,
		 * prov.getMapKeys(), new int[] { R.id.code,R.id.name,R.id.id });
		 * setListAdapter(adapter);
		 */
	}

	/*
	 * public void didclick(View v) {
	 * Log.d("asd","You clicked btn2 - uses an anonymouse inner class"); Intent
	 * intent = new Intent( CategoriesActivity.this, SubcategoriesActivity.class
	 * ); startActivity(intent); }
	 */

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.d(TAG, "Inside onListItemClick!.");
		Object o = this.getListAdapter().getItem(position);
		Log.d(TAG, "Leaving onListItemClick!.");
		String cat = ((Category) o).getName().toString();

		Bundle bundle = new Bundle();

		bundle.putString("catid", categoriesMap.get(cat).getId() + "");
		bundle.putString("catname", categoriesMap.get(cat).getName());
		Intent newIntent = new Intent(CategoriesActivity.this,
				SubcategoriesActivity.class);
		newIntent.putExtras(bundle);
		CategoriesActivity.this.startActivity(newIntent);

	}

	private class CategoryAdapter extends ArrayAdapter<Category> {

		private List<Category> cats;

		// public ImageLoader imageLoader;

		public CategoryAdapter(Context context, int textViewResourceId,
				List<Category> cats) {
			super(context, textViewResourceId, cats);
			this.cats = cats;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.categories_item, null);
			}
			Category o = cats.get(position);
			if (o != null) {
				TextView name_place = (TextView) v.findViewById(R.id.cat_name);
				String catname = catNames.get(position).toString();
				HtmlDesEncoder enc = new HtmlDesEncoder();
				
				if (name_place != null) {
					name_place.setText(enc.encodeString(catname));
				}
			}
			return v;
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
			Intent showSettingsView = new Intent(CategoriesActivity.this,
					SettingsActivity.class);
			startActivity(showSettingsView);
			break;

		case R.id.about_option_menu:
			new AlertDialog.Builder(this)
					.setTitle(getString(R.string.about_option_menu))
					.setMessage(getString(R.string.about_us_dialog))
					.setPositiveButton(android.R.string.ok, null).show();
			break;
		case R.id.logout_option_menu:
			Log.d(TAG, "logout_option_menu pressed");
			/* intent for log out petition */
			Intent logoutIntent = new Intent(CategoriesActivity.this,
					SecurityService.class);

			/* log out progress dialog */
			final ProgressDialog logoutDialog = ProgressDialog.show(
					CategoriesActivity.this, "",
					getString(R.string.log_out_validation), true);

			/* looking for current user and token */
			SharedPreferences settings = getSharedPreferences("LOGIN", 0);
			Map<String, ?> map = settings.getAll();
			String username = (String) map.get("user");
			String token = (String) map.get("token");

			Bundle b = new Bundle();
			b.putString("username", username);
			b.putString("token", token);

			logoutIntent.putExtras(b);
			logoutIntent.putExtra("command", SecurityService.LOGOUT_CMD);
			logoutIntent.putExtra("receiver",
					new ResultReceiver(new Handler()) {
						@Override
						protected void onReceiveResult(int resultCode,
								Bundle resultData) {

							super.onReceiveResult(resultCode, resultData);

							logoutDialog.dismiss();
							if (resultCode == SecurityService.STATUS_OK) {

								/* TODO REMOVE SharedPreferences settings */
								Intent loadLogInView = new Intent(
										CategoriesActivity.this,
										LoginActivity.class);
								startActivity(loadLogInView);
								Toast.makeText(CategoriesActivity.this,
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
			Intent showHomeView = new Intent(CategoriesActivity.this,
					MainActivity.class);
			startActivity(showHomeView);
			break;
		}

		return true;
	}

}
