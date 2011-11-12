package ilmarse.mobile.activities;

import ilmarse.mobile.services.CatalogService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends Activity{

	private String TAG = getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "inside Oncreate");

		setContentView(R.layout.first_screen);
//		populateView();

	}

//	private void populateView(){
//		ImageView logo = (ImageView)findViewById(R.id.logo);
//	}
//	
	
}
