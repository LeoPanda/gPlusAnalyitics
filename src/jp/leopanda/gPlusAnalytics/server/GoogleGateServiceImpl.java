package jp.leopanda.gPlusAnalytics.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;
import jp.leopanda.gPlusAnalytics.dataStore.DataStoreHandler;
import jp.leopanda.gPlusAnalytics.dataStore.DataStoreHandlerOld;
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

  DataStoreHandlerOld dataHandlerOld = new DataStoreHandlerOld();

  /**
   * データストアの内容を最新状態に更新する
   * 
   * @return 常にnull
   * @throws Exception
   */
  public String updateDataStore() throws Exception {
    return new CatchException<String>() {

      @Override
      public String tryApiCall() throws Exception {
//        return dataHandlerOld.updateBrandNew(getCurrentUserId(), getApiService());
        return new DataConductor(getStoreHandler(), getApiService()).updateDataStore();
      }
    }.execute();
  }

  /**
   * データストアをクリアする
   * 
   * @throws Exception
   */
  public String clearDataStore() throws Exception {
    return new CatchException<String>() {

      @Override
      public String tryApiCall() throws Exception {
        dataHandlerOld.clearDataStore(getCurrentUserId());
        return null;
      }
    }.execute();
  }

  /**
   * データストアの初期ロード
   * 
   * @throws Exception
   */
  public String initialLoadToStore() throws Exception {
    return new CatchException<String>() {

      @Override
      public String tryApiCall() throws Exception {
        SourceItems items = getItemsFromOldStore();
        Map<String, Integer> activityMap = new HashMap<String, Integer>();
        for (PlusActivity activity : items.activities) {
          activityMap.put(activity.getId(), activity.getNumOfPlusOners());
        }
        new DataStoreHandler(getCurrentUserId()).putItems(items);
        return "";
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
   * 旧型データストアからactivityとplusOnersを得る
   * 
   * @throws Exception
   */
  private SourceItems getItemsFromOldStore() throws Exception {
    String userId = getApiService().getGplusUserId();
    return dataHandlerOld.getActivityStoreHandler().getItems(userId);
  }

  /**
   * データストアのアクテビティをAPIを読み直して再更新する。
   * 
   * @return
   * @throws Exception
   */
  public String updateActivities() throws Exception {
    return new CatchException<String>() {

      @Override
      public String tryApiCall() throws Exception {
        return dataHandlerOld.updateActivities(getCurrentUserId(), getApiService());
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
  private DataStoreHandler getStoreHandler() throws IOException, NoCredentialException {
    return new DataStoreHandler(getCurrentUserId());
  }

  /**
   * APIサービスの取得
   * 
   * @return
   * @throws IOException
   * @throws NoCredentialException
   */
  private PlusApiService getApiService() throws IOException, NoCredentialException {
    CredentialUtils utils = new CredentialUtils();
    return new PlusApiService(utils.httpTransport, utils.jsonFactory, utils.loadCredential());
  }

  /**
   * カレントユーザーIDの取得
   * 
   * @return
   * @throws IOException
   * @throws NoCredentialException
   */
  private String getCurrentUserId() throws IOException, NoCredentialException {
    return getApiService().getGplusUserId();
  }
}
