package jp.leopanda.gPlusAnalytics.interFace;


import java.io.IOException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import jp.leopanda.gPlusAnalytics.dataObject.ResultPack;

@RemoteServiceRelativePath("GoogleGateService")
public interface GoogleGateService extends RemoteService {
  ResultPack getItems(String userId) throws HostGateException;

  String initialLoadToStore(String userId, String oauthToken) throws HostGateException, IOException;

  String updateDataStore(String userId, String oauthToken) throws HostGateException, IOException;

  String clearDataStore(String userId) throws HostGateException;

}
