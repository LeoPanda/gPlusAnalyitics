package jp.leopanda.gPlusAnalytics.interFace;



import jp.leopanda.gPlusAnalytics.dataObject.ResultPack;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GoogleGateServiceAsync {
  void getItems(String userId, AsyncCallback<ResultPack> callback);

  void initialLoadToStore(String userId, String oauthToken, AsyncCallback<String> callback);

  void updateDataStore(String userId, String oauthToken, AsyncCallback<String> callback);

  void clearDataStore(String userId, AsyncCallback<String> callback);
}
