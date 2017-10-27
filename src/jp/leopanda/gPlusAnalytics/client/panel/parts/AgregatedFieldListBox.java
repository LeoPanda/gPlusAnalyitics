package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;
import jp.leopanda.panelFrame.filedParts.EventAction;
import jp.leopanda.panelFrame.filedParts.ListBoxField;

/**
 * 値リストの重複値を集約したリストボックスを提供する
 * 
 * @author LeoPanda
 *
 */
public abstract class AgregatedFieldListBox<I extends PlusItem> {

  AgregatedElements<String>[] elementList;
  ListBoxField listBox;

  /**
   * コンストラクタ
   */
  public AgregatedFieldListBox(String label, List<I> sourceItems) {
    elementList = getList(sourceItems);
    listBox = new ListBoxField(label, null, elementList);
    listBox.addLabelStyle(MyStyle.FILTER_LABEL.getStyle());
  }

  /**
   * リストボックスの要素リストを作成する
   * 
   * @param sourceItems
   * @return
   */
  protected abstract AgregatedElements<String>[] getList(List<I> sourceItems);

  /**
   * リストボックスイベントアクションの追加
   * 
   * @param eventAction
   */
  public void addEventListener(EventAction eventAction) {
    listBox.addEventListener(eventAction);
  }

  /**
   * リストボックスフィールドを提供する
   * 
   * @return
   */
  public ListBoxField getField() {
    return this.listBox;
  }

  /**
   * 選択された値を提供する
   * 
   * @return
   */
  public String getValue() {
    return (String) elementList[listBox.getSelectedIndex()].getValue();
  }

  /**
   * リストボックスの選択値をクリアする
   */
  public void reset() {
    listBox.reset();
  }
}
