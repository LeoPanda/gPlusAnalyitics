package jp.leopanda.gPlusAnalytics.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;
import jp.leopanda.gPlusAnalytics.dataStore.DataStoreHandler;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateService;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;
import jp.leopanda.googleAuthorization.client.NoCredentialException;
import jp.leopanda.googleAuthorization.server.CatchException;
import jp.leopanda.googleAuthorization.server.CredentialUtils;

/**
 * Google+ REST APIを呼び出すためのサーバーサイドコンポーネント
 * 
 * @author LeoPanda
 *
 */
public class GoogleGateServiceImpl extends RemoteServiceServlet implements GoogleGateService {
  private static final long serialVersionUID = 1L;

  private Map<String, DataStoreHandler> storeHandlerStocker = new HashMap<String, DataStoreHandler>();
  Logger logger = Logger.getLogger(GoogleGateServiceImpl.class.getName());

  /**
   * データストアの内容を最新状態に更新する
   * 
   * @return 常にnull
   * @throws Exception
   */
  public String updateDataStore() throws Exception {
    storeHandlerStocker.clear();
    return new CatchException<String>() {

      @Override
      public String tryApiCall() throws Exception {
        return getDataConductor().updateDataStore(false);
      }
    }.execute();
  }


  /**
   * データストアからソースアイテムを読み込む
   * 
   * @return
   * @throws Exception
   */
  public SourceItems getItems() throws Exception {
    return new CatchException<SourceItems>() {

      @Override
      public SourceItems tryApiCall() throws Exception {
        return getStoreHandler().getItems();
      }

    }.execute();
  }

  /**
   * データコンダクタの取得
   * 
   * @return
   * @throws IOException
   * @throws NoCredentialException
   * @throws HostGateException
   */
  private DataConductor getDataConductor() throws Exception {
    return new DataConductor(getStoreHandler(), getApiService());
  }

  /**
   * データストアハンドラの取得
   * 
   * @return
   * @throws NoCredentialException
   * @throws IOException
   */
  private DataStoreHandler getStoreHandler() throws Exception {
    String userId = getCurrentUserId();
    DataStoreHandler dataStoreHandler;
    if (storeHandlerStocker.containsKey(userId)) {
      dataStoreHandler = storeHandlerStocker.get(userId);
    } else {
      dataStoreHandler = new DataStoreHandler(userId);
      storeHandlerStocker.put(userId, dataStoreHandler);
    }
    return dataStoreHandler;
  }

  /**
   * APIサービスの取得
   * 
   * @return
   * @throws Exception
   */
  private PlusApiService getApiService() throws Exception {
    return new PlusApiService(new CredentialUtils());
  }

  /**
   * カレントユーザーIDの取得
   * 
   * @return
   * @throws Exception
   * @throws NoCredentialException
   */
  private String getCurrentUserId() throws Exception {
    return getApiService().getGplusUserId();
  }

  
}
