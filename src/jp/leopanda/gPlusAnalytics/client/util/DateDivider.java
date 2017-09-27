package jp.leopanda.gPlusAnalytics.client.util;

import java.util.Date;
import java.util.function.Function;

import com.google.gwt.user.client.ui.Label;

import jp.leopanda.gPlusAnalytics.client.enums.DateBreakLevel;

/**
 * 日付のブレークレベルを検出し、分離ラベルを提供する
 * 
 * @author LeoPanda
 *
 */
public class DateDivider {

  private Date currentDate;

  /**
   * コンストラクタ
   */
  public DateDivider() {
    this.currentDate = null;
  }

  public DateDivider(Date currentDate) {
    this.currentDate = currentDate;
  }

  /**
   * カレントの基準日付を提供する
   * 
   * @return
   */
  public Date getCurrentDate() {
    return this.currentDate;
  }

  /**
   * 日付のブレークレベルをチェックする
   * 
   * @param newDate
   * @return
   */
  public DateBreakLevel getBreakLevel(Date newDate) {
    DateBreakLevel breakLevel = DateBreakLevel.NONE;
    if (currentDate == null) {
      breakLevel = setBreakLevel(newDate, DateBreakLevel.YEAR_BREAK);
    } else if (isYearBreak(newDate)) {
      breakLevel = setBreakLevel(newDate, DateBreakLevel.YEAR_BREAK);
    } else if (isMonthBreak(newDate)) {
      breakLevel = setBreakLevel(newDate, DateBreakLevel.MONTH_BREAK);
    }

    return breakLevel;
  }

  /**
   * 年レベルのブレークをチェックする
   * 
   * @param newDate
   * @return
   */
  private boolean isYearBreak(Date newDate) {
    return !equalToFormattedCurrentDate(newDate, date -> Formatter.getYear(date));
  }

  /**
   * 月レベルのブレークをチェックする
   * 
   * @param newDate
   * @return
   */
  private boolean isMonthBreak(Date newDate) {
    return !equalToFormattedCurrentDate(newDate, date -> Formatter.getMonth(date));
  }

  /**
   * 指定されたフォーマッタで取り出した文字列がカレント日付と同一かをチェックする
   * 
   * @param newDate
   * @param formatter
   * @return
   */
  private boolean equalToFormattedCurrentDate(Date newDate, Function<Date, String> formatter) {
    return formatter.apply(newDate).equals(formatter.apply(currentDate));
  }

  /**
   * ブレークレベルのセット
   * 
   * @param newDate
   * @param breakLevel
   * @return
   */
  private DateBreakLevel setBreakLevel(Date newDate, DateBreakLevel breakLevel) {
    currentDate = newDate;
    return breakLevel;
  }

  /**
   * 年区切りラベルを提供する
   * 
   * @return
   */
  public Label getYearLabel() {
    return new Label(Formatter.getYear(currentDate) + "年");
  }

  /**
   * 月区切りラベルを提供する
   * 
   * @return
   */
  public Label getMonthLabel() {
    return new Label(Formatter.getMonth(currentDate) + "月");
  }

  /**
   * 初期化する
   */
  public void clear() {
    this.currentDate = null;
  }

}
