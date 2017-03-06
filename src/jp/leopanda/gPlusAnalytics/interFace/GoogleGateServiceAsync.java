package jp.leopanda.gPlusAnalytics.interFace;



import com.google.gwt.user.client.rpc.AsyncCallback;

import jp.leopanda.gPlusAnalytics.dataObject.StoredItems;

public interface GoogleGateServiceAsync {
  void getItems(AsyncCallback<StoredItems> callback);

  void initialLoadToStore(AsyncCallback<String> callback);

  void updateDataStore(AsyncCallback<String> callback);

  void clearDataStore(AsyncCallback<String> callback);
}
