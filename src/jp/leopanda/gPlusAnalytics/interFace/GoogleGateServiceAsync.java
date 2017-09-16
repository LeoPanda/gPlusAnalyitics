package jp.leopanda.gPlusAnalytics.interFace;



import com.google.gwt.user.client.rpc.AsyncCallback;

import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;

public interface GoogleGateServiceAsync {
  void getItems(AsyncCallback<SourceItems> callback);


  void updateDataStore(AsyncCallback<String> callback);

  
}
