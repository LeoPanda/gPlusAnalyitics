package jp.leopanda.gPlusAnalytics.server;

import java.util.logging.Logger;

/**
 * 実行結果カウンター付きロガー
 * 
 * @author LeoPanda
 *
 */
public class LoggerWidhConter {
  private int numOfNewActivities = 0;
  private int addedActivities = 0;
  private int updatedActivities = 0;
  private int updatedPlusOners = 0;
  private int addedPlusOners = 0;
  private int deletedActivities = 0;
  private int deletedPlusOners = 0;
  private Logger logger;

  /**
   * @param name
   * @param resourceBundleName
   */
  protected LoggerWidhConter(String name) {
    logger = Logger.getLogger(name);
  }

  public void setnumOfNewActivities(int numOfNewActivities) {
    this.numOfNewActivities = numOfNewActivities;
  }
  public void activitiesAdded() {
    addedActivities++;
  }

  public void activitiesUpdated() {
    updatedActivities++;
    addedActivities--;
  }
  public void activitiesDeleted() {
    deletedActivities++;
  }

  public void plusOnerAdded() {
    addedPlusOners++;
  }

  public void setnumOfPlusOnerUpdated(int updatedPlusOners) {
    this.updatedPlusOners = updatedPlusOners;
  }

  public void plusOnerDeleted() {
    deletedPlusOners++;
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
   * 結果ログの書き込み
   */
  public void writeLog() {
    logger.info("更新対象アクテビティ数:" + String.valueOf(numOfNewActivities));
    logger.info("追加アクテビティ数:" + String.valueOf(addedActivities));
    logger.info("更新アクテビティ数:" + String.valueOf(updatedActivities));
    logger.info("削除アクテビティ数:" + String.valueOf(deletedActivities));
    logger.info("追加ユーザー数:" + String.valueOf(addedPlusOners));
    logger.info("更新ユーザー数:" + String.valueOf(updatedPlusOners));
    logger.info("削除ユーザー数:" + String.valueOf(deletedPlusOners));
  }
}
