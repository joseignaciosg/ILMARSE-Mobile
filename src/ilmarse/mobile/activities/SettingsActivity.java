package ilmarse.mobile.activities;

import ilmarse.mobile.services.SecurityService;

import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private String TAG = getClass().getSimpleName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Toast.makeText(SettingsActivity.this,"Bienvenido al �rea de Configuraci�n",
		// Toast.LENGTH_SHORT/Toast.LENGTH_LONG).show();
		setContentView(R.layout.settings);
		String phoneLanguage = getResources().getConfiguration().locale
				.getLanguage();
		if (phoneLanguage.equals("en"))
			setTitle("Settings");
		else
			setTitle("Ajustes");
		LinearLayout changeCheckFrequency = (LinearLayout) findViewById(R.id.change_delay_settings);
		changeCheckFrequency.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				final CharSequence[] items = { "1 min", "5 min", "30 min" };
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SettingsActivity.this);
				builder.setTitle(getString(R.string.frec_notif_title));

				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface,
							int item) {

						SharedPreferences settings = getSharedPreferences(
								"LOGIN", 0);
						Editor edit = settings.edit();
						switch (item) {
						case 0:
//							edit.putString("timeTillCheck", "60000");
							edit.putString("timeTillCheck", "5000");
							break;
						case 1:
//							edit.putString("timeTillCheck", "300000");
							edit.putString("timeTillCheck", "10000");
							break;
						case 2:
							edit.putString("timeTillCheck", "1800000");
							break;
						default:
							edit.putString("timeTillCheck", "300000");
							break;
						}

						edit.commit();

						Toast.makeText(getApplicationContext(),
								getString(R.string.updated_settings),
								Toast.LENGTH_SHORT).show();
						return;
					}
				});

				builder.create().show();
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
			Intent showSettingsView = new Intent(SettingsActivity.this,
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
			Intent logoutIntent = new Intent(SettingsActivity.this,
					SecurityService.class);

			/* log out progress dialog */
			final ProgressDialog logoutDialog = ProgressDialog.show(
					SettingsActivity.this, "",
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
										SettingsActivity.this,
										LoginActivity.class);
								startActivity(loadLogInView);
								Toast.makeText(SettingsActivity.this,
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
			Intent showHomeView = new Intent(SettingsActivity.this,
					MainActivity.class);
			startActivity(showHomeView);
			break;
		}

		return true;
	}
}
