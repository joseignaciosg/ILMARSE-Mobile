package ilmarse.mobile.activities;

import ilmarse.mobile.activities.CategoriesActivity;
import ilmarse.mobile.activities.SplashScreenIlmarse;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashScreenIlmarse extends Activity {
	
	public static final int MILLIS_TIME_TO_WAIT = 1000;
	public static final int STOP = 0;
	
	private Handler splashHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SplashScreenIlmarse.STOP:
					Intent intent = new Intent(SplashScreenIlmarse.this, CategoriesActivity.class);
					startActivity(intent);
					SplashScreenIlmarse.this.finish();
					break;
			}
			super.handleMessage(msg);
		}
	};
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Message message = new Message();
		message.what = SplashScreenIlmarse.STOP;
		// ¿Por que no se puede hacer simplemente sleep(3000)?
		splashHandler.sendMessageDelayed(message, MILLIS_TIME_TO_WAIT);

    }
}