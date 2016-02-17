package jp.leopanda.gPlusAnalytics.interFace;

import java.util.List;





import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("GoogleGateService")
public interface GoogleGateService extends RemoteService{
  List<PlusActivity> getActivities(String userId,String oAuthToken) throws HostGateException;
  List<PlusPeople> getPlusOners(String userId,String oAuthToken) throws HostGateException;
  String updateDataStore(String userId,String oAuthToken,boolean forceUpdate) throws HostGateException;
  String clearDataStore(String userId) throws HostGateException;

}
