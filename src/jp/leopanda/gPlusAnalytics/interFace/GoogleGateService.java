package jp.leopanda.gPlusAnalytics.interFace;


import jp.leopanda.gPlusAnalytics.dataObject.ResultPack;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("GoogleGateService")
public interface GoogleGateService extends RemoteService {
	ResultPack getItems(String userId) throws HostGateException;
	String initialLoadToStore(String userId,String oAuthToken) throws HostGateException;
	String updateDataStore(String userId, String oAuthToken) throws HostGateException;
	String clearDataStore(String userId) throws HostGateException;

}
