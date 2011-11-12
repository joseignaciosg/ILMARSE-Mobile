package ilmarse.mobile.activities;

import ilmarse.mobile.model.api.Product;
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

    String imageUrl="http://1.bp.blogspot.com/_mbWThvBk2kA/S43ftG3N6MI/AAAAAAAANws/FmT_6iWv8iE/s320/books+2.gif";
    ImageView imView;
    TextView prodnameView;
    TextView prodpriceView ;
    Random r= new Random();
    
    @Override
    public void onCreate(Bundle b){
    	super.onCreate(b);
    	
    	setContentView(R.layout.book_product);
    	Log.d(TAG,"inside onCreate");
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,CatalogService.class);
//		int categoryid = new Integer(this.getIntent().getExtras().getString("catid"));
//		int productid = new Integer(this.getIntent().getExtras().getString("productid"));
		Bundle bundle = new Bundle();
		bundle.putString("categoryid", 1+"");
		bundle.putString("productid", 1+"");
		intent.putExtras(bundle);
		
		//should not be here . just testing
//		Product product = new Product()
//		populateProduct(product);

		
		intent.putExtra("command", CatalogService.GET_PRODUCT_CMD);
		
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				if (resultCode == CatalogService.STATUS_OK) {
					Log.d(TAG, "OK received info");
					@SuppressWarnings("unchecked")
					List<Product> list = (List<Product>)resultData.getSerializable("return");
					Product product = list.get(0);
					Log.d(TAG,product.toString());
					Log.d(TAG, "inside product receiver");
//					populateList( new ProductProviderImpl(list) );
					populateProduct(product);

				} else if (resultCode == CatalogService.STATUS_CONNECTION_ERROR) {
					Log.d(TAG, "Connection error.");
				} else {
					Log.d(TAG, "Unknown error.");
				}
			}

		});
		/* wait until iol is working*/
		startService(intent);
		
    }
    
    private void populateProduct(Product prod){
    	Log.d(TAG,"inside populateProduct: product: "+prod.toString() );
    	String imageUrl = prod.getImage_url();
        imView = (ImageView)findViewById(R.id.imview);
        prodnameView = (TextView)findViewById(R.id.product_name);
        prodpriceView = (TextView)findViewById(R.id.product_price);

        
    	downloadFile(imageUrl);
    	setAttributes(prod);
    }
    
    
private void setAttributes(Product prod) {
		Log.d(TAG,"inside setATTR");
		if ( prodnameView != null)
			prodnameView.setText(prod.getName());
		else 
			Log.e(TAG,"prodname is null");
		
		if ( prodpriceView != null)
			prodpriceView.setText(prod.getPrice()+"");
		else 
			Log.e(TAG,"prodprice is null");
		
	}


//   @Override
//   public void onCreate(Bundle icicle) {
//	   Log.d(TAG,"inside onCreate");
//       super.onCreate(icicle);
//       setContentView(R.layout.image_test);
//      
//       Button bt3= (Button)findViewById(R.id.get_imagebt);
//       bt3.setOnClickListener(getImgListener);
//       imView = (ImageView)findViewById(R.id.imview);
//   }    
//
//   View.OnClickListener getImgListener = new View.OnClickListener()
//   {
//
//         @Override
//         public void onClick(View view) {
//              // TODO Auto-generated method stub
//              
//              //i tried to randomize the file download, in my server i put 4 files with name like
//                       //png0.png, png1.png, png2.png so different file is downloaded in button press
//              int i =r.nextInt(4);
//              downloadFile(imageUrl);
//              Log.i("im url",imageUrl);
//         }
//    
//   };
//
//  
   Bitmap bmImg;
   void downloadFile(String fileUrl){
         URL myFileUrl =null;        
         Log.d(TAG,fileUrl);
         try {
              myFileUrl= new URL(fileUrl);
              Log.d(TAG,fileUrl);
         } catch (MalformedURLException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
         }
         try {
              HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
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
