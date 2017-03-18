package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.AgregatedValueList;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.panelFrame.filedParts.EventAction;
import jp.leopanda.panelFrame.filedParts.ListBoxField;

/**
 * アクテビティの投稿年を表示するリストボックスフィールド
 * 
 * @author LeoPanda
 *
 */
public class PublishedYearListBox {

  AgregatedElement<String>[] elementList;
  ListBoxField listBox;

  /**
   * コンストラクタ 
   */
  public PublishedYearListBox(String label,List<PlusActivity> sourceItems) {
    elementList = getList(sourceItems);
    listBox = new ListBoxField(label, null, elementList);
    listBox.addLabelStyle(MyStyle.FILTER_LABEL.getStyle());
  }

  /**
   * リストボックスイベントアクションの追加
   * @param eventAction
   */
  public void addEventListener(EventAction eventAction){
    listBox.addEventListener(eventAction);
  }

  /**
   * リストボックスの要素リストを作成する
   * 
   * @param sourceItems
   * @return
   */
  private AgregatedElement<String>[] getList(
      List<PlusActivity> sourceItems) {

    AgregatedElement<String>[] valueList = new AgregatedValueList<PlusActivity, String>(
        sourceItems) {
      @Override
      public String setAgregateValue(PlusActivity item) {
        return Formatter.getYear(item.published);
      }

      @Override
      public String setFieldName(String field) {
        return field + "年";
      }
    }.getElements();

    return valueList;
  }

  /**
   * リストボックスフィールドを提供する
   * @return
   */
  public ListBoxField getField(){
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

}
