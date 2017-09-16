package jp.leopanda.gPlusAnalytics.server;

/**
 * 日時バッチ更新時の統計情報保存用クラス
 * @author LeoPanda
 *
 */
public class DailyStats {
  public String processDate;
  public StatNumbers statNumbers;
  public DailyStats(String processDate,StatNumbers statNumbers){
    this.processDate = processDate;
    this.statNumbers = statNumbers;
  }
}
