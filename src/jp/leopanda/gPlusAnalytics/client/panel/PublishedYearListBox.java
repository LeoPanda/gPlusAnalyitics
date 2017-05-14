package jp.leopanda.gPlusAnalytics.client.panel;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.panel.abstracts.AgregatedFieldListBox;
import jp.leopanda.gPlusAnalytics.client.util.AgregatedValueList;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * アクテビティの投稿年を表示するリストボックスフィールド
 * 
 * @author LeoPanda
 *
 */
public class PublishedYearListBox extends AgregatedFieldListBox<PlusActivity>{

  /**
   * コンストラクタ 
   */
  public PublishedYearListBox(String label,List<PlusActivity> sourceItems) {
    super(label, sourceItems);
  }


  /**
   * リストボックスの要素リストを作成する
   * 
   * @param sourceItems
   * @return
   */
  protected AgregatedElement<String>[] getList(
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
}
