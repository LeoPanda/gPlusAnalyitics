package jp.leopanda.gPlusAnalytics.server;

import java.util.logging.Logger;

/**
 * 実行結果カウンター付きロガー
 * @author LeoPanda
 *
 */
public class LoggerWidhConter {
  int addedActivities = 0;
  int updatedActivities = 0;
  int updatedPlusOners = 0;
  int addedPlusOners = 0;
  int deletedPlusOners = 0;
  Logger logger;
  /**
   * @param name
   * @param resourceBundleName
   */
  protected LoggerWidhConter(String name) {
    logger = Logger.getLogger(name);
  }

  public void activitiesAdded() {
    addedActivities++;
  }

  public void activitiesUpdated() {
    updatedActivities++;
    addedActivities--;
  }

  public void plusOnerAdded() {
    addedPlusOners++;
  }

  public void plusOnerUpdated() {
    updatedPlusOners++;
  }

  public void plusOnerDeleted() {
    deletedPlusOners++;
  }

  /**
   * 結果ログの書き込み
   */
  public void writeLog() {
    logger.info("追加アクテビティ数:" + String.valueOf(addedActivities));
    logger.info("更新アクテビティ数:" + String.valueOf(updatedActivities));
    logger.info("追加ユーザー数:" + String.valueOf(addedPlusOners));
    logger.info("更新ユーザー数:" + String.valueOf(updatedPlusOners));
    logger.info("削除ユーザー数:" + String.valueOf(deletedPlusOners));
  }
}
