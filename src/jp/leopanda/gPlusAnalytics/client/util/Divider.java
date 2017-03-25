package jp.leopanda.gPlusAnalytics.client.util;

import com.google.gwt.user.client.ui.Label;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;

/**
 * 集合データグルーピング補助クラス
 */
public class Divider {
  private String groupBy = null;
 
  /**
   * コンストラクタ
   */
  public Divider() {
  }

  /**
   * ラベル用ブレークチェック
   * groupBy値を検査し,前回値と変わっていればtrueを返す
   * 
   * @param groupBy
   * @return
   */
  public boolean checkLabel(String groupBy) {
    if (this.groupBy.equals(groupBy)) {
      return false;
    }
    this.groupBy = groupBy;
    return true;
  }

  /**
   * 値集約用ブレークチェック
   * @param groupBy
   * @return
   */
  public boolean checkAgregate(String groupBy){
    if(this.groupBy == null){
      this.groupBy = groupBy; 
    }
    if(this.groupBy.equals(groupBy)){
      return true;
    } 
      return false;
  }
  /**
   * 分離ラベルを生成する
   * 
   * @param groupBy
   * @return
   */
  public Label getLabel(String width) {
    Label label = new Label(groupBy);
    label.addStyleName(MyStyle.DIVIDER_LABEL.getStyle());
    label.setWidth(width);
    return label;
  }

  /**
   * groupBy値を得る
   */
  public String getGroupBy() {
    return groupBy;
  }

  /**
   * groupBy値をリセットする
   */
  public void reset() {
    this.groupBy = null;
  }
}