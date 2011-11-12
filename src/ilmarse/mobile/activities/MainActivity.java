package ilmarse.mobile.activities;

import ilmarse.mobile.services.CatalogService;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity{

	private String TAG = getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "inside Oncreate");

		setContentView(R.layout.first_screen);

		//listener for products section
		ImageButton productsButton = (ImageButton) findViewById(R.id.productsButton);
		productsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent showProductsView = new Intent(MainActivity.this,
						CategoriesActivity.class);
				startActivity(showProductsView);

			}

		});
		
		//listener for orders section
		ImageButton ordersButton = (ImageButton) findViewById(R.id.ordersButton);
		ordersButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent showOrdersView = new Intent(MainActivity.this,
						OrdersActivity.class);
				startActivity(showOrdersView);

			}

		});
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
			Intent showSettingsView = new Intent(MainActivity.this,
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
		
		}
		return true;
	}
	
}
