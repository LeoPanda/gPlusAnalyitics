package jp.leopanda.gPlusAnalytics.server;

import java.io.IOException;
import java.util.List;

import com.google.appengine.api.ThreadManager;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.DeadlineExceededException;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.SourceItems;
import jp.leopanda.gPlusAnalytics.dataStore.DataStoreHandler;
import jp.leopanda.gPlusAnalytics.interFace.HostGateException;
import jp.leopanda.googleAuthorization.client.NoCredentialException;

/**
 * Google+ APIサービスとDataStoreハンドラの両方を操作してデータを管理するクラス
 * 
 * @author LeoPanda
 *
 */
public class DataConductor {

  DataStoreHandler storeHandler;
  PlusApiService apiService;
  List<PlusActivity> newActivities; // 処理対象のアクテビティリスト

  LoggerWidhConter logger = new LoggerWidhConter(DataConductor.class.getName());

  /**
   * コンストラクタ
   * 
   * @throws HostGateException
   * 
   * @throws NoCredentialException
   * @throws IOException
   * 
   */
  public DataConductor(DataStoreHandler storeHandler, PlusApiService apiService) {
    this.storeHandler = storeHandler;
    this.apiService = apiService;
  }

  /**
   * データストアを最新状態に更新する
   * 
   * @throws Exception
   */
  public String updateDataStore() throws Exception {
    SourceItems sourceItems = storeHandler.getItems();
    try {
      storeHandler.putItems(updateSouceItems(sourceItems));
    } catch (Exception e) {
      if (e instanceof DeadlineExceededException) {
        doInterrupt(newActivities, sourceItems, logger);
      } else {
        throw new Exception(e);
      }
    }
    logger.writeLog();
    return "";
  }

  /**
   * Google+APIを呼んで最新状態をチェックし、ソースアイテムに反映する。
   * 
   * @return
   * @throws Exception
   */
  private SourceItems updateSouceItems(SourceItems sourceItems) throws Exception {
    ActivitiesProcesser activitiesProcesser = new ActivitiesProcesser(apiService, logger);
    // 処理対象のアクテビティを選択する
    List<PlusActivity> interruptedActivites = storeHandler.getInterrupted();
    if (interruptedActivites.size() > 0) {
      newActivities = interruptedActivites; // 前回処理が途中で中断されていた場合は残りのアクテビティを処理する
    } else {
      newActivities = getNewActivitiesFromAPI(activitiesProcesser); // 通常時はG+APIから写真付き投稿アクテビティをすべて読み出す
      sourceItems.activities = activitiesProcesser.removeDisusedActivities(newActivities,
          sourceItems.activities);
    }
    // アクテビティリストの処理
    PlusOneCounter plusOneCounter = new PlusOneCounter();
    sourceItems.activities = activitiesProcesser.updatePlusActivitiesAndStackPlusOners(
        newActivities,
        sourceItems.activities);
    sourceItems.activities = activitiesProcesser.setStatisticsInfo(
        plusOneCounter.aggregatePlusOneCount(sourceItems.activities), sourceItems.activities);
    // +1erリストの処理
    PlusOnersProceccer plusOnerProcesser = new PlusOnersProceccer(logger);
    sourceItems.plusOners = plusOnerProcesser
        .addNewPlusOners(activitiesProcesser.getPlusOnersForUpdate(), sourceItems.plusOners);
    sourceItems.plusOners = plusOnerProcesser.updatePlusOners(plusOneCounter,
        sourceItems.plusOners);
    // 中断時未処理アクテビティリストをクリアする
    putInterruptedList(newActivities);

    return sourceItems;
  }

  /**
   * 処理時間超過時の割り込み処理
   * 
   * @param newActivites
   * @param sourceItems
   * @param logger
   * @throws Exception
   * @throws HostGateException
   */
  private void doInterrupt(List<PlusActivity> newActivities, SourceItems sourceItems,
      LoggerWidhConter logger) throws Exception {
    long remainSec = ApiProxy.getCurrentEnvironment().getRemainingMillis() / 1000;
    logger.info("process exceeded:remainSec=" + String.valueOf(remainSec));
    if (newActivities == null) {
      logger.warning("newActivies is null whlie doing exceeded interrupt task.");
    } else {
      if (newActivities.size() > 0) {
        putInterruptedList(newActivities);
        storeHandler.putItems(sourceItems);
        logger.writeLog();
        startNewThread();
      } else {
        logger.info("Exceeded process started but newActivities is 0.");
      }
    }
  }

  /**
   * 新しいスレッドを生成して継続ジョブを実行する
   */
  private void startNewThread() {
    ThreadManager.createBackgroundThread(new Runnable() {

      @Override
      public void run() {
        try {
          new DataConductor(storeHandler, apiService).updateDataStore();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();
  };

  /**
   * 中断時に未処理だったアクテビティリストをデータストアに書き込む
   * 
   * @throws Exception
   */
  private void putInterruptedList(List<PlusActivity> unprocessedActivities) throws Exception {
    int numOfUnprocessedActivities = unprocessedActivities.size();
    if (numOfUnprocessedActivities > 0) {
      logger.info("Stack unprocessed Activities:" + String.valueOf(numOfUnprocessedActivities));
    } else {
      logger.info("Loaded all Activities from G+ API.");
    }
    storeHandler.putInterrupted(unprocessedActivities);

  }

  /**
   * 処理対象のアクテビティをG+APIから読み込む
   * 
   * @return
   * @throws Exception
   */
  private List<PlusActivity> getNewActivitiesFromAPI(ActivitiesProcesser activitiesProcesser)
      throws Exception {
    List<PlusActivity> newActivites = apiService.getPlusActivies(storeHandler.getActorId());// アクテビティは全件読み込む
    newActivites = activitiesProcesser.removeNoImageActivity(newActivites);
    return newActivites;

  }

}
