package ilmarse.mobile.activities;

import ilmarse.mobile.services.CatalogService;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

		ImageButton productsButton = (ImageButton) findViewById(R.id.productsButton);
		productsButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent showProductsView = new Intent(MainActivity.this,
						CategoriesActivity.class);
				startActivity(showProductsView);

			}

		});
	}

	
}
