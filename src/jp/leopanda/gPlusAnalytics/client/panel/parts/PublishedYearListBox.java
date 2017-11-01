package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.util.AgregatedValueList;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * アクテビティの投稿年を表示するリストボックスフィールド
 * 
 * @author LeoPanda
 *
 */
public class PublishedYearListBox extends AgregatedFieldListBox<PlusActivity> {

  /**
   * コンストラクタ
   */
  public PublishedYearListBox(String label, List<PlusActivity> sourceItems) {
    super(label, sourceItems);
  }

  /**
   * リストボックスの要素リストを作成する
   * 
   * @param sourceItems
   * @return
   */
  protected AgregatedElements<String>[] getList(
      List<PlusActivity> sourceItems) {

    AgregatedElements<String>[] valueList = new AgregatedValueList<PlusActivity>(sourceItems)
        .getElements(item -> Formatter.getYear(item.published), field -> field + "年");

    return valueList;
  }
}
