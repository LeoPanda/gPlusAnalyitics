package jp.leopanda.gPlusAnalytics.client;

import java.util.Date;

import jp.leopanda.gPlusAnalytics.client.enums.DateFormat;

import com.google.gwt.i18n.shared.DateTimeFormat;

/**
 * 項目を指定されたフォーマット文字列にして取り出す
 * @author LeoPanda
 *
 */
public class Formatter {

  /**
   * 日付項目から年月日のみを取り出す
   * @param date 入力の日付項目
   * @return
   */
  public String getYYMMDDString(Date date){
    return DateTimeFormat.getFormat(DateFormat.YYMMDD.getValue()).format(date);
  }
  
}
