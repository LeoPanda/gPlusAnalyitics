package jp.leopanda.gPlusAnalytics.client.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import jp.leopanda.gPlusAnalytics.client.panel.parts.AgregatedElements;

/**
 * アイテムリストの特定フィールドの値を集約した選択リストを作成する
 * 
 * @author LeoPanda
 *
 */
public class AgregatedValueList<I> {

  List<I> sourceItems;

  /**
   * コンストラクタ
   */
  public AgregatedValueList(List<I> sourceItems) {
    this.sourceItems = sourceItems;
  }

  /**
   * 集約されたリスト値を抽出する
   * 
   * @param setFieldName
   * @param setField
   * @return
   */
  @SuppressWarnings("unchecked")
  public AgregatedElements<String>[] getElements(Function<I, String> setField,UnaryOperator<String> setFieldName) {
    List<AgregatedElements<String>> listElements = new ArrayList<AgregatedElements<String>>();
    listElements.add(new AgregatedElements<String>("", null));// リストの先頭に空白値を置く
    sourceItems.stream().map(item -> setField.apply(item)).distinct().forEach(field -> listElements
        .add(new AgregatedElements<String>(setFieldName.apply(field), field)));
    return (AgregatedElements<String>[]) listElements.toArray(new AgregatedElements<?>[listElements.size()]);
  }
}
