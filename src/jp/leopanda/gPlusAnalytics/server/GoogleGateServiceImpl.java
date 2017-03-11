package jp.leopanda.gPlusAnalytics.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.io.IOException;

import jp.leopanda.gPlusAnalytics.dataObject.StoredItems;
import jp.leopanda.gPlusAnalytics.dataStore.DataStoreHandler;
import jp.leopanda.gPlusAnalytics.interFace.GoogleGateService;
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

  DataStoreHandler dataHandler = new DataStoreHandler();

  /**
   * データストアの内容を最新状態に更新する
   * 
   * @return 常にnull
   * @throws Exception
   */
  public String updateDataStore()
      throws Exception {
    String result = new CatchException<String>() {

      @Override
      public String tryApiCall() throws Exception {
        return dataHandler.updateBrandNew(getCurrentUserId(), getApiService());
      }
    }.execute();

    return result;
  }

  /**
   * データストアをクリアする
   * 
   * @throws Exception
   */
  public String clearDataStore() throws Exception {
    String result = new CatchException<String>() {

      @Override
      public String tryApiCall() throws Exception {
        dataHandler.clearDataStore(getCurrentUserId());
        return null;
      }
    }.execute();
    return result;
  }

  /**
   * データストアの初期ロード
   * 
   * @throws Exception
   */
  public String initialLoadToStore() throws Exception {
    String result = new CatchException<String>() {

      @Override
      public String tryApiCall() throws Exception {
        return dataHandler.initialLoadToStore(getCurrentUserId(), getApiService());
      }
    }.execute();

    return result;
  }

  /**
   * データストアからactivityとplusOnersを得る
   * 
   * @throws Exception
   */
  public StoredItems getItems() throws Exception {
    StoredItems storedItems = new CatchException<StoredItems>() {

      @Override
      public StoredItems tryApiCall() throws Exception {
        String userId = getApiService().getGplusUserId();
        return dataHandler.getActivityHandler().getItems(userId);

      }
    }.execute();
    return storedItems;
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
