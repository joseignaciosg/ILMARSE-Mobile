package ilmarse.mobile.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends Activity {
	
	private String TAG = getClass().getSimpleName();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		Log.d(TAG, "inside onCreate");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login);
		Log.d(TAG, "after setContentView");

		
		// listener log in button
		Button loginButton = (Button) findViewById(R.id.login_button);
		Log.d(TAG, "after productsButton");
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent showProductsView = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(showProductsView);

			}

		});
	}

}
