package jp.leopanda.gPlusAnalytics.interFace;



import com.google.gwt.user.client.rpc.AsyncCallback;

import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;

public interface GoogleGateServiceAsync {
  void getItems(AsyncCallback<SourceItems> callback);

  void initialLoadToStore(AsyncCallback<String> callback);

  void updateDataStore(AsyncCallback<String> callback);

  void clearDataStore(AsyncCallback<String> callback);
  
  void updateActivities(AsyncCallback<String> callback);
}
