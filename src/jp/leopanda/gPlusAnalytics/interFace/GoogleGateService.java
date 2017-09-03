package jp.leopanda.gPlusAnalytics.interFace;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;

@RemoteServiceRelativePath("GoogleGateService")
public interface GoogleGateService extends RemoteService {
  SourceItems getItems() throws Exception;

  String initialLoadToStore() throws Exception;

  String updateDataStore() throws Exception;

  String clearDataStore() throws Exception;
  
  String updateActivities() throws Exception;

}
