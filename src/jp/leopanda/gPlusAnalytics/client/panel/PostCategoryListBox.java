package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.enums.MyStyle;
import jp.leopanda.gPlusAnalytics.client.util.AgregatedValueList;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;
import jp.leopanda.panelFrame.filedParts.ListBoxField;

/**
 * アクテビティの投稿先を表示するリストボックスフィールド
 * 
 * @author LeoPanda
 *
 */
public class PostCategoryListBox {

  AgregatedElement<String>[] elementList;
  ListBoxField listBox;

  /**
   * コンストラクタ 
   */
  public PostCategoryListBox(String label,List<PlusActivity> sourceItems) {
    elementList = getList(sourceItems);
    listBox = new ListBoxField(label, null, elementList);
    listBox.addStyleName(MyStyle.FILTER_LABEL.getStyle());
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
        return Formatter.getBeforeBracket(item.getAccessDescription());
      }

      @Override
      public String setFieldName(String field) {
        return field;
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
