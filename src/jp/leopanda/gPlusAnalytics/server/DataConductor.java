package jp.leopanda.gPlusAnalytics.server;

import java.io.IOException;
import java.util.List;

import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.DeadlineExceededException;

import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.gPlusAnalytics.dataObject.PlusPeople;
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
  boolean isCronBatch = false;

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
   * @param isCronBatch　処理をcronバッチから呼び出すならTrueを指定する
   * @return
   * @throws Exception
   */
  public String updateDataStore(boolean isCronBatch) throws Exception {
    this.isCronBatch = isCronBatch;
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
    exitingLogProcess();
    return "";
  }

  /**
   * ソースアイテムを最新状態に更新する
   * 
   * @return
   * @throws Exception
   */
  private SourceItems updateSouceItems(SourceItems sourceItems) throws Exception {
    ActivitiesProcesser activitiesProcesser = new ActivitiesProcesser(apiService, logger);
    SummrizerByPlusOners plusOneSummrizer = new SummrizerByPlusOners();
    newActivities = setNewActivities(sourceItems, activitiesProcesser);
    sourceItems.activities = updateActivities(newActivities, sourceItems.activities,
        activitiesProcesser, plusOneSummrizer);
    sourceItems.plusOners = updatePlusOners(activitiesProcesser.getPlusOnersForUpdate(),
        sourceItems.plusOners, plusOneSummrizer);
    // 未処理アクテビティリストのクリア
    putUnprocessedActivities(newActivities);
    return sourceItems;
  }

  /**
   * 処理対象の入力アクテビティを設定する
   * @param sourceItems
   * @param activitiesProcesser
   * @throws HostGateException
   * @throws Exception
   */
  private List<PlusActivity> setNewActivities(SourceItems sourceItems, ActivitiesProcesser activitiesProcesser)
      throws HostGateException, Exception {
    List<PlusActivity> interruptedActivities = storeHandler.getInterruptedActivities();
    if (interruptedActivities.size() > 0) {//処理途中に中断されたアクテビティが残っている場合はこれを処理
      newActivities = interruptedActivities;
    } else {//中断アクテビティがない場合はG+APIから全アクテビティを読み込む
      newActivities = activitiesProcesser.getNewActivitiesFromAPI(storeHandler);
      sourceItems.activities = activitiesProcesser.removeDisusedActivitiesFromSource(newActivities,
          sourceItems.activities);
    }
    return newActivities;
  }

  /**
   * ソースアクテビティリストを更新する
   * 
   * @param newActivities　最新アクテビティリスト
   * @param sourceActivities　ソースアクテビティリスト
   * @param activitiesProcesser
   * @param plusOneCounter
   * @return
   * @throws Exception
   */
  private List<PlusActivity> updateActivities(List<PlusActivity> newActivities,
      List<PlusActivity> sourceActivities, ActivitiesProcesser activitiesProcesser,
      SummrizerByPlusOners plusOneSummrizer) throws Exception {
    sourceActivities = activitiesProcesser.updatePlusActivitiesAndStackPlusOners(
        newActivities, sourceActivities);
    sourceActivities = activitiesProcesser.setStatisticsInfo(
        plusOneSummrizer.aggregatePlusOneCount(sourceActivities), sourceActivities);
    return sourceActivities;
  }

  /**
   * ソース+1erリストを更新する
   * 
   * @param plusOnersForUpdate
   * @param sourcePlusOners
   * @param plusOneSummrizer
   * @return
   * @throws Exception
   */
  private List<PlusPeople> updatePlusOners(List<PlusPeople> plusOnersForUpdate,
      List<PlusPeople> sourcePlusOners, SummrizerByPlusOners plusOneSummrizer) throws Exception {
    PlusOnersProceccer plusOnerProcesser = new PlusOnersProceccer(logger);
    sourcePlusOners = plusOnerProcesser.addNewPlusOners(plusOnersForUpdate, sourcePlusOners);
    sourcePlusOners = plusOnerProcesser.updatePlusOners(plusOneSummrizer, sourcePlusOners);
    return sourcePlusOners;
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
      return;
    }
    if (newActivities.size() == 0) {
      logger.info("Exceeded process started but newActivities is 0.");
      return;
    }
    storeHandler.putItems(sourceItems);
    putUnprocessedActivities(newActivities);
    exitingLogProcess();
    if (isCronBatch) {
      enqueueNewTask();
    } else {
      startNewThread();
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
          new DataConductor(storeHandler, apiService).updateDataStore(isCronBatch);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();
  };

  /**
   * タスクキューを作成して継続ジョブを実行する
   */
  private void enqueueNewTask() {
    Queue queue = QueueFactory.getDefaultQueue();
    queue.add(TaskOptions.Builder.withUrl("/analytics"));
  }

  /**
   * 中断時に未処理だったアクテビティリストをデータストアに書き込む
   * 
   * @throws Exception
   */
  private void putUnprocessedActivities(List<PlusActivity> unprocessedActivities) throws Exception {
    int numOfUnprocessedActivities = unprocessedActivities.size();
    if (numOfUnprocessedActivities > 0) {
      logger.info("Stack unprocessed Activities:" + String.valueOf(numOfUnprocessedActivities));
    } else {
      logger.info("Loaded all Activities from G+ API.");
    }
    storeHandler.putInterrupted(unprocessedActivities);

  }

  /**
   * ログの終結処理
   * 
   * @throws Exception
   */
  private void exitingLogProcess() throws Exception {
    logger.writeLog();
    if (isCronBatch) {
      putDailyStats();
    }
  }

  /**
   * ログ統計情報をデータストアに書き込む
   * 
   * @throws Exception
   */
  public void putDailyStats() throws Exception {
    List<DailyStats> dailyStats = storeHandler.getDailyStats();
    dailyStats.add(logger.getDailyStatsItem());
    storeHandler.putDailyStats(dailyStats);
  }

}
