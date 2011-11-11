package ilmarse.mobile.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ProductActivity extends Activity {
	private String TAG = getClass().getSimpleName();

    ImageView imView;
    String imageUrl="http://eiffel.itba.edu.ar/hci/service/images/100.jpg";
    Random r= new Random();
   /** Called when the activity is first created. */ 
   @Override
   public void onCreate(Bundle icicle) {
	   Log.d(TAG,"inside onCreate!!");
       super.onCreate(icicle);
       setContentView(R.layout.main);
      
       Button bt3= (Button)findViewById(R.id.get_imagebt);
       bt3.setOnClickListener(getImgListener);
       imView = (ImageView)findViewById(R.id.imview);
   }    

   View.OnClickListener getImgListener = new View.OnClickListener()
   {

         @Override
         public void onClick(View view) {
              // TODO Auto-generated method stub
              
              //i tried to randomize the file download, in my server i put 4 files with name like
                       //png0.png, png1.png, png2.png so different file is downloaded in button press
              int i =r.nextInt(4);
              downloadFile(imageUrl+"png"+i+".png");
              Log.i("im url",imageUrl+"png"+i+".png");
         }
    
   };

  
   Bitmap bmImg;
   void downloadFile(String fileUrl){
         URL myFileUrl =null;          
         try {
              myFileUrl= new URL(fileUrl);
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
