package jp.leopanda.gPlusAnalytics.client.util;

import java.util.Date;

import jp.leopanda.gPlusAnalytics.client.enums.DateFormat;

import com.google.gwt.i18n.shared.DateTimeFormat;

/**
 * 項目を指定されたフォーマット文字列にして取り出す
 * 
 * @author LeoPanda
 *
 */
public class Formatter {

  /**
   * 日付項目から年月日のみを取り出す
   * 
   * @param date
   *          入力の日付項目
   * @return
   */
  public static String getYYMDString(Date date) {
    return DateTimeFormat.getFormat(DateFormat.YYMD.getValue()).format(date);
  }

  /**
   * 日付項目から年月のみを取り出す
   * @param date
   * @return
   */
  public static String getYYMString(Date date) {
    return DateTimeFormat.getFormat(DateFormat.YYM.getValue()).format(date);
  }

  /**
   * 日付項目から月のみを取り出す
   * 
   * @param date
   * @return
   */
  public static String getMonth(Date date) {
    return DateTimeFormat.getFormat(DateFormat.MONTH.getValue()).format(date);
  }

  /**
   * 日付項目から年のみを取り出す
   * 
   * @param date
   * @return
   */
  public static String getYear(Date date) {
    return DateTimeFormat.getFormat(DateFormat.YEAR.getValue()).format(date);
  }

  /**
   * ”（”より前の文字列を取り出す
   * 
   * @param source
   * @return
   */
  public static String getBeforeBracket(String source) {
    String bracket = "(";
    if (!source.contains(bracket)) {
      return source;
    }
    return source.substring(0, source.indexOf(bracket) - 1);
  }
}
