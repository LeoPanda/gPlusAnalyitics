package jp.leopanda.gPlusAnalytics.client.enums;

import jp.leopanda.panelFrame.filedParts.ListElement;

/**
 * フィルター論理結合子
 * @author 
 *
 */
public enum BOOLEANS implements ListElement{
  AND("&"),OR("+");
  private String simbol;
  /**
   * コンストラクタ
   */
  private BOOLEANS(String simbol) {
    this.simbol = simbol;
  }
  /* 
   * フィールドボックス作成用
   */
  @Override
  public String getName() {
    return this.simbol;
  }

}