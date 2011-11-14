package ilmarse.mobile.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Toast.makeText(SettingsActivity.this,"Bienvenido al �rea de Configuraci�n",
		// Toast.LENGTH_SHORT/Toast.LENGTH_LONG).show();
		setContentView(R.layout.settings);

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
							edit.putString("timeTillCheck", "60000");
							break;
						case 1:
							edit.putString("timeTillCheck", "300000");
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
}
