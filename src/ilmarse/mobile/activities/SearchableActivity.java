//package ilmarse.mobile.activities;
//
//import ilmarse.mobile.model.api.Product;
//import ilmarse.mobile.model.api.ProductsProvider;
//import ilmarse.mobile.model.impl.ProductProviderImpl;
//import ilmarse.mobile.services.CatalogService;
//import ilmarse.mobile.services.SearchService;
//
//import java.util.List;
//
//import android.app.ListActivity;
//import android.app.SearchManager;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.ResultReceiver;
//import android.util.Log;
//import android.widget.ListAdapter;
//import android.widget.SimpleAdapter;
//
//public class SearchableActivity extends ListActivity{
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//	    super.onCreate(savedInstanceState);
//	    setContentView(R.layout.products);
//
//	    // Get the intent, verify the action and get the query
//	    Intent intent = getIntent();
//	    if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//	      String query = intent.getStringExtra(SearchManager.QUERY);
//	      
//
//			Intent i = new Intent(Intent.ACTION_SYNC, null, this, SearchService.class);
//
//			i.putExtra("command", SearchService.GET_PRODUCTS_CMD);
//			i.putExtra("query", query);
//			/* Se pasa un callback (ResultReceiver), con el cual se procesará la
//			 * respuesta del servicio. Si se le pasa null como parámetro del constructor
//			 * se usa uno de los threads disponibles del proceso. Dado que en el procesamiento
//			 * del mismo se debe modificar la UI, es necesario que ejecute con el thread de UI.
//			 * Es por eso que se lo instancia con un objeto Handler (usando el el thread de UI
//			 * para ejecutarlo).
//			 */
//			i.putExtra("receiver", new ResultReceiver(new Handler()) {
//				@Override
//				protected void onReceiveResult(int resultCode, Bundle resultData) {
//					super.onReceiveResult(resultCode, resultData);
//					if (resultCode == SearchService.STATUS_OK) {
//
//						Log.d("searching", "OK received info");
//
//						@SuppressWarnings("unchecked")
//						List<Product> list = (List<Product>) resultData.getSerializable("return");
//						Log.d("searching",list.toString());
//						populateList( new ProductProviderImpl(list) );
//						Log.d("searching", "inside product receiver");
//
//					} else if (resultCode == CatalogService.STATUS_CONNECTION_ERROR) {
//						Log.d("searching", "Connection error.");
//					} else {
//						Log.d("searching", "Unknown error.");
//					}
//				}
//
//			});
//			startService(i);
//	    }
//	}
//	
//	
//	private void populateList(ProductsProvider prov) {
//		Log.d("products", "OK  populating category list");
//		ListAdapter adapter = new SimpleAdapter(this,
//				prov.getProductsAsMap(), R.layout.products_item,
//				prov.getMapKeys(), new int[] {  R.id.code,R.id.name,R.id.id });
//		setListAdapter(adapter);	
//	}
//}
