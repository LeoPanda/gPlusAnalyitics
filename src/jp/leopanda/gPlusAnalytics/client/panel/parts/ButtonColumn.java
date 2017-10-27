package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.function.BiConsumer;
import java.util.function.Function;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.user.cellview.client.Column;

/**
 * HTMLを表示するテーブルカラム
 * 
 * @author LeoPanda
 *
 */
public class ButtonColumn<I> extends Column<I, String> {
  static final ButtonCell cell = new ButtonCell();
  Function<I, String> itemSetter;

  /**
   * コンストラクタ
   */
  public ButtonColumn(Function<I, String> itemSetter) {
    super(cell);
    this.itemSetter = itemSetter;
  }

  /**
   * クリック時のアクションをセットする
   * 
   * @param clickEventSetter
   */
  public void addClickEvent(BiConsumer<Integer, I> clickEventSetter) {
    setFieldUpdater((index, item, value) -> clickEventSetter.accept(index, item));
  }

  /* 
   * 表示するボタンのテキストをセットする
   */
  @Override
  public String getValue(I object) {
    return itemSetter.apply(object);
  }

}
