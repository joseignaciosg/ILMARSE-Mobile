package ilmarse.mobile.activities;

import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.impl.Book;
import ilmarse.mobile.model.impl.Dvd;
import ilmarse.mobile.services.CatalogService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductActivity extends Activity {
	private String TAG = getClass().getSimpleName();

	String imageUrl = "http://1.bp.blogspot.com/_mbWThvBk2kA/S43ftG3N6MI/AAAAAAAANws/FmT_6iWv8iE/s320/books+2.gif";
	ImageView imView;
	TextView prodnameView;
	TextView prodpriceView;
	
	int currCatId;
	Bitmap bmImg;
	Random r = new Random();

	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);

		Log.d(TAG, "inside onCreate");
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				CatalogService.class);
		int categoryid = new Integer(this.getIntent().getExtras()
				.getString("catid"));
		int productid = new Integer(this.getIntent().getExtras()
				.getString("prodid"));

		currCatId = categoryid;
		switch (categoryid) {
		case 1: // books
			setContentView(R.layout.book_product);
			break;
		default: // dvds
			setContentView(R.layout.dvd_product);
			break;
		}
		Log.d(TAG, "onCreate prodid:" + productid);
		Log.d(TAG, "onCreate categoryid:" + categoryid);
		Bundle bundle = new Bundle();
		bundle.putString("categoryid", categoryid + "");
		bundle.putString("productid", productid + "");
		intent.putExtras(bundle);

		// should not be here . just testing
		// Product product = new Product()
		// populateProduct(product);

		intent.putExtra("command", CatalogService.GET_PRODUCT_CMD);

		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				if (resultCode == CatalogService.STATUS_OK) {
					Log.d(TAG, "OK received info");
					@SuppressWarnings("unchecked")
					List<Product> list = (List<Product>) resultData
							.getSerializable("return");
					Product product = list.get(0);
					Log.d(TAG, product.toString());
					Log.d(TAG, "inside product receiver");
					// populateList( new ProductProviderImpl(list) );
					populateProduct(product);

				} else if (resultCode == CatalogService.STATUS_CONNECTION_ERROR) {
					Log.d(TAG, "Connection error.");
				} else {
					Log.d(TAG, "Unknown error.");
				}
			}

		});
		startService(intent);

	}

	private void populateProduct(Product prod){
    	Log.d(TAG,"inside populateProduct: product: "+prod.toString() );
    	String imageUrl = prod.getImage_url();
        imView = (ImageView)findViewById(R.id.imview);
        prodnameView = (TextView)findViewById(R.id.product_name);
        prodpriceView = (TextView)findViewById(R.id.product_price);
    	downloadFile(imageUrl);
    	setBasicAttributes(prod);
    	switch( currCatId ){
		case 1: //books
			setBookAttributes((Book)prod);
			break;
		case 2: //dvds
			setDvdAttributes((Dvd)prod);
			break;
		}
    }

	private void setBasicAttributes(Product prod) {
		Log.d(TAG, "inside setBasicAttributes");
		if (prodnameView != null)
			prodnameView.setText(prod.getName());
		else
			Log.e(TAG, "prodname is null");

		if (prodpriceView != null)
			prodpriceView.setText(prod.getPrice() + "");
		else
			Log.e(TAG, "prodprice is null");
	}
	
	private void setBookAttributes(Book prod) {
		Log.d(TAG,"inside setBookAttributes");
		TextView authorsView;
		TextView publisherView;
		TextView publishedDateView;
		TextView languageView;
		
		authorsView = (TextView)findViewById(R.id.product_authors);
		publisherView = (TextView)findViewById(R.id.product_publisher);
		publishedDateView = (TextView)findViewById(R.id.product_published_date);
		languageView = (TextView)findViewById(R.id.product_language);

		
		if ( authorsView != null)
			authorsView.setText( prod.getAuthors());
		else 
			Log.e(TAG,"authorsView is null");
		
		if ( publisherView != null)
			publisherView.setText(prod.getPublisher());
		else 
			Log.e(TAG,"publisherView is null");
		
		if ( publishedDateView != null)
			publishedDateView.setText(prod.getPublished_date());
		else 
			Log.e(TAG,"publishedDateView is null");
		
		if ( languageView != null)
			languageView.setText(prod.getLanguage());
		else 
			Log.e(TAG,"languageView is null");
		
	}
	
	
	private void setDvdAttributes(Dvd prod) {
		Log.d(TAG,"inside setDvdAttributes");
		TextView actorsView;
		TextView languageView;
		TextView subtitlesView;
		TextView regionView;
		TextView runtimeView;
		TextView relasedateView;
		TextView aspectratioView;
		
		actorsView = (TextView)findViewById(R.id.product_actors);
		languageView = (TextView)findViewById(R.id.product_language);
		subtitlesView = (TextView)findViewById(R.id.product_subtitles);
		regionView = (TextView)findViewById(R.id.product_region);
		runtimeView = (TextView)findViewById(R.id.product_runtime);
		relasedateView = (TextView)findViewById(R.id.product_release_date);
		aspectratioView = (TextView)findViewById(R.id.product_aspect_ratio);

		
		if ( actorsView != null)
			actorsView.setText( prod.getActors());
		else 
			Log.e(TAG,"actorsView is null");
		
		if ( languageView != null)
			languageView.setText(prod.getLanguage());
		else 
			Log.e(TAG,"languageView is null");

		if ( subtitlesView != null)
			subtitlesView.setText(prod.getSubtitles());
		else 
			Log.e(TAG,"subtitlesView is null");
		
		if ( regionView != null)
			regionView.setText(prod.getRegion());
		else 
			Log.e(TAG,"regionView is null");
		
		if ( runtimeView != null)
			runtimeView.setText(prod.getRun_time());
		else 
			Log.e(TAG,"runtimeView is null");

		if ( relasedateView != null)
			relasedateView.setText(prod.getRelease_date());
		else 
			Log.e(TAG,"relasedateView is null");
		
		if ( aspectratioView != null)
			aspectratioView.setText(prod.getAspect_ratio());
		else 
			Log.e(TAG,"aspectratioView is null");
		
		
}
	

	void downloadFile(String fileUrl) {
		URL myFileUrl = null;
		Log.d(TAG, fileUrl);
		try {
			myFileUrl = new URL(fileUrl);
			Log.d(TAG, fileUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();

			bmImg = BitmapFactory.decodeStream(is);
			imView.setImageBitmap(bmImg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
