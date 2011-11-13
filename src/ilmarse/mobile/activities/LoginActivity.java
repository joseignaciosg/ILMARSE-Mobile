package ilmarse.mobile.activities;

import ilmarse.mobile.model.api.User;
import ilmarse.mobile.services.SecurityService;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private String TAG = getClass().getSimpleName();
	private Button loginButton;
	private EditText usernameText;
	private EditText passwordText;
	private CheckBox remember;
	private CheckBox autoLogin;
	private AlertDialog.Builder alt_bld;

	// private Intent checkPassUsr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.d(TAG, "inside onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// listener log in button
		// Button loginButton = (Button) findViewById(R.id.login_button);
		usernameText = ((EditText) findViewById(R.id.username));
		passwordText = ((EditText) findViewById(R.id.password));


		// usernameText.setOnKeyListener(new OnKeyListener() {
		// public boolean onKey(View v, int keyCode, KeyEvent event) {
		// // If the event is a key-down event on the "enter" button
		// if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
		// (keyCode == KeyEvent.KEYCODE_ENTER)) {
		// Log.d(TAG, "inside  setOnClickListener onClick");
		// Log.d(TAG,usernameText.getText().toString());
		// Log.d(TAG,passwordText.getText().toString());
		//
		// // remember = ((CheckBox) findViewById(R.id.RememberLoginBox));
		// // // remember.setOnCheckedChangeListener(new RememberListener());
		// // autoLogin = ((CheckBox) findViewById(R.id.AutoLoginBox));
		// // if (username.toString() == "jose"
		// // && password.toString() == "jose") {
		// // Intent showProductsView = new Intent(LoginActivity.this,
		// // MainActivity.class);
		// // startActivity(showProductsView);
		// // } else {
		// // AlertDialog alert = alt_bld.create();
		// // // Title for AlertDialog
		// // alert.setTitle("");
		// // // Icon for AlertDialog
		// // alert.setIcon(R.drawable.ilmarselogo);
		// // alert.show();
		// //
		// // }
		//
		// Bundle b = new Bundle();
		//
		//
		// Intent checkPassUsr = new Intent(LoginActivity.this,
		// SecurityService.class);
		//
		// final ProgressDialog validatingLoginDialog =
		// ProgressDialog.show(LoginActivity.this,
		// "", getString(R.string.log_in_validation), true);
		//
		// b.putString("username", usernameText.getText().toString());
		// b.putString("password", passwordText.getText().toString());
		//
		// checkPassUsr.putExtras(b);
		// checkPassUsr.putExtra("command", SecurityService.LOGIN_CMD);
		//
		// checkPassUsr.putExtra("receiver", new ResultReceiver(new Handler()) {
		// @Override
		// protected void onReceiveResult(int resultCode, Bundle resultData) {
		// super.onReceiveResult(resultCode, resultData);
		//
		// validatingLoginDialog.dismiss();
		//
		// Log.d(TAG,"inside onReceiveResult");
		//
		// if (resultCode == SecurityService.STATUS_OK) {
		//
		// @SuppressWarnings("unchecked")
		// List<User> list = (List<User>) resultData.getSerializable("return");
		// User user = list.get(0);
		// Log.d(TAG, user.toString());
		// String token = user.getToken();
		// SharedPreferences settings = getSharedPreferences("LOGIN",0);
		// /*investigate this stuff*/
		// Editor edit = settings.edit();
		// edit.putString("token", token);
		// edit.putString("user", user.getName());
		// edit.commit();
		//
		// Intent mainView = new Intent(LoginActivity.this, MainActivity.class);
		// startActivity(mainView);
		// Toast.makeText(LoginActivity.this, "" + getString(R.string.welcome) +
		// " " + user.getName() + "!",
		// Toast.LENGTH_SHORT/Toast.LENGTH_LONG).show();
		//
		// } else {
		// Toast.makeText(LoginActivity.this, getString(R.string.badlogin),
		// Toast.LENGTH_SHORT/Toast.LENGTH_LONG).show();
		// }
		// }
		// });
		//
		// startService(checkPassUsr);
		// return true;
		//
		// }
		// return false;
		// }
		// });
		// A donde voy si presiono el boton Iniciar Sesion
		Button loginbutton = (Button) findViewById(R.id.login_button);

		loginbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Bundle b = new Bundle();

				Intent callLogInServiceIntent = new Intent(LoginActivity.this,
						SecurityService.class);

				final ProgressDialog validatingLoginDialog = ProgressDialog
						.show(LoginActivity.this, "",
								getString(R.string.log_in_validation), true);

				b.putString("username", usernameText.getText().toString());
				b.putString("password", passwordText.getText().toString());

				Log.d(TAG, usernameText.getText().toString());
				Log.d(TAG, passwordText.getText().toString());

				callLogInServiceIntent.putExtras(b);
				callLogInServiceIntent.putExtra("command",SecurityService.LOGIN_CMD);
				callLogInServiceIntent.putExtra("receiver", new ResultReceiver(		new Handler()) {
					@Override
					protected void onReceiveResult(int resultCode,	Bundle resultData) {
						super.onReceiveResult(resultCode, resultData);

						validatingLoginDialog.dismiss();

						Log.d(TAG, "onReceiveResult: "+resultCode+"");
						if (resultCode == SecurityService.STATUS_OK) {

							// String res = resultData.getString("login");
							@SuppressWarnings("unchecked")
							List<User> list = (List<User>) resultData
									.getSerializable("return");
							User user = list.get(0);
							Log.d(TAG, user.toString());
							String token = user.getToken();
							SharedPreferences settings = getSharedPreferences(
									"LOGIN", 0);
							Editor edit = settings.edit();
							edit.putString("token", token);
							edit.putString("user", user.getName());
							edit.commit();
							Intent loadHomeView = new Intent(
									LoginActivity.this, MainActivity.class);
							startActivity(loadHomeView);
							Toast.makeText(LoginActivity.this,
									 getString(R.string.welcome) + " "
											+ user.getName()
											+ "!",
									Toast.LENGTH_SHORT / Toast.LENGTH_LONG)
									.show();

							Log.d(TAG, "supposed to be logged" + token);

						} else {
							Log.d(TAG, "unknown error");
							Toast.makeText(LoginActivity.this,
									getString(R.string.badlogin),
									Toast.LENGTH_SHORT / Toast.LENGTH_LONG)
									.show();
						}
					}

				});

				startService(callLogInServiceIntent);

			}

		});

	}
}
