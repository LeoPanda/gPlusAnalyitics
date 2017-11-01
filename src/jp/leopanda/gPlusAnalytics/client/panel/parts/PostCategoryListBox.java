package jp.leopanda.gPlusAnalytics.client.panel.parts;

import java.util.List;

import jp.leopanda.gPlusAnalytics.client.util.AgregatedValueList;
import jp.leopanda.gPlusAnalytics.client.util.Formatter;
import jp.leopanda.gPlusAnalytics.dataObject.PlusActivity;

/**
 * アクテビティの投稿先を表示するリストボックスフィールド
 * 
 * @author LeoPanda
 *
 */
public class PostCategoryListBox extends AgregatedFieldListBox<PlusActivity> {

  /**
   * コンストラクタ
   */
  public PostCategoryListBox(String label, List<PlusActivity> sourceItems) {
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

    AgregatedElements<String>[] valueList =
        new AgregatedValueList<PlusActivity>(sourceItems).getElements(
            item -> Formatter.getBeforeBracket(item.getAccessDescription()), field -> field);

    return valueList;
  }
}