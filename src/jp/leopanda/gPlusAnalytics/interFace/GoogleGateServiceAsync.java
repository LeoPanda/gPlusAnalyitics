package jp.leopanda.gPlusAnalytics.interFace;


import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GoogleGateServiceAsync {
  void getActivities(String userId,String oAuthToken, AsyncCallback<List<PlusActivity>> callback);
  void getPlusOners(String userId,String oAuthToken, AsyncCallback<List<PlusPeople>> callback);
  void updateDataStore(String userId,String oAuthToken, boolean forceUpdate,AsyncCallback<String> callback);
  void clearDataStore(String userId, AsyncCallback<String> callback);
}
