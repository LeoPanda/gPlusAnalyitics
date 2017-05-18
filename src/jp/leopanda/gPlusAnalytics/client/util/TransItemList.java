package jp.leopanda.gPlusAnalytics.client.util;

import java.util.ArrayList;
import java.util.List;

import jp.leopanda.gPlusAnalytics.dataObject.PlusItem;

/**
 * アイテムリストを別記憶域に移送する
 * 
 * @author LeoPanda
 *
 * @param <I>
 */
public abstract class TransItemList<I extends PlusItem> {
  public List<I> execute(List<I> inputItems) {
    List<I> outputItems = new ArrayList<I>();
    for (I i : inputItems) {
      outputItems.add(i);
    }
    return outputItems;
  }
}