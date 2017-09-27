package jp.leopanda.gPlusAnalytics.server;

import java.util.logging.Logger;

/**
 * 実行結果カウンター付きロガー
 * 
 * @author LeoPanda
 *
 */
public class LoggerWidhConter {
  private StatNumbers counter = new StatNumbers();
  private Logger logger;

  /**
   * @param name
   * @param resourceBundleName
   */
  protected LoggerWidhConter(String name) {
    logger = Logger.getLogger(name);
  }

  public void setnumOfNewActivities(int numOfNewActivities) {
    counter.numOfNewActivities = numOfNewActivities;
  }

  public void activitiesAdded() {
    counter.addedActivities++;
  }

  public void activitiesUpdated() {
    counter.updatedActivities++;
    counter.addedActivities--;
  }

  public void activitiesDeleted(int count) {
    counter.deletedActivities = count;
  }

  public void plusOnerAdded() {
    counter.addedPlusOners++;
  }

  public void countUpPlusOne(int numOfPlusOne) {
    counter.totalPlusOne += numOfPlusOne;
  }

  public void countUpPlusOners(int numOfPlusOners) {
    counter.totalPlusOners += numOfPlusOners;
  }

  public void setnumOfPlusOnerUpdated(int updatedPlusOners) {
    counter.updatedPlusOners = updatedPlusOners;
  }

  public void plusOnerDeleted() {
    counter.deletedPlusOners++;
  }

  public void info(String msg) {
    this.logger.info(msg);
  }

  public void warning(String msg) {
    this.logger.warning(msg);
  }

  public void severe(String msg) {
    this.logger.severe(msg);
  }

  /**
   * 保存用統計情報を取得する
   * @return
   */
  public DailyStats getDailyStatsItem(){
    return new DailyStats(ServerUtil.getCurrentDate(),this.counter);
  }
  
  /**
   * 結果ログの書き込み
   */
  public void writeLog() {
    logger.info("更新対象アクテビティ数:" + String.valueOf(counter.numOfNewActivities));
    logger.info("追加アクテビティ数:" + String.valueOf(counter.addedActivities));
    logger.info("更新アクテビティ数:" + String.valueOf(counter.updatedActivities));
    logger.info("削除アクテビティ数:" + String.valueOf(counter.deletedActivities));
    logger.info("追加ユーザー数:" + String.valueOf(counter.addedPlusOners));
    logger.info("更新ユーザー数:" + String.valueOf(counter.updatedPlusOners));
    logger.info("削除ユーザー数:" + String.valueOf(counter.deletedPlusOners));
    logger.info("総＋１数:" + String.valueOf(counter.totalPlusOne));
  }
}
