package jp.leopanda.gPlusAnalytics.client.panel.abstracts;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.user.cellview.client.Column;

/**
 * HTMLを表示するテーブルカラム
 * 
 * @author LeoPanda
 *
 */
public abstract class ButtonColumn<I> extends Column<I, String> {
  static final ButtonCell cell = new ButtonCell();

  /**
   * コンストラクタ
   */
  public ButtonColumn() {
    super(cell);
    this.setFieldUpdater((index,item,value) -> addClickEvent(index,item));
  }

  /**
   * ボタンクリック時の処理
   * 
   * @param index クリック時のインデックス
   * @param item　クリック対象のアイテムオブジェクト
   */
  public abstract void addClickEvent(int index, I item);

}
