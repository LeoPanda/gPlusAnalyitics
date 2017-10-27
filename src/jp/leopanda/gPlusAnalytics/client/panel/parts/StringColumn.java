package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.function.Function;

import com.google.gwt.user.cellview.client.TextColumn;

/**
 * 文字列を表示するテーブルカラム
 * @author LeoPanda
 *
 */
public class StringColumn<T> extends TextColumn<T> {

  private Function<T, String> itemSetter;
  /**
   *コンストラクタ 
   */
  public StringColumn(Function<T, String> itemSetter) {
    this.itemSetter = itemSetter;
  }
  
  /* 
   * 表示するカラムの値をセットする
   */
  @Override
  public String getValue(T object) {
    return itemSetter.apply(object);
  }

}
