package ilmarse.mobile.services;

import ilmarse.mobile.model.api.User;
import ilmarse.mobile.model.impl.UserImpl;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class SecurityService extends IntentService {

	private final String TAG = getClass().getSimpleName();
	private final String APIurl = "http://eiffel.itba.edu.ar/hci/service/";

	public static final String LOGIN_CMD = "LogIn";
	public static final String LOGOUT_CMD = "LogOut";

	public static final int STATUS_CONNECTION_ERROR = -1;
	public static final int STATUS_ERROR = -2;
	public static final int STATUS_ILLEGAL_ARGUMENT = -3;
	public static final int STATUS_OK = 0;
	public static final int STATUS_FAIL = -1;

//	private UserProvider userprovider = new UserProviderMock();

	public SecurityService() {
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
			if (command.equals(LOGIN_CMD)) { // command for receiving available
												// categories
				Log.d(TAG, "LOGIN_CMD");
				username = intent.getStringExtra("username");
				password = intent.getStringExtra("password");
				b.putString("username", username);
				b.putString("password", password);
				validate(receiver, b);
			} else if (command.equals(LOGOUT_CMD)) {
				Log.d(TAG, "LOGOUT_CMD");
				logout(receiver, b);
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

	private void logout(ResultReceiver receiver, Bundle b) {
		SharedPreferences settings = getSharedPreferences("LOGIN", 0);
		Map<String, ?> map = settings.getAll();
		String username = (String) map.get("user");
		String token = (String) map.get("token");
		Editor edit = settings.edit();
		edit.remove("LOGIN");
		edit.commit();
		// final DefaultHttpClient client = new DefaultHttpClient();
		// final HttpResponse response;
		// response = client.execute(new HttpGet(APIurl +
		// "Security.groovy?method=SingOut&username"+username+"&token="+token));
		// final String xmlToParse = EntityUtils.toString(response.getEntity());
		// Log.d(TAG, xmlToParse.toString());
		String response;
		// response = fromXMLtoResponce(xmlToParse, catId);
		response = "OK";

		b.putSerializable("return", (Serializable) response);
		receiver.send(STATUS_OK, b);

	}

	@SuppressWarnings("unused")
	private void validate(ResultReceiver receiver, Bundle b)
			throws ClientProtocolException, IOException, Exception {
		// eiffel petition
		Log.d(TAG, "inside validate");
		String username = b.getString("username");
		String password = b.getString("password");
		Log.d(TAG, username + " " + password);

		final DefaultHttpClient client = new DefaultHttpClient();
		final HttpResponse response;
		response = client.execute(new HttpGet(APIurl
				+ "Security.groovy?method=SignIn&username=" + username
				+ "&password=" + password));
		Log.d(TAG,"username:"+username+" "+"password:"+password);
		final String xmlToParse = EntityUtils.toString(response.getEntity());
		Log.d(TAG, xmlToParse.toString());
		User user;
		user = fromXMLtoUser(xmlToParse);
		// user = userprovider.getuser(username,password);

		if (user == null) {
			receiver.send(STATUS_FAIL, b);
			return;
		}
		Log.d(TAG, user.toString());

		List<User> aux = new ArrayList<User>();
		aux.add(user);
		b.putSerializable("return", (Serializable) aux);
		receiver.send(STATUS_OK, b);

	}

	@SuppressWarnings("null")
	private User fromXMLtoUser(String xmlToParse) {

		// <response status="ok">
		// <authentication>
		// <token>702818a8294e9d554acaf62fafa4563</token>
		// <user id="361" username="hci" name="HCI2009"
		// last_login_date="2011-11-14"/>
		// </authentication>
		// </response>
		Log.d(TAG, "inside fromXMLtoUser");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		User user = new UserImpl();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource inStream = new InputSource();
			inStream.setCharacterStream(new StringReader(xmlToParse));
			Document doc = builder.parse(inStream);
			doc.getDocumentElement().normalize();

			NodeList responseList = doc.getElementsByTagName("response");
			Node resp = responseList.item(0);
			Element respE = (Element) resp;
			String status = respE.getAttribute("status");
			if ( status.equals("fail") ){
				return null;
			}
			Log.d(TAG, "not failed");

//			NodeList authList = doc.getElementsByTagName("authentication");
//			Node auth = authList.item(0);
//			Element authE = (Element)auth;
//			NodeList properties = authE.getChildNodes();
			String token = ((Element) doc.getElementsByTagName("token").item(0)).getFirstChild().getNodeValue();
			String username = ((Element) doc.getElementsByTagName("user").item(0)).getAttribute("username");
			String name = ((Element) doc.getElementsByTagName("user").item(0)).getAttribute("name");
			String last_login_date = ((Element) doc.getElementsByTagName("user").item(0)).getAttribute("last_login_date");
			user.setToken(token);
			user.setLast_login_date(last_login_date);
			user.setName(name);
			user.setUsername(username);
			
			Log.d(TAG,"pass:" +user.getUsername());
			Log.e(TAG, "finished!");

			
//			for (int j = 0; j < properties.getLength(); j++) {
//				Node property = properties.item(j);
//				String name = property.getNodeName().toString();
//				Log.d(TAG, name);
//				if (name.equalsIgnoreCase("token")) {
//					user.setToken(property.getFirstChild().getNodeValue());
//					Log.d(TAG, "is token");
//				} else if  (name.equalsIgnoreCase("user")) {
////				}else{
//					Element propE = (Element)property;
//					user.setId(Integer.valueOf(propE.getAttribute("id")));
//					user.setUsername(propE.getAttribute("username"));
//					user.setName(propE.getAttribute("name"));
//					user.setLast_login_date(propE.getAttribute("last_login_date"));
//					Log.d(TAG,"is user");
//					
//				}
//				Log.d(TAG,"leaving");
//			}
//			user = new UserImpl("hci", 361, "hci", "2011-11-14", "702818a8294e9d554acaf62fafa4563");

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
			Log.e(TAG, "here!");
		}
		return user;

	}
}
