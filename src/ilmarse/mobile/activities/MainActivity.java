package ilmarse.mobile.activities;

import ilmarse.mobile.services.SecurityService;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
            new AlertDialog.Builder(this)
            .setTitle(getString(R.string.about_option_menu))
            .setMessage(getString(R.string.about_us_dialog))
            .setPositiveButton(android.R.string.ok, null)
            .show();
			break;
		case R.id.logout_option_menu:
			Log.d(TAG, "logout_option_menu pressed");
			/*intent for log out petition*/
			Intent logoutIntent = new Intent(MainActivity.this,
					SecurityService.class);
			
			/*log out progress dialog*/
			final ProgressDialog logoutDialog = ProgressDialog
					.show(MainActivity.this, "",
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
								MainActivity.this, LoginActivity.class);
						startActivity(loadLogInView);
						Toast.makeText(MainActivity.this,
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
		
		}
		return true;
	}
	
}
