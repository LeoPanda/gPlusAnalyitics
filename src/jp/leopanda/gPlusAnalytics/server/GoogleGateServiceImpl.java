package jp.leopanda.gPlusAnalytics.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.IOException;
import java.util.Calendar;
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
  private Map<String, DataStoreHandler> storeHandlerStocker = new HashMap<String, DataStoreHandler>();
  private final int stackRefrashIntervalMillsec = 6 * 60 * 60 * 1000;
  StackTimeChecker stackTimeChecker = new StackTimeChecker(stackRefrashIntervalMillsec);

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
        // return dataHandlerOld.updateBrandNew(getCurrentUserId(), getApiService());
        return getDataConductor().updateDataStore();
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
  private DataStoreHandler getStoreHandler() throws Exception {
    String userId = getCurrentUserId();
    DataStoreHandler dataStoreHandler;
    if (stackTimeChecker.isNeedToRefresh()) {
      storeHandlerStocker.clear();
    }
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

  /**
   * オンメモリに常駐させているDataStoreのリフレッシュタイムをチェックする
   * 
   * @author LeoPanda
   *
   */
  private class StackTimeChecker {
    Calendar loadedTime = Calendar.getInstance();
    int intervalTime = 0;

    public StackTimeChecker(int intervalTime) {
      this.intervalTime = intervalTime;
    }

    boolean isNeedToRefresh() {
      Calendar nowTime = Calendar.getInstance();
      if (intervalTime < nowTime.compareTo(loadedTime)) {
        loadedTime = nowTime;
        return true;
      }
      return false;
    }
  }

}
