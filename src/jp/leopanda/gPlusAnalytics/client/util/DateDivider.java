package jp.leopanda.gPlusAnalytics.client.util;

import java.util.Date;

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
  public DateDivider(Date currentDate){
    this.currentDate = currentDate;
  }
  
  /**
   * カレントの基準日付を提供する
   * @return
   */
  public Date getCurrentDate(){
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
    } else if (new BreakChecker(newDate, currentDate) {
      @Override
      String dateFormatter(Date date) {
        return Formatter.getYear(date);
      }
    }.isBreak()) {
      breakLevel = setBreakLevel(newDate, DateBreakLevel.YEAR_BREAK);
    } else if (new BreakChecker(newDate, currentDate) {
      @Override
      String dateFormatter(Date date) {
        return Formatter.getMonth(date);
      }
    }.isBreak()) {
      breakLevel = setBreakLevel(newDate, DateBreakLevel.MONTH_BREAK);
    }

    return breakLevel;
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
  
  /**
   * 日付のブレークを検出する抽象クラス
   * 
   * @author LeoPanda
   *
   */
  private abstract class BreakChecker {
    Date compareDate;
    Date targetDate;

    /**
     * コンストラクタ
     * 
     * @param compareDate
     * @param targetDate
     */
    BreakChecker(Date compareDate, Date targetDate) {
      this.compareDate = compareDate;
      this.targetDate = targetDate;
    }

    /**
     * 日付はブレークしているか
     * 
     * @return
     */
    public boolean isBreak() {
      return !dateFormatter(compareDate).equals(dateFormatter(targetDate));
    }

    /**
     * 日付の比較要素を取り出す
     * 
     * @param date
     * @return
     */
    abstract String dateFormatter(Date date);
  }
}
