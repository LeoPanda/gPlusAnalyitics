package jp.leopanda.gPlusAnalytics.interFace;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import jp.leopanda.gPlusAnalytics.dataObject.StoredItems;

@RemoteServiceRelativePath("GoogleGateService")
public interface GoogleGateService extends RemoteService {
  StoredItems getItems() throws Exception;

  String initialLoadToStore() throws Exception;

  String updateDataStore() throws Exception;

  String clearDataStore() throws Exception;
  
  String updateActivities() throws Exception;

}
