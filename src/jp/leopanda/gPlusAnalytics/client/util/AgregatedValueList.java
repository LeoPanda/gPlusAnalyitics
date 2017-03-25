package jp.leopanda.gPlusAnalytics.client.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.leopanda.gPlusAnalytics.client.panel.AgregatedElement;

/**
 * アイテムリストの特定フィールドの値を集約した選択リストを作成する
 * 
 * @author LeoPanda
 *
 */
public abstract class AgregatedValueList<I, F> {

  AgregatedElement<F>[] elements;

  /**
   * コンストラクタ
   * 
   * @param sourceItems
   */
  @SuppressWarnings("unchecked")
  public AgregatedValueList(List<I> sourceItems) {
    List<AgregatedElement<F>> listElements = new ArrayList<AgregatedElement<F>>();
    listElements.add(new AgregatedElement<F>("", null));// リストの先頭に空白値を置く
    for (F field : getAgregateSet(sourceItems)) {
      listElements.add(new AgregatedElement<F>(setFieldName(field), field));
    }
    elements = (AgregatedElement<F>[]) new AgregatedElement<?>[listElements.size()];
    for (int i = 0; i < elements.length; i++) {
      elements[i] = listElements.get(i);
    }
  }

  /**
   * リスト項目を取得する
   * 
   * @return
   */
  public AgregatedElement<F>[] getElements() {
    return elements;
  }

  /**
   * グループ値を抽出する
   * 
   * @param sourceItems
   * @return
   */
  private Set<F> getAgregateSet(List<I> sourceItems) {
    Set<F> agregateSet = new HashSet<F>();
    for (I item : sourceItems) {
      agregateSet.add(setAgregateValue(item));
    }
    return agregateSet;
  }

  /**
   * 集約する値を指定する
   * 
   * @param item
   * @return
   */
  public abstract F setAgregateValue(I item);

  /**
   * 集約した値のネーミング方法を指定する
   * 
   * @param field
   *          集約値を保持するソースアイテム上のフィールド
   * @return
   */
  public abstract String setFieldName(F field);

}