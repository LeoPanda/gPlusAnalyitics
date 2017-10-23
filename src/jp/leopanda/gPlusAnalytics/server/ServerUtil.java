package jp.leopanda.gPlusAnalytics.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import jp.leopanda.gPlusAnalytics.client.enums.DateFormat;

/**
 * サーバーサイド共通ユーティリティ
 * @author LeoPanda
 *
 */
public class ServerUtil {
  /**
   * 当日日付の取得
   * 
   * @return
   */
  public static String getCurrentDate(DateFormat format) {
    final String timeZone = "Asia/Tokyo";
    TimeZone timezone = TimeZone.getTimeZone(timeZone);
    SimpleDateFormat formatter = new SimpleDateFormat(format.getValue());
    formatter.setTimeZone(timezone);
    return formatter.format(new Date());
  }
  
 
}
