package ilmarse.mobile.services;

import ilmarse.mobile.model.api.Product;
import ilmarse.mobile.model.api.User;
import ilmarse.mobile.model.api.UserProvider;
import ilmarse.mobile.model.impl.UserProviderMock;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class SecurityService extends IntentService {

	private final String TAG = getClass().getSimpleName();
	private final String APIurl = "http://eiffel.itba.edu.ar/hci/service/";
	
	public static final String LOGIN_CMD = "LogIn";
	public static final int STATUS_CONNECTION_ERROR = -1;
	public static final int STATUS_ERROR = -2;
	public static final int STATUS_ILLEGAL_ARGUMENT = -3;
	public static final int STATUS_OK = 0;
	public static final int STATUS_FAIL = -1;


	private UserProvider userprovider = new UserProviderMock();
	
	public SecurityService(){
		super("SecurityService");
	}
	
	@Override
	protected void onHandleIntent(final Intent intent) {
		Log.d(TAG, "inside onHandleIntent");

		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		final String command = intent.getStringExtra("command");
		String username, password;
		final Bundle b = new Bundle();
		try {
			if (command.equals(LOGIN_CMD)) { // command for receiving available categories
				Log.d(TAG, "LOGIN_CMD");
				username = intent.getStringExtra("username");
				password = intent.getStringExtra("password");
				b.putString("username",username);
				b.putString("password", password);
				validate(receiver, b);
			}
		} catch (SocketTimeoutException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_CONNECTION_ERROR, b);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ERROR, b);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ERROR, b);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ILLEGAL_ARGUMENT, b);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ERROR, b);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}

		this.stopSelf();
	}


	@SuppressWarnings("unused")
	private void validate(ResultReceiver receiver, Bundle b) throws ClientProtocolException, IOException, Exception {
		//eiffel petition
		Log.d(TAG, "inside validate");
		String username  = b.getString("username");
		String password = b.getString("password");
		Log.d(TAG, username + " " + password);

		
//		final DefaultHttpClient client = new DefaultHttpClient();
//		final HttpResponse response;
//		response = client.execute(new HttpGet(APIurl + "Security.groovy?method=SingIn&username"+username+"&password="+password));
//		final String xmlToParse = EntityUtils.toString(response.getEntity());
//		Log.d(TAG, xmlToParse.toString());
		User user; 
//		product = fromXMLtoUser(xmlToParse, catId);
		user = userprovider.getuser(username,password);

		if ( user == null ){
			receiver.send(STATUS_FAIL, b);
			return;
		}
		Log.d(TAG, user.toString());
		
		List<User> aux = new ArrayList<User>();
		aux.add(user);
		b.putSerializable("return", (Serializable)aux);
		receiver.send(STATUS_OK, b);
		
	}
}
